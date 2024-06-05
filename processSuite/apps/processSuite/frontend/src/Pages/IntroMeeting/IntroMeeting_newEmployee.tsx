import { Alert, Card, Col, Divider, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";

const IntroMeeting_newEmployee: React.FC<CustomFormProps> = (props) => {
  return (
    <>
      <Divider orientation="center">Kennenlerngespräch</Divider>
      <Alert
        type="info"
        message={
          <>
            {" "}
            <p>
              Sie haben heute um{" "}
              <strong> {props.userTask.startToken.time} Uhr </strong> ein
              Kennenlerngespräch mit{" "}
            </p>{" "}
          </>
        }
      />
      <br />
      <Row>
        <Col span={1}></Col>
        <Col span={22}>
          <Card></Card>
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};
export default IntroMeeting_newEmployee;
