package com.cmms.demo.dto;

import com.cmms.demo.domain.RoleApi;
import com.cmms.demo.domain.UserApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApiDTO {
    private Long child_id;
    private String child_name;
    private Long func_id;
    private String func_name;
    private boolean isAllow;
    private String description;

    public static UserApiDTO from(UserApi api) {
        UserApiDTO dto = new UserApiDTO();
        dto.setAllow(api.isAllow());
        dto.setDescription(api.getApiItem().getDescription());
        dto.setChild_id(api.getId());
        dto.setFunc_name(api.getApiItem().getApi().getName());
        dto.setFunc_id(api.getApiItem().getId());
        dto.setChild_name(api.getApiItem().getName());
        return dto;
    }
}
