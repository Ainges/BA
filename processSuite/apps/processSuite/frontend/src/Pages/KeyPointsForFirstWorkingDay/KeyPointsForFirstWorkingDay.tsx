import {
  Button,
  Card,
  Col,
  Divider,
  Form,
  Input,
  List,
  Row,
  Select,
  Space,
  TimePicker,
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { formatDate } from "../../functions/formatDate";
import { useEffect, useState } from "react";
import axios from "axios";
import config from "../../config/config.json";
import dayjs from "dayjs";

const KeyPointsForFirstWorkingDay: React.FC<CustomFormProps> = (props) => {
  const [apiData, setApiData] = useState<person[]>([]);
  const [availableContacts, setAvailableContacts] = useState<options[]>([]);
  const [newBulletPoint, setNewBulletPoint] = useState<string>("");

  // not included in the form
  const [documents, setDocuments] = useState<string[]>([]);

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

  type person = {
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

  const deleteBulletPoint = (indexToDelete: number) => {
    const updatedDocuments = [...documents];
    updatedDocuments.splice(indexToDelete, 1);
    setDocuments(updatedDocuments);
  };

  const addBulletPoint = () => {
    const updatedDocuments = [...documents];
    updatedDocuments.push(newBulletPoint);
    setDocuments(updatedDocuments);
    setNewBulletPoint("");
  };

  const handleSubmit = (values: any) => {
    // Search for the contact persons Name in the available contacts.
    const contactPersonObject = getNamesByEmail(apiData, values.contactPerson);
    const contact_person =
      contactPersonObject?.first_name + " " + contactPersonObject?.last_name;

    props.finishUserTask({
      // Search for the contact persons Name in the available contacts.
      contact_person: contact_person,
      begin_of_first_working_day:
        values.beginTimeFirstWorkingDay.format("HH:mm"),
      contact_person_mail: values.contactPerson,
      documents_needed_for_first_working_day: documents,
    });
  };

  // Get the first and last name of a person by their email
  const getNamesByEmail = (dataset: person[], email: string) => {
    const person = dataset.find((p) => p.email === email);
    if (person) {
      return {
        first_name: person.first_name,
        last_name: person.last_name,
      };
    }
    return null;
  };

  return (
    <>
      <Divider orientation="center">
        Eckpunkte für ersten Arbeitstag von {nameOfEmployee} definieren
      </Divider>
      <Row gutter={[16, 16]}>
        <Col span={1}></Col>
        <Col span={16}>
          <Card title="Eckpunkte">
            <Form name="keyPoints" onFinish={handleSubmit}>
              <Form.Item label="Ansprechpartner" name="contactPerson">
                <Select options={availableContacts}></Select>
              </Form.Item>
              <Form.Item
                label="Beginn des ersten Arbeitstages"
                name="beginTimeFirstWorkingDay"
              >
                <TimePicker
                  format="HH:mm"
                  defaultOpenValue={dayjs("00:00:00", "HH:mm")}
                  placeholder="hh:mm"
                  style={{ width: "100%" }}
                />
              </Form.Item>
              <Form.Item name="ignoredFormItem" label="Benötigte Dokumente">
                <Space.Compact
                  style={{
                    width: "100%",
                    marginTop: "5px",
                  }}
                >
                  <Input
                    name="newBulletpoint"
                    value={newBulletPoint}
                    onChange={(e) => setNewBulletPoint(e.target.value)}
                  />

                  <Button type="primary" onClick={() => addBulletPoint()}>
                    Hinzufügen
                  </Button>
                </Space.Compact>
              </Form.Item>

              {/* Documents needed for first Working Day */}
              <List
                bordered
                dataSource={documents}
                style={{ overflow: "auto", height: "200px" }}
                renderItem={(item: string, index: number) => (
                  <List.Item
                    extra={
                      <Button
                        size="small"
                        onClick={() => deleteBulletPoint(index)}
                      >
                        Delete
                      </Button>
                    }
                  >
                    <List.Item.Meta title={item} />
                  </List.Item>
                )}
              ></List>
              <Divider />
              <Button block type="primary" htmlType="submit">
                Senden
              </Button>
            </Form>
          </Card>
        </Col>
        <Col span={6}>
          <EmployeeData employeeData={employeeData} />
        </Col>
        <Col span={1}></Col>
      </Row>
      <Col span={24}></Col>
    </>
  );
};
export default KeyPointsForFirstWorkingDay;
