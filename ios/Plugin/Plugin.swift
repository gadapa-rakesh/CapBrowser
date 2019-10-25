import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CapBrowser)
public class CapBrowser: CAPPlugin {
    var navigationWebViewController: UINavigationController?
    
    @objc func open(_ call: CAPPluginCall) {
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
            self.navigationWebViewController = UINavigationController.init(rootViewController: webViewController)
            self.navigationWebViewController?.navigationBar.backgroundColor = .white
            self.navigationWebViewController?.modalPresentationStyle = .fullScreen
            self.bridge.viewController.present(self.navigationWebViewController!, animated: true, completion: {
              call.success()
            })
        }
    }
    
    @objc func close(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
         self.navigationWebViewController?.dismiss(animated: true, completion: nil)
        }
    }
}
