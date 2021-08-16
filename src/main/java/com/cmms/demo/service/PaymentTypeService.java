package com.cmms.demo.service;

import com.cmms.demo.domain.PaymentType;

import java.util.List;

public interface PaymentTypeService {
    PaymentType getOne(int id);

    List<PaymentType> getAll();
}
