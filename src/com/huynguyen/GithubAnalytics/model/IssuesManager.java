package com.huynguyen.GithubAnalytics.model;

import android.net.http.AndroidHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huynguyen.GithubAnalytics.Utils;
import com.huynguyen.GithubAnalytics.model.pojo.Issue;
import com.huynguyen.GithubAnalytics.model.pojo.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IssuesManager {
    private static IssuesManager instance;

    public static IssuesManager getInstance() {
        if (instance == null) {
            instance = new IssuesManager();
        }
        return instance;
    }


    private List<Issue> issues;
    private List<User> assignees;
    private List<User> authors;

    private IssuesManager() {
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public boolean isEmpty() {
        return issues == null || issues.size() == 0;
    }

    public void fetchIssues() throws IOException {
        issues = new ArrayList<Issue>();
        assignees = new ArrayList<User>();
        authors = new ArrayList<User>();

        extractData(fetchIssues("open"));
        extractData(fetchIssues("closed"));
    }

    private Issue[] fetchIssues(String state) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token",
                OAuthManager.getInstace().getAccessTokenResult().getAccessToken()));
        params.add(new BasicNameValuePair("filter", "all"));
        params.add(new BasicNameValuePair("state", state));
        String url = Utils.appendParams(Constants.API_ISSUES_URL, params);

        HttpGet get = new HttpGet(url);

        AndroidHttpClient client = null;
        try {
            client = AndroidHttpClient.newInstance("Android");
            HttpResponse response = client.execute(get);
            Utils.validate(response);

            String json = EntityUtils.toString(response.getEntity());

            Gson gson = new GsonBuilder().setDateFormat
                    ("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            return gson.fromJson(json, Issue[].class);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    private void extractData(Issue[] currentIssues) {
        for (Issue i : currentIssues) {

            User assignee = i.getAssignee();
            if (assignee != null
                    && assignee.getLogin() != null
                    && !assignees.contains(assignee)) {
                assignees.add(assignee);
            }

            User author = i.getAuthor();
            if (author != null
                    && author.getLogin() != null
                    && !authors.contains(author)) {
                authors.add(author);
            }

            issues.add(i);
        }
    }
}
