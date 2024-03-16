import React, { useEffect, useState } from "react";
import { CustomFormProps } from "../DialogRenderer";
import {
  Avatar,
  Button,
  Card,
  Checkbox,
  Col,
  DatePicker,
  Layout,
  Row,
  Typography,
} from "antd";
import axios from "axios";
import styles from "./ExampleEmployeeList.module.css";
import Meta from "antd/es/card/Meta";
import { SendOutlined, PlusOutlined } from "@ant-design/icons";

const ExampleEmployeeList: React.FC<CustomFormProps> = () => {
  const { Header, Footer, Sider, Content } = Layout;

  interface Employee {
    id: number;
    username: string;
    email: string;
    fullName: string;
    pictureURI: string;
    companyAndPosition: string;
  }

  const employeeAPIgetAllurl = "http://localhost:8080/employee/all";
  const test = "https://reqres.in/api/users?page=2";

  // TODO: REMOVE THIS
  // ### Loading Delay moked ###
  const [isLoading, setIsLoading] = useState(true);

  // Effect to handle loading delay
  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 500); // 500ms delay

    // Cleanup function to clear the timer
    return () => clearTimeout(timer);
  }, []);

  // ### React State ###

  // Array of Employees
  const [employees, setEmployees] = useState<Employee[]>([]);

  // State of the selected cards
  const [cardselected, setcardSelected] = useState<String[]>([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      const response = await axios.get(employeeAPIgetAllurl);
      setEmployees(response.data);
      console.log(response.data);
    };

    fetchEmployees();
  }, []);

  const handleEmployeeCardClick = (employeeUsername: string) => {
    if (cardselected.includes(employeeUsername)) {
      // If the employee is already selected, remove them from the array
      console.log("Unselecting Employee " + employeeUsername);
      setcardSelected(
        cardselected.filter((username) => username !== employeeUsername)
      );
    } else {
      // If the employee is not selected, add them to the array
      console.log("Selecting Employee " + employeeUsername);
      setcardSelected([...cardselected, employeeUsername]);
    }
    console.log("Click ");
  };

  return (
    <>
      <div className={styles.outerStyle}>
        {/* <Card className={styles.instructionCard}>
          <Button
            type="primary"
            icon={<SendOutlined/>}
            className={styles.buttonStyle}
          >
            Senden
          </Button>
        </Card> */}
        <Card
          className={styles.cardContainerStyle}
          title="Bitte wählen Sie alle Mitarbeiter aus, mit denen ein Kennenlerntermin stattfinden soll."
        >
          <Button
            type="primary"
            icon={<SendOutlined />}
            className={styles.buttonStyle}
          >
            Senden
          </Button>
          <div className={styles.cardContainerContentStyle}>
            {employees.map((employee) => (
              // <Card className={styles.employeeOuterCard}>
              //   <div onClick={() => alert("Hello from here")}>
              <Card
                className={
                  cardselected.includes(employee.username)
                    ? styles.employeeCardSelected
                    : styles.employeeCard
                }
                loading={employees.length === 0 || isLoading}
                key={employee.username}
                onClick={() => handleEmployeeCardClick(employee.username)}
                actions={[<PlusOutlined selected key="add" />]}
              >
                <Meta
                  avatar={<Avatar size={100} src={employee.pictureURI} />}
                  title={employee.fullName}
                  description={employee.companyAndPosition}
                />
              </Card>
            ))}
          </div>
        </Card>
      </div>
    </>
  );
};

export default ExampleEmployeeList;
