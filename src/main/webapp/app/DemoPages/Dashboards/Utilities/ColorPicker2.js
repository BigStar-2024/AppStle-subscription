import { faSquare as farSquare } from '@fortawesome/free-regular-svg-icons';
import { faSquare as fasSquare } from '@fortawesome/free-solid-svg-icons';
import { faTimes as fasTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ColorPicker from 'rc-color-picker';
import 'rc-color-picker/assets/index.css';
import React, { Component } from 'react';
import { Input, InputGroup, InputGroupText, InputGroupAddon } from 'reactstrap';

class ColorPicker2 extends Component {
  changeHandler = colors => {
    this.props.onChange(colors.color);
  };

  closeHandler = colors => {
    this.props.onChange(colors.color);
  };

  render() {
    const { value, name } = this.props;
    return (
      <div>
        <div className="swatch">
          <InputGroup>
            <InputGroupAddon addonType="prepend" style={{ flexGrow: '1' }}>
              <ColorPicker
                enableAlpha={false}
                id={name + '-color-picker'}
                color={value || ''}
                alpha={100}
                onChange={this.changeHandler}
                onClose={this.closeHandler}
                placement="topLeft"
                className="w-100"
                defaultColor=""
                /*onChangeComplete={this.handleColorChange}*/
              >
                <div className="input-group bg-transparent">
                  <InputGroupAddon>
                    {value ? (
                      <InputGroupText style={{ color: value }}>
                        <FontAwesomeIcon size="lg" icon={fasSquare} />
                      </InputGroupText>
                    ) : (
                      <InputGroupText className="bg-transparent-pattern">
                        <FontAwesomeIcon size="lg" icon={farSquare} />
                      </InputGroupText>
                    )}
                  </InputGroupAddon>
                  <Input
                    id={name}
                    type="text"
                    name={name}
                    value={value ? value.replace('#', '').toUpperCase() : ''}
                    className=""
                    onChange={this.changeHandler}
                    onClose={this.closeHandler}
                    autoComplete="off"
                    placeholder={this.props.placeholder}
                    style={{ borderRadius: '0' }}
                  />
                </div>
              </ColorPicker>
            </InputGroupAddon>
            <InputGroupAddon addonType="append" style={{ flex: '0', width: 'auto' }}>
              <div style={{ color: "#495057", cursor: 'pointer' }} onClick={() => this.changeHandler('')}>
                <FontAwesomeIcon size="sm" icon={fasTimes} />
              </div>
            </InputGroupAddon>
          </InputGroup>
        </div>
      </div>
    );
  }
}

export default ColorPicker2;
