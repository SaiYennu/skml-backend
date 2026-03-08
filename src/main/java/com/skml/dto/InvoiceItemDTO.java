package com.skml.dto;

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
public class InvoiceItemDTO {

    private String description;

    private Integer quantity;   // optional

    private Double unitPrice;   // optional

    private Double subtotal;    // required
}
