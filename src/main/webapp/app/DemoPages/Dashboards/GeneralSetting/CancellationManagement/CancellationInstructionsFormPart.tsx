import React, { Fragment } from 'react';
import { FormGroup, Input } from 'reactstrap';
import { Field } from 'react-final-form';

const CancellationInstructionsFormPart = () => {
  const validateInstructions = (instructionInput: string) => {
    if (!instructionInput || !instructionInput.trim()) {
      return 'Please provide cancellation instructions';
    }
  };

  return (
    <Fragment>
      <h5>Provide Cancellation Instructions (Text or HTML)</h5>
      <hr />
      <FormGroup>
        <Field name="cancellationInstructionsText" validate={validateInstructions}>
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
    </Fragment>
  );
};

export default CancellationInstructionsFormPart;
