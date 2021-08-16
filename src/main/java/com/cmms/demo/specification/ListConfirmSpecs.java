package com.cmms.demo.specification;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.WorkingConfirmStatus;
import com.cmms.demo.reponsitory.DriverRepository;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class ListConfirmSpecs {
    public static Specification<BookingScheduleDetail> getListNeedConfirm
            (Long typeId, String driverName, String from, String to, DriverRepository driverRepository){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            Join<BookingScheduleDetail, WorkingConfirmStatus> join = root.join("status");
            predicates.add(criteriaBuilder.notEqual(join.get("type_id"), 1));
            predicates.add(criteriaBuilder.notEqual(join.get("type_id"), 4));
            predicates.add(criteriaBuilder.notEqual(join.get("type_id"), 5));
            Join<BookingScheduleDetail, BookingSchedule> join1 = root.join("bookingSchedule");

            if(from != null && to == null){
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(join1.get("date"), fromDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(from == null && to != null){
                try {
                    java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(join1.get("date"), toDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(from != null && to != null){
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
                    java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
                    predicates.add(criteriaBuilder.between(join1.get("date"), fromDate, toDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(typeId != null){
                predicates.add(criteriaBuilder.equal(join.get("type_id"), typeId));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(join1.get("date")));
            if (driverName != null) {
                List<DriverPOJO> getDriverByName = driverRepository.getListByName(driverName);
                List<Predicate> p = new LinkedList<>();
                if (getDriverByName.size() > 0) {
                    for (DriverPOJO d : getDriverByName) {
                        p.add(criteriaBuilder.equal(root.get("driver_code"), d.getDrive_code()));
                    }
                }
                Predicate p1 = criteriaBuilder.or(p.toArray(new Predicate[0]));
                predicates.add(p1);
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
