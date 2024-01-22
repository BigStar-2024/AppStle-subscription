import { addDays } from 'date-fns';
import { DateTime } from 'luxon';
import React, { useEffect, useState } from 'react';

function DeliveryScheduledDate(props) {
  const { billingDate, dateFormat, displayText } = props;
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);

  useEffect(() => {
    if (billingDate) {
      let dateDay = new Date(billingDate).getDay();
      let fDate = dateDay == 0 || dateDay == 1 ? nextDate(2, addDays(new Date(billingDate), 2)) : nextDate(2, billingDate);
      setFromDate(fDate);
      setToDate(nextDate(5, fDate));
    }
  }, [billingDate]);

  const nextDate = (dayIndex, date) => {
    var today = new Date(date);
    today.setDate(today.getDate() + ((dayIndex - 1 - today.getDay() + 7) % 7) + 1);
    return today.toISOString();
  };

  const formatDate = date => {
    return DateTime.fromISO(date).toFormat(dateFormat);
  };

  return (
    <div
      dangerouslySetInnerHTML={{__html: displayText
        ?.replace('{{fromDate}}', formatDate(fromDate))
        ?.replace('{{toDate}}', formatDate(toDate))
        ?.replace('{{billingDate}}', formatDate(billingDate))}}
    />
  );
}

export default DeliveryScheduledDate;
