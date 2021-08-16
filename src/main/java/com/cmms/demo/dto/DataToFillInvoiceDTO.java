package com.cmms.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataToFillInvoiceDTO {
    private CustomerDTO customer;
    private List<InvoiceDetailDTO> detail;
    private String total_work_time;
    private Long amount;
    private Long advance_amount;
    private String content;
    private Long price_per_hour;
    private PaymentStatusDTO payment_status;
    private String create_date;
    private List<DailyFinanceReportDto> lsReceipt = new ArrayList<>();

}
