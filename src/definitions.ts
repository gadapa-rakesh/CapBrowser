declare module "@capacitor/core" {
  interface PluginRegistry {
    CapBrowser: CapBrowserPlugin;
  }
}

export interface CapBrowserPlugin {
  open(options: { value: string }): Promise<{value: string}>;
}
