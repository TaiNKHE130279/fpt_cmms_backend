package com.cmms.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "project_status")
public class ProjectStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status_name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectStatus")
    private Set<ProjectPOJO> listProject = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public Set<ProjectPOJO> getListProject() {
        return listProject;
    }

    public void setListProject(Set<ProjectPOJO> listProject) {
        this.listProject = listProject;
    }
}
