package com.huynguyen.GithubAnalytics.asynctasks;

import android.os.AsyncTask;
import com.huynguyen.GithubAnalytics.model.OAuthManager;

public class ExchangeAccessTokenTask extends AsyncTask<String, Void,
        Throwable> {

    private Listener listener;

    public ExchangeAccessTokenTask(Listener listener) {
        this.listener = listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected Throwable doInBackground(String... params) {
        try {
            OAuthManager.getInstace().exchangeAccessToken(params[0]);
            return null;
        } catch (Throwable th) {
            return th;
        }
    }

    @Override
    protected void onPostExecute(Throwable throwable) {
        super.onPostExecute(throwable);
        if (listener != null) {
            if (throwable == null) {
                listener.onFinished();
            } else {
                listener.onFailed(throwable);
            }
        }
    }

    public interface Listener {
        void onFinished();

        void onFailed(Throwable th);
    }
}
