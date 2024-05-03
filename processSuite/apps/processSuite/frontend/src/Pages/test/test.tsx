import React from "react";
import { CustomFormProps, CustomFormsRenderer } from "../../DialogRenderer";
import { Card } from "antd";
import Meta from "antd/es/card/Meta";
import styles from "./test.module.css";

const test: React.FC<CustomFormProps> = (props) => {
  return (
    <>
      <div className={styles.container}>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        <Card title="Test"></Card>
        
      </div>
    </>
  );
};

export default test;
