package com.automatedGst.AutomatedGstInvoiceChecker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SandboxConfig {

    @Value("${sandbox.api.base-url}")
    private String baseUrl;

    @Value("${sandbox.api.gstin-url}")
    private String gstinUrl;

    @Value("${sandbox.api.api-key}")
    private String apiKey;

    @Value("${sandbox.api.token}")
    private String token;

    public String getFullUrl() {
        return baseUrl + gstinUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getToken() {
        return token;
    }
}
