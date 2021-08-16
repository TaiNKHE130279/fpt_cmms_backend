package com.cmms.demo.endpoint;

import com.cmms.demo.domain.BonusPOJO;
import com.cmms.demo.domain.Payroll;
import com.cmms.demo.domain.PayrollDetail;
import com.cmms.demo.domain.PayrollOutput;
import com.cmms.demo.dto.BonusDTO;
import com.cmms.demo.dto.PayrollDTO;
import com.cmms.demo.dto.PayrollDetailDTO;
import com.cmms.demo.service.PayrollService;
import com.cmms.demo.serviceImpl.PayrollServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/payroll", name = "Quản lý lương")
public class PayrollController {
    @Autowired
    PayrollService service;

    @GetMapping(value = "/feature", name = "Xem tất cả bản lương")
    public ResponseEntity<PayrollOutput> viewAll(@RequestParam(value = "page") int pageIndex,
                                                 @RequestParam(value = "limit") int pageSize,
                                                 @RequestParam(value = "month", required = false) Integer month,
                                                 @RequestParam(value = "name", required = false) String payroll_name,
                                                 @RequestParam(value = "year", required = false) Integer year,
                                                 @RequestParam(value = "status", required = false) Long status) {
        Page<Payroll> payrollPage = service.filter(pageIndex, pageSize, payroll_name, month, year, status);
        List<Payroll> payrolls = payrollPage.stream().filter(payroll -> payroll.getStatus().getId() != 4L).map(payroll -> payroll).collect(Collectors.toList());
        ;
        PayrollOutput payrollOutput = new PayrollOutput(payrollPage.getTotalPages(), payrolls.stream().map(PayrollDTO::from).collect(Collectors.toList()));
        return new ResponseEntity<>(payrollOutput, HttpStatus.OK);
    }

    @PostMapping(value = "/feature/create", name = "Tạo bảng lương")
    public ResponseEntity<PayrollDTO> create(@RequestBody PayrollDTO dto) {
        if (dto.getDate_start() != null && dto.getDate_end() != null) {
            return new ResponseEntity<>(PayrollDTO.from(service.create(dto)), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(value = "/feature/temporary-save/{id}", name = "Lưu tạm")
    public ResponseEntity<PayrollDTO> temporarySave(@RequestBody PayrollDTO dto, @PathVariable Long id) {
        Payroll payroll = service.updatePayroll(dto, id);
        return new ResponseEntity<>(PayrollDTO.from(payroll), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/salary-closing", name = "Chốt lương")
    public ResponseEntity<PayrollDTO> salaryClosing(@RequestBody PayrollDTO dto) {
        Payroll payroll = service.salaryClosing(dto);
        return new ResponseEntity<>(PayrollDTO.from(payroll), HttpStatus.OK);
    }

    @GetMapping(value = "/feature/get-one-payroll/{id}")
    public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable Long id) {
        Payroll payroll = service.getOnePayroll(id);
        return new ResponseEntity<>(PayrollDTO.from(payroll), HttpStatus.OK);
    }

    @GetMapping(value = "/feature/detail/{id}")
    public ResponseEntity<PayrollDetailDTO> detailPayroll(@PathVariable Long id) {
        PayrollDetail detail = service.getOneDetail(id);
        return new ResponseEntity<>(PayrollDetailDTO.from(detail), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/reset-data/{id}", name = "Tạo lại dữ liệu")
    public ResponseEntity<PayrollDTO> updateDataPayroll(@PathVariable Long id) {
        Payroll payroll = service.updateDataPayroll(id);
        return new ResponseEntity<>(PayrollDTO.from(payroll), HttpStatus.OK);
    }

    @GetMapping(value = "/feature/all-detail/{id}")
    public ResponseEntity<List<PayrollDetailDTO>> detailOfPayroll(@PathVariable Long id) {
        List<PayrollDetail> details = service.getAllDetail(id);
        return new ResponseEntity<>(details.stream().map(PayrollDetailDTO::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/payment/{id}")
    public ResponseEntity<PayrollDetailDTO> payment(@PathVariable("id") Long id, @RequestParam Long value) {
        PayrollDetail detail = service.paymentPayrollDetail(id, value);
        return new ResponseEntity<>(PayrollDetailDTO.from(detail), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/delete-payroll/{id}", name = "Xóa bảng lương")
    public ResponseEntity<PayrollDTO> deletePayment(@PathVariable("id") Long id) {
        Payroll payroll = service.deletePayroll(id);
        return new ResponseEntity<>(PayrollDTO.from(payroll), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/update-detail/{id}")
    public ResponseEntity<List<PayrollDetailDTO>> updateDetail(@PathVariable Long id, List<PayrollDetailDTO> dtos) {
        List<PayrollDetail> details = dtos.stream().map(payrollDetailDTO -> service.updateDetail(id, payrollDetailDTO)).collect(Collectors.toList());
        return new ResponseEntity<>(details.stream().map(detail -> PayrollDetailDTO.from(detail)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/update-one-detail/{detailId}")
    public ResponseEntity<PayrollDetailDTO> updateOneDetail(@PathVariable Long detailId, @RequestBody PayrollDetailDTO detailDTO) {
        PayrollDetail detail = service.updateOneDetail(detailId, detailDTO);
        return new ResponseEntity<>(PayrollDetailDTO.from(detail), HttpStatus.OK);
    }

    @PostMapping(value = "/feature/remove-detail/{id}", name = "Xóa phiếu lương của bảng lương")
    public ResponseEntity<PayrollDetailDTO> removeOneDetail(@PathVariable Long id) {
        PayrollDetail detail = service.removeDetail(id);
        return new ResponseEntity<>(PayrollDetailDTO.from(detail), HttpStatus.OK);
    }

    @PostMapping(value = "/payroll-detail-one-driver/{driver_code}")
    public ResponseEntity<PayrollDetailDTO> getOneDetailOfDriver(@PathVariable String driver_code, @RequestParam Long payroll_id) {
        PayrollDetail detail = service.getOneDetailOfDriver(driver_code, payroll_id);
        return new ResponseEntity<>(PayrollDetailDTO.from(detail), HttpStatus.OK);
    }

    @GetMapping(value = "/payrolls-has-driver/{driver_id}", name = "Phiếu lương nhân viên")
    public ResponseEntity<List<PayrollDTO>> AllPayrollHasDriver(@PathVariable String driver_id) {
        List<Payroll> payrolls = service.findAllPayrollHasDriver(driver_id);
        return new ResponseEntity<>(payrolls.stream().map(PayrollDTO::from).collect(Collectors.toList()), HttpStatus.OK);

    }
}
