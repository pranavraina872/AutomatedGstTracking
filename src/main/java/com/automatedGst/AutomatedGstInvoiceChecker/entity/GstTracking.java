package com.automatedGst.AutomatedGstInvoiceChecker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GstTracking {
    private  double amount;
    private double gstIn;
    private double byuerStatecode;
    private double sellerStatecode;
    private double gstTaxPercentage;
    private double gstTaxAmount;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
