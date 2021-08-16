package com.cmms.demo.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CustomUser extends User {
    private final Long userId;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userID) {
        super(username, password, authorities);
        this.userId = userID;
    }

    public Long getUserId() {
        return userId;
    }
}
