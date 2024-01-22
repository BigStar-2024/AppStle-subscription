import React, {Fragment, useEffect, useState} from 'react';
//import './setting.scss';
import {
  Card,
  CardBody,
  CardHeader,
  Collapse} from 'reactstrap';
import { Tooltip as ReactTooltip } from "react-tooltip";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { ChevronForward } from 'react-ionicons';
import ActiveAllSubcriptionAutomation from './ActiveAllSubcriptionAutomation';
import HideSubscriptionAutomation from './HideSubscriptionAutomation';
import UpdateDeliveryPriceAutomation from './UpdateDeliveryPriceAutomation';
import UpdateNextBillingDateAutomation from './UpdateNextBillingDateAutomation';
import UpdateNextBillingDateTimeAutomation from './UpdateNextBillingDateTimeAutomation';
import ReplaceRemovedVariantAutomation from "./ReplaceRemovedVariantAutomation";
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import ProductPriceAutomation from '../ProductPriceAutomation/ProductPriceAutomation';
import ProductReplaceAutomation from '../ProductReplaceAutomation/ProductReplaceAutomation';
import ProductDeleteAutomation from '../ProductDeleteAutomation/ProductDeleteAutomation';
import UpdateDeliveryMethodNameAutomation from './UpdateDeliveryMethodNameAutomation';
import UpdateBillingIntervalAutomation from "./UpdateBillingIntervalAutomation";
import axios from "axios";
import ProductUpdateMinOrderNumber  from "../ProductUpdateMinOrderNumber/ProductUpdateMinOrderNumber";
import ProductUpdateMaxOrderNumber from "../ProductUpdateMaxOrderNumber/ProductUpdateMaxOrderNumber";
import ShippingAutomation from '../ShippingAutomation/ShippingAutomation';
import KillAutomation from './KillAutomation';
import AddRemoveDiscountCodeAutomation from "./AddRemoveDiscountCodeAutomation";
import AddProductAutomation from './AddProductAutomation';

