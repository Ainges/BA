// 5head chatGPT function
export const formatDateToDEformat = (date: string) => {
  const regex = /^\d{4}-\d{2}-\d{2}$/; // Regular expression for the format yyyy-mm-dd
  if (!regex.test(date)) {
    throw new Error("Invalid date format. Please use the format yyyy-mm-dd.");
  }

  const dateObject = new Date(date);
  const localizedDateString = dateObject.toLocaleDateString("de-DE", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
  return localizedDateString;
};
