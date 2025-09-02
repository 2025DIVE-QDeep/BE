package org.dive2025.qdeep.common.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dive2025.qdeep.common.security.dto.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name="로그인/로그아웃 API",description = "로그인/로그아웃을 위한 API - 해당 API 명세서는 스텁이므로 실제 주소는 /auth를 제외해야 합니다.")
public class AuthStubController {

    @Operation(summary = "로그인 기능",description = "등록된 회원 로그인 하기")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok("로그인 스텁 - 필터처리 : 실제 URI는 (서버 주소)/login ");

    }

    @Operation(summary = "로그아웃 기능",description = "로그인 후 로그아웃 하기 : 쿠키가 필요합니다. 쿠키에서 refresh를 추출하도록 로직이 설정되어있음")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok("로그아웃 스텁 - 필터처리 : 실제 URI는 (서버 주소)/logout");
    }
}
