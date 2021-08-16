package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.BookingSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface BookingScheduleRepository extends JpaRepository<BookingSchedule,Long>
        , CrudRepository<BookingSchedule,Long>, JpaSpecificationExecutor<BookingSchedule> {

    @Query("SELECT bs FROM booking_schedule bs WHERE bs.project.project_code = ?1 AND bs.date = ?2")
    BookingSchedule getOne(String projectCode, Date date);

    @Query("SELECT bs FROM booking_schedule bs WHERE bs.project.project_code = ?1")
    Page<BookingSchedule> getPageBookingByProject(String projectCode, Pageable pageable);

    @Query("SELECT bs FROM booking_schedule bs WHERE bs.project.project_code = ?1 ORDER BY bs.date DESC ")
    Page<BookingSchedule> getPageByProject(String projectCode, Pageable pageable);

    @Query("SELECT bs FROM booking_schedule bs WHERE bs.project.project_code = ?1")
    List<BookingSchedule> getListByProject(String projectCode);


    @Query("SELECT bs FROM booking_schedule bs WHERE bs.date = ?1")
    List<BookingSchedule> getListByDate(Date date);

    @Transactional
    @Modifying
    @Query(value = "DELETE b FROM booking_schedule b WHERE b.project_code = :code AND b.date = :begin ", nativeQuery = true)
    int delete(@Param("code") String code, @Param("begin") Date begin);

    @Transactional
    @Modifying
    @Query(value = "DELETE b FROM booking_schedule b WHERE b.id = :id", nativeQuery = true)
    int deleteByBookingId(@Param("id") Long id);

    @Query("SELECT b FROM booking_schedule b WHERE b.project.project_code = ?1 AND b.date >= ?2")
    List<BookingSchedule> getRemainDate(String projectCode, Date date);

    @Query("SELECT MAX(b.date) FROM booking_schedule b WHERE b.project.project_code = ?1")
    Date getMaxDateOfProject(String projectCode);

    @Query("SELECT MIN(b.date) FROM booking_schedule b WHERE b.project.project_code = ?1")
    Date getMinDateOfProject(String projectCode);
}
