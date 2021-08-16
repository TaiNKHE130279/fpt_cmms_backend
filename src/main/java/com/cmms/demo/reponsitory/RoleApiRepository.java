package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.RoleApi;
import com.cmms.demo.domain.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleApiRepository extends JpaRepository<RoleApi, Long>, CrudRepository<RoleApi, Long>, JpaSpecificationExecutor<RoleApi> {
    @Query(value = "SELECT rp.* FROM role_api  as rp WHERE rp.role = :name", nativeQuery = true)
    List<RoleApi> findAllByRoleId(@Param("name") Integer id);

    @Query(value = "SELECT rp.id,rp.api,rp.role,rp.is_allow FROM role_api as rp\n" +
            "INNER JOIN api_item ai on rp.api = ai.id\n" +
            "INNER JOIN api a on ai.api = a.id\n" +
            "INNER JOIN role r on rp.role = r.role_id\n" +
            "WHERE r.role_name = :role AND a.url = :api AND ai.url=:item AND ai.method = :method ", nativeQuery = true)
    RoleApi findRoleApi(@Param("role") String role, @Param("api") String api, @Param("item") String item, @Param("method") String method);

}
