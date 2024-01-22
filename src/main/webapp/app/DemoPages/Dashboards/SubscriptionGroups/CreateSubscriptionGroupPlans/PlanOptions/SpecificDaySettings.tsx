import React from 'react';
import _ from 'lodash';
import { Field, useFormState } from 'react-final-form';
import { Input, InputGroup, Label, FormGroup, InputGroupAddon } from 'reactstrap';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';

const SpecificMonth = ({ name }: PlanChildProps) => (
  <Field
    name={`${name}.specificMonthValue`}
    id="specificMonthValue"
    initialValue="1"
    className="form-control"
    validate={value => (!value ? 'Please select a month' : undefined)}
  >
    {({ input, meta }) => (
      <Input
        {...input}
        type="select"
        invalid={meta.error && meta.touched}
        style={{
          flexGrow: 1,
        }}
      >
        <option value={''}>Select Month</option>
        <option value={1}>January</option>
        <option value={2}>February</option>
        <option value={3}>March</option>
        <option value={4}>April</option>
        <option value={5}>May</option>
        <option value={6}>June</option>
        <option value={7}>July</option>
        <option value={8}>August</option>
        <option value={9}>September</option>
        <option value={10}>October</option>
        <option value={11}>November</option>
        <option value={12}>December</option>
      </Input>
    )}
  </Field>
);

const SpecificDayErrors = ({ name, index }: { name: string; index: number }) => {
  const { errors, touched } = useFormState();

  const dayError = !!errors?.subscriptionPlans?.[index]?.specificDayValue && touched?.[`${name}.specificDayValue`];
  const monthError = !!errors?.subscriptionPlans?.[index]?.specificMonthValue && touched?.[`${name}.specificMonthValue`];
  const errorMessage = errors?.subscriptionPlans?.[index]?.specificMonthValue || errors?.subscriptionPlans?.[index]?.specificDayValue;

  return dayError || monthError ? <div className="invalid-feedback d-inline-block">{errorMessage}</div> : null;
};

const SpecificDaySettings = (props: PlanChildProps) => {
  const { fields, name, index } = props;
  const planFields = fields.value[index];

  const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  const daysToMap =
    planFields.frequencyInterval === FrequencyIntervalUnit.WEEK
      ? daysOfWeek
      : _.range(1, new Date(new Date().getFullYear(), planFields.specificMonthValue ?? 1, 0).getDate() + 1, 1).map(formatDate);

  function formatDate(day: number | string) {
    day = day.toString();
    const lastNumber = parseInt(day.slice(-1));

    const suffix =
      lastNumber == 1 && !day.endsWith('11')
        ? 'st'
        : lastNumber == 2 && !day.endsWith('12')
        ? 'nd'
        : lastNumber == 3 && !day.endsWith('13')
        ? 'rd'
        : 'th';

    return `${day}${suffix}`;
  }

  return (
    <FormGroup>
      <Label for={`${name}.frequencyType`}>Select Specific Day</Label>
      <div className="d-flex" style={{ gap: '2rem' }}>
        {planFields.frequencyInterval === FrequencyIntervalUnit.YEAR && <SpecificMonth {...props} />}
        <Field
          name={`${name}.specificDayValue`}
          id="specificDayValue"
          initialValue=""
          className="form-control"
          key={`${name}.${index}.${planFields.specificDayEnabled}`}
          validate={value => (!value ? 'Please select a valid day.' : undefined)}
        >
          {({ input, meta }) => (
            <InputGroup>
              <Input
                {...input}
                type="select"
                invalid={meta.error && meta.touched}
                style={{
                  flexGrow: 1,
                  minWidth: '46%',
                }}
              >
                <option value="">Select Day</option>
                {daysToMap.map((day, index) => (
                  <option key={index} value={index + 1}>
                    {day}
                  </option>
                ))}
              </Input>
              {planFields.frequencyInterval === FrequencyIntervalUnit.MONTH && (
                <InputGroupAddon addonType='append'>of the month</InputGroupAddon>
              )}
            </InputGroup>
          )}
        </Field>
      </div>
      <SpecificDayErrors key={`${planFields.specificDayValue}${planFields.specificMonthValue}`} name={name} index={index} />
    </FormGroup>
  );
};

export default SpecificDaySettings;
