package com.automatedGst.AutomatedGstInvoiceChecker.controller;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.service.GstTrackingService;
import com.automatedGst.AutomatedGstInvoiceChecker.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/gst-ui")
public class GstUIController {

    private final GstTrackingService service;

    @Autowired
    private PdfService pdfService;

    public GstUIController(GstTrackingService service) {
        this.service = service;
    }

    // Dashboard
    @GetMapping
    public String home(Model model) {
        model.addAttribute("gstList", service.findAll()); // ✅ FIX
        return "dashboard";
    }

    // New Form
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("gst", new GstTracking());
        return "form";
    }

    // Save + Validate
    @PostMapping("/save")
    public String save(@ModelAttribute GstTracking gst, Model model) {

        GstTracking result = service.validateAndProcess(gst);

        model.addAttribute("gst", result);

        return "form"; // stay on form after calculation
    }

    //  Edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("gst", service.findById(id));
        return "form";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/gst-ui";
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<Resource> generatePdf(@PathVariable Long id) {

        GstTracking gst = service.findById(id);

        String filePath = pdfService.generateReport(gst);

        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error while downloading PDF");
        }
    }


}

