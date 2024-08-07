import {
  Row,
  Divider,
  Col,
  Card,
  Form, Flex,
  Button,
  Alert, Tooltip
} from "antd";
import TextArea from "antd/es/input/TextArea";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";
import { useState } from "react";
import { DecouplerProps } from "../../Interfaces/Decoupler";

const FirstPerformanceReview_Supervisor: React.FC<DecouplerProps> = (props) => {
  const [form] = Form.useForm();
  const [isFormValid, setIsFormValid] = useState(false);

  const onFinish = (values: any) => {
    console.log("Received values of form: ", values);

    props.finishUserTask({
      goals: values.goals,
      trainings: values.trainings,
    });
  };

  const name =
    props.userTask.startToken.OnboardingData.first_name +
    " " +
    props.userTask.startToken.OnboardingData.last_name;

  const firstWorkingDay =
    props.userTask.startToken.OnboardingData.first_working_day;

  // ChatGPT function
  const onValuesChange = () => {
    const values = form.getFieldsValue();
    const hasErrors = Object.keys(values).some((field) => !values[field]);
    setIsFormValid(!hasErrors);
  };
  return (
    <>
      <Row>
        <Divider orientation="center">
          Erstes Mitarbeitergespräch - Sicht Vorgesetzer
        </Divider>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Alert
              message={
                <>
                  {" "}
                  <p>
                    Der Mitarbeiter <strong> {name}</strong> hatte am{" "}
                    <strong>{formatDateToDEformat(firstWorkingDay)}</strong>{" "}
                    seinen ersten Arbeitstag. Das erste Mitarbeitergespräch dazu
                    ist nun fällig. Bitte füllen Sie zusammen mit dem
                    Mitarbeiter das Formular aus. Der Mitarbeiter hat eine
                    eigene Aufgabe erhalten, um seinen bisherigen Arbeitsalltag
                    zu bewerten.
                  </p>
                </>
              }
              type="info"
              showIcon
            />
            <Divider />
            <Form
              form={form}
              layout="vertical"
              onFinish={onFinish}
              onValuesChange={onValuesChange}
              requiredMark="optional"
            >
              <Form.Item
                label="Ziele für die Zukunft"
                name="goals"
                rules={[
                  {
                    required: true,
                    message:
                      "Es müssen Ziele für die Zukunft definiert werden.",
                  },
                ]}
              >
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Was soll erreicht werden? max. 2000 Zeichen..."
                ></TextArea>
              </Form.Item>
              <Form.Item
                label="Schulungen / Mitarbeiterentwicklung"
                name="trainings"
                rules={[
                  {
                    required: true,
                    message:
                      "Es muss eine Art von Mitarbeiterentwicklungsprogramm definiert werden.",
                  },
                ]}
              >
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Welche Schulungen sollen in Zukunft absolviert werden / Wohin möchte sich der Mitarbeiter hin entwickeln? max. 2000 Zeichen..."
                ></TextArea>
              </Form.Item>
              <Flex justify="center" align="center">
                <Tooltip
                  title={isFormValid ? "" : "Bitte füllen Sie alle Felder aus"}
                >
                  <Button
                    type="primary"
                    style={{ width: "200px" }}
                    htmlType="submit"
                    disabled={!isFormValid}
                  >
                    Senden
                  </Button>
                </Tooltip>
              </Flex>
            </Form>
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};
export default FirstPerformanceReview_Supervisor;
