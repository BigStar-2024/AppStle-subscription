import React from 'react';
import { Field } from 'react-final-form';
import { Input, Label, FormGroup } from 'reactstrap';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const FrequencyDescription = ({ name }: PlanChildProps) => {
  return (
    <FormGroup>
      <Field name={`${name}.frequencyDescription`} id={`${name}.frequencyDescription`} type="text" className="form-control" initialValue="">
        {({ input, meta }) => (
          <>
            <Label for={`${name}.frequencyDescription`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              Order Frequency Description
              <HelpTooltip>
                <p>This is the description which your customer will see on the product details page.</p>
                <p className="mb-0">
                  For example, A weekly Pay-As-You-Go plan can be described as: "Customer will get delivery every week and will be billed
                  every week."
                </p>
              </HelpTooltip>
            </Label>
            <Input {...input} invalid={meta.error && meta.touched ? true : null} />
            {meta.error && (
              <div className="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                {meta.error}
              </div>
            )}
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default FrequencyDescription;
