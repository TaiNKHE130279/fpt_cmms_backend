package com.cmms.demo.reponsitory;

import com.cmms.demo.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String>
        , CrudRepository<Invoice,String>, JpaSpecificationExecutor<Invoice> {
    @Query("SELECT c FROM invoices c WHERE c.project.project_code = ?1")
    Invoice getByProjectCode(String code);

    @Query("SELECT i FROM invoices i WHERE i.invoice_code = ?1")
    Invoice getOne(String code);

}
