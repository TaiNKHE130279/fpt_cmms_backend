package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_api")
public class UserApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "api")
    private ApiItem apiItem;

    @ManyToOne
    @JoinColumn(name = "user")
    private UserPOJO user;

    private boolean isAllow=false;
}
