package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.MachineStatus;
import com.cmms.demo.domain.MachineType;
import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.dto.MachineDTO;
import com.cmms.demo.dto.MachineOutput;
import com.cmms.demo.reponsitory.DriverRepository;
import com.cmms.demo.reponsitory.MachineRepository;
import com.cmms.demo.service.MachineService;
import com.cmms.demo.specification.MachineSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachineServiceImpl implements MachineService {
    private Path root = Paths.get("images");
    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    DriverRepository driverRepository;


    public MachinePOJO convertToMachine(MachineDTO dto){
        MachinePOJO machine = new MachinePOJO();
        if(dto.getMachine_code() != null) {
            machine.setMachine_code(dto.getMachine_code());
        } else{
            int count = getMaxMachineCodeNumber();
            machine.setMachine_code("MC-"+ (count + 1));
        }
        machine.setMachine_name(dto.getMachine_name());
        machine.setLicense_plate(dto.getLicense_plate());
        if(dto.getMachine_image_file() != null) {
            try {
                String fileName = System.currentTimeMillis() + "_" + dto.getMachine_image_file().getOriginalFilename();
                Files.copy(dto.getMachine_image_file().getInputStream(), this.root.resolve(fileName));
                machine.setMachine_image(fileName);
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
        }
        machine.setMachine_detail(dto.getMachine_detail());
        if(dto.getMachine_load() != null) {
            machine.setMachine_load(dto.getMachine_load());
        }else{
            machine.setMachine_load(0);
        }
        MachineType type = new MachineType();
        type.setType_id(dto.getMachine_type().getType_id());
        type.setType_name(dto.getMachine_type().getType_name());
        machine.setMachineType(type);

        MachineStatus status = new MachineStatus();
        status.setId(dto.getStatusDTO().getId());
        status.setStatus_name(dto.getStatusDTO().getStatus_name());
        machine.setMachineStatus(status);
        machine.setActive(true);
        return machine;
    }

    public int getMaxMachineCodeNumber(){
        List<MachinePOJO> ls = machineRepository.findAll();
        List<Integer> integerLs = new ArrayList<>();
        for (int i= 0;i<ls.size();i++){
            String[] array = ls.get(i).getMachine_code().split("-");
            integerLs.add(Integer.parseInt(array[1].toString()));
        }
        return getMaxNumber(integerLs);
    }
    public int getMaxNumber(List<Integer> ls){
        if(ls.size() == 0){
            return 0;
        }
        int max = ls.get(0);
        for(int i=1; i<ls.size();i++){
            if(max < ls.get(i)){
                max=ls.get(i);
            }
        }
        return max;
    }

    @Override
    public MachinePOJO addMachine(MachineDTO dto){
        return machineRepository.save(convertToMachine(dto));
    }

    @Override
    public MachineOutput filter(Integer pageIndex, Integer pageSize, Float bottom, Float top, String name, Long typeId, boolean isActive){
        MachineOutput output = new MachineOutput();
        if(pageIndex == null && pageSize == null){
            List<MachinePOJO> ls = machineRepository.findAll(Specification.where(MachineSpecs.filter(bottom, top, name, typeId, isActive)));
            List<MachineDTO> lsDto = ls.stream().map(item -> MachineDTO.from(item, this)).collect(Collectors.toList());
            output.setTotalPages(0);
            output.setMachineList(lsDto);
        }else {
            Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
            Page<MachinePOJO> page = machineRepository.findAll(Specification.where(MachineSpecs.filter(bottom, top, name, typeId, isActive)), pageable);
            List<MachineDTO> lstMachineDto = page.stream().map(item -> MachineDTO.from(item, this))
                    .collect(Collectors.toList());
            output.setMachineList(lstMachineDto);
            output.setTotalPages(page.getTotalPages());
        }
        return output;
    }

    @Override
    public MachinePOJO getOne(String machineCode){
        MachinePOJO machine = machineRepository.findByMachine_code(machineCode);
        if(machine != null){
            return machine;
        }
        return null;
    }

    public List<MachinePOJO> getAll(){return machineRepository.findAll();}

    @Override
    public String getDriverCode(String machineCode){
        return machineRepository.getDriverCode(machineCode);
    }

    @Override
    public DriverPOJO getDriver(String machineCode){
        return driverRepository.getOne(getDriverCode(machineCode));
    }

    @Override
    public List<DriverPOJO> getDriverListNotAssignMachine(){
        return driverRepository.getDriverListNotAssignMachine();
    }

    @Override
    public MachinePOJO update(String code, MachineDTO item){
        MachinePOJO machine = getOne(code);
        if(machine != null){
            if(item.getMachine_name() != null) {
                machine.setMachine_name(item.getMachine_name());
            }
            if(item.getMachine_load() != null) {
                machine.setMachine_load(item.getMachine_load());
            }
            if(item.getMachine_image_file() != null) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + item.getMachine_image_file().getOriginalFilename();
                    Files.copy(item.getMachine_image_file().getInputStream(), this.root.resolve(fileName));
                    machine.setMachine_image(fileName);
                } catch (Exception e) {
                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                }
            }
            if(item.getMachine_detail() != null) {
                machine.setMachine_detail(item.getMachine_detail());
            }
            if(item.getLicense_plate() != null) {
                machine.setLicense_plate(item.getLicense_plate());
            }
            if(item.getMachine_type() != null) {
                MachineType type = new MachineType();
                type.setType_id(item.getMachine_type().getType_id());
                type.setType_name(item.getMachine_type().getType_name());
                machine.setMachineType(type);
            }
            if(item.getStatusDTO() != null){
                MachineStatus status = new MachineStatus();
                status.setId(item.getStatusDTO().getId());
                status.setStatus_name(item.getStatusDTO().getStatus_name());
                machine.setMachineStatus(status);
            }
            if(item.getDriver_code() != null){
                DriverPOJO d = driverRepository.findDriverByMachineCode(code);
                if(d!=null) {
                    driverRepository.updateMachineForDriver(null, d.getDrive_code());
                }
                int update = driverRepository.updateMachineForDriver(code, item.getDriver_code());
            }
            return machineRepository.save(machine);
        }
        return null;
    }

    @Override
    public MachinePOJO checkLicensePlateUnique(String licensePlate){
        List<MachinePOJO> ls = getAll();
        for(int i=0; i<ls.size(); i++){
            MachinePOJO m = ls.get(i);
            if(m.getLicense_plate().equals(licensePlate)){
                return m;
            }
        }
        return null;
    }

    @Override
    public MachineDTO delete(String code){
        MachinePOJO m = getOne(code);
        MachineDTO dto = MachineDTO.from(m, this);
        if(m != null){
            if(driverRepository.findDriverByMachineCode(code)!= null){
                DriverPOJO d = driverRepository.findDriverByMachineCode(code);
                int updateDriverMachine = driverRepository.updateMachineForDriver(null, d.getDrive_code());
            }
            m.setActive(false);
            machineRepository.save(m);
            return dto;
        }
        return dto;
    }

}
