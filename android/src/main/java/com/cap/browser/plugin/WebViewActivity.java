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
    private boolean initialPageLoaded = false;
    private Toolbar toolbar = null;
    private String toolbarType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        this.setupToolbar();

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

    private void setupToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24px);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.toolbarType = getIntent().getStringExtra("toolbarType");
        if(TextUtils.equals(toolbarType, "activity")) {
            toolbar.getMenu().findItem(R.id.action_forward).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_back).setVisible(false);
            //TODO: Add share button functionality
        } else if(TextUtils.equals(toolbarType, "navigation")) {
            //TODO: Remove share button when implemented
        } else if(TextUtils.equals(toolbarType, "blank")){
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.getMenu().findItem(R.id.action_forward).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_back).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(item.getItemId() == R.id.action_forward) {
            webView.goForward();
            return true;
        } else if(item.getItemId() == R.id.action_back) {
            webView.goBack();
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
                MenuItem backButton = toolbar.getMenu().findItem(R.id.action_back);
                if(webView.canGoBack()) {
                    backButton.setIcon(R.drawable.arrow_back_enabled);
                    backButton.setEnabled(true);
                } else {
                    backButton.setIcon(R.drawable.arrow_back_disabled);
                    backButton.setEnabled(false);
                }

                MenuItem forwardBtn = toolbar.getMenu().findItem(R.id.action_forward);
                if(webView.canGoForward()) {
                    forwardBtn.setIcon(R.drawable.arrow_forward_enabled);
                    forwardBtn.setEnabled(true);
                } else {
                    forwardBtn.setIcon(R.drawable.arrow_forward_disabled);
                    forwardBtn.setEnabled(false);
                }

                WebViewBuilder.callbacks.pageLoaded();
                if(!initialPageLoaded) {
                    webView.clearHistory();
                    initialPageLoaded = true;
                }
                super.onPageFinished(view, url);
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

    @Override
    public void onBackPressed() {
        if(webView.canGoBack() && "navigation".equalsIgnoreCase(this.toolbarType)) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class CustomChromeClient extends WebChromeClient {
        Context context;

        public CustomChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
