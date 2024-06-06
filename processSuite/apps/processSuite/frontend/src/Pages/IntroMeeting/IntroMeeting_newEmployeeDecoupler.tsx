import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import IntroMeeting_newEmployee from "./IntroMeeting_newEmployee";

const IntroMeeting_newEmployeeDecoupler: React.FC<CustomFormProps> = (
  props
) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <IntroMeeting_newEmployee {...DecouplerProps} />;
};
export default IntroMeeting_newEmployeeDecoupler;
