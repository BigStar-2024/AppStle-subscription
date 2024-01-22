import React from 'react';
import { Field } from 'react-final-form';
import { Input, Label, FormGroup, InputGroup, InputGroupText, InputGroupAddon } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';

const BillingFrequency = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  function validateBillingCount(value: string, allValues: ISubscriptionGroup) {
    if (value) {
      const frequencyCount = allValues.subscriptionPlans[index].frequencyCount;
      if (frequencyCount && frequencyCount >= parseInt(value)) {
        return 'Prepaid orders must bill more than 1 upcoming order.';
      }

      if (frequencyCount && parseInt(value) % frequencyCount !== 0) {
        return 'Billing Period duration must be a multiple of the delivery frequency duration.';
      }
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Field
        name={`${name}.billingFrequencyCount`}
        id={`${name}.billingFrequencyCount`}
        type="number"
        initialValue={null}
        validate={validateBillingCount}
      >
        {({ input, meta }) => (
          <>
            <Label for={`${name}.billingFrequencyCount`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              Billing Frequency
              <HelpTooltip>
                <p>This is the time period for which the customer will pay in advance.</p>
                <p className="mb-0">
                  For example, if you set the delivery frequency as 1 week, and the billing period as 12 weeks, your customers will be
                  charged for 12 weeks, and receive the deliveries once a week.
                </p>
              </HelpTooltip>
            </Label>
            <InputGroup>
              <InputGroupAddon addonType='prepend'>Every</InputGroupAddon>
              <Input {...input} className="" type="number" invalid={meta.error && meta.touched} />
              <InputGroupAddon addonType='append'>
                <Field
                  id={`${name}.billingFrequencyInterval`}
                  name={`${name}.billingFrequencyInterval`}
                  initialValue={FrequencyIntervalUnit.MONTH}
                >
                  {({ input }) => (
                    <InputGroupText className="text-capitalize w-100 d-flex align-items-center" style={{ gap: '.25rem', maxWidth: '200px', flexGrow: 1, marginLeft: '1rem'  }}>
                      {`${input.value.toLowerCase()}${planFields.billingFrequencyCount != 1 ? 's' : ''}`}
                      <HelpTooltip>
                        <p>The billing interval must be the same as the order fulfillment interval.</p>
                        <p className="font-weight-bold mb-0">
                          For example, if the interval of delivery frequency is in ‘weeks’, then the interval of billing period will also be
                          in ‘weeks’.
                        </p>
                      </HelpTooltip>
                    </InputGroupText>
                  )}
                </Field>
                {meta.error && (
                  <span style={{ order: 4, width: '100%' }} className="d-inline-block invalid-feedback">
                    {meta.error}
                  </span>
                )}
              </InputGroupAddon>
            </InputGroup>
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default BillingFrequency;
