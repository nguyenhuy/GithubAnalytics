package com.huynguyen.GithubAnalytics.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nguyenthanhhuy
 * Date: 1/25/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Issue {
    @SerializedName("assignee")
    private User assignee;
    @SerializedName("title")
    private String title;
    @SerializedName("number")
    private int number;
    @SerializedName("state")
    private String state;
    @SerializedName("user")
    private User author;
    @SerializedName("created_at")
    private Date createdAt;

    public Issue() {
    }

    public User getAssignee() {
        return assignee;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
