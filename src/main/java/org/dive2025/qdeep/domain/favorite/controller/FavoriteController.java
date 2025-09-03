package org.dive2025.qdeep.domain.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.domain.favorite.dto.request.AddFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.request.DeleteFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.response.AddFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.dto.response.DeleteFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


}
