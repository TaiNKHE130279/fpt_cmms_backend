package com.cmms.demo.endpoint;

import com.cmms.demo.domain.PaymentStatus;
import com.cmms.demo.dto.DataToFillInvoiceDTO;
import com.cmms.demo.dto.InvoiceDTO;
import com.cmms.demo.dto.InvoiceOutput;
import com.cmms.demo.dto.PaymentStatusDTO;
import com.cmms.demo.service.InvoiceService;
import com.cmms.demo.serviceImpl.InvoiceServiceImpl;
import com.cmms.demo.serviceImpl.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/invoice", name = "Quản lý hóa đơn")
@CrossOrigin("*")
public class InvoiceController {
    @Autowired
    private InvoiceService service;
    @Autowired
    private PaymentStatusService statusService;

    @GetMapping(name = "Xem danh sách hóa đơn")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<InvoiceOutput> filter(@RequestParam("page") int pageIndex,
                                                @RequestParam("limit") int pageSize,
                                                @RequestParam(value = "customerName", required = false) String customerName,
                                                @RequestParam(value = "date", required = false) String date,
                                                @RequestParam(value = "statusId", required = false) Long statusId){
        return new ResponseEntity<>(service.filter(pageIndex, pageSize, customerName, date, statusId), HttpStatus.OK);
    }

    @GetMapping(value = "/fillDataToInvoice/{projectCode}", name = "Tạo hóa đơn")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/fillDataToInvoice', 'GET', this)}")
    public ResponseEntity<DataToFillInvoiceDTO> fillDataToInvoice(@PathVariable String projectCode) {
        return new ResponseEntity<>(service.fillDataToInvoice(projectCode), HttpStatus.OK);
    }

    @GetMapping("/getByProject/{projectCode}")
    public ResponseEntity<InvoiceDTO> getByProject(@PathVariable String projectCode) {
        return new ResponseEntity<>(InvoiceDTO.from(service.getByProjectCode(projectCode)), HttpStatus.OK);
    }

    @GetMapping(value = "/{code}", name = "Xem chi tiết hóa đơn")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<DataToFillInvoiceDTO> getOne(@PathVariable String code){
        return new ResponseEntity<>(service.getOne(code), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> createData(@RequestBody InvoiceDTO dto){
        return new ResponseEntity<>(InvoiceDTO.from(service.create(dto)), HttpStatus.OK);
    }

    @PutMapping(value = "/{projectCode}", name = "Cập nhật hóa đơn")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'PUT', this)}")
    public ResponseEntity<String> update(@PathVariable String projectCode,
                                         @RequestBody DataToFillInvoiceDTO dto){
        return new ResponseEntity<>(service.update(dto, projectCode), HttpStatus.OK);
    }

    @GetMapping("/getAllPaymentStatus")
    public ResponseEntity<List<PaymentStatusDTO>> getAllPaymentStatus() {
        List<PaymentStatus> ls = statusService.getAll();
        List<PaymentStatusDTO> lsDto = ls.stream().map(PaymentStatusDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @GetMapping("/getOnePaymentStatus/{statusId}")
    public ResponseEntity<PaymentStatusDTO> getAllPaymentStatus(@PathVariable Long statusId) {
        return new ResponseEntity<>(PaymentStatusDTO.from(statusService.getOne(statusId)), HttpStatus.OK);
    }
}
