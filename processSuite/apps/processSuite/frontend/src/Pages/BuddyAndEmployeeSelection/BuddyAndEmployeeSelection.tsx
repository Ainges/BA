import { CustomFormProps } from "../../DialogRenderer/CustomFormsRenderer";
import { DecouplerProps } from "../../Interfaces/Decoupler";
import BuddyAndEmployeeSelectionContextInjection from "./BuddyAndEmployeeSelectionContextInjection";
import BuddyAndEmployeeSelectionProvider from "./BuddyandEmployeeSelectionProvider";

const BuddyAndEmployeeSelection: React.FC<DecouplerProps> = (props) => {
  return (
    <>
      <BuddyAndEmployeeSelectionProvider>
        <BuddyAndEmployeeSelectionContextInjection
          {...props}
        ></BuddyAndEmployeeSelectionContextInjection>
      </BuddyAndEmployeeSelectionProvider>
    </>
  );
};
export default BuddyAndEmployeeSelection;
