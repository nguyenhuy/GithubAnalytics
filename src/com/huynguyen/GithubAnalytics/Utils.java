package com.huynguyen.GithubAnalytics;

import android.text.TextUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String appendParams(final String originalUrl,
                                      List<NameValuePair> params) {
        if (TextUtils.isEmpty(originalUrl)
                || params == null
                || params.size() == 0) {
            return originalUrl;
        }
        StringBuilder builder = new StringBuilder(originalUrl);
        if (!originalUrl.endsWith("?")) {
            builder.append("?");
        }
        builder.append(URLEncodedUtils.format(params, "utf-8"));
        return builder.toString();
    }

    public static void validate(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new IOException("POST request failed with status code " + statusCode);
        }
    }

    public static boolean sameDate(Date date1, Date date2) {
        return date1.getYear() == date2.getYear()
                && date1.getMonth() == date2.getMonth()
                && date1.getDate() == date2.getDate();
    }
}
