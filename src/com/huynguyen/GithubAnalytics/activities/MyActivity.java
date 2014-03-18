package com.huynguyen.GithubAnalytics.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.huynguyen.GithubAnalytics.R;
import com.huynguyen.GithubAnalytics.model.OAuthManager;

public class MyActivity extends Activity {
    private static final int RC_LOGIN = 1;

    private TextView statusTv;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button loginBtn = (Button) findViewById(R.id.login);
        Button issuesBtn = (Button) findViewById(R.id.issues);
        statusTv = (TextView) findViewById(R.id.status);

        statusTv.setText("Let's login");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });
        issuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OAuthManager.getInstace().isAuthorized()) {
                    startIssuesActivity();
                } else {
                    statusTv.setText("Login First!!!");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_LOGIN:
                if (resultCode == RESULT_OK) {
                    statusTv.setText("Good to go: "
                            + OAuthManager.getInstace().getAccessTokenResult()
                            .getAccessToken());
                } else {
                    statusTv.setText("Error occurred. Try later.");
                }
        }
    }

    private void startLoginActivity() {
        Intent i = new Intent(MyActivity.this, OAuthActivity.class);
        startActivityForResult(i, RC_LOGIN);
    }

    private void startIssuesActivity() {
        Intent i = new Intent(MyActivity.this, IssuesActivity.class);
        startActivity(i);
    }
}
