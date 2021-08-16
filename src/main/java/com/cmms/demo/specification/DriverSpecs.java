package com.cmms.demo.specification;

import com.cmms.demo.domain.DriverPOJO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

public class DriverSpecs {
    public static Specification<DriverPOJO> filter(String name, Long bottom, Long top, String address, boolean isActive, String phone){
        return (root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if(phone != null){
                predicates.add(criteriaBuilder.like(root.get("phone"), phone + "%"));
            }
            if(bottom != null && top != null){
                predicates.add(criteriaBuilder.between(root.get("base_salary"), bottom, top));
            }
            if(address != null){
                predicates.add(criteriaBuilder.equal(root.get("address"), "%" + address + "%"));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("drive_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
