package com.cmms.demo.dto;

import com.cmms.demo.domain.ProjectStatus;

public class ProjectStatusDTO {
    private int id;
    private String status_name;

    public static ProjectStatusDTO from(ProjectStatus p){
        ProjectStatusDTO dto = new ProjectStatusDTO();
        dto.setId(p.getId());
        dto.setStatus_name(p.getStatus_name());
        return dto;
    }
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
}
