declare module "@capacitor/core" {
  interface PluginRegistry {
    CapBrowser: CapBrowserPlugin;
  }
}

enum ToolBarType {
  ACTIVITY = "activity",
  NAVIGATION = "navigation",
  BLANK = "blank",
  DONE = "done",
}

export interface Headers {
  [key: string] : string;
}

export interface OpenOptions {
  url: string;
  headers?: Headers;
  isFullScreenPresentationStyle?: boolean;
}

export interface DisclaimerOptions {
  title: string;
  message: string;
  confirmBtn: string;
  cancelBtn: string;
}

export interface OpenWebViewOptions {
  url: string;
  headers?: Headers;
  shareDisclaimer?: DisclaimerOptions;
  toolbarType?: ToolBarType;
  shareSubject?: string;
  title: string;
  isFullScreenPresentationStyle?: boolean;
}

export interface CapBrowserPlugin {
  open(options: OpenOptions): Promise<any>;
  close(): Promise<any>;
  openWebView(options: OpenWebViewOptions): Promise<any>;
}
