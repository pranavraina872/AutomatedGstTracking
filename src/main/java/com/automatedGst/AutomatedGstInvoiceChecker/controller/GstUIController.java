package com.automatedGst.AutomatedGstInvoiceChecker.controller;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.service.GstTrackingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
    @RequestMapping("/gst-ui")
    public class GstUIController {

        private final GstTrackingService service;

        public GstUIController(GstTrackingService service) {
            this.service = service;
        }

        @GetMapping
        public String home(Model model) {
            model.addAttribute("gstList", service.findAll());
            return "dashboard";
        }

        @GetMapping("/new")
        public String newForm(Model model) {
            model.addAttribute("gst", new GstTracking());
            return "form";
        }

        @PostMapping("/save")
        public String save(@ModelAttribute GstTracking gst) {
            service.save(gst);
            return "redirect:/gst-ui";
        }

        @GetMapping("/edit/{id}")
        public String edit(@PathVariable Long id, Model model) {
            model.addAttribute("gst", service.findById(id));
            return "form";
        }

        @GetMapping("/delete/{id}")
        public String delete(@PathVariable Long id) {
            service.deleteById(id);
            return "redirect:/gst-ui";
        }

        @PostMapping("/validate")
        public String validate(@ModelAttribute GstTracking gst, Model model) {
            model.addAttribute("gst", service.validateAndProcess(gst));
            return "form";
        }
    }

