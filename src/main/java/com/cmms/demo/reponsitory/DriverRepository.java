package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.DriverPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<DriverPOJO,String>, CrudRepository<DriverPOJO,String>, JpaSpecificationExecutor<DriverPOJO> {

    @Query("SELECT d FROM drivers d WHERE d.drive_code = ?1")
    DriverPOJO getOne(String code);

    @Query("SELECT d FROM drivers d WHERE d.machine.machine_code IS NULL AND d.isActive = true")
    List<DriverPOJO> getDriverListNotAssignMachine();

    @Query("SELECT d FROM drivers d WHERE d.machine.machine_code IS NOT NULL AND d.isActive = true")
    List<DriverPOJO> getDriverListAssignMachine();

    @Query("SELECT d FROM drivers d WHERE d.user.user_id = ?1")
    DriverPOJO getDriverByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE drivers d SET d.machine.machine_code = ?1 WHERE d.drive_code = ?2")
    int updateMachineForDriver(String machineCode, String driverCode);

    @Query("SELECT d FROM drivers d WHERE d.machine.machine_code = ?1")
    DriverPOJO findDriverByMachineCode(String machineCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE d FROM drivers as d WHERE d.drive_code = :code", nativeQuery = true)
    int delete(@Param("code") String code);

    @Query(value = "SELECT * FROM drivers d WHERE d.name LIKE CONCAT('%', CONVERT(:name, BINARY), '%')", nativeQuery = true)
    List<DriverPOJO> getListByName(@Param("name") String name);
}
