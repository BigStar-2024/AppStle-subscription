import React from 'react';
import { Field } from 'react-final-form';
import { FormGroup, Input, InputGroup, Label } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import { DiscountTypeUnit } from 'app/shared/model/enumerations/discount-type-unit.model';

const DiscountSettings = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  function validateDiscountAmount(value: string | number, allValues: ISubscriptionGroup) {
    const freeTrialEnabled = allValues.subscriptionPlans[index].freeTrialEnabled;

    if (freeTrialEnabled) {
      return undefined;
    }

    if (value == null) {
      return 'Please provide a valid value for discount.';
    }

    if (isNaN(parseFloat(value.toString())) || parseFloat(value.toString()) < 0) {
      return 'Please provide a valid value for discount.';
    }

    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.discountOffer`}>Subscription Discount Offer (Subscribe and Save)</Label>
      <div className="d-flex" style={{ gap: '1rem' }}>
        <Field
          name={`${name}.discountOffer`}
          id={`${name}.discountOffer`}
          type="number"
          initialValue={''}
          validate={validateDiscountAmount}
          className="form-control"
        >
          {({ input, meta }) => (
            <InputGroup style={{ flexGrow: 1, width: '50%' }}>
              <Input {...input} invalid={meta.error && meta.touched} disabled={planFields.freeTrialEnabled} />
              {meta.error && meta.touched && <div className="d-inline-block invalid-feedback">{meta.error}</div>}
            </InputGroup>
          )}
        </Field>
        <Field
          name={`${name}.discountType`}
          id={`${name}.discountType`}
          type="select"
          initialValue={DiscountTypeUnit.PERCENTAGE}
          className="form-control"
        >
          {({ input }) => (
            <Input {...input} disabled={planFields.freeTrialEnabled} style={{ maxWidth: '200px', flexGrow: 1 }}>
              <option value={DiscountTypeUnit.PERCENTAGE}>Percent off(%)</option>
              <option value={DiscountTypeUnit.FIXED}>Amount off</option>
              <option value={DiscountTypeUnit.PRICE}>Fixed Price</option>
            </Input>
          )}
        </Field>
      </div>
    </FormGroup>
  );
};

export default DiscountSettings;
