import { Plugin } from "@capacitor/core/dist/esm/definitions";

declare module "@capacitor/core" {
  interface PluginRegistry {
    CapBrowser: CapBrowserPlugin;
  }
}

export enum ToolBarType {
  ACTIVITY = "activity",
  NAVIGATION = "navigation",
  BLANK = "blank",
  DEFAULT = "",
}

export interface Headers {
  [key: string] : string;
}

export interface OpenOptions {
  url: string;
  headers?: Headers;
  isPresentAfterPageLoad?: boolean;
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
  isPresentAfterPageLoad?: boolean;
}

export interface CapBrowserPlugin extends Plugin {
  open(options: OpenOptions): Promise<any>;
  close(): Promise<any>;
  openWebView(options: OpenWebViewOptions): Promise<any>;
}
