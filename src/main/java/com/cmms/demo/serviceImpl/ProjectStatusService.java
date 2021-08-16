package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.ProjectStatus;
import com.cmms.demo.dto.ProjectStatusDTO;
import com.cmms.demo.reponsitory.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectStatusService {
    @Autowired
    private ProjectStatusRepository repository;

    public ProjectStatus add(ProjectStatusDTO dto){
        ProjectStatus status = new ProjectStatus();
        status.setStatus_name(dto.getStatus_name());
        return repository.save(status);
    }

    public List<ProjectStatus> getAll(){
        return repository.findAll();
    }

    public ProjectStatus getOne(int id){
        return repository.findById(id).orElse(null);
    }

    public ProjectStatus update(int id, String statusName){
        ProjectStatus item = getOne(id);
        if (item != null){
            item.setStatus_name(statusName);
            return repository.save(item);
        }
        return null;
    }
}
