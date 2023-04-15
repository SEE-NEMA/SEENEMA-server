package com.example.SEENEMA.user.dto;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}

