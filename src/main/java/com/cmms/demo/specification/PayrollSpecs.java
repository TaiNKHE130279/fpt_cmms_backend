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

public class PayrollSpecs {
    public static Specification<Payroll> filter(String payroll_name,Integer month, Integer year,Long status){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if(payroll_name != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + payroll_name + "%"));
            }
            if(month != null){
                    predicates.add( criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("end_date")),month));
            }
            if(year != null){
                predicates.add( criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("end_date")),year));
            }
            if(status != null){
                Join<Payroll,PayrollStatus> join = root.join("status");
                predicates.add(criteriaBuilder.equal(join.get("id"), status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
