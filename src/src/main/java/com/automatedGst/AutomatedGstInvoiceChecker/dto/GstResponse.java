package com.automatedGst.AutomatedGstInvoiceChecker.dto;

public class GstResponse {

    private int code;
    private long timestamp;
    private GstWrapper data;
    private String transaction_id;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GstWrapper getData() {
        return data;
    }

    public void setData(GstWrapper data) {
        this.data = data;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
