package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class SignUpResponse extends BaseResponse {
    private String email;
    private String nickname;
    private String profileImage;
    private String accessToken;
    private String refreshToken;

    public static SignUpResponse from(Member member, AuthTokens tokens) {
        return SignUpResponse.builder()
                .resultCode(HttpStatus.CREATED.value())
                .resultMsg(HttpStatus.CREATED.name())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
    }
}
