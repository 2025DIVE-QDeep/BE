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
    USER_NICKNAME_UNSATISFIED(HttpStatus.BAD_REQUEST,"닉네임 형식에 맞지 않습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 글을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 가게를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND,"찜한 가게를 찾을 수 없습니다."),
    NO_RANKER(HttpStatus.NOT_FOUND,"현재 모든 유저가 토큰을 가지지 않습니다."),
    ONLY_ONCE_REVIEW_PER_USER(HttpStatus.CONFLICT,"이미 해당 유저는 글을 작성하였습니다."),
    GPT_RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND,"gpt API의 결과값을 찾을 수 없습니다."),
    UNSUITABLE_FILE_MATCHED(HttpStatus.BAD_REQUEST,"잘못된 파일 형식입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패했습니다."),
    FILES_NOT_FOUND(HttpStatus.NOT_FOUND,"파일을 찾을 수 없습니다."),
    FAVORITE_ERROR(HttpStatus.NOT_FOUND,"찜한 가게가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
