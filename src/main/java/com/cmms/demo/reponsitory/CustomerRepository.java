package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.CustomerPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerPOJO,String>
        , CrudRepository<CustomerPOJO,String>, JpaSpecificationExecutor<CustomerPOJO> {

    @Query("SELECT c FROM customers c WHERE c.customer_code = ?1")
    public CustomerPOJO getOne(String code);
}
