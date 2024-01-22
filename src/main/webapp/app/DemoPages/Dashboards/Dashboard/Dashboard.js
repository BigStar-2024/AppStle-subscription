import { ChevronRight } from '@mui/icons-material';
import { getEntities } from 'app/entities/subscription-group/subscription-group.reducer';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';
import React, { Fragment, useEffect, useState } from 'react';
import { BsChatDotsFill } from 'react-icons/bs';
import { FaQuestionCircle, FaVideo, FaStore } from 'react-icons/fa';
import { GrMail } from 'react-icons/gr';
import { IntercomAPI } from 'react-intercom';
import PerfectScrollbar from 'react-perfect-scrollbar';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { VerticalTimeline, VerticalTimelineElement } from 'react-vertical-timeline-component';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { Card, CardBody, CardHeader, Col, Container, Row, Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import useScreenSize from 'use-screen-size';
import AppSlider from '../RecommendedApps/AppSlider';
import './dashboard.scss';

const Dashboard = ({
  account,
  subscriptionGrpEntities,
  getEntities,
  shopInfo,
  shopPaymentInfoEntity,
  themeSettingsEntity,
  paymentPlanLimit
}) => {
  const [onBoardingFlag, setOnBoardingFlag] = useState(true);
  const size = useScreenSize();

  const [showDemoStoreModal, setShowDemoStoreModal] = useState(false);
  const toggleShowDemoStoreModal = () => setShowDemoStoreModal(!showDemoStoreModal);

  useEffect(() => {
    if (subscriptionGrpEntities?.length === 0) {
      // getEntities()
    }
  }, []);

  useEffect(() => {
    if (shopInfo && shopInfo?.onboardingSeen !== undefined && shopInfo?.onboardingSeen !== null) {
      setOnBoardingFlag(shopInfo?.onboardingSeen);
    }
  }, [shopInfo, shopInfo?.onboardingSeen]);

  let totalQuotaPercentUsed = 0;
  if (paymentPlanLimit.activeSubscriptionCount != null && paymentPlanLimit.planLimit != null) {
    totalQuotaPercentUsed = ((paymentPlanLimit.activeSubscriptionCount / paymentPlanLimit.planLimit) * 100).toFixed(0);
  }

  if (
    paymentPlanLimit?.planInfo?.basedOn == BasedOn.SUBSCRIPTION_ORDER_AMOUNT &&
    paymentPlanLimit.usedOrderAmount != null &&
    paymentPlanLimit.orderAmountLimit != null
  ) {
    totalQuotaPercentUsed = ((paymentPlanLimit.usedOrderAmount / paymentPlanLimit.orderAmountLimit) * 100).toFixed(0);
  }

  const isInsideShopify =
    (window?.location?.href?.includes('myshopify.com') ||
      (window?.location?.ancestorOrigins?.[0] ? window?.location?.ancestorOrigins[0]?.includes('myshopify.com') : false)) &&
    size.width >= 993;

  const latestUpdates = [
    {
      date: 'July 31, 2023',
      title: 'Shipping Profile Redesigned',
      description: 'A new, easy and improved UI for Shipping Profile management, to provide custom shipping options for subscription orders.',
    },
    {
      date: 'July 31, 2023',
      title: "'Fixed Subscription price' under Loyalty discount",
      description: 'Functionality to set fixed price discounts as a loyalty reward. This could be set up to show appreciation for customers who have been long-time subscribers.',
    },
    {
      date: 'July 14, 2023',
      title: 'Bulk Adding of one-time/free products to subscription',
      description: 'Functionality to add free gifts to multiple subscription orders in bulk, for efficiency. This feature is heavily used during Holidays, or to offer free trials of new products.',
    },
    {
      date: 'June 15, 2023',
      title: 'Integration with ShipperHQ',
      description: 'Appstle Subscriptions now seamlessly integrates with ShipperHQ for managing your shipping experience.',
    },
    {
      date: 'June 8, 2023',
      title: 'Location-based inventory check',
      description: 'Apart from overall inventory checks for subscription orders, Appstle now offers inventory checks for specific locations, for granular stock management.',
    },
    {
      date: 'April 28, 2023',
      title: 'Adding/removing discount codes in bulk',
      description: 'Functionality to add/remove/modify subscribe and save discounts to multiple subscription orders in bulk, for efficiency. This feature is generally used to price match existing subscriptions with new subscriptions, and to adjust for inflation/deflation, special promotions, etc.',
    },
    {
      date: 'April 25, 2023',
      title: 'Auto-sync SKUs and product/variant titles',
      description: 'Product/variant titles and SKUs can now be automatically synced directly from Shopify to ease maintenance.'
    },
    {
      date: 'March 11, 2023',
      title: "Create a manual subscription contract",
      description: "Functionality for the merchant to create a subscription order on behalf of the customer. The customer however needs to have a saved payment method with Shopify.",
    },
    {
      date: 'March 10, 2023',
      title: "Update the customer's payment method",
      description: "Functionality for the merchant to update paymement method for a subscription order on behalf of the customer.",
    },
    {
      date: 'February 22, 2023',
      title: 'Loyalty point redemption from Customer Portal',
      description: 'Functionality to redeem loyalty points (from any loyalty app) for a subscription order.',
    },
  ];

  return (
    <Fragment>
      <div className="dashboard-wrapper">
        <PageTitle heading="Dashboard" icon="pe-7s-rocket icon-gradient bg-mean-fruit" sticky={false} skipBulkAutomationPolling={true} />
        <Container fluid>
          <Row>
            <Col lg={8} md={8} sm={12}>
              {/*<Alert color={'success'} className="appstle_error_box">
                <div className="d-flex">
                  <p>
                    🎉 Local Delivery and Pickup options are now available for subscriptions! Your store will be able to
                    use this
                    functionality by default when setting up a local delivery or pickup profile in Shopify. Configure a
                    profile in your
                    Shopify admin settings under "Shipping and delivery". Learn more about this update{' '}
                    <a
                      style={{cursor: 'pointer', color: '#30b1ff'}}
                      onClick={() => {
                        let appUrl = 'https://changelog.shopify.com/posts/introducing-local-delivery-pickup-for-subscriptions';
                        if (window.app) {
                          Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
                            url: appUrl,
                            newContext: true
                          });
                        } else {
                          window.open(appUrl, '_blank');
                        }
                      }}
                    >
                      here.
                    </a>
                  </p>
                </div>
              </Alert>*/}

              <Card>
                <CardBody>
                  <div className="d-flex custom-video-data" style={{ justifyContent: 'space-between' }}>
                    <Row>
                      <div className="custom-list px-4">
                        <span>Hey there {account?.firstName} 👋 </span>
                        <span className="custom-thanks">
                          Thanks for using Appstle Subscriptions. To get started follow the steps below:
                        </span>
                        <ol className="mt-3">
                          <li className="mb-3">
                            <h6>
                              Configure <Link to="/dashboards/subscription-plan">Subscription Selling Plans</Link>
                            </h6>
                            <p className="mb-0">Here you will set rules such as discounts, billing intervals, and more. plans here.</p>
                            <p>
                              Learn more about creating selling plans{' '}
                              <a href="https://intercom.help/appstle/en/articles/4924892-how-to-create-a-subscription-plan" target="_blank">
                                in this help doc
                              </a>{' '}
                              or{' '}
                              <a
                                href="https://www.youtube.com/watch?v=4ViJlPmWSSA&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=27"
                                target="_blank"
                              >
                                in this video tutorial.
                              </a>
                            </p>
                          </li>
                          <li className="mb-3">
                            <h6>Assign Products to Your Selling Plans</h6>
                            <p className="mb-0">You have full control over which products are available as a subscription.</p>
                            <p>
                              Learn more about assigning products to selling plans{' '}
                              <a
                                href="https://intercom.help/appstle/en/articles/8269335-assigning-products-to-subscriptions-selling-plans"
                                target="_blank"
                              >
                                here.
                              </a>
                            </p>
                          </li>
                          <li className="mb-3">
                            <h6>Ensure the Appstle Product Page Widget is Installed on Your Theme</h6>
                            <p className="mb-0">Don't see the widget on your product pages? Some themes need extra configuration. </p>
                            <p>
                              <a
                                href="javascript:;"
                                onClick={() => {
                                  IntercomAPI('showNewMessage', 'Hey, I need help installing the widget on my store.');
                                }}
                              >
                                Our support team is ready to help.
                              </a>
                            </p>
                          </li>
                          <li className="mb-3">
                            <h6>
                              <span className="d-inline text-muted font-weight-normal">(Optional)</span> Set Up a Custom{' '}
                              <Link to="/dashboards/shipping-profile">Shipping Profile</Link> for Subscriptions
                            </h6>
                            <p className="mb-0">Here you can choose to reward your subscribers with cheaper shipping or free shipping.</p>
                            <p>
                              Learn more about custom shipping profiles{' '}
                              <a
                                target="_blank"
                                href="https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders"
                              >
                                in this help doc
                              </a>{' '}
                              or{' '}
                              <a
                                href="https://www.youtube.com/watch?v=AcknZAvk0s8&list=PLJ4hVaZCRPOGqEXL7IAmmaYTftzYsgaXm&index=13"
                                target="_blank"
                              >
                                in this video tutorial.
                              </a>
                            </p>
                          </li>
                          <li className="mb-3">
                            <h6>
                              <span className="d-inline text-muted font-weight-normal text-nowrap">
                                (Optional but Recommeded by Appstle)
                              </span>{' '}
                              Set Up <Link to="/dashboards/dunning-management">Dunning</Link> &{' '}
                              <Link to="/dashboards/Cancellation-management">Cancellation</Link> Management to Control Churn
                            </h6>
                            <p>
                              Learn more about{' '}
                              <a
                                target="_blank"
                                href="https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions"
                              >
                                dunning management
                              </a>{' '}
                              and{' '}
                              <a
                                target="_blank"
                                href="https://intercom.help/appstle/en/articles/5417506-how-to-smartly-use-appstle-s-cancellation-management-feature-to-retain-customers"
                              >
                                cancellation management.
                              </a>
                            </p>
                          </li>
                        </ol>
                      </div>
                    </Row>
                  </div>
                </CardBody>
              </Card>

              <Card className="mt-4">
                <CardHeader style={{ display: 'flex', justifyContent: 'space-between' }}>
                  Our Recommended Apps
                  <Link to="/dashboards/recommended-apps">
                    View All
                    {<ChevronRight />}
                  </Link>
                </CardHeader>
                <CardBody>
                  <Row>
                    <Col md={12} className="p-0">
                      <AppSlider />
                    </Col>
                  </Row>
                </CardBody>
              </Card>
            </Col>
            <Col lg={4} md={4} sm={12}>
              <Card style={{ height: '100%' }} className="custom-card">
                <CardBody>
                  <h6 style={{ fontWeight: 'bold' }}>Help & Support</h6>
                  <div style={{ padding: '7px', border: '1px solid #dfdfdf' }} className="rounded mb-4 custom-liveChat">
                    <div className="mb-2 custom-email">
                      <FaStore size={20} color={'#2392ec'} />
                      <DemoStoreModal showDemoStoreModal={showDemoStoreModal} toggleShowDemoStoreModal={toggleShowDemoStoreModal} />
                      <a
                        className="ml-2"
                        onClick={() => {
                          toggleShowDemoStoreModal();
                        }}
                      >
                        Demo Store
                      </a>
                    </div>
                    <div className="mb-2 custom-email">
                      <BsChatDotsFill size={20} color={'#2392ec'} />
                      <a
                        className="ml-2  "
                        // style={{color: '#2392ec', cursor:'pointer',textDecoration:'none'}}
                        onClick={() => {
                          IntercomAPI('showNewMessage', '');
                        }}
                      >
                        Live chat
                      </a>
                    </div>

                    <div className="mb-2  custom-email">
                      <FaQuestionCircle size={20} color={'#2392ec'} />
                      <a className="ml-2" href="https://intercom.help/appstle/en/collections/2776373-subscriptions" target="_blank">
                        Help Docs
                      </a>
                    </div>
                    <div className="mb-2 custom-email">
                      <FaVideo size={20} color={'#2392ec'} />
                      <Link to="/dashboards/tutorials" className="ml-2">
                        Video Tutorials
                      </Link>
                    </div>
                    <div className="custom-email">
                      <GrMail size={20} color={'#2392ec'} />
                      <a className="ml-2" href="mailto:subscription-support@appstle.com">
                        subscription-support@appstle.com
                      </a>
                    </div>
                  </div>
                  {/*<h6 style={{fontWeight: 'bold'}}>Get Started</h6>*/}
                  {/*<iframe style={{width: "100%", outline: "2px solid #545cd8", outlineOffset: "2px", borderRadius: "5px"}}*/}
                  {/*src="https://www.youtube.com/embed/ed2vr9nF-c0"*/}
                  {/*  title="YouTube video player" frameBorder="0"*/}
                  {/*  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"*/}
                  {/*  allowFullScreen/>*/}
                  <h6 style={{ fontWeight: 'bold' }} className="mt-4">
                    Latest Updates
                  </h6>
                  <div className="scroll-area-sm custom-latest-update">
                    <PerfectScrollbar>
                      <VerticalTimeline layout="1-column" className="vertical-time-simple vertical-without-time">
                        {latestUpdates.map(update => (
                          <VerticalTimelineElement className="vertical-timeline-item">
                            <span>{update.date}</span>
                            <h4 className="timeline-title d-flex align-items-center" style={{ gap: '.25rem' }}>
                              {update.title}
                              {update?.description && <HelpTooltip>{update.description}</HelpTooltip>}
                            </h4>
                          </VerticalTimelineElement>
                        ))}
                      </VerticalTimeline>
                    </PerfectScrollbar>
                  </div>
                </CardBody>
              </Card>
            </Col>
          </Row>
          {/* <HelpPopUp onBoardingFlag={onBoardingFlag}>
            <div>
              <h6>Appstle Subscriptions Onboarding Video</h6>
              <div className="embed-responsive embed-responsive-16by9 mt-3">
                <YoutubeVideoPlayer url="https://youtu.be/tT4MguhtGxg" />
              </div>
            </div>
          </HelpPopUp> */}
        </Container>
      </div>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  account: state.authentication.account,
  subscriptionGrpEntities: state.subscriptionGroup.entities,
  shopInfo: state.shopInfo.entity,
  shopPaymentInfoEntity: state.shopPaymentInfo.entities,
  themeSettingsEntity: state.themeSettings.entity,
  paymentPlanLimit: state.paymentPlan.paymentPlanLimit
});

const mapDispatchToProps = {
  getEntities
};

function DemoStoreModal({ showDemoStoreModal, toggleShowDemoStoreModal }) {
  return (
    <Modal isOpen={showDemoStoreModal} toggle={toggleShowDemoStoreModal}>
      <ModalHeader toggle={toggleShowDemoStoreModal}>Demo Store</ModalHeader>
      <ModalBody>
        <p>
          You can view our demo store to see how Appstle Subscriptions works. The demo store has been configured to showcase the app's
          features.
        </p>
        <img src={require('./demo-store.png')} alt="demo store" width={350} height={300} />
      </ModalBody>
      <ModalFooter>
        <Button color="link" onClick={toggleShowDemoStoreModal}>
          Cancel
        </Button>
        <Button
          color="link"
          onClick={() => {
            window.open('https://apps.shopify.com/subscriptions-by-appstle', '_blank');
            toggleShowDemoStoreModal();
          }}
        >
          Go to Demo Store
        </Button>
      </ModalFooter>
    </Modal>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
