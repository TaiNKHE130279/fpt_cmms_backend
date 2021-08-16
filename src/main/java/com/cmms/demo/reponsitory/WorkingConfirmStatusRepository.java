package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.WorkingConfirmStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingConfirmStatusRepository extends JpaRepository<WorkingConfirmStatus,Long>
        , CrudRepository<WorkingConfirmStatus,Long>, JpaSpecificationExecutor<WorkingConfirmStatus> {
}
