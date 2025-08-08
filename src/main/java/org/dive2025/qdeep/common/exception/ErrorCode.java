package org.dive2025.qdeep.common.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 사용자를 찾을 수 없습니다."),
    USER_USERNAME_DUPLICATED(HttpStatus.CONFLICT,"해당 아이디는 이미 존재합니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.CONFLICT,"해당 닉네임은 이미 존재합니다."),
    USER_NICKNAME_UNSATISFIED(HttpStatus.BAD_REQUEST,"닉네임 형식에 맞지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
