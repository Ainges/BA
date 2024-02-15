import React from 'react';

import { Identity } from '@atlas-engine/atlas_engine_client';
import { StartDialogDisplayedCallback, StartDialogService } from '@atlas-engine-contrib/atlas-ui_sdk';

import { Config } from '../config';
import { ExampleStartDialog } from '../ExampleStartDialog';


export type DialogComponentDict = {
  [id: string]: string;
}

export type StartDialogProps = {
  language: string,
  identity: Identity;
  startDialogConfiguration: StartDialogConfiguration;
  closeStartDialog: () => void;
  openStartDialog: (startDialogId: string) => void;
  startProcess: (processModelId: string, payload?: unknown, startEventId?: string) => void;
  config: Config | undefined;
};

export type StartDialogRendererProps = {
  components?: StartDialogComponentDict;
  config?: Config;
}

export type StartDialogComponentDict = {
  [startDialogId: string]: React.ComponentClass<StartDialogProps> | React.FunctionComponent<StartDialogProps>;
}

export type StartDialogRendererState = {
  targetComponent: React.ComponentClass<StartDialogProps> | React.FunctionComponent<StartDialogProps> | null;
  targetStartDialogConfiguration: StartDialogConfiguration | null;
  targetIdentity: Identity | null;
  currentLanguage: string;
}

export type StartDialogConfiguration = {
  id: string;
  title: string;
  body: string;
  url: string;
  startButtonTitle: string;
}

class StartDialogRenderer extends React.Component<StartDialogRendererProps, StartDialogRendererState> {

  private startDialogService: StartDialogService | null = null;

  constructor(props: StartDialogRendererProps) {
    super(props);

    this.state = {
      targetComponent: null,
      targetIdentity: null,
      targetStartDialogConfiguration: null,
      currentLanguage: 'de',
    };
  }

  private components: StartDialogComponentDict = {
    ExampleStartDialog: ExampleStartDialog,
  };

  public componentDidMount(): void {
    this.startDialogService = new StartDialogService();
    this.startDialogService.onStartDialogDisplayed(this.displayStartDialog as StartDialogDisplayedCallback);

    this.components = {
      ...this.components,
      ...this.props.components,
    };
  }

  public componentWillUnmount(): void {
    if (!this.startDialogService) {
      return;
    }
    this.startDialogService.destroy();
  }

  private displayStartDialog = (config: StartDialogConfiguration, identity: Identity): void => {
    const component = this.components[config.id];
    if (!component) {
      throw new Error(`No component found for usertask ${JSON.stringify(config)}`);
    }

    this.setState({
      targetComponent: component,
      targetIdentity: identity,
      targetStartDialogConfiguration: config,
    });
  };

  public render(): JSX.Element | null {
    if (!this.state.targetComponent || !this.state.targetStartDialogConfiguration || !this.state.targetIdentity) {
      return null;
    }

    const props: StartDialogProps = {
      language: 'de',
      closeStartDialog: (): void => this.startDialogService?.closeStartDialog(),
      openStartDialog: (startDialogId: string): void => this.startDialogService?.openStartDialog(startDialogId),
      identity: this.state.targetIdentity,
      startDialogConfiguration: this.state.targetStartDialogConfiguration,
      startProcess: (processModelId, payload?, startEventId?): void => {
        this.startDialogService?.startProcess(processModelId, payload, startEventId);
      },
      config: this.props.config,
    };

    const componentInstance = React.createElement(this.state.targetComponent, props);

    return componentInstance;
  }

}

export default StartDialogRenderer;
