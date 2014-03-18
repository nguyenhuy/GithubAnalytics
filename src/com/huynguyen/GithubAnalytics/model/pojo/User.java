package com.huynguyen.GithubAnalytics.model.pojo;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    private String login;

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        return  o != null
                && o instanceof User
                && login.equals(((User)o).login);
    }

    @Override
    public String toString() {
        return login;
    }
}
