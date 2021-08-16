package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentTypeRepository
        extends JpaRepository<PaymentType,Integer>, CrudRepository<PaymentType,Integer>,
        JpaSpecificationExecutor<PaymentType> {
}
