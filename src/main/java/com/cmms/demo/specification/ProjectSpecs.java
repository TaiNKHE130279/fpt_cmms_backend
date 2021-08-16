package com.cmms.demo.specification;

import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.domain.ProjectPOJO;
import com.cmms.demo.domain.ProjectStatus;
import com.cmms.demo.domain.ProjectType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

public class ProjectSpecs {
    public static Specification<ProjectPOJO> filter(String customerName
            , Long projectType, Integer projectStatus) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if(customerName != null){
                Join<ProjectPOJO, CustomerPOJO> join = root.join("customer");
                predicates.add(criteriaBuilder.like(join.get("name"), "%"+customerName +"%"));
            }
            if(projectType != null){
                Join<ProjectPOJO, ProjectType> join = root.join("projectType");
                predicates.add(criteriaBuilder.equal(join.get("type_id"), projectType));
            }
            if(projectStatus != null){
                Join<ProjectPOJO, ProjectStatus> join = root.join("projectStatus");
                predicates.add(criteriaBuilder.equal(join.get("id"), projectStatus));
            }else{
                Join<ProjectPOJO, ProjectStatus> join = root.join("projectStatus");
                predicates.add(criteriaBuilder.notEqual(join.get("id"), 4));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("project_code")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
