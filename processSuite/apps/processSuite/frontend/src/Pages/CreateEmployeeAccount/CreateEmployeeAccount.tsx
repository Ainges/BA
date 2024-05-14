import { Card, Col, Divider, Form, Input, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { title } from "process";
import { useEffect, useState } from "react";
import { InewEmployeeDataType } from "../../Interfaces/InewEmployeeData";
import { formatDate } from "../../functions/formatDate";
import config from "../../config/config.json";
import axios from "axios";

type FieldType = {
  email: string;
  password: string;
};

const CreateEmployeeAccount: React.FC<CustomFormProps> = (props) => {
  const [newEmployeeData, setNewEmployeeData] = useState<EmployeeData[]>([]);

  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const currentToken = props.userTask.startToken;

    const NewEmployeedata: EmployeeData[] = [
      {
        title: "Name",
        data:
          currentToken.OnboardingData.first_name +
          " " +
          currentToken.OnboardingData.last_name,
      },
      {
        title: "Erster Arbeitstag",

        data: formatDate(currentToken.OnboardingData.first_working_day),
      },
      {
        title: "Anstellungsart",
        data: currentToken.OnboardingData.employment_status,
      },
      {
        title: "Vereinbarte Arbeitszeit pro Woche",
        data: currentToken.OnboardingData.hours_per_week,
      },
      {
        title: "Private Email",
        data: currentToken.OnboardingData.private_email,
      },
      {
        title: "Geburtsdatum",
        data: formatDate(currentToken.OnboardingData.birth_date),
      },
    ];

    setNewEmployeeData(NewEmployeedata);
  }, [props.userTask.startToken]);

  const first_name: string =
    props.userTask.startToken.OnboardingData.first_name;
  const last_name: string = props.userTask.startToken.OnboardingData.last_name;

  // 5head chatGPT function
  const checkEmailAvailability = async (_: any, value: string) => {
    try {
      setLoading(true);
      const response = await axios.get(
        `${config.camel.host}/api/canonical/user/checkmail/${value}@${config.domainOfCompany}`
      );
      const isAvailable = response.data.isEmailAvailable;
      if (!isAvailable) {
        return Promise.reject("Diese E-Mail-Adresse ist bereits vergeben.");
      }
      return Promise.resolve();
    } catch (error) {
      return Promise.reject("Fehler beim Überprüfen der E-Mail-Verfügbarkeit.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Divider orientation="center">
        Ergänzen von Daten für neuen Mitarbeiter Account
      </Divider>
      <Row>
        <Col span={1}></Col>
        <Col span={10}>
          <Card title="Eingabe">
            <Form
              name="basic"
              labelCol={{ span: 5 }}
              wrapperCol={{ span: 24 }}
              style={{ maxWidth: 600 }}
            >
              <Form.Item
                label="Email"
                name="email"
                initialValue={
                  first_name.toLowerCase() + "." + last_name.toLowerCase()
                }
                rules={[
                  {
                    required: true,
                    message: "Bitte geben Sie eine E-Mail-Adresse ein!",
                  },
                  { validator: checkEmailAvailability },
                ]}
              >
                <Input addonAfter={"@" + config.domainOfCompany} />
              </Form.Item>
              <Form.Item
                label="Passwort"
                name="password"
                rules={[
                  {
                    required: true,
                    message:
                      "Bitte vergeben Sie ein vorläufiges Passwort für den Mitarbeiter!",
                  },
                ]}
              >
                <Input.Password />
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={2}></Col>
        <Col span={10}>
          <EmployeeData employeeData={newEmployeeData} />
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};

export default CreateEmployeeAccount;
