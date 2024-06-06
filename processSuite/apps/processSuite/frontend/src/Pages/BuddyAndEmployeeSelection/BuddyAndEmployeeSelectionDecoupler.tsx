import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import BuddyAndEmployeeSelection from "./BuddyAndEmployeeSelection";

const BuddyAndEmployeeSelectionDecoupler: React.FC<CustomFormProps> = (
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

  return <BuddyAndEmployeeSelection {...DecouplerProps} />;
};
export default BuddyAndEmployeeSelectionDecoupler;
