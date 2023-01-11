package com.netsec.security.auth;

import com.netsec.security.model.Logging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author George Karampelas
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggingResponse {
    private String response;
    private List<Logging> loggingList;
}
