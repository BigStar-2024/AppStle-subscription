import PageTitle from 'app/Layout/AppMain/PageTitle';
import React from 'react';
import { Card, CardBody, CardHeader } from 'reactstrap';
import FAQSection, { FAQEntry } from 'app/DemoPages/Dashboards/Shared/FAQSection';

export default function FAQPage({}) {
  return (
    <>
      <PageTitle heading="Frequently Asked Questions" subheading="" icon="lnr-layers icon-gradient bg-mean-fruit" sticky={false} />
      <main>
        <FAQSection style={{ margin: "0 auto", maxWidth: "980px" }}>
          <FAQEntry question="What are Shopify's Limitations on Allowed Payment Gateways?">
            <p>
              One of the most important limitations set by Shopify is that it currently requires all merchants looking to install any
              subscription app to use one of the following payment gateways: Shopify Payments, PayPal Express, Authorize.net, Shop Pay (for
              express/dynamic checkouts), Stripe, or Apple Pay. Appstle Subscriptions, similar to any other subscription app in Shopify, is
              bound by these criteria.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5214644-shopify-subscription-apis-limitations"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: Shopify Subscription - APIs Limitations
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to Create a Subscription Plan and Add/Remove Products?">
            <p>
              To create subscription plans for your store, you can click on 'Manage Plans,' under the header of subscription plans, on the
              left-hand panel. Here, you can click on 'Create Subscription Plan' on the top right. You can then enter a suitable name for
              your subscription plan and select all the products you want the plan to apply to.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/4924892-how-to-create-a-subscription-plan"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Create a Subscription Plan
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=4ViJlPmWSSA&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=5"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Create Subscription
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=5hsTuLus1i4&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=16"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Adding and Removing Products and Attributes
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="What are the different types of Billing Plans and how to set them up?">
            <p>
              Appstle offers three different billing plans: Pay As You Go, Prepaid One-Time, and Prepaid Auto Renew. With Pay As You Go,
              your customers will be charged at regular intervals, as per the order frequency and interval of their subscription. With
              Prepaid One-Time, you can charge your customers an upfront payment for multiple upcoming orders that are a part of the
              subscription. Prepaid Auto-Renew is quite similar to Prepaid One-Time, however you can enable the prepaid plan to continue
              charging the customer upfront based on the configuration you define.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5275198-what-are-the-different-subscription-plan-types"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: What Are The Different Subscription Plan Types?
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://intercom.help/appstle/en/articles/5202872-how-to-set-up-pre-paid-billing-in-subscriptions"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Set Up Pre-Paid Billing
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://intercom.help/appstle/en/articles/6112308-how-to-smartly-use-the-pay-as-you-go-plan-for-upfront-payments-with-appstle-subscriptions"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Use The "Pay As You Go" Plan For Upfront Payments
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=AKR4gfRdEMU&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=36&pp=iAQB"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Pay As You Go
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=P0ncMfwwz_s&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=37&pp=iAQB"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Prepaid Auto Renew
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=zNJSLOhOJYM&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=38&pp=iAQB"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Prepaid One Time
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to customize the subscription widget?">
            <p>
              You can customize your subscription widget by logging in to your Merchant admin page of the Appstle Subscriptions app, and
              clicking on 'Customize'. Under the 'Customize' section, you will be able to customize widget settings, customer portal
              settings, and add custom CSS.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5000394-how-to-customize-your-subscription-widget-customer-portal-custom-css"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to customize your subscription - widget, customer portal, custom CSS
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to offer custom shipping for subscriptions and recurring orders?">
            <p>
              Appstle Subscriptions enables you to create distinct shipping profiles for subscriptions, and facilitates configuring and
              managing shipping charges for recurring orders directly from the app. To create and manage shipping profiles, navigate to
              Subscription Plans in the left side panel, and click on Shipping profile. Once you are on the Shipping Profile page, click on
              'Create Shipping Profile', appearing on the top-right side of your screen.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Create Custom Shipping Profiles for Subscription Orders
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=AcknZAvk0s8&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=41"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: How to Create A Shipping Profile
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to offer loyalty perks - discount, shipping, gifts - to subscribers?">
            <p>
              Appstle's advanced discounts, otherwise known as 'multi-tier' discounts enable you to automatically change the discount rate
              after a few cycles. You - the merchant, can setup multi tier discounting while creating a subscription plan, or later while
              updating/editing it. You can offer either a discount for subscribing or a discount for staying subscribed. Check out our
              documentation to learn more about how to configure loyalty rewards.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5290343-how-to-configure-loyalty-to-provide-advanced-multi-tiered-discounts-in-appstle-subscriptions"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Configure Loyalty
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=dwONFEPDQYo&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=30&pp=iAQB"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Percentage Discount
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=ER5i3fAjHTQ&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=32"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Shipping Price
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=8oFpQq6lkDg&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=30"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Free Product
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="Build-A-Box Classic Type and Single Product Type - How to set them up?">
            <p>
              'Build-A-Box', also known as box subscriptions, is a subset of subscription or recurring revenue-based business models. A
              customer is allowed to literally build their own customized box of products or services and check them out together as a
              bundle. The first step in creating a Build-A-Box plan is creating a regular or traditional subscription plan. In your
              dashboard, go to Build-A-Box &gt; Configure. Then click 'Create Build-A-Box'. Choose from one of two types of Build-A-Box,
              Classic or Mix and Match.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Set Up Build-A-Box
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=1sIU8pTYwd4&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=35"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Classic Build-A-Box
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=4QPhUyZV0wI&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=37"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Single Product Build-A-Box
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to offer product or service bundling with Appstle Subscriptions?">
            <p>
              To set up bundling, you first need to activate a bundle. Navigate to Bundling &gt; Bundles &gt; Create Bundle. Then, you need
              to enter some basic information about the bundle, such as label text. Once the bundle is configured to your liking, select the
              products that you want to offer as part of the bundle. Once you press Save, the bundle will be live and ready to be purchased.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/6756487-how-to-setup-and-configure-bundling-with-appstle-subscriptions"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Set Up And Configure Bundling
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=63BM5YIJh70&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=38"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Bundling
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="What are order cut-off dates and how to set them up?">
            <p>
              The function of cut-off days while setting pre-set delivery dates is to filter out newly created subscription orders that are
              too close to the pre-set delivery date. Setting cut off days is purely optional; its sole purpose is to reduce the stress on
              merchants! You can read about how to set up a cut-off date in the documentation below.
            </p>
            <span>
              <a
                href="https://intercom.help/appstle/en/articles/5213425-how-to-use-the-pre-set-delivery-date-functionality"
                target="_blank"
                rel="noreferrer noopener"
              >
                Docs: How to Use the Pre-Set Delivery Date Functionality
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=FVyb76d7X9Y&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=34&pp=iAQB"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: Monthly Cutoff Days
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to access the customer portal?">
            <p>
              Accessing the customer portal is a useful way to view and manage customers' subscriptions. You can access the customer portal
              by navigating to Customers, clicking on a customer's name, and clicking "View Customer Subscription Page".
            </p>
            <span>
              <a
                href="https://www.youtube.com/watch?v=aIslGRp83cY&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=25"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: How to Access The Customer Portal
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
          <FAQEntry question="How to integrate Appstle with other Shopify and Shopify Plus apps?">
            <p>
              Appstle has a wide range of integrations with other apps. You can view the full list of integrations in the app by navigating
              to App Integrations in the sidebar. You can also read about how to set up each integration in the documentation below.
            </p>
            <span>
              <a href="https://intercom.help/appstle/en/collections/3319537-app-integrations" target="_blank" rel="noreferrer noopener">
                Docs: App Integrations
              </a>
              <span style={{ paddingLeft: '0.5rem', paddingRight: '0.5rem' }}>|</span>
              <a
                href="https://www.youtube.com/watch?v=Vf67zzsFIbo&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=48"
                target="_blank"
                rel="noreferrer noopener"
              >
                Video: App Integrations (w/ Klaviyo Integration Example)
              </a>
            </span>
            <p>
              If you still have doubts, feel free to email us at <a href="mailto:support@appstle.com">support@appstle.com</a> or use the
              chat widget in the bottom right corner of the app.
            </p>
          </FAQEntry>
        </FAQSection>
      </main>
    </>
  );
}
