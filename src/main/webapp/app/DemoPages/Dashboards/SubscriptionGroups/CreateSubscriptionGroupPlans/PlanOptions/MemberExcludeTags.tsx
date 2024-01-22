import React from 'react';
import { Field } from 'react-final-form';
import { FormGroup, Label } from 'reactstrap';
import Creatable from 'react-select/creatable';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

type OptionType = {
  label?: string;
  value?: string;
}

const MemberExcludeTags = ({ name }: PlanChildProps) => {
  function convertTagStringToArray(value: string | undefined): OptionType[] {
    return value ? value.split(',').map(tag => ({ label: tag, value: tag })) : [];
  }

  function convertArrayToTagString(createdValue: OptionType[]) {
    return createdValue.map(option => option.value).join(',');
  }

  return (
    <FormGroup>
      <Label for={`${name}.memberExclusiveTags`}>
        <strong>Member Exclusive Tags</strong>
      </Label>
      <Field
        name={`${name}.memberExclusiveTags`}
        initialValue={null}
        validate={value => {
          return !value ? 'Please provide atleast one exclusive tag' : undefined;
        }}
      >
        {({ input, meta }) => (
          <>
            <Creatable
              id={input.name}
              value={convertTagStringToArray(input.value)}
              onChange={(createdValue: OptionType[]) => input.onChange(convertArrayToTagString(createdValue))}
              placeholder="Add tags..."
              isMulti
            />
            {meta.error && meta.touched && <div className="invalid-feedback d-block">{meta.error}</div>}
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default MemberExcludeTags;
