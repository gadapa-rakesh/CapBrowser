declare module "@capacitor/core" {
  interface PluginRegistry {
    CapBrowser: CapBrowserPlugin;
  }
}

export interface CapBrowserPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
