package com.example.SEENEMA.post.view.dto;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum StatusEnum {

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");
    private final int statusCode;
    private final String code;

}
