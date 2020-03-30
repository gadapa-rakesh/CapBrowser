# CapBrowser
This [capacitor](https://capacitor.ionicframework.com/) plugin looks similar to [Browser](https://capacitor.ionicframework.com/docs/apis/browser) core plugin but with additional features like 
 - Http header support (iOS & Android)
 - Browser url change event (iOS & Android)
 - Can show LaunchImage as privacy screen (iOS only)
 - Support for plain browser (iOS & Android)
 - Custom configuration for browser UI (iOS & Android)
 

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
        hideNavBar: false,
        hideShareBtn: true
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
        headers: { "one":  "1", "two":  "2" },
        hideShareBtn: true,
        hideNavBar: false
    }

**TODO**

 [ ] Implement capability for hideShareBtn & custom User-Agent

**Credits**
 - [WKWebViewController](https://github.com/Meniny/WKWebViewController) - for iOS
