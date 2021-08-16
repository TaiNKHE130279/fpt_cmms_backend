package com.cmms.demo.endpoint;

import com.cmms.demo.domain.MachineType;
import com.cmms.demo.dto.MachineTypeDTO;
import com.cmms.demo.service.MachineTypeService;
import com.cmms.demo.serviceImpl.MachineTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/machineType")
public class MachineTypeController {
    @Autowired
    private MachineTypeService service;

    @PostMapping
    public ResponseEntity<MachineType> addMachineType(@RequestBody final MachineTypeDTO dto){
        MachineType type = service.add(MachineType.from(dto));
        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<MachineTypeDTO>> getAll(){
        List<MachineType> lstMachineType = service.getAllMachineType();
        List<MachineTypeDTO> lstDto = lstMachineType.stream().map(MachineTypeDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lstDto, HttpStatus.OK);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<MachineTypeDTO> getOne(@PathVariable final Long id){
        MachineTypeDTO dto = MachineTypeDTO.from(service.getOne(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{typeId}")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, 'MACHINE', this)}")
    public ResponseEntity<MachineType> update(@PathVariable final Long typeId, @RequestBody final MachineTypeDTO dto){
        MachineType machineType = service.update(typeId, MachineType.from(dto));
        return new ResponseEntity<>(machineType, HttpStatus.OK);
    }

    @DeleteMapping("/{typeId}")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, 'MACHINE', this)}")
    public ResponseEntity<MachineType> delete(@PathVariable final Long typeId){
        MachineType machineType = service.delete(typeId);
        return new ResponseEntity<>(machineType, HttpStatus.OK);
    }
}
