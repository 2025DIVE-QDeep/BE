package org.dive2025.qdeep.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ApiErrorResponse(ErrorCode errorCode){
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public static ResponseEntity<ApiErrorResponse> error(CustomException e){
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiErrorResponse
                        .builder()
                        .httpStatus(e.getErrorCode().getHttpStatus())
                        .code(e.getErrorCode().name())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }
}
