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
        guard let urlString = call.getString("url") else {
            call.error("Must provide a URL to open")
            return
        }
        
        if urlString.isEmpty {
            call.error("URL must not be empty")
            return
        }
        
        DispatchQueue.main.async {
            let url = URL(string: urlString)
            let webViewController = WKWebViewController.init()
            webViewController.source = .remote(url!)
            webViewController.bypassedSSLHosts = [url!.host!]
            webViewController.websiteTitleInNavigationBar = false
            webViewController.leftNavigaionBarItemTypes = [.reload]
            webViewController.toolbarItemTypes = [.back, .forward, .activity]
            let navigation = UINavigationController.init(rootViewController: webViewController)
            
            self.bridge.viewController.present(navigation, animated: true, completion: {
              call.success()
            })
        }
    }
}
