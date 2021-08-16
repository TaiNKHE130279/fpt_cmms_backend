package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.reponsitory.BookingScheduleRepository;
import com.cmms.demo.reponsitory.DriverRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.BookingScheduleService;
import com.cmms.demo.specification.FullProjectSpecs;
import com.cmms.demo.specification.ListConfirmSpecs;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingScheduleServiceImpl implements BookingScheduleService {
    @Autowired
    private BookingScheduleRepository repository;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @Autowired
    private HolidayService holidayService;
    @Autowired
    private ScheduleDetailServiceImpl detailService;
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private DriverServiceImpl driverServiceImpl;
    @Autowired
    private ContractServiceImpl contractServiceImpl;
    @Autowired
    private WorkingConfirmStatusServiceImpl statusService;
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private DriverRepository driverRepository;

    public BookingSchedule convertFromDto(String projectCode, Date date) {
        BookingSchedule s = new BookingSchedule();
        ProjectPOJO projectPOJO = projectServiceImpl.getOne(projectCode);
        s.setProject(projectPOJO);
        s.setDate(date);
        return s;
    }

    @Override
    public List<BookingSchedule> addBookingSchedule(String projectCode) {
        ProjectPOJO project = projectServiceImpl.getOne(projectCode);
        List<BookingSchedule> ls = new ArrayList<>();
        LocalDate startDate = project.getExpected_starting_date().toLocalDate();
        LocalDate endDate = project.getExpected_end_date().toLocalDate();
        endDate = endDate.plusDays(1);
        return getBookingSchedules(projectCode, ls, startDate, endDate);
    }

    @Override
    public BookingSchedule getOne(String projectCode, String strDate) {
        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            Date date1 = new Date(date.getTime());
            BookingSchedule b = repository.getOne(projectCode, date1);
            return b;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BookingSchedule> update(String projectCode) {
        ProjectPOJO project = projectServiceImpl.getOne(projectCode);
        List<BookingSchedule> ls = new ArrayList<>();
        List<BookingSchedule> lsByProjectCode = repository.getListByProject(projectCode);
        boolean delete = deleteBookingByProject(lsByProjectCode);
        if (delete) {
            ls = addBookingSchedule(projectCode);
        }
        return repository.saveAll(ls);
    }

    public boolean deleteBookingByProject(List<BookingSchedule> ls) {
        int delete = 0;
        for (int i = 0; i < ls.size(); i++) {
            BookingSchedule b = ls.get(i);
            repository.delete(b);
            delete++;
        }
        return delete == ls.size();
    }

    public Long getTotalOverTimeByDriverCode(String driver) {
        Long result = detailRepository.getTotalOverTimeByDriverCode(driver);
        return result;
    }

    @Override
    public Page<BookingSchedule> getPageByProjectCode(int pageIndex, int pageSize, String projectCode) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return repository.getPageBookingByProject(projectCode, pageable);
    }

    @Override
    public List<DataToFillCalendarDTO> getDataToFillCalendar(String begin, String end) {
        List<DataToFillCalendarDTO> lsOutput = new ArrayList<>();
        try {
            LocalDate currentDate = LocalDate.now();
            java.util.Date from = new SimpleDateFormat("yyyy-MM-dd").parse(begin);
            java.util.Date to = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            LocalDate startDate = new Date(from.getTime()).toLocalDate();
            LocalDate endDate = new Date(to.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                List<BookingSchedule> ls = repository.getListByDate(Date.valueOf(date));
                for (int i = 0; i < ls.size(); i++) {
                    BookingSchedule b = ls.get(i);
                    if (date.isEqual(currentDate) && b.getProject().getProjectStatus().getId() != 3) {
                        b.setStatus(statusService.getOne(5L));
                    }
                    if (detailService.checkDateComplete(b.getId()) && b.getProject().getProjectStatus().getId() == 3) {
                        b.setStatus(statusService.getOne(3L));
                    }
                    repository.save(b);
                }
                if (ls.size() > 0) {
                    DataToFillCalendarDTO dto = DataToFillCalendarDTO.from(Date.valueOf(date), ls, contractServiceImpl);
                    lsOutput.add(dto);
                }
            }
            projectServiceImpl.updateStartAndEndDateForProject();
            return lsOutput;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProjectInfoDTO getProjectInfo(String projectCode, String strDate) {
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            BookingSchedule b = repository.getOne(projectCode, new Date(date.getTime()));
            List<BookingScheduleDetail> lsDetail = detailService.getListByBookingId(b.getId());

            ProjectInfoDTO dto = new ProjectInfoDTO();
            dto.setProject_code(b.getProject().getProject_code());
            dto.setProject_name(b.getProject().getProject_name());
            dto.setCustomer(CustomerDTO.from(b.getProject().getCustomer(), null));
            dto.setProject_status(b.getProject().getProjectStatus().getStatus_name());
            dto.setProject_type(b.getProject().getProjectType().getType_name());
            List<ScheduleDetailDTO> lsDto = new ArrayList<>();
            for (int i = 0; i < lsDetail.size(); i++) {
                BookingScheduleDetail item = lsDetail.get(i);
                ScheduleDetailDTO detailDTO = new ScheduleDetailDTO();
                if (item.getId() != null) {
                    detailDTO.setSchedule_detail_id(item.getId());
                }
                detailDTO.setBegin_time(format.format(item.getBegin_time()));
                detailDTO.setFinish_time(format.format(item.getFinish_time()));
                if (item.getDriver_code() != null) {
                    DriverPOJO d = driverServiceImpl.getOne(item.getDriver_code());
                    detailDTO.setDriver_code(d.getDrive_code());
                    detailDTO.setDriver_name(d.getName());
                    detailDTO.setDriver_phone(d.getPhone());
                    detailDTO.setMachine_code(d.getMachine().getMachine_code());
                    detailDTO.setLicense_plate(d.getMachine().getLicense_plate());
                }
                detailDTO.setEvidence_image(item.getEvidence_image());
                detailDTO.setWorking_status(item.getStatus().getType_id());
                detailDTO.setHas_temporary(item.getHas_temporary());
                if (item.getWorked_hours() != null) {
                    detailDTO.setWorked_hour(item.getWorked_hours().toString());
                } else {
                    detailDTO.setWorked_hour(null);
                }
                detailDTO.setWorking_status_name(item.getStatus().getType_name());
                lsDto.add(detailDTO);
            }
            dto.setListDriverInfo(lsDto);
            dto.setDate(b.getDate().toString());
            return dto;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public FullProjectInfoDTO getFullProjectInfo(String projectCode, int pageIndex, int pageSize, String driverName, String from, String to) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<BookingScheduleDetail> page = detailRepository.findAll(
                Specification.where(FullProjectSpecs.getFullProjectInfo(projectCode, driverName, from, to, driverRepository)), pageable);
        List<BookingScheduleDetail> lsFilter = page.toList();
        List<BookingSchedule> lsBooking = getListBookingFromListDetail(lsFilter);
        List<ListDateInfoDTO> lsDriver = new ArrayList<>();
        for (int i = 0; i < lsBooking.size(); i++) {
            BookingSchedule b = lsBooking.get(i);
            List<BookingScheduleDetail> lsDetail = getListDetailFromListDate(b.getId(), lsFilter);
            ListDateInfoDTO dateDto = new ListDateInfoDTO();
            dateDto.setDate(b.getDate().toString());
            List<ScheduleDetailDTO> lsDetailDto = new ArrayList<>();
            for (int j = 0; j < lsDetail.size(); j++) {
                BookingScheduleDetail item = lsDetail.get(j);
                ScheduleDetailDTO detailDTO = new ScheduleDetailDTO();
                if (item.getId() != null) {
                    detailDTO.setSchedule_detail_id(item.getId());
                }
                detailDTO.setBegin_time(format.format(item.getBegin_time()));
                detailDTO.setFinish_time(format.format(item.getFinish_time()));
                if (item.getDriver_code() != null) {
                    DriverPOJO d = driverServiceImpl.getOne(item.getDriver_code());
                    detailDTO.setDriver_code(d.getDrive_code());
                    detailDTO.setDriver_name(d.getName());
                    detailDTO.setDriver_phone(d.getPhone());
                    detailDTO.setMachine_code(d.getMachine().getMachine_code());
                    detailDTO.setLicense_plate(d.getMachine().getLicense_plate());
                }
                detailDTO.setEvidence_image(item.getEvidence_image());
                detailDTO.setWorking_status(item.getStatus().getType_id());
                detailDTO.setHas_temporary(item.getHas_temporary());
                if (item.getWorked_hours() != null) {
                    detailDTO.setWorked_hour(format.format(item.getWorked_hours()));
                }
                detailDTO.setWorking_status_name(item.getStatus().getType_name());
                lsDetailDto.add(detailDTO);
            }
            dateDto.setListDriverInfo(lsDetailDto);
            lsDriver.add(dateDto);
        }
        FullProjectInfoDTO dto = new FullProjectInfoDTO();
        dto.setTotalPages(page.getTotalPages());
        ProjectPOJO p = projectServiceImpl.getOne(projectCode);
        dto.setProject_code(p.getProject_code());
        dto.setProject_name(p.getProject_name());
        dto.setCustomer(CustomerDTO.from(p.getCustomer(), null));
        dto.setProject_status(p.getProjectStatus().getStatus_name());
        dto.setProject_type(p.getProjectType().getType_name());
        if (p.getProjectType().getType_id() == 2) {
            if (contractServiceImpl.getByProjectCode(p.getProject_code()) != null) {
                dto.setHas_contract(true);
            } else {
                dto.setHas_contract(false);
            }
        }
        if (invoiceService.getByProjectCode(p.getProject_code()) != null) {
            dto.setHas_invoice(true);
        } else {
            dto.setHas_invoice(false);
        }
        dto.setLsDate(lsDriver);
        return dto;
    }

    public List<BookingSchedule> getListBookingFromListDetail(List<BookingScheduleDetail> ls) {
        List<BookingSchedule> lsOutput = new ArrayList<>();
        for (int i = 0; i < ls.size(); i++) {
            BookingScheduleDetail sd = ls.get(i);
            if (!checkBooking(sd.getBookingSchedule().getId(), lsOutput)) {
                lsOutput.add(sd.getBookingSchedule());
            }
        }
        return lsOutput;
    }

    public boolean checkBooking(Long id, List<BookingSchedule> ls) {
        for (int i = 0; i < ls.size(); i++) {
            if (ls.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    public List<BookingScheduleDetail> getListDetailFromListDate(Long id, List<BookingScheduleDetail> ls) {
        List<BookingScheduleDetail> lsOutput = new ArrayList<>();
        for (int i = 0; i < ls.size(); i++) {
            if (ls.get(i).getBookingSchedule().getId() == id) {
                lsOutput.add(ls.get(i));
            }
        }
        return lsOutput;
    }

    @NotNull
    private List<BookingSchedule> getBookingSchedules(String projectCode, List<BookingSchedule> ls, LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            BookingSchedule schedule = convertFromDto(projectCode, Date.valueOf(date));
            if (holidayService.getByDate(Date.valueOf(date)) == null) {
                ls.add(schedule);
            }
        }
        return repository.saveAll(ls);
    }

    @Override
    public int cancelBooking(String projectCode, String begin, String end) {
        try {
            int count = 0;
            java.util.Date from = new SimpleDateFormat("yyyy-MM-dd").parse(begin);
            java.util.Date to = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            LocalDate startDate = new Date(from.getTime()).toLocalDate();
            LocalDate endDate = new Date(to.getTime()).toLocalDate();
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                BookingSchedule b = repository.getOne(projectCode, Date.valueOf(date));
                if (b != null) {
                    detailRepository.delete(b.getId());
                    count = repository.delete(projectCode, Date.valueOf(date));
                }
            }
            return count;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
