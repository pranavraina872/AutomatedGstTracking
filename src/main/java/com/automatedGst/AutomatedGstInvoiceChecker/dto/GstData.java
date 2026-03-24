package com.automatedGst.AutomatedGstInvoiceChecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GstData {

    private String gstin;

    @JsonProperty("lgnm")
    private String legalName;

    @JsonProperty("tradeNam")
    private String tradeName;

    private String sts; // status
    private String dty; // taxpayer type

    @JsonProperty("rgdt")
    private String registrationDate;

    @JsonProperty("cxdt")
    private String cancellationDate;

    @JsonProperty("nba")
    private List<String> natureOfBusiness;

    private Pradr pradr;

    // getters & setters


    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getDty() {
        return dty;
    }

    public void setDty(String dty) {
        this.dty = dty;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public List<String> getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(List<String> natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public Pradr getPradr() {
        return pradr;
    }

    public void setPradr(Pradr pradr) {
        this.pradr = pradr;
    }
}
