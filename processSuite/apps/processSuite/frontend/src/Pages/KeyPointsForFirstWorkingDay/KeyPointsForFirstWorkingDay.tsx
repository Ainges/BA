import { Card, Col, Divider, Form, Row, Select } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { formatDate } from "../../functions/formatDate";
import { useEffect, useState } from "react";
import axios from "axios";
import config from "../../config/config.json";

const KeyPointsForFirstWorkingDay: React.FC<CustomFormProps> = (props) => {
  const [apiData, setApiData] = useState<apiData[]>();
  const [availableContacts, setAvailableContacts] = useState<employee[]>([]);

  const token = props.userTask.startToken;
  const onboardingData = token.OnboardingData;
  const nameOfEmployee =
    onboardingData.first_name + " " + onboardingData.last_name;

  const employeeData: EmployeeData[] = [
    {
      title: "Name",
      data: nameOfEmployee,
    },
    {
      title: "Erster Arbeitstag",
      data: formatDate(onboardingData.first_working_day),
    },
    {
      title: "Position",
      data: onboardingData.position,
    },
    {
      title: "Art der Anstellung",
      data: onboardingData.employment_status,
    },
  ];

  type options = {
    value: string;
    label: string;
  };

  type apiData = {
    email: string;
    first_name: string;
    last_name: string;
    position: string;
    profile_picture_url: string;
  };

  async function getApiData() {
    try {
      const response = await axios.get(
        config.camel.host + "/api/canonical/user/"
      );
      console.log("Success");
      console.log(response.data);
      setApiData(response.data);
    } catch (error) {
      console.error("Error");
      console.error(error);
    }
  }

  useEffect(() => {
    getApiData();
  }, []);

  useEffect(() => {
    if (apiData) {
      const contacts: options[] = [];
      apiData.forEach((element: any) => {
        contacts.push({
          label:
            element.first_name +
            " " +
            element.last_name +
            " - " +
            element.position,
          value: element.email,
        });
      });
      setAvailableContacts(contacts);
    }
  }, [apiData]);

  return (
    <>
      <Divider orientation="center">
        Eckpunkte für ersten Arbeitstag von {nameOfEmployee} definieren
      </Divider>
      <Row>
        <Col span={12}>
          <Card title="Eckpunkte">
            <Form name="keyPoints">
              <Form.Item label="Ansprechpartner" name="contactPerson">
                <Select options={availableContacts}></Select>
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={12}>
          <EmployeeData employeeData={employeeData} />
        </Col>
      </Row>
    </>
  );
};
export default KeyPointsForFirstWorkingDay;
