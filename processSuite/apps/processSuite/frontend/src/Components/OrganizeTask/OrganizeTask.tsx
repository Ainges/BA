import { Row, Divider, Col, Card, Button, Space, Checkbox } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import styles from "./OrganizeTask.module.css";
import { EmployeeData } from "../EmployeeData/EmployeeData";
import { useEffect, useState } from "react";
import { title } from "process";
import { format } from "path";
import { formatDate } from "../../functions/formatDate";
import Paragraph from "antd/es/typography/Paragraph";
import { InewEmployeeDataType } from "../../Interfaces/InewEmployeeData";

function isNewEmployeeData(obj: any): obj is InewEmployeeDataType {
  return (
    "OnboardingData" in obj &&
    "first_name" in obj.OnboardingData &&
    "last_name" in obj.OnboardingData &&
    "first_working_day" in obj.OnboardingData &&
    "birth_date" in obj.OnboardingData &&
    "position" in obj.OnboardingData
  );
}

interface additionalData {
  title: string;
  data: string;
}

interface IcomponentProps {
  processData: CustomFormProps;
  title: string;
  task: JSX.Element;
  checkboxText: string;
  resultString: string;
  additionalData: additionalData[];
}

const OrganizeTask: React.FC<IcomponentProps> = (props) => {
  const [newEmployeeData, setNewEmployeeData] = useState<EmployeeData[]>([]);
  const [checkboxChecked, setCheckboxChecked] = useState<boolean>(false);
  const currentToken = props.processData.userTask.startToken;

  useEffect(() => {
    const currentToken = props.processData.userTask.startToken;

    if (!isNewEmployeeData(currentToken)) {
      console.error("Invalid ProcessData!");
      return;
    }

    try {
      const extractedData = [
        {
          title: "Name",
          data:
            currentToken.OnboardingData.first_name + " " +
            currentToken.OnboardingData.last_name,
        },
        {
          title: "Geburtstag",
          data: formatDate(currentToken.OnboardingData.birth_date),
        },
        {
          title: "Erster Arbeitstag",
          data: formatDate(currentToken.OnboardingData.first_working_day),
        },
        {
          title: "Position",
          data: currentToken.OnboardingData.position,
        },
      ];

      props.additionalData.forEach((data) => {
        extractedData.push({
          title: data.title,
          data: data.data,
        });
      });

      setNewEmployeeData(extractedData);
    } catch (error) {
      console.error("Error while extracting data: " + error);
    }
  }, []);
  return (
    <>
      <Row>
        <Divider orientation="center">
          {/* Kuchen für Mitarbeiter organisieren */}
          {props.title}
        </Divider>

        <Col span={18} className={styles.column}>
          <Card title="Aufgabe">
            {/* <Paragraph>
              Der neue Mitarbeiter {currentToken.OnboardingData.first_name}{" "}
              {currentToken.OnboardingData.last_name} hat am{" "}
              {formatDate(currentToken.OnboardingData.first_working_day)} seinen
              ersten Arbeitstag.
            </Paragraph>
            <Paragraph>
              Gleichzeitig hat er am selben Tag Geburtstag. Bitte organisieren
              Sie einen Kuchen für den neuen Mitarbeiter!
            </Paragraph> */}

            {props.task}
            <Checkbox
              checked={checkboxChecked}
              onClick={() => {
                setCheckboxChecked(!checkboxChecked);
              }}
            >
              {/* Kuchen ist auf den{" "}
              {formatDate(currentToken.OnboardingData.first_working_day)}{" "}
              bestellt! */}
              {props.checkboxText}
            </Checkbox>
          </Card>
        </Col>
        <Col span={6} className={styles.column}>
          <EmployeeData employeeData={newEmployeeData} />
        </Col>
      </Row>
      <Row>
        <Col span={24}>
          <Space style={{ width: "100%", justifyContent: "center" }}>
            <Button
              type="primary"
              disabled={!checkboxChecked}
              onClick={() => {
                props.processData.finishUserTask({
                  result: props.resultString,
                });
              }}
            >
              Abschließen
            </Button>
            <Button
              type="default"
              onClick={() => {
                props.processData.suspendUserTask(
                  props.processData.suspendState
                );
              }}
            >
              Zurückstellen
            </Button>
          </Space>
        </Col>
      </Row>
    </>
  );
};
export default OrganizeTask;
