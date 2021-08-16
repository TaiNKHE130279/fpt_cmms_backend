package com.cmms.demo.specification;

import com.cmms.demo.domain.MachinePOJO;
import com.cmms.demo.domain.MachineType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

public class MachineSpecs {
    public static Specification<MachinePOJO> filter(Float bottom, Float top, String name, Long typeId, boolean isActive){
        return (root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            if(name != null){
                predicates.add(criteriaBuilder.like(root.get("machine_name"), "%" + name + "%"));
            }
            if(bottom != null && top != null){
                predicates.add(criteriaBuilder.between(root.get("machine_load"), bottom.floatValue(), top.floatValue()));
            }
            if(typeId != null){
                Join<MachinePOJO, MachineType> join = root.join("machineType");
                predicates.add(criteriaBuilder.equal(join.get("type_id"), typeId));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("machine_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
