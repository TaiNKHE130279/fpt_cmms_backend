package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.dto.CustomerDTO;
import com.cmms.demo.dto.CustomerOutput;
import com.cmms.demo.reponsitory.CustomerRepository;
import com.cmms.demo.reponsitory.ProjectRepository;
import com.cmms.demo.service.CustomerService;
import com.cmms.demo.specification.CustomerSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProvinceServiceImpl provinceServiceImpl;
    @Autowired
    private DistrictServiceImpl districtServiceImpl;
    @Autowired
    private CommuneServiceImpl communeServiceImpl;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public CustomerDTO addCustomer(CustomerDTO dto){
        CustomerPOJO customer = new CustomerPOJO();
        if(dto.getCustomer_code() != null){
            customer.setCustomer_code(dto.getCustomer_code());
        }else{
            int count = getMaxCustomerCodeNumber();
            customer.setCustomer_code("CUS-"+(count +1));
        }
        customer.setName(dto.getName());
        customer.setProvince(provinceServiceImpl.getOne(dto.getProvince_id()));
        customer.setDistrict(districtServiceImpl.getOne(dto.getDistrict_id()));
        customer.setCommune(communeServiceImpl.getOne(dto.getCommune_id()));
        customer.setPhone(dto.getPhone());
        customer.setDetail_address(dto.getDetail_address());
        customer.setId_number(dto.getId_number());
        if(dto.getDate_of_birth() != null) {
            java.util.Date dateOfBirth = null;
            try {
                dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDate_of_birth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            customer.setDate_of_birth(new Date(dateOfBirth.getTime()));
        }
        if(dto.getGender() != null) {
            customer.setGender(Integer.parseInt(dto.getGender()));
        }else{
            customer.setGender(0);
        }
        String detailAddress = "";
        if(dto.getDetail_address() != null){
            detailAddress = dto.getDetail_address() + ", " ;
        }
        customer.setAddress(detailAddress + customer.getCommune().getName() +
                ", "+ customer.getDistrict().getName() + ", " + customer.getProvince().getName());
        CustomerPOJO output = customerRepository.save(customer);
        return CustomerDTO.from(output, null);
    }

    public int getMaxCustomerCodeNumber(){
        List<CustomerPOJO> ls = customerRepository.findAll();
        List<Integer> integerLs = new ArrayList<>();
        for (int i= 0;i<ls.size();i++){
            String[] array = ls.get(i).getCustomer_code().split("-");
            integerLs.add(Integer.parseInt(array[1].toString()));
        }
        return getMaxNumber(integerLs);
    }

    public int getMaxNumber(List<Integer> ls){
        if(ls.size() == 0){
            return 0;
        }
        int max = ls.get(0);
        for(int i=1; i<ls.size();i++){
            if(max < ls.get(i)){
                max=ls.get(i);
            }
        }
        return max;
    }

    @Override
    public CustomerOutput filter(int pageIndex, int pageSize, String name, String phone, String address){
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<CustomerPOJO> page = customerRepository.findAll(
                Specification.where(CustomerSpecs.filter(name,phone, address)), pageable);
        List<CustomerPOJO> ls = page.toList();
        List<CustomerDTO> lsDto = ls.stream().map(item -> CustomerDTO.from(item, projectRepository)).collect(Collectors.toList());
        CustomerOutput output = new CustomerOutput();
        output.setTotalPages(page.getTotalPages());
        output.setCustomerList(lsDto);
        return output;
    }

    @Override
    public CustomerDTO getOne(String code){
        CustomerPOJO customer =  customerRepository.getOne(code);
        return CustomerDTO.from(customer, projectRepository);
    }

    @Override
    public CustomerPOJO update(CustomerDTO dto){
        CustomerPOJO customer = customerRepository.getOne(dto.getCustomer_code());
        if(customer != null){
            if(dto.getName() != null) {
                customer.setName(dto.getName());
            }
            if(dto.getProvince_id() != null) {
                customer.setProvince(provinceServiceImpl.getOne(dto.getProvince_id()));
            }
            if(dto.getDistrict_id() != null){
                customer.setDistrict(districtServiceImpl.getOne(dto.getDistrict_id()));
            }
            if(dto.getCommune_id() != null){
                customer.setCommune(communeServiceImpl.getOne(dto.getCommune_id()));
            }
            if(dto.getPhone() != null) {
                customer.setPhone(dto.getPhone());
            }
            if(dto.getDetail_address() != null){
                customer.setDetail_address(dto.getDetail_address());
            }
            if(dto.getDate_of_birth() != null){
                java.util.Date dateOfBirth = null;
                try {
                    dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDate_of_birth());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                customer.setDate_of_birth(new Date(dateOfBirth.getTime()));
            }
            customer.setGender(Integer.parseInt(dto.getGender()));
            customer.setId_number(dto.getId_number());
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public CustomerPOJO checkPhoneUnique(String phone){
        List<CustomerPOJO> ls = customerRepository.findAll();
        for(int i = 0; i<ls.size(); i++){
            CustomerPOJO d = ls.get(i);
            if(d.getPhone().equals(phone)){
                return d;
            }
        }
        return null;
    }
}
