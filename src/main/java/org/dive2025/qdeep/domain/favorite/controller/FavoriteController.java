package org.dive2025.qdeep.domain.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.domain.favorite.dto.request.AddFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.request.DeleteFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.request.ShowFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.response.AddFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.dto.response.DeleteFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.dto.response.ShowFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@Tag(name="찜 API",
        description = "리뷰글 작성을 위한 찜기는 관련 API")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 찜하기 기능
    @Operation(summary = "찜하기 기능",
            description = "찜하기 API ( Authorization )",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "찜하기 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "찜 저장 예시",
                                            value = """
                                                    {
                                                    "userId":"1",
                                                    "storeId":"1"
                                                    }                                               
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "찜하기 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "userId": "1",
                                                        "storeId": "1"
                                                    }
                                                    """
                                    )
                            )
                    )
            })
    @PostMapping("/add")
    public ResponseEntity<AddFavoriteResponse> addFavorite(@RequestBody AddFavoriteRequest addFavoriteRequest){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavorite(addFavoriteRequest));

    }

    // 찜 삭제 기능
    @Operation(summary = "찜 삭제 기능",
            description = "찜 삭제 API ( Authorization )",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "찜삭제 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "찜 삭제 예시",
                                            value = """
                                                    {
                                                    "userId":"1",
                                                    "storeId":"1"
                                                    }                                               
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "찜 삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "username": "test1",
                                                        "storeName": "해운대 구역살이"
                                                    }
                                                    """
                                    )
                            )
                    )
            })
    @DeleteMapping("/delete")
    public ResponseEntity<DeleteFavoriteResponse> deleteFavorite
    (@RequestBody DeleteFavoriteRequest deleteFavoriteRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(favoriteService.deleteFavorite(deleteFavoriteRequest));
    }

    @Operation(summary = "찜한 가게 리스트업",
    description = "찜한 가게를 보여주는 API",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "찜한 가게 리스트 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "찜 가져오기 예시",
                                            value = """
                                                    {
                                                    "userId":"1",                                                 
                                                    }                                               
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "찜 삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "favoriteList": [
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
                                                                        "content": "테스트용",
                                                                        "s3File": []
                                                                    }
                                                                ],
                                                                "favorite": [
                                                                    {
                                                                        "id": 3,
                                                                        "addTime": "2025-09-12T20:44:05.225802"
                                                                    }
                                                                ]
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
                                                                "favorite": [
                                                                    {
                                                                        "id": 4,
                                                                        "addTime": "2025-09-15T16:46:31.357184"
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                "id": 4,
                                                                "name": "해운대 소금구이",
                                                                "address": "부산 해운대구 센텀동 170-9",
                                                                "hours": "오후 1시 - 오후 3시",
                                                                "description": "신선한 고기와 다양한 사이드 메뉴를 즐길 수 있는 소금구이 전문점",
                                                                "latitude": 35.168773,
                                                                "longtitude": 129.130902,
                                                                "firstUserId": 1,
                                                                "board": [
                                                                    {
                                                                        "id": 13,
                                                                        "postedTime": "2025-09-15T16:16:12.163909",
                                                                        "content": "테스트용 컨텐츠2",
                                                                        "s3File": [
                                                                            {
                                                                                "id": 2,
                                                                                "key": "board/13/c5f3c753-48f7-4826-875c-6cf107db16d4.06.48.png",
                                                                                "size": 2053956,
                                                                                "uploadedAt": "2025-09-15T16:16:12.652378"
                                                                            }
                                                                        ]
                                                                    }
                                                                ],
                                                                "favorite": [
                                                                    {
                                                                        "id": 5,
                                                                        "addTime": "2025-09-15T16:48:20.352701"
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                "id": 5,
                                                                "name": "해운대 할매고기",
                                                                "address": "부산 해운대구 우동 651-1",
                                                                "hours": "오후 1시 - 오후 3시",
                                                                "description": "현지인들에게 사랑받는 고기 요리 전문점으로 할머니가 직접 요리하는 맛을 경험해보세요",
                                                                "latitude": 35.162237,
                                                                "longtitude": 129.176012,
                                                                "firstUserId": 1,
                                                                "board": [
                                                                    {
                                                                        "id": 14,
                                                                        "postedTime": "2025-09-15T16:37:02.459912",
                                                                        "content": "테스트용 컨텐츠2",
                                                                        "s3File": [
                                                                            {
                                                                                "id": 3,
                                                                                "key": "board/14/ae4789d4-5a59-490c-b734-9a00c05c30b2.06.48.png",
                                                                                "size": 2053956,
                                                                                "uploadedAt": "2025-09-15T16:37:03.064001"
                                                                            }
                                                                        ]
                                                                    }
                                                                ],
                                                                "favorite": [
                                                                    {
                                                                        "id": 6,
                                                                        "addTime": "2025-09-15T16:49:53.965723"
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                "id": 6,
                                                                "name": "해운대 불고기집",
                                                                "address": "부산 해운대구 중동 1417-10",
                                                                "hours": "오후 1시 - 오후 3시",
                                                                "description": "고기의 담백한 맛을 즐길 수 있는 불고기 전문 음식점",
                                                                "latitude": 35.161193,
                                                                "longtitude": 129.166472,
                                                                "firstUserId": null,
                                                                "board": [],
                                                                "favorite": [
                                                                    {
                                                                        "id": 7,
                                                                        "addTime": "2025-09-15T16:51:31.344161"
                                                                    }
                                                                ]
                                                            },
                                                            {
                                                                "id": 7,
                                                                "name": "해운대 소격정",
                                                                "address": "부산 해운대구 해운대로 822",
                                                                "hours": "오후 1시 - 오후 3시",
                                                                "description": "신선한 고기와 풍부한 반찬으로 손님을 맞이하는 인기 고기집",
                                                                "latitude": 35.163686,
                                                                "longtitude": 129.16154,
                                                                "firstUserId": 1,
                                                                "board": [
                                                                    {
                                                                        "id": 7,
                                                                        "postedTime": "2025-09-09T23:12:03.439176",
                                                                        "content": "테스트용 컨텐츠",
                                                                        "s3File": []
                                                                    }
                                                                ],
                                                                "favorite": [
                                                                    {
                                                                        "id": 8,
                                                                        "addTime": "2025-09-15T17:03:21.282013"
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/show")
    public ResponseEntity<ShowFavoriteResponse> showFavorite
            (@RequestBody ShowFavoriteRequest showFavoriteRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(favoriteService.showFavorite(showFavoriteRequest.userId()));

    }


}
