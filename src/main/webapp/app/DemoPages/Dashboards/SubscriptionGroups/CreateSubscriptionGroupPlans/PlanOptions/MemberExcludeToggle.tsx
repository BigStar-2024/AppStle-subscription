import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { FormGroup, Label } from 'reactstrap';
import Switch from 'react-switch';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';

const MemberExcludeToggle = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();

  function handleOnChange(nonMemberOnly: boolean) {
    update('subscriptionPlans', index, {
      ...planFields,
      memberOnly: nonMemberOnly ? false : planFields.memberOnly,
      memberExclusiveTags: !nonMemberOnly ? null : planFields.memberExclusiveTags,
    });
  };

  return (
    <FormGroup>
      <OnChange name={`${name}.nonMemberOnly`} children={handleOnChange}/>
      <Field name={`${name}.nonMemberOnly`} initialValue={false}>
        {({ input }) => {
          return (
            <div className="d-flex align-items-center">
              <Label className="d-flex align-items-center mb-0" style={{ gap: '0.25rem' }}>
                <strong>Exclude Specific Tags</strong>
                <HelpTooltip>
                  <p>Customers with these tags cannot view or purchase this subscription.</p>
                  <p className="mb-0">Example: A "Silver tier" product that "Bronze tier" customers cannot purchase</p>
                </HelpTooltip>
              </Label>
              <Switch
                id={`${name}.nonMemberOnly`}
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
          );
        }}
      </Field>
    </FormGroup>
  );
};

export default MemberExcludeToggle;
