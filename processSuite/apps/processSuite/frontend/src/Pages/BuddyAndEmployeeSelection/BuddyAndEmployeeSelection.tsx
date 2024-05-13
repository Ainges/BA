import { CustomFormProps } from "../../DialogRenderer/CustomFormsRenderer";
import BuddyAndEmployeeSelectionContextInjection from "./BuddyAndEmployeeSelectionContextInjection";
import BuddyAndEmployeeSelectionProvider from "./BuddyandEmployeeSelectionProvider";

const BuddyAndEmployeeSelection: React.FC<CustomFormProps> = (props) => {
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
