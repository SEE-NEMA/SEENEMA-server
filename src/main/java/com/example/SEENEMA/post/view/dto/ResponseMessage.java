package com.example.SEENEMA.post.view.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    CREATE("등록 완료"),
    UPDATE("수정 완료"),
    DELETE("삭제 완료");

    private final String msg;
}
