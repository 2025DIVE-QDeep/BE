package org.dive2025.qdeep.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dive2025.qdeep.common.security.auth.UserDetailsImpl;
import org.dive2025.qdeep.domain.user.dto.request.DuplicationCheckRequest;
import org.dive2025.qdeep.domain.user.dto.request.ShowUserInformationRequest;
import org.dive2025.qdeep.domain.user.dto.request.ShowUserReviewRequest;
import org.dive2025.qdeep.domain.user.dto.request.UserCreateRequest;
import org.dive2025.qdeep.domain.user.dto.response.*;
import org.dive2025.qdeep.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name="유저 도메인 API",description = "유저 회원 가입과 관련된 API 레퍼런스")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "회원가입",
            description = "회원가입 로직을 담은 API")
    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService
                        .createUser(request));

    }

    @Operation(summary = "회원가입 시 , 중복 체크",description = "유저 ID가 같은지 체크하는 API")
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestBody DuplicationCheckRequest request){
        userService.checkUsername(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 아이디는 사용 가능합니다."));
    }

    @Operation(summary = "회원가입 시 , 중복 체크",description = "유저의 닉네임이 같은지 체크하는 API")
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody DuplicationCheckRequest request){
        userService.checkNickname(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 닉네임은 사용 가능합니다."));

    }


    @Operation(summary = "회원정보 ",description = "마이 페이지 내에 회원정보를 보여주는 API")
    @GetMapping("/show/information")
    public ResponseEntity<UserInformationResponse> showMyInformation
            (@RequestBody ShowUserInformationRequest showUserInformationRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService
                        .showInformation(showUserInformationRequest.username()));

    }


    @Operation(summary = "내가 쓴 리뷰",description = "내가 쓴 리뷰를 보여주는 API")
    @GetMapping("/show/myReview")
    public ResponseEntity<UserReviewResponse> showReviewListOfMine
            (@RequestBody ShowUserReviewRequest showUserReviewRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService
                        .showReviewListOfMine(showUserReviewRequest.username()));

    }

    @GetMapping("/show/ranking")
    public ResponseEntity<ShowRankingResponse> showRanking(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.showRanking());
    }

}
