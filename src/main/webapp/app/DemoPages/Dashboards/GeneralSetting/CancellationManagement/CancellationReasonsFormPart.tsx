import React from 'react';
import { Field, FieldProps, FieldRenderProps } from 'react-final-form';
import { FieldArray, FieldArrayRenderProps } from 'react-final-form-arrays';
import { Button, FormGroup, Input, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-common-types';
import { faChevronDown, faChevronUp, faTrash } from '@fortawesome/free-solid-svg-icons';
import CancellationReason, { CancellationReasonAction } from 'app/shared/model/cancellation-reason.model';
import RetentionActionsConfiguration from './RetentionActionsConfiguration';

/*
 * Used to keep sizings consistent, manageable, and easily changeable
 */
const columnSizings = {
  cancellationReason: 8, //flex
  cancellationAction: 3, //flex
  cancellationDiscount: 1, //flex
  cancellationPrompt: 1, //flex
  buttonWidth: 1.25, //rem
  gap: 15, //px
};

const buttonContainerWidth = `calc(${columnSizings.buttonWidth * 3}rem + ${columnSizings.gap * 2}px)`;

const defaultReason: CancellationReason = {
  cancellationReason: '',
  cancellationAction: CancellationReasonAction.NONE,
  cancellationDiscount: null,
  cancellationPrompt: true,
};

const CancellationReasonsFormPart = (props: {
  mutators: { push: (name: string, value: CancellationReason) => void; setDiscount: (index: number, value: number) => void };
}) => {
  const { mutators } = props;

  function validateNumOfReasons(values: CancellationReason[]) {
    if (values == null || !Array.isArray(values) || values?.length == null || values.length <= 0) {
      return 'Please enter at least one cancellation reason';
    }
  }

  function validateReason(reason: string) {
    if (!reason || !reason?.trim()) {
      return 'Please provide a reason.';
    }
  }

  function handleActionChange(event: InputEvent, finalFormChange: (event: Event) => void, index: number) {
    const inputTarget = event.target as HTMLSelectElement;

    // Set Discount to null when chose action is not offering a discount
    if (parseFloat(inputTarget.value) != CancellationReasonAction.DISCOUNT) {
      mutators.setDiscount(index, null);
    } else {
      mutators.setDiscount(index, 0);
    }

    finalFormChange(event);
  }

  function validateAction(action: number) {
    if (!(action in CancellationReasonAction)) {
      return 'Please choose a valid retention action.';
    }
  }

  function handleDiscountChange(event: InputEvent, finalFormChange: (event: Event) => void) {
    const inputTarget = event.target as HTMLInputElement;

    if (parseFloat(inputTarget.value) < 0) {
      inputTarget.value = '0';
    }

    if (parseFloat(inputTarget.value) > 100) {
      inputTarget.value = '100';
    }
    finalFormChange(event);
  }

  const isDiscountDisabled = (fields: FieldArrayRenderProps<any, HTMLElement>['fields'], index: number) => {
    return fields.value[index].cancellationAction != CancellationReasonAction.DISCOUNT;
  };

  function validatePrompt(prompt: string | number | boolean) {
    const promptType = ['1', '0', 1, 0, true, false];
    if (!promptType.some(validType => validType == prompt)) {
      return 'Please choose a valid prompt value.';
    }
  }

  function handleMoveUp(fields: FieldArrayRenderProps<any, HTMLElement>['fields'], index: number) {
    if (index > 0) {
      fields.swap(index, index - 1);
    }
  }

  function handleMoveDown(fields: FieldArrayRenderProps<any, HTMLElement>['fields'], index: number) {
    if (index < fields.length - 1) {
      fields.swap(index, index + 1);
    }
  }

  function handleDelete(fields: FieldArrayRenderProps<any, HTMLElement>['fields'], index: number) {
    fields.remove(index);
  }

  return (
    <>
      <h5>Cancellation Reasons</h5>
      <hr />
      <div className="d-flex" style={{ gap: `${columnSizings.gap}px` }}>
        <h6 className="font-weight-bold" style={{ flex: columnSizings.cancellationReason }}>
          Reason
        </h6>
        <h6 className="font-weight-bold" style={{ flex: columnSizings.cancellationAction }}>
          Retention Action
        </h6>
        <h6 className="font-weight-bold" style={{ flex: columnSizings.cancellationDiscount }}>
          Discount (%)
        </h6>
        <h6 className="font-weight-bold" style={{ flex: columnSizings.cancellationPrompt }}>
          Prompt
        </h6>
        {/* Used to take up the space that the buttons in each field would take */}
        <div style={{ width: buttonContainerWidth }}></div>
      </div>

      <FormGroup>
        <FieldArray name="cancellationReasonsJSON" validate={validateNumOfReasons}>
          {({ fields, meta }) => (
            <>
              {meta.error && typeof meta.error === 'string' && <span className="invalid-feedback d-block">{meta.error}</span>}
              {fields.map((name, index) => (
                <Row key={name} className="no-gutters d-flex mb-2" style={{ gap: `${columnSizings.gap}px` }}>
                  <FieldFlex name={`${name}.cancellationReason`} validate={validateReason} flex={columnSizings.cancellationReason}>
                    {/* Form doesn't seem to update field after mutation, so setting value from fields manually becomes necessary */}
                    {({ input, meta }) => (
                      <div>
                        <Input
                          {...input}
                          value={fields.value[index].cancellationReason}
                          className="form-control"
                          placeholder="Please enter your reason here..."
                          invalid={meta.error && meta.touched}
                        />
                        {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
                      </div>
                    )}
                  </FieldFlex>

                  <FieldFlex
                    name={`${name}.cancellationAction`}
                    validate={validateAction}
                    parse={(value: string) => parseInt(value)}
                    flex={columnSizings.cancellationAction}
                  >
                    {({ input, meta }) => (
                      <div>
                        <Select
                          {...input}
                          className="form-control"
                          value={fields.value[index].cancellationAction}
                          onChange={(event: InputEvent) => {
                            handleActionChange(event, input.onChange, index);
                          }}
                        >
                          <option value={CancellationReasonAction.NONE}>None</option>
                          <option value={CancellationReasonAction.DISCOUNT}>Offer Discount</option>
                          <option value={CancellationReasonAction.SWAP}>Swap</option>
                          <option value={CancellationReasonAction.GIFT}>Gift</option>
                          <option value={CancellationReasonAction.SKIP}>Skip</option>
                          <option value={CancellationReasonAction.PAUSE}>Pause Subscription</option>
                          <option value={CancellationReasonAction.CHANGE_DATE}>Change Date</option>
                          <option value={CancellationReasonAction.CHANGE_ADDRESS}>Change Address</option>
                          <option value={CancellationReasonAction.UPDATE_FREQUENCY}>Update Frequency</option>
                        </Select>
                        {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
                      </div>
                    )}
                  </FieldFlex>

                  <FieldFlex
                    name={`${name}.cancellationDiscount`}
                    component="input"
                    parse={(value: string) => parseInt(value)}
                    flex={columnSizings.cancellationDiscount}
                  >
                    {({ input, meta }) => (
                      <div>
                        <Input
                          {...input}
                          value={fields.value[index].cancellationDiscount ?? ''}
                          className="form-control"
                          type="number"
                          onChange={(event: InputEvent) => handleDiscountChange(event, input.onChange)}
                          disabled={isDiscountDisabled(fields, index)}
                          invalid={meta.error && meta.touched}
                        />
                        {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
                      </div>
                    )}
                  </FieldFlex>

                  <FieldFlex
                    name={`${name}.cancellationPrompt`}
                    validate={validatePrompt}
                    parse={(value: string) => parseInt(value) > 0}
                    flex={columnSizings.cancellationPrompt}
                  >
                    {({ input, meta }) => (
                      <div>
                        <Select
                          {...input}
                          className="form-control"
                          value={fields.value[index].cancellationPrompt ? 1 : 0}
                          invalid={meta.error && meta.touched}
                        >
                          <option value={1}>Yes</option>
                          <option value={0}>No</option>
                        </Select>
                        {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
                      </div>
                    )}
                  </FieldFlex>
                  <CancellationReasonsButton
                    icon={faChevronUp}
                    tooltip="Move reason up in list"
                    disabled={index === 0}
                    onClick={() => handleMoveUp(fields, index)}
                  />
                  <CancellationReasonsButton
                    icon={faChevronDown}
                    tooltip="Move reason down in list"
                    disabled={index >= fields.length - 1}
                    onClick={() => handleMoveDown(fields, index)}
                  />
                  <CancellationReasonsButton
                    icon={faTrash}
                    tooltip="Delete reason"
                    color={fields.length > 1 ? 'red' : null}
                    disabled={fields.length <= 1}
                    onClick={() => handleDelete(fields, index)}
                  />
                  {/* This is here since the id field previously existed.
                    I cannot find it being used in anyway, but just in case*/}
                  <Field name="id">{({ input }) => <Input {...input} type="hidden" value={true} />}</Field>
                </Row>
              ))}
            </>
          )}
        </FieldArray>
        <div className="d-flex justify-content-end">
          <Button
            color="primary"
            size="lg"
            onClick={() => {
              mutators.push('cancellationReasonsJSON', defaultReason);
            }}
          >
            Add Reason
          </Button>
        </div>
      </FormGroup>
      <FormGroup className="mt-4">
        <RetentionActionsConfiguration />
      </FormGroup>
    </>
  );
};

const Select = (props: React.ComponentProps<typeof Input>) => {
  return <Input {...props} type="select" />;
};

const FieldFlex = (props: FieldProps<any, FieldRenderProps<any>> & { flex?: number }) => {
  const { flex = 1 } = props;

  return (
    <div style={{ flex }}>
      <Field {...props} />
    </div>
  );
};

const CancellationReasonsButton = (props: {
  icon: IconDefinition;
  onClick: () => void;
  disabled?: boolean;
  color?: string;
  tooltip?: string;
}) => {
  const { icon, onClick, disabled = false, color = 'inherit', tooltip = '' } = props;

  return (
    <>
      <div
        className="d-flex justify-content-around align-items-center"
        style={{
          width: `${columnSizings.buttonWidth}rem`,
          maxHeight: '38px',
          color,
          opacity: disabled ? 0.65 : 1,
          cursor: disabled ? 'auto' : 'pointer',
        }}
        onClick={disabled ? null : onClick}
        title={tooltip}
      >
        <FontAwesomeIcon icon={icon} style={{ width: `${columnSizings.buttonWidth}rem`, height: `${columnSizings.buttonWidth}rem` }} />
      </div>
    </>
  );
};

export default CancellationReasonsFormPart;
