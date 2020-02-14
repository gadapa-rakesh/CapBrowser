package com.cap.browser.plugin;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cap.browser.plugin.capbrowser.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        boolean hideNavBar = getIntent().getBooleanExtra("hideNavBar", false);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        if(hideNavBar) toolbar.setVisibility(View.GONE);

        this.webView = findViewById(R.id.browser_view);
        url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            finish();
        }
        initWebView();

        Bundle headers = getIntent().getBundleExtra("headers");
        Map<String, String> requestHeaders = new HashMap<>();
        if(headers != null) {
            Set<String> strings = headers.keySet();
            for(String key : strings) {
                requestHeaders.put(key, headers.getString(key));
            }
        }
        webView.loadUrl(url, requestHeaders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        webView.setWebChromeClient(new CustomChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                WebViewBuilder.callbacks.urlChangeEvent(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewBuilder.callbacks.pageLoaded();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private class CustomChromeClient extends WebChromeClient {
        Context context;

        public CustomChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
