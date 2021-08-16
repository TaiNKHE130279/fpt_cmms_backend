package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract,String>
        , CrudRepository<Contract,String>, JpaSpecificationExecutor<Contract> {

    @Query("SELECT c FROM contracts c WHERE c.contract_code = ?1")
    Contract getOne(String code);

    @Query("SELECT c FROM contracts c WHERE c.project.project_code = ?1")
    Contract getByProjectCode(String code);
}
