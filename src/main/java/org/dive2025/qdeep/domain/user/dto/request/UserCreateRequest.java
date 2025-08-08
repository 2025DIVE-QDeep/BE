package org.dive2025.qdeep.domain.user.dto.request;

public record UserCreateRequest(String username,
                                String nickname,
                                String password) {
}
