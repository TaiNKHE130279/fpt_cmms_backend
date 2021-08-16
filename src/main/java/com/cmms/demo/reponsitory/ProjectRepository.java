package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.ProjectPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectPOJO,String>
        , CrudRepository<ProjectPOJO,String>, JpaSpecificationExecutor<ProjectPOJO> {
    @Query("SELECT p FROM projects p WHERE p.project_code = ?1")
    ProjectPOJO getOne(String code);

    @Query("SELECT p FROM projects p WHERE p.customer.customer_code = ?1 ORDER BY p.project_code DESC ")
    List<ProjectPOJO> getListByCustomer(String customerCode);
}
