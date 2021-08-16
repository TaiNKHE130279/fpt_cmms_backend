package com.cmms.demo.specification;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.domain.ProjectPOJO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class ListHistoryTaskOfDriverSpecs {
    public static Specification<BookingScheduleDetail> getListHistoryTaskOfDriver(String driverCode, String customerName, String date, Date currentDate){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("driver_code"), driverCode));
            Join<BookingScheduleDetail, BookingSchedule> join1 = root.join("bookingSchedule");
            predicates.add(criteriaBuilder.lessThan(join1.get("date"), currentDate));
            if(customerName != null) {
                Join<BookingSchedule, ProjectPOJO> join2 = join1.join("project");
                Join<ProjectPOJO, CustomerPOJO> join3 = join2.join("customer");
                predicates.add(criteriaBuilder.like(join3.get("name"), "%" + customerName + "%"));
            }
            if(date != null){
                java.util.Date d = null;
                try {
                    d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                predicates.add(criteriaBuilder.equal(join1.get("date"), d));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
