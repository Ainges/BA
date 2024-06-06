import {
  Alert,
  Button,
  Card,
  Col,
  DatePicker,
  Divider,
  Flex,
  Row, TimePicker
} from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import { useState } from "react";
import moment from "moment";
import formatDateToYYYYMMDD from "../../functions/formatDateToYYYYMMDD";
import formatStringDDMMYYYYtoDate from "../../functions/formatStringDDMMYYYYtoDate";

const ScheduleFirstPerformanceReview: React.FC<CustomFormProps> = (props) => {
  const token = props.userTask.startToken;

  // const employeeData = token.employeeData;
  const onboardingData = token.OnboardingData;
  const name = onboardingData.first_name + " " + onboardingData.last_name;

  const [date, setDate] = useState<string>("");
  const [time, setTime] = useState<string>("");

  return (
    <>
      <Divider orientation="center">
        Zeitpunkt für erstes Mitarbeitergespräch festlegen
      </Divider>
      <Row>
        <Col span={1}></Col>
        <Col span={22}>
          <Card>
            <Flex gap="middle" align="center" justify="center">
              <Alert
                style={{ width: "100%" }}
                message={`Bitte wählen Sie ein Datum und eine Uhrzeit für das erste Mitarbeitergespräch mit ${name} aus.`}
                type="info"
                showIcon
              ></Alert>
            </Flex>
            <br />
            <Flex gap="middle" justify="center">
              <DatePicker
                placeholder="Datum"
                format="DD.MM.YYYY"
                disabledDate={(current) => {
                  let customDate = moment().format("YYYY-MM-DD");
                  return current && current < moment(customDate, "YYYY-MM-DD");
                }}
                onChange={(date, dateString) => {
                  console.log("Datum");
                  console.log(dateString);

                  if (typeof dateString === "string" && dateString.length > 0) {
                    console.log("Date found! " + dateString);

                    setDate(dateString);
                  } else {
                    console.log("No date found!");
                    setDate("");
                  }
                }}
              ></DatePicker>
              <TimePicker
                placeholder="Uhrzeit"
                format="HH:mm"
                onChange={(time, timeString) => {
                  console.log("Uhrzeit");
                  console.log(timeString);

                  if (typeof timeString === "string" && timeString.length > 0) {
                    console.log("Time found! " + timeString);

                    setTime(timeString);
                  } else {
                    console.log("No time found!");
                    setTime("");
                  }
                }}
              ></TimePicker>
            </Flex>
            <Divider></Divider>
            <Flex justify="center">
              <Button
                type="primary"
                onClick={() => {
                  //TODO: Format to YYYY-MM-DD HH:mm

                  const dateObject: Date = formatStringDDMMYYYYtoDate(date);

                  console.log("Date: " + date);
                  console.log("Time: " + time);
                  console.log("Date Object: " + dateObject);
                  console.log(
                    "Date Object formatted: " + formatDateToYYYYMMDD(dateObject)
                  );

                  props.finishUserTask({
                    date: formatDateToYYYYMMDD(dateObject),
                    time: time,
                  });
                }}
                disabled={date.length === 0 || time.length === 0}
              >
                Speichern
              </Button>
            </Flex>
          </Card>
        </Col>
        <Col span={1}></Col>
      </Row>
    </>
  );
};
export default ScheduleFirstPerformanceReview;
