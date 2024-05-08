import { Row, Divider, Col, Card, Button, Space, Checkbox } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import styles from "./OrganizeCake.module.css";
import { EmployeeData } from "../../Components/EmployeeData/EmployeeData";
import { useEffect, useState } from "react";
import { title } from "process";
import { format } from "path";
import { formatDate } from "../../functions/formatDate";
import Paragraph from "antd/es/typography/Paragraph";

interface InewEmployeeDataType {
  OnboardingData: {
    first_name: string;
    last_name: string;
    first_working_day: string;
    birth_date: string;
    position: string;
  };
}

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

const OrganizeCake: React.FC<CustomFormProps> = (props) => {
  const [newEmployeeData, setNewEmployeeData] = useState<EmployeeData[]>([]);
  const [checkboxChecked, setCheckboxChecked] = useState<boolean>(false);
  const currentToken = props.userTask.startToken;

  useEffect(() => {
    const currentToken = props.userTask.startToken;

    if (!isNewEmployeeData(currentToken)) {
      console.error("Invalid ProcessData!");
      return;
    }

    try {
      const extractedData = [
        {
          title: "Name",
          data:
            currentToken.OnboardingData.first_name +
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

      setNewEmployeeData(extractedData);
    } catch (error) {
      console.error("Error while extracting data: " + error);
    }
  }, []);
  return (
    <>
      <Row>
        <Divider orientation="center">
          Kuchen für Mitarbeiter organisieren
        </Divider>

        <Col span={18} className={styles.column}>
          <Card title="Aufgabe">
            <Paragraph>
              Der neue Mitarbeiter {currentToken.OnboardingData.first_name}{" "}
              {currentToken.OnboardingData.last_name} hat am{" "}
              {formatDate(currentToken.OnboardingData.first_working_day)} seinen
              ersten Arbeitstag.
            </Paragraph>
            <Paragraph>
              Gleichzeitig hat er am selben Tag Geburtstag. Bitte organisieren
              Sie einen Kuchen für den neuen Mitarbeiter!
            </Paragraph>
            <Checkbox
              checked={checkboxChecked}
              onClick={() => {
                setCheckboxChecked(!checkboxChecked);
              }}
            >
              Kuchen ist auf den{" "}
              {formatDate(currentToken.OnboardingData.first_working_day)}{" "}
              bestellt!
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
                props.finishUserTask({ result: "Cake organized" });
              }}
            >
              Abschließen
            </Button>
            <Button
              type="default"
              onClick={() => {
                props.suspendUserTask(props.suspendState);
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
export default OrganizeCake;