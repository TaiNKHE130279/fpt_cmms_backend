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

public class DayOffSpecs {
    public static Specification<DriverDayOffTracking> filter(String driver_name, Long status, String start,String end){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if(driver_name != null){
                Join<DriverDayOffTracking, DriverPOJO> join1 = root.join("driver_code");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(join1.get("name")),
                        "%" + driver_name + "%"));
            }

            if(status != null){
                Join<DriverDayOffTracking, DayOffRequestStatus> join1 = root.join("status");
                predicates.add(criteriaBuilder.equal(join1.get("id"), status));
            }
            if(start != null){
                try {
                    Date start_date = new Date(format.parse(start).getTime());
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("date_create"),start_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            if(end != null){
                try {
                    Date end_date = new Date(format.parse(end).getTime());
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("date_create"),end_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
