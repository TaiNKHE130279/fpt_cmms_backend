package com.cmms.demo.endpoint;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.dto.DriverDTO;
import com.cmms.demo.dto.DriverOutput;
import com.cmms.demo.dto.MachineDTO;
import com.cmms.demo.dto.ParamToUpdateDriverInfoDto;
import com.cmms.demo.service.DriverService;
import com.cmms.demo.serviceImpl.MachineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/driver", name = "Quản lý lái xe")
public class DriverController {
    @Autowired
    private DriverService service;
    @Autowired
    private MachineServiceImpl machineServiceImpl;


    @PostMapping(value = "/add", name = "Thêm lái xe")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<DriverDTO> addDriver(@RequestBody final DriverDTO dto){
        DriverPOJO driver = service.addDriver(dto);
        return new ResponseEntity<>(DriverDTO.from(driver), HttpStatus.OK);
    }

    @PostMapping("/generate-username")
    public ResponseEntity<DriverDTO> generateAccount(@RequestBody final ParamToUpdateDriverInfoDto dto){
        String userName = service.generateUsername(dto.getName());
        DriverDTO output = new DriverDTO();
        output.setAccount(userName);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PutMapping(name = "Cập nhật thông tin lái xe")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'UPDATE', this)}")
    public ResponseEntity<DriverDTO> updateInfo(@RequestBody final ParamToUpdateDriverInfoDto dto){
        DriverPOJO driver = service.update(dto);
        return new ResponseEntity<>(DriverDTO.from(driver), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{code}",name = "Xóa lái xe")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'DELETE', this)}")
    public ResponseEntity<DriverDTO> delete(@PathVariable final String code){
        DriverDTO d = service.delete(code);
        if(d != null) {
            return new ResponseEntity<>(d, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping(name = "Xem danh sách lái xe")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<DriverOutput> filter(@RequestParam(value = "page", required = false) Integer pageIndex,
                                               @RequestParam(value = "limit", required = false) Integer pageSize,
                                               @RequestParam(value = "bottom", required = false) Long bottom,
                                               @RequestParam(value = "top", required = false) Long top,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "address", required = false) String address,
                                               @RequestParam("isActive") boolean isActive,
                                               @RequestParam(value = "phone", required = false) String phone){
        return new ResponseEntity<>(service.filter(pageIndex, pageSize, name, bottom, top, address, isActive, phone), HttpStatus.OK);
    }

    @GetMapping("/checkPhoneUnique")
    public ResponseEntity<DriverDTO> checkPhoneUnique(@RequestParam("phone") String phone){
        DriverPOJO d = service.checkPhoneUnique(phone);
        if(d!=null) {
            return new ResponseEntity<>(DriverDTO.from(d), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }


    @GetMapping(value = "/getOne/{code}", name = "Xem chi tiết lái xe")
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/getOne', 'GET', this)}")
    public ResponseEntity<DriverDTO> getOne(@PathVariable final String code){
        DriverDTO dto = DriverDTO.from(service.getOne(code));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getOneByUserID/{userId}")
    public ResponseEntity<DriverDTO> getDriverByUserId(@PathVariable final Long userId){
        DriverDTO dto = DriverDTO.from(service.getByUserID(userId));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getAssignMachineLst")
    public ResponseEntity<List<MachineDTO>> getMachineListNotAssign(){
        List<MachinePOJO> machineLst = service.getMachineListNotAssign();
        List<MachineDTO> dtoLst = machineLst.stream().map(item -> MachineDTO.from(item, machineServiceImpl))
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoLst, HttpStatus.OK);
    }

    @GetMapping("/getDriverListAssignMachine")
    public ResponseEntity<List<DriverDTO>> getDriverListAssignMachine(){
        List<DriverPOJO> machineLst = service.getDriverListAssignMachine();
        List<DriverDTO> dtoLst = machineLst.stream().map(DriverDTO::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoLst, HttpStatus.OK);
    }
}
