package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DailyFinanceReport;
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
public interface DailyFinanceReportRepository
        extends JpaRepository<DailyFinanceReport,Long>, CrudRepository<DailyFinanceReport,Long>,
        JpaSpecificationExecutor<DailyFinanceReport> {
    @Transactional
    @Modifying
    @Query(value = "DELETE d FROM daily_finance_report d WHERE d.id = :id", nativeQuery = true)
    int delete(@Param("id") Long id);

    @Query(value = "SELECT daily.* FROM daily_finance_report as daily\n" +
            "WHERE daily.driver_code = :code AND daily.is_payment = false",nativeQuery = true)
    List<DailyFinanceReport> getReportByDriverCode(@Param("code") String staff_name);

    @Query("SELECT d FROM daily_finance_report d WHERE d.project_code = ?1 ORDER BY d.payment_date DESC")
    List<DailyFinanceReport> getListByProjectCode(String projectCode);
}
