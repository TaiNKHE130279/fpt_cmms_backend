package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PayrollDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PayrollDeductionRepository extends JpaRepository<PayrollDeduction, Long>, CrudRepository<PayrollDeduction, Long>,
        JpaSpecificationExecutor<PayrollDeduction> {
}
