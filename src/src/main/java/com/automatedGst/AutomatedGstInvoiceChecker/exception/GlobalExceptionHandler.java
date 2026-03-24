package com.automatedGst.AutomatedGstInvoiceChecker.exception;

import com.automatedGst.AutomatedGstInvoiceChecker.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GstApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGstApiException(GstApiException ex) {
        return new ErrorResponse("GST_ERROR", ex.getMessage());
    }
}
