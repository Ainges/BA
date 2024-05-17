import React, { useEffect } from "react";
import { CustomFormProps } from "../../DialogRenderer";
import { Button, Card, Checkbox, Col, Divider, Row, Space } from "antd";

const PrepareEquipment: React.FC<CustomFormProps> = (props) => {
  const [isLaptopProcured, setIsLaptopProcured] = React.useState(false);
  const [isSmartphoneProcured, setIsSmartphoneProcured] = React.useState(false);
  const [isLaptopPrepared, setIsLaptopPrepared] = React.useState(false);
  const [isSmartphonePrepared, setIsSmartphonePrepared] = React.useState(false);

  const [isSubmitDisabled, setIsSubmitDisabled] = React.useState(true);

  const onProcureChange = (checkedValues: any) => {
    console.log("checked = ", checkedValues);

    checkedValues.includes("LaptopProcured")
      ? setIsLaptopProcured(true)
      : setIsLaptopProcured(false);
    checkedValues.includes("SmartphoneProcured")
      ? setIsSmartphoneProcured(true)
      : setIsSmartphoneProcured(false);
  };

  const onPrepareChange = (checkedValues: any) => {
    checkedValues.includes("LaptopPrepared")
      ? setIsLaptopPrepared(true)
      : setIsLaptopPrepared(false);
    checkedValues.includes("SmartphonePrepared")
      ? setIsSmartphonePrepared(true)
      : setIsSmartphonePrepared(false);
  };

  useEffect(() => {
    if (
      isLaptopProcured &&
      isSmartphoneProcured &&
      isLaptopPrepared &&
      isSmartphonePrepared
    ) {
      setIsSubmitDisabled(false);
    } else {
      setIsSubmitDisabled(true);
    }
  }, [
    isLaptopPrepared,
    isLaptopProcured,
    isSmartphonePrepared,
    isSmartphoneProcured,
  ]);

  return (
    // JSX code for your component's UI goes here
    <>
      <Divider orientation="center">Equipment bestellen & Vorbereiten </Divider>
      <Row>
        <Col span={1}></Col>
        <Col span={11} style={{ margin: 5 }}>
          <Card title="Ausgewähltes Equipment">
            <Space direction="vertical" style={{ display: "flex" }}>
              <Card title="Laptop:">
                {props.userTask.startToken.selectedLaptop}
              </Card>
              <Card title="Smartphone">
                {props.userTask.startToken.selectedSmartphone}
              </Card>
            </Space>
          </Card>
        </Col>
        <Col span={11} style={{ margin: 5 }}>
          <Card title="Aufgaben">
            <Space direction="vertical" style={{ display: "flex" }}>
              <Card title="Equipment bestellt:">
                <Checkbox.Group
                  style={{ width: "100%" }}
                  onChange={onProcureChange}
                >
                  <Checkbox value="LaptopProcured">
                    {props.userTask.startToken.selectedLaptop}
                  </Checkbox>
                  <Checkbox value="SmartphoneProcured">
                    {props.userTask.startToken.selectedSmartphone}
                  </Checkbox>
                </Checkbox.Group>
              </Card>
              <Card title="Equipment vorbereitet">
                <Checkbox.Group
                  style={{ width: "100%" }}
                  onChange={onPrepareChange}
                >
                  <Checkbox value="LaptopPrepared">
                    {props.userTask.startToken.selectedLaptop}
                  </Checkbox>
                  <Checkbox value="SmartphonePrepared">
                    {props.userTask.startToken.selectedSmartphone}
                  </Checkbox>
                </Checkbox.Group>
              </Card>
            </Space>
          </Card>
        </Col>
        <Col span={1}></Col>
        <Divider />
      </Row>
      <Row gutter={[16, 16]}>
        <Col span={4}></Col>
        <Col span={8}>
          <Button
            block
            onClick={() => {
              props.suspendUserTask({
                isLaptopProcured,
                isSmartphoneProcured,
                isLaptopPrepared,
                isSmartphonePrepared,
              });
            }}
          >
            Status speichern (🚧 Work in Progress)
          </Button>
        </Col>
        <Col span={8}>
          <Button
            type="primary"
            block
            disabled={isSubmitDisabled}
            onClick={() => {
              props.finishUserTask({
                prepareEquipment: "Done",
              });
            }}
          >
            Senden
          </Button>
        </Col>
        <Col span={4}></Col>
      </Row>
    </>
  );
};

export default PrepareEquipment;
