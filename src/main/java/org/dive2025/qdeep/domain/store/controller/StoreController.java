package org.dive2025.qdeep.domain.store.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.common.security.auth.UserDetailsImpl;
import org.dive2025.qdeep.domain.store.dto.request.DeleteStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.SaveStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.StoredInformationRequest;
import org.dive2025.qdeep.domain.store.dto.response.DeleteStoreResponse;
import org.dive2025.qdeep.domain.store.dto.response.ShowListByAddressResponse;
import org.dive2025.qdeep.domain.store.dto.response.ShowStoreResponse;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.dive2025.qdeep.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@Tag(name="장소 API",
        description = "장소 정보관련 API")
public class StoreController {

    @Autowired
    private StoreService storeService;


    // 가게 상세 페이지 보여주기
    @Operation(summary = "장소에 관한 정보를 가져오는 기능",
            description = "장소정보 요청 API ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "장소 정보요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "장소 요청 예시",
                                            value = """
                        {
                          "storeId" : "2"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "장소 상세설명 응답",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "장소 상세설명 예시",
                                    value = """
                                           {
                                                       "name": "해운대 소금구이",
                                                       "address": "부산광역시 해운대구 우동 615-14",
                                                       "hours": "12시 - 22시",
                                                       "description": "소금에 구워내는 고기로 유명한 고기집"
                                           }
                                    """
                            )
                    )

            ))
    @GetMapping("/load")
    public ResponseEntity<ShowStoreResponse> showStore(@RequestBody StoredInformationRequest storedInformationRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(storeService
                        .showStore(storedInformationRequest));

    }

    // 가게 리스트 보여주기
    @Operation(summary = "주변에 위치한 장소를 가져오는 기능",
            description = "주변에 위치한 장소 요청 API ",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "장소 상세설명 응답",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "장소 상세설명 예시",
                                    value = """
                                           {
                                                       "storeList": [
                                                           {
                                                               "id": 1,
                                                               "name": "해운대 구역살이",
                                                               "address": "부산광역시 해운대구 우동 620-5",
                                                               "hours": "12시 - 21시",
                                                               "description": "신선한 고기와 정감있는 분위기를 즐길 수 있는 고기집",
                                                               "latitude": 35.158718,
                                                               "longtitude": 129.160487,
                                                               "firstUserId": 1,
                                                               "board": [
                                                                   {
                                                                       "id": 3,
                                                                       "postedTime": "2025-09-02T15:42:05.347228",
                                                                       "content": "테스트용"
                                                                   }
                                                               ],
                                                               "favorite": []
                                                           },
                                                           {
                                                               "id": 2,
                                                               "name": "해운대 소금구이",
                                                               "address": "부산광역시 해운대구 우동 615-14",
                                                               "hours": "12시 - 22시",
                                                               "description": "소금에 구워내는 고기로 유명한 고기집",
                                                               "latitude": 35.157883,
                                                               "longtitude": 129.160223,
                                                               "firstUserId": 1,
                                                               "board": [
                                                                   {
                                                                       "id": 5,
                                                                       "postedTime": "2025-09-03T17:04:57.389641",
                                                                       "content": "테스트용 컨텐츠"
                                                                   }
                                                               ],
                                                               "favorite": []
                                                           },
                                                           {
                                                               "id": 3,
                                                               "name": "해운대 맛원",
                                                               "address": "부산광역시 해운대구 우동 600-6",
                                                               "hours": "12시 - 22시",
                                                               "description": "다양한 고기 메뉴를 즐길 수 있는 인기 고기집",
                                                               "latitude": 35.159236,
                                                               "longtitude": 129.160292,
                                                               "firstUserId": null,
                                                               "board": [],
                                                               "favorite": []
                                                           }
                                                       ]
                                                   }
                                    """
                            )
                    )

            ))
    @GetMapping("/list")
    public ResponseEntity<ShowListByAddressResponse> showListByAddressPart(
            @Parameter(
                    description = "주변 위치",
                    example = "부산광역시"
            )
            @RequestParam(name="addressPart") String addressPart){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ShowListByAddressResponse(storeService
                        .findStoreByAddress(addressPart)));
    }

}
