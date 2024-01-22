import { faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { Component } from 'react';
import { Alert } from 'reactstrap';

export default class SavingDisclaimer extends Component {
  render() {
    return (
      <Alert className="mbg-3" color="warning">
        <span className="pr-2">
          <FontAwesomeIcon icon={faInfoCircle} />
        </span>
        {this.props.text === undefined
          ? 'Please note that because modern browsers cache the javascript files, it may take some time for your storefront to reflect saved changes, however, you can always see the new changes in the private/incognito window of your browser as private window is never cached by the browser.'
          : this.props.text}
      </Alert>
    );
  }
}
