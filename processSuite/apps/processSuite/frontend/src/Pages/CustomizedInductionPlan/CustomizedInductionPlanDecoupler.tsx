import { abort } from "process";
import { CustomFormProps } from "../../DialogRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import CustomizedInductionPlan from "./CustomizedInductionPlan";




const CustomizedInductionPlanDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <CustomizedInductionPlan {...DecouplerProps} />;
};
export default CustomizedInductionPlanDecoupler;