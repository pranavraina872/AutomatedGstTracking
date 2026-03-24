package com.automatedGst.AutomatedGstInvoiceChecker.mapper;

import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstData;
import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;

public class MapToEntity {

    private GstTracking mapToEntity(GstData data) {

        GstTracking entity = new GstTracking();

       // entity.setGstIn(data.getGstIn());
        //entity.setLegalName(data.getLegalName());
        //entity.setState(data.getState());

        return entity;
    }
}
