import { abort } from "process";
import { CustomFormProps } from "../../DialogRenderer";

import { DecouplerProps } from "../../Interfaces/Decoupler";
import SelectEquipment from "./SelectEquipment";

const SelectEquipmentDecoupler: React.FC<CustomFormProps> = (props) => {
  const DecouplerProps: DecouplerProps = {
    userTask: props.userTask,
    suspendState: props.suspendState,
    abortUserTask: props.abortUserTask,
    finishUserTask: props.finishUserTask,
    suspendUserTask: props.suspendUserTask,
    config: props.config,
  };

  return <SelectEquipment {...DecouplerProps} />;
};
export default SelectEquipmentDecoupler;
