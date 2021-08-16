package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus,Integer>
        , CrudRepository<ProjectStatus,Integer>, JpaSpecificationExecutor<ProjectStatus> {
}
