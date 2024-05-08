import {
  Button,
  Card,
  Col,
  Descriptions,
  Divider,
  Form,
  FormProps,
  Input,
  List,
  Row,
  Space,
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import styles from "./CustomizedInductionPlan.module.css";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { describe } from "node:test";
import TextArea from "antd/es/input/TextArea";
import { useState } from "react";

import { SendOutlined } from "@ant-design/icons";

interface inductionPlanElement {
  title: string;
  description: string | undefined;
}

const CustomizedInductionPlan: React.FC<CustomFormProps> = (props) => {
  const currentToken = props.userTask.startToken;

  const [inductionPlan, setInductionPlan] = useState<inductionPlanElement[]>(
    []
  );

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
      // 5head ChatGPT expression to convert date format
      data: new Date(
        `${currentToken.OnboardingData.first_working_day
          .split("-")
          .reverse()
          .join("-")}T00:00:00`
      )
        .toLocaleDateString("de-DE", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
        })
        .toString(),
    },
    {
      title: "Anstellungsart",
      data: currentToken.OnboardingData.employment_status,
    },
    {
      title: "Vereinbarte Arbeitszeit pro Woche",
      data: currentToken.OnboardingData.hours_per_week,
    },
  ];

  type FieldType = {
    title: string;
    description?: string;
  };

  const addBulletPoint: FormProps<FieldType>["onFinish"] = (values) => {
    console.log(values);
    setInductionPlan([
      ...inductionPlan,
      {
        title: values.title,
        description: values.description,
      },
    ]);
  };

  const deleteBulletPoint = (indexToDelete: number) => {
    const updatedPlan = [...inductionPlan];
    updatedPlan.splice(indexToDelete, 1);
    setInductionPlan(updatedPlan);
  };

  return (
    <>
      <Row>
        <Divider orientation="center">Einarbeitungsplan Erstellung</Divider>

        <Col span={18} className={styles.column}>
          <div className={styles.test}>
            <Card title="Einarbeitungsplan">
              {/* Add Points: */}
              <Form name="addBulletPoints" onFinish={addBulletPoint}>
                <Form.Item<FieldType>
                  name="title"
                  rules={[
                    {
                      required: true,
                      message: "Bitte geben Sie einen Title an!",
                    },
                  ]}
                >
                  <Space.Compact
                    style={{
                      width: "100%",
                      marginTop: "5px",
                      // marginBottom: "5px",
                    }}
                  >
                    <Input />

                    <Button type="primary" htmlType="submit">
                      Hinzufügen
                    </Button>
                  </Space.Compact>
                </Form.Item>
                <Form.Item<FieldType> name="description">
                  <Space.Compact style={{ width: "100%", marginBottom: "5px" }}>
                    <TextArea
                      rows={2}
                      placeholder="Beschreibung des Punktes... max 200 Zeichen"
                      maxLength={200}
                      style={{ resize: "none" }}
                    />
                  </Space.Compact>
                </Form.Item>
              </Form>
              <Divider />
              <List
                bordered
                dataSource={inductionPlan}
                style={{ overflow: "auto", height: "300px" }}
                renderItem={(item: inductionPlanElement, index: number) => (
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
                    <List.Item.Meta
                      title={item.title}
                      description={item.description}
                    />
                  </List.Item>
                )}
              ></List>
            </Card>
          </div>
        </Col>
        <Col span={6} className={styles.column}>
          <div id="EmployeeData" className={styles.employeeData}>
            <EmployeeData employeeData={NewEmployeedata}></EmployeeData>
          </div>
        </Col>
        <Col span={20}></Col>

        <Col span={4}>
          <br />
          <br />
          <Button
            type="primary"
            onClick={() => {
              console.log(inductionPlan);
              props.finishUserTask({ inductionPlan: inductionPlan });
            }}
            icon={<SendOutlined />}
          >
            Senden
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default CustomizedInductionPlan;
