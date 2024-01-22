import React from 'react';
import _ from 'lodash';
import { Col, FormGroup, Row } from 'reactstrap';
import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import { PlanChildProps } from '../CreateSubscriptionGroupPlans';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import FrequencyName from './PlanOptions/FrequencyName';
import BillingType from './PlanOptions/BillingType';
import FrequencyDescription from './PlanOptions/FrequencyDescription';
import OrderFrequency from './PlanOptions/OrderFrequency';
import BillingFrequency from './PlanOptions/BillingFrequency';
import CutOff from './PlanOptions/CutOff';
import OfferDiscountToggle from './PlanOptions/OfferDiscountToggle';
import DiscountSettings from './PlanOptions/DiscountSettings';
import SpecificDayToggle from './PlanOptions/SpecificDayToggle';
import SpecificDaySettings from './PlanOptions/SpecificDaySettings';

type PlanGeneralOptionsProps = PlanChildProps;

const PlanGeneralOptions = ({ fields, name, index }: PlanGeneralOptionsProps) => {
  const planChildProps = { fields, name, index };
  const planFields = fields.value[index];

  const insetStyling = { background: '#f6f6f6', padding: '18px', borderRadius: '10px' };

  return (
    <>
      <Row>
        <Col xs={12} md={6}>
          <FrequencyName {...planChildProps} />
        </Col>
        <Col xs={12} md={6}>
          <BillingType {...planChildProps} />
        </Col>
      </Row>
      <FrequencyDescription {...planChildProps} />
      <OrderFrequency {...planChildProps} />

      {planFields.planType !== OrderBillingType.PAY_AS_YOU_GO && <BillingFrequency {...planChildProps} />}

      <div style={{ display: planFields.freeTrialEnabled ? 'none' : 'block' }}>
        <OfferDiscountToggle {...planChildProps} />
        {planFields.discountEnabled && (
          <Row>
            <Col md={12} sm={12} xs={24}>
              <div style={insetStyling}>
                <DiscountSettings {...planChildProps} />
              </div>
            </Col>
          </Row>
        )}
        {planFields.frequencyInterval !== FrequencyIntervalUnit.DAY && (
          <FormGroup>
            <SpecificDayToggle {...planChildProps} />
            {planFields.specificDayEnabled && (
              <div style={insetStyling}>
                <SpecificDaySettings {...planChildProps} />
                <CutOff {...planChildProps} />
              </div>
            )}
          </FormGroup>
        )}
      </div>
    </>
  );
};
export default PlanGeneralOptions;
