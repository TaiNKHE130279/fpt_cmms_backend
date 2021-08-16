package com.cmms.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "project_type")
public class ProjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long type_id;
    private String type_name;

    @OneToMany(mappedBy = "projectType")
    private Set<ProjectPOJO> listProject  = new HashSet<>();

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Set<ProjectPOJO> getListProject() {
        return listProject;
    }

    public void setListProject(Set<ProjectPOJO> listProject) {
        this.listProject = listProject;
    }
}
