package org.dive2025.qdeep.domain.recommend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class GptService {

    @Autowired
    private GptClient gptClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${openai.model}")
    private String model;


    public List<RecommendationResponse> processRequest(GptClientRequest gptClientRequest){


        GptResponse gptResponse =  gptClient
                .sendMessage(new GptRequest(model,
                        gptClientRequest.prompt()));

        String jsonText = gptResponse.output()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND))
                .content()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND))
                .text();

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
%s 성별을 가진 %s 나이대가 방문해볼만한 음식점,카페 카테고리의 장소를 추천해줘.
주소는 %s 근처로 맞춰.
결과는 JSON 형식으로 만들어줘. 
필드 이름은 다음과 같아:
- name
- address
- hours
- description
- latitude
- longitude
다른 정보는 넣지 말고, 반드시 JSON 구조만 반환하되 최상위는 객체로 반환해. 그리고 장소는 3개를 추천해.
""";

        return new GptClientRequest(String
                .format(promptTemplate,
                        promptInputRequest.gender(),
                        promptInputRequest.age(),
                        promptInputRequest.address()));

    }

    // 기존의 요청 메소드를 CompletableFuture타입으로 래핑
    @Async("gptTaskExecutor")
    public CompletableFuture<List<RecommendationResponse>> recommendation
            (GptClientRequest gptClientRequest){

        log.info("Thread Name : {}",Thread.currentThread().getName());

        return CompletableFuture
                .completedFuture(processRequest(gptClientRequest));

    }

}
