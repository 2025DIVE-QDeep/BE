package org.dive2025.qdeep.domain.recommend.controller;

import org.dive2025.qdeep.domain.recommend.dto.request.GptClientRequest;
import org.dive2025.qdeep.domain.recommend.dto.request.PromptInputRequest;
import org.dive2025.qdeep.domain.recommend.dto.response.RecommendationSaveResponse;
import org.dive2025.qdeep.domain.recommend.service.GptService;
import org.dive2025.qdeep.domain.store.dto.response.SavedStoreResponse;
import org.dive2025.qdeep.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gpt")
public class GptController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private GptService gptService;

    @PostMapping("/chat")
    public ResponseEntity<List<SavedStoreResponse>> chat(@RequestBody PromptInputRequest promptInputRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.saveAllResponsesBatch(
                        gptService
                                .processRequest(gptService
                                .prompting(promptInputRequest))));

    }

}
