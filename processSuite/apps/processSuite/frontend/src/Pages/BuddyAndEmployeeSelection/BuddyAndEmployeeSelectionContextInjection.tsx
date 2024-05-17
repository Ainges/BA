import { Button, Flex, Modal, Tooltip } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { useContext, useEffect, useState } from "react";
import SelectBuddy from "../../Components/PreOnboarding_SelectBuddy/SelectBuddy";
import EmployeeList from "../../Components/EmployeeList/EmployeeList";
import { SendOutlined } from "@ant-design/icons";
import styles from "./BuddyAndEmployeeSelection.module.css";

import axios from "axios";
import config from "../../config/config.json";
import { BuddyAndEmployeeSelectionContext } from "./BuddyandEmployeeSelectionProvider";

interface EmployeeDTO {
  email: string;
  first_name: string;
  last_name: string;
  position: string;
  profile_picture_url: string;
}

const BuddyAndEmployeeSelectionContextInjection: React.FC<CustomFormProps> = (
  props
) => {
  // context initialization
  const context = useContext(BuddyAndEmployeeSelectionContext);

  // context State
  const { selectedBuddy, setSelectedBuddy } = context;
  const { selectedEmployees, setSelectedEmployees } = context;
  const { employeeData, setEmployeeData } = context;
  const {
    employeeDataFiltered: employeeDataWithoutBuddy,
    setEmployeeDataFiltered: setEmployeeDataWithoutBuddy,
  } = context;

  // local state
  const [selectedPage, setSelectedPage] = useState<number>(1);
  const [isButtonDisabled, setIsButtonDisabled] = useState<boolean>(true);

  // get URL of API
  const camelHost = config.camel.host;

  useEffect(() => {
    // Fetch employee data from API
    const fetchEmployees = async () => {
      try {
        // Wait to simulate loading
        //TODO: Remove for production
        await new Promise((resolve) => setTimeout(resolve, 300));

        const response = await axios.get(camelHost + "/api/canonical/user/");
        const employees = response.data;
        // Transform employee data to TableDataType
        const tableData = employees.map((employee: EmployeeDTO) => ({
          key: employee.email,
          name: `${employee.first_name} ${employee.last_name}`,
          position: employee.position,
          email: employee.email,
          profilePictureURI: employee.profile_picture_url,
        }));

        setEmployeeData(tableData);
        console.log("Fetched employees:", tableData);
      } catch (error) {
        console.error("Error fetching employees:", error);
      }
    };
    fetchEmployees();

    console.log("viewport height: ", window.innerHeight);
    console.log("viewport width: ", window.innerWidth);
  }, []);

  useEffect(() => {
    if (selectedBuddy.length === 0 || selectedBuddy === undefined) {
      console.log("Please select a buddy and at least one employee.");
      setIsButtonDisabled(true);
    } else {
      console.log("Buddy and employee selected.");
      setIsButtonDisabled(false);
    }
  }, [selectedBuddy, selectedEmployees]);

  // filter out the selected buddy from the employee data
  useEffect(() => {
    const employeeDataWithoutBuddy = employeeData.filter(
      (employee) => employee.key !== selectedBuddy[0]
    );

    if (selectedEmployees.includes(selectedBuddy[0])) {
      // filter out the buddy from the selected employees, if already selected
      setSelectedEmployees(
        selectedEmployees.filter((employee) => employee !== selectedBuddy[0])
      );
    }

    console.log("Employee data without buddy:", employeeDataWithoutBuddy);
    setEmployeeDataWithoutBuddy(employeeDataWithoutBuddy);
  }, [selectedBuddy, employeeData]);

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
      <div id="mycontainer" className={styles.container}>
        {/* Page 1 */}

        {selectedPage === 1 ? (
          // "props" passed through Context
          <SelectBuddy></SelectBuddy>
        ) : (
          // Page 2
          // "props" passed through Context
          <EmployeeList></EmployeeList>
        )}

        {/* Page 1 */}

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

          {/* Page 2 */}
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
            <Tooltip
              title={
                selectedBuddy.length === 0
                  ? "Bitte wählen Sie zumindest einen Buddy aus."
                  : ""
              }
            >
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
        </Flex>
      </div>
    </>
  );
};
export default BuddyAndEmployeeSelectionContextInjection;
