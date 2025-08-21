package org.dive2025.qdeep.domain.recommend.service;

import org.dive2025.qdeep.common.infra.gpt.client.GptClient;
import org.dive2025.qdeep.common.infra.gpt.dto.GptRequest;
import org.dive2025.qdeep.common.infra.gpt.dto.GptResponse;
import org.dive2025.qdeep.domain.recommend.dto.response.GptClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GptService {

    @Autowired
    private GptClient gptClient;

    @Value("${openai.model}")
    private String model;

    public GptClientResponse processRequest(String prompt){

        String reuslt =  gptClient
                .sendMessage(new GptRequest(model,prompt))
                .choices()
                .get(0)
                .message()
                .content();

        return new GptClientResponse(reuslt,
                LocalDateTime.now().toString());

    }


}
