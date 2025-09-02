package org.dive2025.qdeep.domain.user.dto.response;

import org.dive2025.qdeep.domain.user.entity.User;

import java.util.List;

public record ShowRankingResponse(List<User> users) {
}
