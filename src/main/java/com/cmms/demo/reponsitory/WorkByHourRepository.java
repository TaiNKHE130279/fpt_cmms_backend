package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.WorkByHourPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkByHourRepository extends JpaRepository<WorkByHourPOJO,Long>
        , CrudRepository<WorkByHourPOJO,Long>, JpaSpecificationExecutor<WorkByHourPOJO> {
    @Query("SELECT c FROM work_by_hour c WHERE c.project.project_code = ?1")
    WorkByHourPOJO getByProjectCode(String code);
}
