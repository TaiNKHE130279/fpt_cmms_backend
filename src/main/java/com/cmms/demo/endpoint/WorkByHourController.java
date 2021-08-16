package com.cmms.demo.endpoint;

import com.cmms.demo.domain.WorkByHourPOJO;
import com.cmms.demo.dto.WorkByHourDTO;
import com.cmms.demo.service.WorkByHourService;
import com.cmms.demo.serviceImpl.WorkByHourServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workByHour")
@CrossOrigin("*")
public class WorkByHourController {

    @Autowired
    WorkByHourService workByHourServiceImpl;

    @GetMapping("/getOne/{id}")
    public ResponseEntity<WorkByHourDTO> getOne(@PathVariable Long id) {
        WorkByHourPOJO i = workByHourServiceImpl.getOne(id);
        return new ResponseEntity<>(WorkByHourDTO.from(i), HttpStatus.OK);
    }

    @GetMapping("/getByProject/{projectCode}")
    public ResponseEntity<WorkByHourDTO> getByProject(@PathVariable String projectCode) {
        return new ResponseEntity<>(WorkByHourDTO.from(workByHourServiceImpl.getByProjectCode(projectCode)), HttpStatus.OK);
    }

    @PostMapping
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'CREATE', this)}")
    public ResponseEntity<WorkByHourDTO> create(@RequestBody WorkByHourDTO dto) {
        WorkByHourPOJO workByHourPOJO = workByHourServiceImpl.create(dto);
        return new ResponseEntity<>(WorkByHourDTO.from(workByHourPOJO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WorkByHourDTO> update(@RequestBody WorkByHourDTO dto) {
        WorkByHourPOJO i = workByHourServiceImpl.update(dto);
        return new ResponseEntity<>(WorkByHourDTO.from(i), HttpStatus.OK);
    }
}
