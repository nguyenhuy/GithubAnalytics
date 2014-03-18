package com.huynguyen.GithubAnalytics.model;

import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.huynguyen.GithubAnalytics.Utils;
import com.huynguyen.GithubAnalytics.model.pojo.AccessTokenResult;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OAuthManager {
    private static OAuthManager instance;
    public static OAuthManager getInstace() {
        if (instance == null) {
            instance = new OAuthManager();
        }
        return instance;
    }

    public static String getAuthorizeUrl() {
        return Constants.AUTHORIZE_URL
                + "?client_id=" + Constants.CLIENT_ID
                + "&redirect_uri=" + Constants.REDIRECT_URL
                + "&scope=" + Constants.SCOPE;
    }

    private AccessTokenResult accessTokenResult;
    private OAuthManager() {}

    public AccessTokenResult getAccessTokenResult() {
        return accessTokenResult;
    }

    public boolean isAuthorized() {
        return accessTokenResult != null
                && !TextUtils.isEmpty(accessTokenResult.getAccessToken());
    }

    public void exchangeAccessToken(String code) throws IOException {
        if (TextUtils.isEmpty(code)) {
            throw new IOException("code must not be empty");
        }
        ArrayList<BasicNameValuePair> params = new
                ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
        params.add(new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
        params.add(new BasicNameValuePair("code", code));

        HttpPost post = new HttpPost(Constants.ACCESS_TOKEN_URL);
        post.setHeader("Accept", "application/json");
        post.setEntity(new UrlEncodedFormEntity(params));

        AndroidHttpClient client = null;
        try {
            client = AndroidHttpClient.newInstance("Android");
            HttpResponse response = client.execute(post);
            Utils.validate(response);

            String json = EntityUtils.toString(response.getEntity());
            accessTokenResult = new Gson().fromJson(json,
                    AccessTokenResult.class);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}