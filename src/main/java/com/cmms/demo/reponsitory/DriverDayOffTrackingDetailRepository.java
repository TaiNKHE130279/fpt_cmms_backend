package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DriverDayOffTrackingDetailPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface DriverDayOffTrackingDetailRepository extends JpaRepository<DriverDayOffTrackingDetailPOJO, Long>, CrudRepository<DriverDayOffTrackingDetailPOJO, Long>,
        JpaSpecificationExecutor<DriverDayOffTrackingDetailPOJO> {
    @Query(value = "SELECT ddd.*\n" +
            "FROM driver_dayoff_detail as ddd\n" +
            "WHERE ddd.driver_code = :code AND MONTH(ddd.date) = :month AND YEAR(ddd.date) = :year ", nativeQuery = true)
    List<DriverDayOffTrackingDetailPOJO> getAllByDriverId(@Param("code") String code, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT d FROM driver_dayoff_detail d WHERE d.driver_code = ?1 AND d.date BETWEEN ?2 AND ?3")
    List<DriverDayOffTrackingDetailPOJO> getListDayOff(String driverCode, Date from, Date to);

    @Query(value = "SELECT detail.* FROM driver_dayoff_detail as detail\n" +
            "WHERE driver_code = :code AND\n" +
            "date BETWEEN :start AND :end ", nativeQuery = true)
    List<DriverDayOffTrackingDetailPOJO> getAllDayOffByDriver(@Param("code") String code, @Param("start") java.util.Date start, @Param("end") java.util.Date end);

}
