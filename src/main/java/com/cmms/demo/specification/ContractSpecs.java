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

public class ContractSpecs {
    public static Specification filter(String customerName, String date, Long paymentType, boolean isActive){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            if(customerName != null){
                Join<Contract, ProjectPOJO> join2 = root.join("project");
                Join<ProjectPOJO, CustomerPOJO> join3 = join2.join("customer");
                predicates.add(criteriaBuilder.like(join3.get("name"), "%" + customerName + "%"));
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
                Join<Contract, PaymentStatus> join = root.join("paymentStatus");
                predicates.add(criteriaBuilder.equal(join.get("status_id"), paymentType));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("contract_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
