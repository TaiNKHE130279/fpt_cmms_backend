package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "districts")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String rank;
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
    @OneToMany(mappedBy = "district")
    private Set<Commune> communes = new HashSet<>();
}
