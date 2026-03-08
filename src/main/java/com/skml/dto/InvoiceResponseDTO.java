package com.skml.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InvoiceResponseDTO {

    private Long id;
    private String customerName;
    private String customerPhone;
    private Double totalAmount;
    private String pdfPath;
    private LocalDate dateCreated; 
    
}
