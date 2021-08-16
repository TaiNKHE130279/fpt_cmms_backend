package com.cmms.demo.serviceImpl;


import com.cmms.demo.domain.Commune;
import com.cmms.demo.reponsitory.CommuneRepository;
import com.cmms.demo.service.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommuneServiceImpl implements CommuneService {
    @Autowired
    private CommuneRepository repository;

    @Override
    public List<Commune> getAll(){return repository.findAll();}

    @Override
    public Commune getOne(Long id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Commune> getAllByDistrict(Long districtId){
        return repository.getAllByDistrict(districtId);
    }
}
