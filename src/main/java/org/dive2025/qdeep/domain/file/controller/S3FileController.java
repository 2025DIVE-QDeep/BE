package org.dive2025.qdeep.domain.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.domain.file.dto.request.LoadImagesRequest;
import org.dive2025.qdeep.domain.file.service.S3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/S3File")
@Tag(name="파일 API")
public class S3FileController {

    @Autowired
    private S3FileService s3FileService;

    @Operation(
            summary = "파일 로드",
            description = "S3 버킷에 업로드 된 리뷰글의 사진을 가져오는 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "파일 로딩 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "회원가입 예시",
                                            value = """
                                                    {
                                                    " boardId " : " 11 " 
                                                    }                                               
                                                    """
                                    )
                            }
                    )

            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사진 불러오기 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    [
                                                        "https://kangwooju-bucket-s3.s3.ap-northeast-2.amazonaws.com/board/11/3be1ae0b-4c71-4c1b-8eda-ab2c3844e241.06.48.png"
                                                    ]
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/getImages")
    public ResponseEntity<?> getImages(@RequestBody LoadImagesRequest loadImagesRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3FileService
                        .getImagesURL(loadImagesRequest.boardId()));
    }
}
