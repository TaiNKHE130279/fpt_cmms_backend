package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DriverDayOffTracking;
import com.cmms.demo.domain.DriverDayOffTrackingDetailPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DriverDayOffTrackingRepository extends JpaRepository<DriverDayOffTracking,Long>, CrudRepository<DriverDayOffTracking,Long>,
        JpaSpecificationExecutor<DriverDayOffTracking> {
    @Query(value = "SELECT * FROM driver_day_off_tracking as d WHERE  d.is_approved = :value ",nativeQuery = true)
    List<DriverDayOffTracking> getApprovedList(@Param("value") boolean value);

    @Query(value = "SELECT ddo.* FROM driver_day_off_tracking as ddo INNER JOIN drivers d on ddo.driver_code = d.drive_code\n" +
            "INNER JOIN users u on d.user_id = u.user_id WHERE u.account = :name",nativeQuery = true)
    List<DriverDayOffTracking>getRequestListOfUser(@Param("name") String name);
    @Query(value = "SELECT dd.* FROM driver_day_off_tracking as dd\n" +
            "WHERE dd.driver_code = :driver AND is_approved = true ",nativeQuery = true)
    List<DriverDayOffTracking> getListDayOffByDriverID(@Param("driver") String driver);

    @Query(value = "SELECT COUNT(*) FROM driver_dayoff_detail\n" +
            "WHERE driver_code = :code AND\n" +
            "date BETWEEN :start AND :end ",nativeQuery = true)
    int getTotalDayOffByDriver(@Param("code")String code, @Param("start") java.util.Date start, @Param("end") Date end);

    }
