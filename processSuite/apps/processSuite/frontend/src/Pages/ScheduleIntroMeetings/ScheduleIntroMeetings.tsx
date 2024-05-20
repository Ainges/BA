import { Divider, Row, Col, Card, Table, Flex, Button, Tooltip } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { useState } from "react";
import styles from "./ScheduleIntroMeetings.module.css";
import { SendOutlined } from "@ant-design/icons";

const ScheduleIntroMeetings: React.FC<CustomFormProps> = (props) => {
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const [selectedEmployeesWithDateTime, setSelectedEmployeesWithDateTime] =
    useState([]);
    const [selectedDateTime, setSelectedDateTime] = useState<{
        [key: string]: Date | null;
      }>({});

  const sendSelectionToProcess = () => {};


  
  return (
    <>
      <div id="mycontainer" className={styles.container}>
        <Divider orientation="center">Kennenlerngespräche terminieren</Divider>
        <Row>
          <Col span={1}></Col>
          <Col span={22}>
            <Card title="Ausewählte Mitarbeiter:">
              <Table
                pagination={{ position: [] }}
                scroll={{ y: 300 }}
                columns={columns}
                dataSource={selectedEmployeesWithDateTime}
                size="small"
              />
            </Card>

            <Flex justify="space-evenly" align="center">
              <Button
                type="primary"
                icon={<SendOutlined />}
                size="large"
                onClick={() => {
                  sendSelectionToProcess();
                }}
                className={styles.button}
                disabled={isButtonDisabled}
              >
                Senden
              </Button>
            </Flex>
          </Col>
          <Col span={1}></Col>
        </Row>
      </div>
    </>
  );
};
export default ScheduleIntroMeetings;
