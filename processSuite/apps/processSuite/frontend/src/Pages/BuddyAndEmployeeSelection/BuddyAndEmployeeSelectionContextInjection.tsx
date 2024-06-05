import {
  Button,
  Flex,
  Modal,
  Tooltip,
  DatePicker,
  TimePicker,
  Divider,
  Card,
  Table,
  TableColumnsType,
  Row,
  Col,
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { useContext, useEffect, useState } from "react";
import SelectBuddy from "../../Components/PreOnboarding_SelectBuddy/SelectBuddy";
import EmployeeList from "../../Components/EmployeeList/EmployeeList";
import { SendOutlined } from "@ant-design/icons";
import styles from "./BuddyAndEmployeeSelection.module.css";

import axios from "axios";
import config from "../../config/config.json";
import {
  BuddyAndEmployeeSelectionContext,
  TableDataEmployee,
} from "./BuddyandEmployeeSelectionProvider";

interface EmployeeDTO {
  email: string;
  first_name: string;
  last_name: string;
  position: string;
  profile_picture_url: string;
}

interface TableDataSelectTime {
  key: string;
  name: string;
  date: Date | null;
  time: Date | null;
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
  const { employeeDataFiltered, setEmployeeDataFiltered } = context;

  // local state
  const [selectedPage, setSelectedPage] = useState<number>(1);
  const [isButtonDisabled, setIsButtonDisabled] = useState<boolean>(true);

  const [selectedEmployeesWithDateTime, setSelectedEmployeesWithDateTime] =
    useState<TableDataSelectTime[]>([]);

  // get URL of API
  const camelHost = config.camel.host;

  const token = props.userTask.startToken;

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

        // filter out the new employee from the employee data
        const employeeDataWithoutNewEmployee = tableData.filter(
          (employee: { key: any }) => employee.key !== token.email
        );
        console.log("Removed employee:", token.email);

        setEmployeeData(employeeDataWithoutNewEmployee);
        console.log("Fetched employees:", tableData);
      } catch (error) {
        console.error("Error fetching employees:", error);
      }
    };
    fetchEmployees();
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
    setEmployeeDataFiltered(employeeDataWithoutBuddy);
  }, [selectedBuddy, employeeData]);

  // populate the selectedEmployeesWithDateTime state
  useEffect(() => {
    const selectedEmployeesWithDateTime = selectedEmployees.map((employee) => ({
      // Cheap way to convert the key to a string
      // TODO: Find better way
      key: employee.toString() || "", // Convert the key value to a string
      name: employeeData.find((data) => data.key === employee)?.name || "",
      date: null,
      time: null,
    }));
    console.log(
      "Selected employees with date and time:",
      selectedEmployeesWithDateTime
    );
    setSelectedEmployeesWithDateTime(selectedEmployeesWithDateTime);
  }, [selectedEmployees]);

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
        cancelText: "Zurück",
        okText: "Ja, ich bin sicher!",
        okButtonProps: {
          danger: true,
        },
      });
    } else {
      props.finishUserTask(result);
    }
  }

  if (selectedPage === 1) {
    return (
      <>
        <div id="mycontainer" className={styles.container}>
          <SelectBuddy></SelectBuddy>
          <Flex justify="space-evenly" align="center">
            <Tooltip
              title={
                selectedBuddy.length === 0
                  ? "Bitte wählen Sie zumindest einen Buddy aus."
                  : ""
              }
            >
              <Button
                className={styles.button}
                onClick={() => {
                  setSelectedPage(2);
                }}
              >
                Weiter
              </Button>
            </Tooltip>
          </Flex>
        </div>
      </>
    );
  } else if (selectedPage === 2) {
    return (
      <>
        <div id="mycontainer" className={styles.container}>
          <EmployeeList></EmployeeList>
          <Flex justify="space-evenly" align="center">
            <Button
              className={styles.button}
              onClick={() => {
                setSelectedPage(1);
              }}
            >
              zurück
            </Button>

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
          </Flex>
        </div>
      </>
    );
  } else {
    return <></>;
  }
};
export default BuddyAndEmployeeSelectionContextInjection;
