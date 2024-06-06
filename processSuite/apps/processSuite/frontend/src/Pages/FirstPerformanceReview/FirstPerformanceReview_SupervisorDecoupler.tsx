import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import FirstPerformanceReview_Supervisor from "./FirstPerformanceReview_Supervisor";

const FirstPerformanceReview_SupervisorDecoupler: React.FC<CustomFormProps> = (
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

  return <FirstPerformanceReview_Supervisor {...DecouplerProps} />;
};
export default FirstPerformanceReview_SupervisorDecoupler;
