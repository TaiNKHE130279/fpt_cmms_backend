package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiRepository extends JpaRepository<Api, Long>, CrudRepository<Api, Long>, JpaSpecificationExecutor<Api> {
    @Query(value = "SELECT distinct ai.* FROM api as ai\n" +
            "INNER JOIN action a on ai.id = a.api\n" +
            "INNER JOIN decentralization d on a.id = d.action\n" +
            "WHERE d.user = :id", nativeQuery = true)
    List<Api> getApiByUserId(@Param("id") Long id);
}
