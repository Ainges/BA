import {
  Divider,
  Row,
  Col,
  Card,
  Table,
  Flex,
  Button,
  Tooltip,
  DatePicker,
  TimePicker,
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { Key, useEffect, useState } from "react";
import styles from "./ScheduleIntroMeetings.module.css";
import { SendOutlined } from "@ant-design/icons";
import axios from "axios";
import config from "../../config/config.json";
import { render } from "react-dom";

interface Employee {
  name: string;
  email: string;
  date: string | null;
  time: string | null;
}
interface EmployeeDataApi {
  email: string;
  first_name: string;
  last_name: string;
  position: string;
  profile_picture_url: string;
}

const ScheduleIntroMeetings: React.FC<CustomFormProps> = (props) => {
  const camelHost = config.camel.host;
  const token = props.userTask.startToken;

  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const [selectedEmployeesWithDateTime, setSelectedEmployeesWithDateTime] =
    useState<Employee[]>();
  const [selectedDateTime, setSelectedDateTime] = useState<{
    [key: string]: Date | null;
  }>({});

  const [buddy, setBuddy] = useState<Employee>();
  const [buddyDataSource, setBuddyDataSource] = useState<Employee[]>([]);

  const [selectedEmployees, setSelectedEmployees] = useState<Employee[]>(
    token.selectedEmployees
  );
  const [apiData, setApiData] = useState<EmployeeDataApi[]>([]);

  // Query all employees
  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        // Wait to simulate loading
        //TODO: Remove for production
        await new Promise((resolve) => setTimeout(resolve, 300));

        const response = await axios.get(camelHost + "/api/canonical/user/");
        setApiData(response.data);

        console.log("Fetched employees:", response.data);
        console.log("Buddy:", buddy);
        console.log("Employees:", selectedEmployees);
      } catch (error) {
        console.error("Error fetching employees:", error);
      }
    };
    fetchEmployees();
  }, []);
  //  prepare selected employees for table
  useEffect(() => {
    const selectedEmployeesWithDateTime: Employee[] = apiData.map(
      (employee) => {
        return {
          key: employee.email,
          email: employee.email,
          name: employee.first_name + " " + employee.last_name,
          position: employee.position,
          date: null,
          time: null,
        };
      }
    );
    console.log(
      "Selected INIT employees with date and time:",
      selectedEmployeesWithDateTime
    );
    setSelectedEmployeesWithDateTime(selectedEmployeesWithDateTime);
  }, [apiData]);

  // prepare buddy Object for table
  useEffect(() => {
    const localBuddy: Employee = {
      email: token.selectedBuddy,
      name:
        apiData.find((employee) => employee.email === token.selectedBuddy)
          ?.first_name +
        " " +
        apiData.find((employee) => employee.email === token.selectedBuddy)
          ?.last_name,
      date: null,
      time: null,
    };
    console.log("Buddy:", localBuddy);
    setBuddy(localBuddy);

    const buddyDataSource = [localBuddy];
    console.log("BuddyDataSource:", buddyDataSource);
    setBuddyDataSource(buddyDataSource);
  }, [apiData]);

  const sendSelectionToProcess = () => {};


  // #### handle Date Change ####
  const handleDateChange = (
    date: Date | null,
    dateString: string,
    key: Key
  ) => {
    console.log(
      "Date formated to ISO String:",
      date?.toISOString().split("T")[0]
    );
    console.log("DateString:", dateString);
    console.log("Key:", key);

    if (date === null) {
      console.error("Date is null in handleDateChange!");
      return;
    }

    setSelectedEmployeesWithDateTime(() => {
      return selectedEmployeesWithDateTime?.map((employee) => {
        if (employee.email === key) {
          return {
            ...employee,
            date: date?.toISOString().split("T")[0],
          };
        } else {
          return employee;
        }
      });
    });
  };

  // #### handle Time Change ####
  const handleTimeChange = (time: Date | null, timeString: string) => {
    console.log(time, timeString);
  };

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "email",
    },
    {
      title: "Datum",
      dataIndex: "date",
      key: "date",
      render: (text: Date, record: any) => {
        return (
          <DatePicker
            format="DD.MM.YYYY"
            onChange={(date, dateString) => {
              handleDateChange(
                date.toDate(),
                dateString.toString(),
                record.key
              );
            }}
          ></DatePicker>
        );
      },
    },
    {
      title: "Uhrzeit",
      dataIndex: "time",
      key: "time",
      render: () => {
        return <TimePicker format="HH:mm"></TimePicker>;
      },
    },
  ];

  return (
    <>
      <div id="mycontainer" className={styles.container}>
        <Divider orientation="center">Kennenlerngespräche terminieren</Divider>
        <Row>
          <Col span={1}></Col>
          <Col span={22}>
            <Card title="Ausewählte Mitarbeiter:">
              <Table
                pagination={{ position: [] }}
                scroll={{ y: 300 }}
                columns={columns}
                dataSource={selectedEmployeesWithDateTime}
                size="small"
              />
            </Card>
            <Col span={1}></Col>
          </Col>
        </Row>
        <Row>
          <Col span={1}></Col>
          <Col span={22}>
            <Card title="Buddy:">
              <Table
                pagination={{ position: [] }}
                scroll={{ y: 300 }}
                style={{ height: "200px" }}
                columns={columns}
                dataSource={buddyDataSource}
                size="small"
              />
            </Card>
          </Col>
          <Col span={1}></Col>
        </Row>
        <Flex justify="space-evenly" align="center">
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
          <Button
            onClick={() => {
              console.log(
                "Selected employees with date and time:",
                selectedEmployeesWithDateTime
              );
            }}
          >
            Check state
          </Button>
        </Flex>
      </div>
    </>
  );
};

export default ScheduleIntroMeetings;
