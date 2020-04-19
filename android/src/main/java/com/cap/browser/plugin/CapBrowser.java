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
import android.text.TextUtils;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONException;
import org.json.JSONObject;

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

    @PluginMethod()
    public void open(PluginCall call) {
        String url = call.getString("url");
        if(url == null || TextUtils.isEmpty(url)) {
            call.error("Invalid URL");
        }
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getCustomTabsSession());
        builder.addDefaultShareMenuItem();
        CustomTabsIntent tabsIntent = builder.build();
        tabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + getContext().getPackageName()));
        tabsIntent.intent.putExtra(android.provider.Browser.EXTRA_HEADERS, this.getHeaders(call));
        tabsIntent.launchUrl(getContext(), Uri.parse(url));

        call.success();
    }

    @PluginMethod()
    public void openWebView(PluginCall call) {
        String url = call.getString("url");
        if(url == null || TextUtils.isEmpty(url)) {
            call.error("Invalid URL");
        }
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
        intent.putExtra("url", url);
        intent.putExtra("headers", this.getHeaders(call));
        intent.putExtra("title", call.getString("title", "New Window"));
        intent.putExtra("hideNavBar", call.getBoolean("hideNavBar", false));
        intent.putExtra("toolbarType", call.getString("toolbarType", ""));

        JSONObject disclaimerInput = call.getObject("shareDisclaimer", null);
        Bundle disclaimerContent = new Bundle();
        if(disclaimerInput != null) {
            try {
                disclaimerContent.putString("title", disclaimerInput.getString("title"));
                disclaimerContent.putString("message", disclaimerInput.getString("message"));
                disclaimerContent.putString("confirmBtn", disclaimerInput.getString("confirmBtn"));
                disclaimerContent.putString("cancelBtn", disclaimerInput.getString("cancelBtn"));
            } catch (JSONException e) {
                // do nothing in case of exception
                e.printStackTrace();
            }
        }
        intent.putExtra("shareDisclaimerContent", disclaimerContent);
        intent.putExtra("shareSubject", call.getString("shareSubject", null));
        getContext().startActivity(intent);
        call.success();
    }

    @PluginMethod()
    public void close(PluginCall call) {
        Intent intent = new Intent(getContext(), getBridge().getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
        call.success();
    }

    private Bundle getHeaders(PluginCall pluginCall) {
        JSObject headersProvided = pluginCall.getObject("headers");
        Bundle headers = new Bundle();
        if(headersProvided != null) {
            Iterator<String> keys = headersProvided.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                headers.putString(key, headersProvided.getString(key));
            }
        }
        return headers;
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
