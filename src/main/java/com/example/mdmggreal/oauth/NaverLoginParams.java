package com.example.mdmggreal.oauth;

import com.example.mdmggreal.member.type.OAuthProvider;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Builder
public class NaverLoginParams implements OAuthLoginParams {
    private String authorizationCode;

    private String state;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String encodedCode = URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8); // redirectURI 설정 부분

        body.add("code", encodedCode);
        body.add("state", state);
        return body;
    }
}
