package com.netsec.security.exception;

import org.springframework.http.ResponseEntity;

/**
 * @author George Karampelas
 */
public class RepsonseEntityBuilder {
    public static ResponseEntity<Object> build(ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
