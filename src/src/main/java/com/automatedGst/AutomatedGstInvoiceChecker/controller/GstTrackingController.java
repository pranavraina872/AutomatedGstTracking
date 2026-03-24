package com.automatedGst.AutomatedGstInvoiceChecker.controller;

import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstData;
import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.service.GstService;
import com.automatedGst.AutomatedGstInvoiceChecker.service.GstTrackingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequestMapping("/api/gst")
    public class GstTrackingController {

    private final GstTrackingService service;


        public GstTrackingController(GstTrackingService service) {
            this.service = service;
        }

        @GetMapping
        public List<GstTracking> getAll() {
            return service.findAll();
        }

        @GetMapping("/{id}")
        public GstTracking getById(@PathVariable Long id) {
            return service.findById(id);
        }

        @PostMapping
        public GstTracking create(@RequestBody GstTracking gstTracking) {
            return service.save(gstTracking);
        }

        @PutMapping("/{id}")
        public GstTracking update(@PathVariable Long id, @RequestBody GstTracking gstTracking) {
            gstTracking.setId(id);
            return service.save(gstTracking);
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            service.deleteById(id);
        }

    @PostMapping("/validate")
    public GstTracking validateAndSave(@RequestBody GstTracking gstTracking) {
        return service.validateAndProcess(gstTracking);
    }
    }


