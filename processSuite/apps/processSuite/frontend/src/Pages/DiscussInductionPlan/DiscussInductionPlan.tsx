import {
  Alert,
  Button,
  Card,
  Col,
  Divider,
  Flex,
  Row, Table
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { IinductionPlanElement } from "../CustomizedInductionPlan/CustomizedInductionPlan";
import { useEffect, useState } from "react";

const DiscussInductionPlan: React.FC<CustomFormProps> = (props) => {
  const [inductionPlan, setInductionPlan] = useState<IinductionPlanElement[]>(
    []
  );

  // set induction plan from props
  useEffect(() => {
    console.log("DiscussInductionPlan props:");
    console.log(props.userTask.startToken.inductionPlan);

    setInductionPlan(props.userTask.startToken.inductionPlan);
  }, []);

  const name =
    props.userTask.startToken.OnboardingData.first_name +
    " " +
    props.userTask.startToken.OnboardingData.last_name;

  return (
    <>
      <Row>
        <Col span={1}></Col>
        <Col span={22}>
          <Divider orientation="center">Einarbeitungsplan besprechen </Divider>
          <Card style={{ overflow: "auto" }}>
            <Flex gap="middle" align="center" justify="center">
              <Alert
                style={{ width: "100%" }}
                message={
                  <>
                    <span>
                      Bitte besprechen Sie den Einarbeitungsplan mit dem neuen
                      Mitarbeiter <strong>{name}</strong>.

                      Die einzelnen Aufgaben liegen dem Mitarbeiter nach den
                      wesentlichen Aufgaben des ersten Arbeitstages zur
                      Abarbeitung vor.
                    </span>
                  </>
                }
                type="info"
                showIcon
              ></Alert>
            </Flex>
            <br />

            <Card title="Einarbeitungsplan">
              <Table
                columns={[
                  {
                    title: "Aufgabe",
                    dataIndex: "title",
                    key: "title",
                  },
                  {
                    title: "Beschreibung",
                    dataIndex: "description",
                    key: "description",
                  },
                ]}
                dataSource={inductionPlan}
                pagination={false}
                scroll={{ y: 250 }}
              ></Table>
            </Card>
            <br />
            <Button
              type="primary"
              block
              onClick={() => {
                console.log("Induction Plan:");
                console.log(inductionPlan);

                props.finishUserTask({
                  DiscussInductionPlan: "done",
                });
              }}
            >
              Erledigt
            </Button>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default DiscussInductionPlan;
