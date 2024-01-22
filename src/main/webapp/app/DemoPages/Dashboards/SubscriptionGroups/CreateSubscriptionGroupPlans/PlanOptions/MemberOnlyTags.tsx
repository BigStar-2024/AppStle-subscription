import React from 'react';
import { Field } from 'react-final-form';
import  Creatable from 'react-select/creatable';
import { FormGroup, Label } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

type OptionType = {
  label?: string;
  value?: string;
}

const MemberOnlyTags = ({ name }: PlanChildProps) => {
  function convertTagStringToArray(value: string | undefined): OptionType[] {
    return value ? value.split(',').map(tag => ({ label: tag, value: tag })) : [];
  }

  function convertArrayToTagString(createdValue: OptionType[]) {
    return createdValue.map(option => option.value).join(',');
  }

  return (
    <FormGroup>
      <Label for={`${name}.memberInclusiveTags`}>
        <strong>
          Member Inclusive Tags <span className="text-muted">(Optional)</span>
        </strong>
      </Label>
      <Field name={`${name}.memberInclusiveTags`} initialValue={null}>
        {({ input }) => (
          <Creatable
            id={input.name}
            value={convertTagStringToArray(input.value)}
            onChange={(createdValue: OptionType[]) => input.onChange(convertArrayToTagString(createdValue))}
            placeholder="Add tags..."
            isMulti
          />
        )}
      </Field>
    </FormGroup>
  );
};

export default MemberOnlyTags;
