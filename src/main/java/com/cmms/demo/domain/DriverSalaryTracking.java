package com.cmms.demo.domain;

import javax.persistence.*;

@Entity(name = "driver_salary_tracking")
public class DriverSalaryTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_code")
    private DriverPOJO driver_code;
    private Long received_salary;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    private String evidence_image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DriverPOJO getDriver_code() {
        return driver_code;
    }

    public void setDriver_code(DriverPOJO driver_code) {
        this.driver_code = driver_code;
    }

    public Long getReceived_salary() {
        return received_salary;
    }

    public void setReceived_salary(Long received_salary) {
        this.received_salary = received_salary;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getEvidence_image() {
        return evidence_image;
    }

    public void setEvidence_image(String evidence_image) {
        this.evidence_image = evidence_image;
    }
}
