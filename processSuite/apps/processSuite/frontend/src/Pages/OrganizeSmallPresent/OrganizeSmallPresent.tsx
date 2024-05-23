import { Alert, Card, Col, Divider, Row } from "antd";
import { CustomFormProps } from "../../DialogRenderer";
import styles from "./OrganizeSmallPresent.module.css";
import OrganizeTask from "../../Components/OrganizeTask/OrganizeTask";
import { UserTaskResult } from "@5minds/processcube_engine_sdk";
import { FormState } from "@atlas-engine-contrib/atlas-ui_sdk";
import Paragraph from "antd/es/typography/Paragraph";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";

const OrganizeSmallPresent: React.FC<CustomFormProps> = (props) => {
  const currentToken = props.userTask.startToken;

  const additionalData = [
    { title: "Adresse", data: currentToken.OnboardingData.postal_address },
  ];
  return (
    <>
      <OrganizeTask
        processData={props}
        title={`Eine kleine Aufmerksamkeit für ${currentToken.OnboardingData.first_name} ${currentToken.OnboardingData.last_name} organisieren`}
        task={
          <>
            <Paragraph>
              Der neue Mitarbeiter {currentToken.OnboardingData.first_name}{" "}
              {currentToken.OnboardingData.last_name} hat am{" "}
              {formatDateToDEformat(
                currentToken.OnboardingData.first_working_day
              )}{" "}
              seinen ersten Arbeitstag.
            </Paragraph>
            <Paragraph>
              Er hat noch vor seinem ersten Arbeitstag Geburtstag. Bitte
              organisieren Sie eine kleine Aufmerksamkeit für den neuen
              Mitarbeiter!
            </Paragraph>
            <Alert
              type="warning"
              banner
              message="Achten Sie bitte darauf, dass das Geschenk nicht zu früh zugestellt wird!"
            ></Alert>
            <Alert
              type="warning"
              banner
              message="Achten Sie bitte außerdem darauf, dass der Wert den Betrag von 10€ nicht übersteigt!"
            ></Alert>
            <br></br>
          </>
        }
        checkboxText={`Es wurde eine kleine Aufmerksamkeit and den neuen Mitarbeiter geschickt!`}
        resultString={"Present send!"}
        additionalData={additionalData}
      ></OrganizeTask>
    </>
  );
};
export default OrganizeSmallPresent;
