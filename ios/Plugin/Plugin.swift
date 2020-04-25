import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CapBrowser)
public class CapBrowser: CAPPlugin {
    var navigationWebViewController: UINavigationController?
    private var privacyScreen: UIImageView?
    private var isSetupDone = false
    
    private func setup(){
        self.isSetupDone = true
        
        #if swift(>=4.2)
        NotificationCenter.default.addObserver(self, selector: #selector(appDidBecomeActive(_:)), name: UIApplication.didBecomeActiveNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(appWillResignActive(_:)), name: UIApplication.willResignActiveNotification, object: nil)
        #else
        NotificationCenter.default.addObserver(self, selector: #selector(appDidBecomeActive(_:)), name:.UIApplicationDidBecomeActive, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(appWillResignActive(_:)), name:.UIApplicationWillResignActive, object: nil)
        #endif
    }
    
    @objc func openWebView(_ call: CAPPluginCall) {
        if !self.isSetupDone {
            self.setup()
        }
        guard let urlString = call.getString("url") else {
            call.error("Must provide a URL to open")
            return
        }
        
        if urlString.isEmpty {
            call.error("URL must not be empty")
            return
        }
        
        var headers = call.get("headers", [String: String].self, [:])
        let userAgent = headers?["User-Agent"]
        headers?.removeValue(forKey: "User-Agent")
        
        var disclaimerContent = call.getObject("shareDisclaimer", defaultValue: nil)
        let toolbarType = call.getString("toolbarType")
        if toolbarType != "activity" {
            disclaimerContent = nil
        }
        
        DispatchQueue.main.async {
            let url = URL(string: urlString)
            let webViewController = WKWebViewController.init()
            webViewController.source = .remote(url!)
            webViewController.headers = headers
            webViewController.leftNavigaionBarItemTypes = self.getToolbarItems(toolbarType: toolbarType ?? "")
            
            webViewController.toolbarItemTypes = []
            webViewController.doneBarButtonItemPosition = .right
            webViewController.capBrowserPlugin = self
            webViewController.title = call.getString("title") ?? ""
            webViewController.shareSubject = call.getString("shareSubject")
            if userAgent != nil {
                webViewController.customUserAgent = userAgent
            }
            webViewController.shareDisclaimer = disclaimerContent
            self.navigationWebViewController = UINavigationController.init(rootViewController: webViewController)
            self.navigationWebViewController?.navigationBar.isTranslucent = false
            self.navigationWebViewController?.toolbar.isTranslucent = false
            self.navigationWebViewController?.navigationBar.backgroundColor = .white
            self.navigationWebViewController?.toolbar.backgroundColor = .white
            self.navigationWebViewController?.modalPresentationStyle = .fullScreen
            self.navigationWebViewController?.navigationBar.isHidden = call.getBool("hideNavBar", false) ?? false
            self.bridge.viewController.present(self.navigationWebViewController!, animated: true, completion: {
              call.success()
            })
        }
    }
    
    func getToolbarItems(toolbarType: String) -> [BarButtonItemType] {
        var result: [BarButtonItemType] = []
        if toolbarType == "activity" {
            result.append(.activity)
        } else if toolbarType == "navigation" {
            result.append(.back)
            result.append(.forward)
        }
        return result
    }
    
    @objc func open(_ call: CAPPluginCall) {
        if !self.isSetupDone {
            self.setup()
        }
        guard let urlString = call.getString("url") else {
            call.error("Must provide a URL to open")
            return
        }
        
        if urlString.isEmpty {
            call.error("URL must not be empty")
            return
        }
        
        let headers = call.get("headers", [String: String].self, [:])
        
        DispatchQueue.main.async {
            let url = URL(string: urlString)
            let webViewController = WKWebViewController.init()
            webViewController.source = .remote(url!)
            webViewController.headers = headers
            webViewController.leftNavigaionBarItemTypes = [.reload]
            webViewController.toolbarItemTypes = [.back, .forward, .activity]
            webViewController.capBrowserPlugin = self
            webViewController.hasDynamicTitle = true
            self.navigationWebViewController = UINavigationController.init(rootViewController: webViewController)
            self.navigationWebViewController?.navigationBar.isTranslucent = false
            self.navigationWebViewController?.toolbar.isTranslucent = false
            self.navigationWebViewController?.navigationBar.backgroundColor = .white
            self.navigationWebViewController?.toolbar.backgroundColor = .white
            self.navigationWebViewController?.modalPresentationStyle = .fullScreen
            self.bridge.viewController.present(self.navigationWebViewController!, animated: true, completion: {
              call.success()
            })
        }
    }
    
    @objc func close(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
         self.navigationWebViewController?.dismiss(animated: true, completion: nil)
         call.success()
        }
    }
    
    private func showPrivacyScreen(){
        if privacyScreen == nil {
            self.privacyScreen = UIImageView()
            if let launchImage = UIImage(named: "LaunchImage") {
                privacyScreen!.image = launchImage
                privacyScreen!.frame = UIScreen.main.bounds
                privacyScreen!.contentMode = .scaleAspectFill
                privacyScreen!.isUserInteractionEnabled = false
            } else if let launchImage = UIImage(named: "Splash") {
                privacyScreen!.image = launchImage
                privacyScreen!.frame = UIScreen.main.bounds
                privacyScreen!.contentMode = .scaleAspectFill
                privacyScreen!.isUserInteractionEnabled = false
            }
        }
        self.navigationWebViewController?.view.addSubview(self.privacyScreen!)
    }
    
    private func hidePrivacyScreen(){
        self.privacyScreen?.removeFromSuperview()
    }
    
    @objc func appDidBecomeActive(_ notification: NSNotification) {
        self.hidePrivacyScreen()
    }
    
    @objc func appWillResignActive(_ notification: NSNotification) {
        self.showPrivacyScreen()
    }
}
