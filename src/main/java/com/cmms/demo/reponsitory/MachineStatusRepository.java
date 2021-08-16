package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineStatusRepository extends JpaRepository<MachineStatus,Integer>
        , CrudRepository<MachineStatus,Integer>, JpaSpecificationExecutor<MachineStatus> {
}
