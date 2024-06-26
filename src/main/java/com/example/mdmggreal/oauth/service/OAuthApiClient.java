package com.example.mdmggreal.oauth.service;

import com.example.mdmggreal.member.type.OAuthProvider;
import com.example.mdmggreal.oauth.OAuthLoginParams;
import com.example.mdmggreal.oauth.dto.OAuthInfoResponse;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
