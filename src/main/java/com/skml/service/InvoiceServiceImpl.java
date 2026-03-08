package com.skml.service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skml.dto.InvoiceItemDTO;
import com.skml.dto.InvoiceRequestDTO;
import com.skml.dto.InvoiceResponseDTO;
import com.skml.entity.Invoice;
import com.skml.entity.InvoiceItem;
import com.skml.exception.InvoiceGenerationException;
import com.skml.repository.InvoiceRepository;
import com.skml.util.CloudinaryService;
import com.skml.util.InvoicePdfGenerator;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoicePdfGenerator invoicePdfGenerator;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public Invoice createInvoice(InvoiceRequestDTO requestDTO) {

        try {
            // 1️⃣ Invoice create
            Invoice invoice = new Invoice();
            invoice.setCustomerName(requestDTO.getCustomerName());
            invoice.setCustomerPhone(requestDTO.getCustomerPhone());
            invoice.setCustomerAddress(requestDTO.getCustomerAddress());
            invoice.setAddBankDetails(requestDTO.getAddBankDetails());
            invoice.setDateCreated(LocalDate.now());

            // 2️⃣ Items + total calculation
            List<InvoiceItem> items = new ArrayList<>();
            double totalAmount = 0;

            for (InvoiceItemDTO dto : requestDTO.getItems()) {

                InvoiceItem item = new InvoiceItem();
                item.setDescription(dto.getDescription());
                item.setQuantity(dto.getQuantity());
                item.setUnitPrice(dto.getUnitPrice());
                item.setSubtotal(dto.getSubtotal());
                item.setInvoice(invoice);

                totalAmount += dto.getSubtotal();
                items.add(item);
            }

            invoice.setItems(items);
            invoice.setTotalAmount(totalAmount);

            // 3️⃣ Save invoice (ID generate avvali)
            Invoice savedInvoice = invoiceRepository.save(invoice);

            // 4️⃣ Generate PDF
            File pdfFile = invoicePdfGenerator.generatePdf(savedInvoice);

            // 5️⃣ Upload to Cloudinary
            String cloudinaryUrl = cloudinaryService.uploadPdf(pdfFile);

            // 6️⃣ Update pdfPath
            savedInvoice.setPdfPath(cloudinaryUrl);
            invoiceRepository.save(savedInvoice);

            return savedInvoice;

        } catch (Exception e) {
            // ❌ any error → FULL rollback
            throw new InvoiceGenerationException("Invoice creation failed", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getAllInvoices() {

        return invoiceRepository.findAll().stream().map(inv -> {

            InvoiceResponseDTO dto = new InvoiceResponseDTO();
            dto.setId(inv.getId());
            dto.setCustomerName(inv.getCustomerName());
            dto.setCustomerPhone(inv.getCustomerPhone());
            dto.setTotalAmount(inv.getTotalAmount());
            dto.setPdfPath(inv.getPdfPath());
            dto.setDateCreated(inv.getDateCreated());
            return dto;
        }).toList();
    }

}
