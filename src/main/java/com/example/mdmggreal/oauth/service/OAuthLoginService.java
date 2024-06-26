package com.example.mdmggreal.oauth.service;

import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.CustomUserInfoDto;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.oauth.OAuthLoginParams;
import com.example.mdmggreal.oauth.dto.SignInResponse;
import com.example.mdmggreal.oauth.dto.OAuthInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtUtil jwtUtil;

    public SignInResponse signIn(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Member member = findMember(oAuthInfoResponse);
        if (member == null) {
            // 1. 회원가입 안된 회원
            return SignInResponse.builder()
                    .resultCode(ErrorCode.SIGNUP_REQUIRED.getHttpStatus().value())
                    .resultMsg(ErrorCode.SIGNUP_REQUIRED.getMessage())
                    .email(oAuthInfoResponse.getEmail())
                    .profileImage(oAuthInfoResponse.getProfileImage())
                    .build();
        } else {
            // 2. 회원가입 된 회원
            return SignInResponse.from(member, jwtUtil.createTokens(CustomUserInfoDto.of(member)));
        }
    }

    /* 이메일로 회원가입 유무 확인 */
    private Member findMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .orElse(null);  // 가입안되어있는 경우
    }
}
