package org.dive2025.qdeep.domain.user.dto.response;

public record UserCreateResponse(String username,
                                 String nickname,
                                 String creationTime) {
}
