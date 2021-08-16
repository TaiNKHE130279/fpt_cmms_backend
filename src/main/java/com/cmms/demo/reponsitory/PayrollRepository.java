package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PayrollRepository
        extends JpaRepository<Payroll, Long>, CrudRepository<Payroll, Long>,
        JpaSpecificationExecutor<Payroll> {
    @Query(value = "SELECT pr.* FROM payroll as pr\n" +
            "INNER JOIN payroll_detail pd on pr.id = pd.payroll_id\n" +
            "INNER JOIN drivers d on pd.user_id = d.user_id\n" +
            "WHERE d.drive_code = :driver", nativeQuery = true)
    List<Payroll> findAllByDriverCode(@Param("driver") String driver_id);

    @Query(value = "SELECT * FROM payroll as pr\n" +
            "            WHERE NOT EXISTS(\n" +
            "              SELECT * FROM payroll as payroll2\n" +
            "            WHERE payroll2.id = pr.id AND pr.id = :id \n" +
            "              ) AND pr.status_id = 1 ", nativeQuery = true)
    List<Payroll> getAllIgnoreId(@Param("id") Long id);
}
