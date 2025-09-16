package org.dive2025.qdeep.domain.recommend.dto.response;

public record RecommendationResponse(String name,
                                     String address,
                                     String hours,
                                     String description,
                                     double latitude,
                                     double longtitude) {
}
