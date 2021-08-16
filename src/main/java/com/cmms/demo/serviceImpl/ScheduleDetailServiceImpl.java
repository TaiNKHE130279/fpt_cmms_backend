package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.reponsitory.BookingScheduleRepository;
import com.cmms.demo.reponsitory.DriverRepository;
import com.cmms.demo.reponsitory.ProjectRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.ScheduleDetailService;
import com.cmms.demo.specification.ListConfirmSpecs;
import com.cmms.demo.specification.ListHistoryTaskOfDriverSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleDetailServiceImpl implements ScheduleDetailService {
    private Path root = Paths.get("images");

    @Autowired
    private ScheduleDetailRepository repository;
    @Autowired
    private WorkingConfirmStatusServiceImpl statusService;
    @Autowired
    private BookingScheduleServiceImpl bookingService;
    @Autowired
    private DriverServiceImpl driverServiceImpl;
    @Autowired
    private MachineServiceImpl machineServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @Autowired
    private BookingScheduleRepository bookingRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectStatusService projectStatusService;
    @Autowired
    private DriverRepository driverRepository;

    public BookingScheduleDetail convertFromDto(String driverCode, String machineCode, Date date
            , String projectCode, String beginTime, String finishTime) {
        BookingScheduleDetail d = new BookingScheduleDetail();
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            if(beginTime != null && finishTime != null) {
                d.setBegin_time(new Time(format.parse(beginTime).getTime()));
                d.setFinish_time(new Time(format.parse(finishTime).getTime()));
            }
            BookingSchedule b = bookingService.getOne(projectCode, date.toString());
            if (b == null) {
                b = new BookingSchedule();
                b.setProject(projectServiceImpl.getOne(projectCode));
                b.setDate(date);
                b.setStatus(statusService.getOne(4L));
                bookingRepository.save(b);
            }
            d.setBookingSchedule(b);
            if (driverCode != null) {
                DriverPOJO driver = driverServiceImpl.getOne(driverCode);
                d.setDriver_code(driver.getDrive_code());
                if (machineCode == null) {
                    d.setMachine_code(driver.getMachine().getMachine_code());
                } else {
                    d.setMachine_code(machineCode);
                }
            } else {
                d.setDriver_code(null);
                d.setMachine_code(null);
            }
            WorkingConfirmStatus status = statusService.getOne(Long.valueOf(1));
            d.setStatus(status);
            d.setHas_temporary(false);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BookingScheduleDetail> assignDriverOnOneDate(List<ListDriverDTO> listDriver
            , Date date, String projectCode, String beginTime, String finishTime) {
        List<BookingScheduleDetail> lstOutput = new ArrayList<>();
        if(listDriver == null){
            BookingSchedule b = bookingService.getOne(projectCode, date.toString());
            if (b == null) {
                b = new BookingSchedule();
                b.setProject(projectServiceImpl.getOne(projectCode));
                b.setDate(date);
                b.setStatus(statusService.getOne(4L));
                bookingRepository.save(b);
            }
        }else {
            for (int i = 0; i < listDriver.size(); i++) {
                ListDriverDTO item = listDriver.get(i);
                lstOutput.add(convertFromDto(item.getDriver_code(), null,
                        date, projectCode, beginTime, finishTime));
            }
        }
        return lstOutput;
    }

    public List<BookingScheduleDetail> assignDriverOnOneBookingForm(RequestBodyToAssignDriverDTO dto) {
        List<BookingScheduleDetail> lstOutput = new ArrayList<>();
        try {
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getBegin_date());
            java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getEnd_date());
            LocalDate startDate = new Date(fromDate.getTime()).toLocalDate();
            LocalDate endDate = new Date(toDate.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                String beginTime = "";
                String finishTime = "";
                if (date.isEqual(startDate)) {
                    beginTime = dto.getBegin_time();
                    String[] str = beginTime.split(":");
                    if (startDate.isEqual(endDate.minusDays(1))) {
                        finishTime = dto.getFinish_time();
                    } else {
                        if (Integer.parseInt(str[0]) >= 17) {
                            continue;
                        }
                        finishTime = "17:00:00";
                    }
                } else if (date.isEqual(endDate.minusDays(1))) {
                    beginTime = "8:00:00";
                    finishTime = dto.getFinish_time();
                } else {
                    beginTime = "8:00:00";
                    finishTime = "17:00:00";
                }
                if(dto.getListDriver() == null || dto.getListDriver().size() == 0){
                    lstOutput.addAll(assignDriverOnOneDate(null, Date.valueOf(date),
                            dto.getProject_code(), beginTime, finishTime));
                }else{
                    lstOutput.addAll(assignDriverOnOneDate(dto.getListDriver(), Date.valueOf(date),
                            dto.getProject_code(), beginTime, finishTime));
                }

            }
            return lstOutput;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<BookingScheduleDetail> assignDriver(List<RequestBodyToAssignDriverDTO> lsDto) {
        List<BookingScheduleDetail> lstOutput = new ArrayList<>();
        for (int i = 0; i < lsDto.size(); i++) {
            RequestBodyToAssignDriverDTO dto = lsDto.get(i);
            lstOutput.addAll(assignDriverOnOneBookingForm(dto));
        }
        return repository.saveAll(lstOutput);
    }

    @Override
    public BookingScheduleDetail getOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<BookingScheduleDetail> update(List<ParamToUpdateScheduleDetailDTO> lsDto) {
        List<BookingScheduleDetail> lsOutput = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("HH:mm");
        BookingSchedule b = bookingService.getOne(lsDto.get(0).getProject_code(), lsDto.get(0).getDate());
        for (int i = 0; i < lsDto.size(); i++) {
            ParamToUpdateScheduleDetailDTO param = lsDto.get(i);
            if (param.getSchedule_detail_id() != 0) {
                BookingScheduleDetail bd = getOne(param.getSchedule_detail_id());
                if (bd != null) {
                    if (param.getBegin_time() != null) {
                        try {
                            bd.setBegin_time(new Time(format.parse(param.getBegin_time()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (param.getFinish_time() != null) {
                        try {
                            bd.setFinish_time(new Time(format.parse(param.getFinish_time()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (param.getDriver_code() != null) {
                        DriverPOJO d = driverServiceImpl.getOne(param.getDriver_code());
                        bd.setDriver_code(d.getDrive_code());
                        bd.setMachine_code(d.getMachine().getMachine_code());
                    }
                    lsOutput.add(bd);
                }
            } else {
                BookingScheduleDetail bd = new BookingScheduleDetail();
                try {
                    bd.setBegin_time(new Time(format.parse(param.getBegin_time()).getTime()));
                    bd.setFinish_time(new Time(format.parse(param.getFinish_time()).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (param.getDriver_code() != null) {
                    DriverPOJO d = driverServiceImpl.getOne(param.getDriver_code());
                    bd.setDriver_code(d.getDrive_code());
                    bd.setMachine_code(d.getMachine().getMachine_code());
                } else {
                    bd.setDriver_code(null);
                    bd.setMachine_code(null);
                }
                WorkingConfirmStatus status = statusService.getOne(Long.valueOf(1));
                bd.setStatus(status);
                bd.setBookingSchedule(b);
                bd.setHas_temporary(false);
                lsOutput.add(bd);
            }
        }
        return repository.saveAll(lsOutput);
    }

    @Override
    public List<DriverDTO> getListDriverNotAssigned(String beginDate
            , String finishDate, String beginTime, String finishTime) {
        List<DriverPOJO> lstDriver = driverServiceImpl.getDriverListAssignMachine();
        List<DriverDTO> lstDriverAssigned = getListDriverAssignedManyDate(beginDate, finishDate, beginTime, finishTime);
        List<DriverDTO> lstDriverNotAssigned = new ArrayList<>();
        if (lstDriverAssigned != null || lstDriverAssigned.size() > 0) {
            for (int i = 0; i < lstDriver.size(); i++) {
                String driverCode = lstDriver.get(i).getDrive_code();
                if (!checkDriverExistedInList(driverCode, lstDriverAssigned)) {
                    DriverPOJO d = driverServiceImpl.getOne(driverCode);
                    if (d != null) {
                        lstDriverNotAssigned.add(DriverDTO.from(d));
                    }
                }
            }
        }
        return lstDriverNotAssigned;
    }

    public boolean checkDriverExistedInList(String driverCode, List<DriverDTO> lstDriverAssigned) {
        if (lstDriverAssigned.size() > 0) {
            for (int i = 0; i < lstDriverAssigned.size(); i++) {
                String code = lstDriverAssigned.get(i).getDrive_code();
                if (code.equals(driverCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<BookingScheduleDetail> getListByBookingId(Long id) {
        return repository.getListByBookingId(id);
    }

    @Override
    public List<DriverDTO> getListDriverAssigned(String projectCode) {
        List<DriverDTO> lsOutput = new ArrayList<>();
        List<BookingScheduleDetail> ls = repository.getListDriverAssigned(projectCode);
        for (int i = 0; i < ls.size(); i++) {
            DriverPOJO d = driverServiceImpl.getOne(ls.get(i).getDriver_code());
            if (!checkDriverIsExisted(d.getDrive_code(), lsOutput)) {
                lsOutput.add(DriverDTO.from(d));
            }
        }
        return lsOutput;
    }

    public boolean checkDriverIsExisted(String code, List<DriverDTO> lsOutput) {
        for (int i = 0; i < lsOutput.size(); i++) {
            if (lsOutput.get(i).getDrive_code().equals(code)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ScheduleDetailByDriverDTO> getScheduleDetailByDriver(String driverCode) {
        List<ScheduleDetailByDriverDTO> lsOutput = new ArrayList<>();
        List<BookingScheduleDetail> ls = repository.getDetailByDriverCode(driverCode);
        for (int i = 0; i < ls.size(); i++) {
            BookingScheduleDetail bs = ls.get(i);
            ScheduleDetailByDriverDTO dto = new ScheduleDetailByDriverDTO();
            dto.setDriver_code(bs.getDriver_code());
            dto.setMachine_code(bs.getMachine_code());
            dto.setBegin_time(bs.getBegin_time().toString());
            dto.setFinish_time(bs.getFinish_time().toString());
            dto.setDate(bs.getBookingSchedule().getDate().toString());
            lsOutput.add(dto);
        }
        return lsOutput;
    }

    public List<DriverDTO> getListDriverAssignedManyDate(String beginDate
            , String finishDate, String beginTime, String finishTime) {
        try {
            List<DriverDTO> lsOutput = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
            java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(finishDate);
            LocalDate startDate = new Date(fromDate.getTime()).toLocalDate();
            LocalDate endDate = new Date(toDate.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                String startTime = "";
                String endTime = "";
                if (date.isEqual(startDate)) {
                    startTime = beginTime;
                    if (startDate.isEqual(endDate.minusDays(1))) {
                        endTime = finishTime;
                    } else {
                        endTime = "17:00:00";
                    }
                } else if (date.isEqual(endDate.minusDays(1))) {
                    startTime = "8:00:00";
                    endTime = finishTime;
                } else {
                    startTime = "8:00:00";
                    endTime = "17:00:00";
                }
                List<DriverDTO> lsDto = getListDriverAssignedOneDate(Date.valueOf(date), startTime, endTime);
                for (int i = 0; i < lsDto.size(); i++) {
                    if (!checkDriverIsExisted(lsDto.get(i).getDrive_code(), lsOutput)) {
                        lsOutput.add(lsDto.get(i));
                    }
                }
            }
            return lsOutput;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DriverDTO> getListDriverAssignedOneDate(Date date, String beginTime, String finishTime) {
        try {
            List<DriverDTO> lsOutput = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            List<BookingScheduleDetail> lsByDate = repository.getListByDate(date);
            if (lsByDate.size() > 0) {
                for (int j = 0; j < lsByDate.size(); j++) {
                    LocalTime startTime = new Time(format.parse(beginTime).getTime()).toLocalTime();
                    LocalTime endTime = new Time(format.parse(finishTime).getTime()).toLocalTime();
                    Long id = lsByDate.get(j).getId();
                    for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
                        BookingScheduleDetail sd = repository.getListDriver(id, Time.valueOf(time));
                        if (sd != null) {
                            DriverPOJO d = driverServiceImpl.getOne(sd.getDriver_code());
                            if (!checkDriverIsExisted(d.getDrive_code(), lsOutput)) {
                                lsOutput.add(DriverDTO.from(d));
                            }
                        }
                        LocalTime t = time.plusHours(1);
                        if (t.getHour() == 0) {
                            return lsOutput;
                        }
                    }
                }
            }
            return lsOutput;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String deleteById(Long id) {
        int delete = repository.deleteByDetailId(id);
        if (delete > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public List<ListTaskOfDriverDTO> getListTaskByDriverAndDate(String driverCode) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        List<ListTaskOfDriverDTO> lsOutput = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Date from = Date.valueOf(currentDate.with(DayOfWeek.MONDAY));
        Date to = Date.valueOf(currentDate.with(DayOfWeek.SUNDAY));
        List<BookingScheduleDetail> ls = repository.getListTaskByDriverAndDate(driverCode, from, to);
        for (int i = 0; i < ls.size(); i++) {
            ListTaskOfDriverDTO dto = new ListTaskOfDriverDTO();
            BookingScheduleDetail bd = ls.get(i);
            dto.setSchedule_detail_id(bd.getId());
            dto.setProject_code(bd.getBookingSchedule().getProject().getProject_code());
            dto.setCustomer(CustomerDTO.from(bd.getBookingSchedule().getProject().getCustomer(), null));
            dto.setBegin_time(format.format(bd.getBegin_time()));
            dto.setFinish_time(format.format(bd.getFinish_time()));
            dto.setMachine_code(bd.getMachine_code());
            dto.setLicense_plate(machineServiceImpl.getOne(bd.getMachine_code()).getLicense_plate());
            dto.setDate(bd.getBookingSchedule().getDate().toString());
            dto.setWorking_status_id(bd.getStatus().getType_id());
            dto.setWorking_status_name(bd.getStatus().getType_name());
            if (bd.getWorked_hours() != null) {
                dto.setWorked_hours(format.format(bd.getWorked_hours()));
            }
            lsOutput.add(dto);
        }
        return lsOutput;
    }



    @Override
    public String updateTimeByDriver(Long scheduleDetailId, String workedHours, MultipartFile image) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        try {
            BookingScheduleDetail bd = getOne(scheduleDetailId);
            if (bd != null) {
                bd.setWorked_hours(new Time(format.parse(workedHours).getTime()));
//                long value = bd.getWorked_hours().getTime() - Time.valueOf("8:00").getTime();
//                if(value > 0){
//                    bd.setOver_time(value);
//                }

                bd.setStatus(statusService.getOne(Long.valueOf(2)));
                if(image != null) {
                    try {
                        String fileName = System.currentTimeMillis()+"_"+image.getOriginalFilename();
                        Files.copy(image.getInputStream(), this.root.resolve(fileName));
                        bd.setEvidence_image(fileName);
                    } catch (Exception e) {
                        throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                    }
                }
                BookingScheduleDetail update = repository.save(bd);
                if (update != null) {
                    return "success";
                }
            }
            return "fail";
        } catch (ParseException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @Override
    public int executeCompleteBtn(String projectCode, String chosenDate) {
        try {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(chosenDate);
            Date maxDate = getMaxDate(projectCode);
            if(maxDate != null) {
                LocalDate startDate = new Date(fromDate.getTime()).toLocalDate();
                startDate = startDate.plusDays(1);
                LocalDate endDate = maxDate.toLocalDate();
                if (!startDate.isEqual(endDate) || startDate.isAfter(endDate)) {
                    List<BookingScheduleDetail> lsRemainDetail = repository.getRemainDetail(projectCode, Date.valueOf(startDate));
                    for (int i = 0; i < lsRemainDetail.size(); i++) {
                        int delete = repository.deleteByDetailId(lsRemainDetail.get(i).getId());
                    }
                    List<BookingSchedule> lsBooking = bookingRepository.getRemainDate(projectCode, Date.valueOf(startDate));
                    for (int j = 0; j < lsBooking.size(); j++) {
                        int delete = bookingRepository.deleteByBookingId(lsBooking.get(j).getId());
                    }
                }
            }
            ProjectPOJO project = projectServiceImpl.getOne(projectCode);
            if (project != null) {
                project.setProjectStatus(projectStatusService.getOne(3));
                projectRepository.save(project);
                return 1;
            }
            return 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Date getMaxDate(String projectCode) {
        return repository.getMaxDate(projectCode);
    }

    @Override
    public int checkDate(String projectCode, String driverCode, String date) {
        try {
            java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            BookingScheduleDetail b = repository.checkDate(driverCode, new Date(fromDate.getTime()), projectCode);
            if (b != null) {
                return 1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public HistoryTaskOfDriverOutput getListNeedConfirm(int pageIndex, int pageSize,
                                                        Long typeId, String driverName, String from, String to) {
        List<ListTaskOfDriverDTO> lsOutput = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        List<DriverPOJO> getDriverByName = driverRepository.getListByName(driverName);
        Page<BookingScheduleDetail> page = repository.findAll(
                Specification.where(ListConfirmSpecs.getListNeedConfirm(typeId, driverName, from, to, driverRepository)), pageable);
        List<BookingScheduleDetail> ls = page.toList();
        for (int i = 0; i < ls.size(); i++) {
            ListTaskOfDriverDTO dto = new ListTaskOfDriverDTO();
            BookingScheduleDetail bd = ls.get(i);
            dto.setSchedule_detail_id(bd.getId());
            dto.setProject_code(bd.getBookingSchedule().getProject().getProject_code());
            dto.setCustomer(CustomerDTO.from(bd.getBookingSchedule().getProject().getCustomer(), null));
            dto.setBegin_time(bd.getBegin_time().toString());
            dto.setFinish_time(bd.getFinish_time().toString());
            dto.setMachine_code(bd.getMachine_code());
            dto.setLicense_plate(machineServiceImpl.getOne(bd.getMachine_code()).getLicense_plate());
            dto.setDate(bd.getBookingSchedule().getDate().toString());
            dto.setWorking_status_id(bd.getStatus().getType_id());
            dto.setWorking_status_name(bd.getStatus().getType_name());
            if (bd.getWorked_hours() != null) {
                dto.setWorked_hours(bd.getWorked_hours().toString());
            }
            dto.setDriver_name(driverServiceImpl.getOne(bd.getDriver_code()).getName());
            dto.setImage(bd.getEvidence_image());
            lsOutput.add(dto);
        }
        HistoryTaskOfDriverOutput output = new HistoryTaskOfDriverOutput();
        output.setTotalPages(page.getTotalPages());
        output.setLs(lsOutput);
        return output;
    }

    @Override
    public int updateWorkingStatus(Long scheduleDetailId, Long typeId) {
        BookingScheduleDetail sd = repository.getById(scheduleDetailId);
        if (sd != null) {
            sd.setStatus(statusService.getOne(Long.valueOf(typeId)));
            repository.save(sd);
            return 1;
        }
        return 0;
    }

    @Override
    public HistoryTaskOfDriverOutput getListHistoryTaskOfDriver(int pageIndex, int pageSize
            , String driverCode, String date, String customerName) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        List<ListTaskOfDriverDTO> lsOutput = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Page<BookingScheduleDetail> page = repository.findAll(Specification.where(ListHistoryTaskOfDriverSpecs
                .getListHistoryTaskOfDriver(driverCode, customerName, date, Date.valueOf(currentDate))), pageable);
        List<BookingScheduleDetail> ls = page.toList();
        for (int i = 0; i < ls.size(); i++) {
            ListTaskOfDriverDTO dto = new ListTaskOfDriverDTO();
            BookingScheduleDetail bd = ls.get(i);
            dto.setSchedule_detail_id(bd.getId());
            dto.setProject_code(bd.getBookingSchedule().getProject().getProject_code());
            dto.setCustomer(CustomerDTO.from(bd.getBookingSchedule().getProject().getCustomer(), null));
            dto.setBegin_time(format.format(bd.getBegin_time()));
            dto.setFinish_time(format.format(bd.getFinish_time()));
            dto.setMachine_code(bd.getMachine_code());
            dto.setLicense_plate(machineServiceImpl.getOne(bd.getMachine_code()).getLicense_plate());
            dto.setDate(bd.getBookingSchedule().getDate().toString());
            dto.setWorking_status_id(bd.getStatus().getType_id());
            dto.setWorking_status_name(bd.getStatus().getType_name());
            if (bd.getWorked_hours() != null) {
                dto.setWorked_hours(format.format(bd.getWorked_hours()));
            }
            lsOutput.add(dto);
        }
        HistoryTaskOfDriverOutput output = new HistoryTaskOfDriverOutput();
        output.setTotalPages(page.getTotalPages());
        output.setLs(lsOutput);
        return output;
    }

    public int deleteScheduleByDayOff(String driverCode, Date from, Date to) {
        LocalDate startDate = new Date(from.getTime()).toLocalDate();
        LocalDate endDate = new Date(to.getTime()).toLocalDate();
        endDate = endDate.plusDays(1);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            List<BookingScheduleDetail> lsByDriverAndDate = repository.getByDriverAndDate(driverCode, Date.valueOf(date));
            if (lsByDriverAndDate.size() > 0) {
                for (int i = 0; i < lsByDriverAndDate.size(); i++) {
                    BookingScheduleDetail sd = lsByDriverAndDate.get(i);
                    if (sd != null) {
                        int delete = repository.deleteByDetailId(sd.getId());
                        List<BookingScheduleDetail> lsDriverByDate = repository.getListByBookingId(sd.getBookingSchedule().getId());
                        if (lsDriverByDate.size() == 0) {
                            int deleteBooking = bookingRepository.deleteByBookingId(sd.getBookingSchedule().getId());
                        }
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public boolean checkDateComplete(Long bookingId) {
        List<BookingScheduleDetail> ls = repository.getListByBookingId(bookingId);
        if (ls != null && ls.size() > 0) {
            for (int i = 0; i < ls.size(); i++) {
                BookingScheduleDetail sd = ls.get(i);
                if (sd.getStatus().getType_id() != 3) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkHasWorkTime(String projectCode){
        List<BookingScheduleDetail> lsDetail = repository.getDetailByProjectCode(projectCode);
        for(int i = 0; i<lsDetail.size(); i++){
            BookingScheduleDetail sd = lsDetail.get(i);
            if(sd.getWorked_hours() != null && sd.getStatus().getType_id() == 3){
                return true;
            }
        }
        return false;
    }

}