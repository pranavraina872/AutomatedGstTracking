package com.automatedGst.AutomatedGstInvoiceChecker.service;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PdfService {

    public String generateReport(GstTracking gst) {

        String fileName = "gst_report_" + gst.getId() + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            document.add(new Paragraph("GST Compliance Report"));
            document.add(new Paragraph("-----------------------------------"));

            document.add(new Paragraph("GSTIN: " + gst.getGstIn()));
            document.add(new Paragraph("Amount: " + gst.getAmount()));

            document.add(new Paragraph("IGST: " + gst.getIgst()));
            document.add(new Paragraph("CGST: " + gst.getCgst()));
            document.add(new Paragraph("SGST: " + gst.getSgst()));

            document.add(new Paragraph("Total Amount: " + gst.getTotalAmount()));

            document.add(new Paragraph("-----------------------------------"));

            document.add(new Paragraph("Compliance Status: " + gst.getComplianceStatus()));
            document.add(new Paragraph("Remarks: " + gst.getComplianceRemarks()));
            document.add(new Paragraph("Risk Level: " + gst.getRiskLevel()));
            document.add(new Paragraph("Compliance Score: " + gst.getComplianceScore()));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
