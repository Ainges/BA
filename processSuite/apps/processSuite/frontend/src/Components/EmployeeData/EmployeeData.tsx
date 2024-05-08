import { Card, List } from "antd";

export type EmployeeData = {
  title: string;
  data: string;
};

// react functional component in ts

export const EmployeeData: React.FC<{ employeeData: EmployeeData[] }> = ({
  employeeData,
}) => {
  return (
    <>
      <Card title="Daten zu neuem Mitarbeiter">
        <List
          bordered
          dataSource={employeeData}
          renderItem={(item: EmployeeData, index: number) => (
            <List.Item>
              <List.Item.Meta title={item.title} description={item.data} />
            </List.Item>
          )}
        ></List>
      </Card>
    </>
  );
};
