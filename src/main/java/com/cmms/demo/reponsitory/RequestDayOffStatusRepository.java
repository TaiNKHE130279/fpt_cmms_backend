package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DayOffRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RequestDayOffStatusRepository extends JpaRepository<DayOffRequestStatus,Long>, CrudRepository<DayOffRequestStatus,Long>, JpaSpecificationExecutor<DayOffRequestStatus> {

    DayOffRequestStatus findByStatus(String name);
}
