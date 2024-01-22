import React from 'react';
import { UncontrolledAlert,Alert } from 'reactstrap';

export default function MessageBox(props) {
    const {color, message } = props
    return (
    // success, danger
    // <UncontrolledAlert color={color || "info"} className="appstle_error_box mb-2 mt-2">
    //   {message}
    // </UncontrolledAlert>
    <Alert color={color || "info"} className="appstle_error_box mb-2 mt-2">
         {message}
      </Alert>
  )
}
