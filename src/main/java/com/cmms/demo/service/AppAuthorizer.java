package com.cmms.demo.service;

import org.springframework.security.core.Authentication;

public interface AppAuthorizer {
    boolean authorize(Authentication authentication,String action,String method, Object callerObj);
}
