# CapBrowser
This [capacitor](https://capacitor.ionicframework.com/) plugin looks similar to [Browser](https://capacitor.ionicframework.com/docs/apis/browser) core plugin but with additional features like 
 - Http header support (iOS & Android)
 - Browser url change event (iOS & Android)
 - Can show LaunchImage as privacy screen (iOS only)
 - Support for plain browser (iOS & Android)
 - Custom configuration for browser UI (iOS & Android)
 

**API** 

***open***

```javascript
CapBrowser.open({
    url:  'http://google.com/',
    headers: { "one":  "1", "two":  "2" },
    isPresentAfterPageLoad: true
}).then(() => {
    console.log('showing the window');
})
```

***openWebView - Opens a plain webView instance without navBar***

```javascript
CapBrowser.openWebView({
    url: 'https://www.google.com/',
    headers: { "one": "1", "two": "2" },
    title: "Custom Title",
    hideNavBar: false,
    toolbarType: "navigation",
    shareDisclaimer: {
    title: "Disclaimer",
    message: "Some Content",
    confirmBtn: "Proceed",
    cancelBtn: "No"
    },
    shareSubject: "Some subject",
    isPresentAfterPageLoad: true
}).then(() => {
    console.log('showing the window');
})
```

***Close***

```javascript
CapBrowser.close().then(() => {
    console.log('browser closed');
})
```
   
***Available Events - Works for both android (on webView) and iOS***

```javascript
CapBrowser.addListener("urlChangeEvent", (info:  any) => {
    console.log(info.url)
})

CapBrowser.addListener("confirmBtnClicked", (info:  any) => {
    // will be triggered when user clicks on confirm button when disclaimer is required, works only on iOS
    console.log(info.url)
})
```

***Options***

**toobarType**
```javascript
export enum ToolBarType {
    ACTIVITY = "activity",
    NAVIGATION = "navigation",
    BLANK = "blank",
    DEFAULT = ""
}
```

**TODO**
 
 [ ] XXX

**Credits**
 - [WKWebViewController](https://github.com/Meniny/WKWebViewController) - for iOS
