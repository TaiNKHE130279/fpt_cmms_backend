package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DayOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DayOffRequestRepository extends JpaRepository<DayOffRequest,Long>, CrudRepository<DayOffRequest,Long>,
        JpaSpecificationExecutor<DayOffRequest> {

}
