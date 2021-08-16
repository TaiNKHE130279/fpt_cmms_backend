package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.Province;
import com.cmms.demo.reponsitory.ProvinceRepository;
import com.cmms.demo.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private ProvinceRepository repository;

    @Override
    public List<Province> getAll(){return repository.findAll();}

    @Override
    public Province getOne(Long id){
        return repository.findById(id).orElse(null);
    }
}
