package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.HolidayType;
import com.cmms.demo.reponsitory.HolidayTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayTypeService {
    @Autowired
    private HolidayTypeRepository repository;
    public HolidayType getOne(Long id){
        return repository.findById(id).orElse(null);
    }
}
