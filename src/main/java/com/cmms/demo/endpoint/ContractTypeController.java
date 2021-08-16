package com.cmms.demo.endpoint;

import com.cmms.demo.domain.ContractType;
import com.cmms.demo.dto.ContractTypeDTO;
import com.cmms.demo.service.ContractTypeService;
import com.cmms.demo.serviceImpl.ContractTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/contractType",name = "Quản lý kiểu hợp đồng")
public class ContractTypeController {
    @Autowired
    private ContractTypeService service;

    @PostMapping
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'CONTRACT', this)}")
    public ResponseEntity<ContractTypeDTO> addContractType(@RequestBody ContractTypeDTO dto) {
        ContractType type = service.add(dto);
        return new ResponseEntity<>(ContractTypeDTO.from(type), HttpStatus.OK);
    }

    @GetMapping
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'CONTRACT', this)}")
    public ResponseEntity<List<ContractTypeDTO>> getAllContractType() {
        List<ContractType> lsType = service.getAll();
        List<ContractTypeDTO> lsDto = lsType.stream().map(ContractTypeDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @GetMapping("/getOne/{id}")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'CONTRACT', this)}")
    public ResponseEntity<ContractTypeDTO> getAllContractType(@PathVariable Long id) {
        ContractType type = service.getOne(id);
        return new ResponseEntity<>(ContractTypeDTO.from(type), HttpStatus.OK);
    }

    @PutMapping
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'CONTRACT', this)}")
    public ResponseEntity<ContractTypeDTO> update(@RequestBody ContractTypeDTO dto) {
        ContractType type = service.update(dto);
        return new ResponseEntity<>(ContractTypeDTO.from(type), HttpStatus.OK);
    }
}
