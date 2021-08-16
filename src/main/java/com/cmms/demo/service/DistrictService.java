package com.cmms.demo.service;

import com.cmms.demo.domain.District;

import java.util.List;

public interface DistrictService {
    List<District> getAll();

    District getOne(Long id);

    List<District> getAllByProvince(Long provinceId);
}
