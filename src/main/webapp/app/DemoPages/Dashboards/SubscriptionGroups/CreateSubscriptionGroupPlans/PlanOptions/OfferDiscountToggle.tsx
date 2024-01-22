import React from 'react';
import { Field } from 'react-final-form';
import Switch from 'react-switch';
import { Label } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const OfferDiscount = ({ name }: PlanChildProps) => (
  <Field name={`${name}.discountEnabled`} initialValue={false}>
    {({ input }) => {
      return (
        <div className="d-flex align-items-center">
          <Label>
            <strong>Offer Discount For Subscription?</strong>
          </Label>
          <Switch
            checked={input.value}
            onChange={input.onChange}
            onColor="#86d3ff"
            onHandleColor="#2693e6"
            handleDiameter={20}
            uncheckedIcon={false}
            checkedIcon={false}
            height={17}
            width={36}
            boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
            activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
            className="ml-2 mb-2"
          />
        </div>
      );
    }}
  </Field>
);

export default OfferDiscount;
