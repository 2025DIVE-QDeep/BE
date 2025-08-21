package org.dive2025.qdeep.common.infra.gpt.client;

import org.dive2025.qdeep.common.infra.gpt.dto.GptRequest;
import org.dive2025.qdeep.common.infra.gpt.dto.GptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GptClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String url;

    public GptResponse sendMessage(GptRequest request){
        return restTemplate.postForObject(url, request, GptResponse.class);
    }

}
