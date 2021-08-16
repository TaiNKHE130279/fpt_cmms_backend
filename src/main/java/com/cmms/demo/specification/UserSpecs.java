package com.cmms.demo.specification;

import com.cmms.demo.domain.RolePOJO;
import com.cmms.demo.domain.UserPOJO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class UserSpecs {
    public static Specification<UserPOJO> filter(String account, Long roleId, Long statusId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (account != null && !account.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("account")),
                        "%" + account + "%"));
            }
            if (statusId != null) {
                predicates.add(criteriaBuilder.equal(root.get("account_status"), statusId));
            }
            if (roleId != null) {
                Root<UserPOJO> user = root;
                Root<RolePOJO> role = criteriaQuery.from(RolePOJO.class);
                Expression<Collection<UserPOJO>> roleUser = role.get("set");
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(role.get("role_id"), roleId), criteriaBuilder.isMember(user, roleUser)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
