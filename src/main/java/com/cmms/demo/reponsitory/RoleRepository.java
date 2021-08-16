package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.RoleApi;
import com.cmms.demo.domain.RolePOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RolePOJO, Integer>, CrudRepository<RolePOJO, Integer>, JpaSpecificationExecutor<RolePOJO> {
    @Query(value = "SELECT r.* FROM user_role as ur INNER JOIN role r on ur.role_id = r.role_id INNER JOIN users u on ur.user_id = u.user_id\n" +
            "WHERE u.account = :name", nativeQuery = true)
    List<RolePOJO> getAllRoleByUserName(@Param("name") String name);

    @Query(value = "INSERT INTO user_role (user_role.user_id,user_role.role_id) VALUES (:userid,:roleid)", nativeQuery = true)
    boolean saveUserRole(@Param("userid") Long user_id, @Param("roleid") int role_id);

    @Query(value = "SELECT * FROM role as r WHERE r.role_name = :name ", nativeQuery = true)
    RolePOJO findByRole_name(@Param("name") String roleName);

    }
