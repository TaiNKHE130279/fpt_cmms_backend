package com.cmms.demo.service;

import com.cmms.demo.domain.Commune;

import java.util.List;

public interface CommuneService {
    List<Commune> getAll();

    Commune getOne(Long id);

    List<Commune> getAllByDistrict(Long districtId);
}
