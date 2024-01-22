import React, { useState } from 'react';
import _ from 'lodash';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { FormGroup, Label } from 'reactstrap';
import Switch from 'react-switch';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import {  SubscriptionPlanCustomField } from 'app/shared/model/subscription-group.model';

const CustomFieldsToggle = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const [prevCustomFields, setPrevCustomFields] = useState<SubscriptionPlanCustomField[]>(planFields.formFieldJsonArray);

  const {
    mutators: { update },
  } = useForm();

  function handleJsonToggleOnChange(formFieldJsonToggle: boolean) {
    if (formFieldJsonToggle) {
      if (!_.isEqual(planFields.formFieldJsonArray, prevCustomFields)) {
        setPrevCustomFields(planFields.formFieldJsonArray);
      }
    } else {
      setPrevCustomFields(planFields.formFieldJsonArray);
    }

    update('subscriptionPlans', index, {
      ...planFields,
      formFieldJsonArray: formFieldJsonToggle ? prevCustomFields : null,
    });
  }

  return (
    <FormGroup>
      <OnChange name={`${name}.formFieldJsonToggle`} children={handleJsonToggleOnChange} />
      <Field name={`${name}.formFieldJsonToggle`}>
        {({ input }) => (
          <div className="d-flex align-items-center">
            <Label>
              <strong>Add custom advanced fields</strong>
            </Label>
            <Switch
              id={`${name}.formFieldJsonToggle`}
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
              className="ml-2"
            />
          </div>
        )}
      </Field>
    </FormGroup>
  );
};

export default CustomFieldsToggle;
