import React, { Fragment, useEffect, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import BlockUi from '@availity/block-ui';
import { Loader } from 'react-loaders';
import { connect } from 'react-redux';
import { Button, ButtonGroup, Card, CardBody, CardHeader, Collapse, Container, Table } from 'reactstrap';
import { getBillingUrl } from 'app/entities/payment-plan/payment-billing.reducer';
import { getPaymentPlanByShop } from 'app/entities/payment-plan/payment-plan.reducer';
import { getAllValidPlanInfos } from 'app/entities/plan-info/plan-info.reducer';
import { validateDiscountCode } from 'app/entities/plan-info-discount/plan-info-discount.reducer';
import PlanTable from './PlanTable';
import classnames from 'classnames';
import './PlanTable.scss';
import DiscountCode from './DiscountCode';
import createApp from "@shopify/app-bridge";
import { ChevronForward, Checkmark, Close } from 'react-ionicons';
import { API_KEY } from "app/config/constants";
import { Redirect } from '@shopify/app-bridge/actions';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';
import {
  Automations,
  CoreProduct,
  CustomerPrivileges,
  DedicatedSupport,
  GrowSubscriptionRevenue,
  MerchantPrivileges
} from './PlanArray';
import { getLockStatus } from 'app/modules/account/lock-billing-plan/lock.reducer';


const forward_arrow_icon = {
  marginLeft: 'auto',
  transition: 'transform 0.2s',
  transformOrigin: 'center'
};

const PlanTableForAccordion = (planArray, type, annualSelected) => {
  return <Table bordered>
    <thead>
      <tr>
        <th style={{ width: "20%" }}></th>
        <th style={{ width: "16%" }} hidden={annualSelected}>Free</th>
        <th style={{ width: "16%" }}>Starter</th>
        <th style={{ width: "16%" }}>Business</th>
        <th style={{ width: "16%" }}>Business Premium</th>
        <th style={{ width: "16%" }}>Enterprise</th>
      </tr>
    </thead>
    <tbody>
      {
        type === "CoreProduct" && <tr>
          <th scope="row">Monthly Subscription Sales</th>
          <td hidden={annualSelected}>Up to $500</td>
          <td>Up to $5,000</td>
          <td>Up to $30,000</td>
          <td>Up to $100,000</td>
          <td>Unlimited</td>
        </tr>
      }

      {
        planArray && planArray?.length > 0 && planArray?.map(data =>
          <tr>
            <td>{data.title}</td>
            <td hidden={annualSelected}>{data.free
              ? <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#3ac47d' }}>
                <Checkmark width="24px" height="24px" color="#fff" />
              </div>
              : <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#f21604' }}>
                <Close width="24px" height="24px" color="#fff" icon='ios-close' />
              </div>
            }</td>
            <td>{data.starter ? <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#3ac47d' }}>
                <Checkmark width="24px" height="24px" color="#fff" />
            </div>
              : <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#f21604' }}>
                <Close width="24px" height="24px" color="#fff" icon='ios-close' />
              </div>
            }</td>
            <td>{data.business ? <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#3ac47d' }}>
                <Checkmark width="24px" height="24px" color="#fff" />
            </div>
              : <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#f21604' }}>
                <Close width="24px" height="24px" color="#fff" icon='ios-close' />
              </div>
            }</td>
            <td>{data.enterprise ? <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#3ac47d' }}>
                <Checkmark width="24px" height="24px" color="#fff" />
            </div>
              : <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#f21604' }}>
                <Close width="24px" height="24px" color="#fff" icon='ios-close' />
              </div>
            }</td>
            <td>{data.enterprisePlus ? <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#3ac47d' }}>
                <Checkmark width="24px" height="24px" color="#fff" />
            </div>
              : <div className="page-heading-icon-wrapper" style={{ backgroundColor: '#f21604' }}>
                <Close width="24px" height="24px" color="#fff" icon='ios-close' />
              </div>
            }</td>
          </tr>
        )
      }
    </tbody>
  </Table>
}

export function PlanInfoList(props) {
  const { paymentPlanEntity, planInfoList, planInfoListLoaded, paymentPlanLimit } = props;
  const [blocking, setBlocking] = useState(false);
  const [annualSelected, setAnnualSelected] = useState(false);
  const [monthlyPlanList, setMonthlyPlanList] = useState();
  const [yearlyPlanList, setYearlyPlanList] = useState();
  const [filteredPlanList, setFilteredPlanList] = useState([]);
  const [discountCopoun, setDiscountCopoun] = useState();
  const [isValidDiscountCode, setIsValidDiscountCode] = useState();
  const [totalQuotaPercentUsed, setTotalQuotaPercentUsed] = useState(0);
  const [isTestChange, setIsTestChange] = useState(false);
  const [lockStatus, setLockStatus] = useState(true);
  const [showLockMessage, setShowLockMessage] = useState(false);

  useEffect(() => {
    getLockStatus().then(res => {
      setLockStatus(res);
      setShowLockMessage(res);
    });
  }, []);

  useEffect(() => {
    setIsTestChange(paymentPlanEntity?.testCharge && paymentPlanEntity?.planInfo?.price > 0 ? "TEST" : "");
  }, [paymentPlanEntity]);
  const [accordion, setAccordion] = useState([true, true, true, true, true, true]);

  useEffect(() => {
    props.getAllValidPlanInfos();
  }, []);

  useEffect(() => {
    props.getPaymentPlanByShop();
  }, []);

  useEffect(() => {
    if (planInfoList != null && planInfoList.length > 0) {
      setFilteredPlanList(planInfoList?.filter(plan => plan.planType === 'MONTHLY'));
    }
  }, [planInfoList]);
  console.log(BasedOn, 'BasedOn')
  useEffect(() => {
    if (props.billingUrl != null) {

      /*if (!window['app'] && sessionStorage['host']) {
        window['app'] = createApp({
          apiKey: API_KEY,
          host: sessionStorage['host']
        });
      }*/

      if (!window['app'] && window.__SHOPIFY_DEV_HOST) {
        console.log("came here -> !window['app'] && sessionStorage['host']");
        window['app'] = createApp({
          apiKey: API_KEY,
          host: window.__SHOPIFY_DEV_HOST
        });
      }

      if (window.app) {
        Redirect.create(app).dispatch(Redirect.Action.REMOTE, props.billingUrl.billingUrl);
      } else {
        window.location = props.billingUrl.billingUrl;
      }
    }
  }, [props]);

  useEffect(() => {
    if (props?.planInfoDiscountEntity?.valid) {
      props.getAllValidPlanInfos(discountCopoun);
    }
  }, [props.planInfoDiscountEntity]);
  useEffect(() => {
    let totalQuotaPercentUsed = 0;
    if (paymentPlanLimit && BasedOn) {
      if (paymentPlanLimit.activeSubscriptionCount != null && paymentPlanLimit.planLimit != null) {
        totalQuotaPercentUsed = ((paymentPlanLimit.activeSubscriptionCount / paymentPlanLimit.planLimit) * 100).toFixed(0);
      }

      if (paymentPlanLimit?.planInfo?.basedOn == BasedOn?.SUBSCRIPTION_ORDER_AMOUNT && paymentPlanLimit.usedOrderAmount != null && paymentPlanLimit.orderAmountLimit != null) {
        totalQuotaPercentUsed = ((paymentPlanLimit.usedOrderAmount / paymentPlanLimit.orderAmountLimit) * 100).toFixed(0);
      }
      setTotalQuotaPercentUsed(totalQuotaPercentUsed)
    }

  }, [paymentPlanLimit])

  const activatePlan = plan => {
    props.getBillingUrl(plan.id, discountCopoun, window.__SHOPIFY_DEV_HOST);
    setBlocking(true);
  };

  const handleChangePlanType = isAnnual => {
    setAnnualSelected(isAnnual);
    if (isAnnual) {
      setFilteredPlanList(planInfoList?.filter(plan => plan.planType === 'YEARLY'));
    } else {
      setFilteredPlanList(planInfoList?.filter(plan => plan.planType === 'MONTHLY'));
    }
  };

  const validateAndApplyDiscount = async () => {
    const res = await props.validateDiscountCode(discountCopoun);
    if (res.value.data?.valid === false) {
      // toast.error("Invalid discount code")
      setIsValidDiscountCode(false);
      setTimeout(function () {
        setIsValidDiscountCode('');
      }, 5000);
    } else {
      setIsValidDiscountCode(true);
    }
  };

  const clearDiscountCode = () => {
    setIsValidDiscountCode('');
    setDiscountCopoun('');
    props.getAllValidPlanInfos();
  };

  const toggleAccordion = tab => {
    const state = accordion.map((x, index) => (tab === index ? !x : false));
    setAccordion(state);
  };
  useEffect(() => {
    test();
  }, []);
  const test = () => {
    let stickyElem = document.querySelector(".sticky-div");
    let currStickyPos = stickyElem.getBoundingClientRect().top + window.pageYOffset;
    window.onscroll = function () {
      if (window.pageYOffset > currStickyPos) {
        stickyElem.style.position = "sticky";
        stickyElem.style.top = "47px";
      } else {
        stickyElem.style.position = "relative";
        stickyElem.style.top = "initial";
      }
    }
  }
  return (
    <BlockUi
      tag="div"
      blocking={blocking}
      className="block-overlay-dark"
      loader={<Loader color="#ffffff" active type="ball-triangle-path" />}
    >
      <Fragment>
        <ReactCSSTransitionGroup
          component="div"
          transitionName="TabsAnimation"
          transitionAppear
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}
        >
          <Container fluid>
            <div component="main" className="text-center m-3">
              <Card className="main-card mb-2">
                <CardBody>
                  <div>
                    <div> {totalQuotaPercentUsed > 100 ? <h1 style={{ marginTop: '4rem' }}>
                      Please Upgrade your plan.
                    </h1> :
                      <h1 className="h2 text-primary" style={{ marginTop: '4rem' }}>
                        Pricing plans tailored for your Shopify store
                      </h1>}

                      {(!paymentPlanEntity || !paymentPlanEntity.planInfo) && planInfoList && planInfoList.length > 0 ? (
                        <h4 className="m-2">
                          Start Your {planInfoList[0].trialDays} Day Free Starter Trial
                        </h4>
                      ) : null}

                      {props?.trialExpired && (
                        <h3 className="mb-2 text-danger">
                          Please re-activate your plan to continue using the app.
                        </h3>
                      )}

                      {!paymentPlanEntity.planInfo ? null : (
                        <h3 className="h5 mb-2 text-primary">
                          You are currently on the <b>{paymentPlanEntity.planInfo.name} </b> <b className={"text-danger"}>{isTestChange}</b> plan
                          <span> {totalQuotaPercentUsed > 100 ? `. Monthly subscription quota utilized: ${paymentPlanLimit.usedOrderAmount?.toLocaleString()}/${paymentPlanLimit.orderAmountLimit?.toLocaleString()}` : null}</span>
                        </h3>
                      )}

                      {paymentPlanEntity.planInfo && props.convertedTestShop && paymentPlanEntity?.testCharge && paymentPlanEntity?.planInfo?.price > 0  ? (
                        <h4 className="h6 mb-2 text-danger">
                          You are currently on a <b className={"text-danger"}>{isTestChange}</b> plan, Please reactivate a plan to continue.
                        </h4>
                      ) : ""}

                      {showLockMessage && (
                        <>
                          <h4 className="mb-2 text-danger">
                            You are on a custom plan, please contact support to downgrade your plan.
                          </h4>
                          <Button
                            color="primary"
                            size="md"
                            className="btn-shadow btn-wide btn-pill"
                            onClick={() => {
                              Intercom("showNewMessage", "Hi, I would like to downgrade my plan. Could you assist me?")
                            }}
                          >
                            Contact Support
                          </Button>
                        </>
                      )}

                      <div className="d-flex justify-content-center mt-5">
                        <div>
                          <ButtonGroup size="lg" className="mb-6 mt-6">
                            <Button
                              caret="true"
                              outline
                              color="primary  btn-hover-shine  btn-transition"
                              size={'lg'}
                              className={`btn-shadow pl-3 pr-3 ${classnames({ active: annualSelected === false })}`}
                              onClick={() => {
                                handleChangePlanType(false);
                              }}
                            >
                              MONTHLY
                            </Button>
                            <Button
                              outline
                              color="primary  btn-hover-shine  btn-transition"
                              size={'lg'}
                              className={`btn-shadow pr-3 pl-3 ${classnames({ active: annualSelected === true })}`}
                              onClick={() => {
                                handleChangePlanType(true);
                              }}
                            >
                              ANNUAL
                            </Button>
                          </ButtonGroup>
                          <h5 className={'mt-2 text-primary'}>
                            <b>Save 20% on Annual plans</b>
                          </h5>
                        </div>
                      </div>
                      <BlockUi tag="div" blocking={props?.planInfoLoading || lockStatus}>
                        <DiscountCode
                          isValidDiscountCode={isValidDiscountCode}
                          setDiscountCopoun={setDiscountCopoun}
                          discountCopoun={discountCopoun}
                          validatingDIscountCode={props?.validatingDIscountCode}
                          clearDiscountCode={clearDiscountCode}
                          validateAndApplyDiscount={validateAndApplyDiscount}
                          planInfoDiscountEntity={props?.planInfoDiscountEntity}
                        />

                        <div className="mt-5 sticky-div" style={{ padding: "16px", zIndex: 1, background: "white", width: "100%", boxShadow: "0 0.46875rem 2.1875rem rgb(8 10 37 / 3%), 0 0.9375rem 1.40625rem rgb(8 10 37 / 3%), 0 0.25rem 0.53125rem rgb(8 10 37 / 5%), 0 0.125rem 0.1875rem rgb(8 10 37 / 3%)", borderWidth: 0,
    transition: "all 0.2s" }}>
                          <PlanTable planInfoList={filteredPlanList} isAnnual={annualSelected} onActivate={activatePlan}
                            paymentPlanEntity={paymentPlanEntity} trialExpired={props?.trialExpired} />
                        </div>

                        <div className="mb-5">
                          <Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(0)}
                              aria-expanded={accordion[0]}
                              aria-controls="collapseFour"
                            >
                              Core Product
                              <span style={{ ...forward_arrow_icon, transform: accordion[0] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[0]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(CoreProduct, "CoreProduct", annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card>
                          <Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(1)}
                              aria-expanded={accordion[1]}
                              aria-controls="collapseFour"
                            >
                              Email notification Features
                              <span style={{ ...forward_arrow_icon, transform: accordion[1] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[1]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(MerchantPrivileges, null, annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card>
                          <Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(2)}
                              aria-expanded={accordion[2]}
                              aria-controls="collapseFour"
                            >
                              Analytics
                              <span style={{ ...forward_arrow_icon, transform: accordion[2] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[2]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(CustomerPrivileges, null, annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card>
                          <Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(3)}
                              aria-expanded={accordion[3]}
                              aria-controls="collapseFour"
                            >
                              Automations
                              <span style={{ ...forward_arrow_icon, transform: accordion[3] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[3]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(Automations, null, annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card>
                          <Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(4)}
                              aria-expanded={accordion[4]}
                              aria-controls="collapseFour"
                            >
                              Grow subscription revenue
                              <span style={{ ...forward_arrow_icon, transform: accordion[4] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[4]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(GrowSubscriptionRevenue, null, annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card><Card className="main-card">
                            <CardHeader
                              style={{ textTransform: 'unset', fontSize: '17px', fontWeight: 'bold' }}
                              onClick={() => toggleAccordion(5)}
                              aria-expanded={accordion[5]}
                              aria-controls="collapseFour"
                            >
                              Dedicated Support
                              <span style={{ ...forward_arrow_icon, transform: accordion[5] ? 'rotate(90deg)' : '' }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                              </span>
                            </CardHeader>
                            <Collapse isOpen={accordion[5]} data-parent="#accordion" id="collapseFour"
                              aria-labelledby="headingOne">
                              <CardBody>
                                {PlanTableForAccordion(DedicatedSupport, null, annualSelected)}
                              </CardBody>
                            </Collapse>
                          </Card>
                        </div>

                        <DiscountCode
                          isValidDiscountCode={isValidDiscountCode}
                          setDiscountCopoun={setDiscountCopoun}
                          discountCopoun={discountCopoun}
                          validatingDIscountCode={props?.validatingDIscountCode}
                          clearDiscountCode={clearDiscountCode}
                          validateAndApplyDiscount={validateAndApplyDiscount}
                          planInfoDiscountEntity={props?.planInfoDiscountEntity}
                        />
                      </BlockUi>
                      <div className="mt-4">
                        Need more subscription limit or have custom requirements?{' '}
                        <a href="javascript:Intercom('showNewMessage');">Contact us</a> or{' '}
                        <a href="mailto:subscription-support@appstle.com">Email us</a>
                      </div>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          </Container>
        </ReactCSSTransitionGroup>
      </Fragment>
    </BlockUi>
  );
}

const mapStateToProp = state => ({
  planInfoList: state.planInfo.entities,
  planInfoListLoaded: state.planInfo.planInfoListLoaded,
  planInfoLoading: state.planInfo.loading,
  billingUrl: state.billingUrl.confirmationUrl,
  billingUrlLoading: state.billingUrl.loading,
  paymentPlanEntity: state.paymentPlan.entity,
  paymentPlanLoading: state.paymentPlan.loading,
  account: state.authentication.account,
  accountLoaded: state.authentication.sessionHasBeenFetched,
  validatingDIscountCode: state.planInfoDiscount.validatingDIscountCode,
  planInfoDiscountEntity: state.planInfoDiscount.entity,
  paymentPlanLimit: state.paymentPlan.paymentPlanLimit,
});

const mapDispatchToProps = {
  getAllValidPlanInfos,
  getBillingUrl,
  validateDiscountCode,
  getPaymentPlanByShop: getPaymentPlanByShop
};

export default connect(mapStateToProp, mapDispatchToProps)(PlanInfoList);
