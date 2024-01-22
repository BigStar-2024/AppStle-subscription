import React from 'react';
import { Field, useForm } from 'react-final-form';
import { OnChange } from 'react-final-form-listeners';
import { FormGroup, Label } from 'reactstrap';
import Switch from 'react-switch';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const MemberOnlyToggle = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const {
    mutators: { update },
  } = useForm();

  function handleOnChange(memberOnly: boolean) {
    update('subscriptionPlans', index, {
      ...planFields,
      nonMemberOnly: memberOnly ? false : planFields.nonMemberOnly,
      memberInclusiveTags: !memberOnly ? null : planFields.memberInclusiveTags,
    });
  }

  return (
    <FormGroup>
      <OnChange name={`${name}.memberOnly`} children={handleOnChange}/>
      <Field name={`${name}.memberOnly`} initialValue={false}>
        {({ input }) => {
          return (
            <div className="d-flex align-items-center">
              <Label className="d-flex align-items-center mb-0" style={{ gap: '0.25rem' }}>
                <strong>Require Specific Tags</strong>
                <HelpTooltip>
                  <p>Customers must have this tag(s) in order to purchase this subscription.</p>
                  <p className="mb-0">Example: "Platinum Member" tag</p>
                </HelpTooltip>
              </Label>
              <Switch
                id={`${name}.memberOnly`}
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

export default MemberOnlyToggle;
