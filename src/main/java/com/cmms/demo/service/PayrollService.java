package com.cmms.demo.service;

import com.cmms.demo.domain.BonusPOJO;
import com.cmms.demo.domain.Payroll;
import com.cmms.demo.domain.PayrollDetail;
import com.cmms.demo.dto.BonusDTO;
import com.cmms.demo.dto.PayrollDTO;
import com.cmms.demo.dto.PayrollDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayrollService {

    Page<Payroll> filter(int pageIndex, int pageSize, String payroll_name, Integer month, Integer year, Long status);

    List<Payroll> findAll();

    Payroll create(PayrollDTO dto);

    Payroll updatePayroll(PayrollDTO dto, Long id);

    PayrollDetail updateDetail(Long id, PayrollDetailDTO payrollDetailDTO);

    Payroll salaryClosing(PayrollDTO dto);

    PayrollDetail getOneDetail(Long id);

    Payroll getOnePayroll(Long id);

    List<PayrollDetail> getAllDetail(Long id);

    PayrollDetail updateOneDetail(Long detailId, PayrollDetailDTO detailDTO);

    PayrollDetail removeDetail(Long id);

    Payroll updateDataPayroll(Long id);

    PayrollDetail paymentPayrollDetail(Long id, Long value);

    Payroll deletePayroll(Long id);

    PayrollDetail getOneDetailOfDriver(String driver_id, Long payroll_id);

    List<Payroll> findAllPayrollHasDriver(String driver_id);

}