const SubscriptionAutomation = ({...props}) => {
  const [accordionState, setAccordionState] = useState([false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false]);
  const [cssAccordianState, setCssAccordianState] = useState([])
  const [isAutomationRunning, setIsAutomationRunning] = useState(false);
  const [currentAutomationStatus, setCurrentAutomationStatus] = useState([])

  useEffect(() => {
    getAutomationStatus();
  }, [])

  const getAutomationStatus = () => {
    return axios.get('api/bulk-automations')
    .then(res => {
      let itemRunning = res?.data?.filter(item => item?.running);
      setIsAutomationRunning(Boolean(itemRunning.length));
      setCurrentAutomationStatus(res?.data)
      return res?.data
    })
  }

  const toggleAccordion = (tab) => {
    const prevState = accordionState;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  }

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  }

  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading="Bulk Automations"
          icon="pe-7s-repeat icon-gradient bg-mean-fruit"
          alertType="warning"
          titleMessage="Bulk update will run in background, so after triggering update you can continue with other activities."
          progressValue="100"
          progressType="success"
          progressAnimated={true}
          progressText={"Bulk automation is in progress"}
          showProgress={isAutomationRunning}
          sticky={false}
          tutorialButton={{
            show: true,
            docs: [
              {
                title: "What are Bulk Automations?",
                url: "https://intercom.help/appstle/en/articles/7198870-what-are-bulk-automations"
              }
            ]
          }}
          />
           <FeatureAccessCheck hasAnyAuthorities={'enableAutomation'} upgradeButtonText="Upgrade to enable automation">
                <ReactTooltip effect='solid'
                              delayUpdate={500}
                              html={true}
                              place={'right'}
                              border={true}
                              type={'info'} multiline="true"/>
                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card">
                    <CardHeader
                      onClick={() => (toggleAccordion(0))}
                      aria-expanded={accordionState[0]}
                      aria-controls="modifysubscriptionstatus"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Subscription Status
                      <span style={{...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="modifysubscriptionstatus"
                              aria-labelledby="WidgetLabel">
                        <CardBody>
                        {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_STATUS")).pop())?.running ? <ActiveAllSubcriptionAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_STATUS" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                        </CardBody>
                      </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(1))}
                      aria-expanded={accordionState[1]}
                      aria-controls="HideSubscriptionAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Hide Subscriptions
                      <span style={{...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="HideSubscriptionAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "HIDE_SUBSCRIPTIONS")).pop())?.running ? <HideSubscriptionAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="HIDE_SUBSCRIPTIONS" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(2))}
                      aria-expanded={accordionState[2]}
                      aria-controls="updateDeliveryPriceAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Delivery Price
                      <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[2]} data-parent="#accordion" id="updateDeliveryPriceAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_DELIVERY_PRICE")).pop())?.running ? <UpdateDeliveryPriceAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_DELIVERY_PRICE" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(9))}
                      aria-expanded={accordionState[9]}
                      aria-controls="updateDeliveryMethodNameAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Delivery Method Name
                      <span style={{...forward_arrow_icon, transform: accordionState[9] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[9]} data-parent="#accordion" id="updateDeliveryMethodNameAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_DELIVERY_METHOD")).pop())?.running ? <UpdateDeliveryMethodNameAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_DELIVERY_METHOD" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(3))}
                      aria-expanded={accordionState[3]}
                      aria-controls="updateNextBillingDateTime"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Next Renewal Date Time
                      <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[3]} data-parent="#accordion" id="updateNextBillingDateTime"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_NEXT_BILLING_DATE_TIME")).pop())?.running ? <UpdateNextBillingDateTimeAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_NEXT_BILLING_DATE_TIME" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(4))}
                      aria-expanded={accordionState[4]}
                      aria-controls="updateNextBillingDateAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Next Renewal Date
                      <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[4]} data-parent="#accordion" id="updateNextBillingDateAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_NEXT_BILLING_DATE")).pop())?.running ? <UpdateNextBillingDateAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_NEXT_BILLING_DATE" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(10))}
                      aria-expanded={accordionState[10]}
                      aria-controls="updateBillingInterval"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Billing Interval
                      <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[10]} data-parent="#accordion" id="updateNextBillingDateAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_BILLING_INTERVAL")).pop())?.running ? <UpdateBillingIntervalAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_BILLING_INTERVAL" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>


            <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(8))}
                      aria-expanded={accordionState[8]}
                      aria-controls="replaceRemovedAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> REPLACE REMOVED VARIANT
                      <span style={{...forward_arrow_icon, transform: accordionState[8] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[8]} data-parent="#accordion" id="replaceRemovedAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "REPLACE_REMOVED_VARIANT")).pop())?.running ? <ReplaceRemovedVariantAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="REPLACE_REMOVED_VARIANT" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>


                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(5))}
                      aria-expanded={accordionState[5]}
                      aria-controls="priceAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Price Automation
                      <span style={{...forward_arrow_icon, transform: accordionState[5] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[5]} data-parent="#accordion" id="priceAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_LINE_PRICE")).pop())?.running ? <ProductPriceAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_LINE_PRICE" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(6))}
                      aria-expanded={accordionState[6]}
                      aria-controls="replaceAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Product Replace Automation
                      <span style={{...forward_arrow_icon, transform: accordionState[6] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[6]} data-parent="#accordion" id="replaceAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "REPLACE_PRODUCT")).pop())?.running ? <ProductReplaceAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="REPLACE_PRODUCT" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(7))}
                      aria-expanded={accordionState[7]}
                      aria-controls="deleteAutomation"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Delete Automation
                      <span style={{...forward_arrow_icon, transform: accordionState[7] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[7]} data-parent="#accordion" id="deleteAutomation"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "DELETE_REMOVED_PRODUCT")).pop())?.running ? <ProductDeleteAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="DELETE_REMOVED_PRODUCT" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                    </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(11))}
                      aria-expanded={accordionState[11]}
                      aria-controls="updateMinOrderNumber"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Minimum Number of Orders
                      <span style={{...forward_arrow_icon, transform: accordionState[11] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[11]} data-parent="#accordion" id="updateMinOrderNumber"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_MIN_MAX_CYCLES")).pop())?.running ? <ProductUpdateMinOrderNumber getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_MIN_MAX_CYCLES" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(12))}
                      aria-expanded={accordionState[12]}
                      aria-controls="updateMaxOrderNumber"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Maximum Number of Orders
                      <span style={{...forward_arrow_icon, transform: accordionState[12] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[12]} data-parent="#accordion" id="updateMaxOrderNumber"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_MIN_MAX_CYCLES")).pop())?.running ? <ProductUpdateMaxOrderNumber getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_MIN_MAX_CYCLES" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(13))}
                      aria-expanded={accordionState[13]}
                      aria-controls="updateDeliveryMethodType"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Update Shipping Type
                      <span style={{...forward_arrow_icon, transform: accordionState[13] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[13]} data-parent="#accordion" id="updateDeliveryMethodType"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "UPDATE_DELIVERY_METHOD_TYPE")).pop())?.running ? <ShippingAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="UPDATE_DELIVERY_METHOD_TYPE" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(14))}
                      aria-expanded={accordionState[14]}
                      aria-controls="addRemoveDiscountCode"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Add/Remove Discount Code
                      <span style={{...forward_arrow_icon, transform: accordionState[14] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[14]} data-parent="#accordion" id="addRemoveDiscountCode"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "ADD_REMOVE_DISCOUNT_CODE")).pop())?.running ? <AddRemoveDiscountCodeAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="ADD_REMOVE_DISCOUNT_CODE" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(15))}
                      aria-expanded={accordionState[15]}
                      aria-controls="addProduct"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Add Product
                      <span style={{...forward_arrow_icon, transform: accordionState[14] ? 'rotate(90deg)' : ''}}>
                         <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[15]} data-parent="#accordion" id="addProduct"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                      {!((currentAutomationStatus?.filter(item => item?.automationType === "ADD_PRODUCT")).pop())?.running ? <AddProductAutomation getAutomationStatus={getAutomationStatus} /> :
                        <KillAutomation type="ADD_PRODUCT" getAutomationStatus={getAutomationStatus} currentAutomationStatus={currentAutomationStatus} />}
                      </CardBody>
                    </Collapse>
                  </Card>
                </div>
            </FeatureAccessCheck>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  bulkAutomationa: state.bulkAutomation.entities,
});

const mapDispatchToProps = {
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubscriptionAutomation);
