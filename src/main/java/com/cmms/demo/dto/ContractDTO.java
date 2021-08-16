package com.cmms.demo.dto;

import com.cmms.demo.domain.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDTO {
    private String contract_code;
    private String project_code;
    private String start_date;
    private String end_date;
    private CustomerDTO customer;
    private Date create_date;
    private Long contract_price;
    private Long advance_amount;
    private String evidence_image;
    private ContractTypeDTO type;
    private String folder_id;
    private String content;

    public static ContractDTO from(Contract item){
        ContractDTO dto = new ContractDTO();
        dto.setContract_code(item.getContract_code());
        dto.setCreate_date(item.getCreate_date());
        dto.setContract_price(item.getContract_price());
        dto.setAdvance_amount(item.getAdvance_amount());
        dto.setEvidence_image(item.getEvidence_image());
        dto.setProject_code(item.getProject().getProject_code());
        dto.setCreate_date(item.getCreate_date());
        dto.setCustomer(CustomerDTO.from(item.getProject().getCustomer(), null));

        ContractTypeDTO type = new ContractTypeDTO();
        type.setType_id(item.getType().getType_id());
        type.setType_name(item.getType().getType_name());
        dto.setType(type);
        //dto.setFolder_id(item.getFolder_id());
        dto.setContent(item.getContent());
        dto.setStart_date(item.getProject().getExpected_starting_date().toString());
        dto.setEnd_date(item.getProject().getExpected_end_date().toString());
        return dto;
    }
}
