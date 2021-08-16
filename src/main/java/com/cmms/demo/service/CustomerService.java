package com.cmms.demo.service;

import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.dto.CustomerDTO;
import com.cmms.demo.dto.CustomerOutput;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerDTO addCustomer(CustomerDTO dto);

    CustomerOutput filter(int pageIndex, int pageSize, String name, String phone, String address);

    CustomerDTO getOne(String code);

    CustomerPOJO update(CustomerDTO dto);

    CustomerPOJO checkPhoneUnique(String phone);
}
