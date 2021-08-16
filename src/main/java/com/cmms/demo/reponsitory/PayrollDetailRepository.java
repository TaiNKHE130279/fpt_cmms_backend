package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.PayrollDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Long>, CrudRepository<PayrollDetail, Long>,
        JpaSpecificationExecutor<PayrollDetail> {
    @Query(value = "SELECT * FROM payroll_detail as pd WHERE pd.payroll_id = :id ",nativeQuery = true)
    List<PayrollDetail> findAllByPayrollId(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "DELETE pd FROM payroll_bonus  as pd WHERE pd.id = :id ",nativeQuery = true)
    int deleteBonusByPayrollId(@Param("id")Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE pd FROM payroll_deduction  as pd WHERE pd.id = :id ",nativeQuery = true)
    int deleteDeductionByPayrollId(@Param("id")Long aLong);

    @Query(value = "SELECT pd.* FROM payroll_detail  as pd INNER JOIN drivers d on pd.user_id = d.user_id\n" +
            "WHERE d.drive_code = :driver AND pd.payroll_id =:payroll",nativeQuery = true)
    PayrollDetail getOneDetailByDriver(@Param("driver") String driver_id,@Param("payroll") Long payroll_id);
}
