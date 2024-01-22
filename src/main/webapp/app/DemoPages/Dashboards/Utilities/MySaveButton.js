import React, {Component} from 'react';
import LaddaButton, {EXPAND_RIGHT} from 'react-ladda';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import { faLock } from '@fortawesome/free-solid-svg-icons';

export default class MySaveButton extends Component {
  render() {
    const {updating, onClick, updatingText, text, showSaveIcon, type, addBuffer, disabled, id, lockedIcon, style, title} = this.props;
    return (
      <LaddaButton
        className={this.props.className + ' ' + `btn btn-shadow btn-primary`}
        size="lg"
        data-style={EXPAND_RIGHT}
        loading={updating}
        type={type ? type : 'submit'}
        onClick={onClick}
        disabled ={disabled}
        id={id}
        style={style}
        title={title}
      >
      {
        lockedIcon && <FontAwesomeIcon
                        className='mr-2'
                        icon={faLock}/>
      }
        {showSaveIcon ? (
          <span>
            <FontAwesomeIcon icon="save"/>{' '}
          </span>
        ) : null}
        {addBuffer && <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>}
        {updating ? updatingText || 'Saving' : text || 'Save'}
        {addBuffer && <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>}
      </LaddaButton>
    );
  }
}
