package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Long>
        , CrudRepository<Holiday,Long>, JpaSpecificationExecutor<Holiday> {
    @Query("SELECT h FROM holidays h WHERE h.date = ?1")
    Holiday getByDate(Date date);
}
