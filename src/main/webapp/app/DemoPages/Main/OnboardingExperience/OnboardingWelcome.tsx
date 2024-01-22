import React, { useEffect, useState, ChangeEvent } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { getEntities as getShopPaymentInfoEntities } from 'app/entities/shop-payment-info/shop-payment-info.reducer';
// @ts-ignore IntercomAPI is an export even though LSP is saying otherwise
import { IntercomAPI } from 'react-intercom';
import { Row, Col, Card, CardBody, Button, Input } from 'reactstrap';
import { OnboardingPage } from '../OnboardingExperience';
import { IShopPaymentInfo } from 'app/shared/model/shop-payment-info.model';

type OnboardingOption = {
  optionText: string;
  message: React.ReactNode | string;
  buttonText: string;
  action: () => void;
};

const WelcomeCard = ({ goToNextStep }) => {
  const onboardingOptions: OnboardingOption[] = [
    {
      optionText: 'Start selling my products as subscriptions',
      message: 'Create subscription selling plans for your products, setting rules such as discounts, billing intervals, and more.',
      buttonText: 'Get Started',
      action: () => goToNextStep(),
    },
    {
      optionText: 'Migrate my existing subscriptions over to Appstle Subscriptions',
      message:
        'If you have existing subscriptions on another app or platform, our migration specialists are ready to help you smoothly migrate your subscriptions to Appstle Subscriptions.',
      buttonText: 'Talk to a Migration Specialist',
      action: () => openIntercom(),
    },
  ];

  const [selectedOption, setSelectedOption] = useState(null);

  function openIntercom() {
    IntercomAPI(
      'showNewMessage',
      "Hello, I'm looking to migrate my subscriptions over from a different platform. Would you be able to assist me?"
    );
  }

  return (
    <Col className={'d-flex justify-content-center'}>
      <Card style={{ maxWidth: '600px', minWidth: '400px', width: '100%' }}>
        <CardBody className="d-flex flex-column align-items-center p-4">
          <h2 className="h4">How can we help you get started?</h2>
          <p>Choose what best describes how you want to use Appstle Subscriptions</p>
          <Input
            id="onboarding-options"
            type="select"
            onChange={(event: ChangeEvent<HTMLSelectElement>) => setSelectedOption(onboardingOptions?.[event.target.value] ?? null)}
            defaultValue={''}
            className={`${!selectedOption ? 'text-secondary' : ''}`}
          >
            <option value="" className="text-secondary" disabled>
              I want to use Appstle Subscriptions to...
            </option>
            {onboardingOptions.map((option, index) => (
              <option value={index} className="text-dark">{option.optionText}</option>
            ))}
          </Input>
          {!!selectedOption && (
            <>
              <hr className="w-100" />
              <p>{selectedOption.message}</p>
              <Button size="lg" color="primary" onClick={selectedOption.action} className="mt-auto align-self-end">
                {selectedOption.buttonText}
              </Button>
            </>
          )}
        </CardBody>
      </Card>
    </Col>
  );
};

const UpdatePaymentCard = () => {
  const login = useSelector((state: IRootState) => state.authentication.account.login);
  return (
    <Card style={{ maxWidth: '600px', minWidth: '400px', width: '100%' }}>
      <CardBody className="d-flex flex-column align-items-start">
        <h2 className="text-center w-100 mt-2 mb-4">Update Your Payment Gateway</h2>
        <p>
          Shopify currently requires all merchants looking to install any subscription app to use Shopify Payments, Stripe, Paypal express
          gateway, Apple Pay, Shop Pay or Authorized.Net only. Please update your payment gateway by clicking the button below.
        </p>
        <p>
          You can see all qualifying criteria for Shopify subscriptions API here:{' '}
          <a target="_blank" href="https://help.shopify.com/en/manual/products/subscriptions/setup#eligibility-requirements">
            Eligibility Criteria
          </a>
        </p>
        <div className="d-flex justify-content-center w-100">
          <a href={`https://${login}/admin/settings/payments`} target="_blank" style={{ whiteSpace: 'nowrap' }}>
            <Button color="primary" size="lg">
              Update Payment Gateway
            </Button>
          </a>{' '}
        </div>
      </CardBody>
    </Card>
  );
};

const OnboardingWelcome: OnboardingPage = props => {
  const { goToNextStep } = props;

  const firstName = useSelector((state: IRootState) => state.authentication.account.firstName);
  const isUsingShopifyPay = useSelector(
    (state: IRootState) => state.shopPaymentInfo.entities?.paymentSettings?.supportedDigitalWallets?.includes('SHOPIFY_PAY') ?? true
  );
  const isPaymentInfoLoading = useSelector((state: IRootState) => state.shopPaymentInfo.loading);
  const dispatch = useDispatch();

  useEffect(() => {
    fetchShopPaymentInfo();
  }, []);

  const fetchShopPaymentInfo = async () => {
    if (!isPaymentInfoLoading && !isUsingShopifyPay) {
      (dispatch as (entity: any) => Promise<any>)(getShopPaymentInfoEntities()).then(res => {
        const paymentInfoEntity: IShopPaymentInfo = res?.value?.data;
        const isUsingShopifyPayCheck = paymentInfoEntity?.paymentSettings?.supportedDigitalWallets?.includes('SHOPIFY_PAY') ?? false;
        if (!isUsingShopifyPayCheck) {
          setTimeout(fetchShopPaymentInfo, 3000);
        }
      });
    }
  };

  return (
    <div className="d-flex w-100 h-100 flex-column justify-content-center p-5">
      <Row style={{ flex: '1 1 0px' }} className="mb-lg-5 pb-5 justify-content-center align-items-end ">
        <div>
          <p className="mb-1 lead text-center">Welcome {firstName}. Let's get you started with using</p>
          <h1 style={{ color: '#0AA8EB' }} className="font-weight-bold text-center">
            Appstle Subscriptions
          </h1>
        </div>
      </Row>
      <Row className="flex-row justify-content-center flex-nowrap">
        {isUsingShopifyPay ? <WelcomeCard goToNextStep={goToNextStep} /> : <UpdatePaymentCard />}
      </Row>
      <Row style={{ flex: '1 1 0px' }} className="empty mt-lg-5 pt-5"></Row>
    </div>
  );
};

export default OnboardingWelcome;
