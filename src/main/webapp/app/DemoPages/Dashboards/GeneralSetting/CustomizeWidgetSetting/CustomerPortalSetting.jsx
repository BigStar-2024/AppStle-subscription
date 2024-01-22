import React, {Fragment, useEffect, useState} from 'react';
//import './setting.scss';
import { Card, CardBody, CardHeader, Col, Collapse, FormGroup, FormText, Input, Label, Row} from 'reactstrap';
import Tabs, {TabPane} from 'rc-tabs';
import TabContent from 'rc-tabs/lib/SwipeableTabContent';
import ScrollableInkTabBar from 'rc-tabs/lib/ScrollableInkTabBar';
import Switch from 'react-switch';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { ChevronForward } from 'react-ionicons';
import {Help} from "@mui/icons-material";
import {
  getEntities,
  getEntity,
  updateEntity
} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import './customer-setting.scss';
import Select from 'react-select'
import { Tooltip as ReactTooltip } from "react-tooltip";

import { getEntitiesByCategory } from 'app/entities/shop-customization/shop-customization.reducer';
import ShopCustomizationField from '../ShopCustomization/ShopCustomizationField';
import ManageFilterData from './ManageFilterData';
import FAQSection, { FAQEntry } from 'app/DemoPages/Dashboards/Shared/FAQSection'
import Loader from 'react-loaders';
import HelpTooltip from '../../Shared/HelpTooltip';

