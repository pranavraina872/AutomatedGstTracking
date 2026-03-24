package com.automatedGst.AutomatedGstInvoiceChecker.repository;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GstTrackingRepo extends JpaRepository<GstTracking, Long> {
}
