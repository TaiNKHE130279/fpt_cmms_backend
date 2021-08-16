package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DriverTemporary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverTemporaryRepository extends JpaRepository<DriverTemporary,Long>
        , CrudRepository<DriverTemporary,Long>, JpaSpecificationExecutor<DriverTemporary> {

    @Query("SELECT t FROM driver_temporary t WHERE t.detail.id = ?1")
    DriverTemporary getOneByScheduleDetailId(Long id);
}
