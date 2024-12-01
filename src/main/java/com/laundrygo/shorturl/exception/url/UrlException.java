package com.laundrygo.shorturl.exception.url;

import com.laundrygo.shorturl.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class UrlException extends RuntimeException{
    private final CommonErrorCodeType commonErrorCodeType;

    public UrlException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}

