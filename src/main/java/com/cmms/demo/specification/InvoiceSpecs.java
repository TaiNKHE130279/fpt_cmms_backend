package com.cmms.demo.specification;

import com.cmms.demo.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class InvoiceSpecs {
    public static Specification<Invoice> filter(String cusName , String date, Long paymentType) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (cusName != null) {
                Join<Invoice, ProjectPOJO> join1 = root.join("project");
                Join<ProjectPOJO, CustomerPOJO> join2 = join1.join("customer");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(join2.get("name")),
                        "%" + cusName + "%"));
            }
            if(date != null){
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    predicates.add(criteriaBuilder.equal(root.get("create_date"), new Date(fromDate.getTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(paymentType != null){
                Join<Invoice, PaymentStatus> join = root.join("paymentStatus");
                predicates.add(criteriaBuilder.equal(join.get("status_id"), paymentType));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("invoice_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
