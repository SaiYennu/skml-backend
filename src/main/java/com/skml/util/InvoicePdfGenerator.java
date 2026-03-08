package com.skml.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.skml.entity.Invoice;
import com.skml.entity.InvoiceItem;
import com.skml.exception.InvoiceGenerationException;

@Component
public class InvoicePdfGenerator {

    // ===== Shop Details =====
    @Value("${invoice.shop.name}")
    private String shopName;

    @Value("${invoice.shop.gst}")
    private String gstNumber;

    @Value("${invoice.shop.address}")
    private String shopAddress;

    @Value("${invoice.shop.website}")
    private String website;

    @Value("${invoice.shop.contact}")
    private String contact;

    // ===== Bank Details =====
    @Value("${invoice.bank.name}")
    private String bankName;

    @Value("${invoice.bank.accountName}")
    private String bankAccountName;

    @Value("${invoice.bank.accountNo}")
    private String bankAccountNo;

    @Value("${invoice.bank.ifsc}")
    private String bankIfsc;

    public File generatePdf(Invoice invoice) {

        try {
            String fileName = "INVOICE_" +
                    invoice.getDateCreated().format(DateTimeFormatter.ISO_DATE)
                    + "_" + invoice.getId() + ".pdf";

            File pdfFile = new File(System.getProperty("java.io.tmpdir"), fileName);

            String html = buildHtml(invoice);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();

            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                renderer.createPDF(fos);
            }

            return pdfFile;

        } catch (DocumentException | java.io.IOException e) {
            throw new InvoiceGenerationException("PDF generation failed", e);
        }
    }

    private String buildHtml(Invoice invoice) {

        StringBuilder rows = new StringBuilder();

        for (InvoiceItem item : invoice.getItems()) {
            rows.append("<tr>")
                .append("<td>").append(item.getDescription()).append("</td>")
                .append("<td>").append(item.getQuantity() != null ? item.getQuantity() : "-").append("</td>")
                .append("<td>").append(item.getUnitPrice() != null ? item.getUnitPrice() : "-").append("</td>")
                .append("<td>").append(item.getSubtotal()).append("</td>")
                .append("</tr>");
        }

        String bankDetailsHtml = "";
        if (Boolean.TRUE.equals(invoice.getAddBankDetails())) {
            bankDetailsHtml =
                "<h3>Bank Details</h3>" +
                "<p>Bank Name: " + bankName + "</p>" +
                "<p>Account Name: " + bankAccountName + "</p>" +
                "<p>Account No: " + bankAccountNo + "</p>" +
                "<p>IFSC: " + bankIfsc + "</p>";
        }

        return
            "<html>" +
            "<head>" +
            "<style>" +
            "body { font-family: Arial; font-size: 12px; margin-bottom: 120px; }" +
            "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
            "th, td { border: 1px solid black; padding: 6px; text-align: left; }" +
            ".header { text-align: center; }" +
            ".footer { position: fixed; bottom: 20px; width: 100%; text-align: center; font-size: 11px; }" +
            "</style>" +
            "</head>" +

            "<body>" +

            // ===== Header =====
            "<div class='header'>" +
            "<h2>" + shopName + "</h2>" +
            "<p>PAN / GST No: " + gstNumber + "</p>" +
            "<p>" + shopAddress + " | " + website + "</p>" +
            "</div>" +

            "<hr/>" +

            // ===== Client Details =====
            "<h3>Client Details</h3>" +
            "<p>Name: " + invoice.getCustomerName() + "</p>" +
            "<p>Phone: " + invoice.getCustomerPhone() + "</p>" +
            "<p>Address: " + invoice.getCustomerAddress() + "</p>" +
            "<p>Date: " + invoice.getDateCreated() + "</p>" +

            // ===== Bank Details (Optional) =====
            bankDetailsHtml +

            // ===== Items Table =====
            "<table>" +
            "<thead>" +
            "<tr>" +
            "<th>Description</th>" +
            "<th>Qty</th>" +
            "<th>Unit Price</th>" +
            "<th>Subtotal</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            rows +
            "</tbody>" +
            "</table>" +

            "<h3>Total Amount: " + invoice.getTotalAmount() + "</h3>" +

            // ===== Footer =====
            "<div class='footer'>" +
            "<hr/>" +
            "<p><b>Thank you for your business</b></p>" +
            "<p>Contact us: " + contact + "</p>" +
            "</div>" +

            "</body></html>";
    }
}
