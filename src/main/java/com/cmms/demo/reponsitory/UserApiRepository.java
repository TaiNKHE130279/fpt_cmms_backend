package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.UserApi;
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
public interface UserApiRepository extends JpaRepository<UserApi, Long>, CrudRepository<UserApi, Long>, JpaSpecificationExecutor<UserApi> {
    @Query(value = "SELECT d.* FROM decentralization d\n" +
            "INNER JOIN action a on d.action = a.id\n" +
            "INNER JOIN api a2 on a.api = a2.id\n" +
            "WHERE a2.api_name = :api AND a.action_name = :action AND d.user = :user ", nativeQuery = true)
    UserApi findOneAuthority(@Param("action") String action, @Param("api") String api, @Param("user") Long id);


    @Query(value = "SELECT d.* FROM\n" +
            "decentralization as d\n" +
            "WHERE d.user = :user AND d.action = :action", nativeQuery = true)
    UserApi getOneByUserAndAction(@Param("user") Long user, @Param("action") Long action);

    @Query(value = "SELECT ar.id,ar.action,ar.api,ar.user FROM authorities as ar\n" +
            "group by ar.action, ar.api\n" +
            "order by ar.api", nativeQuery = true)
    List<UserApi> getAllActionOfApi();

    @Query(value = "SELECT d.* FROM\n" +
            "decentralization as d\n" +
            "WHERE d.user = :id ", nativeQuery = true)
    List<UserApi> getAllByUserId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE d FROM decentralization as d\n" +
            "WHERE d.action = :id ",nativeQuery = true)
    int  deleteByActionId(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM decentralization as ra\n" +
            "WHERE ra.action = :id ",nativeQuery = true)
    int isExist(Long id);
}
