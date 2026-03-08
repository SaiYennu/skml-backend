package com.skml.service;

import java.util.List;

import com.skml.dto.InvoiceRequestDTO;
import com.skml.dto.InvoiceResponseDTO;
import com.skml.entity.Invoice;

public interface InvoiceService {

    Invoice createInvoice(InvoiceRequestDTO requestDTO);

    List<InvoiceResponseDTO> getAllInvoices();
}

