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
   
   ***Available Events - Only for iOS***

    CapBrowser.addListener("urlChangeEvent", (info:  any) => {
	    console.log(info.url)
    })

**TODO**
 - Implement url change event for android

**Credits**
 - [WKWebViewController](https://github.com/Meniny/WKWebViewController) - for iOS
