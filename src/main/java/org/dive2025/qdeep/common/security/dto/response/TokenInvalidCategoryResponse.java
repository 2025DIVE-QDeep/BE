package org.dive2025.qdeep.common.security.dto.response;

public record TokenInvalidCategoryResponse(String category,
                                           String message,
                                           String date) {
}
