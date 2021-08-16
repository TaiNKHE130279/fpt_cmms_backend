package com.cmms.demo.service;

import com.cmms.demo.domain.MachineType;

import java.util.List;

public interface MachineTypeService {
    MachineType add(MachineType machineType);

    List<MachineType> getAllMachineType();

    MachineType getOne(Long id);

    MachineType update(Long id, MachineType item);

    MachineType delete(Long id);
}
