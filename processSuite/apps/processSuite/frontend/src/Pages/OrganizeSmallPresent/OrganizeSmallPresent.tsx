import { Alert } from "antd";
import OrganizeTask from "../../Components/OrganizeTask/OrganizeTask";
import Paragraph from "antd/es/typography/Paragraph";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";
import { DecouplerProps } from "../../Interfaces/Decoupler";

const OrganizeSmallPresent: React.FC<DecouplerProps> = (props) => {
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
