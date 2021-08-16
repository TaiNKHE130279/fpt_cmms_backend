package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PayrollStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollStatusRepository extends JpaRepository<PayrollStatus, Long>, CrudRepository<PayrollStatus, Long>,
        JpaSpecificationExecutor<PayrollStatus> {
}
