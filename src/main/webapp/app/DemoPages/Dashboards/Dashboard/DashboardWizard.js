import React from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { CardBody, Row, Col } from 'reactstrap';
import { OnboardingChecklistStep } from 'app/shared/model/enumerations/onboarding-checklist-step.model';
import DashboardMultiStepper from './DashboardMultiStepper';

const DashboardWizard = () => {
  const steps = [
    OnboardingChecklistStep.SUBSCRIPTION_PLANS,
    OnboardingChecklistStep.APPSTLE_WIDGET,
    OnboardingChecklistStep.EMAIL_TEMPLATES,
    OnboardingChecklistStep.DUNNING_CANCELLATION,
    OnboardingChecklistStep.BUILD_A_BOX,
    OnboardingChecklistStep.SHIPPING_PROFILE,
    OnboardingChecklistStep.SUPPORT,
  ];

  return (
    <ReactCSSTransitionGroup
      component="div"
      transitionName="TabsAnimation"
      transitionAppear
      transitionAppearTimeout={0}
      transitionEnter={false}
      transitionLeave={false}
    >
      <Row className="custom-space">
        <Col md="12" className="p-xl-0">
          <CardBody className="p-0">
            <div className="forms-wizard-vertical">
              <DashboardMultiStepper steps={steps} />
            </div>
          </CardBody>
        </Col>
      </Row>
    </ReactCSSTransitionGroup>
  );
};
export default DashboardWizard;
