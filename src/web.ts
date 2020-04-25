import { WebPlugin } from '@capacitor/core';
import { CapBrowserPlugin } from './definitions';

export class CapBrowserWeb extends WebPlugin implements CapBrowserPlugin {
  constructor() {
    super({
      name: 'CapBrowser',
      platforms: ['web']
    });
  }
  async open(options: import("./definitions").OpenOptions): Promise<any> {
    console.log(options);
    return true;
  }
  
  async close(): Promise<any> {
    return true;
  }
  
  async openWebView(options: import("./definitions").OpenWebViewOptions): Promise<any> {
    console.log(options);
    return true;
  }
}

const CapBrowser = new CapBrowserWeb();

export { CapBrowser };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(CapBrowser);
