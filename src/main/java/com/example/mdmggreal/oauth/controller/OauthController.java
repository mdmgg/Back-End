package com.example.mdmggreal.oauth.controller;

import com.example.mdmggreal.oauth.NaverLoginParams;
import com.example.mdmggreal.oauth.dto.NaverLoginURLResponse;
import com.example.mdmggreal.oauth.dto.SignInRequest;
import com.example.mdmggreal.oauth.dto.SignInResponse;
import com.example.mdmggreal.oauth.service.NaverService;
import com.example.mdmggreal.oauth.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final NaverService naverService;
    private final OAuthLoginService oAuthLoginService;

    // 네이버 로그인 URL 반환
    @GetMapping("/naver")
    public ResponseEntity<NaverLoginURLResponse> naverConnect() throws UnsupportedEncodingException {
        return ResponseEntity.ok(naverService.createNaverURL());
    }

    // 네이버 인가코드, 상태값 받아서 로그인 시도
    @GetMapping("/signin")
    public ResponseEntity<SignInResponse> naverSignin(@RequestBody SignInRequest request) {
        NaverLoginParams params = NaverLoginParams.builder()
                .authorizationCode(request.getCode())
                .state(request.getState())
                .build();
        return ResponseEntity.ok(oAuthLoginService.signIn(params));
    }

// 네이버 인가코드, 상태값 받아서 로그인 시도 - 프론트 없이 테스트할 때 사용
//    @GetMapping("/callback/naver")
//    public ResponseEntity<SignInResponse> naverSigninTest(@RequestParam String code, @RequestParam String state) {
//        NaverLoginParams params = NaverLoginParams.builder()
//                .authorizationCode(code)
//                .state(state)
//                .build();
//        return ResponseEntity.ok(oAuthLoginService.signIn(params));
//    }
}
