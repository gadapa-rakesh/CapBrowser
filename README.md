# CapBrowser
This [capacitor](https://capacitor.ionicframework.com/) plugin looks similar to [Browser](https://capacitor.ionicframework.com/docs/apis/browser) core plugin but with additional features like 
 - Http header support
 - Browser url change event

**API** 

***open***

    CapBrowser.open({
        url:  'http://google.com/',
        headers: { "one":  "1", "two":  "2" }
    }).then(() => {
        console.log('showing the window');
    })

***openWebView - Opens a plain webView instance without navBar***

    CapBrowser.openWebView({
        url:  'http://google.com/',
        headers: { "one":  "1", "two":  "2" },
        title: "Custom Title",
        hideNavBar: true
    }).then(() => {
        console.log('showing the window');
    })

***Close***

    CapBrowser.close().then(() => {
	    console.log('browser closed');
    })
   
***Available Events - Works for both android (on webView) and iOS***

    CapBrowser.addListener("urlChangeEvent", (info:  any) => {
	    console.log(info.url)
    })

***Options***

    {
        url:  'http://google.com/',
        headers: { "one":  "1", "two":  "2" }
    }

**TODO**
 - Implement toolbar on android WebView for basic user actions

**Credits**
 - [WKWebViewController](https://github.com/Meniny/WKWebViewController) - for iOS
