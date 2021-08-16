package com.cmms.demo.domain;

import com.cmms.demo.dto.MachineDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "machine")
public class MachinePOJO {
    @Id
    private String machine_code;
    private String machine_name;
    @Column(unique = true)
    private String license_plate;
    @Column(columnDefinition="TEXT")
    private String machine_image;
    private String machine_detail;
    private float machine_load;
    @ManyToOne
    @JoinColumn(name = "machine_status", nullable = true)
    private MachineStatus machineStatus;
    @ManyToOne
    @JoinColumn(name = "machine_type", nullable = false)
    private MachineType machineType;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;
}
