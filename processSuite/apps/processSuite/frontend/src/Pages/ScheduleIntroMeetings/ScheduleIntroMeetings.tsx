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
  Space,
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { Key, useEffect, useState } from "react";
import styles from "./ScheduleIntroMeetings.module.css";
import { SendOutlined } from "@ant-design/icons";
import axios from "axios";
import config from "../../config/config.json";
import formatDateToYYYYMMDD from "../../functions/formatDateToYYYYMMDD";

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

  // const [selectedDateTime, setSelectedDateTime] = useState<{
  //   [key: string]: Date | null;
  // }>({});

  const [buddy, setBuddy] = useState<Employee>();
  const [buddyDataSource, setBuddyDataSource] = useState<Employee[]>([]);

  const [selectedEmployeesEmail, setSelectedEmployeesEmail] = useState<
    string[]
  >(token.selectedEmployees);

  const [apiData, setApiData] = useState<EmployeeDataApi[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<EmployeeDataApi[]>(
    []
  );

  // Filter sleected employees from API Data
  useEffect(() => {
    const selectedEmployeesLocal = apiData.filter((employee) =>
      selectedEmployeesEmail.includes(employee.email)
    );

    console.log(
      "Selected employees, enhanced with API Data:",
      selectedEmployeesLocal
    );
    setSelectedEmployees(selectedEmployeesLocal);
  }, [selectedEmployeesEmail, apiData]);

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
        console.log("selectedBuddy:", buddy);
        console.log("selectedEmployeesEmail:", selectedEmployeesEmail);
      } catch (error) {
        console.error("Error fetching employees:", error);
      }
    };
    fetchEmployees();
  }, []);
  //  prepare selected employees for table
  useEffect(() => {
    // TODO: Use Props Data instead of API Data
    // TODO: Enhance props Data with Api Data
    const selectedEmployeesWithDateTime: Employee[] = selectedEmployees.map(
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
  }, [apiData, selectedEmployees]);

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

  // #### Send Selection to Process ####
  const sendSelectionToProcess = () => {
    // only return the most important data to the process
    const selectedEmployeesWithDateTimeFiltered =
      selectedEmployeesWithDateTime?.map((employee) => {
        return {
          email: employee.email,
          date: employee.date,
          time: employee.time,
        };
      });
    const buddyFiltered = {
      email: buddy?.email,
      date: buddy?.date,
      time: buddy?.time,
    };

    props.finishUserTask({
      selectedEmployees: selectedEmployeesWithDateTimeFiltered,
      selectedBuddy: buddyFiltered,
    });
  };

  // #### handle Date Change for Employees ####
  const handleDateChangeEmployees = (
    date: Date | null,
    dateString: string,
    key: Key
  ) => {
    // The reasonf for using the formatDateToYYYYMMDD function is that the the toISOString function returns the date in UTC format
    // this lead to a wrong date in the process
    // for example: 2021-09-01T00:00:00.000Z -> 2021-08-31
    setSelectedEmployeesWithDateTime(() => {
      return selectedEmployeesWithDateTime?.map((employee) => {
        if (employee.email === key) {
          return {
            ...employee,
            date:
              date === null || date === undefined
                ? null
                : formatDateToYYYYMMDD(date),
          };
        } else {
          return employee;
        }
      });
    });
  };

  // #### handle Time Change for Employees ####
  const handleTimeChangeEmployees = (
    time: Date | null,
    timeString: string,
    key: Key
  ) => {
    setSelectedEmployeesWithDateTime(() => {
      return selectedEmployeesWithDateTime?.map((employee) => {
        if (employee.email === key) {
          return {
            ...employee,
            time: time === null || time === undefined ? null : timeString,
          };
        } else {
          return employee;
        }
      });
    });
  };

  // #### handle Date Change for Buddy ####
  const handleDateChangeBuddy = (
    date: Date | null,
    dateString: string,
    key: Key
  ) => {
    // The reasonf for using the formatDateToYYYYMMDD function is that the the toISOString function returns the date in UTC format
    // this lead to a wrong date in the process
    // for example: 2021-09-01T00:00:00.000Z -> 2021-08-31

    if (buddy === undefined) {
      console.info("Buddy is undefined in handleDateChange of Buddy!");
      return;
    }

    setBuddy(() => {
      return {
        ...buddy,
        date:
          date === null || date === undefined
            ? null
            : formatDateToYYYYMMDD(date),
      };
    });
  };

  // #### handle Time Change for Buddy ####
  const handleTimeChangeBuddy = (
    time: Date | null,
    timeString: string,
    key: Key
  ) => {
    console.log("Time:", time);
    console.log("TimeString:", timeString);

    //TODO: Implement better error handling

    if (buddy === undefined) {
      console.info("Buddy is undefined in handleTimeChange of Buddy!");
      return;
    }

    setBuddy(() => {
      return {
        ...buddy,
        time: time === null || time === undefined ? null : timeString,
      };
    });
  };

  // #### Check if Submission is ok ####
  useEffect(() => {
    const isEveryEmployeeScheduled = selectedEmployeesWithDateTime?.every(
      (employee) => employee.date !== null && employee.time !== null
    );

    const isBuddyScheduled = buddy?.date !== null && buddy?.time !== null;
    console.log("isEveryEmployeeScheduled:", isEveryEmployeeScheduled);
    console.log("isBuddyScheduled:", isBuddyScheduled);

    if (isEveryEmployeeScheduled && isBuddyScheduled) {
      console.log("Every employee has time and date set");
      console.log(() => {});
      setIsButtonDisabled(false);
    } else {
      console.log("Not every employee has time and date set yet");
      setIsButtonDisabled(true);
    }
  }, [selectedEmployeesWithDateTime, buddy]);

  const columnsEmployees = [
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
            placeholder="Datum"
            onChange={(date, dateString) => {
              if (date === null) {
                handleDateChangeEmployees(null, "", record.key);
                return;
              }
              handleDateChangeEmployees(
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
      render: (text: Date, record: any) => {
        return (
          <TimePicker
            format="HH:mm"
            onChange={(time, timeString) => {
              if (time === null) {
                handleTimeChangeEmployees(null, "", record.key);
                return;
              }
              handleTimeChangeEmployees(
                time.toDate(),
                timeString.toString(),
                record.key
              );
            }}
          ></TimePicker>
        );
      },
    },
  ];
  const columnsBuddy = [
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
            placeholder="Datum"
            onChange={(date, dateString) => {
              if (date === null) {
                handleDateChangeBuddy(null, "", record.key);
                return;
              }

              handleDateChangeBuddy(
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
      render: (text: Date, record: any) => {
        return (
          <TimePicker
            format="HH:mm"
            onChange={(time, timeString) => {
              if (time === null) {
                handleTimeChangeBuddy(null, "", record.key);
                return;
              }

              handleTimeChangeBuddy(
                time.toDate(),
                timeString.toString(),
                record.key
              );
            }}
          ></TimePicker>
        );
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
                columns={columnsEmployees}
                dataSource={selectedEmployeesWithDateTime}
                size="small"
              />
            </Card>
            <Col span={1}></Col>
          </Col>
        </Row>
        <br />
        <Row>
          <Col span={1}></Col>
          <Col span={22}>
            <Card title="Buddy:">
              <Table
                pagination={{ position: [] }}
                scroll={{ y: 300 }}
                style={{ height: "200px" }}
                columns={columnsBuddy}
                dataSource={buddyDataSource}
                size="small"
              />
            </Card>
          </Col>
          <Col span={1}></Col>
        </Row>
        <br />
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
          {/* <Button
            onClick={() => {
              console.log(
                "Selected employees with date and time:",
                selectedEmployeesWithDateTime
              );
            }}
          >
            Check state Employees
          </Button>
          <Button
            onClick={() => {
              console.log("Buddy:", buddy);
            }}
          >
            Check state Buddy
          </Button> */}
        </Flex>
      </div>
    </>
  );
};

export default ScheduleIntroMeetings;
