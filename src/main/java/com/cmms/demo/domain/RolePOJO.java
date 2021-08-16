package com.cmms.demo.domain;

import com.cmms.demo.dto.RoleDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "role")
public class RolePOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int role_id;
    private String name;
    private String role_name;
    private String description;
    @OneToMany(mappedBy = "role")
    private List<RoleApi> roleApis = new ArrayList<>();

    @ManyToMany(mappedBy = "rolePOJOSet", cascade = {CascadeType.MERGE})
    private Set<UserPOJO> set = new HashSet<>();

    public static RolePOJO convertToEntity(RoleDTO dto) {
        RolePOJO pojo = new RolePOJO();
        pojo.setRole_id(dto.getRole_id());
        pojo.setRole_name(dto.getRole_name());
        if (dto.getDescription() != null) {
            pojo.setDescription(dto.getDescription());
        }
        return pojo;
    }
}
