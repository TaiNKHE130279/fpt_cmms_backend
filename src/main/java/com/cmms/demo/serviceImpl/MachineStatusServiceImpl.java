package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.MachineStatus;
import com.cmms.demo.reponsitory.MachineStatusRepository;
import com.cmms.demo.service.MachineStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineStatusServiceImpl implements MachineStatusService {
    @Autowired
    private MachineStatusRepository repository;

    @Override
    public MachineStatus getOne(int id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<MachineStatus> getAll(){return repository.findAll();}
}
