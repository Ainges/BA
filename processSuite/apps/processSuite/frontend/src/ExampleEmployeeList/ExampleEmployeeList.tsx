import React, { useEffect, useState } from "react";
import { CustomFormProps } from "../DialogRenderer";
import {
  Avatar,
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
import { SettingOutlined, PlusOutlined } from "@ant-design/icons";

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
      {/* <Card className={styles.instructionCard}>
        <Typography.Title level={4}>Mitarbeiter Auswahl</Typography.Title>
        <Typography.Paragraph>
          Wählen Sie die Mitarbeiter aus, mit denen der neue Miarbeiter ein Gesprächstermin ausmachen soll.
        </Typography.Paragraph>
      </Card> */}
      <div className={styles.outerStyle}>
        <Card className={styles.cardContainerStyle} title="Mitarbeiter">
          {employees.map((employee) => (
            // <Card className={styles.employeeOuterCard}>
            //   <div onClick={() => alert("Hello from here")}>
            <Card
              className={
                cardselected.includes(employee.username)
                  ? styles.employeeCardSelected
                  : styles.employeeCard
              }
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
            //   </div>
            //   <Checkbox className={styles.checkboxStyle}></Checkbox>
            // </Card>
          ))}
        </Card>
      </div>
    </>
  );
};

export default ExampleEmployeeList;
