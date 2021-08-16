package com.cmms.demo.service;

import com.cmms.demo.domain.WorkingConfirmStatus;

import java.util.List;

public interface WorkingConfirmStatusService {
    List<WorkingConfirmStatus> getAll();

    WorkingConfirmStatus getOne(Long id);
}
