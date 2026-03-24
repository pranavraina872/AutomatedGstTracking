package com.automatedGst.AutomatedGstInvoiceChecker.service;

import com.automatedGst.AutomatedGstInvoiceChecker.config.SandboxConfig;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstData;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstRequest;
import com.automatedGst.AutomatedGstInvoiceChecker.dto.GstResponse;
import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import com.automatedGst.AutomatedGstInvoiceChecker.exception.GstApiException;
import com.automatedGst.AutomatedGstInvoiceChecker.repository.GstTrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


    @Service
    public class GstTrackingService {

        private final SandboxConfig config;

        private RestTemplate restTemplate =new RestTemplate();

        private final GstTrackingRepo repo;

        @Autowired
        private GstTrackingRepo gstRepository;

        public GstTrackingService(SandboxConfig config, GstTrackingRepo repository) {
            this.config = config;
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

                double taxAmount = (gstTracking.getAmount() * gstTracking.getGstTaxPercentage()) / 100;

                // ✅ INTRA STATE → CGST + SGST
                if (gstTracking.getBuyerStateCode() != null &&
                        gstTracking.getSellerStateCode() != null &&
                        gstTracking.getBuyerStateCode().equals(gstTracking.getSellerStateCode())) {

                    gstTracking.setCgst(taxAmount / 2);
                    gstTracking.setSgst(taxAmount / 2);
                    gstTracking.setIgst(0.0);

                } else {
                    // ✅ INTER STATE → IGST
                    gstTracking.setIgst(taxAmount);
                    gstTracking.setCgst(0.0);
                    gstTracking.setSgst(0.0);
                }

                gstTracking.setGstAmount(taxAmount);
                gstTracking.setTotalAmount(gstTracking.getAmount() + taxAmount);
            }
        }


        public GstData fetchGstDetails(String gstin) {

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



                return response.getBody().getData().getData();

            } catch (HttpClientErrorException.BadRequest ex) {

                // ✅ Extract API error message
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
                entity.setSellerStateCode(data.getGstin().substring(0, 2));
            }

            return entity;
        }

        public GstTracking validateAndProcess(GstTracking gstTracking) {

            // ✅ Validate GST
            GstData gstData = fetchGstDetails(gstTracking.getGstIn());

            if (gstData == null || gstData.getGstin() == null) {
                throw new GstApiException("Invalid GSTIN");
            }

            // ✅ Extract seller state
            String sellerState = gstTracking.getGstIn().substring(0, 2);
            gstTracking.setSellerStateCode(sellerState);

            // ✅ Calculate GST
            calculateGst(gstTracking);

            return repo.save(gstTracking);
        }
    }












