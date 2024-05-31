import { Button, Card, Col, Divider, Flex, Form, Input, Rate, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import TextArea from "antd/es/input/TextArea";
import { FrownOutlined, MehOutlined, SmileOutlined } from "@ant-design/icons";

const FirstPerformanceReview_Employee: React.FC<CustomFormProps> = (props) => {
  const [form] = Form.useForm();

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

  return (
    <>
      <Row>
        <Divider orientation="center">
          Erstes Mitarbeitergespräch - Sicht Mitarbeiter
        </Divider>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Form form={form} layout="vertical" onFinish={onFinish}>
              <Form.Item label="Aktuelle Aufgaben" name="currentTasks">
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Welche Aufgaben haben Sie aktuell?"
                />
              </Form.Item>
              <Form.Item
                label="Wie Zufrieden sind Sie aktuell mit ihren Aufgaben?"
                name="satisfaction"
              >
                <Rate
                  defaultValue={3}
                  character={({ index = 0 }) => customIcons[index + 1]}
                />
              </Form.Item>
              <Form.Item
                label="Verbesserungsvorschläge / Wünsche"
                name="suggestions"
              >
                <TextArea
                  maxLength={2000}
                  autoSize={{ minRows: 5, maxRows: 5 }}
                  placeholder="Was könnte besser gemacht werden?"
                />
              </Form.Item>

              <Flex justify="center" align="center">
                <Button
                  type="primary"
                  style={{ width: "200px" }}
                  htmlType="submit"
                >
                  Senden
                </Button>
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
