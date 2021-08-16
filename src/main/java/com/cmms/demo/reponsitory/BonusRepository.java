package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.BonusPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface BonusRepository extends JpaRepository<BonusPOJO,Long>
        , CrudRepository<BonusPOJO,Long>, JpaSpecificationExecutor<BonusPOJO> {
}
