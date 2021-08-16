package com.cmms.demo.endpoint;

import com.cmms.demo.domain.ProjectPOJO;
import com.cmms.demo.domain.ProjectStatus;
import com.cmms.demo.dto.ProjectDTO;
import com.cmms.demo.dto.ProjectOutput;
import com.cmms.demo.dto.ProjectStatusDTO;
import com.cmms.demo.service.ProjectService;
import com.cmms.demo.serviceImpl.ProjectServiceImpl;
import com.cmms.demo.serviceImpl.ProjectStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/project", name = "Quản lý dự án")
public class ProjectController {

    @Autowired
    private ProjectService service;
    @Autowired
    private ProjectStatusService statusService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO dto) {
        ProjectPOJO p = service.addProject(dto);
        return new ResponseEntity<>(ProjectDTO.from(p), HttpStatus.OK);
    }

    @GetMapping("/getOne/{code}")
    public ResponseEntity<ProjectDTO> getOne(@PathVariable final String code) {
        ProjectPOJO p = service.getOne(code);
        return new ResponseEntity<>(ProjectDTO.from(p), HttpStatus.OK);
    }

    @GetMapping(name = "Xem danh sách dự án")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, 'GET', this)}")
    public ResponseEntity<ProjectOutput> filter(@RequestParam("page") int pageIndex,
            @RequestParam("limit") int pageSize,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "projectType", required = false) Long projectType,
            @RequestParam(value = "projectStatus", required = false) Integer projectStatus) {
        return new ResponseEntity<>(service.filter(pageIndex, pageSize, customerName, projectType, projectStatus), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ProjectDTO> update(@RequestBody ProjectDTO dto) {
        ProjectPOJO p = service.update(dto);
        return new ResponseEntity<>(ProjectDTO.from(p), HttpStatus.OK);
    }

    @PutMapping(value = "/{projectCode}", name = "Hủy dự án")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'DELETE', this)}")
    public ResponseEntity<String> cancelProject(@PathVariable String projectCode){
        return new ResponseEntity<>(service.cancelProject(projectCode), HttpStatus.OK);
    }

    @GetMapping("/getAllProjectStatus")
    public ResponseEntity<List<ProjectStatusDTO>> getAllProjectStatus(){
        List<ProjectStatus> ls = statusService.getAll();
        List<ProjectStatusDTO> lsOutput = ls.stream().map(ProjectStatusDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(lsOutput, HttpStatus.OK);
    }

}
