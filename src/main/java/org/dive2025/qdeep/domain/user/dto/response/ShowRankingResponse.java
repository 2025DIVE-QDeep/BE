package org.dive2025.qdeep.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.dive2025.qdeep.domain.user.entity.User;

import java.util.List;

public record ShowRankingResponse(List<UserRankingInfo> users) {
}
