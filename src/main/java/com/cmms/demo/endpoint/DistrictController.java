package com.cmms.demo.endpoint;

import com.cmms.demo.domain.District;
import com.cmms.demo.dto.DistrictDTO;
import com.cmms.demo.service.DistrictService;
import com.cmms.demo.serviceImpl.DistrictServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/district")
public class DistrictController {
    @Autowired
    private DistrictService service;

    @GetMapping
    public ResponseEntity<List<DistrictDTO>> getAll(){
        List<District> ls = service.getAll();
        List<DistrictDTO> output = ls.stream().map(DistrictDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictDTO> getOne(@PathVariable Long id){
        return new ResponseEntity<>(DistrictDTO.from(service.getOne(id)), HttpStatus.OK);
    }

    @GetMapping("/getByProvince/{provinceId}")
    public ResponseEntity<List<DistrictDTO>> getAllByProvince(@PathVariable Long provinceId){
        List<District> ls = service.getAllByProvince(provinceId);
        List<DistrictDTO> output = ls.stream().map(DistrictDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
