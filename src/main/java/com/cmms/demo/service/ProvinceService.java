package com.cmms.demo.service;

import com.cmms.demo.domain.Province;

import java.util.List;

public interface ProvinceService {
    List<Province> getAll();

    Province getOne(Long id);
}
