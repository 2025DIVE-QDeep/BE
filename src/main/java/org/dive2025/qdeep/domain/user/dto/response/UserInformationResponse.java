package org.dive2025.qdeep.domain.user.dto.response;

public record UserInformationResponse(String nickname,
                                      Integer amountOfFirst,
                                      Integer amountOfReview) {
}
