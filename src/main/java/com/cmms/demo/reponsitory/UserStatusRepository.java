package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.UserStatus;
import com.cmms.demo.domain.UserPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserStatusRepository extends JpaRepository<UserStatus,Long>, CrudRepository<UserStatus,Long>, JpaSpecificationExecutor<UserPOJO> {
    @Query(value = "SELECT * FROM account_status as accs \n" +
            "WHERE accs.status_name = :name ",nativeQuery = true)
    UserStatus findByStatusName(@Param("name") String statusName);
}
