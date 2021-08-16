package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus,Long>
        , CrudRepository<PaymentStatus,Long>, JpaSpecificationExecutor<PaymentStatus> {

}
