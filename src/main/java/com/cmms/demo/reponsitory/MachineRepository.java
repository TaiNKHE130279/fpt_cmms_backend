package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.MachinePOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MachineRepository extends JpaRepository<MachinePOJO,String>
        , CrudRepository<MachinePOJO,String>, JpaSpecificationExecutor<MachinePOJO> {

    @Query("SELECT m FROM machine m WHERE m.machine_code = ?1")
    MachinePOJO findByMachine_code(String machineCode);

    @Query("SELECT d.drive_code FROM drivers d WHERE d.machine.machine_code = ?1")
    String getDriverCode (String machineCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE m FROM machine as m WHERE m.machine_code = :code", nativeQuery = true)
    int delete(@Param("code") String code);

}
