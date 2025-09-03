package org.dive2025.qdeep.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name="유저 도메인 API",
        description = "유저 회원 가입과 관련된 API 레퍼런스")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "회원가입",
            description = "회원가입 로직을 담은 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "회원가입 예시",
                                            value = """
                                                    {
                                                    "username":"test1",
                                                    "nickname":"관리자1",
                                                    "password":"1234"
                                                    }                                               
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService
                        .createUser(request));

    }

    @Operation(summary = "회원가입 시 , 중복 체크",
            description = "유저의 아이디가 같은지 체크하는 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "유저 아이디 중복체크 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "내가 쓴 리뷰 요청예시",
                                            value = """
                        {
                          "content" : "test2"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "회원가입 시 , 아이디가 중복되는지 체크하는 API",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value =  """
                                   
                                   {                                   
                                   "해당 아이디는 사용 가능합니다. "
                                   }
                                   
                                   """
                                   )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 존재하는 아이디",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                         "httpStatus": "CONFLICT",
                                                         "code": "USER_USERNAME_DUPLICATED",
                                                         "message": "해당 아이디는 이미 존재합니다."
                                                    }
                            """
                                    )
                            )
                    )

            })
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestBody DuplicationCheckRequest request){
        userService.checkUsername(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 아이디는 사용 가능합니다."));
    }

    @Operation(summary = "회원가입 시 , 중복 체크",
            description = "유저의 닉네임이 같은지 체크하는 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "유저 닉네임 중복체크 Request DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "내가 쓴 리뷰 요청예시",
                                            value = """
                        {
                          "content" : "test2"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "회원가입 시 , 닉네임이 중복되는지 체크하는 API",
                    content = @Content(
                           mediaType = "application/json",
                           examples = @ExampleObject(
                                  value =  """
                                   
                                   {                                  
                                   "해당 닉네임은 사용 가능합니다."                              
                                   }
                                   
                                   """
                           )
                    )
            ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 존재하는 닉네임",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                         "httpStatus": "CONFLICT",
                                                         "code": "USER_NICKNAME_DUPLICATED",
                                                         "message": "해당 닉네임은 이미 존재합니다."
                                                    }
                            """
                                    )
                            )
                    )
            })
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody DuplicationCheckRequest request){
        userService.checkNickname(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 닉네임은 사용 가능합니다."));

    }


    @Operation(summary = "회원정보 ",
            description = "마이 페이지 내에 회원정보를 보여주는 API ( 요청조건 X , Authorization )",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "마이페이제 내에 회원정보를 보여주는 API",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nickname": "관리자1",
                                                "amountOfFirst": 1,
                                                "amountOfReview": null
                                            }
                                            """
                            )
                    )
            ))
    @GetMapping("/show/information")
    public ResponseEntity<UserInformationResponse> showMyInformation
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService
                        .showInformation(userDetails.getUsername()));

    }


    @Operation(summary = "내가 쓴 리뷰",
            description = "내가 쓴 리뷰를 보여주는 API (요청조건 X, Authorization)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
            @ApiResponse(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                                                             
                                            {
                                                "board": [
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
            )

    })
    @GetMapping("/show/myReview")
    public ResponseEntity<UserReviewResponse> showReviewListOfMine
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService
                        .showReviewListOfMine(userDetails.getUsername()));

    }

    @Operation(summary = "랭킹 확인하기",
            description = "랭킹을 확인하는 API ( 요청 조건 X ) ",
            responses = {
            @ApiResponse(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                    {
                      "users": [
                        {"nickname":"관리자1","amountOfFirst":5,"amountOfReview":10},
                        {"nickname":"관리자2","amountOfFirst":4,"amountOfReview":8}
                      ]
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping("/show/ranking")
    public ResponseEntity<ShowRankingResponse> showRanking(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.showRanking());
    }

}
