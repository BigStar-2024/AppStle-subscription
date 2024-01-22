import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState } from 'react';
import Switch from 'react-switch';
import { Input, InputGroup, InputGroupAddon } from 'reactstrap';
import ColorPicker3 from './ColorPicker3';
import { faTimes as fasTimes } from '@fortawesome/free-solid-svg-icons';

const ShopCustomizationField = (props) => {
  const field = props?.field;
  const [value, setValue] = useState("");
  useState(() => {
    if (props?.field?.value) {
      setValue(props?.field?.value);
      props.input.onChange({
        value: props?.field?.value,
        labelId: field?.id,
        id: field?.shopCustomizationId
      });
    }
  }, [props]);
  const onTextChange = (val) => {
    if (field?.type == "TOGGLE") {
      if (val == true) {
        setFinalValue(1);
      } else {
        setFinalValue('');
      }
    } else {
      setFinalValue(val);
    }
  }
  const setFinalValue = (val) => {
    setValue(val);
    props.input.onChange({
      value: val,
      labelId: field?.id,
      id: field?.shopCustomizationId
    });
  }
  return (
    <>
      {field?.type == "COLOR_PICKER" && <ColorPicker3 value={value} name={props?.input.name} onChange={(value) => onTextChange(value)} />}
      {(field?.type == "PIXEL_FIELD" || field?.type == "TEXT_FIELD") &&
        <>
          <InputGroup>
            <Input {...props?.input} value={value} onChange={(value) => onTextChange(value.target.value)} />
            {field?.type == "PIXEL_FIELD" &&
              <InputGroupAddon addonType='append'>px</InputGroupAddon>
            }
          </InputGroup>
          <div style={{ display:"flex",justifyContent:"flex-end" }}>
              <span style={{  color: "blue", cursor: "pointer", fontSize: "12px", textDecoration: "underline" }}  onClick={() => onTextChange('')}>Reset</span></div>
        </>
      }
      {field?.type == "TOGGLE" &&
        <Switch checked={Boolean(value == 1)} onColor="#3ac47d" onChange={(value) => onTextChange(value)} handleDiameter={20} boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
          activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)" height={17} width={36} id={"material-switch-" + field?.id} />
      }
    </>
  );
}
export default ShopCustomizationField;
