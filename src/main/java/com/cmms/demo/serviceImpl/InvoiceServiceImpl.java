package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.reponsitory.*;
import com.cmms.demo.service.InvoiceService;
import com.cmms.demo.specification.InvoiceSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository repository;
    @Autowired
    ProjectServiceImpl projectServiceImpl;
    @Autowired
    GoogleDriveService googleDriveService;
    @Autowired
    private PaymentStatusService statusService;
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private BookingScheduleRepository bookingRepository;
    @Autowired
    private DriverServiceImpl driverServiceImpl;
    @Autowired
    private ContractServiceImpl contractServiceImpl;
    @Autowired
    private WorkByHourRepository workByHourRepository;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private DailyFinanceReportRepository dailyRepository;


    @Override
    public Invoice create(InvoiceDTO dto){
        ProjectPOJO project = projectServiceImpl.getOne(dto.getProject_code());
        Invoice invoice = new Invoice();
        if (dto.getInvoice_code() != null) {
            invoice.setInvoice_code(dto.getInvoice_code());
        } else {
            invoice.setInvoice_code("INVOICE-" + dto.getProject_code());
        }

        invoice.setProject(project);
        invoice.setCreate_date(new Date(System.currentTimeMillis()));

        if(dto.getEvidence_image() != null) {
            invoice.setEvidence_image(dto.getEvidence_image());
        }

        if(project.getProjectType().getType_id() == 2){
            Contract c = contractServiceImpl.getOneByProjectCode(project.getProject_code());
            invoice.setAdvance_amount(c.getAdvance_amount());
            invoice.setTotal_amount(c.getContract_price());
            if(c.getAdvance_amount() == 0){
                invoice.setPaymentStatus(statusService.getOne(1L));
            }else if(c.getAdvance_amount() > 0){
                invoice.setPaymentStatus(statusService.getOne(2L));
            }else{
                invoice.setPaymentStatus(statusService.getOne(3L));
            }
        }else{
            invoice.setAdvance_amount(dto.getAdvance_amount());
            invoice.setTotal_amount(dto.getTotal_amount());
            invoice.setPaymentStatus(statusService.getOne(1L));
        }
        return repository.save(invoice);
    }

    @Override
    public DataToFillInvoiceDTO getOne(String invoiceCode) {
        Invoice invoice = repository.getOne(invoiceCode);
        DataToFillInvoiceDTO output = new DataToFillInvoiceDTO();
        if(invoice != null){
            ProjectPOJO project = invoice.getProject();
            output.setCustomer(CustomerDTO.from(project.getCustomer(), null));

            List<DailyFinanceReport> lsReceipt = dailyRepository.getListByProjectCode(project.getProject_code());
            output.setLsReceipt(lsReceipt.stream().map(item ->
                    DailyFinanceReportDto.from(item, projectServiceImpl, driverServiceImpl)).collect(Collectors.toList()));

            output.setPayment_status(PaymentStatusDTO.from(invoice.getPaymentStatus()));
            output.setCreate_date(invoice.getCreate_date().toString());

            if(project.getProjectType().getType_id() == 1) {
                WorkByHourPOJO workByHourPOJO = workByHourRepository.getByProjectCode(project.getProject_code());
                List<BookingSchedule> lsBooking = bookingRepository.getListByProject(project.getProject_code());
                List<InvoiceDetailDTO> lsDetail = new ArrayList<>();
                for (int i = 0; i < lsBooking.size(); i++) {
                    BookingSchedule b = lsBooking.get(i);
                    List<BookingScheduleDetail> lsBookingDetail = detailRepository.getListByBookingId(b.getId());
                    for (int j = 0; j < lsBookingDetail.size(); j++) {
                        InvoiceDetailDTO dto = new InvoiceDetailDTO();
                        BookingScheduleDetail sd = lsBookingDetail.get(j);
                        DriverPOJO driverPOJO = driverServiceImpl.getOne(sd.getDriver_code());
                        dto.setDriver_name(driverPOJO.getName());
                        dto.setPhone(driverPOJO.getPhone());
                        dto.setDate(b.getDate().toString());
                        dto.setWork_time(sd.getWorked_hours().toString());
                        dto.setImage(sd.getEvidence_image());
                        lsDetail.add(dto);
                    }
                }
                output.setDetail(lsDetail);
                output.setTotal_work_time(getTotalWorkTime(lsDetail));
                output.setAmount(invoice.getTotal_amount());
                output.setPrice_per_hour(workByHourPOJO.getPrice_per_hour());
                output.setAdvance_amount(invoice.getAdvance_amount());
            }else{
                Contract c = contractServiceImpl.getOneByProjectCode(project.getProject_code());
                output.setAmount(c.getContract_price());
                output.setAdvance_amount(c.getAdvance_amount());
                output.setDetail(null);
                output.setContent(c.getContent());
            }
        }
        return output;
    }

    @Override
    public InvoiceOutput filter(int pageIndex, int pageSize, String cusName , String date, Long paymentType) {
        Pageable pageable = PageRequest.of(pageIndex -1, pageSize);
        Page<Invoice> page = repository.findAll(Specification.where(InvoiceSpecs.filter(cusName, date, paymentType)), pageable);
        List<Invoice> ls = page.toList();
        List<InvoiceDTO> lsDto = ls.stream().map(InvoiceDTO::from).collect(Collectors.toList());
        InvoiceOutput output = new InvoiceOutput();
        output.setTotalPages(page.getTotalPages());
        output.setInvoiceList(lsDto);
        return output;
    }

    @Override
    public DataToFillInvoiceDTO fillDataToInvoice(String projectCode){
        DataToFillInvoiceDTO output = new DataToFillInvoiceDTO();
        ProjectPOJO project = projectServiceImpl.getOne(projectCode);
        output.setCustomer(CustomerDTO.from(project.getCustomer(), null));

        List<DailyFinanceReport> lsReceipt = dailyRepository.getListByProjectCode(projectCode);
        output.setLsReceipt(lsReceipt.stream().map(item ->
                DailyFinanceReportDto.from(item, projectServiceImpl, driverServiceImpl)).collect(Collectors.toList()));

        if(project.getProjectType().getType_id() == 1) {
            WorkByHourPOJO workByHourPOJO = workByHourRepository.getByProjectCode(projectCode);
            List<BookingSchedule> lsBooking = bookingRepository.getListByProject(projectCode);
            List<InvoiceDetailDTO> lsDetail = new ArrayList<>();
            for (int i = 0; i < lsBooking.size(); i++) {
                BookingSchedule b = lsBooking.get(i);
                List<BookingScheduleDetail> lsBookingDetail = detailRepository.getListByBookingId(b.getId());
                for (int j = 0; j < lsBookingDetail.size(); j++) {
                    InvoiceDetailDTO dto = new InvoiceDetailDTO();
                    BookingScheduleDetail sd = lsBookingDetail.get(j);
                    DriverPOJO driverPOJO = driverServiceImpl.getOne(sd.getDriver_code());
                    dto.setDriver_name(driverPOJO.getName());
                    dto.setPhone(driverPOJO.getPhone());
                    dto.setDate(b.getDate().toString());
                    dto.setWork_time(sd.getWorked_hours().toString());
                    lsDetail.add(dto);
                }
            }
            output.setDetail(lsDetail);
            output.setTotal_work_time(getTotalWorkTime(lsDetail));
            output.setAmount(getTotalAmount(output.getTotal_work_time(), workByHourPOJO.getPrice_per_hour()));
            output.setPrice_per_hour(workByHourPOJO.getPrice_per_hour());
        }else{
            Contract c = contractServiceImpl.getOneByProjectCode(projectCode);
            output.setAmount(c.getContract_price());
            output.setAdvance_amount(c.getAdvance_amount());
            output.setDetail(null);
            output.setContent(c.getContent());
        }
        return output;
    }

    public String getTotalWorkTime(List<InvoiceDetailDTO> ls){
        int hours = 0, minutes = 0;
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("HH:mm");
        for(int i = 0; i<ls.size(); i++){
            try {
                java.util.Date t = format.parse(ls.get(i).getWork_time());
                calendar.setTime(t);
                hours += calendar.get(Calendar.HOUR);
                minutes += calendar.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        hours += minutes / 60;
        minutes = minutes % 60;
        String result = "";
        if(minutes == 0){
            result = hours + ":"+minutes+"0";
        }else if(minutes < 10){
            result = hours + ":"+"0"+minutes;
        }
        else{
            result = hours +":"+minutes;
        }
        return result;
    }

    public Long getTotalAmount(String totalWorkTime, Long pricePerHour){
        String[] str = totalWorkTime.split(":");
        int hour = Integer.parseInt(str[0]);
        int minute = Integer.parseInt(str[1]);
        Long result = 0L;
        result = hour * pricePerHour + (minute * pricePerHour) / 60;
        return result;
    }

    @Override
    public Invoice getByProjectCode(String project_code) {
        return repository.getByProjectCode(project_code);
    }

    @Override
    public String update(DataToFillInvoiceDTO dto, String projectCode){
        Invoice item = getByProjectCode(projectCode);
        if(item != null){
            if(dto.getCustomer() != null && dto.getCustomer().getCustomer_code() != null){
                CustomerPOJO customer = customerService.update(dto.getCustomer());
            }
            if(dto.getPrice_per_hour() != null){
                WorkByHourPOJO workByHourPOJO = workByHourRepository.getByProjectCode(item.getProject().getProject_code());
                if(workByHourPOJO != null){
                    workByHourPOJO.setPrice_per_hour(dto.getPrice_per_hour());
                    workByHourRepository.save(workByHourPOJO);
                }
            }
            return "success";
        }
        return "fail";
    }

}
