import React from 'react';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { FormGroup, Input, Label } from 'reactstrap';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { Field } from 'react-final-form';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';

const MaximumCycles = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  function validateMaximumValues(value: string, allValues: ISubscriptionGroup) {
    if (value != null) {
      if (isNaN(parseInt(value)) || parseInt(value) <= 0) {
        return 'Please provide a valid value for max cycle.';
      }

      if (!!allValues.subscriptionPlans[index].minCycles && parseInt(value) < allValues.subscriptionPlans[index].minCycles) {
        return 'Max recurring period must be equal or greater than the min cycle period';
      }
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.maxCycles`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
        <b>Maximum Number</b> of {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO ? 'Billing Iterations' : 'Orders'}
        <HelpTooltip>
          {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO
            ? "The customer's subscription will pause automatically if a maximum billing cycle limit is set and that limit has been reached."
            : "The customer's subscription will pause automatically if a maximum billing cycle limit is set and that limit has been reached."}
        </HelpTooltip>
      </Label>
      <Field
        key={`${name}.maxCycles.${planFields.maxCycles}`}
        name={`${name}.maxCycles`}
        id={`${name}.maxCycles`}
        type="number"
        initialValue={null}
        className="form-control"
        validate={validateMaximumValues}
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

export default MaximumCycles;
