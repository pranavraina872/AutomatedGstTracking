package com.automatedGst.AutomatedGstInvoiceChecker.dto;

public class GstRequest {

    private String gstin;

    public GstRequest(String gstin) {
        this.gstin = gstin;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
