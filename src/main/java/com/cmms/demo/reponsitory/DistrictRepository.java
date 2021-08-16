package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long>
        , CrudRepository<District,Long>, JpaSpecificationExecutor<District> {

    @Query("SELECT d FROM districts as d WHERE d.province.id = ?1")
    List<District> getAllByProvince(Long id);
}
