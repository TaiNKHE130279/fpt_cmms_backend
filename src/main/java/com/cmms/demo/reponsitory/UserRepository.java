package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.UserPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserPOJO,Long>, CrudRepository<UserPOJO,Long>, JpaSpecificationExecutor<UserPOJO>, PagingAndSortingRepository<UserPOJO,Long> {
    UserPOJO findByAccount(String string);

    @Query(value = "SELECT u.* FROM user_role\n" +
            "INNER JOIN users u on user_role.user_id = u.user_id\n" +
            "WHERE user_role.role_id = :role",nativeQuery = true)
    List<UserPOJO> findByRole(@Param("role") int role);

    @Transactional
    @Modifying
    @Query(value = "DELETE u FROM users as u WHERE u.user_id = :id", nativeQuery = true)
    int deleteDriverUser(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE d FROM user_role as d WHERE d.user_id = :id", nativeQuery = true)
    int deleteUserRole(@Param("id") Long id);
}
