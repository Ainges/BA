import React, { useEffect, useState } from "react";
import { CustomFormProps } from "../DialogRenderer";
import { Avatar, Card, Col, DatePicker, Layout, Row } from "antd";
import axios from "axios";
import styles from "./ExampleEmployeeList.module.css";
import Meta from "antd/es/card/Meta";

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
  const [employees, setEmployees] = useState<Employee[]>([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      const response = await axios.get(employeeAPIgetAllurl);
      setEmployees(response.data);
      console.log(response.data);
    };

    fetchEmployees();
  }, []);

  //   const [dimensions, setDimensions] = useState({
  //     width: window.innerWidth,
  //     height: window.innerHeight,
  //   });
  //   useEffect(() => {
  //     const updateDimensions = () => {
  //       setDimensions({
  //         width: window.innerWidth,
  //         height: window.innerHeight,
  //       });
  //     };

  //     window.addEventListener("resize", updateDimensions);

  //     return () => {
  //       window.removeEventListener("resize", updateDimensions);
  //     };
  //   }, []);

  return (
    <>
      {/* <div style={{ width: dimensions.width, height: dimensions.height }}> */}
      <Layout className={styles.layoutStyle}>
        <Header className={styles.headerStyle}>Header</Header>
        <Content className={styles.contentStyle}>
          <div className={styles.cardContainerStlye}>
            <Card title="Mitarbeiter"  className={styles.cardContainerStlye}>
              {employees.map((employee) => (
                <Card key={employee.username}>
                  <Meta
                    avatar={<Avatar size={100} src={employee.pictureURI} />}
                    title={employee.fullName}
                    description={employee.companyAndPosition}
                  />
                </Card>
              ))}
            </Card>
          </div>
        </Content>
        <Footer className={styles.footerStyle}>Footer</Footer>
      </Layout>
      {/* </div> */}
    </>
  );
};

export default ExampleEmployeeList;
