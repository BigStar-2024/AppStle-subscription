import React from 'react';
import { Input, InputGroup, InputGroupText, Label, FormGroup, InputGroupAddon } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { Field } from 'react-final-form';

const CutOff = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  function validateCutOff(value: string) {
    const parsedValue = parseInt(value);
    if (!value) {
      return undefined;
    } else if (isNaN(parsedValue)) {
      return `Cutoff field value should be a number`;
    } else if (value) {
      if (
        planFields.specificDayEnabled &&
        planFields.frequencyInterval === FrequencyIntervalUnit.MONTH &&
        (parsedValue > planFields.frequencyCount * 31 || parsedValue < 1)
      ) {
        return `Cutoff cannot be greater than ${planFields.frequencyCount * 31} days or less than 1`;
      }
      if (
        planFields.specificDayEnabled &&
        planFields.frequencyInterval === FrequencyIntervalUnit.WEEK &&
        (parsedValue > planFields.frequencyCount * 7 || parsedValue < 1)
      ) {
        return `Cutoff cannot be greater than ${planFields.frequencyCount * 7} days or less than 1`;
      }
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Field name={`${name}.cutOff`} id={`${name}.cutOff`} className="form-control" validate={validateCutOff}>
        {({ input, meta }) => (
          <>
            <Label for={`${name}.cutOff`} className="d-flex align-items-center" style={{ gap: '.25rem' }}>
              Cutoff
              <HelpTooltip>
                The cutoff indicates how many days in advance the new order would need to be placed in order to qualify for the upcoming
                order cycle.
              </HelpTooltip>
            </Label>
            <InputGroup>
              <Input {...input} type="number" invalid={meta.error && meta.touched} />
              <InputGroupAddon addonType='append'>{`Day${parseInt(input.value) !== 1 ? 's' : ''}`} before order day</InputGroupAddon>
              {meta.error && meta.touched && <div className="invalid-feedback d-inline-block">{meta.error}</div>}
            </InputGroup>
          </>
        )}
      </Field>
    </FormGroup>
  );
};

export default CutOff;
