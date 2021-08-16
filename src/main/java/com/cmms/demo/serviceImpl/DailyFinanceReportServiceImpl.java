package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.DailyFinanceReport;
import com.cmms.demo.domain.Invoice;
import com.cmms.demo.domain.PaymentStatus;
import com.cmms.demo.domain.PaymentType;
import com.cmms.demo.dto.DailyFinanceReportDto;
import com.cmms.demo.dto.DailyFinanceReportOutput;
import com.cmms.demo.reponsitory.DailyFinanceReportRepository;
import com.cmms.demo.reponsitory.InvoiceRepository;
import com.cmms.demo.service.DailyFinanceReportService;
import com.cmms.demo.specification.DailyFinanceRpSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyFinanceReportServiceImpl implements DailyFinanceReportService {
    @Autowired
    private DailyFinanceReportRepository repository;
    @Autowired
    private PaymentTypeServiceImpl typeService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private DriverServiceImpl driverServiceImpl;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PaymentStatusService paymentStatusService;

    @Override
    public DailyFinanceReportDto addDailyReport(DailyFinanceReportDto dto) {
        DailyFinanceReport dailyFinanceReport = new DailyFinanceReport();
        dailyFinanceReport.setAmount(dto.getAmount());
        dailyFinanceReport.setTitle(dto.getTitle());
        dailyFinanceReport.setNote(dto.getNote());
        dailyFinanceReport.setPayment_date(new Date(System.currentTimeMillis()));
        PaymentType paymentType = typeService.getOne(dto.getPayment_type().getType_id());
        dailyFinanceReport.setPaymentType(paymentType);
        dailyFinanceReport.set_auto(dto.is_auto());
        if (dto.getProject_code() != null) {
            dailyFinanceReport.setProject_code(dto.getProject_code());
            Invoice invoice = invoiceRepository.getByProjectCode(dto.getProject_code());
            if(invoice != null){
                if(invoice.getAdvance_amount()!=null){
                    invoice.setAdvance_amount(invoice.getAdvance_amount() + dto.getAmount());
                }else{
                    invoice.setAdvance_amount(0 + dto.getAmount());
                }
                if(invoice.getAdvance_amount() == invoice.getTotal_amount()){
                    invoice.setPaymentStatus(paymentStatusService.getOne(3L));
                }else if(invoice.getAdvance_amount() > 0){
                    invoice.setPaymentStatus(paymentStatusService.getOne(2L));
                }
                invoiceRepository.save(invoice);
            }
        }
        if (dto.getDriver() != null && dto.getDriver().getDrive_code() != null) {
            dailyFinanceReport.setDriver_code(dto.getDriver().getDrive_code());
        }
        repository.save(dailyFinanceReport);
        return DailyFinanceReportDto.from(dailyFinanceReport, projectService, driverServiceImpl);
    }

    @Override
    public DailyFinanceReportOutput filter(int pageIndex, int pageSize, Integer paymentType, String fromDate, String toDate, String title) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<DailyFinanceReport> page = repository.findAll(Specification.where(DailyFinanceRpSpecs.filter(paymentType, fromDate, toDate, title)), pageable);
        List<DailyFinanceReport> ls = page.toList();
        List<DailyFinanceReportDto> lsDto = ls.stream()
                .map(item -> DailyFinanceReportDto.from(item, projectService, driverServiceImpl)).collect(Collectors.toList());
        DailyFinanceReportOutput output = new DailyFinanceReportOutput();
        output.setTotalPages(page.getTotalPages());
        output.setLs(lsDto);
        return output;
    }

    @Override
    public DailyFinanceReportDto getOne(Long id) {
        DailyFinanceReport d =  repository.findById(id).orElse(null);
        return DailyFinanceReportDto.from(d, projectService, driverServiceImpl);
    }

    @Override
    public DailyFinanceReportDto updateDailyFinanceRp(DailyFinanceReportDto item) {
        DailyFinanceReport d = repository.findById(item.getId()).orElse(null);
        if (d != null) {
            if (item.getAmount() != null) {
                d.setAmount(item.getAmount());
            }
            if (item.getNote() != null) {
                d.setNote(item.getNote());
            }
            if (item.getTitle() != null) {
                d.setTitle(item.getTitle());
            }
            if (item.getProject_code() != null) {
                d.setProject_code(item.getProject_code());
            }
            if (item.getDriver().getDrive_code() != null) {
                d.setDriver_code(item.getDriver().getDrive_code());
            }
        }
        repository.save(d);
        return DailyFinanceReportDto.from(d, projectService, driverServiceImpl);
    }

    @Override
    public String deleteDailyFinanceRp(Long id) {
        int delete = repository.delete(id);
        if (delete > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
    public DailyFinanceReport saveReport(DailyFinanceReport report){
       return repository.save(report);
    }
    public List<DailyFinanceReport> getReportByStaff(String staff_name){
        List<DailyFinanceReport> reports = repository.getReportByDriverCode(staff_name);
        return reports;
    }
    public void saveAll(List<DailyFinanceReport> financeReports){
        repository.saveAll(financeReports);
    }
}
