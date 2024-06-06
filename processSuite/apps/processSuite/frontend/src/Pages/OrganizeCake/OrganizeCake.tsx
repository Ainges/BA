import { UserTaskResult } from "@5minds/processcube_engine_sdk";
import { FormState } from "@atlas-engine-contrib/atlas-ui_sdk";
import OrganizeTask from "../../Components/OrganizeTask/OrganizeTask";
import { CustomFormProps } from "../../DialogRenderer";
import Paragraph from "antd/es/typography/Paragraph";
import { formatDateToDEformat } from "../../functions/formatDateToDEformat";
import { DecouplerProps } from "../../Interfaces/Decoupler";

const OrganizeCake: React.FC<DecouplerProps> = (props) => {
  const currentToken = props.userTask.startToken;
  return (
    <>
      <OrganizeTask
        processData={props}
        title={"Kuchen für Mitarbeiter organisieren"}
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
              Gleichzeitig hat er am selben Tag Geburtstag. Bitte organisieren
              Sie einen Kuchen für den neuen Mitarbeiter!
            </Paragraph>
          </>
        }
        checkboxText={`Kuchen ist auf den ${formatDateToDEformat(
          currentToken.OnboardingData.first_working_day
        )} bestellt!`}
        resultString={"Cake organized!"}
        additionalData={[]}
      ></OrganizeTask>
    </>
  );
};
export default OrganizeCake;
