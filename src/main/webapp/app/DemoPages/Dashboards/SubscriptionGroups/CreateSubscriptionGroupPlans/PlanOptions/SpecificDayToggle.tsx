import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import Switch from 'react-switch';
import { Input, Label, FormGroup } from 'reactstrap';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';

const SpecificDayToggle = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();

  function handleOnChange(specificDayEnabled: boolean) {
    const defaultSpecificDayValue = planFields.specificDayEnabled && planFields.frequencyInterval === FrequencyIntervalUnit.WEEK ? 1 : null;

    if (planFields.specificDayEnabled) {
      update('subscriptionPlans', index, {
        ...planFields,
        specificDayEnabled,
        frequencyType: 'ON_SPECIFIC_DAY',
        specificDayValue: planFields.specificDayValue ?? defaultSpecificDayValue,
      });
    } else {
      update('subscriptionPlans', index, {
        ...planFields,
        specificDayEnabled,
        frequencyType: 'ON_PURCHASE_DAY',
        specificDayValue: null,
        specificMonthValue: null,
        cutOff: null,
      });
    }
  }

  return (
    <FormGroup>
      <OnChange name={`${name}.specificDayEnabled`} children={handleOnChange} />
      <Field name={`${name}.specificDayEnabled`} initialValue={false}>
        {({ input }) => {
          return (
            <div className="d-flex align-item-center mt-3">
              <Label for={`${name}.specificDayEnabled`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
                <strong>
                  {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO ? 'Set specific billing days?' : 'Set specific order days?'}
                </strong>
                <HelpTooltip>
                  {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO
                    ? 'The date pre-set by the merchant, on which the prepaid plan will be renewed.'
                    : 'The date pre-set by the merchant, on which the orders will be placed.'}
                </HelpTooltip>
              </Label>
              <Switch
                checked={input.value}
                onChange={input.onChange}
                onColor="#86d3ff"
                onHandleColor="#2693e6"
                handleDiameter={20}
                uncheckedIcon={false}
                checkedIcon={false}
                boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                height={17}
                width={36}
                className="ml-2 mb-2"
              />
            </div>
          );
        }}
      </Field>
      <Field
        name={`${name}.frequencyType`}
        id={`${name}.frequencyType`}
        type="hidden"
        initialValue="ON_PURCHASE_DAY"
        render={({ input }) => <Input {...input} />}
      />
    </FormGroup>
  );
};

export default SpecificDayToggle;
