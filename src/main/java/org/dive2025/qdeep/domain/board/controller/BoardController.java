package org.dive2025.qdeep.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.domain.board.dto.request.BoardListRequest;
import org.dive2025.qdeep.domain.board.dto.request.BoardRequest;
import org.dive2025.qdeep.domain.board.dto.response.BoardCreationResponse;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@Tag(name="리뷰글 API",
        description = "리뷰글 작성 및 조회 API")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 글을 생성하는 메소드
    @Operation(summary = "리뷰 쓰기 기능",
            description = "내가 방문한 장소에 대해 리뷰쓰는 API (Authorization)",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
        리뷰 작성 API (Authorization)
        - 'board'는 JSON으로 작성
        - 'files'는 form-data로 업로드 (선택)
        """,
                    required = true,
                    content = @Content(
                                    mediaType = "multipart/form-data",
                                    schema = @Schema(type = "object", description = "리뷰 작성 파트"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "리뷰 JSON 예시",
                                                    value = """
                                {
                                  "content": "테스트용 컨텐츠",
                                  "userId": "1",
                                  "storeId": "2"
                                }
                                """
                                            )
                                    }
                            )

            ),
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "content": "테스트용 컨텐츠",
                                                        "nickname": "관리자1",
                                                        "creationTime": "2025-09-03T17:04:57.410302",
                                                        "storeName": "해운대 소금구이",
                                                        "address": "부산광역시 해운대구 우동 615-14",
                                                        "hours": "12시 - 22시"
                                                    }
                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 작성한 리뷰",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "httpStatus": "CONFLICT",
                                                        "code": "ONLY_ONCE_REVIEW_PER_USER",
                                                        "message": "이미 해당 유저는 글을 작성하였습니다."
                                                    }
                            """
                                    )
                            )
                    )

            })
    @PostMapping("/create")
    public ResponseEntity<BoardCreationResponse> create
    (@RequestPart("board") BoardRequest boardRequest,
     @RequestPart(value = "files",required = false) List<MultipartFile> files){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.create(boardRequest,files));
    }



    // Store과 관련된 board(리뷰)를 보여주는 메소드
    @Operation(summary = "해당 장소와 관련된 리뷰글을 보여주는 기능",
            description = "리뷰를 보여주는 API ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "유저 닉네임 중복체크 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "리뷰글 보여주기 예시",
                                            value = """
                        {
                          "storeId":"1"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "리뷰글 보여주기",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "리뷰글 응답 예시",
                                    value = """
                                            {
                                                "boardList": [
                                                    {
                                                        "id": 3,
                                                        "postedTime": "2025-09-02T15:42:05.347228",
                                                        "content": "테스트용"
                                                    }
                                                ]
                                            }
                                    """
                            )
                    )

            ))
    @GetMapping ("/list")
    ResponseEntity<List<Board>> showBoardByStore
            (@RequestBody BoardListRequest boardListRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(boardService.showBoardByStore(boardListRequest));
    }

}
