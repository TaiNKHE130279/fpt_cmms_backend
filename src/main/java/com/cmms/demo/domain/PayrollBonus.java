package com.cmms.demo.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "payroll_bonus")
public class PayrollBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bonus_id")
    private BonusPOJO bonus_id;


    @ManyToOne
    @JoinColumn(name = "payroll_detail_id")
    private PayrollDetail payroll_detail_id;

    private Long quantity = 1L;
    private Long value;
    private Long total_value;
}
