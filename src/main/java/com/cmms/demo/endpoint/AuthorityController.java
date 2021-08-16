package com.cmms.demo.endpoint;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.*;
import com.cmms.demo.serviceImpl.AuthorityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/decentralization")
@CrossOrigin("*")
public class AuthorityController {
    @Autowired
    AuthorityServiceImpl service;


    @PostMapping("/update/{id}")
    public ResponseEntity<List<UserApiDTO>> updateByUserId(@PathVariable("id") Long id, @RequestBody List<Long> actions) {
        List<UserApi> list = service.update(id, actions);
        List<UserApiDTO> dtos = list.stream().map(UserApiDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/get-all-api")
    public ResponseEntity<List<ApiDTO>> getAll() {
        List<Api> apis = service.getAll();
        return new ResponseEntity<>(apis.stream().map(ApiDTO::from1).collect(Collectors.toList()), HttpStatus.OK);
    }

        @PostMapping("/add-action/{api}")
    public ResponseEntity<ApiDTO> addActionForApi(@PathVariable("api") Long id_api, @RequestBody List<String> actions) {
        Api api = service.addActionForApi(id_api, actions);
        return new ResponseEntity<>(ApiDTO.from(api), HttpStatus.OK);
    }
}
