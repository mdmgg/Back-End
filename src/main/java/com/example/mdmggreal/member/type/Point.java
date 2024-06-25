package com.example.mdmggreal.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Point {
    COMMENT(10),
    TRIAL(20),
    POST(30);


    private final int point;
}
