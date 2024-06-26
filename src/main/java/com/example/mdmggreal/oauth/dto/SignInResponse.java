package com.example.mdmggreal.oauth.dto;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.entity.Member;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class SignInResponse extends BaseResponse {
    // 모든 응답에 포함
    private String email;
    private String profileImage;

    // 회원가입이 되어있어 로그인처리 가능한 응답에 포함. 회원가입 안되어있는 경우 null
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public static SignInResponse from(Member member, AuthTokens tokens) {

        return SignInResponse.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.name())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
    }
}
