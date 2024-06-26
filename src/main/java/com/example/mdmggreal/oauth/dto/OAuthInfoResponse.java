package com.example.mdmggreal.oauth.dto;

import com.example.mdmggreal.member.type.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();

    String getProfileImage();

    OAuthProvider getOAuthProvider();
}
