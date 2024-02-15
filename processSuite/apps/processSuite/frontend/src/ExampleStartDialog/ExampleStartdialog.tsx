import React from 'react';

import { StartDialogProps, StartDialogRendererState } from '../DialogRenderer';

export class ExampleStartDialog extends React.Component<StartDialogProps, StartDialogRendererState> {

  public render(): JSX.Element {
    return (
      <p>This is a start-dialog.</p>
    );
  }
}
