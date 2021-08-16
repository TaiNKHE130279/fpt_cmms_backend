package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.District;
import com.cmms.demo.reponsitory.DistrictRepository;
import com.cmms.demo.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository repository;

    @Override
    public List<District> getAll(){return repository.findAll();}

    @Override
    public District getOne(Long id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<District> getAllByProvince(Long provinceId){
        return repository.getAllByProvince(provinceId);
    }

}
