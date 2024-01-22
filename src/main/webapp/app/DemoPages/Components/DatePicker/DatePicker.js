import React, { Fragment } from 'react';

import { InputGroup, InputGroupAddon, InputGroupText } from 'reactstrap';

import { faCalendarAlt } from '@fortawesome/free-solid-svg-icons';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import DatePicker from 'react-datepicker';
import './datepicker.css';

class DatePickerComponent extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(date) {
    this.props.onSelectDay(date);
  }

  render() {
    return (
      <Fragment>
        <InputGroup>
          <InputGroupAddon addonType='prepend'>
            <InputGroupText>
              <FontAwesomeIcon icon={faCalendarAlt} />
            </InputGroupText>
          </InputGroupAddon>
          <DatePicker className={'form-control'} selected={this.props.selectedDate} onChange={this.handleChange} />
        </InputGroup>
      </Fragment>
    );
  }
}

export default DatePickerComponent;
