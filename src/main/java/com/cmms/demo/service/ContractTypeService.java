package com.cmms.demo.service;

import com.cmms.demo.domain.ContractType;
import com.cmms.demo.dto.ContractTypeDTO;

import java.util.List;

public interface ContractTypeService {
    ContractType add (ContractTypeDTO dto);

    ContractType getOne(Long id);

    List<ContractType> getAll();

    ContractType update(ContractTypeDTO dto);
}
