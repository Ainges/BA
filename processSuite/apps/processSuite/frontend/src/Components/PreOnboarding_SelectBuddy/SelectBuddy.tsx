import React, { useContext } from "react";
import { Button, Card, Col, Divider, Row, Table, TableColumnsType } from "antd";
import styles from "./SelectBuddy.module.css";
import Paragraph from "antd/es/typography/Paragraph";
import { RowSelectionType } from "antd/es/table/interface";
import {
  BuddyAndEmployeeSelectionContext,
  TableDataType,
} from "../../Pages/BuddyAndEmployeeSelection/BuddyAndEmployeeSelectionProvider";

interface BuddySelection {
  selectedBuddy: React.Key[];
  setSelectedBuddy: React.Dispatch<React.SetStateAction<React.Key[]>>;
}

const SelectBuddy: React.FC<BuddySelection> = ({}) => {
  // context initialization
  const context = useContext(BuddyAndEmployeeSelectionContext);

  // context State
  const { selectedBuddy, setSelectedBuddy } = context;
  const { selectedEmployees, setSelectedEmployees } = context;
  const { employeeData, setEmployeeData } = context;
  const { employeeDataWithoutBuddy, setEmployeeDataWithoutBuddy } = context;

  const columns: TableColumnsType<TableDataType> = [
    {
      title: "Name",
      dataIndex: "name",
      render: (text, record) => (
        <a>
          <img
            className={styles.profilePicture}
            width={50}
            height={50}
            src={record.profilePictureURI}
          ></img>
          {"    " + text}
        </a>
      ),
    },
    {
      title: "Position",
      dataIndex: "position",
    },
    {
      title: "E-mail",
      dataIndex: "email",
    },
  ];

  // Only Sample Data
  const employeeArray: TableDataType[] = [
    {
      key: "1",
      name: "John Brown",
      position: "CEO",
      email: "dunder@mifflin.de",
      profilePictureURI: "https://picsum.photos/id/237/50/50",
    },
    {
      key: "2",
      name: "Jim Green",
      position: "CTO",
      email: "London No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/232/50/50",
    },
    {
      key: "3",
      name: "Joe Black",
      position: "COO",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/231/50/50",
    },
    {
      key: "4",
      name: "4",
      position: "Leiterin des Partyplanungsausschusses",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/235/50/50",
    },
    {
      key: "5",
      name: "5",
      position: "adasd",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/234/50/50",
    },
    {
      key: "6",
      name: "6",
      position: "99",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/230/50/50",
    },
    {
      key: "7",
      name: "7",
      position: "99",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/1/50/50",
    },
    {
      key: "8",
      name: "8",
      position: "99",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/999/50/50",
    },
    {
      key: "9",
      name: "9",
      position: "1000",
      email: "Sydney No. 1 Lake Park",
      profilePictureURI: "https://picsum.photos/id/1000/50/50",
    },
  ];

  // rowSelection object indicates the need for row selection
  const rowSelection = {
    // To allow only one Selection - cast is needed for TypeScript
    type: "radio" as RowSelectionType,
    // To LOAD the selected Buddy from State
    selectedRowKeys: selectedBuddy,
    // To SAVE the selected Buddy in State
    onChange: (selectedRowKeys: React.Key[], selectedRows: TableDataType[]) => {
      console.log(`SelectedBuddy: ${selectedRowKeys}`);
      setSelectedBuddy(selectedRowKeys.slice(-1));
    },
  };

  // const sendSelectedEmployees = () => {
  //   console.log("Selected employees:", selectedRows);
  //   props.finishUserTask({ selectedBuddy: selectedRows });
  // };

  return (
    <>
      <div className={styles.outer}>
        <Divider orientation="left">Buddy Auswahl</Divider>
        <Row>
          <Col span={2}></Col>
          <Col span={20}>
            <Paragraph>
              Bitte wählen Sie einen "Buddy" für den neuen Mitarbeiter aus!
            </Paragraph>
          </Col>
          <Col span={2}></Col>
        </Row>
        <br />
        <Row>
          <Col span={2}></Col>
          <Col span={20}>
            <div className={styles.card}>
              <Card>
                <Table
                  pagination={{ position: [] }}
                  scroll={{ y: 400 }}
                  rowSelection={rowSelection}
                  loading={employeeData.length === 0}
                  columns={columns}
                  dataSource={employeeData}
                />
              </Card>
            </div>
          </Col>
          <Col span={2}></Col>
        </Row>
        {/* <Button
          onClick={() => {
            //print all state variables
            console.log("selectedBuddy: ", selectedBuddy);
            console.log("selectedEmployees: ", selectedEmployees);
            console.log("employeeData: ", employeeData);
            console.log("employeeDataWithoutBuddy: ", employeeDataWithoutBuddy);
          }}
        >
          PRINT STATE
        </Button> */}
      </div>
    </>
  );
};

export default SelectBuddy;
