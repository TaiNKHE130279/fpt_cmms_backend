package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType,Long>
        , CrudRepository<ContractType,Long>, JpaSpecificationExecutor<ContractType> {
}
