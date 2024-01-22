import React, { useEffect, useState } from 'react';
import { Button } from 'reactstrap';
import useWindowDimensions from 'app/shared/util/window-dimensions';
import { IRootState } from 'app/shared/reducers';
import { useSelector, useDispatch } from 'react-redux';
import { completeChecklistItem, updateChecklistCompleted } from 'app/entities/onboarding-info/onboarding-info.reducer';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';
import DashboardSteps from './DashboardSteps';

const StepDisplay = ({
  steps,
  index,
  isUsingControls,
  goToPrevious,
  goToNext,
}: {
  steps: OnboardingChecklistStep[];
  index: number;
  isUsingControls: boolean;
  goToPrevious?: () => void;
  goToNext?: () => void;
}) => {
  return (
    <div className="right">
      <DashboardSteps step={steps[index]} />
      <div className="divider" />
      <div className="clearfix">
        {isUsingControls && (
          <div>
            <Button
              color="secondary"
              className="btn-shadow float-left btn-wide btn-pill"
              outline
              style={index > 0 ? {} : { display: 'none' }}
              onClick={() => goToPrevious()}
            >
              Previous
            </Button>

            <Button
              color="primary"
              className="btn-shadow btn-wide float-right btn-pill btn-hover-shine"
              style={index < steps.length - 1 ? {} : { display: 'none' }}
              onClick={() => goToNext()}
            >
              Next
            </Button>
          </div>
        )}
      </div>
    </div>
  );
};

type CompletedStepsState = { [K in OnboardingChecklistStep]: boolean };

const StepsList = ({
  steps,
  navState,
  setNavState,
}: {
  steps: OnboardingChecklistStep[];
  navState: number;
  setNavState: (step: number) => void;
}) => {
  const { width } = useWindowDimensions();
  const completedStepsState: CompletedStepsState = useSelector(
    (state: IRootState) =>
      Object.entries(OnboardingChecklistStep).reduce<Partial<CompletedStepsState>>((compState, [key, step]) => {
        compState[key] = state.onboardingInfo.entity.completedChecklistSteps?.includes(step);
        if (step === OnboardingChecklistStep.APPSTLE_WIDGET) compState[key] = compState[key] && state.shopInfo.isAppEmbed;
        compState[key] = compState[key] || state.onboardingInfo.entity.checklistCompleted;
        return compState;
      }, {}) as CompletedStepsState
  );

  const stepTitles: { [K in OnboardingChecklistStep]: string } = {
    [OnboardingChecklistStep.SUBSCRIPTION_PLANS]: 'Subscription Plans',
    [OnboardingChecklistStep.APPSTLE_WIDGET]: 'Appstle Widget',
    [OnboardingChecklistStep.EMAIL_TEMPLATES]: 'Email Templates',
    [OnboardingChecklistStep.DUNNING_CANCELLATION]: 'Dunning & Cancellation',
    [OnboardingChecklistStep.BUILD_A_BOX]: 'Build-A-Box',
    [OnboardingChecklistStep.SHIPPING_PROFILE]: 'Shipping Profile',
    [OnboardingChecklistStep.SUPPORT]: 'Help & Support',
  };

  return (
    <ol className="forms-wizard">
      {steps.map((step: OnboardingChecklistStep, index: number) => (
        <li
          className={`${index === navState ? 'form-wizard-step-doing' : ''} ${
            completedStepsState[step] ? 'form-wizard-step-done' : ''
          } d-block`}
          onClick={({ currentTarget }) => setNavState(currentTarget.value)}
          key={index}
          value={index}
        >
          <div className="d-flex align-items-center">
            <em>{index + 1}</em>
            <span>{stepTitles[step]?.toUpperCase()}</span>
          </div>
          {width < 1200 && <StepDisplay steps={steps} index={index} isUsingControls={false} />}
        </li>
      ))}
    </ol>
  );
};

const DashboardMultiStepper = ({ steps }: { steps: OnboardingChecklistStep[] }) => {
  const { width } = useWindowDimensions();

  const isExistingSubscriptionPlan = useSelector((state: IRootState) => state.subscriptionGroup.entities.length > 0);
  const uncompletedChecklistSteps = useSelector((state: IRootState) => state.onboardingInfo.entity.uncompletedChecklistSteps);
  const completedChecklistSteps = useSelector((state: IRootState) => state.onboardingInfo.entity.completedChecklistSteps);
  const dispatch = useDispatch();

  const [navState, setNavState] = useState(0);

  useEffect(() => {
    if (isExistingSubscriptionPlan) {
      dispatch(completeChecklistItem(OnboardingChecklistStep.SUBSCRIPTION_PLANS));
    }

    if (uncompletedChecklistSteps?.length <= 0 && completedChecklistSteps?.length >= 1) {
      dispatch(updateChecklistCompleted());
    }

  }, [isExistingSubscriptionPlan, uncompletedChecklistSteps])

  const goToNext = () => {
    if (navState < steps.length - 1) {
      setNavState(navState + 1);
    }
  };

  const goToPrevious = () => {
    if (navState > 0) {
      setNavState(navState - 1);
    }
  };

  return (
    <div>
      <StepsList steps={steps} navState={navState} setNavState={setNavState} />
      {width >= 1200 && <StepDisplay steps={steps} index={navState} isUsingControls goToNext={goToNext} goToPrevious={goToPrevious} />}
    </div>
  );
};

export default DashboardMultiStepper;
