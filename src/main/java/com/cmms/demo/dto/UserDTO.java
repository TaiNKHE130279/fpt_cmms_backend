package com.cmms.demo.dto;

import com.cmms.demo.domain.RolePOJO;
import com.cmms.demo.domain.UserPOJO;
import com.cmms.demo.serviceImpl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String account;
    private String password;
    private Long account_status;
    private List<RoleDTO> role;
    private String token;
    private Long id;

    private List<UserApiDTO> functions;

    public List<RoleDTO> getRole() {
        return role;
    }

    public void setRole(List<RoleDTO> roles) {
        this.role = roles;
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

    public Long getAccount_status() {
        return account_status;
    }

    public void setAccount_status(Long account_status) {
        this.account_status = account_status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static UserDTO from(UserPOJO pojo) {
        UserDTO dto = new UserDTO();
        dto.setId(pojo.getUser_id());
        //dto.setFunctions(pojo.getAuthorities().stream().map(UserApiDTO::from).collect(Collectors.toList()));
        dto.setRole(pojo.getRolePOJOSet().stream().map(rolePOJO ->RoleDTO.fromWithOutDecentralization(rolePOJO)).collect(Collectors.toList()));
        dto.setAccount(pojo.getAccount());
        return dto;
    }
    public UserDTO convertToUserDTO(UserPOJO pojo, List<RolePOJO> get) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(pojo.getPassword());
        userDTO.setAccount(pojo.getAccount());
        userDTO.setToken(pojo.getToken());
        userDTO.setAccount_status(pojo.getAccount_status().getStatus_id());
        userDTO.setRole(get.stream().map(rolePOJO ->RoleDTO.from(rolePOJO)).collect(Collectors.toList()));
        userDTO.setId(pojo.getUser_id());
        //userDTO.setFunctions(pojo.getAuthorities().stream().map(UserApiDTO::from).collect(Collectors.toList()));
        return userDTO;
    }

}
