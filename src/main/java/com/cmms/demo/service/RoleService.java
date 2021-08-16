package com.cmms.demo.service;

import com.cmms.demo.domain.RoleApi;
import com.cmms.demo.domain.RolePOJO;
import com.cmms.demo.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RolePOJO getOneById(Integer id);

    List<RolePOJO> findAll();

    RolePOJO update(RoleDTO dto);

    List<RoleApi> getRoleApis(Integer id);

    RoleApi getOneByKeyName(String s,String action,String menu,String method);

    RolePOJO create(RoleDTO dto);
}
