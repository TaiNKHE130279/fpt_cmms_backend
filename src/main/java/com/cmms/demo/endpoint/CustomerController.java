package com.cmms.demo.endpoint;

import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.dto.CustomerDTO;
import com.cmms.demo.dto.CustomerOutput;
import com.cmms.demo.service.CustomerService;
import com.cmms.demo.serviceImpl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/customer", name = "Quản lý khách hàng")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @PostMapping(name = "Thêm khách hàng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'POST', this)}")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO dto) {
        return new ResponseEntity<>(service.addCustomer(dto), HttpStatus.OK);
    }

    @GetMapping(name = "Xem danh sách khách hàng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'GET', this)}")
    public ResponseEntity<CustomerOutput> filter(@RequestParam("page") int pageIndex,
                                                 @RequestParam("limit") int pageSize,
                                                 @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "address", required = false) String address) {
        return new ResponseEntity<>(service.filter(pageIndex, pageSize, name, phone, address), HttpStatus.OK);
    }

    @GetMapping(value = "/getOne/{customerCode}", name = "Xem chi tiết khách hàng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '/getOne', 'GET', this)}")
    public ResponseEntity<CustomerDTO> getOne(@PathVariable final String customerCode) {
        return new ResponseEntity<>(service.getOne(customerCode), HttpStatus.OK);
    }

    @PutMapping(name = "Cập nhật thông tin khách hàng")
    // @PreAuthorize("{@appAuthorizer.authorize(authentication, '', 'UPDATE', this)}")
    public ResponseEntity<CustomerDTO> update(@RequestBody CustomerDTO dto) {
        CustomerPOJO c = service.update(dto);
        return new ResponseEntity<>(CustomerDTO.from(c, null), HttpStatus.OK);
    }

    @GetMapping("/checkPhoneUnique")
    public ResponseEntity<CustomerDTO> checkPhoneUnique(@RequestParam("phone") String phone) {
        CustomerPOJO d = service.checkPhoneUnique(phone);
        if (d != null) {
            return new ResponseEntity<>(CustomerDTO.from(d, null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

}
