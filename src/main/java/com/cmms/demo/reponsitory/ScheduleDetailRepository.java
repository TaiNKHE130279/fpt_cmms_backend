package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.BookingScheduleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface ScheduleDetailRepository extends JpaRepository<BookingScheduleDetail, Long>
        , CrudRepository<BookingScheduleDetail, Long>, JpaSpecificationExecutor<BookingScheduleDetail> {

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.bookingSchedule.id = ?1")
    List<BookingScheduleDetail> getListByBookingId(Long id);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.bookingSchedule.project.project_code = ?1")
    List<BookingScheduleDetail> getDetailByProjectCode(String code);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1")
    List<BookingScheduleDetail> getDetailByDriverCode(String code);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.bookingSchedule.project.project_code = ?1")
    List<BookingScheduleDetail> getListDriverAssigned(String projectCode);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1 " +
            "AND sd.bookingSchedule.date = ?2 AND sd.bookingSchedule.project.project_code = ?3")
    BookingScheduleDetail getDetailByDriverAndDate(String driverCode, Date date, String projectCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE sd FROM booking_schedule_detail as sd WHERE sd.booking_schedule_id = :id", nativeQuery = true)
    int delete(@Param("id") Long id);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.id = ?1 AND sd.begin_time <= ?2 AND sd.finish_time >= ?2")
    BookingScheduleDetail getListDriver(Long id, Time time);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.bookingSchedule.date = ?1")
    List<BookingScheduleDetail> getListByDate(Date date);

    @Transactional
    @Modifying
    @Query(value = "DELETE sd FROM booking_schedule_detail as sd WHERE sd.id = :id", nativeQuery = true)
    int deleteByDetailId(@Param("id") Long id);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1 AND sd.bookingSchedule.date BETWEEN ?2 AND ?3")
    List<BookingScheduleDetail> getListTaskByDriverAndDate(String driverCode, Date from, Date to);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.bookingSchedule.project.project_code = ?1 " +
            " AND sd.bookingSchedule.date >= ?2")
    List<BookingScheduleDetail> getRemainDetail(String projectCode, Date date);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1 AND sd.bookingSchedule.date = ?2 AND " +
            " sd.bookingSchedule.project.project_code <> ?3")
    BookingScheduleDetail checkDate(String driverCode, Date date, String projectCode);

    @Query("SELECT MAX (b.date) FROM booking_schedule b WHERE b.project.project_code = ?1")
    Date getMaxDate(String projectCode);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1 AND sd.bookingSchedule.date = ?2")
    List<BookingScheduleDetail> getByDriverAndDate(String driverCode, Date date);

    @Query("SELECT sd FROM booking_schedule_detail sd WHERE sd.driver_code = ?1 AND sd.bookingSchedule.date >= ?2")
    List<BookingScheduleDetail> getByDriver(String driverCode, Date date);

    @Query(value = "SELECT SUM(sd.over_time) FROM booking_schedule_detail as sd\n" +
            "WHERE sd.driver_code = :code", nativeQuery = true)
    Long getTotalOverTimeByDriverCode(@Param("code") String driver);
}
