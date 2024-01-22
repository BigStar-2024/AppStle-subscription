import React, { Fragment, useEffect, useRef, useState } from 'react';
import { FormGroup, Input, InputGroup, Label, Row, Button } from 'reactstrap';
import { toast } from 'react-toastify';
import { Field } from 'react-final-form';
import './InputWithVariables.scss';

const InputWithVariables = ({
  formState,
  addVariablesToTextArea,
  selectDropdown,
  fieldName,
  rows,
  label,
  helpText,
  buttonText,
  insertText,
  maxLength
}) => {
  const validateRequired = (value, displayName) => {
    const type = typeof value;
    if ((type === 'string' && value?.trim()) || (type === 'number')) {
      if ((replaceVariableValues(value)?.length > maxLength)) {
        return `"${displayName}" should not exceed max length of ${maxLength}`;
      } else {
        return undefined;
      }
    } else {
      return `"${displayName}" field cannot be empty.`;
    }

  };

  const setTextAreaCursorData = () => {
    setTextAreaSelectionStart(textAreaRef.current.selectionStart);
    setTextAreaSelectionEnd(textAreaRef.current.selectionEnd);
  };

  const insertAtCursor = (field, insertText) => {
    //IE support
    let fieldValue = '';
    if (document.selection) {
      // field.focus();
      sel = document.selection.createRange();
      sel.text = insertText;
    }
    //MOZILLA and others
    else if (textAreaSelectionStart || field.selectionStart == '0') {
      // let startPos = textAreaSelectionStart == '0' ? field.value.length : parseInt(textAreaSelectionStart);
      // let endPos = textAreaSelectionEnd == '0' ? field.value.length : parseInt(textAreaSelectionEnd);
      let startPos = parseInt(textAreaSelectionStart);
      let endPos = parseInt(textAreaSelectionEnd);
      fieldValue = field.value.substring(0, startPos) + insertText + field.value.substring(endPos, field.value.length);
      field.selectionStart = startPos + insertText.length;
      field.selectionEnd = startPos + insertText.length;
      setTextAreaSelectionStart(String(startPos + insertText.length));
      setTextAreaSelectionEnd(String(startPos + insertText.length));
      return fieldValue;
    } else {
      return (fieldValue += insertText);
    }
  };

  const textAreaRef = useRef();
  let [addCoupounButtonClicked, setAddCopounButtonClicked] = useState(false);
  let [textAreaSelectionStart, setTextAreaSelectionStart] = useState('0');
  let [textAreaSelectionEnd, setTextAreaSelectionEnd] = useState('0');
  let [expandEmojiPicker, setExpandEmojiPicker] = useState(false);

  useEffect(() => {
    if (textAreaRef?.current) {
      textAreaRef.current.selectionStart = parseInt(textAreaSelectionStart);
      textAreaRef.current.selectionEnd = parseInt(textAreaSelectionEnd);
      // textAreaRef?.current?.focus();
    }
  }, [textAreaSelectionStart]);

  const getLiquidVariableText = () => {
    if (formState?.[fieldName + 'insertType'] === 'customer.id') {
      return '{{ customer.id }}';
    } else if (formState?.[fieldName + 'insertType'] === 'subscriptionContract.id') {
      return '{{ subscriptionContract.id }}';
    } else if (formState?.[fieldName + 'insertType'] === 'firstOrder.id') {
      return '{{ firstOrder.id }}';
    } else if (formState?.[fieldName + 'insertType'] === 'subscriptionContract.currentCycle') {
      return '{{ subscriptionContract.currentCycle }}'
    } else if (formState?.[fieldName + 'insertType'] === 'sellingPlan.id') {
      return '{{ subscriptionContract.sellingPlanIds }}'
    }else if (formState?.[fieldName + 'insertType'] === 'sellingPlan.name') {
      return '{{ subscriptionContract.sellingPlanNames }}'
    }
  };

  const toastOption = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  }

  const replaceVariableValues = (value) => {
    return value?.replaceAll('{{ customer.id }}', '5143218323632')
    .replaceAll('{{ subscriptionContract.id }}', '1114833072')
    .replaceAll('{{ firstOrder.id }}', '3745777647792')
    .replaceAll('{{ subscriptionContract.currentCycle }}', '1')
    .replaceAll('{{ sellingPlan.id }}', '2')
    .replaceAll('{{ sellingPlan.name }}', '3')
  }

  return (
    <FormGroup style={{position: 'relative'}}>
      <Label style={{ width: '100%', fontSize: '18px', marginBottom: '0' }} for="messageTime">
        {label ? label : 'Text Area'}
      </Label>
      <div style={{ color: 'inherit' }}>
        <div className="mt-3">
          <div>Your <b>{label}</b> is currently set as:<br/><span style={{visibility: formState?.[fieldName] ? 'visible' : 'hidden'}} className="orderTaggingExample">{replaceVariableValues(formState?.[fieldName])}</span></div>
        </div>
      </div>
      <div style={{}} className="insert_variable">
        {!addCoupounButtonClicked && (
          <Button color="primary" onClick={() => setAddCopounButtonClicked(true)}>
            {buttonText ? buttonText : 'Add Dynamic Variables'}
          </Button>
        )}
        {addCoupounButtonClicked && (
          <Field
            render={({ input, meta }) => (
              <>
                <Input {...input} style={{ display: 'inline-block' }}>
                  <option disabled value="selectType">
                    Select Type
                  </option>
                  {selectDropdown.map(option => {
                    return (
                      <option key={option?.value} value={option?.value}>
                        {option?.name}
                      </option>
                    );
                  })}
                </Input>
              </>
            )}
            autoComplete="off"
            id="insertType"
            className="form-control"
            type="select"
            name={fieldName + 'insertType'}
            initialValue="selectType"
          />
        )}

        {addCoupounButtonClicked && formState?.insertType !== 'selectType' && (
          <>
          <Button
            color="success"
            style={{ marginLeft: '2%', justifySelf: 'flex-end' }}
            onClick={() => {
                const insertText = getLiquidVariableText()
                insertText && addVariablesToTextArea(fieldName, insertAtCursor(textAreaRef.current, insertText))
            }}
          >
            {insertText ? insertText : 'Insert'}
          </Button>
          <Button color="danger ml-1" onClick={() => setAddCopounButtonClicked(false)}>X</Button>
          </>
        )}
      </div>
      <Field
        render={({ input, meta }) => (
          <>
            <textarea
              {...input}
              type="textarea"
              rows={rows ? rows : '2'}
              onChange={value => {
                setTextAreaCursorData();
                input.onChange(value);
              }}
              onClick={() => {
                setTextAreaCursorData();
              }}
              onInput={() => {
                setTextAreaCursorData();
              }}
              invalid={meta.error && meta.touched ? true : null}
              ref={textAreaRef}
              className="form-control"
            />
          </>
        )}
        autoComplete="off"
        id={fieldName}
        className="form-control"
        type="text"
        name={fieldName}
      />


    </FormGroup>
  );
};

export default InputWithVariables;
