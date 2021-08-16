package com.cmms.demo.endpoint;

import com.cmms.demo.domain.MyMessage;
import com.cmms.demo.domain.RolePOJO;
import com.cmms.demo.domain.UserOutput;
import com.cmms.demo.domain.UserPOJO;
import com.cmms.demo.dto.RoleDTO;
import com.cmms.demo.dto.UserDTO;
import com.cmms.demo.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/user",name = "Quản lý người dùng")
@CrossOrigin("*")
public class UserController {
    Logger logger = LoggerFactory.getLogger(getClass());

    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/getOne','GET', this)}")
    @GetMapping(value = "getOne/{id}",name = "Xem thông tin 1 tài khoản.")
    public ResponseEntity<UserDTO> getOneUserById(@PathVariable Long id) {
        UserPOJO user = userDetailsService.getUserById(id);
        return new ResponseEntity<>(UserDTO.from(user), HttpStatus.OK);
    }

    @GetMapping(name = "Xem tất cả tài khoản.")
    @PreAuthorize("{@appAuthorizer.authorize(authentication, '','GET', this)}")
    public ResponseEntity<UserOutput> getAllUser(
            @RequestParam(value = "account",required = false) String acc,
            @RequestParam("page") int pageIndex,
            @RequestParam("size")int pageSize,
            @RequestParam(value = "role",required = false) Long role,
            @RequestParam(value = "status",required = false) Long status) {
        Page<UserPOJO> pojos = userDetailsService.getList(pageIndex,pageSize,acc,role,status);

        List<UserDTO> userDTOList = pojos.stream().map(
                userPOJO -> new UserDTO().convertToUserDTO(userPOJO, userDetailsService.getAllRByUserName(userPOJO.getAccount()))).collect(Collectors.toList());
        UserOutput  output = new UserOutput(pojos.getTotalPages(),userDTOList);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }


    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/update','POST', this)}")
    @PostMapping(value = "/update",name = "Cập nhật")
    public ResponseEntity<MyMessage> updateOneUser(@RequestBody UserDTO userDTO) {
        boolean isSuccess = userDetailsService.updateOneUser(userDTO);
        if (isSuccess) {
            return new ResponseEntity<>(new MyMessage("Update information sucess!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyMessage("Update information fail!"), HttpStatus.BAD_REQUEST);
        }
    }

    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/change-password','POST', this)}")
    @PostMapping(value = "/change-password",name = "Thay đổi mật khẩu")
    public ResponseEntity<MyMessage> changePassword(@RequestParam("oldPassword") String oldPassword,
                                                    @RequestParam("newPassword") String newPassword) {
        boolean isSuccess = userDetailsService.changePassword(oldPassword, newPassword);
        if (isSuccess) {
            return new ResponseEntity<>(new MyMessage("Change password sucess!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyMessage("Change password fail!"), HttpStatus.BAD_REQUEST);
        }

    }

    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/reset-password','POST', this)}")
    @PostMapping(value = "/reset-password/{id}",name = "Tạo lại mật khẩu")
    public ResponseEntity<MyMessage> resetPassword(@PathVariable Long id) {
        boolean isSuccess = userDetailsService.resetPassword(Long.valueOf(id));
        if (isSuccess) {
            return new ResponseEntity<>(new MyMessage("Password has been changed to default: 123456."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyMessage("Execution failed."), HttpStatus.BAD_REQUEST);
        }
    }

    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/remove','POST', this)}")
    @PostMapping(value = "/remove/{id}",name = "Khóa tài khoản")
    public ResponseEntity<MyMessage> removeUser(@PathVariable Long id) {
        boolean isSuccess = userDetailsService.changeStatusUser(Long.valueOf(id), "INACTIVE");
        if (isSuccess) {
            return new ResponseEntity<>(new MyMessage("Remove user sucess"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyMessage("Execution failed."), HttpStatus.BAD_REQUEST);
        }
    }
    //@PreAuthorize("{@appAuthorizer.authorize(authentication, '/unlock','POST', this)}")
    @PostMapping(value = "/unlock/{id}",name = "Mở khóa tài khoản")
    public ResponseEntity<MyMessage> unlockUser(@PathVariable Long id) {
        boolean isSuccess = userDetailsService.changeStatusUser(Long.valueOf(id), "ACTIVE");
        if (isSuccess) {
            return new ResponseEntity<>(new MyMessage("Remove user sucess"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyMessage("Execution failed."), HttpStatus.BAD_REQUEST);
        }
    }


    //@PreAuthorize("{@appAuthorizer.authorize(authentication, 'VIEW', this)}")
    @RequestMapping(value = "/register", method = RequestMethod.POST,name = "Đăng ký tài khoản")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
        UserPOJO currentUser = userDetailsService.getUserByName(userDetailsService.currentAccount());
        boolean isAdmin = false;
        boolean isOwner = false;
        for (RolePOJO rolePOJO : currentUser.getRolePOJOSet()) {
            if (rolePOJO.getRole_id() == 1) {
                isAdmin = true;
            }
            if (rolePOJO.getRole_id() == 2) {
                isOwner = true;
            }
        }
        List<RolePOJO> roles = new ArrayList<>();
        if (isAdmin) {
            roles.add(userDetailsService.findOneRoleById(2));
        }
        if (isOwner) {
            roles.add(userDetailsService.findOneRoleById(3));
        }
        List<RoleDTO> dtos = roles.stream().map(rolePOJO ->RoleDTO.from(rolePOJO)).collect(Collectors.toList());
        user.setRole(dtos);
        UserPOJO pojo = userDetailsService.save(user);

        return ResponseEntity.ok(new UserDTO().convertToUserDTO(pojo, userDetailsService.getAllRByUserName(pojo.getAccount())));
    }

    @Autowired
    private UserService userDetailsService;
}
