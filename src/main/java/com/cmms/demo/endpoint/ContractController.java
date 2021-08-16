package com.cmms.demo.endpoint;

import com.cmms.demo.domain.Contract;
import com.cmms.demo.dto.ContractDTO;
import com.cmms.demo.dto.ContractOutput;
import com.cmms.demo.service.ContractService;
import com.cmms.demo.serviceImpl.ContractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/contract",name = "Quản lý hợp đồng")
public class ContractController {
    @Autowired
    private ContractService service;


    @PostMapping(name = "Tạo hợp đồng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO dto) {
        Contract c = service.createContract(dto);
        return new ResponseEntity<>(ContractDTO.from(c), HttpStatus.OK);
    }

    @GetMapping(value = "/getOne/{code}",name = "Xem chi tiết một hợp đồng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '/getOne', 'GET', this)}")
    public ResponseEntity<ContractDTO> getOne(@PathVariable String code) {
        Contract c = service.getOne(code);
        return new ResponseEntity<>(ContractDTO.from(c), HttpStatus.OK);
    }

    @GetMapping(name = "Xem danh sách hợp đồng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<ContractOutput> filter(@RequestParam("page") int pageIndex,
                                                 @RequestParam("limit") int pageSize,
                                                 @RequestParam(value = "customerName", required = false) String customerName,
                                                 @RequestParam(value = "date", required = false) String date,
                                                 @RequestParam(value = "statusId", required = false) Long statusId,
                                                 @RequestParam(value = "isActive") boolean isActive) {
        return new ResponseEntity<>(service.filter(pageIndex, pageSize, customerName, date, statusId, isActive), HttpStatus.OK);
    }

    @PutMapping(name = "Cập nhật thông tin hợp đồng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'PUT', this)}")
    public ResponseEntity<ContractDTO> update(@RequestBody ContractDTO dto) {
        Contract c = service.update(dto);
        return new ResponseEntity<>(ContractDTO.from(c), HttpStatus.OK);
    }

    @GetMapping("/getOneByProjectCode/{projectCode}")
    public ResponseEntity<ContractDTO> getOneByProjectCode(@PathVariable String projectCode) {
        return new ResponseEntity<>(ContractDTO.from(service.getOneByProjectCode(projectCode)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{contractCode}", name = "Hủy hợp đồng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'DELETE', this)}")
    public ResponseEntity<Integer> cancelContract(@PathVariable String contractCode) {
        return new ResponseEntity<>(service.cancelContract(contractCode), HttpStatus.OK);
    }
}
