package com.cmms.demo.endpoint;

import com.cmms.demo.domain.RolePOJO;
import com.cmms.demo.dto.RoleDTO;
import com.cmms.demo.serviceImpl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/role",name = "Quản lý phân quyền")
@CrossOrigin("*")
public class RoleController {
    @Autowired
    RoleServiceImpl roleService;

    @GetMapping(value = "/getOne/{id}",name = "Xem thông tin 1 quyền")
    public ResponseEntity<RoleDTO> getOneRoleById(@PathVariable Integer id) {
        RolePOJO role = roleService.getOneById(id);
        return new ResponseEntity<>(RoleDTO.from(role), HttpStatus.OK);
    }

    @GetMapping(name = "Xem tất cả quyền")
    public ResponseEntity<List<RoleDTO>> getAllRole() {
        List<RolePOJO> role = roleService.findAll();
        return new ResponseEntity<>(role.stream().map(RoleDTO::from).collect(Collectors.toList()), HttpStatus.OK);
    }
    @PostMapping(value = "/update",name = "Cập nhật quyền")
    public ResponseEntity<RoleDTO>updateInfoRole(@RequestBody RoleDTO dto){
        RolePOJO role = roleService.update(dto);
        return new ResponseEntity<>(RoleDTO.from(role),HttpStatus.OK);
    }
    @PostMapping(value = "/create",name = "Tạo quyền truy cập mới")
    public ResponseEntity<RoleDTO>createRole(@RequestBody RoleDTO dto){
        RolePOJO role = roleService.create(dto);
        return new ResponseEntity<>(RoleDTO.from(role),HttpStatus.OK);
    }

}
