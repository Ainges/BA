import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import FirstPerformanceReview_Employee from "./FirstPerformanceReview_Employee";

const FirstPerformanceReview_EmployeeDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <FirstPerformanceReview_Employee {...DecouplerProps} />;
};
export default FirstPerformanceReview_EmployeeDecoupler;
