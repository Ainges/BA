import { CustomFormProps } from "../../DialogRenderer";

import { DecouplerProps } from "../../Interfaces/Decoupler";
import CreateEmployeeAccount from "./CreateEmployeeAccount";




const CreateEmployeeAccountDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <CreateEmployeeAccount {...DecouplerProps} />;
};
export default CreateEmployeeAccountDecoupler;