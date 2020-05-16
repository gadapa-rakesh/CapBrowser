
  Pod::Spec.new do |s|
    s.name = 'CapBrowser'
    s.version = '0.0.1'
    s.summary = 'Capacitor wrapper around https://github.com/Meniny/WKWebViewContr oller with android support'
    s.license = 'MIT'
    s.homepage = 'https://github.com/gadapa-rakesh/CapBrowser.git'
    s.author = 'Rakesh Rao'
    s.source = { :git => 'https://github.com/gadapa-rakesh/CapBrowser.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.resources = 'ios/Plugin/**/*.{lproj,storyboard,xcdatamodeld,xib,xcassets,json}'
  end