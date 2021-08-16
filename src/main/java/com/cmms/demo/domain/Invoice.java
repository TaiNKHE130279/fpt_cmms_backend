package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "invoices")
public class Invoice {
    @Id
    private String invoice_code;
    @OneToOne
    @JoinColumn(name = "project_code",nullable = false)
    private ProjectPOJO project;
    private Date create_date;
    private Long total_amount;
    private Long advance_amount;
    @Column(columnDefinition="TEXT")
    private String evidence_image;
    @ManyToOne
    @JoinColumn(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

}
