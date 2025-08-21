package org.dive2025.qdeep.domain.recommend.dto.response;
import java.util.List;

public record GptClientResponse(List<String> reslut,
                                String timeStamp) {
}
