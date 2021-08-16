package com.cmms.demo.endpoint;

import com.cmms.demo.domain.ProjectType;
import com.cmms.demo.dto.ProjectTypeDTO;
import com.cmms.demo.service.ProjectTypeService;
import com.cmms.demo.serviceImpl.ProjectTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projectType")
public class ProjectTypeController {
    @Autowired
    private ProjectTypeService service;

    @GetMapping
//    @PreAuthorize("{@appAuthorizer.authorize(authentication, 'PROJECT', this)}")
    public ResponseEntity<List<ProjectTypeDTO>> getAllProjectType(){
        List<ProjectType> lsType = service.getAll();
        List<ProjectTypeDTO> lsDto = lsType.stream().map(ProjectTypeDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsDto, HttpStatus.OK);
    }

    @GetMapping("/getOne/{id}")
//    @PreAuthorize("{@appAuthorizer.authorize(authentication, 'PROJECT', this)}")
    public ResponseEntity<ProjectTypeDTO> getOneProjectType(@PathVariable final Long id){
        ProjectType type = service.getOne(id);
        return new ResponseEntity<>(ProjectTypeDTO.from(type), HttpStatus.OK);
    }
}
