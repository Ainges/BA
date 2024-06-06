import { abort } from "process";
import { CustomFormProps } from "../../DialogRenderer";
import OrganizeSmallPresent from "./OrganizeSmallPresent";
import { DecouplerProps } from "../../Interfaces/Decoupler";




const OrganizeSmallPresentDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <OrganizeSmallPresent {...DecouplerProps} />;
};
export default OrganizeSmallPresentDecoupler;