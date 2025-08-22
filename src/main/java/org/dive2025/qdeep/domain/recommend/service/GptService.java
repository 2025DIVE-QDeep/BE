package org.dive2025.qdeep.domain.recommend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.common.infra.gpt.client.GptClient;
import org.dive2025.qdeep.common.infra.gpt.dto.GptRequest;
import org.dive2025.qdeep.common.infra.gpt.dto.GptResponse;
import org.dive2025.qdeep.domain.recommend.dto.request.GptClientRequest;
import org.dive2025.qdeep.domain.recommend.dto.request.PromptInputRequest;
import org.dive2025.qdeep.domain.recommend.dto.response.RecommendationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GptService {

    @Autowired
    private GptClient gptClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${openai.model}")
    private String model;

    //
    public List<RecommendationResponse> processRequest(GptClientRequest gptClientRequest){


        GptResponse gptResponse =  gptClient
                .sendMessage(new GptRequest(model,
                        gptClientRequest.prompt()));

        System.out.println(gptResponse);

        String jsonText = gptResponse.output()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND))
                .content()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND))
                .text();

        System.out.println(jsonText);

        // TypeReference 정리 필요

        // 최상위 객체의 모든 필드를 탐색
        List<RecommendationResponse> responses = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonText);

            rootNode.fields().forEachRemaining(entry -> {
                JsonNode arrayNode = entry.getValue();
                if (arrayNode.isArray()) {
                    for (JsonNode node : arrayNode) {
                        try {
                            RecommendationResponse response = objectMapper.treeToValue(node, RecommendationResponse.class);
                            responses.add(response);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("JSON 변환 실패", e);
                        }
                    }
                }
            });

        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND);
        }

        return responses;

    }

    // 사용자의 입력 부분을 프롬프팅 조정해주는 메소드
    public GptClientRequest prompting(PromptInputRequest promptInputRequest) {

        String promptTemplate = """
%s 근처에서 %s 카테고리의 장소를 추천해줘.
방문 시간은 %s에 맞춰.
결과는 JSON 형식으로 만들어줘. 
필드 이름은 다음과 같아:
- name
- address
- hours
- description
- latitude
- longtitude
다른 정보는 넣지 말고, JSON 구조만 반환해. 그리고 장소는 3개를 추천해
""";

        return new GptClientRequest(String
                .format(promptTemplate,
                        promptInputRequest.address(),
                        promptInputRequest.category(),
                        promptInputRequest.hours()));

    }

}
