import { Button, Divider, Flex, Modal, Pagination, Space, Tooltip } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import type { PaginationProps } from "antd";
import { useEffect, useState } from "react";
import SelectBuddy from "../../Components/PreOnboarding_SelectBuddy/SelectBuddy";
import EmployeeList from "../../Components/EmployeeList/EmployeeList";
import { SendOutlined, PlusOutlined } from "@ant-design/icons";
import styles from "./BuddyAndEmployeeSelection.module.css";

const BuddyAndEmployeeSelection: React.FC<CustomFormProps> = (props) => {
  const [selectedPage, setSelectedPage] = useState<number>(1);

  const [selectedBuddy, setSelectedBuddy] = useState<React.Key[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<React.Key[]>([]);

  const [isButtonDisabled, setIsButtonDisabled] = useState<boolean>(true);

  useEffect(() => {
    if (selectedBuddy.length === 0 || SelectBuddy === undefined) {
      console.log("Please select a buddy and at least one employee.");
      setIsButtonDisabled(true);
    } else {
      console.log("Buddy and employee selected.");
      setIsButtonDisabled(false);
    }
  }, [selectedBuddy, selectedEmployees]);

  function sendSelectionToProcess() {
    const result = {
      selectedBuddy: selectedBuddy.at(0),
      selectedEmployees: selectedEmployees,
    };
    if (selectedEmployees.length === 0) {
      Modal.confirm({
        title: "Kein Mitarbeiter ausgewählt!",
        content:
          "Sind Sie sich sicher, dass Sie sonst für keinen Mitarbeiter ein Kennenlerngespräch ausmachen wollen?",
        onOk() {
          props.finishUserTask(result);
        },
        onCancel() {
          return;
        },
      });
    } else {
      props.finishUserTask(result);
    }
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
          selectedBuddy={selectedBuddy}
        ></EmployeeList>
      )}
      <Flex justify="space-evenly" align="center">
        <Button
          className={styles.button}
          onClick={() => {
            setSelectedPage(2);
          }}
          hidden={selectedPage === 2}
        >
          Weiter
        </Button>
        <Button
          className={styles.button}
          onClick={() => {
            setSelectedPage(1);
          }}
          hidden={selectedPage === 1}
        >
          zurück
        </Button>

        {selectedPage === 2 ? (
          <Tooltip title="Bitte wählen Sie zumindest einen Buddy aus.">
            <Button
              type="primary"
              icon={<SendOutlined />}
              size="large"
              onClick={() => {
                sendSelectionToProcess();
              }}
              className={styles.button}
              disabled={isButtonDisabled}
            >
              Senden
            </Button>
          </Tooltip>
        ) : (
          <></>
        )}

        {/* <Button
          onClick={() => {
            console.log("SelectedBuddy is:" + selectedBuddy);
            console.log("SelectedEmployees is:" + selectedEmployees);
            console.log("Button is disabled:" + isButtonDisabled);
          }}
        >
          Check
        </Button> */}
      </Flex>
    </>
  );
};
export default BuddyAndEmployeeSelection;
