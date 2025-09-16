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
                                                        "gender" : "남자",
                                                        "age" : "10대",
                                                        "address" : "대전광역시 중구"
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
                                                            "name": "치즈케이크",
                                                            "address": "대전 중구 대사문로 58",
                                                            "hours": "10:00 - 20:00",
                                                            "description": "분위기 좋은 카페에서 다채로운 치즈케이크를 즐길 수 있는 곳",
                                                            "latitude": 36.320225,
                                                            "longtitude": 127.422372
                                                        },
                                                        {
                                                            "name": "부엉이가조타",
                                                            "address": "대전 중구 중앙로 136",
                                                            "hours": "11:30 - 22:00",
                                                            "description": "아기자기한 인테리어와 맛있는 음식이 인기있는 식당",
                                                            "latitude": 36.327659,
                                                            "longtitude": 127.424304
                                                        },
                                                        {
                                                            "name": "카페로뮤",
                                                            "address": "대전 중구 대종로 453",
                                                            "hours": "09:00 - 21:00",
                                                            "description": "편안한 분위기와 직접 로스팅한 커피를 즐길 수 있는 카페",
                                                            "latitude": 36.332241,
                                                            "longtitude": 127.422127
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
