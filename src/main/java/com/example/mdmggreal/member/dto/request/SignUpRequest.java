package com.example.mdmggreal.member.dto.request;

import com.example.mdmggreal.member.type.Agree;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private String email;

    private String nickname;

    private String profileImage;

    // 약관 동의 여부
    private Agree agrees;
}
