/*
This interface is used to decouple the UI from the 5minds CustomFormProps interface.
The Interface is built very similar to the CustomFormProps interface, but the implementation is not dependend on the 5minds SDK.

In case the Proces Engine is replaced with another engine, the actuall React components don´t have to change.
*/
export interface DecouplerProps {
  userTask: any;
  suspendState: any;
  abortUserTask: () => void;
  finishUserTask: (result: UserTaskResult) => void;
  suspendUserTask: (state: any) => void;
  config: any;
}

interface UserTaskResult {
  [formFieldId: string]: any;
}

interface userTask {
    startToken: Object;
    
}
