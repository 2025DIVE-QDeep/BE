package org.dive2025.qdeep.domain.recommend.service;

import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.common.infra.gpt.client.GptClient;
import org.dive2025.qdeep.common.infra.gpt.dto.GptRequest;
import org.dive2025.qdeep.common.infra.gpt.dto.GptResponse;
import org.dive2025.qdeep.domain.recommend.dto.response.GptClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GptService {

    @Autowired
    private GptClient gptClient;

    @Value("${openai.model}")
    private String model;

    public GptClientResponse processRequest(String prompt){

        GptResponse gptResponse =  gptClient
                .sendMessage(new GptRequest(model,prompt));

        List<String> content = gptResponse.output()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GPT_RESPONSE_NOT_FOUND))
                .content()
                .stream()
                .map(GptResponse.Content::text)
                .collect(Collectors
                        .toList());

        return new GptClientResponse(content,
                LocalDateTime.now().toString());

    }


}
