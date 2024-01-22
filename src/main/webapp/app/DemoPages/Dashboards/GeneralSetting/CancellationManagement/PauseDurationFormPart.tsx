import React, { Fragment } from 'react';
import { FormGroup, Input, Label } from 'reactstrap';
import { Field } from 'react-final-form';

const PauseDurationFormPart = () => {
  const validateDurationCycle = (durationInput: string) => {
    if (!durationInput) {
      return 'Please enter a valid number';
    }

    if (parseInt(durationInput) == NaN || parseFloat(durationInput) % 1 !== 0 || parseInt(durationInput) < 1) {
      return 'Please enter a whole number equal to 1 or higher';
    }
  };

  const validateInstructions = (instructionInput: string) => {
    if (!instructionInput || !instructionInput.trim()) {
      return 'Please provide cancellation instructions';
    }
  };

  return (
    <Fragment>
      <h5>Provide Pause Option Settings</h5>
      <hr />
      <FormGroup>
        <Label for="pauseDurationCycle">Number of Cycles to Pause For:</Label>
        <Field name="pauseDurationCycle" validate={validateDurationCycle}>
          {({ input, meta }) => (
            <div>
              <Input
                {...input}
                id="pauseDurationCycle"
                className="form-control col-sm-2"
                type="number"
                placeholder="0"
                invalid={meta.error && meta.touched}
              />
              {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
            </div>
          )}
        </Field>
      </FormGroup>
      <FormGroup>
        <Label for="pauseInstructionsText">Pause Instructions (Text or HTML)</Label>
        <Field name="pauseInstructionsText" validate={validateInstructions}>
          {({ input, meta }) => (
            <div>
              <Input
                {...input}
                className="form-control"
                type="textarea"
                rows={8}
                placeholder="Please enter your instructions here..."
                invalid={meta.error && meta.touched}
              />
              {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
            </div>
          )}
        </Field>
      </FormGroup>
      <FormGroup>
        <Label for="subscriptionIsStillPausedText">Subscription Is Still Paused Message</Label>
        <Field name="subscriptionIsStillPausedText" component="input" className="form-control" />
      </FormGroup>
    </Fragment>
  );
};

export default PauseDurationFormPart;
