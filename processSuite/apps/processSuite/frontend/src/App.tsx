import { Component } from 'react';

import { CustomFormsRenderer, StartDialogRenderer } from './DialogRenderer';
import { default as config } from './config/config.json';

import './custom.css';

export default class App extends Component {
  static displayName = App.name;

  render(): JSX.Element {
    return (
      <div style={{ height: '100%' }}>
        <CustomFormsRenderer config={config} />
        <StartDialogRenderer config={config} />
      </div>
    );
  }
}
