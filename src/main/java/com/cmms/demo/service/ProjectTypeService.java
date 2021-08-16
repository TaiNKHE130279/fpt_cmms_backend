package com.cmms.demo.service;

import com.cmms.demo.domain.ProjectType;

import java.util.List;

public interface ProjectTypeService {
    List<ProjectType> getAll();

    ProjectType getOne(Long id);
}
