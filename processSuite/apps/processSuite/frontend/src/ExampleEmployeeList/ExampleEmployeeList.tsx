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

  //   return (
  //     <>
  //       <div className={styles.outerStyle}>
  //         <Card
  //           className={styles.cardContainerStyle}
  //           title={
  //             <>
  //               <Typography.Text>
  //                 Bitte wählen Sie die Mitarbeiter aus, für die ein
  //                 Kennenlerntermin mit dem neuen Mitarbeiter gefunden werden soll.
  //               </Typography.Text>
  //               <Button
  //                 type="primary"
  //                 icon={<SendOutlined />}
  //                 className={styles.buttonStyle}
  //               >
  //                 Senden
  //               </Button>
  //             </>
  //           }
  //         >
  //           <div className={styles.contentContainerStyle}>
  //             <Row gutter={[16, 16]}>
  //               {employees.map((employee) => (
  //                 <Col
  //                   xs={{ flex: "100%" }}
  //                   sm={{ flex: "50%" }}
  //                   md={{ flex: "40%" }}
  //                   lg={{ flex: "20%" }}
  //                   xl={{ flex: "10%" }}
  //                 >
  //                   <Card
  //                     className={
  //                       cardselected.includes(employee.username)
  //                         ? styles.employeeCardSelected
  //                         : styles.employeeCard
  //                     }

  //                     loading={employees.length === 0 || isLoading}
  //                     key={employee.username}
  //                     onClick={() => handleEmployeeCardClick(employee.username)}
  //                     actions={[<PlusOutlined selected key="add" />]}
  //                   >
  //                     <Meta
  //                       avatar={<Avatar size={100} src={employee.pictureURI} />}
  //                       title={employee.fullName}
  //                       description={employee.companyAndPosition}
  //                     />
  //                   </Card>
  //                 </Col>
  //               ))}
  //             </Row>
  //           </div>
  //         </Card>
  //       </div>
  //     </>
  //   );

  return (
    <>
    <Card title="Test" className={styles.leftCard}>Test</Card>
      <div className={styles.outerContainer}>
                   {employees.map((employee) => (
                  <Col
                  >
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
                  </Col>
                ))}
        {/* <Card className={styles.cards} title="1">
          Test1
        </Card>
        <Card className={styles.cards} title="2">
          Test2
        </Card>
        <Card className={styles.cards} title="3">
          Test3
        </Card>
        <Card className={styles.cards} title="4">
          Test4
        </Card>
        <Card className={styles.cards} title="5">
          Test5
        </Card>
        <Card className={styles.cards} title="6">
          Test6
        </Card>
        <Card className={styles.cards} title="7">
          Test7
        </Card>
        <Card className={styles.cards} title="8">
          Test8
        </Card> */}
      </div>
    </>
  );
};

export default ExampleEmployeeList;
