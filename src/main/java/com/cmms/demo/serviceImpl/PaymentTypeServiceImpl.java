package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.PaymentType;
import com.cmms.demo.reponsitory.PaymentTypeRepository;
import com.cmms.demo.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository repository;

    @Autowired
    public PaymentTypeServiceImpl(PaymentTypeRepository repository) {
        this.repository = repository;
    }

    public PaymentType addPaymentType(PaymentType item){
        if(getOne(item.getType_id()) != null) {
            return repository.save(item);
        }
        return null;
    }

    @Override
    public PaymentType getOne(int id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PaymentType> getAll(){
        return repository.findAll();
    }

    public PaymentType updatePaymentType(int id, PaymentType item){
        PaymentType itemToEdit = getOne(id);
        itemToEdit.setType_name(item.getType_name());
        return repository.save(itemToEdit);
    }

    public PaymentType deletePaymentType(int id){
        PaymentType item = getOne(id);
        repository.delete(item);
        return item;
    }
}
