package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.ApiItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ApiItemRepository extends JpaRepository<ApiItem, Long>, CrudRepository<ApiItem, Long>, JpaSpecificationExecutor<ApiItem> {
    @Query(value = "SELECT a.* FROM api as ai\n" +
            "INNER JOIN action a on ai.id = a.api\n" +
            "INNER JOIN decentralization d on a.id = d.action\n" +
            "WHERE d.user = :id", nativeQuery = true)
    List<ApiItem> getAllActionByUserId(@Param("id") Long id);

    @Query(value = "SELECT ac.* FROM action as ac\n" +
            "WHERE api = :id", nativeQuery = true)
    List<ApiItem> getAllActionByApiId(@Param("id") Long id_api);

    @Query(value = "SELECT a.* FROM role_action as ra\n" +
            "INNER JOIN action a on ra.action_id = a.id\n" +
            "INNER JOIN role r on ra.role_id = r.role_id\n" +
            "WHERE r.role_id = :id ",nativeQuery = true)
    List<ApiItem> getAllActionByRoleId(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = "DELETE d FROM role_action as d\n" +
            "WHERE d.action_id = :id  ",nativeQuery = true)
    int  deleteRoleActionByActionId(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM role_action as ra\n" +
            "WHERE ra.action_id = :id",nativeQuery = true)
    int isExist(@Param("id") Long id);
}
