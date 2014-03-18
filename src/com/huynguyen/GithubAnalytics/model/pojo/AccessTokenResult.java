package com.huynguyen.GithubAnalytics.model.pojo;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResult {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;

    public AccessTokenResult() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
