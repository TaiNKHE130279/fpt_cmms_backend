package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.reponsitory.*;
import com.cmms.demo.service.PayrollService;
import com.cmms.demo.specification.PayrollSpecs;
import org.apache.http.HttpException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {
    @Autowired
    PayrollRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    UserServiceImpl userDetailsService;
    @Autowired
    DriverServiceImpl driverServiceImpl;
    @Autowired
    PayrollDetailRepository detailRepository;
    @Autowired
    DriverDayOffTrackingRepository dayOffRequestRepository;
    @Autowired
    DriverDayOffTrackingDetailRepository offTrackingDetailRepository;
    @Autowired
    BonusRepository bonusRepository;
    @Autowired
    DeductionRepository deductionRepository;

    @Autowired
    PayrollStatusRepository statusRepository;

    @Autowired
    PayrollBonusRepository payrollBonusRepository;

    @Autowired
    PayrollDeductionRepository payrollDeductionRepository;
    @Autowired
    DriverDayOffTrackingSerivce offTrackingSerivce;

    @Autowired
    DailyFinanceReportServiceImpl dailyFinanceReportServiceImpl;

    @Autowired
    BookingScheduleServiceImpl bookingScheduleService;

    @Override
    public Page<Payroll> filter(int pageIndex, int pageSize, String payroll_name, Integer month, Integer year,Long status) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by("id").descending());
        Page<Payroll> payrollss = repository.findAll(Specification.where(PayrollSpecs.filter(payroll_name, month, year,status)), pageable);
        return payrollss;
    }

    @Override
    public List<Payroll> findAll() {

        List<Payroll> payrolls = repository.findAll().stream().filter(payroll -> payroll.getStatus().getId() != 4L).collect(Collectors.toList());
        Collections.sort(payrolls, (o1, o2) -> o2.getCreate_date().compareTo(o1.getCreate_date()));
        return payrolls;
    }

    private boolean checkBetweenDate(java.util.Date input, java.util.Date start, java.util.Date end) {
        if (input.compareTo(start) >= 0 && input.compareTo(end) <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public Payroll create(PayrollDTO dto) {
        List<Payroll> payrolls = findAll();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        boolean isExist = false;
        for (Payroll payroll : payrolls) {
            if (payroll.getStatus().getId() != 4) {//not delete yet
                if (payroll.getStatus().getId() == 2) { // closing salary
                    try {
                        java.util.Date date_Start = format.parse(dto.getDate_start());
                        java.util.Date date_End = format.parse(dto.getDate_end());

                        if (checkBetweenDate(date_Start, payroll.getStart_date(), payroll.getEnd_date())) {
                            isExist = true;
                        }
                        if (checkBetweenDate(date_End, payroll.getStart_date(), payroll.getEnd_date())) {
                            isExist = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isExist) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "Khoảng thời gian đã có sẵn.");
        }
        Payroll payroll = maptoEntity(dto);
        payroll.setName("Bảng lương " + format.format(payroll.getStart_date()) + " - " + format.format(payroll.getEnd_date()));
        Payroll newPayroll = repository.save(payroll);
        newPayroll.setCode("BL000" + newPayroll.getId());
        newPayroll.setStatus(statusRepository.getById(1L));
        List<PayrollDetail> detail = addDetailForPayroll(newPayroll);
        newPayroll.setSalary_total(detail.stream().map(detail1 -> detail1.getActually_received()).reduce(0L, Long::sum));
        newPayroll.setTotal_paid(detail.stream().map(de -> de.getPaid_staff()).reduce(0L, Long::sum));
        newPayroll.setSalary_remain(detail.stream().map(detail1 -> detail1.getSalary_remain()).reduce(0L, Long::sum));
        return repository.save(newPayroll);
    }

    private List<PayrollDetail> addDetailForPayroll(Payroll payroll) {
        List<UserPOJO> users = userDetailsService.repository.findAll().stream().filter(userDetailsService::isStaff).collect(Collectors.toList());
        List<PayrollDetail> details = users.stream().filter(pojo -> pojo.getDriver() != null).map(pojo -> createDetail(pojo, payroll)).collect(Collectors.toList());
        return details;
    }

    private PayrollDetail createDetail(UserPOJO pojo, Payroll payroll) {
        PayrollDetail payrollDetail = new PayrollDetail();
        DriverPOJO driver = driverServiceImpl.getByUserID(pojo.getUser_id());
        if (driver != null) {
            payrollDetail.setUser_id(pojo);
            payrollDetail.setPayroll(payroll);
            Long totalDayOff = getTotalDayOff(driver, payroll);
            payrollDetail.setNumber_day_off(totalDayOff);
            payrollDetail.setPayroll(payroll);
            PayrollDetail newPayrollDetail = detailRepository.save(payrollDetail);
            List<PayrollBonus> bonuses = new ArrayList<>();
            List<PayrollDeduction> deductions = new ArrayList<>();
            if (totalDayOff <= 0) {
                if (getTotalDateBetween(payroll) >= 30) {
                    PayrollBonus payrollBonus = new PayrollBonus();
                    BonusPOJO bonusPOJO = bonusRepository.getById(1L);
                    payrollBonus.setBonus_id(bonusPOJO);
                    payrollBonus.setPayroll_detail_id(newPayrollDetail);
                    payrollBonus.setValue(bonusPOJO.getValue());
                    payrollBonus.setTotal_value(payrollBonus.getQuantity() * payrollBonus.getValue());
                    bonuses.add(payrollBonusRepository.save(payrollBonus));
                }
            } else {
                if (totalDayOff > 5) {
                    DeductionPOJO deductionPOJO = deductionRepository.getById(1L);
                    PayrollDeduction payrollDeduction = new PayrollDeduction();
                    payrollDeduction.setDeduction_id(deductionPOJO);
                    payrollDeduction.setQuantity(totalDayOff - 5);
                    payrollDeduction.setValue(deductionPOJO.getValue());
                    payrollDeduction.setTotal_value(totalDayOff * deductionPOJO.getValue());
                    payrollDeduction.setPayroll_detail_id(newPayrollDetail);
                    deductions.add(payrollDeductionRepository.save(payrollDeduction));
                }
            }
            Long over_time = bookingScheduleService.getTotalOverTimeByDriverCode(driver.getDrive_code());
            if (over_time != null) {
                Long hours = ((over_time / (1000 * 60 * 60)) % 24);
                Long minutes = ((over_time / 1000) / 60);
                hours += (minutes / 60);
                PayrollBonus payrollBonus = new PayrollBonus();
                BonusPOJO bonusPOJO = bonusRepository.getById(2L);
                payrollBonus.setBonus_id(bonusPOJO);
                payrollBonus.setPayroll_detail_id(newPayrollDetail);
                payrollBonus.setValue(bonusPOJO.getValue());
                payrollBonus.setQuantity(hours);
                payrollBonus.setTotal_value(payrollBonus.getQuantity() * payrollBonus.getValue());
                bonuses.add(payrollBonusRepository.save(payrollBonus));
            }
            newPayrollDetail.setBonus(bonuses);
            newPayrollDetail.setDeduction(deductions);
            newPayrollDetail.setNominal_income(roundUp(driver.getBase_salary(), daysInMonth(payroll.getStart_date())));
            newPayrollDetail.setWorked_day(getTotalDateBetween(payroll) - totalDayOff);
            Long actualSalary = newPayrollDetail.getNominal_income() * newPayrollDetail.getWorked_day();
            if (newPayrollDetail.getBonus().size() > 0) {
                actualSalary += newPayrollDetail.getBonus().stream().map(bonus -> bonus.getTotal_value()).reduce(0L, Long::sum);
            }
            if (newPayrollDetail.getDeduction().size() > 0) {
                actualSalary -= (newPayrollDetail.getDeduction().stream().map(deduction -> deduction.getTotal_value()).reduce(0L, Long::sum));
            }

            newPayrollDetail.setActually_received(actualSalary);


            List<DailyFinanceReport> reports = dailyFinanceReportServiceImpl.getReportByStaff(newPayrollDetail.getUser_id().getDriver().getDrive_code());
            Long total_paid = reports.stream().map(dailyFinanceReport -> dailyFinanceReport.getAmount()).reduce(0L, Long::sum);
            newPayrollDetail.setPaid_staff(total_paid);
            newPayrollDetail.setSalary_remain(newPayrollDetail.getActually_received() - newPayrollDetail.getPaid_staff());

            return detailRepository.save(newPayrollDetail);
        }
        return new PayrollDetail();
    }


    private void setStatusOfFinanceReport(DailyFinanceReport report) {
        report.set_payment(true);
    }

    private long getTotalDateBetween(Payroll payroll) {
        LocalDate start = Instant.ofEpochMilli(payroll.getStart_date().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate end = Instant.ofEpochMilli(payroll.getEnd_date().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long numOfDaysBetween = ChronoUnit.DAYS.between(start, end) + 1L;
        return numOfDaysBetween;
    }

    private int daysInMonth(java.util.Date date) {
        YearMonth yearMonthObject = YearMonth.of(date.getYear(), date.getMonth());
        int daysInMonth = yearMonthObject.lengthOfMonth();

        return daysInMonth;
    }

    private long roundUp(long num, long divisor) {
        long value = (num + divisor - 1) / divisor;
        long roundedNumber = (value + 500) / 1000 * 1000;
        return roundedNumber;
    }

    private Long getTotalDayOff(DriverPOJO driver, Payroll payroll) {
        int value = dayOffRequestRepository.getTotalDayOffByDriver(driver.getDrive_code(), payroll.getStart_date(), payroll.getEnd_date());
        return Long.valueOf(value);
    }

    private List<DriverDayOffTrackingDetailPOJO> getAllDayOff(DriverPOJO pojo, Payroll payroll) {
        List<DriverDayOffTrackingDetailPOJO> details = offTrackingDetailRepository.getAllDayOffByDriver(pojo.getDrive_code(), payroll.getStart_date(), payroll.getEnd_date());
        return details;
    }


    private Payroll maptoEntity(PayrollDTO dto) {
        Payroll payroll = mapper.map(dto, Payroll.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date date1 = format.parse(dto.getDate_start());
            java.util.Date date2 = format.parse(dto.getDate_end());
            payroll.setStart_date(new Date(date1.getTime()));
            payroll.setEnd_date(new Date(date2.getTime()));
            payroll.setCreate_date(new Date(System.currentTimeMillis()));
            if (dto.getId() != null) {
                payroll.setId(dto.getId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return payroll;
    }

    @Override
    public Payroll updatePayroll(PayrollDTO dto, Long id) {
        Payroll payroll = repository.getById(id);
        if (payroll.isDone()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Không thể sửa, do bảng lương đã thanh toán xong.");
        }
        if (payroll.isClosing()) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Không thể sửa, do bảng lương đã được chốt.");
        }
        payroll.setCode(dto.getCode());
        payroll.setName(dto.getName());
        List<Long> ids = getIdDetailForRemove(payroll.getDetail().stream().map(payrollDetail -> payrollDetail.getId()).collect(Collectors.toList()), dto.getDetail().stream().map(detailDTO -> detailDTO.getId()).collect(Collectors.toList()));
        for (PayrollDetail detail : payroll.getDetail()) {
            if (ids.contains(detail.getId())) {
                detail.setRemove(true);
            }
        }
        return repository.save(payroll);
    }

    private List<Long> getIdDetailForRemove(List<Long> details, List<Long> dto) {
        List<Long> ids = new ArrayList<>();
        for (Long aLong : details) {
            if (!dto.contains(aLong)) {
                ids.add(aLong);
            }
        }
        return ids;
    }

    @Override
    public PayrollDetail updateDetail(Long id, PayrollDetailDTO payrollDetailDTO) {
        PayrollDetail detail = detailRepository.getById(payrollDetailDTO.getId());
        return detailRepository.save(detail);
    }

    @Override
    public Payroll salaryClosing(PayrollDTO dto) {
        Payroll payroll = updateInforPayroll(dto);
        payroll.setClosing(true);
        List<DriverPOJO> driver = payroll.getDetail().stream().map(payrollDetail -> payrollDetail.getUser_id().getDriver()).collect(Collectors.toList());
        for (DriverPOJO driverPOJO : driver) {
            List<DriverDayOffTrackingDetailPOJO> dayOffDetails = getAllDayOff(driverPOJO, payroll);
            dayOffDetails.forEach(detailPOJO -> {
                detailPOJO.setRemove(true);
            });
            List<DailyFinanceReport> reports = dailyFinanceReportServiceImpl.getReportByStaff(driverPOJO.getDrive_code());
            reports.forEach(report -> report.set_payment(true));
            offTrackingDetailRepository.saveAll(dayOffDetails);
            dailyFinanceReportServiceImpl.saveAll(reports);
            List<Payroll> payrolls = repository.getAllIgnoreId(payroll.getId());
            payrolls.forEach(payroll1 -> payroll1.setStatus(statusRepository.getById(4L)));
            repository.saveAll(payrolls);
        }



        payroll.setStatus(statusRepository.getById(2L));
        return repository.save(payroll);
    }


    private Payroll updateInforPayroll(PayrollDTO dto) {
        List<PayrollDetailDTO> detailDTOS = dto.getDetail();
        Payroll payroll = repository.getById(dto.getId());
        List<PayrollDetail> details = detailDTOS.stream().map(payrollDetailDTO -> updateDetail(payroll.getId(), payrollDetailDTO)).collect(Collectors.toList());
        payroll.setDetail(details);
        return payroll;
    }

    @Override
    public PayrollDetail getOneDetail(Long id) {
        PayrollDetail detail = detailRepository.getById(id);
        return detail;
    }

    @Override
    public Payroll getOnePayroll(Long id) {
        Payroll payroll = repository.findById(id).orElse(null);
        List<PayrollDetail> details = payroll.getDetail().stream().filter(payrollDetail -> !payrollDetail.isRemove()).collect(Collectors.toList());
        payroll.setTotal_paid(details.stream().map(payrollDetail -> payrollDetail.getPaid_staff()).reduce(0L, Long::sum));
        payroll.setSalary_remain(details.stream().map(payrollDetail -> payrollDetail.getSalary_remain()).reduce(0L, Long::sum));
        payroll.setDetail(details);
        payroll.setSalary_total(details.stream().map(payrollDetail -> payrollDetail.getActually_received()).reduce(0L, Long::sum));
        return repository.save(payroll);
    }

    @Override
    public List<PayrollDetail> getAllDetail(Long id) {
        List<PayrollDetail> details = detailRepository.findAllByPayrollId(id);
        return details;
    }

    private BonusPOJO createBonus(BonusDTO dto) {
        BonusPOJO pojo = new BonusPOJO();
        pojo.setValue(dto.getValue());
        pojo.setName(dto.getName());
        if (dto.getUnit() != null) {
            pojo.setType(dto.getUnit());
        }
        pojo.setDefault(false);
        return bonusRepository.save(pojo);
    }

    private PayrollBonus createPayrollBonus(PayrollBonusDTO dto, Long payrollDetail) {
        PayrollBonus payrollBonus = new PayrollBonus();
        PayrollDetail detail = detailRepository.getById(payrollDetail);
        dto.getDto().setValue(dto.getValue());
        BonusPOJO bonus = createBonus(dto.getDto());
        payrollBonus.setBonus_id(bonus);
        payrollBonus.setValue(dto.getValue());
        payrollBonus.setQuantity(dto.getQuantity());
        payrollBonus.setTotal_value(dto.getValue() * dto.getQuantity());
        payrollBonus.setPayroll_detail_id(detail);
        return payrollBonus;
    }

    private void deletePayrollBonus(List<PayrollBonus> bonuses, List<PayrollBonusDTO> bonusDTOS) {
        List<Long> ids = new ArrayList<>();
        List<Long> id_bonuses = bonuses.stream().map(dto -> dto.getId()).collect(Collectors.toList());
        List<Long> id_bonusDTO = bonusDTOS.stream().filter(dto -> dto.getId() != null).map(dto -> dto.getId()).collect(Collectors.toList());
        for (Long x : id_bonuses) {
            if (!id_bonusDTO.contains(x)) {
                ids.add(x);
            }
        }
        ids.forEach(aLong -> detailRepository.deleteBonusByPayrollId(aLong));
    }

    private void deletePayrollDeduction(List<PayrollDeduction> deductions, List<PayrollDeductionDTO> deductionDTOS) {
        List<Long> ids = new ArrayList<>();
        List<Long> id_bonuses = deductions.stream().map(dto -> dto.getId()).collect(Collectors.toList());
        List<Long> id_bonusDTO = deductionDTOS.stream().filter(dto -> dto.getId() != null).map(dto -> dto.getId()).collect(Collectors.toList());
        for (Long x : id_bonuses) {
            if (!id_bonusDTO.contains(x)) {
                ids.add(x);
            }
        }
        ids.forEach(aLong -> detailRepository.deleteDeductionByPayrollId(aLong));
    }

    private PayrollDetail processDetail(PayrollDetailDTO dto) {
        List<PayrollBonusDTO> createBonus = dto.getBonus().stream().filter(payrollBonusDTO -> payrollBonusDTO.getId() == null).collect(Collectors.toList());
        PayrollDetail detail = detailRepository.getById(dto.getId());
        List<PayrollBonus> payrollBonusList = detail.getBonus();
        deletePayrollBonus(payrollBonusList, dto.getBonus());
        deletePayrollDeduction(detail.getDeduction(), dto.getDeduction());
        List<PayrollBonus> bonuses = new ArrayList<>();
        List<PayrollDeduction> deductions = new ArrayList<>();

        Long current_salary = detail.getActually_received();
        Long deduction_old = detail.getDeduction().stream().map(deduction -> deduction.getTotal_value()).reduce(0L, Long::sum);
        Long bonus_old = detail.getBonus().stream().map(bonus -> bonus.getTotal_value()).reduce(0L, Long::sum);

        for (PayrollBonusDTO dto1 : createBonus) {
            bonuses.add(payrollBonusRepository.save(createPayrollBonus(dto1, dto.getId())));
        }
        List<PayrollDeductionDTO> createDeduction = dto.getDeduction().stream().filter(payrollDeductionDTO -> payrollDeductionDTO.getId() == null).collect(Collectors.toList());
        for (PayrollDeductionDTO dto1 : createDeduction) {
            deductions.add(payrollDeductionRepository.save(createPayrollDeduction(dto1, dto.getId())));
        }
        for (PayrollBonus bonus : updatePayrollBonus(dto)) {
            bonuses.add(bonus);
        }
        for (PayrollDeduction deduction : updatePayrollDeduction(dto)) {
            deductions.add(deduction);
        }
        if (deductions != null) {
            current_salary += deduction_old;
            Long deduction_new = deductions.stream().map(deduction -> deduction.getTotal_value()).reduce(0L, Long::sum);
            current_salary -= deduction_new;
            detail.setActually_received(current_salary);
        }
        if (bonuses != null) {
            current_salary -= bonus_old;
            detail.setBonus(bonuses);
            Long total_bonus = bonuses.stream().map(bonus -> bonus.getTotal_value()).reduce(0L, Long::sum);
            current_salary += total_bonus;
            detail.setActually_received(current_salary);
        }
        detail.setSalary_remain(detail.getActually_received() - detail.getPaid_staff());
        return detailRepository.save(detail);
    }

    private List<PayrollDeduction> updatePayrollDeduction(PayrollDetailDTO dto) {
        List<PayrollDeduction> deductions = new ArrayList<>();
        if (dto.getDeduction() != null) {
            for (PayrollDeductionDTO dto1 : dto.getDeduction()) {
                if (dto1.getId() != null) {
                    PayrollDeduction deduction = payrollDeductionRepository.getById(dto1.getId());
                    deduction.getDeduction_id().setName(dto1.getDto().getName());
                    deduction.getDeduction_id().setType(dto1.getDto().getUnit());
                    deduction.setValue(dto1.getValue());
                    deduction.setQuantity(dto1.getQuantity());
                    deduction.setTotal_value(dto1.getValue() * dto1.getQuantity());
                    deductions.add(deduction);
                }
            }
        }
        return deductions;
    }

    private List<PayrollBonus> updatePayrollBonus(PayrollDetailDTO dto) {
        List<PayrollBonus> bonuses = new ArrayList<>();
        if (dto.getBonus() != null) {
            for (PayrollBonusDTO dto1 : dto.getBonus()) {
                if (dto1.getId() != null) {
                    PayrollBonus bonus = payrollBonusRepository.getById(dto1.getId());
                    bonus.setValue(dto1.getValue());
                    bonus.setQuantity(dto1.getQuantity());
                    bonus.setTotal_value(dto1.getValue() * dto1.getQuantity());
                    bonus.getBonus_id().setName(dto1.getDto().getName());
                    bonus.getBonus_id().setType(dto1.getDto().getUnit());
                    bonuses.add(bonus);
                }
            }
        }
        return bonuses;
    }

    private PayrollDeduction createPayrollDeduction(PayrollDeductionDTO dto1, Long payroll_id) {
        DeductionPOJO deduction = new DeductionPOJO();
        PayrollDetail detail = detailRepository.getById(payroll_id);
        deduction.setName(dto1.getDto().getName());
        deduction.setValue(dto1.getDto().getValue());
        if (dto1.getDto().getUnit() != null) {
            deduction.setType(dto1.getDto().getUnit());
        }
        DeductionPOJO newDeduction = deductionRepository.save(deduction);
        PayrollDeduction payrollDeduction = new PayrollDeduction();
        payrollDeduction.setDeduction_id(newDeduction);
        payrollDeduction.setPayroll_detail_id(detail);
        payrollDeduction.setValue(dto1.getValue());
        payrollDeduction.setQuantity(dto1.getQuantity());
        payrollDeduction.setTotal_value(dto1.getValue() * dto1.getQuantity());
        return payrollDeduction;
    }


    @Override
    public PayrollDetail updateOneDetail(Long detailId, PayrollDetailDTO detailDTO) {
        PayrollDetail detail = detailRepository.getById(detailId);
        if (detail != null) {
            return processDetail(detailDTO);
        }
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Bảng lương không tồn tại");
    }

    @Override
    public PayrollDetail removeDetail(Long id) {
        PayrollDetail detail = detailRepository.getById(id);
        detail.setRemove(true);
        return detailRepository.save(detail);
    }

    @Override
    public Payroll updateDataPayroll(Long id) {
        Payroll payroll = repository.getById(id);
        payroll.getDetail().forEach(payrollDetail -> payrollDetail.setRemove(false));
        return repository.save(payroll);
    }

    @Override
    public PayrollDetail paymentPayrollDetail(Long id, Long value) {
        PayrollDetail detail = detailRepository.getById(id);
        detail.setPaid_staff(detail.getPaid_staff() + value);
        Long temp = detail.getSalary_remain() - value;
        if (temp < 0) {
            detail.setSalary_remain(0L);
        } else {
            detail.setSalary_remain(temp);
        }
        return detailRepository.save(detail);
    }

    @Override
    public Payroll deletePayroll(Long id) {
        Payroll payroll = repository.getById(id);
        payroll.setStatus(statusRepository.getById(4L));
        return repository.save(payroll);
    }

    @Override
    public PayrollDetail getOneDetailOfDriver(String driver_id, Long payroll_id) {
        PayrollDetail detail = detailRepository.getOneDetailByDriver(driver_id, payroll_id);

        return detail;
    }

    @Override
    public List<Payroll> findAllPayrollHasDriver(String driver_id) {
        DriverPOJO driverPOJO = driverServiceImpl.getOne(driver_id);
        List<Payroll> payrolls = repository.findAllByDriverCode(driver_id).stream().filter(payroll -> payroll.isClosing()).filter(payroll -> payroll.getStatus().getId() != 4).collect(Collectors.toList());
        List<Payroll> payrollWasfilter = new ArrayList<>();
        for (Payroll payroll : payrolls) {
            List<PayrollDetail> wasFilter = new ArrayList<>();
            for (PayrollDetail detail : payroll.getDetail()) {
                if (detail.getUser_id().getUser_id() == driverPOJO.getUser().getUser_id()) {
                    wasFilter.add(detail);
                }
            }
            if (wasFilter.size() != 0) {
                payroll.setDetail(wasFilter);
                payrollWasfilter.add(payroll);
            }
        }
        return payrollWasfilter;
    }
}
