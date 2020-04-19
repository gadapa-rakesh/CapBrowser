package com.cap.browser.plugin;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;

public class WebViewBuilder {
    public static WebViewCallbacks callbacks;

    public WebViewBuilder(WebViewCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public Intent buildWebView(Activity activity) {
        Intent browserIntent = new Intent(activity, WebViewActivity.class);
        return browserIntent;
    }
}
