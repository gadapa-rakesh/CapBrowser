import Foundation
import Capacitor
import WKWebViewController

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CapBrowser)
public class CapBrowser: CAPPlugin {
    
    @objc func open(_ call: CAPPluginCall) {
        let inputURL = call.getString("url") ?? ""
        
        let url = URL.init(string: inputURL)!
        let webViewController = WKWebViewController.init()
        webViewController.source = .remote(url)
        webViewController.bypassedSSLHosts = [url.host!]
        webViewController.userAgent = call.getString("user-agent") ?? ""
        webViewController.websiteTitleInNavigationBar = false
        webViewController.navigationItem.title = url.host
        webViewController.leftNavigaionBarItemTypes = [.reload]
        webViewController.toolbarItemTypes = [.back, .forward, .activity]
        
        let navigation = UINavigationController.init(rootViewController: webViewController)
        
        self.bridge.viewController.present(navigation, animated: true, completion: {
          call.success()
        })
    }
}
