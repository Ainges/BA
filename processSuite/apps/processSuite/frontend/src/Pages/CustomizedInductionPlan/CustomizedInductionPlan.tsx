import {
  Alert,
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
import { useEffect, useState } from "react";

import { SendOutlined } from "@ant-design/icons";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";
import { DecouplerProps } from "../../Interfaces/Decoupler";

export interface IinductionPlanElement {
  title: string;
  description: string | undefined;
}

interface InewEmployeeDataType {
  OnboardingData: {
    first_name: string;
    last_name: string;
    first_working_day: string;
    employment_status: string;
    hours_per_week: string;
  };
}

const CustomizedInductionPlan: React.FC<DecouplerProps> = (props) => {
  const [newEmployeeData, setNewEmployeeData] = useState<EmployeeData[]>([]);
  const [inductionPlan, setInductionPlan] = useState<IinductionPlanElement[]>(
    []
  );
  const [isSendButtonDisabled, setIsSendButtonDisabled] =
    useState<boolean>(false);
  const [isPropsValid, setIsPropsValid] = useState<boolean>(false);

  function isNewEmployeeData(obj: any): obj is InewEmployeeDataType {
    return (
      "OnboardingData" in obj &&
      "first_name" in obj.OnboardingData &&
      "last_name" in obj.OnboardingData &&
      "first_working_day" in obj.OnboardingData &&
      "employment_status" in obj.OnboardingData &&
      "hours_per_week" in obj.OnboardingData
    );
  }

  useEffect(() => {
    const currentToken = props.userTask.startToken;

    if (!isNewEmployeeData(currentToken)) {
      setIsPropsValid(false);
      setIsSendButtonDisabled(true);
      console.error("Invalid Token Data");
      return;
    }

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
    ];
    setIsPropsValid(true);
    setNewEmployeeData(NewEmployeedata);
  }, [props.userTask.startToken]);

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
                renderItem={(item: IinductionPlanElement, index: number) => (
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
          {isPropsValid ? (
            <div id="EmployeeData" className={styles.employeeData}>
              <EmployeeData employeeData={newEmployeeData}></EmployeeData>
            </div>
          ) : (
            <>
              <Alert
                message="Ein Fehler ist aufgetreten!"
                showIcon
                description="Achtung - Es wurden keine gültigen Daten übergeben! Bitte wenden Sie sich an den Administrator!"
                type="error"
              />
              <br />
              <Alert
                message="Weiterführende Informationen"
                showIcon
                description="Der Prozess hat dem Formular keine gültigen Daten übergeben!"
                type="info"
              />
            </>
          )}
        </Col>
        <Col span={20}></Col>

        <Col span={4}>
          <br />
          <br />
          <Button
            type="primary"
            disabled={inductionPlan.length === 0 || isSendButtonDisabled}
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
