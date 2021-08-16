package com.cmms.demo.endpoint;

import com.cmms.demo.domain.Commune;
import com.cmms.demo.dto.CommuneDTO;
import com.cmms.demo.service.CommuneService;
import com.cmms.demo.serviceImpl.CommuneServiceImpl;
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
@RequestMapping("/api/v1/commune")
public class CommuneController {
    @Autowired
    private CommuneService service;

    @GetMapping
    public ResponseEntity<List<CommuneDTO>> getAll(){
        List<Commune> ls = service.getAll();
        List<CommuneDTO> output = ls.stream().map(CommuneDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommuneDTO> getOne(@PathVariable Long id){
        return new ResponseEntity<>(CommuneDTO.from(service.getOne(id)), HttpStatus.OK);
    }

    @GetMapping("/getByDistrict/{districtId}")
    public ResponseEntity<List<CommuneDTO>> getAllByDistrict(@PathVariable Long districtId){
        List<Commune> ls = service.getAllByDistrict(districtId);
        List<CommuneDTO> output = ls.stream().map(CommuneDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
