package com.cn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @description:
 * @author: helisen
 * @create: 2021-01-23 14:08
 **/
public class NameRequiredException extends ResponseStatusException {
    public NameRequiredException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
