package com.cmms.demo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class UserPOJO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column(unique = true,nullable = false,name = "account")
    private String account;
    private String password;
    private String token;

    @OneToMany(mappedBy = "user",cascade = CascadeType.MERGE)
    List<UserApi> authorities = new ArrayList<>();

    public List<UserApi> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserApi> authorities) {
        this.authorities = authorities;
    }

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "account_status")
    private UserStatus account_status;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private DriverPOJO driver;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}

    )
    List<RolePOJO> rolePOJOSet = new ArrayList<>();

    public List<RolePOJO> getRolePOJOSet() {
        return rolePOJOSet;
    }

    public void setRolePOJOSet(List<RolePOJO> rolePOJOSet) {
        this.rolePOJOSet = rolePOJOSet;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DriverPOJO getDriver() {
        return driver;
    }

    public void setDriver(DriverPOJO driver) {
        this.driver = driver;
    }

    public UserStatus getAccount_status() {
        return account_status;
    }

    public void setAccount_status(UserStatus account_status) {
        this.account_status = account_status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
