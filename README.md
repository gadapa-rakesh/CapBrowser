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

***Close***

    CapBrowser.close().then(() => {
	    console.log('browser closed');
    })
   
***Available Events - Works for android with openPlainBrowser flag as true***

    CapBrowser.addListener("urlChangeEvent", (info:  any) => {
	    console.log(info.url)
    })

***Options***

    {
        url:  'http://google.com/',
        headers: { "one":  "1", "two":  "2" },
        openPlainBrowser: true // uses android WebView when true else CustomTabsIntent
    }

**TODO**
 - Implement toolbar on android WebView for basic user actions

**Credits**
 - [WKWebViewController](https://github.com/Meniny/WKWebViewController) - for iOS
