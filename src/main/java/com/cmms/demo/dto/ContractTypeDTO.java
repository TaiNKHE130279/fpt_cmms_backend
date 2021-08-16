package com.cmms.demo.dto;

import com.cmms.demo.domain.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractTypeDTO {
    private Long type_id;
    private String type_name;
    private String file_path;

    public static ContractTypeDTO from(ContractType item){
        ContractTypeDTO dto = new ContractTypeDTO();
        dto.setType_id(item.getType_id());
        dto.setType_name(item.getType_name());
        dto.setFile_path(item.getFile_path());
        return dto;
    }
}
