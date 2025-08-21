package org.dive2025.qdeep.domain.recommend.controller;

import org.dive2025.qdeep.common.infra.gpt.client.GptClient;
import org.dive2025.qdeep.common.infra.gpt.dto.GptRequest;
import org.dive2025.qdeep.common.infra.gpt.dto.GptResponse;
import org.dive2025.qdeep.domain.recommend.dto.response.GptClientResponse;
import org.dive2025.qdeep.domain.recommend.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/gpt")
public class GptController {

    @Autowired
    private GptService gptService;
    @PostMapping("/chat")
    public ResponseEntity<GptClientResponse> chat(@RequestParam(name = "prompt")String prompt){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gptService.processRequest(prompt));

    }

}
