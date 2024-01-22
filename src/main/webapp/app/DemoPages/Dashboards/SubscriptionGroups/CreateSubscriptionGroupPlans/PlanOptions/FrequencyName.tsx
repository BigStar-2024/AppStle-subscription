import React from 'react';
import { Field } from 'react-final-form';
import { Input, Label, FormGroup } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';

const FrequencyName = ({ name, index }: PlanChildProps) => {
  function validateOrderFrequencyName(value: string, allValues: ISubscriptionGroup) {
    if (!value) {
      return 'Please provide Order Frequency Name.';
    } else {
      if (allValues['subscriptionPlans']?.length === 1) {
        return undefined;
      }
      allValues.subscriptionPlans.forEach((plan, i) => {
        if (
          plan?.frequencyName
            ?.toLowerCase()
            .split(' ')
            .join('') ===
            value
              ?.toLowerCase()
              .split(' ')
              .join('') &&
          i !== index
        ) {
          return `Order Frequency Name should be unique - You cannot use same Order Frequency Name of "${value}" on multiple subscription plans.`;
        }
      });
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Field
        name={`${name}.frequencyName`}
        id={`${name}.frequencyName`}
        type="text"
        className="form-control"
        initialValue=""
        validate={validateOrderFrequencyName}
      >
        {({ input, meta }) => (
          <>
            <Label for={`${name}.frequencyName`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              Order Frequency Name
              <HelpTooltip>
                Order frequency refers to the delivery or fulfillment frequency. This is the name which your customer will see on the
                product details page. It could be a common name such as ‘Weekly’ or ‘Monthly’, or a more customized name.
              </HelpTooltip>
            </Label>

            <Input {...input} placeholder="e.g. 'Monthly' or 'Weekly'" invalid={meta.error && meta.touched} />
            {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default FrequencyName;
