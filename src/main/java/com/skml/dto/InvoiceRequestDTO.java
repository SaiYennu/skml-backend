package com.skml.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequestDTO {

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    private Boolean addBankDetails;

    private List<InvoiceItemDTO> items;
}
