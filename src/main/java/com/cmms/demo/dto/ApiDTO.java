package com.cmms.demo.dto;

import com.cmms.demo.domain.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiDTO {
    private Long key;
    private String name;
    private String url;
    private List<ApiItemDTO> children;

    public static ApiDTO from(Api api) {
        ApiDTO dto = new ApiDTO();
        dto.setName(api.getName());
        dto.setKey(-(api.getId()));
        dto.setChildren(api.getItems().stream().map(ApiItemDTO::from).collect(Collectors.toList()));
        return dto;
    }

    public static ApiDTO from1(Api api) {
        ApiDTO dto = new ApiDTO();
        dto.setName(api.getName());
        dto.setKey(-(api.getId()));
        dto.setChildren(api.getItems().stream().map(ApiItemDTO::from1).collect(Collectors.toList()));
        return dto;
    }

}
