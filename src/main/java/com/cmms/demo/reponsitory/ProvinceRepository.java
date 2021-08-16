package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long>
        , CrudRepository<Province,Long>, JpaSpecificationExecutor<Province> {
}
