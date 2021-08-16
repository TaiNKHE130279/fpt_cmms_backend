package com.cmms.demo.specification;

import com.cmms.demo.domain.CustomerPOJO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

public class CustomerSpecs {
    public static Specification<CustomerPOJO> filter(String name, String phone, String address){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if(name != null){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if(phone != null){
                predicates.add(criteriaBuilder.like(root.get("phone"), phone +"%"));
            }

            if(address != null){
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + address + "%"));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("customer_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
