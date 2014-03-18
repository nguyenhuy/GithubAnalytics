package com.huynguyen.GithubAnalytics.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.*;
import com.huynguyen.GithubAnalytics.R;
import com.huynguyen.GithubAnalytics.asynctasks.ExchangeAccessTokenTask;
import com.huynguyen.GithubAnalytics.model.Constants;
import com.huynguyen.GithubAnalytics.model.OAuthManager;

public class OAuthActivity extends Activity implements
        ExchangeAccessTokenTask.Listener {

    private static final int DIALOG_GETTING_ACCESS_TOKEN = 0;

    private WebView webview;
    private ExchangeAccessTokenTask exchangeAccessTokenTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oauth);
        webview = (WebView) findViewById(R.id.webview);

        askForAuthentication();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exchangeAccessTokenTask != null) {
            exchangeAccessTokenTask.setListener(null);
            exchangeAccessTokenTask.cancel(true);
            exchangeAccessTokenTask = null;
        }
        webview.stopLoading();
        webview.setWebViewClient(null);
        webview.setWebChromeClient(null);
        webview.setOnTouchListener(null);
    }

    public void askForAuthentication() {
        webview.getSettings().setJavaScriptEnabled(true);
        enableCookies();
        //attach WebViewClient to intercept the callback url
        webview.setWebViewClient(new OAuthWebViewClient());

        // Request focus for this webview
        webview.requestFocus(View.FOCUS_DOWN);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        // update the progress bar
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Return the app name after finish loading
                if (progress == 100) {
                    setTitle("Done");
                } else {
                    //Make the bar disappear after URL is loaded, and changes string to Loading...
                    setTitle("Loading: " + progress + "%");
                }
            }
        });

        // Send user to authorization page.
        webview.loadUrl(OAuthManager.getAuthorizeUrl());
    }

    @Override
    public void onFinished() {
        dismissDialog(DIALOG_GETTING_ACCESS_TOKEN);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFailed(Throwable th) {
        dismissDialog(DIALOG_GETTING_ACCESS_TOKEN);
        setTitle("Error from GitHub.");
        setResult(RESULT_CANCELED);
        finish();
    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            // Check for our custom callback protocol.
            // Otherwise, use default behavior.
            if (url.startsWith(Constants.REDIRECT_URL)
                    && exchangeAccessTokenTask == null) {
                Uri uri = Uri.parse(url);

                String code = uri.getQueryParameter("code");
                if (!TextUtils.isEmpty(code)) {
                    setTitle("Getting access token");
                    showDialog(DIALOG_GETTING_ACCESS_TOKEN);
                    exchangeAccessTokenTask =
                            new ExchangeAccessTokenTask(OAuthActivity.this);
                    exchangeAccessTokenTask.execute(code);
                } else {
                    setTitle("Error!!! Did you cancel?");
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    private void enableCookies() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_GETTING_ACCESS_TOKEN) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Getting access token");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return dialog;
        }
        return super.onCreateDialog(id);
    }
}