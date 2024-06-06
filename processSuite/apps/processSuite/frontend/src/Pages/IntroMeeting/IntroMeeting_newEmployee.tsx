import { Alert, Button, Col, Divider, Row } from "antd";
import { useEffect, useState } from "react";
import axios from "axios";
import { DecouplerProps } from "../../Interfaces/Decoupler";

interface IEmployee {
  email: string;
  first_name: string;
  last_name: string;
  position: string;
  profile_picture_url: string;
}

const IntroMeeting_newEmployee: React.FC<DecouplerProps> = (props) => {
  const [employee, setEmployee] = useState<IEmployee>();

  useEffect(() => {
    axios
      .get(
        "http://localhost:8080/api/canonical/user/byMail/" +
          props.userTask.startToken.email
      )
      .then((response) => {
        setEmployee(response.data);
        console.log(response.data);
      });
  }, []);

  return (
    <>
      <Divider orientation="center">
        Kennenlerngespräch mit Mitarbeiter führen
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
                    {employee?.first_name} {employee?.last_name}
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
export default IntroMeeting_newEmployee;
