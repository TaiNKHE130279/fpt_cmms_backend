package com.cmms.demo.serviceImpl;

import com.cmms.demo.config.JwtTokenUtil;
import com.cmms.demo.domain.*;
import com.cmms.demo.dto.UserDTO;
import com.cmms.demo.reponsitory.UserStatusRepository;
import com.cmms.demo.reponsitory.DriverRepository;
import com.cmms.demo.reponsitory.RoleRepository;
import com.cmms.demo.reponsitory.UserRepository;
import com.cmms.demo.service.UserService;
import com.cmms.demo.specification.UserSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    JwtTokenUtil tokenUtil;
    @Autowired
    UserStatusRepository statusRepository;
    @Override
    public UserPOJO getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserPOJO user = repository.findByAccount(s);
        if (user == null) {
            throw new UsernameNotFoundException("Tài khoản không khả dụng với tên: " + s);
        }
        if (user.getAccount_status().getStatus_name().equalsIgnoreCase("INACTIVE")) {
            throw new UsernameNotFoundException("Tài khoản hiện đang bị khóa, vui lòng liên hệ quản lý. ");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<RolePOJO> roles = getAllRByUserName(user.getAccount());
        for (RolePOJO role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole_name()));
        }
        return new CustomUser(user.getAccount(), user.getPassword(),
                authorities, user.getUser_id());
    }
    @Override
    public List<RolePOJO> getAllRByUserName(String name) {
        return roleRepository.getAllRoleByUserName(name);
    }

    @Override
    public RolePOJO findOneRoleById(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    public UserStatus getByStatusName(String name) {
        return statusRepository.findByStatusName(name);
    }
    @Override
    public UserPOJO save(UserDTO user) {
        UserPOJO newUser = new UserPOJO();
        newUser.setAccount(user.getAccount());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setAccount_status(getByStatusName("ACTIVE"));
        List<RolePOJO> roles = user.getRole().stream().map(RolePOJO::convertToEntity).collect(Collectors.toList());
        newUser.setRolePOJOSet(roles);
        UserPOJO pojo = repository.save(newUser);
        return pojo;
    }

    public List<UserPOJO> findAll(){
        return repository.findAll();
    }
    @Override
    public Page<UserPOJO> getList(int pageIndex, int pageSize, String account, Long roleId, Long statusId) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<UserPOJO> pUsers = repository.findAll(Specification.where(UserSpecs.filter(account, roleId, statusId)), pageable);
        return pUsers;
    }

    public UserPOJO saveToken(String token, UserPOJO u) {
        u.setToken(token);
        return repository.save(u);
    }

    @Override
    public boolean updateOneUser(UserDTO userDTO) {
        String currentUserName = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();

        }
        if (currentUserName.equals(userDTO.getAccount())) {
            if (currentUserName != null) {
                UserPOJO pojo = repository.findByAccount(currentUserName);
                if (pojo.getAccount().isEmpty() || pojo.getAccount() == null) {
                    return false;
                } else {
                    pojo.setAccount(userDTO.getAccount());
                    pojo.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
                    repository.save(pojo);
                    return true;
                }
            }
            {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        String account = currentAccount();
        UserPOJO pojo = repository.findByAccount(account);
        if (bcryptEncoder.matches(oldPassword, pojo.getPassword())) {
            pojo.setPassword(bcryptEncoder.encode(newPassword));
            repository.save(pojo);
            return true;
        }
        return false;
    }

    @Override
    public String currentAccount() {
        String currentUserName = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();

        }

        if (currentUserName != null) {
            return currentUserName;
        }
        return null;
    }
    @Override
    public UserPOJO getUserByName(String currentUserName) {
        return repository.findByAccount(currentUserName);
    }
    @Override
    public boolean resetPassword(Long id) {
        UserPOJO userPOJO = repository.findById(id).orElse(null);
        if (!userPOJO.getAccount().isEmpty() || userPOJO.getAccount() != null) {
            userPOJO.setPassword(bcryptEncoder.encode("123456"));
            repository.save(userPOJO);
            return true;
        }
        return false;
    }

    @Override
    // statusName = ACTIVE or INACTIVE
    public boolean changeStatusUser(Long id, String statusName) {
        UserPOJO userPOJO = repository.findById(id).orElse(null);
        if (!userPOJO.getAccount().isEmpty() || userPOJO.getAccount() != null) {
            userPOJO.setAccount_status(statusRepository.findByStatusName(statusName));
            repository.save(userPOJO);
            if(statusName == "ACTIVE"){
                DriverPOJO driver = driverRepository.getDriverByUserId(id);
                driver.setActive(true);
                driverRepository.save(driver);
            }
            List<RolePOJO> roles = userPOJO.getRolePOJOSet();
            if (checkRoleOfUser(roles)) {
                DriverPOJO driver = driverRepository.getDriverByUserId(id);
                if (driver != null) {
                    int isSuccess = driverRepository.updateMachineForDriver(null, driver.getDrive_code());
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkRoleOfUser(List<RolePOJO> ls) {
        for (int i = 0; i < ls.size(); i++) {
            if (ls.get(i).getRole_name().equals("ROLE_DRIVER")) {
                return true;
            }
        }
        return false;
    }


    public void logOut() {
        String account = currentAccount();
        UserPOJO pojo = repository.findByAccount(account);
        pojo.setToken("");
        repository.save(pojo);
    }
    public boolean isStaff(UserPOJO userPOJO){
        return userPOJO.getRolePOJOSet().stream().allMatch(rolePOJO -> rolePOJO.getRole_id() != 1 && rolePOJO.getRole_id() != 2);
    }
}
