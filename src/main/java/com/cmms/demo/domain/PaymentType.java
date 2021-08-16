package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payment_type")
public class PaymentType {
    @Id
    private int type_id;
    private String type_name;
    @OneToMany(mappedBy = "paymentType")
    private Set<DailyFinanceReport> listDailyFinanceRp = new HashSet<>();
}
