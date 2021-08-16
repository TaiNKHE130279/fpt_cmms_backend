package com.cmms.demo.domain;

import com.cmms.demo.dto.MachineTypeDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "machine_type")
public class MachineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long type_id;
    private String type_name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machineType")
    private Set<MachinePOJO> listMachine = new HashSet<>();

    public static MachineType from(MachineTypeDTO dto){
        MachineType type = new MachineType();
        type.setType_name(dto.getType_name());
        return type;
    }

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

    public Set<MachinePOJO> getListMachine() {
        return listMachine;
    }

    public void setListMachine(Set<MachinePOJO> listMachine) {
        this.listMachine = listMachine;
    }
}
