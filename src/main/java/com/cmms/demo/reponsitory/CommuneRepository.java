package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommuneRepository extends JpaRepository<Commune,Long>
        , CrudRepository<Commune,Long>, JpaSpecificationExecutor<Commune> {

    @Query("SELECT c FROM communes c WHERE c.district.id = ?1")
    List<Commune> getAllByDistrict(Long id);
}
