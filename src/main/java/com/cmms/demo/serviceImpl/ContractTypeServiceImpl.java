package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.ContractType;
import com.cmms.demo.dto.ContractTypeDTO;
import com.cmms.demo.reponsitory.ContractTypeRepository;
import com.cmms.demo.service.ContractTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ContractTypeServiceImpl implements ContractTypeService {
    @Autowired
    private ContractTypeRepository repository;

    @Override
    public ContractType add(ContractTypeDTO dto) {
        ContractType type = new ContractType();
        type.setType_name(dto.getType_name());
        dto.setFile_path(dto.getFile_path());
        return repository.save(type);
    }

    @Override
    public ContractType getOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ContractType> getAll() {
        return repository.findAll();
    }

    @Override
    public ContractType update(ContractTypeDTO dto) {
        ContractType item = getOne(dto.getType_id());
        if (item != null) {
            if (dto.getType_name() != null) {
                item.setType_name(dto.getType_name());
            }
            if (dto.getFile_path() != null) {
                item.setFile_path(dto.getFile_path());
            }
            return repository.save(item);
        }
        return null;
    }
}
