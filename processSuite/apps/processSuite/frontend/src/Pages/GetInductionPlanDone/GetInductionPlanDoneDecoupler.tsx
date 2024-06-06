import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import GetInductionPlanDone from "./GetInductionPlanDone";

const GetInductionPlanDoneDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <GetInductionPlanDone {...DecouplerProps} />;
};
export default GetInductionPlanDoneDecoupler;
