import { Button, Card, Col, Divider, Form, Input, Row, Space } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { title } from "process";
import { useEffect, useState } from "react";
import { InewEmployeeDataType } from "../../Interfaces/InewEmployeeData";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";
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

        data: formatDateToDEformat(
          currentToken.OnboardingData.first_working_day
        ),
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
        data: formatDateToDEformat(currentToken.OnboardingData.birth_date),
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

  // chatGPT function for validating input
  const validateInput = (_: any, value: any) => {
    const invalidCharacters = /[<>()\[\]\\,;:@\s"]/; // Regex for invalid characters
    if (invalidCharacters.test(value)) {
      return Promise.reject("Ungültige Zeichen im E-Mail-Benutzernamen.");
    }
    return Promise.resolve();
  };

  const submit = (values: any) => {
    console.log("Values: " + values.email + " " + values.password);
    props.finishUserTask({
      email: values.email + "@" + config.domainOfCompany,
      password: values.password,
    });
  };

  type emailType = {};

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
              onFinish={submit}
            >
              <Form.Item
                label="Email"
                name="email"
                hasFeedback
                validateDebounce={300}
                initialValue={
                  first_name.toLowerCase() + "." + last_name.toLowerCase()
                }
                rules={[
                  {
                    required: true,
                    message: "Bitte geben Sie eine E-Mail-Adresse ein!",
                  },
                  { validator: checkEmailAvailability },
                  { validator: validateInput },
                ]}
              >
                <Input
                  maxLength={32}
                  addonAfter={"@" + config.domainOfCompany}
                />
              </Form.Item>
              <Form.Item
                label="Passwort"
                name="password"
                hasFeedback
                validateDebounce={500}
                rules={[
                  {
                    required: true,
                    message:
                      "Bitte vergeben Sie ein vorläufiges Passwort für den Mitarbeiter!",
                  },
                  {
                    min: 8,
                    message:
                      "Das Passwort muss mindestens 8 Zeichen lang sein.",
                  },
                ]}
              >
                <Input.Password />
              </Form.Item>
              <Space direction="horizontal">
                <Button type="primary" htmlType="submit">
                  Senden
                </Button>
              </Space>
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
