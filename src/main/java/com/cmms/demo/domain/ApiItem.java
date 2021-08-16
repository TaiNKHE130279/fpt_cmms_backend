package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ApiItem")
public class ApiItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String url;
    String name;
    String description;
    String method;
    @ManyToOne
    @JoinColumn(name = "api")
    private Api api;
    @OneToMany(mappedBy = "apiItem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<UserApi> userApis = new ArrayList<>();

    @OneToMany(mappedBy = "apiItem")
    List<RoleApi> roleApis = new ArrayList<>();


}

