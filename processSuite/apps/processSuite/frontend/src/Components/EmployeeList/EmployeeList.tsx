import React, { useContext } from "react";
import {
  Alert, Card,
  Col,
  Divider,
  Row,
  Table,
  TableColumnsType
} from "antd";
import styles from "./EmployeeList.module.css";
import Paragraph from "antd/es/typography/Paragraph";
import { RowSelectionType } from "antd/es/table/interface";
import {
  BuddyAndEmployeeSelectionContext,
  TableDataType,
} from "../../Pages/BuddyAndEmployeeSelection/BuddyAndEmployeeSelectionProvider";

interface EmployeeSelection {
  selectedEmployees: React.Key[];
  setSelectedEmployees: React.Dispatch<React.SetStateAction<React.Key[]>>;
  selectedBuddy: React.Key[];
}

const EmployeeList: React.FC<EmployeeSelection> = ({}) => {
  // ### Context initialization
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

  const rowSelection = {
    type: "checkbox" as RowSelectionType,
    selectedRowKeys: selectedEmployees,

    // To disable selection of the buddy
    getCheckboxProps: (record: TableDataType) => ({
      disabled: record.email === selectedBuddy.at(0)?.toString(), // Column configuration not to be checked
      name: record.name,
    }),

    onChange: (selectedRowKeys: React.Key[], selectedRows: TableDataType[]) => {
      console.log(
        `selectedRowKeys: ${selectedRowKeys}`,
        "In State:",
        selectedEmployees
      );
      setSelectedEmployees(selectedRowKeys);
    },
  };

  return (
    <>
      <div className={styles.outer}>
        <Divider orientation="left">Mitarbeiter Auswahl</Divider>
        <Row>
          <Col span={2}></Col>
          <Col span={20}>
            <Paragraph>
              Bitte beachten Sie, dass der "Buddy" hier nicht nochmal ausgewählt werden kann, und aus diesem Grund auch nicht in der Liste erscheint.
            </Paragraph>
          </Col>
          <Col span={2}></Col>
        </Row>
        <Row>
          <Col span={2}></Col>
          <Col span={20}>
            <Alert
              message="Der Buddy muss hier NICHT nochmal ausgewählt werden!"
              type="info"
              showIcon
            />
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
                  loading={employeeDataWithoutBuddy.length === 0}
                  columns={columns}
                  dataSource={employeeDataWithoutBuddy}
                />
              </Card>
            </div>
          </Col>
          <Col span={2}></Col>
        </Row>
      </div>
    </>
  );
};

export default EmployeeList;
