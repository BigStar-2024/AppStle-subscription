import React, { Fragment, useEffect, useRef, useState } from 'react';
import { FormGroup, Input, InputGroup,  Label, Row, Button } from 'reactstrap';
import { toast } from 'react-toastify';
import { Field } from 'react-final-form';
import { Picker, Emoji } from 'emoji-mart';
import OutsideClickHandler from 'react-outside-click-handler';

const TextAreaWithCopoun = ({
                              formState,
                              addVariablesToTextArea,
                              selectDropdown,
                              fieldName,
                              rows,
                              label,
                              helpText,
                              buttonText,
                              insertText
                            }) => {
  const validateRequired = (value, displayName) => {
    const type = typeof value;
    if (type === 'string' && value?.trim()) {
      return undefined;
    } else if (type === 'number') {
      return undefined;
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
      field.focus();
      sel = document.selection.createRange();
      sel.text = insertText;
    }
    //MOZILLA and others
    else if (textAreaSelectionStart || field.selectionStart == '0') {
      let startPos = textAreaSelectionStart == '0' ? field.value.length : parseInt(textAreaSelectionStart);
      let endPos = textAreaSelectionEnd == '0' ? field.value.length : parseInt(textAreaSelectionEnd);
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
      textAreaRef?.current?.focus();
    }
  }, [textAreaSelectionStart]);

  const getLiquidVariableText = () => {
    if (formState?.insertType === 'discount') {
      if (isNaN(formState?.discount)) {
        toast.error('Discount value should be a number.', toastOption);
      } else if (!formState?.discount) {
        toast.error('Discount field cannot be empty.', toastOption);
      } else if (formState?.discount <= 0) {
        toast.error('Discount value should be greater than 0.', toastOption);
      } else {
        return `{{${formState?.discount}_${formState?.discountType}_off_discount_code}}`;
      }
    } else if (formState?.insertType === 'shop_name') {
      return '{{shop.name}}';
    } else if (formState?.insertType === 'customer_name') {
      return '{{ customer.name }}';
    } else if (formState?.insertType === 'checkout_url') {
      return '{{ checkoutUrl }}';
    } else if (formState?.insertType === 'shop_url') {
      return '{{shop.url}}';
    } else if (formState?.insertType === 'shipping_company') {
      return '{{shippingCompany}}';
    } else if (formState?.insertType === 'tracking_number') {
      return '{{trackingNumber}}';
    }
  };

  const toastOption = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  }

  return (
    <FormGroup style={{position: 'relative'}}>
      <Label style={{ width: '100%', fontSize: '18px', marginBottom: '0' }} for="messageTime">
        {label ? label : 'Text Area'}
      </Label>
      <div style={{ color: 'inherit' }}>
        <div class="automation_settings_help_text">{helpText ? helpText : 'Enter text here'}</div>
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
                  {selectDropdown.map(value => {
                    return (
                      <option key={value} value={value}>
                        {value
                          ?.split('_')
                          .map(word => word[0].toUpperCase() + word.substr(1).toLowerCase())
                          .join(' ')}
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
            name="insertType"
            initialValue="selectType"
          />
        )}

        {addCoupounButtonClicked &&
        formState?.insertType === 'discount' &&
        (formState?.discountType === 'percent' || formState?.discountType === 'bucks') && (
          <Field
            render={({ input, meta }) => (
              <>
                <Input placeholder="Enter Discount" min="0" {...input} style={{ marginLeft: '2%', display: 'inline-block' }} />
              </>
            )}
            autoComplete="off"
            id="discount"
            className="form-control"
            type="number"
            name="discount"
            initialValue="5"
          />
        )}
        {addCoupounButtonClicked && formState?.insertType === 'discount' && (
          <Field
            render={({ input, meta }) => (
              <>
                <Input {...input} style={{ marginLeft: '2%', display: 'inline-block' }}>
                  <option value="percent">Percent (%)</option>
                  <option value="bucks">Bucks</option>
                </Input>
              </>
            )}
            autoComplete="off"
            id="discountType"
            className="form-control"
            type="select"
            name="discountType"
            initialValue="percent"
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
              rows={rows ? rows : '8'}
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
            <div className="emojiWrapper">
              <OutsideClickHandler useCapture={true} onOutsideClick={() => setExpandEmojiPicker(false)}>
                <Picker set='apple' skinEmoji="hand" showPreview={false} title='Change Skin tone' onClick={(emoji, event) => addVariablesToTextArea(fieldName, insertAtCursor(textAreaRef.current, emoji?.native))} emoji='point_right' style={{display: expandEmojiPicker ? 'block' : 'none', position: 'absolute', bottom: '42px', left: '0px', zIndex: '9999999'}}/>
              </OutsideClickHandler>
              <Emoji emoji='blush' set='apple' size={20} onClick={() => setExpandEmojiPicker(!expandEmojiPicker)} />
            </div>

          </>
        )}
        autoComplete="off"
        id={fieldName}
        className="form-control"
        type="number"
        name={fieldName}
        validate={value => validateRequired(value, label)}
      />


    </FormGroup>
  );
};

export default TextAreaWithCopoun;
