package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.RoleApi;
import com.cmms.demo.service.AppAuthorizer;
import com.cmms.demo.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@Service("appAuthorizer")
public class AppAuthorizerImpl implements AppAuthorizer {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthorityServiceImpl service;

    @Autowired
    RoleService roleService;

    @Override
    public boolean authorize(Authentication authentication, String action,String method, Object callerObj) {
        String securedPath = extractSecuredPath(callerObj);
        if (securedPath == null || "".equals(securedPath.trim())) {//login, logout
            return true;
        }
        String menuCode = securedPath.substring(1);//Bỏ dấu "/" ở đầu Path
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            if (user == null) {
                return isAllow;
            }
            List<String> roles = authentication.getAuthorities().stream()
                    .map(r -> r.getAuthority()).collect(Collectors.toList());

            List<RoleApi> apis = roles.stream().map(s -> roleService.getOneByKeyName(s,action,menuCode,method)).collect(Collectors.toList()); ;
            RoleApi roleApi = apis.stream().filter(api -> api.isAllow()).findFirst().orElse(null);
            if(roleApi == null){
                throw new AccessDeniedException("Bạn không có quyền truy cập");
            }
            {
                isAllow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return isAllow;
    }

    // Lay ra securedPath duoc Annotate RequestMapping trong Controller
    private String extractSecuredPath(Object callerObj) {
        Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
        Optional<Annotation> annotation = Arrays.asList(clazz.getAnnotations()).stream().filter((ann) -> {
            return ann instanceof RequestMapping;
        }).findFirst();
        System.out.println(ResolvableType.forClass(callerObj.getClass()).getType().getTypeName());
        if (annotation.isPresent()) {
            return ((RequestMapping) annotation.get()).value()[0];
        }
        return null;
    }

}
