package com.cmms.demo.dto;

import com.cmms.demo.domain.ApiItem;
import com.cmms.demo.domain.ActionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiItemDTO {
    private Long key;
    private String name;
    private String description;
    private Long api_id;
    private String url;
    private boolean isAllow;

    public static ApiItemDTO from1(ApiItem action){
        ApiItemDTO dto = new ApiItemDTO();
        dto.setApi_id(action.getApi().getId());
        dto.setName(ActionName.getApiNameByCode(action.getName()));
        dto.setKey(action.getId());
        dto.setUrl(action.getUrl());
        return dto;
    }

    public static ApiItemDTO from(ApiItem action) {
        ApiItemDTO dto = new ApiItemDTO();
        dto.setKey(action.getId());
        dto.setName(action.getName());
        dto.setDescription(action.getDescription());
        dto.setApi_id(action.getApi().getId());
        dto.setUrl(action.getUrl());
        return dto;
    }


}
