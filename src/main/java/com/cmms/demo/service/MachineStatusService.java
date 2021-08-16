package com.cmms.demo.service;

import com.cmms.demo.domain.MachineStatus;

import java.util.List;

public interface MachineStatusService {
    MachineStatus getOne(int id);

    List<MachineStatus> getAll();
}
