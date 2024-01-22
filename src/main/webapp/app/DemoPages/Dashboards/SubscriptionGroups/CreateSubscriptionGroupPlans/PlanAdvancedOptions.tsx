import React, { useState, useEffect } from 'react';
import _ from 'lodash';
import { CardBody, Col, Collapse, FormGroup, Row, CardHeader } from 'reactstrap';
import { ChevronRight } from '@mui/icons-material';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';
import { PlanChildProps } from '../CreateSubscriptionGroupPlans';
import MinimumCycles from './PlanOptions/MinimumCycles';
import MaximumCycles from './PlanOptions/MaximumCycles';
import UpcomingOrderEmailBuffer from './PlanOptions/UpcomingOrderEmailBuffer';
import OfferTrialToggle from './PlanOptions/OfferTrialToggle';
import TrialSettings from './PlanOptions/TrialSettings';
import MemberOnlyToggle from './PlanOptions/MemberOnlyToggle';
import MemberOnlyTags from './PlanOptions/MemberOnlyTags';
import MemberExcludeToggle from './PlanOptions/MemberExcludeToggle';
import MemberExcludeTags from './PlanOptions/MemberExcludeTags';
import CustomFieldsToggle from './PlanOptions/CustomFieldsToggle';
import CustomFieldsSettings from './PlanOptions/CustomFieldsSettings';

type PlanAdvancedOptionsProps = PlanChildProps;

const PlanAdvancedOptions = ({ fields, name, index }: PlanAdvancedOptionsProps) => {
  const planFields = fields.value[index];
  const planChildProps = { fields, name, index };

  const [isShowing, setIsShowing] = useState(false);

  const insetStyling = { background: '#eee', padding: '18px', borderRadius: '10px' };

  return (
    <div style={{ margin: '-1.25rem', marginTop: '0' }}>
      <hr className="m-0" />
      <Collapse isOpen={isShowing}>
        <FeatureAccessCheck hasAnyAuthorities={'accessAdvanceSubscriptionPlanOptions'} upgradeButtonText="Upgrade your plan">
          <CardBody style={{ background: '#f6f6f6' }}>
            {planFields.planType !== OrderBillingType.PREPAID && (
              <Row className="mb-2">
                <Col sm={6} xs={12}>
                  <MinimumCycles {...planChildProps} />
                </Col>
                <Col sm={6} xs={12}>
                  <MaximumCycles {...planChildProps} />
                </Col>
              </Row>
            )}
            <div style={{ display: 'none' }}>
              <Row>
                <Col sm={6} xs={12}>
                  <UpcomingOrderEmailBuffer {...planChildProps} />
                </Col>
              </Row>
            </div>
            <div>
              <OfferTrialToggle {...planChildProps} />
              {planFields.freeTrialEnabled && (
                <div style={insetStyling}>
                  <TrialSettings {...planChildProps} />
                </div>
              )}

              {/* SEGMENT BASED PLAN SECTION */}

              <Row>
                <Col xs={12} sm={12} md={12} lg={12}>
                  <FormGroup className="mb-4">
                    <MemberOnlyToggle {...planChildProps} />
                    {planFields.memberOnly && (
                      <div style={insetStyling}>
                        <MemberOnlyTags {...planChildProps} />
                      </div>
                    )}
                  </FormGroup>
                  <FormGroup className="mb-4">
                    <MemberExcludeToggle {...planChildProps} />
                    {planFields.nonMemberOnly && (
                      <div style={insetStyling}>
                        <MemberExcludeTags {...planChildProps} />
                      </div>
                    )}
                  </FormGroup>

                  <CustomFieldsToggle {...planChildProps} />
                  {planFields.formFieldJsonToggle && <CustomFieldsSettings {...planChildProps} />}
                </Col>
              </Row>
            </div>
          </CardBody>
        </FeatureAccessCheck>
      </Collapse>
      <CardHeader onClick={() => setIsShowing(!isShowing)} style={{ justifyContent: 'center', cursor: 'pointer', background: '#eee' }}>
        {!isShowing ? 'Show Advanced Options' : 'Collapse Advanced Options'}
        {<ChevronRight style={{ transform: !isShowing ? 'rotate(0deg)' : 'rotate(-90deg)', transition: 'all 0.2s' }} />}
      </CardHeader>
    </div>
  );
};

export default PlanAdvancedOptions;
