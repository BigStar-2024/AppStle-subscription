import React from 'react';
// @ts-ignore
import { IntercomAPI } from 'react-intercom';
import { Button } from 'reactstrap';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';
import { useDispatch } from 'react-redux';
import { completeChecklistItem } from 'app/entities/onboarding-info/onboarding-info.reducer';

const DashboardStepCard = ({ title, children }: { title: string; children?: React.ReactNode }) => {
  return (
    <div className="w-100">
      <h4 className="py-3 text-primary font-weight-bold">{title}</h4>
      {children}
    </div>
  );
};

type DashboardStepButton = {
  text: string;
  link?: string;
  openInNewTab?: boolean;
  action?: () => void;
};

const DashboardStepButtons = ({ buttons }: { buttons: DashboardStepButton[] }) => {
  return (
    <div className="mt-3" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.8rem' }}>
      {buttons.map((btn, index) => (
        <div key={index} className={`d-flex ${index % 2 == 0 ? 'justify-content-start' : 'justify-content-end'}`}>
          <a href={btn.link ?? null} target={btn.openInNewTab ? '_blank' : null}>
            <Button color="primary" size="lg" onClick={btn.action} className="btn-pill" style={{ minWidth: '6.25rem' }}>
              {btn.text}
            </Button>
          </a>
        </div>
      ))}
    </div>
  );
};

const DashboardSteps = ({ step }: { step: OnboardingChecklistStep }) => {
  const dispatch = useDispatch();

  if (step === OnboardingChecklistStep.SUPPORT) {
    dispatch(completeChecklistItem(OnboardingChecklistStep.SUPPORT));
  }

  return (
    <>
      {step === OnboardingChecklistStep.SUBSCRIPTION_PLANS && (
        <DashboardStepCard title="Subscription Plans">
          <div>
            Configure <a href="#/dashboards/subscription-plan">subscription selling plans.</a> Here you will set rules such as discounts,
            billing intervals, and more. You have full control over which products are available as a subscription. Assign products to a
            subscription plan within the subscription plan settings, or even from the product within the Shopify admin.
          </div>
          <br />
          <div>
            Learn more about creating selling plans{' '}
            <a href="https://intercom.help/appstle/en/articles/4924892-how-to-create-a-subscription-plan" target="_blank">
              here.
            </a>
          </div>
          <DashboardStepButtons
            buttons={[
              { text: 'Subscription Plans', link: '#/dashboards/subscription-plan' },
              {
                text: 'Help',
                link: 'https://intercom.help/appstle/en/articles/4924892-how-to-create-a-subscription-plan',
                openInNewTab: true,
              },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.APPSTLE_WIDGET && (
        <DashboardStepCard title="Appstle Widget">
          Ensure the Appstle product page widget is installed on your theme. Don't see the widget on your product pages? Our support team is
          ready to help.
          <DashboardStepButtons
            buttons={[
              { text: 'Customize Widget', link: '#/dashboards/product-page-widget' },
              { text: 'Widget Help', action: () => IntercomAPI('showNewMessage', 'Hey, I need help installing the widget on my store.') },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.SHIPPING_PROFILE && (
        <DashboardStepCard title="Shipping Profile">
          (Optional) Set up a custom <a href="#/dashboards/shipping-profile"> shipping profile</a> for subscriptions.
          <DashboardStepButtons
            buttons={[
              { text: 'Shipping Profile', link: '#/dashboards/shipping-profile' },
              {
                text: 'Learn More',
                link: 'https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders',
                openInNewTab: true,
              },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.EMAIL_TEMPLATES && (
        <DashboardStepCard title="Email Templates">
          Customize your subscription <a href="#/dashboards/email-templates">email templates</a> Using Klaviyo? Learn how you can send
          subscription emails from Klaviyo.
          <a target="_blank" href="https://intercom.help/appstle/en/articles/6089718-how-to-customize-emails">
            {' '}
            Learn more about creating emails templates here.
          </a>
          <DashboardStepButtons
            buttons={[
              { text: 'Email Templates', link: '#/dashboards/email-templates' },
              { text: 'Learn More', link: 'https://intercom.help/appstle/en/articles/6089718-how-to-customize-emails', openInNewTab: true },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.DUNNING_CANCELLATION && (
        <DashboardStepCard title="Dunning & Cancellation">
          Set up <a href="#/dashboards/dunning-management"> dunning </a> and
          <a href="#/dashboards/Cancellation-management"> cancellation </a>management. Control what happens when a customer's payment
          attempt fails and settings related to cancellation. Learn more about
          <a target="_blank" href="https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions">
            {' '}
            dunning
          </a>{' '}
          and
          <a
            target="_blank"
            href="https://intercom.help/appstle/en/articles/5417506-how-to-smartly-use-appstle-s-cancellation-management-feature-to-retain-customers"
          >
            {' '}
            cancellation.
          </a>
          <DashboardStepButtons
            buttons={[
              { text: 'Dunning Management', link: '#/dashboards/dunning-management' },
              {
                text: 'Dunning Help',
                link: 'https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions',
                openInNewTab: true,
              },
              { text: 'Cancellation Management', link: '#/dashboards/Cancellation-management' },
              {
                text: 'Cancellation Help',
                link:
                  'https://intercom.help/appstle/en/articles/5417506-how-to-smartly-use-appstle-s-cancellation-management-feature-to-retain-customers',
                openInNewTab: true,
              },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.BUILD_A_BOX && (
        <DashboardStepCard title="Build-A-Box">
          Allow your customers to easily create subscriptions with our <a href="#/dashboards/subscription-bundling">build-a-box</a> feature.
          Learn how to easily add a build-a-box page to your store{' '}
          <a target="_blank" href="https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box">
            here.
          </a>
          <DashboardStepButtons
            buttons={[
              { text: 'Build-A-Box', link: '#/dashboards/subscription-bundling' },
              { text: 'Help', link: 'https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box', openInNewTab: true },
            ]}
          />
        </DashboardStepCard>
      )}
      {step === OnboardingChecklistStep.SUPPORT && (
        <DashboardStepCard title="Help & Support">
          If you need any help or support, our support team is ready to help.
          <DashboardStepButtons buttons={[{ text: 'Get Support', action: () => IntercomAPI('show') }]} />
        </DashboardStepCard>
      )}
    </>
  );
};

export default DashboardSteps;
