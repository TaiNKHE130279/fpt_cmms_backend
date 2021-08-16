package com.cmms.demo.endpoint;

import com.cmms.demo.domain.PaymentType;
import com.cmms.demo.dto.DailyFinanceReportDto;
import com.cmms.demo.dto.DailyFinanceReportOutput;
import com.cmms.demo.dto.PaymentTypeDto;
import com.cmms.demo.service.DailyFinanceReportService;
import com.cmms.demo.service.PaymentTypeService;
import com.cmms.demo.serviceImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/dailyFinanceRp", name = "Quản lý sổ quỹ")
public class DailyFinanceReportController {

    @Autowired
    private DailyFinanceReportService dailyFinanceReportServiceImpl;
    @Autowired
    private PaymentTypeService paymentTypeServiceImpl;

    @GetMapping("/getAllType")
    public ResponseEntity<List<PaymentTypeDto>> getAllType(){
        List<PaymentType> paymentType = paymentTypeServiceImpl.getAll();
        List<PaymentTypeDto> dtoList =
                paymentType.stream().map(PaymentTypeDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/getOneType/{id}")
    public ResponseEntity<PaymentTypeDto> getOneType(@PathVariable final int id){
        PaymentTypeDto paymentType = PaymentTypeDto.from(paymentTypeServiceImpl.getOne(id));
        return new ResponseEntity<>(paymentType, HttpStatus.OK);
    }

    @GetMapping(name = "Xem danh sách phiếu thu/chi")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<DailyFinanceReportOutput> filter(@RequestParam("page") int pageIndex,
                                                           @RequestParam("limit") int pageSize,
                                                           @RequestParam(value = "type", required = false) Integer typeId,
                                                           @RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to,
                                                           @RequestParam(value = "title", required = false) String title){
        return new ResponseEntity<>(dailyFinanceReportServiceImpl.filter(pageIndex, pageSize, typeId, from, to, title), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DailyFinanceReportDto> getOne(@PathVariable Long id){
        return new ResponseEntity<>(dailyFinanceReportServiceImpl.getOne(id), HttpStatus.OK);
    }

    @PostMapping(name = "Tạo phiếu thu/chi")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<DailyFinanceReportDto> addDailyFinanceReport(@RequestBody final DailyFinanceReportDto dto){
        return new ResponseEntity<>(dailyFinanceReportServiceImpl.addDailyReport(dto), HttpStatus.OK);
    }

    @PutMapping(name = "Cập nhật phiếu thu/chi")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'PUT', this)}")
    public ResponseEntity<DailyFinanceReportDto> updateDailyFinanceRp(@RequestBody final DailyFinanceReportDto dto){
        return new ResponseEntity<>(dailyFinanceReportServiceImpl.updateDailyFinanceRp(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", name = "Xóa phiếu thu/chi")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'DELETE', this)}")
    public ResponseEntity<String> deleteDailyFinanceRp(@PathVariable final Long id){
        return new ResponseEntity<>(dailyFinanceReportServiceImpl.deleteDailyFinanceRp(id), HttpStatus.OK);
    }

}
