import {
  Button,
  Card,
  Col,
  Divider,
  Flex,
  Form,
  Rate,
  Row,
  Tooltip,
} from "antd";
import TextArea from "antd/es/input/TextArea";
import { FrownOutlined, MehOutlined, SmileOutlined } from "@ant-design/icons";
import { useState } from "react";
import { DecouplerProps } from "../../Interfaces/Decoupler";

const FirstPerformanceReview_Employee: React.FC<DecouplerProps> = (props) => {
  const [form] = Form.useForm();
  const [isFormValid, setIsFormValid] = useState(false);

  const customIcons: Record<number, React.ReactNode> = {
    1: <FrownOutlined />,
    2: <FrownOutlined />,
    3: <MehOutlined />,
    4: <SmileOutlined />,
    5: <SmileOutlined />,
  };

  const onFinish = (values: any) => {
    console.log("Received values of form: ", values);
    props.finishUserTask({
      currentTasks: values.currentTasks,
      satisfaction: values.satisfaction,
      suggestions: values.suggestions,
    });
  };
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
          Erstes Mitarbeitergespräch - Sicht Mitarbeiter
        </Divider>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Form
              form={form}
              layout="vertical"
              onFinish={onFinish}
              onValuesChange={onValuesChange}
              requiredMark="optional"
            >
              <Form.Item
                label="Aktuelle Aufgaben"
                name="currentTasks"
                rules={[
                  {
                    required: true,
                    message: "Bitte geben Sie hier Ihre aktuellen Aufgaben ein",
                  },
                ]}
              >
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Welche Aufgaben haben Sie aktuell?"
                />
              </Form.Item>
              <Form.Item
                label="Wie Zufrieden sind Sie aktuell mit ihren Aufgaben?"
                name="satisfaction"
                rules={[
                  {
                    required: true,
                    message: "Bitte geben Sie hier Ihre Zufriedenheit an",
                  },
                ]}
              >
                <Rate
                  defaultValue={0}
                  character={({ index = 0 }) => customIcons[index + 1]}
                />
              </Form.Item>
              <Form.Item
                label="Verbesserungsvorschläge / Wünsche"
                name="suggestions"
                rules={[
                  {
                    required: true,
                    message:
                      "Bitte geben Sie hier Verbesserungsvorschläge oder Wünsche ein",
                  },
                ]}
              >
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Was könnte besser gemacht werden?"
                />
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
export default FirstPerformanceReview_Employee;
