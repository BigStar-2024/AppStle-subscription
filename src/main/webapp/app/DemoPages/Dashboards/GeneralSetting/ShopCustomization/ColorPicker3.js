import { faSquare as farSquare } from '@fortawesome/free-regular-svg-icons';
import { faSquare as fasSquare } from '@fortawesome/free-solid-svg-icons';
import { faTimes as fasTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ColorPicker from 'rc-color-picker';
import 'rc-color-picker/assets/index.css';
import React, { useState } from 'react';
import { Input, InputGroup, InputGroupAddon, InputGroupText } from 'reactstrap';

const ColorPicker3 = (props) => {
  const [value, setValue] = useState("");
  const [name, setName] = useState("");

  useState(() => {
    if (props?.name) {
      setName(props?.name)
    }
    if (props?.value) {
      setValue(props?.value)
    }
  }, [props]);

  const changeHandler = colors => {
    if (colors) {
      setValue(colors.color);
      props?.onChange(colors.color)
    } else {
      setValue("");
      props?.onChange("")
    }
  };

  const closeHandler = colors => {
    if (colors) {
      setValue(colors.color);
      props?.onChange(colors.color)
    } else {
      setValue("");
      props?.onChange("")
    }
  };


  return (
    <div>
      <div className="swatch">
        <InputGroup>
          <InputGroupAddon addonType='prepend' style={{ flexGrow: '1' }}>
            <ColorPicker
              enableAlpha={false}
              id={name + '-color-picker'}
              color={value || ''}
              alpha={100}
              onChange={changeHandler}
              onClose={closeHandler}
              placement="topLeft"
              className="w-100"
              defaultColor=""
            >
              <div className="input-group bg-transparent">
                <InputGroupAddon addonType='prepend'>
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
                  value={value}
                  className=""
                  onChange={changeHandler}
                  onClose={closeHandler}
                  autoComplete="off"
                  placeholder={props?.placeholder}
                  style={{ borderRadius: '0' }}
                />
              </div>
            </ColorPicker>
          </InputGroupAddon>
          <InputGroupAddon addonType='append' style={{ flex: '0', width: 'auto' }}>
            <div style={{ color: "#495057", cursor: 'pointer' }} onClick={() => changeHandler('')}>
              <FontAwesomeIcon size="sm" icon={fasTimes} />
            </div>
          </InputGroupAddon>
        </InputGroup>
        <div style={{ display:"flex",justifyContent:"flex-end" }}>
              <span style={{color: "blue", cursor: "pointer", fontSize: "12px", textDecoration: "underline" }}  onClick={() => changeHandler('')}>Reset</span></div>
      </div>
    </div>
  );
}


export default ColorPicker3;
