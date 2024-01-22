import React, { useState, ComponentType, useEffect, useCallback } from 'react';
import { Container, Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import Loader from 'react-loaders';
import { useSelector, useDispatch } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { getEntities as getShopPaymentInfoEntities } from 'app/entities/shop-payment-info/shop-payment-info.reducer';
import OnboardingWelcome from './OnboardingExperience/OnboardingWelcome';
import OnboardingCreateSubscription from './OnboardingExperience/OnboardingCreateSubscription';
import OnboardingWidgetSetup from './OnboardingExperience/OnboardingWidgetSetup';
import { getSession } from '../../shared/reducers/authentication';
import { getProfile } from '../../shared/reducers/application-profile';
import { getPaymentPlanByShop } from 'app/entities/payment-plan/payment-plan.reducer';
import { getEntity as getThemeSettings } from 'app/entities/theme-settings/theme-settings.reducer';

type OnboardingPageProps = {
  goToNextStep: () => void;
};

export type OnboardingPage = ComponentType<OnboardingPageProps>;

const OnboardingExperience = () => {
  const history = useHistory();

  const isLoading = useSelector(
    (state: IRootState) => state.authentication.loading || state.shopPaymentInfo.entities?.paymentSettings == null
  );
  const dispatch = useDispatch();

  const [onboardingStep, setOnboardingStep] = useState(0);
  const [isCloseModalOpen, setIsCloseModalOpen] = useState(false);

  const onboardingStepPages: OnboardingPage[] = [OnboardingWelcome, OnboardingCreateSubscription, OnboardingWidgetSetup];

  const CurrentStepPage = onboardingStepPages?.[onboardingStep] ?? null;

  useEffect(() => {
    dispatch(getShopPaymentInfoEntities());
    dispatch(getSession());
    dispatch(getProfile());
    dispatch(getPaymentPlanByShop(1));
    dispatch(getThemeSettings(1));
  }, []);

  function goToDashboard() {
    history.push({ pathname: '/main/dashboard', state: { onboarding: true } });
  }

  function goToNextStep() {
    if (onboardingStep >= onboardingStepPages.length - 1) {
      goToDashboard();
    } else {
      setOnboardingStep(onboardingStep + 1);
    }
  }

  if (isLoading) {
    return (
      <div className="loader-container">
        <div className="loader-container-inner">
          <div className="text-center">
            <Loader active type="ball-pulse-rise" />
          </div>
          <h6 className="mt-4">Please wait while we load your onboarding experience...</h6>
        </div>
      </div>
    );
  }

  return (
    <ReactCSSTransitionGroup
      component="div"
      transitionName="FadeInAnimation"
      transitionAppear
      transitionAppearTimeout={0}
      transitionEnter={false}
      transitionLeave={false}
    >
      <Container fluid className="position-relative" style={{ backgroundColor: '#f1f4f6' }}>
        <div className="position-absolute app-header__logo" style={{ top: 0 }}>
          <span className="logo-src"></span>
        </div>
        <ReactCSSTransitionGroup
          key={onboardingStep}
          component="div"
          transitionName="FadeInAnimation"
          transitionAppear={onboardingStep > 0}
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}
        >
          <CurrentStepPage goToNextStep={goToNextStep} />
        </ReactCSSTransitionGroup>
        <button
          onClick={() => setIsCloseModalOpen(true)}
          className="text-secondary position-absolute"
          style={{ border: 'unset', background: 'unset', right: '35px', top: '20px', cursor: 'pointer' }}
        >
          <u>Exit Onboarding</u>
        </button>
        <CloseModal isOpen={isCloseModalOpen} setIsOpen={setIsCloseModalOpen} goToDashboard={goToDashboard} />
      </Container>
    </ReactCSSTransitionGroup>
  );
};

const CloseModal = ({ isOpen, setIsOpen, goToDashboard }) => (
  <Modal isOpen={isOpen} toggle={() => setIsOpen(!isOpen)}>
    <ModalHeader>Are you sure?</ModalHeader>
    <ModalBody>Are you sure you sure you want to leave the onboarding experience?</ModalBody>
    <ModalFooter>
      <Button color="primary" onClick={() => setIsOpen(false)}>
        Back to Onboarding
      </Button>
      <Button color="secondary" onClick={() => goToDashboard()}>
        Go to Dashboard
      </Button>
    </ModalFooter>
  </Modal>
);

export default OnboardingExperience;
