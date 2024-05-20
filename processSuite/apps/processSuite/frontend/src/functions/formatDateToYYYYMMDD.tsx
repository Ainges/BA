function formatDateToYYYYMMDD(date: Date) {
    // Holen des Jahres
    var year = date.getFullYear();
    // Holen des Monats und hinzufügen einer führenden Null falls nötig
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    // Holen des Tages und hinzufügen einer führenden Null falls nötig
    var day = ('0' + date.getDate()).slice(-2);
    
    // Zusammenfügen in das gewünschte Format
    return year + '-' + month + '-' + day;
}
export default formatDateToYYYYMMDD;