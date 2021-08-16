package com.cmms.demo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "role_api")
@Data
public class RoleApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "api")
    private ApiItem apiItem;

    @ManyToOne
    @JoinColumn(name = "role")
    private RolePOJO role;

    private boolean isAllow = false;
}
