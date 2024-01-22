import React from 'react';
import { FormGroup, Label, Input } from 'reactstrap';
import { Field } from 'react-final-form';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const UpcomingOrderEmailBuffer = ({ name }: PlanChildProps) => {
  return (
    <FormGroup>
      <Label for={`${name}.upcomingOrderEmailBuffer`}>Upcoming Order Email Buffer</Label>
      <Field
        name={`${name}.upcomingOrderEmailBuffer`}
        id={`${name}.upcomingOrderEmailBuffer`}
        type="select"
        initialValue={null}
        className="form-control"
      >
        {({ input, meta }) => (
          <Input {...input} invalid={meta.error && meta.touched} style={{ flexGrow: 1 }}>
            <option key={-1} value={null}>
              Select Upcoming Order Email Buffer
            </option>
            {new Array(15).fill(undefined).map((_, index) => (
              <option key={index} value={index + 1}>
                {index + 1}
              </option>
            ))}
          </Input>
        )}
      </Field>
    </FormGroup>
  );
};

export default UpcomingOrderEmailBuffer;
