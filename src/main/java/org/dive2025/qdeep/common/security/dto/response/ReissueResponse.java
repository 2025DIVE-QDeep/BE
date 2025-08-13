package org.dive2025.qdeep.common.security.dto.response;

public record ReissueResponse(String status,
                              String message,
                              String AccessToken,
                              String refreshToken) {
}
