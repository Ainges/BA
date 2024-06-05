import { CustomFormProps } from "../../DialogRenderer";
import { Alert, Button, Card, Col, Divider, Row } from "antd";


const IntroMeeting_selectedEmployee: React.FC<CustomFormProps> = (props) => {
  return (
    <>
      <Divider orientation="center">
        Kennenlerngespräch mit neuem Mitarbeiter führen
      </Divider>
      <Row>
        {" "}
        <Col span={6}></Col>
        <Col span={12}>
          <Alert
            type="info"
            showIcon
            message="Info"
            description={
              <>
                {" "}
                <p>
                  Sie haben heute um{" "}
                  <strong> {props.userTask.startToken.time} Uhr </strong> ein
                  Kennenlerngespräch mit{" "}
                  <strong>
                    {" "}
                    {props.userTask.startToken.onboardingData?.first_name}{" "}
                    {props.userTask.startToken.onboardingData?.last_name}
                  </strong>
                  .
                </p>{" "}
              </>
            }
          />
        </Col>
        <Col span={6}></Col>
      </Row>
      <Divider />
      <Row>
        <Col span={6}></Col>
        <Col span={12}>
          <Button
            type="primary"
            onClick={() => {
              props.finishUserTask({
                done: true,
              });
            }}
            block
          >
            Erledigt
          </Button>
        </Col>
        <Col span={6}></Col>
      </Row>
    </>
  );
};
export default IntroMeeting_selectedEmployee;
