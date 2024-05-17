import React, { useEffect } from "react";
import { CustomFormProps } from "../../DialogRenderer";
import {
  Row,
  Col,
  Card,
  Divider,
  Alert,
  Form,
  Radio,
  Space,
  Button,
  RadioChangeEvent,
} from "antd";
import axios from "axios";
import config from "../../config/config.json";
import style from "./SelectEquipment.module.css";

interface EquipmentData {
  name: string;
  description: string;
}

interface ApiData {
  Laptops: EquipmentData[];
  Smartphones: EquipmentData[];
}

const SelectEquipment: React.FC<CustomFormProps> = (props) => {
  const [laptopForm] = Form.useForm();
  const [smartphoneForm] = Form.useForm();
  const [apiData, setApiData] = React.useState<ApiData | null>(null);
  const [isSubmitDisabled, setIsSubmitDisabled] = React.useState(true);

  const [selectedLaptop, setSelectedLaptop] = React.useState<string | null>(
    null
  );
  const [selectedSmartphone, setSelectedSmartphone] = React.useState<
    string | null
  >(null);

  async function getApiData() {
    try {
      const response = await axios.get(
        config.camel.host + "/onboarding/preonboarding/AvailableEquipment"
      );
      console.log("Success");
      console.log(response.data);
      setApiData(response.data);
    } catch (error) {
      console.error("Error");
      console.error(error);
    }
  }

  useEffect(() => {
    getApiData();
  }, []);

  useEffect(() => {
    if (selectedLaptop && selectedSmartphone) {
      setIsSubmitDisabled(false);
    } else {
      setIsSubmitDisabled(true);
    }
  }, [selectedLaptop, selectedSmartphone]);

  const handleSubmit = () => {
    console.log("Selected Laptop: ", selectedLaptop);
    console.log("Selected Smartphone: ", selectedSmartphone);

    props.finishUserTask({
      selectedLaptop: selectedLaptop,
      selectedSmartphone: selectedSmartphone,
    });
  };

  return (
    <>
      <Row gutter={[16, 16]}>
        <Divider orientation="center">Equipment Auswahl</Divider>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Alert
              banner
              message="Hier können Sie sich einen Arbeitslaptop und ein Firmensmartphone auswählen"
              type="info"
            />
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
      <br />
      {/* Row 2 */}
      <Row gutter={[16, 16]}>
        <Col span={1}></Col>

        <Col span={11}>
          <Card>
            <Form.Item label="Laptops" required>
              <Radio.Group
                key="laptop"
                onChange={(e: RadioChangeEvent) => {
                  setSelectedLaptop(e.target.value);
                }}
                value={selectedLaptop}
              >
                <Space direction="vertical">
                  {apiData?.Laptops.map((laptop, index) => (
                    <Radio key={laptop.name} value={laptop.name}>
                      {laptop.name}
                    </Radio>
                  ))}
                </Space>
              </Radio.Group>
            </Form.Item>
          </Card>
        </Col>
        <Col span={11}>
          <Card>
            <Form.Item label="Smartphones" required>
              <Radio.Group
                key="smartphone"
                onChange={(e: RadioChangeEvent) => {
                  setSelectedSmartphone(e.target.value);
                }}
                value={selectedSmartphone}
              >
                <Space direction="vertical">
                  {apiData?.Smartphones.map((smartphone, index) => (
                    <Radio key={smartphone.name} value={smartphone.name}>
                      {smartphone.name}
                    </Radio>
                  ))}
                </Space>
              </Radio.Group>
            </Form.Item>
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
      <br />
      {/* Row 3 */}
      <Row gutter={[16, 16]}>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Space style={{ width: 100 }}>
              <Button
                type="primary"
                onClick={handleSubmit}
                disabled={isSubmitDisabled}
              >
                Senden
              </Button>
            </Space>
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};

export default SelectEquipment;
