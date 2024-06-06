import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import KeyPointsForFirstWorkingDay from "./KeyPointsForFirstWorkingDay";

const KeyPointsForFirstWorkingDayDecoupler: React.FC<CustomFormProps> = (
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

  return <KeyPointsForFirstWorkingDay {...DecouplerProps} />;
};
export default KeyPointsForFirstWorkingDayDecoupler;
