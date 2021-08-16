package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "customers")
public class CustomerPOJO {
    @Id
    private String customer_code;
    private String name;
    private String phone;
    @OneToMany(mappedBy = "customer")
    private Set<ProjectPOJO> listProject = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;
    @OneToOne
    @JoinColumn(name = "district_id")
    private District district;
    @OneToOne
    @JoinColumn(name = "commune_id")
    private Commune commune;
    private String detail_address;
    private Date date_of_birth;
    private Integer gender;
    private String id_number;
    private String address;
}
