package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.ContractDTO;
import com.cmms.demo.dto.ContractOutput;
import com.cmms.demo.reponsitory.BookingScheduleRepository;
import com.cmms.demo.reponsitory.ContractRepository;
import com.cmms.demo.reponsitory.ProjectRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.ContractService;
import com.cmms.demo.specification.ContractSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository repository;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BookingScheduleRepository bookingRepository;
    @Autowired
    private ScheduleDetailRepository detailRepository;
    @Autowired
    private ProjectStatusService projectStatusService;
    @Autowired
    private CustomerServiceImpl customerService;

    @Override
    public Contract createContract(ContractDTO dto) {
        Contract c = new Contract();
        if (dto.getContract_code() != null) {
            c.setContract_code(dto.getContract_code());
        } else {
            c.setContract_code("CONTRACT-" + dto.getProject_code());
        }
        ProjectPOJO projectPOJO = projectServiceImpl.getOne(dto.getProject_code());
        c.setProject(projectPOJO);
        c.setContract_price(dto.getContract_price());
        c.setAdvance_amount(dto.getAdvance_amount());
        c.setEvidence_image(dto.getEvidence_image());

        ContractType type = new ContractType();
        type.setType_id(dto.getType().getType_id());
        type.setType_name(dto.getType().getType_name());
        c.setType(type);

        c.setCreate_date(new Date(System.currentTimeMillis()));


        if (dto.getEvidence_image() != null) {
            c.setEvidence_image(dto.getEvidence_image());
        }
        c.setContent(dto.getContent());
        c.setActive(true);

        return repository.save(c);
    }

    @Override
    public Contract getOne(String code) {
        return repository.getOne(code);
    }

    public Contract getByProjectCode(String code) {
        return repository.getByProjectCode(code);
    }

    @Override
    public ContractOutput filter(int pageIndex, int pageSize, String customerName, String date, Long paymentType, boolean isActive) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Contract> page = repository.findAll(Specification.where(ContractSpecs.filter(customerName, date, paymentType, isActive)), pageable);
        List<Contract> ls = page.toList();
        List<ContractDTO> lsDto = ls.stream().map(ContractDTO::from).collect(Collectors.toList());
        ContractOutput output = new ContractOutput();
        output.setTotalPages(page.getTotalPages());
        output.setContractList(lsDto);
        return output;
    }

    @Override
    public Contract update(ContractDTO dto) {
        Contract item = getOne(dto.getContract_code());
        if (item != null) {
            if (dto.getCustomer() != null && dto.getCustomer().getCustomer_code() != null) {
                CustomerPOJO customer = customerService.update(dto.getCustomer());
            }
            if (dto.getContract_price() != null) {
                item.setContract_price(dto.getContract_price());
            }
            if (dto.getAdvance_amount() != null) {
                item.setAdvance_amount(dto.getAdvance_amount());
            }
            if (dto.getEvidence_image() != null) {
                item.setEvidence_image(dto.getEvidence_image());
            }
            if (dto.getType() != null) {
                ContractType type = new ContractType();
                type.setType_id(dto.getType().getType_id());
                type.setType_name(dto.getType().getType_name());
                item.setType(type);
            }
            if (dto.getContent() != null) {
                item.setContent(dto.getContent());
            }
            return repository.save(item);
        }
        return null;
    }

    @Override
    public Contract getOneByProjectCode(String code) {
        return repository.getByProjectCode(code);
    }

    @Override
    public int cancelContract(String contractCode) {
        Contract contract = getOne(contractCode);
        if (contract != null) {
            ProjectPOJO project = contract.getProject();
            project.setProjectStatus(projectStatusService.getOne(4));
            projectRepository.save(project);
            List<BookingScheduleDetail> lsDetail = detailRepository.getDetailByProjectCode(contract.getProject().getProject_code());
            for (int i = 0; i < lsDetail.size(); i++) {
                BookingScheduleDetail d = lsDetail.get(i);
                int delete = detailRepository.deleteByDetailId(d.getId());
            }
            List<BookingSchedule> lsBooking = bookingRepository.getListByProject(contract.getProject().getProject_code());
            for (int j = 0; j < lsBooking.size(); j++) {
                BookingSchedule b = lsBooking.get(j);
                int delete = bookingRepository.deleteByBookingId(b.getId());
            }
            contract.setActive(false);
            repository.save(contract);
            return 1;
        }
        return 0;
    }
}
