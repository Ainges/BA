import { Card, Col, Divider, List, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import styles from "./CustomizedInductionPlan.module.css";
import { title } from "process";
const CustomizedInductionPlan: React.FC<CustomFormProps> = (props) => {
  const currentToken = props.userTask.startToken;

  const NewEmployeedata = [
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
      ).toLocaleDateString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }),
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

  return (
    <>
      <Row>
        <Divider orientation="center">Einarbeitungsplan Erstellung</Divider>
        <Col span={18} className={styles.column}>
          <div className={styles.test}>
            <Card title="Einarbeitungsplan"> </Card>
          </div>
        </Col>
        <Col span={6} className={styles.column}>
          <div id="EmployeeData" className={styles.employeeData}>
            <Card title="Daten zu neuem Mitarbeiter">
              <List
                bordered
                dataSource={NewEmployeedata}
                renderItem={(item, index) => (
                  <List.Item>
                    <List.Item.Meta
                      title={item.title}
                      description={item.data}
                    />
                  </List.Item>
                )}
              ></List>
            </Card>
          </div>
        </Col>
      </Row>
    </>
  );
};

export default CustomizedInductionPlan;
