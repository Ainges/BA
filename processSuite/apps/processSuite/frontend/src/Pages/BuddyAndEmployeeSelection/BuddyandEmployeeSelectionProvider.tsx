import React, { ReactNode, createContext, useState } from "react";

export interface TableDataEmployee {
  key: React.Key;
  name: string;
  position: string;
  email: string;
  profilePictureURI: string;
}

// Define the context
interface BuddyAndEmployeeSelectionContextProps {
  // selected Buddy state
  selectedBuddy: React.Key[];
  setSelectedBuddy: React.Dispatch<React.SetStateAction<React.Key[]>>;

  // selected Employees state
  selectedEmployees: React.Key[];
  setSelectedEmployees: React.Dispatch<React.SetStateAction<React.Key[]>>;

  // Fetched Data from API stored in State
  employeeData: TableDataEmployee[];
  setEmployeeData: React.Dispatch<React.SetStateAction<TableDataEmployee[]>>;

  employeeDataFiltered: TableDataEmployee[];
  setEmployeeDataFiltered: React.Dispatch<
    React.SetStateAction<TableDataEmployee[]>
  >;
}

export const BuddyAndEmployeeSelectionContext =
  createContext<BuddyAndEmployeeSelectionContextProps>({
    selectedBuddy: [],
    setSelectedBuddy: () => {},

    selectedEmployees: [],
    setSelectedEmployees: () => {},

    employeeData: [],
    setEmployeeData: () => {},

    employeeDataFiltered: [],
    setEmployeeDataFiltered: () => {},
  });

interface BuddyAndEmployeeSelectionProviderProps {
  children: ReactNode;
}

// Define the provider component
const BuddyAndEmployeeSelectionProvider: React.FC<
  BuddyAndEmployeeSelectionProviderProps
> = ({ children }) => {
  const [selectedBuddy, setSelectedBuddy] = useState<React.Key[]>([]);
  const [selectedEmployees, setSelectedEmployees] = useState<React.Key[]>([]);
  const [employeeData, setEmployeeData] = useState<TableDataEmployee[]>([]);
  const [employeeDataFiltered, setEmployeeDataFiltered] = useState<
    TableDataEmployee[]
  >([]);

  return (
    <BuddyAndEmployeeSelectionContext.Provider
      value={{
        selectedBuddy,
        setSelectedBuddy,

        selectedEmployees,
        setSelectedEmployees,

        employeeData,
        setEmployeeData,

        employeeDataFiltered,
        setEmployeeDataFiltered,
      }}
    >
      {children}
    </BuddyAndEmployeeSelectionContext.Provider>
  );
};

export default BuddyAndEmployeeSelectionProvider;
