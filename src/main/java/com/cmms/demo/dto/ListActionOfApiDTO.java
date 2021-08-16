package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListActionOfApiDTO {
    private Long key;
    private String name;
    private List<ApiItemDTO> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListActionOfApiDTO dto2 = (ListActionOfApiDTO) o;
        return Objects.equals(key, dto2.key) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, name, children);
    }
}
