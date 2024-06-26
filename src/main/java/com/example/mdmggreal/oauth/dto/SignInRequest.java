package com.example.mdmggreal.oauth.dto;

import com.example.mdmggreal.member.type.Agree;
import lombok.Getter;

@Getter
public class SignInRequest {

    private String code;

    private String state;
}
