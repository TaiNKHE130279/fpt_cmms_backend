package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DeductionPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DeductionRepository extends JpaRepository<DeductionPOJO,Long>
        , CrudRepository<DeductionPOJO,Long>, JpaSpecificationExecutor<DeductionPOJO> {
}
