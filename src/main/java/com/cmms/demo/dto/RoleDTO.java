package com.cmms.demo.dto;

import com.cmms.demo.domain.RolePOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private int role_id;
    private String role_name;
    private String name;
    private List<RoleApiDTO> functions = new ArrayList<>();
    private String description;
    public static RoleDTO from(RolePOJO rolePOJO) {
        RoleDTO dto = new RoleDTO();
        dto.setRole_id(rolePOJO.getRole_id());
        dto.setRole_name(rolePOJO.getRole_name());
        dto.setName(rolePOJO.getName());
        dto.setFunctions(rolePOJO.getRoleApis().stream().map(RoleApiDTO::from).collect(Collectors.toList()));
        return dto;
    }
    public static RoleDTO fromWithOutDecentralization(RolePOJO rolePOJO){
        RoleDTO dto = new RoleDTO();
        dto.setRole_id(rolePOJO.getRole_id());
        dto.setRole_name(rolePOJO.getRole_name());
        //dto.setFunctions(rolePOJO.getRoleApis().stream().map(RoleApiDTO::from).collect(Collectors.toList()));
        dto.setDescription(rolePOJO.getDescription());
        return dto;
    }
}
