package com.cmms.demo.specification;

import com.cmms.demo.domain.DailyFinanceReport;
import com.cmms.demo.domain.PaymentType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class DailyFinanceRpSpecs {
    public static Specification filter(Integer paymentType, String from, String to, String title){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if(paymentType != null){
                Join<DailyFinanceReport, PaymentType> join = root.join("paymentType");
                predicates.add(criteriaBuilder.equal(join.get("type_id"), paymentType));
            }
            if(from != null && to == null){
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
                    predicates.add(criteriaBuilder.equal(root.get("payment_date"), new Date(fromDate.getTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (from != null && to != null) {
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
                    java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
                    predicates.add(criteriaBuilder.between(root.get("payment_date"), new Date(fromDate.getTime()), new Date(toDate.getTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(title != null){
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("payment_date")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
