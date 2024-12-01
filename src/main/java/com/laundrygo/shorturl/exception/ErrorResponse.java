package com.laundrygo.shorturl.exception;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String code;
    private String message;
    private List<String> errors;

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @Builder
    public ErrorResponse(String code, String message, List<String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    static ErrorResponse create(CommonErrorCodeType commonErrorCodeType) {
        return ErrorResponse.builder()
                .code(commonErrorCodeType.getErrorCode())
                .message(commonErrorCodeType.getMessage())
                .build();
    }
}
