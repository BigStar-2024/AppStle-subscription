import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { Label, FormGroup } from 'reactstrap';
import Switch from 'react-switch';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const OfferTrialToggle = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();

  function handleOnChange(freeTrialEnabled: boolean) {
    update('subscriptionPlans', index, {
      ...planFields,
      discountEnabled2: freeTrialEnabled,
    });
  }

  return (
    <FormGroup>
      <OnChange name={`${name}.freeTrialEnabled`} children={handleOnChange} />
      <Field name={`${name}.freeTrialEnabled`} initialValue={false}>
        {({ input }) => (
          <div className="d-flex align-items-center mb-3">
            <Label for={`${name}.freeTrialEnabled`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              <strong style={{ whiteSpace: "nowrap" }}>Offer Trial?</strong>
              <HelpTooltip>
                <p>
                  Enabling trial will allow the customer to order product in this plan at discounted cost or for free on their first order.
                </p>
                <p className="mb-0">
                  Appstle will bill the subscription immediately after the specified trial period. All other plan settings will apply.
                </p>
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
              disabled={planFields.discountEnabled2Masked}
              id={`${name}.freeTrialEnabled`}
            />

            <span className="d-block ml-3 mb-2 text-muted" style={{ fontSize: '.7rem' }}>
              <strong>Please note</strong>: Once this setting has been turned on, you cannot turn it off due to Shopify settings.
            </span>
          </div>
        )}
      </Field>
    </FormGroup>
  );
};

export default OfferTrialToggle;
