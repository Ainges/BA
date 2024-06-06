import { abort } from "process";
import { CustomFormProps } from "../../DialogRenderer";

import { DecouplerProps } from "../../Interfaces/Decoupler";

const ScheduleFirstPerformanceReviewDecoupler: React.FC<DecouplerProps> = (
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

  return <ScheduleFirstPerformanceReviewDecoupler {...DecouplerProps} />;
};
export default ScheduleFirstPerformanceReviewDecoupler;
