package com.automatedGst.AutomatedGstInvoiceChecker.service;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.repository.GstTrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    public class GstTrackingService {

        private final GstTrackingRepo repo;

        public GstTrackingService(GstTrackingRepo repository) {
            this.repo= repository;
        }

        public GstTracking save(GstTracking gstTracking) {
            calculateGst(gstTracking);
            return repo.save(gstTracking);
        }

        public List<GstTracking> findAll() {
            return repo.findAll();
        }

        public GstTracking findById(Long id) {
            return repo.findById(id).orElse(null);
        }

        public void deleteById(Long id) {
            repo.deleteById(id);
        }

        private void calculateGst(GstTracking gstTracking) {
            if (gstTracking.getAmount() != null && gstTracking.getGstTaxPercentage() != null) {
                double taxAmount = gstTracking.getAmount() * gstTracking.getGstTaxPercentage() / 100;
                gstTracking.setGstAmount(taxAmount);

                if (gstTracking.getBuyerStateCode() != null && gstTracking.getSellerStateCode() != null) {
                    if (gstTracking.getBuyerStateCode().equals(gstTracking.getSellerStateCode())) {
                        gstTracking.setGstAmount(taxAmount / 2); // CGST
                    } else {
                        gstTracking.setGstAmount(taxAmount);
                    }
                }
                gstTracking.setTotalAmount(gstTracking.getAmount() + taxAmount);
            }
        }
    }












