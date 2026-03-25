package com.automatedGst.AutomatedGstInvoiceChecker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GstTracking {


    private Double amount;
    private Double gstAmount;
    private String buyerStateCode;
    private String sellerStateCode;
    private Double gstTaxPercentage;
    private String gstIn;
    private Double totalAmount;
    private Double igst;
    private Double cgst;
    private Double sgst;
    private String complianceStatus;
    private String complianceRemarks;
    private String riskLevel;
    private Integer complianceScore;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


}
