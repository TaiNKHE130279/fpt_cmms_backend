package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PayrollBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PayrollBonusRepository extends JpaRepository<PayrollBonus, Long>, CrudRepository<PayrollBonus, Long>,
        JpaSpecificationExecutor<PayrollBonus> {
}
