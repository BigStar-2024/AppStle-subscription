import React from 'react';
import { FormGroup, Input, Label } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { Field } from 'react-final-form';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';

const MinimumCycles = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  function validateMinimumCycles(value: string, allValues: ISubscriptionGroup) {
    if (value != null) {
      if (isNaN(parseInt(value)) || parseInt(value) <= 0) {
        return 'Please provide a valid value for min cycle.';
      }

      if (!!allValues.subscriptionPlans[index].maxCycles && parseInt(value) > allValues.subscriptionPlans[index].maxCycles) {
        return 'Min recurring period must be less than or equal the max cycle period';
      }
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.minCycles`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
        <b>Minimum Number</b> of {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO ? 'Billing Iterations' : 'Orders'}
        <HelpTooltip>
          {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO
            ? 'Minimum number of billing iteration you want to bind your customers with, before they can cancel their subscription. Default value is one (the very first billing iteration).'
            : 'Minimum number of orders you want to bind your customers with, before they can cancel their subscription. Default value is one (the very first order).'}
        </HelpTooltip>
      </Label>
      <Field
        name={`${name}.minCycles`}
        id={`${name}.minCycles`}
        type="number"
        initialValue={null}
        className="form-control"
        validate={validateMinimumCycles}
      >
        {({ input, meta }) => (
          <>
            <Input {...input} invalid={meta.error && meta.touched} />
            {meta.error && meta.touched && <div className="invalid-feedback d-inline-block">{meta.error}</div>}
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default MinimumCycles;
