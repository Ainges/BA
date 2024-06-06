import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import ScheduleIntroMeetings from "./ScheduleIntroMeetings";

const ScheduleIntroMeetingsDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <ScheduleIntroMeetings {...DecouplerProps} />;
};
export default ScheduleIntroMeetingsDecoupler;
