import { Button, Divider, Flex, Pagination, Space } from "antd";
import { CustomFormProps } from "../DialogRenderer";
import type { PaginationProps } from "antd";
import { useState } from "react";
import SelectBuddy from "../PreOnboarding_SelectBuddy/SelectBuddy";
import EmployeeList from "../EmployeeList/EmployeeList";
import { SendOutlined, PlusOutlined } from "@ant-design/icons";
import styles from "./BuddyAndEmployeeSelection.module.css";

const BuddyAndEmployeeSelection: React.FC<CustomFormProps> = (props) => {
  const [selectedPage, setSelectedPage] = useState<number>(1);
  const onPaginationChange: PaginationProps["onChange"] = (page) => {
    console.log(page);
    setSelectedPage(page);
  };

  const [selectedBuddy, setSelectedBuddy] = useState<React.Key[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<React.Key[]>([]);

  function sendSelectionToProcess() {
    const result = {
      selectedBuddy: selectedBuddy.at(0),
      selectedEmployees: selectedEmployees,
    };

    props.finishUserTask(result);
  }

  return (
    <>
      {selectedPage === 1 ? (
        <SelectBuddy
          selectedBuddy={selectedBuddy}
          setSelectedBuddy={setSelectedBuddy}
        ></SelectBuddy>
      ) : (
        <EmployeeList
          selectedEmployees={selectedEmployees}
          setSelectedEmployees={setSelectedEmployees}
        ></EmployeeList>
      )}
      <Flex justify="space-evenly" align="center">
        <Pagination
          defaultCurrent={10}
          current={selectedPage}
          onChange={onPaginationChange}
          total={20}
        />

        <Button
          type="primary"
          icon={<SendOutlined />}
          size="large"
          onClick={() => {
            sendSelectionToProcess();
          }}
          className={styles.button}
        >
          Senden
        </Button>
        <Button
          onClick={() => {
            console.log("SelectedBuddy is:" + selectedBuddy);
            console.log("SelectedEmployees is:" + selectedEmployees);
          }}
        >
          Check
        </Button>
      </Flex>
    </>
  );
};
export default BuddyAndEmployeeSelection;
