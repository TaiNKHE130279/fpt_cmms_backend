package com.cmms.demo.endpoint;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.dto.DriverDTO;
import com.cmms.demo.dto.MachineDTO;
import com.cmms.demo.dto.MachineOutput;
import com.cmms.demo.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/machine", name = "Quản lý máy")
public class MachineController {

    @Autowired
    private MachineService machineServiceImpl;

    @PostMapping(name = "Thêm máy")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<MachineDTO> addMachine(@RequestBody final MachineDTO dto){
        MachinePOJO machine = machineServiceImpl.addMachine(dto);
        return new ResponseEntity<>(MachineDTO.from(machine, machineServiceImpl), HttpStatus.OK);
    }

    @GetMapping(name = "Xem danh sách máy")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<MachineOutput> filter(@RequestParam(value = "page", required = false) Integer pageIndex,
                                                @RequestParam(value = "limit", required = false) Integer pageSize,
                                                @RequestParam(value = "bottom", required = false) Float bottom,
                                                @RequestParam(value = "top", required = false) Float top,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "type", required = false) Long typeId,
                                                @RequestParam(value = "isActive") boolean isActive){
        return new ResponseEntity<>(machineServiceImpl.filter(pageIndex,pageSize,bottom,top,name, typeId, isActive), HttpStatus.OK);
    }

    @GetMapping(value = "/getOne/{code}", name = "Xem chi tiết máy")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/getOne', 'GET', this)}")
    public ResponseEntity<MachineDTO> getOne(@PathVariable final String code){
        MachinePOJO machine = machineServiceImpl.getOne(code);
        return new ResponseEntity<>(MachineDTO.from(machine, machineServiceImpl), HttpStatus.OK);
    }

    @PutMapping(value = "/{code}", name = "Cập nhật thông tin máy")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'PUT', this)}")
    public ResponseEntity<MachineDTO> update(@PathVariable final  String code, @RequestBody MachineDTO dto){
        MachinePOJO machine = machineServiceImpl.update(code, dto);
        return new ResponseEntity<>(MachineDTO.from(machine, machineServiceImpl), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{code}", name = "Xóa máy")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'DELETE', this)}")
    public ResponseEntity<MachineDTO> delete(@PathVariable final String code){
        MachineDTO message = machineServiceImpl.delete(code);
        if(message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping("/getAssignDriverLst")
    public ResponseEntity<List<DriverDTO>> getDriverListNotAssignMachine(){
        List<DriverPOJO> driverLs = machineServiceImpl.getDriverListNotAssignMachine();
        List<DriverDTO> dtoList = driverLs.stream().map(DriverDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/checkLicensePlateUnique")
    public ResponseEntity<MachineDTO> checkLicensePlateUnique(@RequestParam("licensePlate") String licensePlate){
        MachinePOJO m = machineServiceImpl.checkLicensePlateUnique(licensePlate);
        if(m!=null) {
            return new ResponseEntity<>(MachineDTO.from(m, machineServiceImpl), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }


}
