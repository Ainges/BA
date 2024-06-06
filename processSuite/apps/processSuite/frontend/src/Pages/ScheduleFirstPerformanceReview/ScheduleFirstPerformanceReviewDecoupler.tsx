
import { DecouplerProps } from "../../Interfaces/Decoupler";
import ScheduleFirstPerformanceReview from "./ScheduleFirstPerformanceReview";

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

  return <ScheduleFirstPerformanceReview {...DecouplerProps} />;
};
export default ScheduleFirstPerformanceReviewDecoupler;
