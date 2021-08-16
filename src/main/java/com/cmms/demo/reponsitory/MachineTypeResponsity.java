package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.MachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineTypeResponsity extends JpaRepository<MachineType, Long>, CrudRepository<MachineType,Long>, JpaSpecificationExecutor<MachineType> {

}
