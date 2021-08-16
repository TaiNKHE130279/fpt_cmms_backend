package com.cmms.demo.dto;

import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.service.MachineService;
import com.cmms.demo.serviceImpl.MachineServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDTO {
    private String machine_code;
    private String machine_name;
    private String license_plate;
    private String machine_image;
    private String machine_detail;
    private Float machine_load;
    private MachineTypeDTO machine_type;
    private String driver_code;
    private String driver_name;
    private MachineStatusDTO statusDTO;
    private MultipartFile machine_image_file;

    public static MachineDTO from(MachinePOJO machine, MachineService service){
        MachineDTO dto = new MachineDTO();
        dto.setMachine_code(machine.getMachine_code());
        dto.setMachine_name(machine.getMachine_name());
        dto.setLicense_plate(machine.getLicense_plate());
        dto.setMachine_image(machine.getMachine_image());
        dto.setMachine_detail(machine.getMachine_detail());
        dto.setMachine_load(machine.getMachine_load());

        MachineTypeDTO type = new MachineTypeDTO();
        type.setType_id(machine.getMachineType().getType_id());
        type.setType_name(machine.getMachineType().getType_name());
        dto.setMachine_type(type);

        MachineStatusDTO statusDTO = new MachineStatusDTO();
        statusDTO.setId(machine.getMachineStatus().getId());
        statusDTO.setStatus_name(machine.getMachineStatus().getStatus_name());
        dto.setStatusDTO(statusDTO);
        if(service.getDriverCode(machine.getMachine_code()) != null) {
            dto.setDriver_code(service.getDriverCode(machine.getMachine_code()));
            dto.setDriver_name(service.getDriver(machine.getMachine_code()).getName());
        }
        return dto;
    }

}
