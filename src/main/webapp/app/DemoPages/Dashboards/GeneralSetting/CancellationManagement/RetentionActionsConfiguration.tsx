import React, { useState } from 'react';
import { Field, useFormState } from 'react-final-form';
import Switch from 'react-switch';
import { FormGroup, Label, Row, Col, Card, CardHeader, Collapse, Input } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import HelpTooltip from '../../Shared/HelpTooltip';

const RetentionActionsConfiguration = () => {
  const [isOpen, setIsOpen] = useState(false);

  const { values } = useFormState();

  const insetStyling = { background: '#f6f6f6', padding: '18px', borderRadius: '10px' };

  function validateDurationCycle(durationInput: string) {
    if (durationInput == null) {
      return undefined;
    }

    if (parseInt(durationInput) == NaN || parseFloat(durationInput) % 1 !== 0 || parseInt(durationInput) < 0) {
      return 'Please enter a whole number equal to 0 or higher';
    }
  }

  function validateEmailAddress(val: string) {
    const valid = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
      val
    );
    return !valid ? 'Please enter a valid email address' : undefined;
  }

  return (
    <Card>
      <CardHeader style={{ cursor: 'pointer' }} onClick={() => setIsOpen(!isOpen)}>
        Retention Actions Configuration
        <FontAwesomeIcon className="ml-1" icon={isOpen ? faChevronDown : faChevronRight} />
      </CardHeader>
      <Collapse className="p-4" style={{ backgroundColor: '#fafbfc' }} isOpen={isOpen}>
        <Row>
          <Col xs={12} sm={12} md={4} lg={4}>
            <FormGroup>
              <Label for="discountRecurringCycleLimitOnCancellation" className="text-nowrap">Recurring Cycle Limit On Cancellation Discount</Label>
              <Field name="discountRecurringCycleLimitOnCancellation" component="input" type="number" className="form-control" />
            </FormGroup>
          </Col>
          <Col xs={12} sm={12} md={4} lg={4}>
            <FormGroup>
              <Label for="pauseDurationCycle" className="d-flex align-items-center text-nowrap" style={{ gap: '.15rem' }}>
                Number of Cycles to Pause For on Pause
                <HelpTooltip>If set to 0, subscription will pause indefinitely</HelpTooltip>
              </Label>
              <Field name="pauseDurationCycle" validate={validateDurationCycle}>
                {({ input, meta }) => (
                  <div>
                    <Input {...input} id="pauseDurationCycle" type="number" placeholder="0" invalid={meta.error && meta.touched} />
                    {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
                  </div>
                )}
              </Field>
            </FormGroup>
          </Col>
        </Row>
        <FormGroup className="d-flex align-items-start" style={{ gap: '1.5rem', flexWrap: 'wrap' }}>
          <div style={{ padding: '9px 0', minWidth: '300px' }}>
            <Label for="enableDiscountEmail" className="d-block">
              Receive an email when customer chooses discount?
            </Label>
            <Field name="enableDiscountEmail" id="enableDiscountEmail" initialValue={false}>
              {({ input }) => (
                <Switch
                  checked={Boolean(input.value)}
                  onChange={input.onChange}
                  onColor="#86d3ff"
                  onHandleColor="#2693e6"
                  handleDiameter={20}
                  uncheckedIcon={false}
                  checkedIcon={false}
                  height={18}
                  width={36}
                  boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                  activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                  className="my-1"
                />
              )}
            </Field>
          </div>
          {values?.enableDiscountEmail && (
            <div style={{ ...insetStyling, padding: '9px 18px' }}>
              <Label for="discountEmailAddress">Email Address</Label>
              <Field name="discountEmailAddress" id="discountEmailAddress" validate={validateEmailAddress}>
                {({ input, meta }) => (
                  <>
                    <Input {...input} invalid={meta?.error && meta?.touched} style={{ minWidth: '300px' }} />
                    {meta?.error && meta?.touched && <span className="invalid-feedback">{meta.error}</span>}
                  </>
                )}
              </Field>
            </div>
          )}
        </FormGroup>
        <div className="mt-4 mb-3">
          <h5 className="text-dark">Action Messages</h5>
        </div>
        <FormGroup>
          <Label for="discountMessageOnCancellation" className="d-flex align-items-center" style={{ gap: '.15rem ' }}>
            Discount Message On Cancellation
            <HelpTooltip>
              <p>
                Use {'{{discountAmount}}'}, {'{{cycleLimit}}'}, and {'{{cycleDuration}}'} to automatically fill in information about the
                offered discount.
              </p>
              <p>For example: For a monthly subscription and an offered 25% discount with a cycle limit of 3, the text:</p>
              <p>"{'Get a {{discountAmount}}% discount for {{cycleLimit}} {{cycleDuration}}.'}"</p>
              <p>will become:</p>
              <p>"Get a 25% discount for 3 months."</p>
            </HelpTooltip>
          </Label>
          <Field name="discountMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="swapMessageOnCancellation">Swap Message On Cancellation</Label>
          <Field name="swapMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="giftMessageOnCancellation">Gift Message On Cancellation</Label>
          <Field name="giftMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="skipMessageOnCancellation">Skip Message On Cancellation</Label>
          <Field name="skipMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="pauseMessageOnCancellation" className="d-flex align-items-center" style={{ gap: '.15rem ' }}>
            Pause Message On Cancellation
            <HelpTooltip>
              <p>
                Use {'{{cycleAmount}}'}, and {'{{cycleDuration}}'} to automatically fill in information about the offered discount.
              </p>
              <p>For example: For a monthly subscription that would be paused for 3 cycles, the text:</p>
              <p>"{'Would you like to pause your subscription for {{cycleAmount}} {{cycleDuration}} instead?'}"</p>
              <p>will become:</p>
              <p>"Would you like to pause your subscription for 3 months instead?"</p>
            </HelpTooltip>
          </Label>
          <Field name="pauseMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="changeDateMessageOnCancellation">Change Date Message On Cancellation</Label>
          <Field name="changeDateMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="changeAddressMessageOnCancellation">Change Address Message On Cancellation</Label>
          <Field name="changeAddressMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
        <FormGroup>
          <Label for="updateFrequencyMessageOnCancellation">Update Frequency Message On Cancellation</Label>
          <Field name="updateFrequencyMessageOnCancellation" component="input" type="text" className="form-control" />
        </FormGroup>
      </Collapse>
    </Card>
  );
};

export default RetentionActionsConfiguration;
