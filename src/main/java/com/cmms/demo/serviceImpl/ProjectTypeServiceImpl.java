package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.ProjectType;
import com.cmms.demo.dto.ProjectTypeDTO;
import com.cmms.demo.reponsitory.ProjectTypeRepository;
import com.cmms.demo.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTypeServiceImpl implements ProjectTypeService {
    @Autowired
    private ProjectTypeRepository repository;

    public ProjectType add(ProjectTypeDTO dto){
        ProjectType type = new ProjectType();
        if(dto.getType_id() != null){
            type.setType_id(dto.getType_id());
        }
        type.setType_name(dto.getType_name());
        return repository.save(type);
    }

    @Override
    public List<ProjectType> getAll(){
        return repository.findAll();
    }

    @Override
    public ProjectType getOne(Long id){
        return repository.findById(id).orElse(null);
    }

    public ProjectType update(Long id, String typeName){
        ProjectType item = getOne(id);
        if(item != null){
            item.setType_name(typeName);
            return repository.save(item);
        }
        return null;
    }
}
