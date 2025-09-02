package org.dive2025.qdeep.common.security.dto.response;

public record LogoutErrorResponse (boolean error,
                                   String method,
                                   String details,
                                   String timeStamp){
}
