package com.cmms.demo.dto;

import com.cmms.demo.domain.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private String invoice_code;
    private String project_code;
    private CustomerDTO customer;
    private Long total_amount;
    private Long advance_amount;
    private String create_date;
    private PaymentStatusDTO status;
    private String evidence_image;

    public static InvoiceDTO from(Invoice invoice){
        InvoiceDTO dto = new InvoiceDTO();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dto.setCreate_date(format.format(invoice.getCreate_date()));
        dto.setProject_code(invoice.getProject().getProject_code());
        dto.setCustomer(CustomerDTO.from(invoice.getProject().getCustomer(), null));
        dto.setStatus(PaymentStatusDTO.from(invoice.getPaymentStatus()));
        dto.setInvoice_code(invoice.getInvoice_code());
        dto.setEvidence_image(invoice.getEvidence_image());
        dto.setTotal_amount(invoice.getTotal_amount());
        dto.setAdvance_amount(invoice.getAdvance_amount());
        return dto;
    }

}
