package com.cap.browser.plugin;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.util.Iterator;

@NativePlugin()
public class CapBrowser extends Plugin {
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";  // Change when in stable
    private CustomTabsClient customTabsClient;
    private CustomTabsSession currentSession;

    CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            customTabsClient = client;
            client.warmup(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /** Call Data **/
    private String url;
    private Bundle headers;

    @PluginMethod()
    public void openWebView(PluginCall call) {
        if(!this.setupData(call)) return;
        this.openWebViewIntent(call);
    }

    @PluginMethod()
    public void open(PluginCall call) {
        if(!this.setupData(call)) return;
        this.openChromeTab(call);
    }

    @PluginMethod()
    public void close(PluginCall call) {
        Intent intent = new Intent(getContext(), getBridge().getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
        call.success();
    }

    private boolean setupData(PluginCall call) {
        /** Get URL **/
        String requestedUrl = call.getString("url");

        if (requestedUrl == null) {
            call.error("Must provide a URL to open");
            return false;
        }

        if (requestedUrl.isEmpty()) {
            call.error("URL must not be empty");
            return false;
        }

        this.url = requestedUrl;

        /** Extract Headers **/
        JSObject headersProvided = call.getObject("headers");
        if(headersProvided != null) {
            Iterator<String> keys = headersProvided.keys();
            this.headers = new Bundle();
            while(keys.hasNext()) {
                String key = keys.next();
                this.headers.putString(key, headersProvided.getString(key));
            }
        }
        return true;
    }

    private void openWebViewIntent(PluginCall pluginCall) {
        WebViewBuilder builder = new WebViewBuilder(new WebViewCallbacks() {
            @Override
            public void urlChangeEvent(String url) {
                notifyListeners("urlChangeEvent", new JSObject().put("url", url));
            }

            @Override
            public void pageLoaded() {
                notifyListeners("browserPageLoaded", new JSObject());
            }
        });
        Intent intent = builder.buildWebView(getActivity());
        intent.putExtra("url", this.url);
        intent.putExtra("headers", this.headers);
        getContext().startActivity(intent);
        pluginCall.success();
    }

    private void openChromeTab(PluginCall pluginCall) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getCustomTabsSession());
        builder.addDefaultShareMenuItem();
        CustomTabsIntent tabsIntent = builder.build();
        tabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + getContext().getPackageName()));
        tabsIntent.intent.putExtra(android.provider.Browser.EXTRA_HEADERS, this.headers);
        tabsIntent.launchUrl(getContext(), Uri.parse(url));

        pluginCall.success();
    }

    protected void handleOnResume() {
        boolean ok = CustomTabsClient.bindCustomTabsService(getContext(), CUSTOM_TAB_PACKAGE_NAME, connection);
        if (!ok) {
            Log.e(getLogTag(), "Error binding to custom tabs service");
        }
    }

    protected void handleOnPause() {
        getContext().unbindService(connection);
    }

    public CustomTabsSession getCustomTabsSession() {
        if (customTabsClient == null) {
            return null;
        }

        if (currentSession == null) {
            currentSession = customTabsClient.newSession(new CustomTabsCallback(){
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    switch (navigationEvent) {
                        case NAVIGATION_FINISHED:
                            notifyListeners("browserPageLoaded", new JSObject());
                            break;
                    }
                }
            });
        }
        return currentSession;
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
    }
}
