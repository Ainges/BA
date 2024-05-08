export const formatDate = (date: string) => {
  // 5head ChatGPT expression to convert date format
  const reversedDate = date.split("-").reverse().join("-");
  const dateTimeString = `${reversedDate}T00:00:00`;
  const dateObject = new Date(dateTimeString);
  const localizedDateString = dateObject.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
  const finalString = localizedDateString.toString();
  return finalString;
};
