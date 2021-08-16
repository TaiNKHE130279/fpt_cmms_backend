package com.cmms.demo.service;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.dto.MachineDTO;
import com.cmms.demo.dto.MachineOutput;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MachineService {
    MachinePOJO addMachine(MachineDTO dto);

    MachineOutput filter(Integer pageIndex, Integer pageSize, Float bottom, Float top, String name, Long typeId, boolean isActive);

    MachinePOJO getOne(String machineCode);

    String getDriverCode(String machineCode);

    DriverPOJO getDriver(String machineCode);

    List<DriverPOJO> getDriverListNotAssignMachine();

    MachinePOJO update(String code, MachineDTO item);

    MachinePOJO checkLicensePlateUnique(String licensePlate);

    MachineDTO delete(String code);
}
