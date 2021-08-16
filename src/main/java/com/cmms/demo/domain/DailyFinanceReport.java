package com.cmms.demo.domain;

import com.cmms.demo.dto.DailyFinanceReportDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "daily_finance_report")
public class DailyFinanceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long amount;
    private Date payment_date;
    private String note;
    private String project_code;
    private String driver_code;
    private boolean is_payment = false;
    private boolean is_auto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type", nullable = false)
    private PaymentType paymentType;
}
