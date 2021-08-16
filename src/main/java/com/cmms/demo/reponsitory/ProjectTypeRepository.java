package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTypeRepository extends JpaRepository<ProjectType,Long>
        , CrudRepository<ProjectType,Long>, JpaSpecificationExecutor<ProjectType> {
}
