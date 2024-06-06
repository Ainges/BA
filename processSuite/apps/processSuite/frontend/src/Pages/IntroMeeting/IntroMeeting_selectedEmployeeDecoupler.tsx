import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import IntroMeeting_selectedEmployee from "./IntoMeeting_selectedEmployee";




const IntroMeeting_selectedEmployeeDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <IntroMeeting_selectedEmployee {...DecouplerProps} />;
};
export default IntroMeeting_selectedEmployeeDecoupler;