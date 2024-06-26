package com.example.mdmggreal.oauth.service;

import com.example.mdmggreal.oauth.dto.NaverLoginURLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class NaverService {

    @Value("${oauth.naver.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.naver.url.auth}")
    private String AUTH_URL;

    @Value("${oauth.naver.url.callback}")
    private String CALLBACK_URL;

    public NaverLoginURLResponse createNaverURL() throws UnsupportedEncodingException {
        StringBuffer url = new StringBuffer();
        String authUrl = AUTH_URL + "/oauth2.0/authorize";

        // 네이버 API 명세에 맞춰서 작성
        String redirectURI = URLEncoder.encode(CALLBACK_URL, StandardCharsets.UTF_8); // redirectURI 설정 부분
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();

        url.append(authUrl);
        url.append("?response_type=code");
        url.append("&client_id=").append(CLIENT_ID);
        url.append("&state=").append(state);
        url.append("&redirect_uri=").append(redirectURI);

        return NaverLoginURLResponse.builder()
                .naverLoginUrl(url.toString())
                .build();
    }
}
