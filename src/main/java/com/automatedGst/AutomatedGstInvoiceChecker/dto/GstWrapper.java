package com.automatedGst.AutomatedGstInvoiceChecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GstWrapper {

    private GstData data;

    @JsonProperty("status_cd")
    private String statusCode;

    public GstData getData() {
        return data;
    }

    public void setData(GstData data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
