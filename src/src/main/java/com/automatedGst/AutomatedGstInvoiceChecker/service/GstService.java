package com.automatedGst.AutomatedGstInvoiceChecker.service;

import com.automatedGst.AutomatedGstInvoiceChecker.config.SandboxConfig;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstData;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstRequest;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstResponse;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstWrapper;
import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.exception.GstApiException;
import com.automatedGst.AutomatedGstInvoiceChecker.repository.GstTrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GstService {

    @Autowired
    private GstTrackingRepo gstRepository;

    private final SandboxConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public GstService(SandboxConfig config) {
        this.config = config;
    }

    public GstTracking fetchAndSave(String gstin) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", config.getToken());
            headers.set("x-api-key", config.getApiKey());

            GstRequest request = new GstRequest(gstin);

            HttpEntity<GstRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<GstResponse> response = restTemplate.exchange(
                    config.getFullUrl(),
                    HttpMethod.POST,
                    entity,
                    GstResponse.class
            );



            if (response.getBody() == null) {
                throw new RuntimeException("Empty response from GST API");
            }


            GstData data = response.getBody().getData().getData();

            GstTracking gstTracking = mapToEntity(data);
            return gstRepository.save(gstTracking);


        } catch (HttpClientErrorException.BadRequest ex) {


            String responseBody = ex.getResponseBodyAsString();

            if (responseBody.contains("Invalid GSTIN pattern")) {
                throw new GstApiException("Entered GSTIN is invalid. Please check format.");
            }

            throw new GstApiException("Bad Request from GST API: " + responseBody);

        } catch (HttpClientErrorException ex) {

            throw new GstApiException("Client error: " + ex.getResponseBodyAsString());

        } catch (HttpServerErrorException ex) {

            throw new GstApiException("GST API server error. Please try later.");

        } catch (Exception ex) {

            throw new GstApiException("Unexpected error: " + ex.getMessage());
        }
    }

    private GstTracking mapToEntity(GstData data) {

        GstTracking entity = new GstTracking();

        entity.setGstIn(data.getGstin());

        if (data.getPradr() != null && data.getPradr().getAddr() != null) {
        }

        return entity;
    }
}
