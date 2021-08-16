package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.PaymentStatus;
import com.cmms.demo.reponsitory.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentStatusService {
    @Autowired
    private PaymentStatusRepository repository;

    public PaymentStatus getOne(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<PaymentStatus> getAll(){return repository.findAll();}
}
