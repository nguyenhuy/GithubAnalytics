package com.huynguyen.GithubAnalytics.model;

/**
 * Created with IntelliJ IDEA.
 * User: nguyenthanhhuy
 * Date: 1/25/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {
    String REDIRECT_URL = "http://githubanalytics.huynguyen.com";

    String CLIENT_ID = "";
    String CLIENT_SECRET = "";
    String SCOPE = "user, public_repo, repo";

    String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";
    String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    String API_ROOT_URL = "https://api.github.com";
    String API_ISSUES_URL = API_ROOT_URL + "/issues";
}
