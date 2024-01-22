import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { Input, Label, FormGroup, InputGroup, InputGroupAddon } from 'reactstrap';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import { PlanChildProps} from '../../CreateSubscriptionGroupPlans';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';

const OrderFrequency = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();

  function handleIntervalOnChange(frequencyInterval: FrequencyIntervalUnit) {
    const defaultSpecificDayValue = planFields.specificDayEnabled && frequencyInterval === FrequencyIntervalUnit.WEEK ? 1 : null;

    update('subscriptionPlans', index, {
      ...planFields,
      billingFrequencyInterval: frequencyInterval,
      frequencyType: 'ON_PURCHASE_DAY',
      specificDayEnabled: false,
      specificDayValue: defaultSpecificDayValue,
      specificMonthValue: null,
      cutOff: null,
    });
  }

  function validateFrequencyCount(value: string, allValues: ISubscriptionGroup) {
    const type = allValues.subscriptionPlans[index].planType === OrderBillingType.PAY_AS_YOU_GO ? 'Order' : 'Delivery/Fulfillment';
    if (!value || isNaN(parseInt(value))) {
      return `Please provide a valid value for the ${type} frequency.`;
    } else if (parseInt(value) <= 0) {
      return `${type} frequency cannot be less than 1.`;
    }
    return undefined;
  }

  function validateFrequencyInterval(value: string) {
    if (!value) {
      return 'Please provide a valid value for the frequency.';
    }
    return undefined;
  }

  return (
    <FormGroup>
      <OnChange name={`${name}.frequencyInterval`} children={handleIntervalOnChange} />
      <Label for={`${name}.frequencyCount`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
        {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO ? 'Order Fulfillment Frequency' : 'Order Frequency'}
        <HelpTooltip maxWidth={500}>
          {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO ? (
            <>
              <p>
                The time gap between two fulfillments within the subscription prepaid order. The number should be a minimum of 1, and the
                timeline can be days/weeks/months/years.{' '}
              </p>
              <p className="mb-0">
                For instance, if you have mentioned fulfillment frequency as 1 month (and billing frequency is 12 months), the
                product/service needs to be fulfilled every month until the end billing frequency that is 12 months.
              </p>
            </>
          ) : (
            <>
              <p>
                The time gap between two order deliveries within the subscription plan. The number should be a minimum of 1, and the
                timeline can be days/weeks/months/years.
              </p>
              <p className="mb-0">
                For instance, if we have a subscription plan setup for 1 week and the first order is placed today, the next order will be
                automatically placed exactly after a week. The cycle will continue every week.
              </p>
            </>
          )}
        </HelpTooltip>
      </Label>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
        <Field
          name={`${name}.frequencyCount`}
          id={`${name}.frequencyCount`}
          type="number"
          initialValue={null}
          validate={validateFrequencyCount}
        >
          {({ input, meta }) => (
            <InputGroup style={{ flexGrow: 1, width: '50%' }}>
              <InputGroupAddon addonType='prepend'>Every</InputGroupAddon>
              <Input {...input} className="" type="number" invalid={meta.error && meta.touched} />
              {meta.error && meta.touched && (
                <span style={{ order: 4, width: '100%' }} className="d-inline-block invalid-feedback">
                  {meta.error}
                </span>
              )}
            </InputGroup>
          )}
        </Field>
        <Field
          id={`${name}.frequencyInterval`}
          name={`${name}.frequencyInterval`}
          className="form-control"
          initialValue={FrequencyIntervalUnit.MONTH}
          validate={validateFrequencyInterval}
        >
          {({ input, meta }) => {
            const pluralEnd = planFields.frequencyCount != 1 ? 's' : '';

            return (
              <Input
                {...input}
                type="select"
                invalid={meta.error && meta.touched}
                style={{
                  flexGrow: 1,
                  maxWidth: '200px',
                }}
              >
                <option value={FrequencyIntervalUnit.DAY}>Day{pluralEnd}</option>
                <option value={FrequencyIntervalUnit.WEEK}>Week{pluralEnd}</option>
                <option value={FrequencyIntervalUnit.MONTH}>Month{pluralEnd}</option>
                <option value={FrequencyIntervalUnit.YEAR}>Year{pluralEnd}</option>
              </Input>
            );
          }}
        </Field>
      </div>
    </FormGroup>
  );
};

export default OrderFrequency;
