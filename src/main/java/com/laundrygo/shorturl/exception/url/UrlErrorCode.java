package com.laundrygo.shorturl.exception.url;

import com.laundrygo.shorturl.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UrlErrorCode implements CommonErrorCodeType {
    INVALID_URL_FORMAT("INVALID_URL_FORMAT", "잘못된 URL 형식입니다.", HttpStatus.BAD_REQUEST),
    URL_NOT_FOUND("URL_NOT_FOUND", "등록되지 않은 단축 URL입니다.", HttpStatus.NOT_FOUND),
    URL_GENERATION_FAILED("URL_GENERATION_FAILED", "단축 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_SHORT_URL("DUPLICATE_SHORT_URL", "이미 존재하는 단축 URL입니다.", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
