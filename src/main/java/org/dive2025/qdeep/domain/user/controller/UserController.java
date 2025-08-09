package org.dive2025.qdeep.domain.user.controller;

import org.dive2025.qdeep.domain.user.dto.request.DuplicationCheckRequest;
import org.dive2025.qdeep.domain.user.dto.request.UserCreateRequest;
import org.dive2025.qdeep.domain.user.dto.response.DuplicationCheckResponse;
import org.dive2025.qdeep.domain.user.dto.response.UserCreateResponse;
import org.dive2025.qdeep.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService
                        .createUser(request));

    }

    @GetMapping("check-username")
    public ResponseEntity<?> checkUsername(@RequestBody DuplicationCheckRequest request){
        userService.checkUsername(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 아이디는 사용 가능합니다."));
    }

    @GetMapping("check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody DuplicationCheckRequest request){
        userService.checkNickname(request);
        return ResponseEntity.status(HttpStatus.OK).body(new DuplicationCheckResponse("해당 닉네임은 사용 가능합니다."));

    }


}
