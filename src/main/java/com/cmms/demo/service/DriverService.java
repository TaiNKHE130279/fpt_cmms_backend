package com.cmms.demo.service;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.dto.DriverDTO;
import com.cmms.demo.dto.DriverOutput;
import com.cmms.demo.dto.ParamToUpdateDriverInfoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DriverService {
    DriverOutput filter(Integer pageIndex, Integer pageSize, String name, Long bottom, Long top, String address, boolean isActive, String phone);

    DriverPOJO getOne(String code);

    DriverPOJO getByUserID(Long userId);

    String generateUsername(String name);

    DriverPOJO addDriver(DriverDTO item);

    List<DriverPOJO> getDriverListAssignMachine();

    List<MachinePOJO> getMachineListNotAssign();

    DriverPOJO checkPhoneUnique(String phone);

    DriverPOJO update(ParamToUpdateDriverInfoDto dto);

    DriverDTO delete(String code);
}
