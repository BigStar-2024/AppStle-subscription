import React from 'react';
import DatePicker from 'react-datepicker';

const DatePickerWithTimezone = ({ selected, onChange, timezone, ...props }) => {
  return (
    <DatePicker
      selected={selected}
      onChange={(date, e) => {
        onChange(date);
      }}
      {...props}
    />
  );
};

export default DatePickerWithTimezone;
