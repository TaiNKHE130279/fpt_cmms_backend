package com.cmms.demo.service;

import com.cmms.demo.domain.Invoice;
import com.cmms.demo.dto.DataToFillInvoiceDTO;
import com.cmms.demo.dto.InvoiceDTO;
import com.cmms.demo.dto.InvoiceOutput;

public interface InvoiceService {
    Invoice create(InvoiceDTO dto);

    DataToFillInvoiceDTO getOne(String invoiceCode);

    InvoiceOutput filter(int pageIndex, int pageSize, String cusName, String date, Long paymentType);

    DataToFillInvoiceDTO fillDataToInvoice(String projectCode);

    Invoice getByProjectCode(String project_code);

    String update(DataToFillInvoiceDTO dto, String invoiceCode);
}
