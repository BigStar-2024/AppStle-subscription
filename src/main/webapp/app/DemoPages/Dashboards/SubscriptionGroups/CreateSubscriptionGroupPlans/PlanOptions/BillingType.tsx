import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { Input, Label, FormGroup } from 'reactstrap';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const BillingType = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();


  return (
    <FormGroup>
      <Field
        name={`${name}.planType`}
        id={`${name}.planType`}
        className="form-control"
        initialValue={OrderBillingType.PAY_AS_YOU_GO}
      >
        {({ input, meta }) => (
          <>
            <Label for={`${name}.planType`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              Order Billing Type
              <HelpTooltip>
                <div style={{ padding: '4px' }}>
                  <p style={{ textAlign: 'center', fontWeight: 'bold' }}>We support 3 kind of subscription billing plans:</p>
                  <hr style={{ borderColor: '#fff' }} />
                  <p>
                    <b>1. Pay As You Go:</b> This plan type charges the customer just for the 'immediately next' recurring order or
                    delivery.
                  </p>
                  <p>
                    <b>2. Prepaid One-time Plan:</b> This plan type allows you to receive the payment for multiple periods of future orders
                    at the same time.
                  </p>
                  <p>
                    Please note that in ‘Prepaid One-time Plan’, the contract pauses at the end of the billing cycle, and needs to be
                    manually renewed by the customer.
                  </p>
                  <p className="mb-0">
                    <b>3. Prepaid Auto-renew:</b> This plan is very similar to the Prepaid One-time Plan, except that the contract does not
                    pause at the end of the billing cycle, and auto renews, until the customer expressly cancels the subscription.
                  </p>
                </div>
              </HelpTooltip>
            </Label>
            <Input {...input} type="select" invalid={meta.error && meta.touched}>
              <option value={OrderBillingType.PAY_AS_YOU_GO}>Pay As You Go (Auto-renew)</option>
              <option value={OrderBillingType.PREPAID}>Prepaid One-Time</option>
              <option value={OrderBillingType.ADVANCED_PREPAID}>Prepaid Auto-renew</option>
            </Input>
            {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default BillingType;
