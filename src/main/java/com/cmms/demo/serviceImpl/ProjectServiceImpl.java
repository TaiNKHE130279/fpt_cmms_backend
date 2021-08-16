package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.*;
import com.cmms.demo.dto.ProjectDTO;
import com.cmms.demo.dto.ProjectOutput;
import com.cmms.demo.dto.ProjectResponseDTO;
import com.cmms.demo.reponsitory.BookingScheduleRepository;
import com.cmms.demo.reponsitory.CustomerRepository;
import com.cmms.demo.reponsitory.ProjectRepository;
import com.cmms.demo.reponsitory.ScheduleDetailRepository;
import com.cmms.demo.service.ProjectService;
import com.cmms.demo.specification.ProjectSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectTypeServiceImpl typeService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectStatusService statusService;
    @Autowired
    private BookingScheduleRepository bookingRepository;
    @Autowired
    private ContractServiceImpl contractService;
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private ScheduleDetailServiceImpl detailService;
    @Autowired
    private ScheduleDetailRepository detailRepository;

    @Override
    public ProjectPOJO addProject(ProjectDTO dto) {
        ProjectPOJO project = new ProjectPOJO();
        if (dto.getProject_code() != null) {
            project.setProject_code(dto.getProject_code());
        } else {
            int count = getMaxProjectCodeNumber();
            project.setProject_code("PRJ-" + (count + 1));
        }
        project.setProject_name(dto.getProject_name());
        try {
            if (dto.getExpected_starting_date() != null && dto.getExpected_end_date() != null) {
                java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getExpected_starting_date());
                java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getExpected_end_date());
                project.setExpected_starting_date(new Date(fromDate.getTime()));
                project.setExpected_end_date(new Date(toDate.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CustomerPOJO customer = customerRepository.getOne(dto.getCustomer().getCustomer_code());
        project.setCustomer(customer);

        ProjectType type = typeService.getOne(dto.getProject_type());
        project.setProjectType(type);

        ProjectStatus status = statusService.getOne(dto.getProject_status());
        project.setProjectStatus(status);

        return projectRepository.save(project);
    }

    public int getMaxProjectCodeNumber() {
        List<ProjectPOJO> ls = projectRepository.findAll();
        List<Integer> integerLs = new ArrayList<>();
        for (int i = 0; i < ls.size(); i++) {
            String[] array = ls.get(i).getProject_code().split("-");
            integerLs.add(Integer.parseInt(array[1].toString()));
        }
        return getMaxNumber(integerLs);
    }


    public int getMaxNumber(List<Integer> ls) {
        if (ls.size() == 0) {
            return 0;
        }
        int max = ls.get(0);
        for (int i = 1; i < ls.size(); i++) {
            if (max < ls.get(i)) {
                max = ls.get(i);
            }
        }
        return max;
    }

    @Override
    public ProjectPOJO getOne(String code) {
        return projectRepository.getOne(code);
    }

    @Override
    public ProjectOutput filter(int pageIndex, int pageSize, String customerName
            , Long projectType, Integer projectStatus) {
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<ProjectPOJO> page = projectRepository.findAll(Specification.where(ProjectSpecs.filter(customerName, projectType, projectStatus)), pageable);
        List<ProjectPOJO> ls = page.toList();
        for (int i = 0; i < ls.size(); i++) {
            ProjectPOJO project = ls.get(i);
            BookingSchedule b = bookingRepository.getOne(project.getProject_code(), Date.valueOf(currentDate));
            if (b != null && b.getProject().getProjectStatus().getId() != 3) {
                project.setProjectStatus(statusService.getOne(2));
                projectRepository.save(project);
            }
        }
        List<ProjectResponseDTO> lsDto = ls.stream().map(item -> ProjectResponseDTO.from(item, contractService, invoiceService, detailService))
                .collect(Collectors.toList());
        ProjectOutput output = new ProjectOutput();
        output.setTotalPages(page.getTotalPages());
        output.setCustomerList(lsDto);
        return output;
    }

    public void updateStartAndEndDateForProject() {
        List<ProjectPOJO> ls = projectRepository.findAll();
        for (int i = 0; i < ls.size(); i++) {
            ProjectPOJO p = ls.get(i);
            Date minDate = bookingRepository.getMinDateOfProject(p.getProject_code());
            p.setExpected_starting_date(minDate);
            Date maxDate = bookingRepository.getMaxDateOfProject(p.getProject_code());
            p.setExpected_end_date(maxDate);
            projectRepository.save(p);
        }
    }

    @Override
    public ProjectPOJO update(ProjectDTO dto) {
        ProjectPOJO item = getOne(dto.getProject_code());
        if (item != null) {
            if (dto.getProject_name() != null) {
                item.setProject_name(dto.getProject_name());
            }
            if (dto.getProject_status() != null) {
                ProjectStatus status = statusService.getOne(dto.getProject_status());
                item.setProjectStatus(status);
            }
            if (dto.getExpected_starting_date() != null) {
                try {
                    java.util.Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getExpected_starting_date());
                    item.setExpected_starting_date(new Date(fromDate.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (dto.getExpected_end_date() != null) {
                try {
                    java.util.Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getExpected_end_date());
                    item.setExpected_starting_date(new Date(toDate.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (dto.getProject_type() != null) {
                ProjectType type = typeService.getOne(dto.getProject_type());
                item.setProjectType(type);
            }
            return projectRepository.save(item);
        }
        return null;
    }

    @Override
    public String cancelProject(String projectCode){
        ProjectPOJO project = projectRepository.getOne(projectCode);
        project.setProjectStatus(statusService.getOne(4));
        ProjectPOJO p = projectRepository.save(project);
        List<BookingScheduleDetail> lsDetail = detailRepository.getDetailByProjectCode(projectCode);
        for (int i=0; i<lsDetail.size(); i++){
            BookingScheduleDetail d = lsDetail.get(i);
            int delete = detailRepository.deleteByDetailId(d.getId());
        }
        List<BookingSchedule> lsBooking = bookingRepository.getListByProject(projectCode);
        for(int j=0; j<lsBooking.size(); j++){
            BookingSchedule b = lsBooking.get(j);
            int delete = bookingRepository.deleteByBookingId(b.getId());
        }
        if(p != null){
            return "success";
        }else{
            return "fail";
        }
    }
}
