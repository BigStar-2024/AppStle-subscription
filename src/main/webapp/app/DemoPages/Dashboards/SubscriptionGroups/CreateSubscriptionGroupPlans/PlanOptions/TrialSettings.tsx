import React from 'react';
import { FormGroup, Label, Input, Row, Col, InputGroup, FormText } from 'reactstrap';
import { Field } from 'react-final-form';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import { DiscountTypeUnit } from 'app/shared/model/enumerations/discount-type-unit.model';

const TrialPeriodSettings = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];
  const pluralEnd = planFields.frequencyCount != 1 ? 's' : '';

  function validateTrialPeriodCount(value: string) {
    if (value == null || isNaN(parseInt(value))) {
      return 'Please provide a valid value for Trial Count';
    }
    if (parseInt(value) < 1) {
      return 'Please provide a valid value for Trial Count';
    }

    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.freeTrialCount`}>Trial Period</Label>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
        <Field name={`${name}.freeTrialCount`} id={`${name}.freeTrialCount`} type="number" validate={validateTrialPeriodCount}>
          {({ input, meta }) => (
            <div style={{ flexGrow: 1, width: '50%' }}>
              <Input {...input} className="" type="number" invalid={meta.error && meta.touched} />
              {meta.error && meta.touched && <div className="invalid-feedback d-inline-block">{meta.error}</div>}
            </div>
          )}
        </Field>
        <Field name={`${name}.freeTrialInterval`} id={`${name}.freeTrialInterval`} type="select" initialValue={FrequencyIntervalUnit.DAY}>
          {({ input }) => (
            <Input
              {...input}
              className="form-control"
              style={{
                flexGrow: 1,
                maxWidth: '200px',
              }}
            >
              <option value={FrequencyIntervalUnit.DAY}>Day{pluralEnd}</option>
              <option value={FrequencyIntervalUnit.WEEK}>Week{pluralEnd}</option>
              <option value={FrequencyIntervalUnit.MONTH}>Month{pluralEnd}</option>
              <option value={FrequencyIntervalUnit.YEAR}>Year{pluralEnd}</option>
            </Input>
          )}
        </Field>
      </div>
      <FormText>Please visit Loyalty section to setup additional discounts after the Trial period</FormText>
    </FormGroup>
  );
};

const TrialDiscountSettings = ({ name }: PlanChildProps) => {
  function validateDiscountAmount(value: string) {
    if (value == null || isNaN(parseInt(value)) || parseInt(value) < 0) {
      return 'Please provide a valid value for discount.';
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.discountOffer`}>
        Discount Offer <b>DURING Trial Period</b>
      </Label>
      <div className="d-flex" style={{ gap: '1rem' }}>
        <Field
          name={`${name}.discountOffer`}
          id={`${name}.discountOffer`}
          type="number"
          initialValue={null}
          className="form-control"
          validate={validateDiscountAmount}
        >
          {({ input, meta }) => (
            <div style={{ width: '50%', flexGrow: 1 }}>
              <Input {...input} invalid={meta.error && meta.touched} />
              {meta.error && meta.touched && <div className="invalid-feedback d-block">{meta.error}</div>}
            </div>
          )}
        </Field>
        <Field
          name={`${name}.discountType`}
          id="discountType"
          type="select"
          initialValue={DiscountTypeUnit.PERCENTAGE}
          className="form-control"
        >
          {({ input }) => (
            <Input {...input} style={{ maxWidth: '200px', flexGrow: 1 }}>
              <option value={DiscountTypeUnit.PERCENTAGE}>Percent off(%)</option>
              <option value={DiscountTypeUnit.PRICE}>Amount off</option>
              <option value={DiscountTypeUnit.FIXED}>Fixed Price</option>
            </Input>
          )}
        </Field>
      </div>
      <FormText>For free trial offer 100% discount during trial period</FormText>
    </FormGroup>
  );
};

const AfterTrialDiscountSettings = ({ name }: PlanChildProps) => {
  function validateAfterTrialDiscountAmount(value: string) {
    if (value == null || isNaN(parseInt(value)) || parseInt(value) < 0) {
      return 'Please provide a valid value for discount.';
    }
    return undefined;
  }

  return (
    <FormGroup>
      <Label for={`${name}.discountOffer2`}>
        Discount Offer <b>AFTER Trial Period</b> ends
      </Label>
      <div className="d-flex" style={{ gap: '1rem' }}>
        <Field
          id={`${name}.discountOffer2`}
          name={`${name}.discountOffer2`}
          type="number"
          initialValue={null}
          className="form-control"
          validate={validateAfterTrialDiscountAmount}
          render={({ input, meta }) => (
            <InputGroup className="discountAmount" style={{ width: '50%', flexGrow: 1 }}>
              <Input {...input} invalid={meta.error && meta.touched} />
              {meta.error && meta.touched && <div className="invalid-feedback d-block">{meta.error}</div>}
            </InputGroup>
          )}
        />
        <Field
          name={`${name}.discountType2`}
          id="discountType2"
          type="select"
          initialValue={DiscountTypeUnit.PERCENTAGE}
          className="form-control"
        >
          {({ input }) => (
            <Input {...input} style={{ maxWidth: '200px', flexGrow: 1 }}>
              <option value={DiscountTypeUnit.PERCENTAGE}>Percent off(%)</option>
              <option value={DiscountTypeUnit.PRICE}>Amount off</option>
              <option value={DiscountTypeUnit.FIXED}>Fixed Price</option>
            </Input>
          )}
        </Field>
      </div>
    </FormGroup>
  );
};

const TrialSettings = (props: PlanChildProps) => {
  return (
    <FormGroup>
      <Row className="mb-2">
        <Col sm={12} xs={24}>
          <TrialPeriodSettings {...props} />
        </Col>
      </Row>
      <Row className="mb-2">
        <Col sm={12} xs={24}>
          <TrialDiscountSettings {...props} />
        </Col>
      </Row>
      <Row>
        <Col md={12} sm={12} xs={24}>
          <AfterTrialDiscountSettings {...props} />
        </Col>
      </Row>
    </FormGroup>
  );
};

export default TrialSettings;
