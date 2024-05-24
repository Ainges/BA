import { Button, Card, Col, Divider, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import Meta from "antd/es/card/Meta";

const GetInductionPlanDone: React.FC<CustomFormProps> = (props) => {
  return (
    <>
      <Row>
        <Divider orientation="center">
          Punkt aus Einarbeitungsplan abarbeiten
        </Divider>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Meta
              title={props.userTask.startToken.title}
              description={props.userTask.startToken.description}
            />
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
      <Row style={{ marginTop: "20px" }}>
        <Col span={1}></Col>
        <Col span={22}>
          <Button
            type="primary"
            block
            onClick={() => {
              props.finishUserTask({
                done: true,
              });
            }}
          >
            Erledigt
          </Button>
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};

export default GetInductionPlanDone;
