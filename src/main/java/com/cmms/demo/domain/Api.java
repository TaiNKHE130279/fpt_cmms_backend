package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "api")
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String name;
    @OneToMany(mappedBy = "api", cascade = {CascadeType.MERGE, CascadeType.REMOVE},fetch = FetchType.EAGER)
    List<ApiItem> items = new ArrayList<>();

}
