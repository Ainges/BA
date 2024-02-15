import React from 'react';

import { DataModels, Identity } from '@atlas-engine/atlas_engine_client';
import { CustomFormService, FormState } from '@atlas-engine-contrib/atlas-ui_sdk';

import { Config } from '../config';
import { ExampleCustomForm } from '../ExampleCustomForm';


export type CustomFormProps = {
  userTask: DataModels.FlowNodeInstances.UserTaskInstance;
  suspendState: FormState;
  abortUserTask: () => void;
  finishUserTask: (result: DataModels.FlowNodeInstances.UserTaskResult) => void;
  suspendUserTask: (state: FormState) => void;
  config: Config,
}

export type CustomFormRendererProps = {
  components?: CustomFormsComponentDict;
  config?: Config;
}

export type CustomFormsServiceState = {
  targetComponent: React.ComponentClass<CustomFormProps> | React.FunctionComponent<CustomFormProps> | null;
  userTask: DataModels.FlowNodeInstances.UserTaskInstance | null;
  suspendState: FormState | null;
}

export type CustomFormsComponentDict = {
  [preferredControl: string]: React.ComponentClass<CustomFormProps> | React.FunctionComponent<CustomFormProps>;
}

export class CustomFormsRenderer extends React.Component<CustomFormRendererProps, CustomFormsServiceState> {

  public state: CustomFormsServiceState = {
    targetComponent: null,
    userTask: null,
    suspendState: null,
  };

  private components: CustomFormsComponentDict = {
    ExampleCustomForm: ExampleCustomForm,
  };

  private customFormService: CustomFormService | null = null;

  public componentDidMount(): void {
    this.customFormService = new CustomFormService();
    this.customFormService.onUserTaskReceived(this.updateComponent);

    this.components = {
      ...this.components,
      ...this.props.components,
    };
  }

  public updateComponent = (
    userTask: DataModels.FlowNodeInstances.UserTaskInstance,
    _identity: Identity,
    suspendState: FormState | null,
  ): void => {

    const preferredControl = userTask.userTaskConfig.customForm;
    if (!preferredControl) {
      throw new Error(`No preferredControl set in usertask: ${JSON.stringify(userTask)}`);
    }

    const component = this.components[preferredControl];
    if (!component) {
      throw new Error(`No component found for usertask ${JSON.stringify(userTask)}`);
    }

    this.setState({
      targetComponent: component,
      userTask: userTask,
      suspendState: suspendState,
    });
  };

  public componentWillUnmount(): void {
    if (!this.customFormService) {
      return;
    }
    this.customFormService.destroy();
  }

  public suspendUserTask = (state: FormState): void => {
    this.customFormService?.suspendUserTask(state);
  };

  public abortUserTask = (): void => {
    this.customFormService?.terminateProcessInstance();
  };

  public finishUserTask = (result: DataModels.FlowNodeInstances.UserTaskResult): void => {
    this.customFormService?.finishUserTask(result);
  };

  public render(): JSX.Element | null {
    if (!this.state.targetComponent) {
      return null;
    }

    const componentInstance = React.createElement(this.state.targetComponent, {
      userTask: this.state.userTask,
      suspendState: this.state.suspendState,
      suspendUserTask: this.suspendUserTask,
      abortUserTask: this.abortUserTask,
      finishUserTask: this.finishUserTask,
    } as CustomFormProps);

    return componentInstance;
  }

}
