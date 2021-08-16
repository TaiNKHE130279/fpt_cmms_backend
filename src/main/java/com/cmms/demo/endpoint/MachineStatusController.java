package com.cmms.demo.endpoint;

import com.cmms.demo.domain.MachineStatus;
import com.cmms.demo.dto.MachineStatusDTO;
import com.cmms.demo.service.MachineStatusService;
import com.cmms.demo.serviceImpl.MachineStatusServiceImpl;
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
@RequestMapping("/api/v1/machineStatus")
public class MachineStatusController {
    @Autowired
    private MachineStatusService service;

    @GetMapping
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, 'MACHINE', this)}")
    public ResponseEntity<List<MachineStatusDTO>> getAll(){
        List<MachineStatus> lstMachineType = service.getAll();
        List<MachineStatusDTO> lstDto = lstMachineType.stream().map(MachineStatusDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lstDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, 'MACHINE', this)}")
    public ResponseEntity<MachineStatusDTO> getOne(@PathVariable final int id){
        MachineStatusDTO dto = MachineStatusDTO.from(service.getOne(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
