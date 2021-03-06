package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayTypeRepository extends JpaRepository<HolidayType,Long>
        , CrudRepository<HolidayType,Long>, JpaSpecificationExecutor<HolidayType> {
}
