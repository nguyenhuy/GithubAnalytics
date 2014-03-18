package com.huynguyen.GithubAnalytics.asynctasks;

import android.os.AsyncTask;
import com.huynguyen.GithubAnalytics.model.IssuesManager;

/**
 * Created with IntelliJ IDEA.
 * User: nguyenthanhhuy
 * Date: 1/25/13
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class FetchIssuesTask extends AsyncTask<Void, Void, Throwable> {
    private Listener listener;

    public FetchIssuesTask(Listener listener) {
        this.listener = listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            IssuesManager.getInstance().fetchIssues();
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
                listener.onFinish();
            } else {
                listener.onFailed(throwable);
            }
        }
    }

    public interface Listener {
        void onFinish();

        void onFailed(Throwable th);
    }
}
