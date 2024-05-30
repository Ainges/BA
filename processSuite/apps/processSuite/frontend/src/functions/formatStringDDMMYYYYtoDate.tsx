function formatStringDDMMYYYYtoDate(ddmmyyyyString: string) {
  const [day, month, year] = ddmmyyyyString
    .split(".")
    .map((part) => parseInt(part, 10));

  // Beachte: Der Monat ist nullbasiert (0 = Januar, 1 = Februar, ...), daher müssen wir 1 subtrahieren
  const date: Date = new Date(year, month - 1, day);

  return date;
}

export default formatStringDDMMYYYYtoDate;
