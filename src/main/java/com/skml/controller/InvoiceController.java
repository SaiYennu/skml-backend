package com.skml.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skml.dto.InvoiceRequestDTO;
import com.skml.dto.InvoiceResponseDTO;
import com.skml.entity.Invoice;
import com.skml.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // 🔐 JWT protected (SecurityConfig lo already set ayyi undali)
    @PostMapping
    public ResponseEntity<?> createInvoice(
            @RequestBody InvoiceRequestDTO requestDTO
    ) {
        Invoice invoice = invoiceService.createInvoice(requestDTO);
        return ResponseEntity.ok(invoice);
    }
    
    @GetMapping
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

}
