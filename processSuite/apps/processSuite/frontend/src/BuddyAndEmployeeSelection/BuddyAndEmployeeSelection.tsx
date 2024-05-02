import { Pagination } from "antd";
import { CustomFormProps } from "../DialogRenderer";
import type { PaginationProps } from "antd";
import { useState } from "react";
import SelectBuddy from "../PreOnboarding_SelectBuddy/SelectBuddy";
import EmployeeList from "../EmployeeList/EmployeeList";

const BuddyAndEmployeeSelection: React.FC<CustomFormProps> = (props) => {
  const [selectedPage, setSelectedPage] = useState<number>(1);
  const onPaginationChange: PaginationProps["onChange"] = (page) => {
    console.log(page);
    setSelectedPage(page);
  };

  return (
    <>
      {selectedPage === 1 ? (
        <SelectBuddy {...props}></SelectBuddy>
      ) : (
        <EmployeeList {...props}></EmployeeList>
      )}
      <Pagination
        defaultCurrent={10}
        current={selectedPage}
        onChange={onPaginationChange}
        total={20}
      />
    </>
  );
};
export default BuddyAndEmployeeSelection;
