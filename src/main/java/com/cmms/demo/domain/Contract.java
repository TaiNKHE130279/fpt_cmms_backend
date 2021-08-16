package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "contracts")
public class Contract {
    @Id
    private String contract_code;
    @OneToOne
    @JoinColumn(name = "project_code",nullable = false)
    private ProjectPOJO project;
    private Date create_date;
    private Long contract_price;
    private Long advance_amount;
    private String evidence_image;
    @ManyToOne
    @JoinColumn(name = "contract_type")
    private ContractType type;

    private String content;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;
}