const customerPortalSetting = ({
                                 customerPortalSettingEntity,
                                 getEntities,
                                 getEntity,
                                 updateEntity,
                                 createEntity,
                                 getEntitiesByCategory,
                                 shopCustomizationFields,
                                 customizationFiledLoading,
                                 ...props
                               }) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([false, false, false, false , false, false]);
  const [isApplySubscriptionDiscount, setIsApplySubscriptionDiscount] = useState(false);
  const [formValues, setFormValues] = useState({});
  const [productFilterConfig, setProductFilterConfig] = useState({enabled: false, filters: []});


  useEffect(() => {
    getEntitiesByCategory("CUSTOMER_PORTAL")
    getEntity(0);
  }, []);

  const saveEntity = values => {
    const formValues = {
      ...values,
      datePickerEnabledDaysV2: values?.datePickerEnabledDaysV2?.length ? values.datePickerEnabledDaysV2.map(e => e.value).join(",") : "0,1,2,3,4,5,6",
    }
    formValues.productFilterConfig = JSON.stringify(productFilterConfig);
    updateEntity(formValues);
  };

  useEffect(() => {
    setIsApplySubscriptionDiscount(customerPortalSettingEntity?.applySubscriptionDiscount);
    const formValues= {
      ...customerPortalSettingEntity,
      datePickerEnabledDaysV2: customerPortalSettingEntity?.datePickerEnabledDaysV2 ? customerPortalSettingEntity?.datePickerEnabledDaysV2?.split(",").map(d => days.find(day => day.value == d)) : [],
    }
    setFormValues(formValues);
  }, [customerPortalSettingEntity]);

  const ReactSelectAdapter = ({ input, ...rest }) => (
    <Select
      isMulti
      {...input}
      {...rest}
    />
  )

  const days = [
    { value: "0", label: "Sunday" },
    { value: "1", label: "Monday" },
    { value: "2", label: "Tuesday" },
    { value: "3", label: "Wednesday" },
    { value: "4", label: "Thursday" },
    { value: "5", label: "Friday" },
    { value: "6", label: "Saturday" }
  ];

  const updateApplySubscriptionDiscount = () => {
    customerPortalSettingEntity.applySubscriptionDiscount = !isApplySubscriptionDiscount;
    setIsApplySubscriptionDiscount(!isApplySubscriptionDiscount);
  };

  const toggleAccordion = tab => {
    const state = accordionState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  };

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  useEffect(() => {console.log("shopCustomizationFields :- ", shopCustomizationFields);}, [shopCustomizationFields])
  let submit;
  const identity = value => value;
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
          heading="Customer Portal Settings"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-how-to-customize-your-subscription-widget-customer-portal-custom-css' target='blank'> Click here to learn more about your customer portal settings</a>"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
          tutorialButton={{
            show: true,
            docs: [
              {
                title: "How to Customize Your Subscription - Widget, Customer Portal, Custom CSS",
                url: "https://intercom.help/appstle/en/articles/5000394-how-to-customize-your-subscription-widget-customer-portal-custom-css"
              }
            ]
          }}
        />

        <Form
          initialValues={formValues}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            submit =
              Object.keys(errors).length === 0 && errors.constructor === Object
                ? handleSubmit
                : () => {
                  if (Object.keys(errors).length) handleSubmit();
                  setFormErrors(errors);
                  setErrorsVisibilityToggle(!errorsVisibilityToggle);
                };
            return (
              <>
                <Row className="align-items-center" style={{marginLeft: '0px'}}>
                  <Col>
                    <div>
                      <h4>Welcome to Customer Portal Settings</h4>
                      <p className="text-muted">
                        Customer Portal refers to the page your customers will use, to manage their subscriptions. This
                        section will help
                        you to customize your customer portal, and make is easy and intuitive as possible, to provide a
                        seamless experience
                        to your Shoppers. This section will help you customize your Customer Portal, and make it as easy
                        and intuitive as
                        possible.
                      </p>
                      <p className="text-muted">
                        If you have any questions at any time, just reach out to us on{' '}
                        <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                      </p>
                    </div>
                  </Col>
                </Row>
                <form onSubmit={handleSubmit}>
                  <div id="accordion" className="accordion-wrapper mb-3">
                    <Card className="main-card">
                      <CardHeader
                        onClick={() => toggleAccordion(0)}
                        aria-expanded={accordionState[0]}
                        aria-controls="widgetLabelSettingsWrapper"
                        style={{cursor: 'pointer'}}
                      >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal Labels
                        <span style={{...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : ''}}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>
                      <Collapse
                        isOpen={accordionState[0]}
                        data-parent="#accordion"
                        id="widgetLabelSettingsWrapper"
                        aria-labelledby="WidgetLabel"
                      >
                        <CardBody>
                          {/* <Tabs tabsWrapperClass="card-header" {...tabConfig} /> */}
                          <Tabs
                            defaultActiveKey="1"
                            className="cp-setting-tabs"
                            renderTabBar={() => <ScrollableInkTabBar/>}
                            renderTabContent={() => <TabContent/>}
                          >
                            <TabPane tab="Add Product" key="1">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="orderFrequencyTextV2">Order Frequency Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide order Frequency Text.' : undefined;
                                      }}
                                      id="orderFrequencyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="orderFrequencyTextV2"
                                      placeholder="Order Frequency Text"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="amountLblV2">Amount Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide  Amount Label.' : undefined;
                                      }}
                                      id="amountLblV2"
                                      className="form-control"
                                      type="text"
                                      name="amountLblV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="totalProductsTextV2">Total Products Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Total Products Text' : undefined;
                                      }}
                                      id="totalProductsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="totalProductsTextV2"
                                      placeholder="Total Products Text"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancelFreqBtnTextV2">Cancel Frequency Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Cancel Frequency Button Text.' : undefined;
                                      }}
                                      id="cancelFreqBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cancelFreqBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="productInSubscriptionTextV2">Product Accordion Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide year text' : undefined;
                                      }}
                                      id="productInSubscriptionTextV2"
                                      className="form-control"
                                      type="text"
                                      name="productInSubscriptionTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="productLabelTextV2">Products Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide year text' : undefined;
                                      }}
                                      id="productLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="productLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="skipBadgeTextV2">Skip Badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide skip badge text' : undefined;
                                      }}
                                      id="skipBadgeTextV2"
                                      className="form-control"
                                      type="text"
                                      name="skipBadgeTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectProductLabelTextV2">Select Product Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter select product label text.' : undefined;
                                      }}
                                      id="selectProductLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="selectProductLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="purchaseOptionLabelTextV2">Purchase Option Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter purchase option label text.' : undefined;
                                      }}
                                      id="purchaseOptionLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="purchaseOptionLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="finishLabelTextV2">Finish Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter finish label text.' : undefined;
                                      }}
                                      id="finishLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="finishLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="nextBtnTextV2">Next Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter next button text.' : undefined;
                                      }}
                                      id="nextBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="nextBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="previousBtnTextV2">Previous Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter previous button text.' : undefined;
                                      }}
                                      id="previousBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="previousBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="closeBtnTextV2">Close Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter close button text.' : undefined;
                                      }}
                                      id="closeBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="closeBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deleteConfirmationMsgTextV2">Delete Confirmation Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter delete confirmation message text.' : undefined;
                                      }}
                                      id="deleteConfirmationMsgTextV2"
                                      className="form-control"
                                      type="text"
                                      name="deleteConfirmationMsgTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deleteMsgTextV2">Delete Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter delete message text.' : undefined;
                                      }}
                                      id="deleteMsgTextV2"
                                      className="form-control"
                                      type="text"
                                      name="deleteMsgTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="yesBtnTextV2">Yes Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter yes button text.' : undefined;
                                      }}
                                      id="yesBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="yesBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="noBtnTextV2">No Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter no button text.' : undefined;
                                      }}
                                      id="noBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="noBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="oneTimePurchaseNoteTextV2">One Time Purchase Note Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter one time purchase note text.' : undefined;
                                      }}
                                      id="oneTimePurchaseNoteTextV2"
                                      className="form-control"
                                      type="text"
                                      name="oneTimePurchaseNoteTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="choosePurchaseOptionLabelTextV2">Select Purchase Option Label
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter select purchase option label text.' : undefined;
                                      }}
                                      id="choosePurchaseOptionLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="choosePurchaseOptionLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="clickHereTextV2">Click Here Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter click here text.' : undefined;
                                      }}
                                      id="clickHereTextV2"
                                      className="form-control"
                                      type="text"
                                      name="clickHereTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="productAddMessageTextV2">Product Add Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter product add message text.' : undefined;
                                      }}
                                      id="productAddMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="productAddMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="oneTimePurchaseMessageTextV2">One Time Purchase Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter one time purchase message text.' : undefined;
                                      }}
                                      id="oneTimePurchaseMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="oneTimePurchaseMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="contractUpdateMessageTextV2">Contract Update Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter contract update message text.' : undefined;
                                      }}
                                      id="contractUpdateMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="contractUpdateMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="oneTimePurchaseDisplayMessageTextV2">One Time Purchase Display Message
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter one time purchase dispaly message text.' : undefined;
                                      }}
                                      id="oneTimePurchaseDisplayMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="oneTimePurchaseDisplayMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addProductFinishedMessageTextV2">Add Product Finished Message
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter add product finished message text' : undefined;
                                      }}
                                      id="addProductFinishedMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addProductFinishedMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="sendEmailTextV2">Send Email Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="sendEmailTextV2"
                                      className="form-control"
                                      type="text"
                                      name="sendEmailTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="emailAddressTextV2">Send Email Address Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="emailAddressTextV2"
                                      className="form-control"
                                      type="text"
                                      name="emailAddressTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="emailMagicLinkTextV2">Email Magic Link Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="emailMagicLinkTextV2"
                                      className="form-control"
                                      type="text"
                                      name="emailMagicLinkTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="retriveMagicLinkTextV2">Retrieve Magic Link Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="retriveMagicLinkTextV2"
                                      className="form-control"
                                      type="text"
                                      name="retriveMagicLinkTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="retrieveMagicLinkDescriptionV2">Retrieve Magic Link description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="retrieveMagicLinkDescriptionV2"
                                      className="form-control"
                                      type="text"
                                      name="retrieveMagicLinkDescriptionV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="validEmailMessageV2">Please enter valid email Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="validEmailMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="validEmailMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="fromV2">From</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="fromV2"
                                      className="form-control"
                                      type="text"
                                      name="fromV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="skippedInventoryMGMTTextV2">Skipped inventory managment Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="skippedInventoryMGMTTextV2"
                                      className="form-control"
                                      type="text"
                                      name="skippedInventoryMGMTTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="contractErrorMessageTextV2">Contract Error Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter contract error message text' : undefined;
                                      }}
                                      id="contractErrorMessageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="contractErrorMessageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addToSubscriptionTitleCPV2">Add To Subscription Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter add to subscription title.' : undefined;
                                      }}
                                      id="addToSubscriptionTitleCPV2"
                                      className="form-control"
                                      type="text"
                                      name="addToSubscriptionTitleCPV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="oneTimePurchaseTitleCPV2">One Time Purchase Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter one time purchase title.' : undefined;
                                      }}
                                      id="oneTimePurchaseTitleCPV2"
                                      className="form-control"
                                      type="text"
                                      name="oneTimePurchaseTitleCPV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="seeMoreProductBtnTextV2">See More Product Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter see more product button text.' : undefined;
                                      }}
                                      id="seeMoreProductBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="seeMoreProductBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="queueBadgeTextV2">Queue Badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide queue badge text' : undefined;
                                      }}
                                      id="queueBadgeTextV2"
                                      className="form-control"
                                      type="text"
                                      name="queueBadgeTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addNewButtonTextV2">Add new button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide add new buttom text' : undefined;
                                      }}
                                      id="addNewButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addNewButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="updateChangeOrderBtnTextV2">Change Order Update button</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide change order update button Text.' : undefined;
                                      }}
                                      id="updateChangeOrderBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="updateChangeOrderBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addProductButtonTextV2">Add Product Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide add product button text' : undefined;
                                      }}
                                      id="addProductButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addProductButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="everyLabelTextV2">Every Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="everyLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="everyLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="attributeHeadingTextV2">Attribute Heading Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Attribute Heading Text.' : undefined;
                                      }}
                                      id="attributeHeadingTextV2"
                                      className="form-control"
                                      type="text"
                                      name="attributeHeadingTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="enterOrderNoteLblTextV2">Edit Order note Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="enterOrderNoteLblTextV2"
                                      className="form-control"
                                      type="text"
                                      name="enterOrderNoteLblTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="additionalOrderDetailsTextV2">Additional Order Details Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="additionalOrderDetailsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="additionalOrderDetailsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="minimumNumberOfOrderHeadingTextV2">Minimum Number Of Orders Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="minimumNumberOfOrderHeadingTextV2"
                                      className="form-control"
                                      type="text"
                                      name="minimumNumberOfOrderHeadingTextV2"
                                    />
                                  </FormGroup>
                                </Col>

                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="maximumNumberOfOrderHeadingTextV2">Maximum Number Of Orders Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="maximumNumberOfOrderHeadingTextV2"
                                      className="form-control"
                                      type="text"
                                      name="maximumNumberOfOrderHeadingTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="viewAttributeLabelTextV2">View Attribute Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter View Attribute Label Text.' : undefined;
                                      }}
                                      id="viewAttributeLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="viewAttributeLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="attributeNameLabelTextV2">Attribute Name label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Attribute Name label text.' : undefined;
                                      }}
                                      id="attributeNameLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="attributeNameLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="attributeValueV2">Attribute Value label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Attribute Value label text.' : undefined;
                                      }}
                                      id="attributeValueV2"
                                      className="form-control"
                                      type="text"
                                      name="attributeValueV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="swapProductLabelTextV2">Swap Product Label Text</Label>
                                    <Field
                                      initialValue="to swap the current product."
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Swap Product Label text.' : undefined;
                                      }}
                                      id="swapProductLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="swapProductLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="swapProductSearchBarTextV2">Swap Product Search bar text</Label>
                                    <Field
                                      initialValue="Search a product to swap"
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Swap product search bar Text.' : undefined;
                                      }}
                                      id="swapProductSearchBarTextV2"
                                      className="form-control"
                                      type="text"
                                      name="swapProductSearchBarTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectExistingProductButtonText">Select Product To Swap Button Text</Label>
                                    <Field
                                      initialValue="Select"
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="selectExistingProductButtonText"
                                      className="form-control"
                                      type="text"
                                      name="selectExistingProductButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="swapProductBtnTextV2">Edit Swap Product</Label>
                                    <Field
                                      initialValue="swap product"
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter swap product button text.' : undefined;
                                      }}
                                      id="swapProductBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="swapProductBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addProductSwapButtonTextV2">Swap Product Add</Label>
                                    <Field
                                      initialValue="swap product"
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter swap product button text.' : undefined;
                                      }}
                                      id="addProductSwapButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addProductSwapButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="removeDiscountCodeLabel">Remove product discount popup label</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="removeDiscountCodeLabel"
                                      className="form-control"
                                      type="text"
                                      name="removeDiscountCodeLabel"
                                    />
                                  </FormGroup>
                                </Col>

                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="splitContractText">Split contract button text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="splitContractText"
                                      className="form-control"
                                      type="text"
                                      name="splitContractText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="splitContractMessage">Split contract message</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="splitContractMessage"
                                      className="form-control"
                                      type="text"
                                      name="splitContractMessage"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="freeTrialLoyaltyTextV2">Free Trial Loyalty Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="freeTrialLoyaltyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="freeTrialLoyaltyTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="percentageLoyaltyTextV2">Percentage Loyalty Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="percentageLoyaltyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="percentageLoyaltyTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="amountOffLoyaltyTextV2">Amount Off Loyalty Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="amountOffLoyaltyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="amountOffLoyaltyTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shippingAmountLoyaltyTextV2">Shipping Amount Loyalty Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="shippingAmountLoyaltyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="shippingAmountLoyaltyTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="freeProductLoyaltyTextV2">Free Product Loyalty Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="freeProductLoyaltyTextV2"
                                      className="form-control"
                                      type="text"
                                      name="freeProductLoyaltyTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderAttributesTextV2">Order Attribute label text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="orderAttributesTextV2"
                                      className="form-control"
                                      type="text"
                                      name="orderAttributesTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectSplitMethodLabelText">Select Split Method Label Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="selectSplitMethodLabelText"
                                      className="form-control"
                                      type="text"
                                      name="selectSplitMethodLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="splitWithOrderPlacedSelectOptionText">Split With Order Placed Select Option Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="splitWithOrderPlacedSelectOptionText"
                                      className="form-control"
                                      type="text"
                                      name="splitWithOrderPlacedSelectOptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="splitWithoutOrderPlacedSelectOptionText">Split Without Order Placed Select Option Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="splitWithoutOrderPlacedSelectOptionText"
                                      className="form-control"
                                      type="text"
                                      name="splitWithoutOrderPlacedSelectOptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="outOfStockButtonTextV2">Out Of Stock Button Text</Label>
                                    <Field
                                      initialValue=""
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="outOfStockButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="outOfStockButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Payment Detail" key="2">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="updatePaymentMessageV2">Update Payment Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value
                                          ? 'The request has been sent to your email for change your payment details. If you do not receive mail send another request.'
                                          : undefined;
                                      }}
                                      id="updatePaymentMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="updatePaymentMessageV2"
                                    />
                                    <FormText>{'Customized variables eg. {{customer_email_id}}'}</FormText>
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subTotalLabelTextV2">Subtotal Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Subtotal Label Text' : undefined;
                                      }}
                                      id="subTotalLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="subTotalLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="paymentDetailAccordionTitleV2">Payment Detail Accordion Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide payment Detail Accordion Title' : undefined;
                                      }}
                                      id="paymentDetailAccordionTitleV2"
                                      className="form-control"
                                      type="text"
                                      name="paymentDetailAccordionTitleV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="paymentInfoTextV2">Payment Info Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Payment Info Text' : undefined;
                                      }}
                                      id="paymentInfoTextV2"
                                      className="form-control"
                                      type="text"
                                      name="paymentInfoTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cardTypeTextV2">Card Type Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide card Type Text' : undefined;
                                      }}
                                      id="cardTypeTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cardTypeTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="editFrequencyBtnTextV2">Edit Frequency Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide edit Frequency Btn Text' : undefined;
                                      }}
                                      id="editFrequencyBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="editFrequencyBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="noSubscriptionMessageV2">No Subscription Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide No Subscription Message' : undefined;
                                      }}
                                      id="noSubscriptionMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="noSubscriptionMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cardLastFourDigitTextV2">Last 4 Digits of the Card Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide card Last Four Digit Text' : undefined;
                                      }}
                                      id="cardLastFourDigitTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cardLastFourDigitTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                {/* <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="statusTextV2">Status Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide status Text' : undefined;
                                      }}
                                      id="statusTextV2"
                                      className="form-control"
                                      type="text"
                                      name="statusTextV2"
                                    />
                                  </FormGroup>
                                </Col> */}
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cardExpiryTextV2">Card Expiry Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide card Expiry Text' : undefined;
                                      }}
                                      id="cardExpiryTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cardExpiryTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="editPaymentButtonTextV2">Edit Payment Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide edit payment button text' : undefined;
                                      }}
                                      id="editPaymentButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="editPaymentButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="creditCardTextV2">Credit Card Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide credit card text' : undefined;
                                      }}
                                      id="creditCardTextV2"
                                      className="form-control"
                                      type="text"
                                      name="creditCardTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="endingWithTextV2">Ending With Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide ending with text' : undefined;
                                      }}
                                      id="endingWithTextV2"
                                      className="form-control"
                                      type="text"
                                      name="endingWithTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="nextOrderText">Next Order Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Next Order Text.' : undefined;
                                      }}
                                      id="nextOrderText"
                                      className="form-control"
                                      type="text"
                                      name="nextOrderText"
                                      placeholder="Next Order Text"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="chooseAnotherPaymentMethodTitleText">Choose Another Payment Method Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide choose another payment method title text' : undefined;
                                      }}
                                      id="chooseAnotherPaymentMethodTitleText"
                                      className="form-control"
                                      type="text"
                                      name="chooseAnotherPaymentMethodTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectPaymentMethodTitleText">Select Payment Method</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide select payment method' : undefined;
                                      }}
                                      id="selectPaymentMethodTitleText"
                                      className="form-control"
                                      type="text"
                                      name="selectPaymentMethodTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="changePaymentMessage">Change Payment Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide change payment message.' : undefined;
                                      }}
                                      id="changePaymentMessage"
                                      className="form-control"
                                      type="text"
                                      name="changePaymentMessage"
                                      placeholder="Change Payment Message"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="updatePaymentMethodTitleTextV2">Update payment Method Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide update payment method title text.' : undefined;
                                      }}
                                      id="updatePaymentMethodTitleTextV2"
                                      className="form-control"
                                      type="text"
                                      name="updatePaymentMethodTitleTextV2"
                                      placeholder="Update payment Method Title Text"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Cancel Subscription" key="3">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionBtnText">Cancel Subscription Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Cancel Subscription Button Text.' : undefined;
                                      }}
                                      id="cancelSubscriptionBtnText"
                                      className="form-control"
                                      type="text"
                                      name="cancelSubscriptionBtnText"
                                      placeholder="eg. Cancel Subscription"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cardHolderNameText">Card Holder Name Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide card Holder Name Text' : undefined;
                                      }}
                                      id="cardHolderNameText"
                                      className="form-control"
                                      type="text"
                                      name="cardHolderNameText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="successText">Success Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Success text' : undefined;
                                      }}
                                      id="successText"
                                      className="form-control"
                                      type="text"
                                      name="successText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionConfirmPrepaidText">Cancel Subscription Confirm
                                      Prepaid Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel subscription confirm prepaid text' : undefined;
                                      }}
                                      id="cancelSubscriptionConfirmPrepaidText"
                                      className="form-control"
                                      type="text"
                                      name="cancelSubscriptionConfirmPrepaidText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionConfirmPayAsYouGoText">
                                      Cancel Subscription Confirm Pay As You Go Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel subscription confirm pay as you go text' : undefined;
                                      }}
                                      id="cancelSubscriptionConfirmPayAsYouGoText"
                                      className="form-control"
                                      type="text"
                                      name="cancelSubscriptionConfirmPayAsYouGoText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionPrepaidButtonText">Cancel Subscription Prepaid Button
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel subscription prepaid button text' : undefined;
                                      }}
                                      id="cancelSubscriptionPrepaidButtonText"
                                      className="form-control"
                                      type="text"
                                      name="cancelSubscriptionPrepaidButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionPayAsYouGoButtonText">
                                      Cancel Subscription Pay As You Go Button Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel subscription pay as you go button text' : undefined;
                                      }}
                                      id="cancelSubscriptionPayAsYouGoButtonText"
                                      className="form-control"
                                      type="text"
                                      name="cancelSubscriptionPayAsYouGoButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectCancellationReasonLabelText">Select Cancellation Reason Label
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter select cancellation reason label text.' : undefined;
                                      }}
                                      id="selectCancellationReasonLabelText"
                                      className="form-control"
                                      type="text"
                                      name="selectCancellationReasonLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectCancellationReasonPlaceholderTextV2">Select Cancellation Reason Placeholder Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter select cancellation reason label text.' : undefined;
                                      }}
                                      id="selectCancellationReasonPlaceholderTextV2"
                                      className="form-control"
                                      type="text"
                                      name="selectCancellationReasonPlaceholderTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancellationReasonTextV2">Cancellation Feedback Label</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="cancellationReasonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cancellationReasonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancellationDateTitleText">Cancellation Date Title Text</Label>
                                    <Field
                                      render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancellation date title text.' : undefined;
                                      }}
                                      id="cancellationDateTitleText"
                                      className="form-control"
                                      type="text"
                                      name="cancellationDateTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectedCancellationReasonTitleText">
                                     Selected Cancellation Reason Title Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/> }
                                      validate={value => {
                                        return !value ? 'Please provide selected cancellation reason title text' : undefined;
                                      }}
                                      id="selectedCancellationReasonTitleText"
                                      className="form-control"
                                      type="text"
                                      name="selectedCancellationReasonTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancellationNoteTitleText">Cancellation Note Title Text</Label>
                                    <Field
                                      render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter cancellation note title text.' : undefined;
                                      }}
                                      id="cancellationNoteTitleText"
                                      className="form-control"
                                      type="text"
                                      name="cancellationNoteTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancelAccordionTitle">Accordion Title for Cancel Subscription</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Accordion title for cancel subscription.' : undefined;
                                      }}
                                      id="cancelAccordionTitle"
                                      className="form-control"
                                      type="text"
                                      name="cancelAccordionTitle"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="noCancelTheSubscriptionButtonTextV2">No, Cancel The Subscription Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide No, Cancel The Subscription Button Text.' : undefined;
                                      }}
                                      id="noCancelTheSubscriptionButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="noCancelTheSubscriptionButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="acceptButtonTextV2">Accept Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide accept button text' : undefined;
                                      }}
                                      id="acceptButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="acceptButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancellationReasonPlaceholderV2">Your Cancellation Reason Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide your cancellation reason placeholder text.' : undefined;
                                      }}
                                      id="cancellationReasonPlaceholderV2"
                                      className="form-control"
                                      type="text"
                                      name="cancellationReasonPlaceholderV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="selectCancellationReasonRequiredMsgV2">Select Cancellation Reason Required Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide select cancellation reason required message text.' : undefined;
                                      }}
                                      id="selectCancellationReasonRequiredMsgV2"
                                      className="form-control"
                                      type="text"
                                      name="selectCancellationReasonRequiredMsgV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Upcoming Order" key="4">
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="viewMoreText">View More Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide view more text' : undefined;
                                      }}
                                      id="viewMoreText"
                                      className="form-control"
                                      type="text"
                                      name="viewMoreText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="updateEditShippingButtonText">Update Edit Shipping Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide update edit shipping button text' : undefined;
                                      }}
                                      id="updateEditShippingButtonText"
                                      className="form-control"
                                      type="text"
                                      name="updateEditShippingButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelEditShippingButtonText">Cancel Edit Shipping Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel edit shipping button text' : undefined;
                                      }}
                                      id="cancelEditShippingButtonText"
                                      className="form-control"
                                      type="text"
                                      name="cancelEditShippingButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountNoteTitle">Discount Note Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide discount note title' : undefined;
                                      }}
                                      id="discountNoteTitle"
                                      className="form-control"
                                      type="text"
                                      name="discountNoteTitle"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingFulfillmentText">Upcoming Fulfillment Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide upcoming fulfillment text' : undefined;
                                      }}
                                      id="upcomingFulfillmentText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingFulfillmentText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="fulfilmentTabTitleV2">Fulfilment Tab table Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="fulfilmentTabTitleV2"
                                      className="form-control"
                                      type="text"
                                      name="fulfilmentTabTitleV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupSuccessTitleText">Upcoming Order Success Popup
                                      Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Success Popup Title Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupSuccessTitleText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupSuccessTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupSuccessDescriptionText">
                                      Upcoming Order Success Popup Description Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Success Popup Description Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupSuccessDescriptionText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupSuccessDescriptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupSuccessClosebtnText">
                                      Upcoming Order Success Popup Close button Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Success Popup close button Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupSuccessClosebtnText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupSuccessClosebtnText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupFailureTitleText">Upcoming Order Failure Popup
                                      Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Failure Popup Title Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupFailureTitleText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupFailureTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupFailureDescriptionText">
                                      Upcoming Order Failure Popup Description Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Failure Popup Description Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupFailureDescriptionText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupFailureDescriptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupFailureClosebtnText">
                                      Upcoming Order Failure Popup Close button Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Upcoming Order Failure Popup Close button Text.' : undefined;
                                      }}
                                      id="upcomingOrderChangePopupFailureClosebtnText"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderChangePopupFailureClosebtnText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="frequencyChangeWarningTitle">Frequency Change Warning Title text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Frequency Change Warning Title text.' : undefined;
                                      }}
                                      id="frequencyChangeWarningTitle"
                                      className="form-control"
                                      type="text"
                                      name="frequencyChangeWarningTitle"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="frequencyChangeWarningDescription">Frequency Change Warning
                                      Description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Frequency Change Warning Description.' : undefined;
                                      }}
                                      id="frequencyChangeWarningDescription"
                                      className="form-control"
                                      type="text"
                                      name="frequencyChangeWarningDescription"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pleaseSelectText">Please Select Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter proper text.' : undefined;
                                      }}
                                      id="pleaseSelectText"
                                      className="form-control"
                                      type="text"
                                      name="pleaseSelectText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountCouponRemoveText">Discount Coupon Remove Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Discount Coupon Remove Text.' : undefined;
                                      }}
                                      id="discountCouponRemoveText"
                                      className="form-control"
                                      type="text"
                                      name="discountCouponRemoveText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountCouponNotAppliedText">Discount Coupon Not Applied Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Discount Coupon Not Applied Text.' : undefined;
                                      }}
                                      id="discountCouponNotAppliedText"
                                      className="form-control"
                                      type="text"
                                      name="discountCouponNotAppliedText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="sellingPlanNameText">Selling Plan Name Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter selling plan name text ' : undefined;
                                      }}
                                      id="sellingPlanNameText"
                                      className="form-control"
                                      type="text"
                                      name="sellingPlanNameText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="upcomingTabHeaderHTML">Upcoming Tab Header HTML</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="upcomingTabHeaderHTML"
                                      className="form-control"
                                      type="textarea"
                                      name="upcomingTabHeaderHTML"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="schedulesTabHeaderHTML">Schedules Tab Header HTML</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="schedulesTabHeaderHTML"
                                      className="form-control"
                                      type="textarea"
                                      name="schedulesTabHeaderHTML"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="historyTabHeaderHTML">History Tab Header HTML</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="historyTabHeaderHTML"
                                      className="form-control"
                                      type="textarea"
                                      name="historyTabHeaderHTML"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Shipping Address" key="5">
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="firstNameLabelText">First Name Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide first name label text' : undefined;
                                      }}
                                      id="firstNameLabelText"
                                      className="form-control"
                                      type="text"
                                      name="firstNameLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="lastNameLabelText">Last Name Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide last name label text' : undefined;
                                      }}
                                      id="lastNameLabelText"
                                      className="form-control"
                                      type="text"
                                      name="lastNameLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="phoneLabelText">Phone Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide phone label text' : undefined;
                                      }}
                                      id="phoneLabelText"
                                      className="form-control"
                                      type="text"
                                      name="phoneLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="provinceLabelText">Province Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide province label text' : undefined;
                                      }}
                                      id="provinceLabelText"
                                      className="form-control"
                                      type="text"
                                      name="provinceLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="zipLabelText">Zip Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide zip label text' : undefined;
                                      }}
                                      id="zipLabelText"
                                      className="form-control"
                                      type="text"
                                      name="zipLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addressHeaderTitleText">Address Header Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide address header title text' : undefined;
                                      }}
                                      id="addressHeaderTitleText"
                                      className="form-control"
                                      type="text"
                                      name="addressHeaderTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shippingLabelText">Shipping Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide shipping label text' : undefined;
                                      }}
                                      id="shippingLabelText"
                                      className="form-control"
                                      type="text"
                                      name="shippingLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shippingAddressNotAvailableText">Shipping Address not available
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide shipping address not available text' : undefined;
                                      }}
                                      id="shippingAddressNotAvailableText"
                                      className="form-control"
                                      type="text"
                                      name="shippingAddressNotAvailableText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="address1LabelText">Address 1 Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide address 1 label text' : undefined;
                                      }}
                                      id="address1LabelText"
                                      className="form-control"
                                      type="text"
                                      name="address1LabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="address2LabelText">Address 2 Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide address 2 label text' : undefined;
                                      }}
                                      id="address2LabelText"
                                      className="form-control"
                                      type="text"
                                      name="address2LabelText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="companyLabelText">Company Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide company label text' : undefined;
                                      }}
                                      id="companyLabelText"
                                      className="form-control"
                                      type="text"
                                      name="companyLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="countryLabelText">Country Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide country label text' : undefined;
                                      }}
                                      id="countryLabelText"
                                      className="form-control"
                                      type="text"
                                      name="countryLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cityLabelText">City Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide city label text' : undefined;
                                      }}
                                      id="cityLabelText"
                                      className="form-control"
                                      type="text"
                                      name="cityLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="LocalshippingOptionTextV2">Local Shipping Option Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="LocalshippingOptionTextV2"
                                      className="form-control"
                                      type="text"
                                      name="LocalshippingOptionTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pickupShippingOptionTextV2">Pickup Shipping Option Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="pickupShippingOptionTextV2"
                                      className="form-control"
                                      type="text"
                                      name="pickupShippingOptionTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectShippingMethodLabelTextV2">Select Shipping Option Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="selectShippingMethodLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="selectShippingMethodLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pickupLocationLabelTextV2">Pickup Location Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="pickupLocationLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="pickupLocationLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="noPickupLocationFoundLabelTextV2">No Pickup Location Found Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="noPickupLocationFoundLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="noPickupLocationFoundLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shippingLabelTextV2">Shipping Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="shippingLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="shippingLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="localDeliveryLabelTextV2">Local Delivery Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="localDeliveryLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="localDeliveryLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pickupLabelTextV2">Pickup Label text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="pickupLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="pickupLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="provinceCodeLabelText">Province Code Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Province Code' : undefined;
                                      }}
                                      id="provinceCodeLabelText"
                                      className="form-control"
                                      type="text"
                                      name="provinceCodeLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="countryCodeLabelText">Country Code Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Country Code' : undefined;
                                      }}
                                      id="countryCodeLabelText"
                                      className="form-control"
                                      type="text"
                                      name="countryCodeLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Order Info" key="6">
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelChangeOrderBtnTextV2">Change Order Cancel Button</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide change order cancel button text' : undefined;
                                      // }}
                                      id="cancelChangeOrderBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cancelChangeOrderBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="editChangeOrderBtnText">Change Order Edit button</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide change order edit button text.' : undefined;
                                      }}
                                      id="editChangeOrderBtnText"
                                      className="form-control"
                                      type="text"
                                      name="editChangeOrderBtnText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="nextDeliveryDate">Next Delivery Date Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="nextDeliveryDate"
                                      className="form-control"
                                      type="text"
                                      name="nextDeliveryDate"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="upcomingOrderAccordionTitle">Upcoming Order Accordion Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide upcoming Order Accordion Title' : undefined;
                                      }}
                                      id="upcomingOrderAccordionTitle"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderAccordionTitle"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="updateFreqBtnText">Update Frequency Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Update Frequency Button Text' : undefined;
                                      }}
                                      id="updateFreqBtnText"
                                      className="form-control"
                                      type="text"
                                      name="updateFreqBtnText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="updatePaymentBtnText">Update Payment Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Update Payment Button Text' : undefined;
                                      }}
                                      id="updatePaymentBtnText"
                                      className="form-control"
                                      type="text"
                                      name="updatePaymentBtnText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="paymentNotificationText">Payment Notification Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Update Payment Message' : undefined;
                                      }}
                                      id="paymentNotificationText"
                                      className="form-control"
                                      type="text"
                                      name="paymentNotificationText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="manageSubscriptionButtonText">Manage Subscription Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide manage subscription button Text.' : undefined;
                                      }}
                                      id="manageSubscriptionButtonText"
                                      className="form-control"
                                      type="text"
                                      name="manageSubscriptionButtonText"
                                      placeholder="eg. Manage Subscription"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="addProductLabelTextV2">Search Product Field Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide Search Field Label.' : undefined;
                                      // }}
                                      id="addProductLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addProductLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="paymentMethodTypeText">Payment Method Type Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide payment method type text' : undefined;
                                      }}
                                      id="paymentMethodTypeText"
                                      className="form-control"
                                      type="text"
                                      name="paymentMethodTypeText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="quantityLbl">Quantity Label</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide quantity Label' : undefined;
                                      }}
                                      id="quantityLbl"
                                      className="form-control"
                                      type="text"
                                      name="quantityLbl"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="nextOrderDateLbl">Next Order Date Label</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide next Order Date label' : undefined;
                                      }}
                                      id="nextOrderDateLbl"
                                      className="form-control"
                                      type="text"
                                      name="nextOrderDateLbl"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="orderNoLbl">Order Number Label</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide order No Label' : undefined;
                                      }}
                                      id="orderNoLbl"
                                      className="form-control"
                                      type="text"
                                      name="orderNoLbl"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelButtonTextV2">Cancel Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel button text' : undefined;
                                      }}
                                      id="cancelButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="cancelButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deleteButtonText">Delete button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide delete button text.' : undefined;
                                      }}
                                      id="deleteButtonText"
                                      className="form-control"
                                      type="text"
                                      name="deleteButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="updateButtonText">Update Button text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide update button Text.' : undefined;
                                      }}
                                      id="updateButtonText"
                                      className="form-control"
                                      type="text"
                                      name="updateButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                {/* <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="statusLblV2">Status Label</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide status label' : undefined;
                                      }}
                                      id="statusLblV2"
                                      className="form-control"
                                      type="text"
                                      name="statusLblV2"
                                    />
                                  </FormGroup>
                                </Col> */}
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subscriptionNoText">Subscription Number Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide subscription Number Text' : undefined;
                                      }}
                                      id="subscriptionNoText"
                                      className="form-control"
                                      type="text"
                                      name="subscriptionNoText"
                                    />
                                    <FormText>{'Customized variables eg. {{subscriptionFrequency}}, {{sellingPlanName}}'}</FormText>
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="portalLoginLinkText">Account Link Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Account Link' : undefined;
                                      }}
                                      id="portalLoginLinkText"
                                      className="form-control"
                                      type="text"
                                      name="portalLoginLinkText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="skipOrderButtonText">Skip Order Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Skip Order button text' : undefined;
                                      }}
                                      id="skipOrderButtonText"
                                      className="form-control"
                                      type="text"
                                      name="skipOrderButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="activeBadgeText">Active Order badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Active Order badge text.' : undefined;
                                      }}
                                      id="activeBadgeText"
                                      className="form-control"
                                      type="text"
                                      name="activeBadgeText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="closeBadgeText">Cancelled Order badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide Close Order Badge.' : undefined;
                                      }}
                                      id="closeBadgeText"
                                      className="form-control"
                                      type="text"
                                      name="closeBadgeText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="dayText">Day Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide day text' : undefined;
                                      }}
                                      id="dayText"
                                      className="form-control"
                                      type="text"
                                      name="dayText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="daysTextV2">Days Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="daysTextV2"
                                      className="form-control"
                                      type="text"
                                      name="daysTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="monthText">Month Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide month text' : undefined;
                                      }}
                                      id="monthText"
                                      className="form-control"
                                      type="text"
                                      name="monthText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="monthsTextV2">Months Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}

                                      id="monthsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="monthsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="yearText">Year Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide year text' : undefined;
                                      }}
                                      id="yearText"
                                      className="form-control"
                                      type="text"
                                      name="yearText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="yearsTextV2">Years Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}

                                      id="yearsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="yearsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="seeMoreDetailsText">See More Details Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide day text' : undefined;
                                      }}
                                      id="seeMoreDetailsText"
                                      className="form-control"
                                      type="text"
                                      name="seeMoreDetailsText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="hideDetailsText">Hide Details Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide hide details text' : undefined;
                                      }}
                                      id="hideDetailsText"
                                      className="form-control"
                                      type="text"
                                      name="hideDetailsText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="weekText">Week Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide week text' : undefined;
                                      }}
                                      id="weekText"
                                      className="form-control"
                                      type="text"
                                      name="weekText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="weeksTextV2">Weeks Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="weeksTextV2"
                                      className="form-control"
                                      type="text"
                                      name="weeksTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pauseSubscriptionText">Pause Subscription Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide change pause subscription text' : undefined;
                                      }}
                                      id="pauseSubscriptionText"
                                      className="form-control"
                                      type="text"
                                      name="pauseSubscriptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="resumeSubscriptionText">Resume Subscription Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide resume subscription text' : undefined;
                                      }}
                                      id="resumeSubscriptionText"
                                      className="form-control"
                                      type="text"
                                      name="resumeSubscriptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pauseBadgeText">Pause Badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide pause badge text' : undefined;
                                      }}
                                      id="pauseBadgeText"
                                      className="form-control"
                                      type="text"
                                      name="pauseBadgeText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="productRemovedTooltip">Product Removed Tooltip</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide product removed tooltip' : undefined;
                                      }}
                                      id="productRemovedTooltip"
                                      className="form-control"
                                      type="text"
                                      name="productRemovedTooltip"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deliveryPriceText">Delivery Price Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide delivery price text' : undefined;
                                      }}
                                      id="deliveryPriceText"
                                      className="form-control"
                                      type="text"
                                      name="deliveryPriceText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shippingOptionText">Shipping Option Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide shipping option text' : undefined;
                                      }}
                                      id="shippingOptionText"
                                      className="form-control"
                                      type="text"
                                      name="shippingOptionText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="openBadgeText">Open Badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter open badge text.' : undefined;
                                      }}
                                      id="openBadgeText"
                                      className="form-control"
                                      type="text"
                                      name="openBadgeText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="customerIdText">Customer Id Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Customer Id' : undefined;
                                      }}
                                      id="customerIdText"
                                      className="form-control"
                                      type="text"
                                      name="customerIdText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="helloNameText">Hello Name Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Hello' : undefined;
                                      }}
                                      id="helloNameText"
                                      className="form-control"
                                      type="text"
                                      name="helloNameText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="greetingText">Greeting Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="greetingText"
                                      className="form-control"
                                      type="text"
                                      name="greetingText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="contractCancelledBadgeText">Contract Cancelled Badge Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="contractCancelledBadgeText"
                                      className="form-control"
                                      type="text"
                                      name="contractCancelledBadgeText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="goBackButtonText">Go Back Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Go Back' : undefined;
                                      }}
                                      id="goBackButtonText"
                                      className="form-control"
                                      type="text"
                                      name="goBackButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="editQuantityLabelText">Edit Quantity Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="editQuantityLabelText"
                                      className="form-control"
                                      type="text"
                                      name="editQuantityLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderNoteText">Order Note Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="orderNoteText"
                                      className="form-control"
                                      type="text"
                                      name="orderNoteText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="changeVariantLabelText">Change Variant Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Change Variant' : undefined;
                                      }}
                                      id="changeVariantLabelText"
                                      className="form-control"
                                      type="text"
                                      name="changeVariantLabelText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pleaseWaitLoaderText">Please Wait Loader Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please Wait..' : undefined;
                                      }}
                                      id="pleaseWaitLoaderText"
                                      className="form-control"
                                      type="text"
                                      name="pleaseWaitLoaderText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountCodeText">Discount Code Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter discount code text.' : undefined;
                                      }}
                                      id="discountCodeText"
                                      className="form-control"
                                      type="text"
                                      name="discountCodeText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountCodeApplyButtonText">Discount Code Apply Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter discount code apply button text.' : undefined;
                                      }}
                                      id="discountCodeApplyButtonText"
                                      className="form-control"
                                      type="text"
                                      name="discountCodeApplyButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="fulfilledText">Fulfilled Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter fulfilled text.' : undefined;
                                      }}
                                      id="fulfilledText"
                                      className="form-control"
                                      type="text"
                                      name="fulfilledText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="frequencyChangeWarningTitle">Frequency Change Warning Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Frequency Change Warning Title.' : undefined;
                                      }}
                                      id="frequencyChangeWarningTitle"
                                      className="form-control"
                                      type="text"
                                      name="frequencyChangeWarningTitle"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="frequencyChangeWarningDescription">Frequency Change Warning
                                      Description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter Frequency Change Warning Description.' : undefined;
                                      }}
                                      id="frequencyChangeWarningDescription"
                                      className="form-control"
                                      type="text"
                                      name="frequencyChangeWarningDescription"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="unknownPaymentReachoutUsText">Unknown Payment Reach Out Us Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="unknownPaymentReachoutUsText"
                                      className="form-control"
                                      type="text"
                                      name="unknownPaymentReachoutUsText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="paypalLblText">Paypal Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="paypalLblText"
                                      className="form-control"
                                      type="text"
                                      name="paypalLblText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="shopPayLblText">Shop Pay label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="shopPayLblText"
                                      className="form-control"
                                      type="text"
                                      name="shopPayLblText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="removeDiscountCodeAlertText">Remove Discount COde Alert Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="removeDiscountCodeAlertText"
                                      className="form-control"
                                      type="text"
                                      name="removeDiscountCodeAlertText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addDiscountCodeAlertText">Add Discount Code Alert Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="addDiscountCodeAlertText"
                                      className="form-control"
                                      type="text"
                                      name="addDiscountCodeAlertText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addDiscountCodeText">Add Discount Code Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="addDiscountCodeText"
                                      className="form-control"
                                      type="text"
                                      name="addDiscountCodeText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderTotalText">Order Total Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="orderTotalText"
                                      className="form-control"
                                      type="text"
                                      name="orderTotalText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="hasBeenRemovedText">Has Been Removed Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="hasBeenRemovedText"
                                      className="form-control"
                                      type="text"
                                      name="hasBeenRemovedText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="productLblText">Product Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="productLblText"
                                      className="form-control"
                                      type="text"
                                      name="productLblText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>

                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deleteProductTitleText">Delete Product Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="deleteProductTitleText"
                                      className="form-control"
                                      type="text"
                                      name="deleteProductTitleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="totalLblText">Total Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="totalLblText"
                                      className="form-control"
                                      type="text"
                                      name="totalLblText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="editCommonText">Edit Common Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="editCommonText"
                                      className="form-control"
                                      type="text"
                                      name="editCommonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="editShippingButtonTextV2">Edit Shipping</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="editShippingButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="editShippingButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="variantLblText">Variant Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="variantLblText"
                                      className="form-control"
                                      type="text"
                                      name="variantLblText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="areyousureCommonMessageText">Are you sure common message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="areyousureCommonMessageText"
                                      className="form-control"
                                      type="text"
                                      name="areyousureCommonMessageText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderCreatedDateTextV2">Created Order date Label Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="orderCreatedDateTextV2"
                                      className="form-control"
                                      type="text"
                                      name="orderCreatedDateTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="failureText">Failure Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="failureText"
                                      className="form-control"
                                      type="text"
                                      name="failureText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="saveButtonTextV2">Save Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="saveButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="saveButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderDateTextV2">Order Date Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="orderDateTextV2"
                                      className="form-control"
                                      type="text"
                                      name="orderDateTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="searchProductBtnTextV2">Search Product Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="searchProductBtnTextV2"
                                      className="form-control"
                                      type="text"
                                      name="searchProductBtnTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="selectProductToAdd">Select Product To Add</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="selectProductToAdd"
                                      className="form-control"
                                      type="text"
                                      name="selectProductToAdd"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="minCycleText">Min Cycle Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="minCycleText"
                                      className="form-control"
                                      type="text"
                                      name="minCycleText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="maxCycleText">Max Cycle Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="maxCycleText"
                                      className="form-control"
                                      type="text"
                                      name="maxCycleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="editDeliveryInternalText">Edit Delivery Internal Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="editDeliveryInternalText"
                                      className="form-control"
                                      type="text"
                                      name="editDeliveryInternalText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="deliveryFrequencyText">Delivery Frequency Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="deliveryFrequencyText"
                                      className="form-control"
                                      type="text"
                                      name="deliveryFrequencyText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderSkipAlertTextV2">Upcoming Order Skip Alert Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="upcomingOrderSkipAlertTextV2"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderSkipAlertTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderPlaceNowAlertTextV2">Upcoming Order Place Now Alert
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="upcomingOrderPlaceNowAlertTextV2"
                                      className="form-control"
                                      type="text"
                                      name="upcomingOrderPlaceNowAlertTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderNowText">Order Now Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="orderNowText"
                                      className="form-control"
                                      type="text"
                                      name="orderNowText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="rescheduleText">Reschedule Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="rescheduleText"
                                      className="form-control"
                                      type="text"
                                      name="rescheduleText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="oneTimePurchaseOnlyText">One Time Purchase Only Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="oneTimePurchaseOnlyText"
                                      className="form-control"
                                      type="text"
                                      name="oneTimePurchaseOnlyText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="priceLbl">Price Label Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="priceLbl"
                                      className="form-control"
                                      type="text"
                                      name="priceLbl"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="requireFieldMessage">Required Field Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="requireFieldMessage"
                                      className="form-control"
                                      type="text"
                                      name="requireFieldMessage"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="validNumberRequiredMessage">Valid Number Required Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="validNumberRequiredMessage"
                                      className="form-control"
                                      type="text"
                                      name="validNumberRequiredMessage"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="variantLbl">Variant Label Text </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter valid text' : undefined;
                                      }}
                                      id="variantLbl"
                                      className="form-control"
                                      type="text"
                                      name="variantLbl"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="reschedulingPolicies">Reschedule Description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="reschedulingPolicies" className="form-control" type="textarea" name="reschedulingPolicies" placeholder="Please enter rescheduling policies."/>
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Advanced" key="7">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="discountCouponAppliedText">Discount Coupon Applied Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter discount coupon applied text.' : undefined;
                                      }}
                                      id="discountCouponAppliedText"
                                      className="form-control"
                                      type="text"
                                      name="discountCouponAppliedText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="unableToUpdateSubscriptionStatusMessageText">
                                      Unable To Update Subscription Status Message Text
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter unable to update subscription status message text.' : undefined;
                                      }}
                                      id="unableToUpdateSubscriptionStatusMessageText"
                                      className="form-control"
                                      type="text"
                                      name="unableToUpdateSubscriptionStatusMessageText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subscriptionPausedMessageText">Subscription Paused Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter subscription paused message text.' : undefined;
                                      }}
                                      id="subscriptionPausedMessageText"
                                      className="form-control"
                                      type="text"
                                      name="subscriptionPausedMessageText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subscriptionActivatedMessageText">Subscription Activated Message
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter subscription activated message text.' : undefined;
                                      }}
                                      id="subscriptionActivatedMessageText"
                                      className="form-control"
                                      type="text"
                                      name="subscriptionActivatedMessageText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="popUpSuccessMessageV2">Popup Success Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter popup success message text.' : undefined;
                                      }}
                                      id="popUpSuccessMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="popUpSuccessMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="popUpErrorMessage">Popup Error Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please enter popup error message text.' : undefined;
                                      }}
                                      id="popUpErrorMessage"
                                      className="form-control"
                                      type="text"
                                      name="popUpErrorMessage"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionMinimumBillingIterationsMessage">
                                      Cancel Subscription Minimum Billing Iterations Message
                                    </Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please provide cancel subscription minimum billing iterations message' : undefined;
                                      }}
                                      id="cancelSubscriptionMinimumBillingIterationsMessage"
                                      className="form-control"
                                      type="textarea"
                                      name="cancelSubscriptionMinimumBillingIterationsMessage"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="freezeUpdateSubscriptionMessageV2">Freeze Subscription Update
                                      Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide cancel freeze subscription update message' : undefined;
                                      // }}
                                      id="freezeUpdateSubscriptionMessageV2"
                                      className="form-control"
                                      type="textarea"
                                      name="freezeUpdateSubscriptionMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="subscriptionContractFreezeMessageV2">Subscription contract freeze
                                      message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide subscription contract freeze message' : undefined;
                                      // }}
                                      id="subscriptionContractFreezeMessageV2"
                                      className="form-control"
                                      type="textarea"
                                      name="subscriptionContractFreezeMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="expiredTokenText">Expired Token Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Your magic link is expired.Please access Subscription Portal by logging into your account using same email that you used to buy subscription.' : undefined;
                                      // }}
                                      id="expiredTokenText"
                                      className="form-control"
                                      type="text"
                                      name="expiredTokenText"
                                      placeholder="eg. Your magic link is expired..."
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="noProductFoundMessageV2">No Product Found Message Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Your magic link is expired.Please access Subscription Portal by logging into your account using same email that you used to buy subscription.' : undefined;
                                      // }}
                                      id="noProductFoundMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="noProductFoundMessageV2"
                                      placeholder="eg. Your magic link is expired..."
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="preventCancellationBeforeDaysMessage">Prevent pause/cancel Before Days
                                      Message</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide prevent cancel/pause before Days Message' : undefined;
                                      // }}
                                      id="preventCancellationBeforeDaysMessage"
                                      className="form-control"
                                      type="textarea"
                                      name="preventCancellationBeforeDaysMessage"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="shopPayPaymentUpdateTextV2">ShopPay Payment Update Instruction
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      id="shopPayPaymentUpdateTextV2"
                                      className="form-control"
                                      type="textarea"
                                      name="shopPayPaymentUpdateTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="initialDiscountNoteDescription">Initial Discount Note
                                      Description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide initial discount note description' : undefined;
                                      // }}
                                      id="initialDiscountNoteDescription"
                                      className="form-control"
                                      type="textarea"
                                      name="initialDiscountNoteDescription"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="afterCycleDiscountNoteDescription">After Cycle Discount Note
                                      Description</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      // validate={value => {
                                      //   return !value ? 'Please provide after Cycle discount note description' : undefined;
                                      // }}
                                      id="afterCycleDiscountNoteDescription"
                                      className="form-control"
                                      type="textarea"
                                      name="afterCycleDiscountNoteDescription"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="topHtml">Customer Portal Top HTML</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="topHtml"
                                      className="form-control"
                                      type="textarea"
                                      name="topHtml"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="bottomHtml">Customer Portal Bottom HTML</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      id="bottomHtml"
                                      className="form-control"
                                      type="textarea"
                                      name="bottomHtml"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="addToOrderLabelText">Add to order</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="addToOrderLabelTextV2"
                                      className="form-control"
                                      type="text"
                                      name="addToOrderLabelTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingTabTitleV2">Upcoming tab title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="upcomingTabTitleV2"
                                      className="form-control"
                                      type="text"
                                      name="upcomingTabTitleV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="scheduledTabTitleV2">Schedule tab title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="scheduledTabTitleV2"
                                      className="form-control"
                                      type="text"
                                      name="scheduledTabTitleV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="historyTabTitleV2">History Tab Title text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="historyTabTitleV2"
                                      className="form-control"
                                      type="text"
                                      name="historyTabTitleV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="noOrderNotAvailableMessageV2">No Order Note Added Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="noOrderNotAvailableMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="noOrderNotAvailableMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="continueTextV2">Continue text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="continueTextV2"
                                      className="form-control"
                                      type="text"
                                      name="continueTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="confirmSwapText">Confirm Swap text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmSwapText"
                                      className="form-control"
                                      type="text"
                                      name="confirmSwapText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="confirmAddProduct">Confirm Add Product Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmAddProduct"
                                      className="form-control"
                                      type="text"
                                      name="confirmAddProduct"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountDetailsTitleTextV2">Discount Details Title Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="discountDetailsTitleTextV2"
                                      className="form-control"
                                      type="text"
                                      name="discountDetailsTitleTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="chooseDifferentProductActionText">Choose Different Product Action
                                      text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="chooseDifferentProductActionText"
                                      className="form-control"
                                      type="text"
                                      name="chooseDifferentProductActionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="chooseDifferentProductText">Choose Different Product Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="chooseDifferentProductText"
                                      className="form-control"
                                      type="text"
                                      name="chooseDifferentProductText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="confirmSkipFulfillmentBtnText">Confirm Skip Fulfillment Button
                                      text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmSkipFulfillmentBtnText"
                                      className="form-control"
                                      type="text"
                                      name="confirmSkipFulfillmentBtnText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="confirmSkipOrder">Confirm Skip Order text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmSkipOrder"
                                      className="form-control"
                                      type="text"
                                      name="confirmSkipOrder"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="skipFulfillmentButtonText">Skip Fulfillment Button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="skipFulfillmentButtonText"
                                      className="form-control"
                                      type="text"
                                      name="skipFulfillmentButtonText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="confirmCommonText">Confirm Common Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmCommonText"
                                      className="form-control"
                                      type="text"
                                      name="confirmCommonText"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="orderNowDescriptionText">Order Now Description Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="orderNowDescriptionText"
                                      className="form-control"
                                      type="text"
                                      name="orderNowDescriptionText"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="contractPausedTextV2">Contract Paused Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="contractPausedTextV2"
                                      className="form-control"
                                      type="text"
                                      name="contractPausedTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pastOrderFailedTextV2">Past Order Failed Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="pastOrderFailedTextV2"
                                      className="form-control"
                                      type="text"
                                      name="pastOrderFailedTextV2"
                                    />
                                  </FormGroup>
                                </Col> <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pastOrderTryAgainButtonTextV2">Past Order Try again button Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="pastOrderTryAgainButtonTextV2"
                                      className="form-control"
                                      type="text"
                                      name="pastOrderTryAgainButtonTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="goBackAccountPageTextV2">My Account Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="goBackAccountPageTextV2"
                                      className="form-control"
                                      type="text"
                                      name="goBackAccountPageTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="showSellingPlanIntervalInEditFrequencyV2">Show Selling Plan Interval In Edit Frequency Dropdown</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                      <option value="false">Disabled</option>
                                      <option value="true">Enabled</option>
                                    </Input>}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="showSellingPlanIntervalInEditFrequencyV2"
                                      className="form-control"
                                      type="select"
                                      name="showSellingPlanIntervalInEditFrequencyV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="selectOneTimePurchaseTabByDefaultV2">Select ONE TIME Purchase Tab by default</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                      <option value="false">Disabled</option>
                                      <option value="true">Enabled</option>
                                    </Input>}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="selectOneTimePurchaseTabByDefaultV2"
                                      className="form-control"
                                      type="select"
                                      name="selectOneTimePurchaseTabByDefaultV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="allowAttributeToEditV2">Allow Custom attributes to Edit</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                      <option value="true">Enabled</option>
                                      <option value="false">Disabled</option>
                                    </Input>}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="allowAttributeToEditV2"
                                      className="form-control"
                                      type="select"
                                      name="allowAttributeToEditV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="emailTriggeredSuccesfullyLabelV2">Email Triggered Succesfylly Label Text</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input} />}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="emailTriggeredSuccesfullyLabelV2"
                                      className="form-control"
                                      type="text"
                                      name="emailTriggeredSuccesfullyLabelV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                            <TabPane tab="Rewards" key="8">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="redeemRewardsTextV2">Redeem Rewards Text</Label>
                                    <Field render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="redeemRewardsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="redeemRewardsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="rewardsTextV2">Rewards Text</Label>
                                    <Field render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="rewardsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="rewardsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="yourRewardsTextV2">Your Rewards Text</Label>
                                    <Field render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="yourRewardsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="yourRewardsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="yourAvailableRewardsPointsTextV2">Your Available Rewards Points Text</Label>
                                    <Field render={({input, meta}) =>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null}/>}
                                      id="yourAvailableRewardsPointsTextV2"
                                      className="form-control"
                                      type="text"
                                      name="yourAvailableRewardsPointsTextV2"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </TabPane>
                          </Tabs>
                        </CardBody>
                      </Collapse>
                    </Card>

                    <Card className="main-card mt-3">
                      <CardHeader
                        onClick={() => toggleAccordion(1)}
                        aria-expanded={accordionState[1]}
                        aria-controls="widgetLabelSettingsWrapper"
                        style={{cursor: 'pointer'}}
                      >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal
                        Permission
                        <span style={{...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : ''}}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>
                      <Collapse
                        isOpen={accordionState[1]}
                        data-parent="#accordion"
                        id="widgetLabelSettingsWrapper"
                        aria-labelledby="WidgetLabel"
                      >
                        <FeatureAccessCheck
                          hasAnyAuthorities={'enableCustomerPortalSettings'}
                          upgradeButtonText="Upgrade to enable customer portal permission"
                        >
                          <CardBody>
                            <Row>
                              <Col xs={12} sm={12} md={4} lg={4}>
                                <h5>Control which features are available to your customers when managing a
                                  subscription</h5>
                                <FormText>
                                  <p className="lead text-muted" style={{fontSize: '1rem'}}>
                                    Toggle a customer portal feature on or off to control whether the feature is
                                    available to your customers
                                    from within the customer portal.
                                  </p>
                                </FormText>
                              </Col>
                              <Col xs={12} sm={12} md={8} lg={8}>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="cancelSub"
                                  />
                                  <Label for="cancelSub" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Cancel subscription button
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="allowOrderNow"
                                  />
                                  <Label for="allowOrderNow" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                  Order now button - Allows customers to place a recurring order immediately
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="showShipment"
                                  />
                                  <Label for="showShipment" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Skip order button - Skip the upcoming order
                                  </Label>
                                </FormGroup>
                                {/* <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="allowRe_PlaceTheOrder"
                                  />
                                  <Label for="allowRe_PlaceTheOrder" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Try again button - re-try for placing the last failed order
                                  </Label>
                                </FormGroup> */}
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableSkipFulFillment"
                                  />
                                  <Label for="enableSkipFulFillment" style={{ marginBottom: "0rem", marginLeft: "2rem" }}>Customer
                                    can <b>skip</b> their <b>upcoming fulfillments?</b></Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="pauseResumeSub"
                                  />
                                  <Label for="pauseResumeSub" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Pause and resume a subscription
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="changeOrderFrequency"
                                  />
                                  <Label for="changeOrderFrequency" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Change billing intervals
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="changeNextOrderDate"
                                  />
                                  <Label for="changeNextOrderDate" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Allow changing of the next order date
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="editProductFlag"
                                  />
                                  <Label for="editProductFlag" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Edit<b>&nbsp;product quantities</b>
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="deleteProductFlag"
                                  />
                                  <Label for="deleteProductFlag" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Delete products
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="changeShippingAddressFlag"
                                  />
                                  <Label for="changeShippingAddressFlag"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Change shipping address
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="orderNoteFlag"
                                  />
                                  <Label for="orderNoteFlag" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Add a note to the upcoming order
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="useUrlWithCustomerId"
                                  />
                                  <label for="useUrlWithCustomerId" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Allow login with the Appstle Magic Link
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="updateShipmentBillingDate"
                                  />
                                  <label for="updateShipmentBillingDate"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Change future order dates after the upcoming order
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="discountCode"
                                  />
                                  <label for="discountCode" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Apply discount codes
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <ReactTooltip
                                    html={true}
                                    id="freezeOrderTillMinCycle"
                                    effect="solid"
                                    delayUpdate={500}
                                    place="right"
                                    border={true}
                                    type="info"
                                  />
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={(Boolean(input.value))}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="freezeOrderTillMinCycle"
                                  />
                                  <label for="freezeOrderTillMinCycle"
                                         style={{marginBottom: "0rem", marginLeft: "2rem"}}>Freeze subscription edit until minimum order reached?</label>
                                  <Help
                                    style={{fontSize: '1rem', marginLeft: "1rem"}}
                                    data-for="freezeOrderTillMinCycle"
                                    data-tip={"<div style='max-width:350px'><p>The customer's subscription cannot be edited until the minimum order attempts specified in subscription plan settings have been reached</p><p>Please Note: Regardless of this setting, if specified minimum orders attempts have not been reached, the customer:</p><ul><li>Can add new products to the subscription</li><li>Can not cancel the subscription</li></ul></div>"}/>
                                </FormGroup>

                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableViewAttributes"
                                  />
                                  <label for="enableViewAttributes" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Show product attributes
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableEditAttributes"
                                  />
                                  <label for="enableEditAttributes" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Edit product attributes
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableEditOrderNotes"
                                  />
                                  <label for="enableEditOrderNotes" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Edit order notes
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableTabletForceView"
                                  />
                                  <label for="enableTabletForceView" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Enable tablet view
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="magicLinkEmailFlag"
                                  />
                                  <label for="magicLinkEmailFlag" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    When the magic link has expired, allow new magic link to be sent by email
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableRedirectMyAccountButton"
                                  />
                                  <label for="enableRedirectMyAccountButton" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Allow redirect to the account page within the same tab on the "My Account" click.
                                  </label>
                                </FormGroup>
                                <hr/>
                                <p>
                                  <b>Product Permissions</b>
                                </p>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableAllowOnlyOneDiscountCode"
                                  />
                                  <Label for="enableAllowOnlyOneDiscountCode" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Allow only one discount code to be applied on subscription
                                  </Label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableRedirectToProductPage"
                                    />
                                    <Label for="enableRedirectToProductPage" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                      Allow product details page redirection on product title and image click.
                                    </Label>
                                  </FormGroup>
                                  <FormGroup style={{display: 'flex'}}>
                                    <Field
                                      render={({input, meta}) => (
                                        <Switch
                                          checked={Boolean(input.value)}
                                          onColor="#3ac47d"
                                          onChange={input.onChange}
                                          handleDiameter={20}
                                          boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                          activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                          height={17}
                                          width={36}
                                          id="material-switch"
                                        />
                                      )}
                                      className="form-control"
                                    name="addAdditionalProduct"
                                  />
                                  <Label for="addAdditionalProduct" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Add new products to the subscription
                                  </Label>
                                </FormGroup>
                                <FeatureAccessCheck
                                  hasAnyAuthorities={'accessOneTimeProductUpsells'}
                                  upgradeButtonText="Upgrade your plan"
                                >
                                  <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                    <Field
                                      render={({input, meta}) => (
                                        <Switch
                                          checked={Boolean(input.value)}
                                          onColor="#3ac47d"
                                          onChange={input.onChange}
                                          handleDiameter={20}
                                          boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                          activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                          height={17}
                                          width={36}
                                          id="material-switch"
                                        />
                                      )}
                                      className="form-control"
                                      name="addOneTimeProduct"
                                    />
                                    <label for="addOneTimeProduct" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                      Add one time products
                                    </label>
                                  </FormGroup>
                                </FeatureAccessCheck>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableSwapProductFeature"
                                  />
                                  <label for="enableSwapProductFeature"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Product swap option
                                  </label>
                                </FormGroup>
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="includeOutOfStockProduct"
                                  />
                                  <label for="includeOutOfStockProduct"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    Allow the customer to add out of stock product
                                  </label>
                                </FormGroup>
                                {/* <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={(Boolean(input.value))}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="hideAddSubscriptionProductSection"
                                  />
                                  <label for="hideAddSubscriptionProductSection" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Want to <b>hide add product as a subscription section?</b></label>
                                </FormGroup> */}
                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="allowOnlyOneTimeProductOnAddProductFlag"
                                  />
                                  <label for="allowOnlyOneTimeProductOnAddProductFlag"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    {' '}
                                    Exclude subscription-only products from being added as a one time product
                                  </label>
                                </FormGroup>

                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="removeDiscountCodeAutomatically"
                                  />
                                  <label for="removeDiscountCodeAutomatically"
                                         style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    {' '}
                                    Automatically Remove discount during deleting product from subscription ?
                                  </label>
                                </FormGroup>

                                <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                  <Field
                                    render={({input, meta}) => (
                                      <Switch
                                        checked={Boolean(input.value)}
                                        onColor="#3ac47d"
                                        onChange={input.onChange}
                                        handleDiameter={20}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                        height={17}
                                        width={36}
                                        id="material-switch"
                                      />
                                    )}
                                    className="form-control"
                                    name="enableSwapProductVariant"
                                  />
                                  <label for="enableSwapProductVariant" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                    {' '} Allow changing product variant.
                                  </label>
                                </FormGroup>

                                <FeatureAccessCheck
                                  hasAnyAuthorities={'accessSplitContract'}
                                  upgradeButtonText="Upgrade your plan"
                                >

                                  <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                    <Field
                                      render={({input, meta}) => (
                                        <Switch
                                          checked={Boolean(input.value)}
                                          onColor="#3ac47d"
                                          onChange={input.onChange}
                                          handleDiameter={20}
                                          boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                          activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                          height={17}
                                          width={36}
                                          id="material-switch"
                                        />
                                      )}
                                      className="form-control"
                                      name="enableSplitContract"
                                    />
                                    <label for="enableSplitContract" style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                      {' '}
                                      Enable split contract ?
                                    </label>
                                  </FormGroup>
                                </FeatureAccessCheck>
                              </Col>
                            </Row>
                          </CardBody>
                        </FeatureAccessCheck>
                      </Collapse>
                    </Card>
                    <Card className="main-card mt-3">
                      <CardHeader
                        onClick={() => toggleAccordion(2)}
                        aria-expanded={accordionState[2]}
                        aria-controls="widgetLabelSettingsWrapper"
                        style={{cursor: 'pointer'}}
                      >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal
                        Configuration
                        <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>
                      <Collapse
                        isOpen={accordionState[2]}
                        data-parent="#accordion"
                        id="widgetLabelSettingsWrapper"
                        aria-labelledby="WidgetLabel"
                      >
                        <CardBody>
                          <Row>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="productSelectionOption">Product Selection Option</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                      <option value="ALL_PRODUCTS">All products</option>
                                      <option value="PRODUCTS_FROM_ALL_PLANS">Products from all subscription plans
                                      </option>
                                      <option value="PRODUCTS_FROM_CURRENT_PLAN">
                                        Only products that are available within the customer's original subscription
                                        plan(S)
                                      </option>
                                    </Input>
                                  )}
                                  validate={value => {
                                    return !value ? 'Please Select Retry Attempts.' : undefined;
                                  }}
                                  type="select"
                                  className="form-control"
                                  name="productSelectionOption"
                                />
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="localeDate">Locale Date</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                      <option value="af-NA">Afrikaans (Namibia)</option>
                                      <option value="af-ZA">Afrikaans (South Africa)</option>
                                      <option value="ak-GH">Akan (Ghana)</option>
                                      <option value="sq-AL">Albanian (Albania)</option>
                                      <option value="am-ET">Amharic (Ethiopia)</option>
                                      <option value="ar-DZ">Arabic (Algeria)</option>
                                      <option value="ar-BH">Arabic (Bahrain)</option>
                                      <option value="ar-EG">Arabic (Egypt)</option>
                                      <option value="ar-IQ">Arabic (Iraq)</option>
                                      <option value="ar-JO">Arabic (Jordan)</option>
                                      <option value="ar-KW">Arabic (Kuwait)</option>
                                      <option value="ar-LB">Arabic (Lebanon)</option>
                                      <option value="ar-LY">Arabic (Libya)</option>
                                      <option value="ar-MA">Arabic (Morocco)</option>
                                      <option value="ar-OM">Arabic (Oman)</option>
                                      <option value="ar-QA">Arabic (Qatar)</option>
                                      <option value="ar-SA">Arabic (Saudi Arabia)</option>
                                      <option value="ar-SD">Arabic (Sudan)</option>
                                      <option value="ar-SY">Arabic (Syria)</option>
                                      <option value="ar-TN">Arabic (Tunisia)</option>
                                      <option value="ar-AE">Arabic (United Arab Emirates)</option>
                                      <option value="ar-YE">Arabic (Yemen)</option>
                                      <option value="hy-AM">Armenian (Armenia)</option>
                                      <option value="as-IN">Assamese (India)</option>
                                      <option value="asa-TZ">Asu (Tanzania)</option>
                                      <option value="az-Cyrl">Azerbaijani (Cyrillic)</option>
                                      <option value="az-Latn">Azerbaijani (Latin)</option>
                                      <option value="bm-ML">Bambara (Mali)</option>
                                      <option value="eu-ES">Basque (Spain)</option>
                                      <option value="be-BY">Belarusian (Belarus)</option>
                                      <option value="bem-ZM">Bemba (Zambia)</option>
                                      <option value="bem-ZM">(Zambia)</option>
                                      <option value="bez-TZ">Bena (Tanzania)</option>
                                      <option value="bn-BD">Bengali (Bangladesh)</option>
                                      <option value="bn-IN">Bengali (India)</option>
                                      <option value="bs-BA">Bosnian (Bosnia and Herzegovina)</option>
                                      <option value="bg-BG">Bulgarian (Bulgaria)</option>
                                      <option value="my-MM">Burmese (Myanmar [Burma])</option>
                                      <option value="ca-ES">Catalan (Spain)</option>
                                      <option value="tzm-Latn">Central Morocco Tamazight (Latin)</option>
                                      <option value="chr-US">Cherokee (United States)</option>
                                      <option value="cgg-UG">Chiga (Uganda)</option>
                                      <option value="zh-Hans">Chinese (Simplified Han)</option>
                                      <option value="zh-Hant">Chinese (Traditional Han)</option>
                                      <option value="kw-GB">Cornish (United Kingdom)</option>
                                      <option value="hr-HR">Croatian (Croatia)</option>
                                      <option value="cs-CZ">Czech (Czech Republic)</option>
                                      <option value="da-DK">Danish (Denmark)</option>
                                      <option value="nl-BE">Dutch (Belgium)</option>
                                      <option value="nl-NL">Dutch (Netherlands)</option>
                                      <option value="ebu-KE">Embu (Kenya)</option>
                                      <option value="en-AS">English (American Samoa)</option>
                                      <option value="en-AU">English (Australia)</option>
                                      <option value="en-BE">English (Belgium)</option>
                                      <option value="en-BZ">English (Belize)</option>
                                      <option value="en-BW">English (Botswana)</option>
                                      <option value="en-CA">English (Canada)</option>
                                      <option value="en-GU">English (Guam)</option>
                                      <option value="en-HK">English (Hong Kong SAR China)</option>
                                      <option value="en-IN">English (India)</option>
                                      <option value="en-IE">English (Ireland)</option>
                                      <option value="en-IL">English (Israel)</option>
                                      <option value="en-JM">English (Jamaica)</option>
                                      <option value="en-MT">English (Malta)</option>
                                      <option value="en-MH">English (Marshall Islands)</option>
                                      <option value="en-MU">English (Mauritius)</option>
                                      <option value="en-NA">English (Namibia)</option>
                                      <option value="en-NZ">English (New Zealand)</option>
                                      <option value="en-MP">English (Northern Mariana Islands)</option>
                                      <option value="en-PK">English (Pakistan)</option>
                                      <option value="en-PH">English (Philippines)</option>
                                      <option value="en-SG">English (Singapore)</option>
                                      <option value="en-ZA">English (South Africa)</option>
                                      <option value="en-TT">English (Trinidad and Tobago)</option>
                                      <option value="en-UM">English (U.S. Minor Outlying Islands)</option>
                                      <option value="en-VI">English (U.S. Virgin Islands)</option>
                                      <option value="en-GB">English (United Kingdom)</option>
                                      <option value="en-US">English (United States)</option>
                                      <option value="en-ZW">English (Zimbabwe)</option>
                                      <option value="et-EE">Estonian (Estonia)</option>
                                      <option value="ee-GH">Ewe (Ghana)</option>
                                      <option value="ee-TG">Ewe (Togo)</option>
                                      <option value="fo-FO">Faroese (Faroe Islands)</option>
                                      <option value="fil-PH">Filipino (Philippines)</option>
                                      <option value="fi-FI">Finnish (Finland)</option>
                                      <option value="fr-BE">French (Belgium)</option>
                                      <option value="fr-BJ">French (Benin)</option>
                                      <option value="fr-BF">French (Burkina Faso)</option>
                                      <option value="fr-BI">French (Burundi)</option>
                                      <option value="fr-CM">French (Cameroon)</option>
                                      <option value="fr-CA">French (Canada)</option>
                                      <option value="fr-CF">French (Central African Republic)</option>
                                      <option value="fr-TD">French (Chad)</option>
                                      <option value="fr-KM">French (Comoros)</option>
                                      <option value="fr-CG">French (Congo - Brazzaville)</option>
                                      <option value="fr-CD">French (Congo - Kinshasa)</option>
                                      <option value="fr-CI">French (Côte d’Ivoire)</option>
                                      <option value="fr-DJ">French (Djibouti)</option>
                                      <option value="fr-GQ">French (Equatorial Guinea)</option>
                                      <option value="fr-FR">French (France)</option>
                                      <option value="fr-GA">French (Gabon)</option>
                                      <option value="fr-GP">French (Guadeloupe)</option>
                                      <option value="fr-GN">French (Guinea)</option>
                                      <option value="fr-LU">French (Luxembourg)</option>
                                      <option value="fr-MG">French (Madagascar)</option>
                                      <option value="fr-ML">French (Mali)</option>
                                      <option value="fr-MQ">French (Martinique)</option>
                                      <option value="fr-MC">French (Monaco)</option>
                                      <option value="fr-NE">French (Niger)</option>
                                      <option value="fr-RW">French (Rwanda)</option>
                                      <option value="fr-RE">French (Réunion)</option>
                                      <option value="fr-BL">French (Saint Barthélemy)</option>
                                      <option value="fr-MF">French (Saint Martin)</option>
                                      <option value="fr-SN">French (Senegal)</option>
                                      <option value="fr-CH">French (Switzerland)</option>
                                      <option value="fr-TG">French (Togo)</option>
                                      <option value="ff-SN">Fulah (Senegal)</option>
                                      <option value="gl-ES">Galician (Spain)</option>
                                      <option value="lg-UG">Ganda (Uganda)</option>
                                      <option value="ka-GE">Georgian (Georgia)</option>
                                      <option value="de-AT">German (Austria)</option>
                                      <option value="de-BE">German (Belgium)</option>
                                      <option value="de-DE">German (Germany)</option>
                                      <option value="de-LI">German (Liechtenstein)</option>
                                      <option value="de-LU">German (Luxembourg)</option>
                                      <option value="de-CH">German (Switzerland)</option>
                                      <option value="el-CY">Greek (Cyprus)</option>
                                      <option value="el-GR">Greek (Greece)</option>
                                      <option value="gu-IN">Gujarati (India)</option>
                                      <option value="guz-KE">Gusii (Kenya)</option>
                                      <option value="ha-Latn">Hausa (Latin)</option>
                                      <option value="haw-US">Hawaiian (United States)</option>
                                      <option value="he-IL">Hebrew (Israel)</option>
                                      <option value="hi-IN">Hindi (India)</option>
                                      <option value="hu-HU">Hungarian (Hungary)</option>
                                      <option value="is-IS">Icelandic (Iceland)</option>
                                      <option value="ig-NG">Igbo (Nigeria)</option>
                                      <option value="id-ID">Indonesian (Indonesia)</option>
                                      <option value="ga-IE">Irish (Ireland)</option>
                                      <option value="it-IT">Italian (Italy)</option>
                                      <option value="it-CH">Italian (Switzerland)</option>
                                      <option value="ja-JP">Japanese (Japan)</option>
                                      <option value="kea-CV">Kabuverdianu (Cape Verde)</option>
                                      <option value="kab-DZ">Kabyle (Algeria)</option>
                                      <option value="kl-GL">Kalaallisut (Greenland)</option>
                                      <option value="kln-KE">Kalenjin (Kenya)</option>
                                      <option value="kam-KE">Kamba (Kenya)</option>
                                      <option value="kn-IN">Kannada (India)</option>
                                      <option value="kk-Cyrl">Kazakh (Cyrillic)</option>
                                      <option value="km-KH">Khmer (Cambodia)</option>
                                      <option value="ki-KE">Kikuyu (Kenya)</option>
                                      <option value="rw-RW">Kinyarwanda (Rwanda)</option>
                                      <option value="kok-IN">Konkani (India)</option>
                                      <option value="ko-KR">Korean (South Korea)</option>
                                      <option value="khq-ML">Koyra Chiini (Mali)</option>
                                      <option value="ses-ML">Koyraboro Senni (Mali)</option>
                                      <option value="lag-TZ">Langi (Tanzania)</option>
                                      <option value="lv-LV">Latvian (Latvia)</option>
                                      <option value="lt-LT">Lithuanian (Lithuania)</option>
                                      <option value="luo-KE">Luo (Kenya)</option>
                                      <option value="luy-KE">Luyia (Kenya)</option>
                                      <option value="mk-MK">Macedonian (Macedonia)</option>
                                      <option value="jmc-TZ">Machame (Tanzania)</option>
                                      <option value="kde-TZ">Makonde (Tanzania)</option>
                                      <option value="mg-MG">Malagasy (Madagascar)</option>
                                      <option value="ms-BN">Malay (Brunei)</option>
                                      <option value="ms-MY">Malay (Malaysia)</option>
                                      <option value="ml-IN">Malayalam (India)</option>
                                      <option value="mt-MT">Maltese (Malta)</option>
                                      <option value="gv-GB">Manx (United Kingdom)</option>
                                      <option value="mr-IN">Marathi (India)</option>
                                      <option value="mas-KE">Masai (Kenya)</option>
                                      <option value="mas-TZ">Masai (Tanzania)</option>
                                      <option value="mer-KE">Meru (Kenya)</option>
                                      <option value="mfe-MU">Morisyen (Mauritius)</option>
                                      <option value="naq-NA">Nama (Namibia)</option>
                                      <option value="ne-IN">Nepali (India)</option>
                                      <option value="ne-NP">Nepali (Nepal)</option>
                                      <option value="nd-ZW">North Ndebele (Zimbabwe)</option>
                                      <option value="nb-NO">Norwegian Bokmål (Norway)</option>
                                      <option value="nn-NO">Norwegian Nynorsk (Norway)</option>
                                      <option value="nyn-UG">Nyankole (Uganda)</option>
                                      <option value="or-IN">Oriya (India)</option>
                                      <option value="om-ET">Oromo (Ethiopia)</option>
                                      <option value="om-KE">Oromo (Kenya)</option>
                                      <option value="ps-AF">Pashto (Afghanistan)</option>
                                      <option value="fa-AF">Persian (Afghanistan)</option>
                                      <option value="fa-IR">Persian (Iran)</option>
                                      <option value="pl-PL">Polish (Poland)</option>
                                      <option value="pt-BR">Portuguese (Brazil)</option>
                                      <option value="pt-GW">Portuguese (Guinea-Bissau)</option>
                                      <option value="pt-MZ">Portuguese (Mozambique)</option>
                                      <option value="pt-PT">Portuguese (Portugal)</option>
                                      <option value="pa-Arab">Punjabi (Arabic)</option>
                                      <option value="pa-Guru">Punjabi (Gurmukhi)</option>
                                      <option value="ro-MD">Romanian (Moldova)</option>
                                      <option value="ro-RO">Romanian (Romania)</option>
                                      <option value="rm-CH">Romansh (Switzerland)</option>
                                      <option value="rof-TZ">Rombo (Tanzania)</option>
                                      <option value="ru-MD">Russian (Moldova)</option>
                                      <option value="ru-RU">Russian (Russia)</option>
                                      <option value="ru-UA">Russian (Ukraine)</option>
                                      <option value="rwk-TZ">Rwa (Tanzania)</option>
                                      <option value="saq-KE">Samburu (Kenya)</option>
                                      <option value="sg-CF">Sango (Central African Republic)</option>
                                      <option value="seh-MZ">Sena (Mozambique)</option>
                                      <option value="sr-Cyrl">Serbian (Cyrillic)</option>
                                      <option value="sr-Latn">Serbian (Latin)</option>
                                      <option value="sn-ZW">Shona (Zimbabwe)</option>
                                      <option value="ii-CN">Sichuan Yi (China)</option>
                                      <option value="si-LK">Sinhala (Sri Lanka)</option>
                                      <option value="sk-SK">Slovak (Slovakia)</option>
                                      <option value="sl-SI">Slovenian (Slovenia)</option>
                                      <option value="xog-UG">Soga (Uganda)</option>
                                      <option value="so-DJ">Somali (Djibouti)</option>
                                      <option value="so-ET">Somali (Ethiopia)</option>
                                      <option value="so-KE">Somali (Kenya)</option>
                                      <option value="so-SO">Somali (Somalia)</option>
                                      <option value="es-AR">Spanish (Argentina)</option>
                                      <option value="es-BO">Spanish (Bolivia)</option>
                                      <option value="es-CL">Spanish (Chile)</option>
                                      <option value="es-CO">Spanish (Colombia)</option>
                                      <option value="es-CR">Spanish (Costa Rica)</option>
                                      <option value="es-DO">Spanish (Dominican Republic)</option>
                                      <option value="es-EC">Spanish (Ecuador)</option>
                                      <option value="es-SV">Spanish (El Salvador)</option>
                                      <option value="es-GQ">Spanish (Equatorial Guinea)</option>
                                      <option value="es-GT">Spanish (Guatemala)</option>
                                      <option value="es-HN">Spanish (Honduras)</option>
                                      <option value="es-419">Spanish (Latin America)</option>
                                      <option value="es-MX">Spanish (Mexico)</option>
                                      <option value="es-NI">Spanish (Nicaragua)</option>
                                      <option value="es-PA">Spanish (Panama)</option>
                                      <option value="es-PY">Spanish (Paraguay)</option>
                                      <option value="es-PE">Spanish (Peru)</option>
                                      <option value="es-PR">Spanish (Puerto Rico)</option>
                                      <option value="es-ES">Spanish (Spain)</option>
                                      <option value="es-US">Spanish (United States)</option>
                                      <option value="es-UY">Spanish (Uruguay)</option>
                                      <option value="es-VE">Spanish (Venezuela)</option>
                                      <option value="sw-KE">Swahili (Kenya)</option>
                                      <option value="sw-TZ">Swahili (Tanzania)</option>
                                      <option value="sv-FI">Swedish (Finland)</option>
                                      <option value="sv-SE">Swedish (Sweden)</option>
                                      <option value="gsw-CH">Swiss German (Switzerland)</option>
                                      <option value="shi-Latn">Tachelhit (Latin)</option>
                                      <option value="shi-Tfng">Tachelhit (Tifinagh)</option>
                                      <option value="dav-KE">Taita (Kenya)</option>
                                      <option value="ta-IN">Tamil (India)</option>
                                      <option value="ta-LK">Tamil (Sri Lanka)</option>
                                      <option value="te-IN">Telugu (India)</option>
                                      <option value="teo-KE">Teso (Kenya)</option>
                                      <option value="teo-UG">Teso (Uganda)</option>
                                      <option value="th-TH">Thai (Thailand)</option>
                                      <option value="bo-CN">Tibetan (China)</option>
                                      <option value="bo-IN">Tibetan (India)</option>
                                      <option value="ti-ER">Tigrinya (Eritrea)</option>
                                      <option value="ti-ET">Tigrinya (Ethiopia)</option>
                                      <option value="to-TO">Tonga (Tonga)</option>
                                      <option value="tr-TR">Turkish (Turkey)</option>
                                      <option value="uk-UA">Ukrainian (Ukraine)</option>
                                      <option value="ur-IN">Urdu (India)</option>
                                      <option value="ur-PK">Urdu (Pakistan)</option>
                                      <option value="uz-Arab">Uzbek (Arabic)</option>
                                      <option value="uz-Cyrl">Uzbek (Cyrillic)</option>
                                      <option value="uz-Latn">Uzbek (Latin)</option>
                                      <option value="vi-VN">Vietnamese (Vietnam)</option>
                                      <option value="vun-TZ">Vunjo (Tanzania)</option>
                                      <option value="cy-GB">Welsh (United Kingdom)</option>
                                      <option value="yo-NG">Yoruba (Nigeria)</option>
                                      <option value="zu-ZA">Zulu (South Africa)</option>
                                    </Input>
                                  )}
                                  validate={value => {
                                    return !value ? 'Please Select Locale Date.' : undefined;
                                  }}
                                  type="select"
                                  className="form-control"
                                  name="localeDate"
                                />
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="dateFormat">Date format for Customer Portal</Label>
                                <Field
                                  render={({input, meta}) => <Input {...input}
                                                                    invalid={meta.error && meta.touched ? true : null}/>}
                                  validate={value => {
                                    return !value ? 'Please enter Date format' : undefined;
                                  }}
                                  initialValue="dd-MM-yyyy"
                                  id="dateFormat"
                                  className="form-control"
                                  type="text"
                                  name="dateFormat"
                                />
                                <small>
                                  Please visit{' '}
                                  <a target="_blank" href="https://date-fns.org/v2.25.0/docs/format">
                                    this link
                                  </a>{' '}
                                  to know more about date formats.
                                </small>
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="showSellingPlanFrequencies">Show Selling Plan Frequencies</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <Input type="select" {...input}>
                                      <option value="true">Enable</option>
                                      <option value="false">Disable</option>
                                    </Input>
                                  )}
                                  className="form-control"
                                  name="showSellingPlanFrequencies"
                                />
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="allowDeleteForBuildABoxProductV2">Allow Delete for Build a Box</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <Input type="select" {...input}>
                                      <option value="false">Disable</option>
                                      <option value="true">Enable</option>
                                    </Input>
                                  )}
                                  className="form-control"
                                  name="allowDeleteForBuildABoxProductV2"
                                />
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <ReactTooltip
                                    html={true}
                                    id="productsSizePerPageV2"
                                    effect="solid"
                                    delayUpdate={500}
                                    place="right"
                                    border={true}
                                    type="info"
                                  />
                                <Label for="productsSizePerPageV2">Products size per page</Label>
                                <HelpTooltip>
                                <p><b>Swap Product</b> and <b>Add Product</b> options are available in the Customer Portal. In the Product section, we currently display 10 default products. If you wish to load additional products, you can adjust the product loading size here.</p><ui><li>The value of the <b>Products size per page</b> field must be between 1 and 100.</li></ui>
                                </HelpTooltip>
                                <Field
                                  render={({input, meta}) => <Input {...input}
                                                                    invalid={meta.error && meta.touched ? true : null}/>}
                                  validate={value => {
                                    return !value || value <= 0
                                    ? 'Please provide a minimum value of 1.'
                                    : value > 100
                                    ? 'Please provide a maximum value of 100.'
                                    : undefined;
                                  }}
                                  id="productsSizePerPageV2"
                                  className="form-control"
                                  type="number"
                                  name="productsSizePerPageV2"
                                  placeholder="eg. 1"
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="minQtyToAllowDuringAddProduct">Minimum Quantity required while add/edit/swap
                                  product</Label>
                                <Field
                                  render={({input, meta}) => <Input {...input}
                                                                    invalid={meta.error && meta.touched ? true : null}/>}
                                  validate={value => {
                                    return !value ? 'Please provide minimum qty value.' : undefined;
                                  }}
                                  id="minQtyToAllowDuringAddProduct"
                                  className="form-control"
                                  type="number"
                                  name="minQtyToAllowDuringAddProduct"
                                  placeholder="eg. 1"
                                />
                              </FormGroup>
                            </Col>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="datePickerEnabledDaysV2">Enabled Days to Reschedule Order</Label>
                                <Field
                                  key="datePickerEnabledDaysV2"
                                  id="datePickerEnabledDaysV2"
                                  name="datePickerEnabledDaysV2"
                                  component={ReactSelectAdapter}
                                  options={days}
                                  placeholder="Select Enabled Days"
                                />
                                <small>
                                  Please don't edit if you are not sure about this. contact support if you wanna make
                                  changes.
                                </small>
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                <Field
                                  render={({input, meta}) => (
                                    <Switch
                                      checked={Boolean(input.value)}
                                      onColor="#3ac47d"
                                      onChange={input.onChange}
                                      handleDiameter={20}
                                      boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                      activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                      height={17}
                                      width={36}
                                      id="material-switch"
                                    />
                                  )}
                                  className="form-control"
                                  name="applySellingPlanBasedDiscount"
                                />
                                <label for="applySellingPlanBasedDiscount"
                                       style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                  <span>Apply discount to the product being added based on the product Selling Plan?</span>
                                  <br/>
                                  <span>
                                    (If not selected then discount will be applied based on existing products in the subscription)
                                  </span>
                                </label>
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                <Field
                                  render={({input, meta}) => (
                                    <Switch
                                      checked={Boolean(input.value)}
                                      onColor="#3ac47d"
                                      onChange={input.onChange}
                                      handleDiameter={20}
                                      boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                      activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                      height={17}
                                      width={36}
                                      id="material-switch"
                                    />
                                  )}
                                  className="form-control"
                                  name="applySubscriptionDiscountForOtp"
                                />
                                <label for="applySubscriptionDiscountForOtp"
                                       style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                  <span>Apply discounts on one time products</span>
                                  <br/>
                                  <span>
                                    (If not selected then no discount will be applied)
                                  </span>
                                </label>
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            <Col xs={12} sm={12} md={4} lg={4}>
                              <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                <Field
                                  render={({input, meta}) => (
                                    <Switch
                                      checked={Boolean(input.value)}
                                      onColor="#3ac47d"
                                      onChange={input.onChange}
                                      handleDiameter={20}
                                      boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                      activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                      height={17}
                                      width={36}
                                      id="material-switch"
                                    />
                                  )}
                                  className="form-control"
                                  name="applySubscriptionDiscount"
                                />
                                <label for="applySubscriptionDiscount"
                                       style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                  Offer discount for adding product to subscription?
                                </label>
                              </FormGroup>
                            </Col>
                          </Row>
                          {values?.applySubscriptionDiscount && (
                            <Row>
                              <Col xs={12} sm={12} md={4} lg={4}>
                                <FormGroup>
                                  <Label for="subscriptionDiscountTypeUnit">Subscription Discount Type</Label>
                                  <Field
                                    render={({input, meta}) => (
                                      <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                        <option value="PERCENTAGE">PERCENTAGE</option>
                                        <option value="AMOUNT">AMOUNT</option>
                                      </Input>
                                    )}
                                    validate={value => {
                                      return !value ? 'Please Select Subscription Discount Type.' : undefined;
                                    }}
                                    type="select"
                                    className="form-control"
                                    name="subscriptionDiscountTypeUnit"
                                  />
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={4} lg={4}>
                                <FormGroup>
                                  <Label for="subscriptionDiscount">Subscription Discount</Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input}
                                                                      invalid={meta.error && meta.touched ? true : null}/>}
                                    validate={value => {
                                      return !value ? 'Please provide  subscription discount.' : undefined;
                                    }}
                                    id="subscriptionDiscount"
                                    className="form-control"
                                    type="number"
                                    name="subscriptionDiscount"
                                    placeholder="Subscription Discount"
                                  />
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={4} lg={4}>
                                <FormGroup>
                                  <Label for="upSellMessage">Upsell Message</Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input}
                                                                      invalid={meta.error && meta.touched ? true : null}/>}
                                    validate={value => {
                                      return !value ? 'Please provide Upsell Message.' : undefined;
                                    }}
                                    id="upSellMessage"
                                    className="form-control"
                                    type="text"
                                    name="upSellMessage"
                                    placeholder="Upsell Message"
                                  />
                                </FormGroup>
                              </Col>
                            </Row>
                          )}

                        {/* Settings replaced by the settings in Cancellation Management (V2)
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                <Field
                                  render={({input, meta}) => (
                                    <Switch
                                      checked={Boolean(input.value)}
                                      onColor="#3ac47d"
                                      onChange={input.onChange}
                                      handleDiameter={20}
                                      boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                      activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                      height={17}
                                      width={36}
                                      id="material-switch"
                                    />
                                  )}
                                  className="form-control"
                                  name="offerDiscountOnCancellation"
                                />
                                <label for="offerDiscountOnCancellation"
                                       style={{marginBottom: '0rem', marginLeft: '2rem'}}>
                                  Offer discount on cancellation of subscription?
                                </label>
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                              {values?.offerDiscountOnCancellation ? (
                                <>
                                  <FormGroup>
                                    <Label for="discountAccordionTitle">Discount Accordion Title</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please add Discount Accordion Title' : undefined;
                                      }}
                                      type="text"
                                      className="form-control"
                                      name="discountAccordionTitle"
                                    />
                                  </FormGroup>
                                </>
                              ) : (
                                <></>
                              )}
                            </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                              {values?.offerDiscountOnCancellation ? (
                                <>
                                  <FormGroup>
                                    <Label for="discountRecurringCycleLimitOnCancellation">Recurring Cycle Limit On
                                      Cancellation</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please add Recurring Cycle Limit On Cancellation.' : undefined;
                                      }}
                                      type="number"
                                      className="form-control"
                                      name="discountRecurringCycleLimitOnCancellation"
                                    />
                                  </FormGroup>
                                </>
                              ) : (
                                <></>
                              )}
                            </Col>
                            <Col>
                              {values?.offerDiscountOnCancellation ? (
                                <>
                                  <FormGroup>
                                    <Label for="discountPercentageOnCancellation">Discount Percentage On
                                      Cancellation</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please Add Discount Percentage On Cancellation.' : undefined;
                                      }}
                                      type="text"
                                      className="form-control"
                                      name="discountPercentageOnCancellation"
                                    />
                                  </FormGroup>
                                </>
                              ) : (
                                <></>
                              )}
                            </Col>
                            <Col xs={12}>
                              {values?.offerDiscountOnCancellation ? (
                                <>
                                  <FormGroup>
                                    <Label for="discountMessageOnCancellation">Discount Message On Cancellation</Label>
                                    <Field
                                      render={({input, meta}) => <Input {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}/>}
                                      validate={value => {
                                        return !value ? 'Please Add Discount Message On Cancellation' : undefined;
                                      }}
                                      type="text"
                                      className="form-control"
                                      name="discountMessageOnCancellation"
                                    />
                                  </FormGroup>
                                </>
                              ) : (
                                <></>
                              )}
                            </Col>
                          </Row>*/}
                        </CardBody>
                      </Collapse>
                    </Card>

                    <Card className="main-card mt-3">
                      <CardHeader
                        onClick={() => toggleAccordion(3)}
                        aria-expanded={accordionState[3]}
                        aria-controls="widgetLabelSettingsWrapper"
                        style={{cursor: 'pointer'}}
                      >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal Advanced
                        Setup
                        <span style={{...forward_arrow_icon, transform: accordionState[3] ? 'rotate(90deg)' : ''}}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>

                        <Collapse
                          isOpen={accordionState[3]}
                          data-parent="#accordion"
                          id="widgetLabelSettingsWrapper"
                          aria-labelledby="WidgetLabel"
                        >
                          <FeatureAccessCheck hasAnyAuthorities="accessAdvancedCustomerPortalSettings">
                          <CardBody>
                            <Row>
                              <Col xs={12} sm={12} md={12} lg={12}>
                                <FormGroup>
                                  <Label for="preventCancellationBeforeDays">
                                    Prevent Subscription Cancellation/Pause/Skip before Days (from Next Order Date)
                                  </Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input}
                                                                      invalid={meta.error && meta.touched ? true : null}/>}
                                    validate={value => {
                                      return value && value <= 0 ? 'Please provide valid value of days.' : undefined;
                                    }}
                                    id="preventCancellationBeforeDays"
                                    className="form-control"
                                    type="number"
                                    name="preventCancellationBeforeDays"
                                  />
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={12} lg={12}>
                                <FormGroup>
                                  <Label for="variantIdsToFreezeEditRemove">
                                    Add Variant Ids those are disabled from edit/remove from your existing subscription.
                                  </Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input} />}
                                    id="variantIdsToFreezeEditRemove"
                                    className="form-control"
                                    type="text"
                                    name="variantIdsToFreezeEditRemove"
                                  />
                                  <FormText>Add comma separated ids. eg. 1111111,2222222,333333</FormText>
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={12} lg={12}>
                                <FormGroup>
                                  <Label for="disAllowVariantIdsForOneTimeProductAdd">
                                    Add Variant Ids that can't be added for one time purchase.
                                  </Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input} />}
                                    id="disAllowVariantIdsForOneTimeProductAdd"
                                    className="form-control"
                                    type="text"
                                    name="disAllowVariantIdsForOneTimeProductAdd"
                                  />
                                  <FormText>Add comma separated ids. eg. 1111111,2222222,333333</FormText>
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={12} lg={12}>
                                <FormGroup>
                                  <Label for="allowedProducttIdsForOneTimeProductAdd">
                                    Add Product Ids to be added as one time purchase.
                                  </Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input} />}
                                    id="allowedProductIdsForOneTimeProductAdd"
                                    className="form-control"
                                    type="text"
                                    name="allowedProductIdsForOneTimeProductAdd"
                                  />
                                  <FormText>Add comma separated ids. eg. 1111111,2222222,333333</FormText>
                                </FormGroup>
                              </Col>
                              <Col xs={12} sm={12} md={12} lg={12}>
                                <FormGroup>
                                  <Label for="disAllowVariantIdsForSubscriptionProductAdd">
                                    Add Variant Ids that can't be added for subscription purchase.
                                  </Label>
                                  <Field
                                    render={({input, meta}) => <Input {...input} />}
                                    id="disAllowVariantIdsForSubscriptionProductAdd"
                                    className="form-control"
                                    type="text"
                                    name="disAllowVariantIdsForSubscriptionProductAdd"
                                  />
                                  <FormText>Add comma separated ids. eg. 1111111,2222222,333333</FormText>
                                </FormGroup>
                              </Col>
                            </Row>
                          </CardBody>
                          </FeatureAccessCheck>

                        </Collapse>
                    </Card>
                    <Card className="main-card mt-3">
                      <CardHeader onClick={() => toggleAccordion(4)} aria-expanded={accordionState[4]}
                        aria-controls="widgetLabelSettingsWrapper" style={{ cursor: 'pointer' }} >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal Customization
                        <span style={{ ...forward_arrow_icon, transform: accordionState[4] ? 'rotate(90deg)' : '' }}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>
                      <Collapse
                        isOpen={accordionState[4]}
                        data-parent="#accordion"
                        id="widgetLabelSettingsWrapper"
                        aria-labelledby="WidgetLabel"
                      >
                        <CardBody>
                          <Row>
                            {shopCustomizationFields.map((field, index) => <>{
                              field?.type == 'TOGGLE' ? (
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup className={'d-flex align-items-center'}>
                                    <Field
                                      render={({ input, meta }) => <ShopCustomizationField field={field} input={input} />}
                                      id={`shopCustomizationData[${index}]`}
                                      className="form-control"
                                      type="text"
                                      name={`shopCustomizationData[${index}]`}
                                    />
                                    <Label for={field?.id} style={{ marginBottom: '0rem', marginLeft: '2rem' }}> {field?.label} </Label>
                                  </FormGroup>
                                </Col>
                              ) : (
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for={field?.id}> {field?.label} </Label>
                                    <Field
                                      render={({ input, meta }) => <ShopCustomizationField field={field} input={input} />}
                                      id={`shopCustomizationData[${index}]`}
                                      className="form-control"
                                      type="text"
                                      name={`shopCustomizationData[${index}]`}
                                    />
                                  </FormGroup>
                                </Col>
                              )
                            }</>)}
                          </Row>
                        </CardBody>
                      </Collapse>
                    </Card>
                    <Card className="main-card mt-3">
                      <CardHeader onClick={() => toggleAccordion(5)} aria-expanded={accordionState[5]}
                        aria-controls="widgetLabelSettingsWrapper" style={{ cursor: 'pointer' }} >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Customer Portal Product Filter Menu
                        <span style={{ ...forward_arrow_icon, transform: accordionState[5] ? 'rotate(90deg)' : '' }}>
                          <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                      </CardHeader>
                      <Collapse
                        isOpen={accordionState[5]}
                        data-parent="#accordion"
                        id="widgetLabelSettingsWrapper"
                        aria-labelledby="WidgetLabel"
                      >
                        <CardBody>
                          <ManageFilterData
                            values={values}
                            settingEntity={customerPortalSettingEntity}
                            productFilterConfig={productFilterConfig}
                            module={'CustomerPortalSetting'}
                            setProductFilterConfig={setProductFilterConfig} />
                        </CardBody>
                      </Collapse>
                    </Card>
                    <FAQSection title="Customer Portal FAQs" icon="lnr-store" isAllInitialOpen className="mt-3">
                      <FAQEntry question="How do customers manage the subscription?">
                        The customers will be able to manage the subscription from the customer portal.
                      </FAQEntry>
                      <FAQEntry question="How can customers access the customer portal?">
                        There are two ways through which customers can access the customer portal and manage their subscriptions:
                        <ol className="mb-0">
                          <li>
                            When customers purchase a product, they are sent an email (if they provided their own) with their magic link that will take them to their customer portal.
                            (As per Shopify limitations, it automatically gets expired every 7 days.)
                          </li>
                          <li>
                            Customers can create an account on your Shopify store and login, where they can see the "Manage Subscription" button.
                            Clicking it will lead them to the customer portal where they can manage their subscriptions.
                          </li>
                        </ol>
                      </FAQEntry>
                    </FAQSection>
                  </div>
                </form>
              </>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  loading: state.customerPortalSettings.loading,
  updating: state.customerPortalSettings.updating,
  updateSuccess: state.customerPortalSettings.updateSuccess,
  shopCustomizationFields: state.shopCustomization.shopCustomizationFields,
  customizationFiledLoading: state.shopCustomization.loading
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity,
  getEntitiesByCategory
};

export default connect(mapStateToProps, mapDispatchToProps)(customerPortalSetting);
