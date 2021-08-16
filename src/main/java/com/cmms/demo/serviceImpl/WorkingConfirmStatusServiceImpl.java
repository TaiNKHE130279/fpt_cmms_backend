package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.WorkingConfirmStatus;
import com.cmms.demo.reponsitory.WorkingConfirmStatusRepository;
import com.cmms.demo.service.WorkingConfirmStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkingConfirmStatusServiceImpl implements WorkingConfirmStatusService {
    @Autowired
    private WorkingConfirmStatusRepository repository;

    @Override
    public List<WorkingConfirmStatus> getAll(){return repository.findAll();}
@Override
    public WorkingConfirmStatus getOne(Long id){
        return repository.findById(id).orElse(null);
    }
}
