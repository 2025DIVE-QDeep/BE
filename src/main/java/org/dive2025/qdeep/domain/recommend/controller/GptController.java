package org.dive2025.qdeep.domain.recommend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="추천 API",
        description = "Gpt API를 가공한 추천 API")
public class GptController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private GptService gptService;

    @Operation(summary = "프롬프트와 관련된 장소를 추천해주는 기능",
            description = "추천 API ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "추천 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "추천 요청 예시",
                                            value = """
                        {
                          " address " : " 부산특별시 해운대구 ",
                          " hours" : " 오후 1시 ~ 오후 3시 " ,
                          " category " : " 고기집 "
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "추천 장소 보여주기",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "추천 장소 응답 예시",
                                    value = """
                                            [
                                                        {
                                                            "name": "해운대 소금구이",
                                                            "address": "부산 해운대구 센텀동 170-9",
                                                            "hours": "오후 1시 - 오후 3시",
                                                            "description": "신선한 고기와 다양한 사이드 메뉴를 즐길 수 있는 소금구이 전문점"
                                                        },
                                                        {
                                                            "name": "해운대 할매고기",
                                                            "address": "부산 해운대구 우동 651-1",
                                                            "hours": "오후 1시 - 오후 3시",
                                                            "description": "현지인들에게 사랑받는 고기 요리 전문점으로 할머니가 직접 요리하는 맛을 경험해보세요"
                                                        },
                                                        {
                                                            "name": "해운대 불고기집",
                                                            "address": "부산 해운대구 중동 1417-10",
                                                            "hours": "오후 1시 - 오후 3시",
                                                            "description": "고기의 담백한 맛을 즐길 수 있는 불고기 전문 음식점"
                                                        }
                                                    ]
                                    """
                            )
                    )

            ))
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
