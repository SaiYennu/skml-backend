package com.skml.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.skml.entity.Invoice;
import com.skml.entity.InvoiceItem;
import com.skml.exception.InvoiceGenerationException;

@Component
public class InvoicePdfGenerator {

    private static final String SHOP_NAME = "VINAY WORKS";
    private static final String GST_NUMBER = "36ABCDE1234F1Z5";
    private static final String SHOP_ADDRESS = "Hyderabad, Telangana";
    private static final String WEBSITE = "www.vinayworks.com";
    private static final String CONTACT = "9999999999";

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
            "<p>Account Name: Vinay Works</p>" +
            "<p>Account No: 1234567890</p>" +
            "<p>IFSC: HDFC0000123</p>";
    }

    return
        "<html>" +
        "<head>" +
        "<style>" +
        "body { font-family: Arial; font-size: 12px; }" +
        "table { width: 100%; border-collapse: collapse; }" +
        "th, td { border: 1px solid black; padding: 6px; }" +
        ".header { text-align: center; }" +
        ".footer { margin-top: 30px; text-align: center; }" +
        "</style>" +
        "</head>" +

        "<body>" +

        "<div class='header'>" +
        "<h2>" + SHOP_NAME + "</h2>" +
        "<p>GST: " + GST_NUMBER + "</p>" +
        "<p>" + SHOP_ADDRESS + " | " + WEBSITE + "</p>" +
        "</div>" +

        "<hr/>" +

        "<h3>Client Details</h3>" +
        "<p>Name: " + invoice.getCustomerName() + "</p>" +
        "<p>Phone: " + invoice.getCustomerPhone() + "</p>" +
        "<p>Address: " + invoice.getCustomerAddress() + "</p>" +
        "<p>Date: " + invoice.getDateCreated() + "</p>" +

        bankDetailsHtml +

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

        "<div class='footer'>" +
        "<p>Thank you for your business</p>" +
        "<p>Contact us: " + CONTACT + "</p>" +
        "</div>" +

        "</body></html>";
}

}
