package org.dive2025.qdeep.common.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.common.security.dto.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name="로그인/로그아웃 API",description = "로그인/로그아웃을 위한 API - 해당 API 명세서는 스텁이므로 실제 주소는 /auth를 제외해야 합니다.")
public class AuthStubController {

    @Operation(summary = "로그인",
            description = "로그인 API ( Authorization )",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "유저 닉네임 중복체크 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "로그인 요청 예시",
                                            value = """
                        {
                          "username":"test1",
                          "password":"1234"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공"
            ))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok("로그인 스텁 - 필터처리 : 실제 URI는 (서버 주소)/login ");

    }

    @Operation(summary = "로그아웃",
            description = "로그아웃 API ( 요청 조건 X , Authorization )",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그아웃 성공",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok("로그아웃 스텁 - 필터처리 : 실제 URI는 (서버 주소)/logout");
    }
}
