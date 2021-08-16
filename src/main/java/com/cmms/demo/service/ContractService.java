package com.cmms.demo.service;

import com.cmms.demo.domain.Contract;
import com.cmms.demo.dto.ContractDTO;
import com.cmms.demo.dto.ContractOutput;

public interface ContractService {
    Contract createContract(ContractDTO dto);

    Contract getOne(String code);

    ContractOutput filter(int pageIndex, int pageSize, String customerName, String date, Long paymentType, boolean isActive);

    Contract update(ContractDTO dto);

    Contract getOneByProjectCode(String projectCode);

    int cancelContract(String contractCode);
}
