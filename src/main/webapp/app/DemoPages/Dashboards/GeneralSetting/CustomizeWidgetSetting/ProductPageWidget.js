import React, {Fragment, useEffect, useState, useRef} from 'react';
import Loader from 'react-loaders';
//import './setting.scss';
import {
  Card,
  CardBody,
  CardHeader,
  CardFooter,
  Col,
  Collapse,
  FormGroup,
  FormText,
  Input,
  Button,
  InputGroup,
  Label,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row
} from 'reactstrap';
import { Tooltip as ReactTooltip } from "react-tooltip";
import Switch from 'react-switch';
import {Field, Form, FormSpy} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect, useSelector} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getEntity,
  getEntities,
  reset,
  updateEntity
} from "app/entities/subscription-widget-settings/subscription-widget-settings.reducer";

import {
    getEntity as getThemeSettingEntity,
    getEntities as getThemeSettingEntities,
    updateEntity as updateThemeSettingEntity
  } from "app/entities/theme-settings/theme-settings.reducer";

import {data} from './subscription-customisation-trimmed.js'
import {DropdownList} from 'react-widgets';
import ColorPicker2 from '../../Utilities/ColorPicker2.js';
import { ChevronForward } from 'react-ionicons';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
// import * as widgetImage from "../../../../../static/theme/assets/utils/images/widget-setting";
// import  from "../../../../../static/theme/assets/utils/images/widget-setting/";
import selected_discount_format_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/selected_discount_format_text.png';
import manageSubDetails from '../../../../../static/theme/assets/utils/images/widget-setting/checkoutPageSubdetail.png';
import default_tooltip_description
  from '../../../../../static/theme/assets/utils/images/widget-setting/default_tooltip_description.png';
import onetimepurchaseText
  from '../../../../../static/theme/assets/utils/images/widget-setting/onetimepurchaseText.png';
import prepaid_plan_description_on_tooltip
  from '../../../../../static/theme/assets/utils/images/widget-setting/prepaid_plan_description_on_tooltip.png';
import discount_tier_explaination_on_tooltip
  from '../../../../../static/theme/assets/utils/images/widget-setting/discount_tier_explaination_on_tooltip.png';
import deliveryfreLabel from '../../../../../static/theme/assets/utils/images/widget-setting/deliveryfreLabel.png';
import manageSubButtonText
  from '../../../../../static/theme/assets/utils/images/widget-setting/manageSubButtonText.png';
import one_time_price_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/one_time_price_text.png';
import accPageManagesubbtnHtml
  from '../../../../../static/theme/assets/utils/images/widget-setting/accPageManagesubbtnHtml.png';
import orderSummerySubscriptionTitle
  from '../../../../../static/theme/assets/utils/images/widget-setting/orderSummerySubscriptionTitle.png';
import purchase_options_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/purchase_options_text.png';
import selected_pay_as_you_go_selling_plan_price_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/selected_pay_as_you_go_selling_plan_price_text.png';
import selling_plan_title_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/selling_plan_title_text.png';
import subscription_option_text
  from '../../../../../static/theme/assets/utils/images/widget-setting/subscription_option_text.png';
  import off_frequency_example
    from '../../../../../static/theme/assets/utils/images/widget-setting/offFrequencyExample.png';

import sellingplan_type_text from '../../../../../static/theme/assets/utils/images/widget-setting/sellingplan_type_text.png';

import tooltip_title from '../../../../../static/theme/assets/utils/images/widget-setting/tooltip_title.png';
import fequencyText from '../../../../../static/theme/assets/utils/images/widget-setting/fequencyText.png'
import {Help} from '@mui/icons-material';
import {IntercomAPI} from 'react-intercom';
import {Link} from 'react-router-dom';
import WidgetTypes from '../../WidgetTypes/WidgetTypes.jsx';
import SubscriptionCustomCSS from '../../SubscriptionCustomCss/SubscriptionCustomCSS.jsx';
import HTMLTextEditor from '../../../Forms/Components/WysiwygEditor/HTMLTextEditor.js';
import ShopCustomizationField from '../ShopCustomization/ShopCustomizationField.js';
import { getEntitiesByCategory } from 'app/entities/shop-customization/shop-customization.reducer';
import { completeChecklistItem } from 'app/entities/onboarding-info/onboarding-info.reducer'
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';

const ProductPageWidget = ({subWidgetSettingEntity, getEntities, getEntity,
    customizationFiledLoading,
    getThemeSettingEntity,
    updateThemeSettingEntity,
    shopCustomizationFields,
    getEntitiesByCategory,
     updateEntity, createEntity, completeChecklistItem, ...props}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([false, false, false, false, false, false, false]);
  const [cssAccordianState, setCssAccordianState] = useState([]);
  const [subWidgetSettingEntityFormState, setSubWidgetSettingEntityFormState] = useState({})

   const paymentData = useSelector(state => state.paymentPlan.entity);
   const widgetTypeFormSubmitRef = useRef(null);
   const customCSSFormSubmitRef = useRef(null);


  useEffect(() => {
    // getEntities()
    getEntitiesByCategory("PRODUCT_PAGE_WIDGET")
    getEntity(0)
    setCssAccordianState([...Object.keys(data).map((el, index) => (index === 0 ? true : false))])
    completeChecklistItem(OnboardingChecklistStep.APPSTLE_WIDGET);
  }, [])

  useEffect(() => {
    getThemeSettingEntity(0);
  }, [])


  const saveEntity = async values => {
      console.log(values);
    await updateEntity(values);
    // updateThemeSettingEntity(values);
  };

  const toggleAccordion = (tab) => {
    const prevState = accordionState;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  }

  const validateForNumber = (value, field) => (value ? (isNaN(value) ? `${field} - Must be a number` : undefined) : undefined);

  const toggleCssAccordion = (tab) => {
    const prevState = cssAccordianState;
    const state = prevState.map((x, index) => (tab === index ? !x : x));
    setCssAccordianState(state);
  }

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  }

  const widgetTypes = [{
    value: "WIDGET_TYPE_1",
    image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_11.08.34_AM_lll4oMORU.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657085961088"
  },
    {
      value: "WIDGET_TYPE_2",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_12.48.54_AM_m1rEUFf7R.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657048821287",

    }, {
      value: "WIDGET_TYPE_3",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_1.06.07_AM_KWdIdqgF2.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657049793940",

    }, {
      value: "WIDGET_TYPE_4",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_12.52.35_AM_9n_TIjH86.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657048982249",

    }];

  let submit;
  let widgetTypeSubmit;
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
          heading="Subscription Widget Settings"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn about customizing your subscription widget.</a>"

          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
            // widgetTypeSubmit();
            widgetTypeFormSubmitRef.current?.click();
            customCSSFormSubmitRef?.current?.click();
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
                url: "https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget"
              }
            ]
          }}
        />

                <Card className="main-card mt-3">
                    <CardHeader
                            onClick={() => (toggleAccordion(5))}
                            aria-expanded={accordionState[5]}
                            aria-controls="widgetTypes"
                            style={{cursor: 'pointer'}}>
                            <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget Types
                            <span style={{...forward_arrow_icon, transform: accordionState[5] ? 'rotate(90deg)' : ''}}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                            </CardHeader>
                            <Collapse isOpen={accordionState[5]} data-parent="#accordion" id="widgetTypes"
                                    aria-labelledby="WidgetLabel">
                            <CardBody>
                                <FeatureAccessCheck
                                 hasAnyAuthorities={'accessWidgetDesignOptions'}
                                 upgradeButtonText="Upgrade your plan"
                                >

                                <WidgetTypes
                                widgetTypeSubmit={widgetTypeSubmit}
                                widgetTypeFormSubmitRef = {widgetTypeFormSubmitRef}
                                subWidgetSettingEntityFormState={subWidgetSettingEntityFormState}
                                />
                                </FeatureAccessCheck>
                        </CardBody>
                    </Collapse>
                  </Card>

                <Form
                initialValues={subWidgetSettingEntity}
                onSubmit={saveEntity}
                render={({handleSubmit, form, submitting, pristine, values, errors}) => {
                    submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
                    if (Object.keys(errors).length) handleSubmit();
                    setFormErrors(errors);
                    setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    }
                    return (
                    <form onSubmit={handleSubmit}>
                        <ReactTooltip effect='solid'
                                    delayUpdate={500}
                                    html={true}
                                    place={'right'}
                                    border={true}
                                    type={'info'} multiline="true"/>
                        <div id="accordion" className=" mb-3">
                        {/* WIDGET LABELS */}
                        <Card className="main-card mt-3">
                            <CardHeader
                            onClick={() => (toggleAccordion(0))}
                            aria-expanded={accordionState[0]}
                            aria-controls="widgetLabelSettingsWrapper"
                            style={{cursor: 'pointer'}}>
                            <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget Labels
                            <span style={{...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : ''}}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                            </CardHeader>
                            <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="widgetLabelSettingsWrapper"
                                    aria-labelledby="WidgetLabel">
                            <CardBody>
                                <Row>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="oneTimeFrequencyText">One Time Purchase Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${onetimepurchaseText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="oneTimePurchaseText"
                                        className="form-control"
                                        type="text"
                                        name="oneTimePurchaseText"
                                        placeholder="One time"
                                    />
                                    </FormGroup>
                                </Col>  <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="oneTimeFrequencyTextV2">One Time Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="oneTimeFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="oneTimeFrequencyTextV2"
                                        placeholder="One time"
                                        parse={value => value}
                                    />
                                    </FormGroup>
                                </Col>  <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="monthFrequencyTextV2">Month Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="monthFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="monthFrequencyTextV2"
                                        placeholder="One time Purchase"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>

                                <Row>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="purchaseOptionsText">Purchase Options Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${purchase_options_text} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="purchaseOptionsText"
                                        className="form-control"
                                        type="text"
                                        name="purchaseOptionsText"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="dayFrequencyTextV2">Day Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="dayFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="dayFrequencyTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="daysFrequencyTextV2">Days Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="daysFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="daysFrequencyTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="subscriptionOptionText">Subscription Option Text <Help
                                        style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                        data-tip={`<div style='max-width:500px'><img src=${subscription_option_text} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="subscriptionOptionText"
                                        className="form-control"
                                        type="text"
                                        name="subscriptionOptionText"
                                        placeholder="eg. Subscription1"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. valid format: {{{discountValue}}},{{{frequency}}},{{{price}}} "}</FormText>
                                    {/* eg. Subscribe and save <span class='appstle_subcsribe_save_discount'>{{{discountValue}}} off</span> */}
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="weekFrequencyTextV2">Week Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="weekFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="weekFrequencyTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="weeksFrequencyTextV2">Weeks Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="weeksFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="weeksFrequencyTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="weeklyLabelTextV2">Weekly Selling plan label Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${sellingplan_type_text} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="weeklyLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="weeklyLabelTextV2"
                                        placeholder=""
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="monthlyLabelTextV2">Monthly Selling plan label Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${sellingplan_type_text} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="monthlyLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="monthlyLabelTextV2"
                                        placeholder=""
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="yearlyLabelTextV2">Yearly Selling plan label Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${sellingplan_type_text} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="yearlyLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="yearlyLabelTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="prepayLabelTextV2">Prepay Selling plan label Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${sellingplan_type_text} width="300px"></img></div>`}/>
                                    </Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="prepayLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="prepayLabelTextV2"
                                        placeholder="Purchase Options Text"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="sellingPlanSelectTitle">Selling Plan Select Title <Help
                                        style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                        data-tip={`<div style='max-width:500px'><img src=${deliveryfreLabel} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}

                                        id="sellingPlanSelectTitle"
                                        className="form-control"
                                        type="text"
                                        name="sellingPlanSelectTitle"
                                        placeholder="eg. sellingPlanSelectTitle"
                                    />
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="yearFrequencyTextV2">Year Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            invalid={meta.error && meta.touched ? true : null}/>
                                    )}
                                        id="yearFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="yearFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={4} lg={4} className="md-6">
                                    <FormGroup>
                                    <Label for="yearsFrequencyTextV2">Years Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            invalid={meta.error && meta.touched ? true : null}/>
                                    )}
                                        id="yearsFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="yearsFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="orderStatusManageSubscriptionTitle">Manage Subscription box title (This box
                                        will be visible to buyer on order summary page)
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${orderSummerySubscriptionTitle} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a Title for Manage subscription box"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        validate={value => {
                                        return !value ? 'Please provide Mange Subscription Box title' : undefined;
                                        }}
                                        id="orderStatusManageSubscriptionTitle"
                                        className="form-control"
                                        type="text"
                                        name="orderStatusManageSubscriptionTitle"
                                    />
                                    </FormGroup>
                                </Col><Col xs={12} sm={12} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="monthsFrequencyTextV2">Months Frequency Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${fequencyText} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            invalid={meta.error && meta.touched ? true : null}/>
                                    )}
                                        id="monthsFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="monthsFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="orderStatusManageSubscriptionDescription">Manage Subscription box description
                                        (This box will be visible to buyer on order summary page)
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${manageSubDetails} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a description for Manage subscription box"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        validate={value => {
                                        return !value ? 'Please provide Mange Subscription Box description' : undefined;
                                        }}
                                        id="orderStatusManageSubscriptionDescription"
                                        className="form-control"
                                        type="text"
                                        name="orderStatusManageSubscriptionDescription"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="orderStatusManageSubscriptionButtonText">Manage Subscription box Button text
                                        (This box will be visible to buyer on order summary page)
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${manageSubButtonText} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a description for Manage subscription box"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        validate={value => {
                                        return !value ? 'Please provide Mange Subscription box Button text' : undefined;
                                        }}
                                        id="orderStatusManageSubscriptionButtonText"
                                        className="form-control"
                                        type="text"
                                        name="orderStatusManageSubscriptionButtonText"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="sellingPlanTitleText">Selling Plan Title Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${selling_plan_title_text} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter a selling plan title text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="sellingPlanTitleText"
                                        className="form-control"
                                        type="text"
                                        name="sellingPlanTitleText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. valid format: {{sellingPlanName}} {{sellingPlanPrice}} {{secondSellingPlanPrice}} {{discountText}}."}</FormText>
                                    </FormGroup>
                                </Col>
                                <Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="oneTimePriceText">One Time Price Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${one_time_price_text} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter a one time price text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="oneTimePriceText"
                                        className="form-control"
                                        type="text"
                                        name="oneTimePriceText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. valid format: {{price}}"}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="selectedPayAsYouGoSellingPlanPriceText">Selected Pay As You Go Selling Plan
                                        Price Text<Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                                        data-tip={`<div style='max-width:500px'><img src=${selected_pay_as_you_go_selling_plan_price_text} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter a selected pay as you go selling plan price text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="selectedPayAsYouGoSellingPlanPriceText"
                                        className="form-control"
                                        type="text"
                                        name="selectedPayAsYouGoSellingPlanPriceText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. valid format: {{price}}"}</FormText>
                                    </FormGroup>
                                </Col>
                                <Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="selectedPrepaidSellingPlanPriceText">Selected Prepaid Selling Plan Price
                                        Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter a selected prepaid selling plan price text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="selectedPrepaidSellingPlanPriceText"
                                        className="form-control"
                                        type="text"
                                        name="selectedPrepaidSellingPlanPriceText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{totalPrice}} {{pricePerDelivery}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="totalPricePerDeliveryText">Total Price Per Delivery Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter a total price per delivery text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="totalPricePerDeliveryText"
                                        className="form-control"
                                        type="text"
                                        name="totalPricePerDeliveryText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{prepaidPerDeliveryPrice}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="offFrequencyTextV2">Off Frequency Text</Label><Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                                        data-tip={`<div style='max-width:500px'><img src=${off_frequency_example} width="300px"></img></div>`}/>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please enter off frequency text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="offFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="offFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="allowFulfilmentCountViaPropertiesV2">Send Fulfillment count via property</Label><Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                                        />
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input type="select" {...input}>
                                            <option value="false">Disable</option>
                                            <option value="true">Enable</option>
                                        </Input>
                                        )}
                                        id="allowFulfilmentCountViaPropertiesV2"
                                        className="form-control"
                                        type="select"
                                        name="allowFulfilmentCountViaPropertiesV2"
                                    />
                                    </FormGroup>
                                </Col>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="onetimeDescriptionTextV2">One Time Description Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            placeholder="Please enter one time description text"
                                            invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="onetimeDescriptionTextV2"
                                        className="form-control"
                                        type="text"
                                        name="onetimeDescriptionTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="cancelAnytimeLabelTextV2">Cancel Anytime Label Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            placeholder=""
                                            invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="cancelAnytimeLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="cancelAnytimeLabelTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                < Col xs={6} sm={6} md={6} lg={6} className="md-6">
                                    <FormGroup>
                                    <Label for="noSubscriptionLabelTextV2">No Subscription Label Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            placeholder=""
                                            invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="noSubscriptionLabelTextV2"
                                        className="form-control"
                                        type="text"
                                        name="noSubscriptionLabelTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="selectedDiscountFormat">Selected Discount Format Text
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${selected_discount_format_text} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a selected discount format text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        id="selectedDiscountFormat"
                                        className="form-control"
                                        type="text"
                                        name="selectedDiscountFormat"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{selectedDiscountPercentage}} {{pricePerDelivery}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="manageSubscriptionBtnFormat">Manage Subscription Button Format
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${accPageManagesubbtnHtml} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a manage subscription button text"
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        )}
                                        validate={value => {
                                        return !value ? 'Please provide manage subscription button text' : undefined;
                                        }}
                                        id="manageSubscriptionBtnFormat"
                                        className="form-control"
                                        type="text"
                                        name="manageSubscriptionBtnFormat"
                                    />
                                    <FormText>Please be careful while changing this text. Invalid format can break the
                                        subscription widget.</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="subscriptionPriceDisplayText">Collection page price Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                placeholder="Please Enter a price discount text on collection page"
                                        />
                                        )}
                                        id="subscriptionPriceDisplayText"
                                        className="form-control"
                                        type="text"
                                        name="subscriptionPriceDisplayText"
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. eg. {{subscriptionPrice}}"}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} md={12} className="md-6">
                                    <FormGroup>
                                        <Label for="loyaltyDetailsLabelText">Loyalty detail title text</Label>
                                        <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                            <Input {...input}
                                            />
                                        )}
                                        id="loyaltyDetailsLabelText"
                                        className="form-control"
                                        type="text"
                                        name="loyaltyDetailsLabelText"
                                        />
                                    </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                <Col xs={12} md={12} className="md-6">
                                    <FormGroup>
                                        <Label for="loyaltyPerkDescriptionTextV2">Loyalty perks description text</Label>
                                        <Field
                                        parse={value => value}
                                        render={({ input, meta }) => (
                                            <Input {...input}/>
                                        )}
                                        id="loyaltyPerkDescriptionTextV2"
                                        className="form-control"
                                        type="textarea"
                                        name="loyaltyPerkDescriptionTextV2"
                                        />
                                        <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{discount}} {{discountType}} {{formatDiscountedPrice}} {{billingCycleBlock}} is available to use as well."}</FormText>
                                    </FormGroup>
                                    </Col>

                                </Row>
                                <hr/>
                                <h5>Tooltip Customization Labels</h5>
                                <hr/>

                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="tooltipTitle">Tooltip Title <Help
                                        style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                        data-tip={`<div style='max-width:500px'><img src=${tooltip_title} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}/>
                                        )}
                                        id="tooltipTitle"
                                        className="form-control"
                                        type="text"
                                        name="tooltipTitle"
                                        placeholder="eg. My tooltip"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <div style={{ alignItems: 'center' }}>
                                        <Label for="tooltipDesctiption">Default Tooltip Description <Help
                                            style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${default_tooltip_description} width="300px"></img></div>`}/>
                                        </Label>
                                        <HTMLTextEditor
                                            defaultValue={values?.tooltipDesctiption}
                                            addHandler={value => form?.change('tooltipDesctiption', value)}
                                        />
                                    </div>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                rows="4"
                                        />
                                        )}
                                        id="tooltipDesctiption"
                                        className="form-control"
                                        type="textarea"
                                        name="tooltipDesctiption"
                                        placeholder="eg. Welcome to .."
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <div style={{ alignItems: 'center' }}>
                                            <Label for="tooltipDescriptionOnPrepaidPlan">Prepaid Plan Description on tooltip
                                                <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                                    data-tip={`<div style='max-width:500px'><img src=${prepaid_plan_description_on_tooltip} width="300px"></img></div>`}/>
                                            </Label>
                                            <HTMLTextEditor
                                                defaultValue={values?.tooltipDescriptionOnPrepaidPlan}
                                                addHandler={value => form?.change('tooltipDescriptionOnPrepaidPlan', value)}
                                            />
                                        </div>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                rows="4"
                                        />
                                        )}
                                        id="tooltipDescriptionOnPrepaidPlan"
                                        className="form-control"
                                        type="textarea"
                                        name="tooltipDescriptionOnPrepaidPlan"
                                        placeholder="eg. Welcome to .."
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{pricePerDelivery}} {{totalPrice}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <div style={{ alignItems: 'center' }}>
                                            <Label for="tooltipDescriptionOnMultipleDiscount">Discount Tier explanation on tooltip
                                                <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                                    data-tip={`<div style='max-width:500px'><img src=${discount_tier_explaination_on_tooltip} width="300px"></img></div>`}/>
                                            </Label>
                                            <HTMLTextEditor
                                                defaultValue={values?.tooltipDescriptionOnMultipleDiscount}
                                                addHandler={value => form?.change('tooltipDescriptionOnMultipleDiscount', value)}
                                            />
                                        </div>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                rows="4"
                                        />
                                        )}
                                        id="tooltipDescriptionOnMultipleDiscount"
                                        className="form-control"
                                        type="textarea"
                                        name="tooltipDescriptionOnMultipleDiscount"
                                        placeholder="eg. Welcome to .."
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{firstPrice}} {{secondPrice}} {{discountOne}} {{discountTwo}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                    <FormGroup>
                                    <Label for="tooltipDescriptionCustomization">Tooltip Description Customization
                                        <Help style={{fontSize: '1rem', marginBottom: '3px', marginLeft: '4px'}}
                                            data-tip={`<div style='max-width:500px'><img src=${discount_tier_explaination_on_tooltip} width="300px"></img></div>`}/></Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                                rows="4"
                                        />
                                        )}
                                        id="tooltipDescriptionCustomization"
                                        className="form-control"
                                        type="textarea"
                                        name="tooltipDescriptionCustomization"
                                        placeholder="eg. Welcome to .."
                                    />
                                    <FormText>{"Please be careful while changing this text. Invalid format can break the subscription widget. {{defaultTooltipDescription}} {{prepaidDetails}} {{discountDetails}} is available to use as well."}</FormText>
                                    </FormGroup>
                                </Col>


                                </Row>
                                <hr/>
                                <h5>Widget Selectors</h5>
                                <hr/>
                                <Row>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="quickViewModalPollingSelector">Quick View Polling Selector</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="quickViewModalPollingSelector"
                                        className="form-control"
                                        type="text"
                                        name="quickViewModalPollingSelector"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="formMappingAttributeName">Form Mapping Attribute Name</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="formMappingAttributeName"
                                        className="form-control"
                                        type="text"
                                        name="formMappingAttributeName"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="formMappingAttributeSelector">Form Mapping Attribute Selector</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="formMappingAttributeSelector"
                                        className="form-control"
                                        type="text"
                                        name="formMappingAttributeSelector"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="quantitySelector">Quantity Selector</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="quantitySelector"
                                        className="form-control"
                                        type="text"
                                        name="quantitySelector"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="widgetParentSelector">Widget Parent Selector</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="widgetParentSelector"
                                        className="form-control"
                                        type="text"
                                        name="widgetParentSelector"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="updatePriceOnQuantityChange">Enable Update Price on Quantity Change</Label>
                                    <Field
                                        parse={value => value}
                                        render={({ input, meta }) => (
                                        <Input type="select" {...input}>
                                            <option value="false">Disable</option>
                                            <option value="true">Enable</option>
                                        </Input>
                                        )}
                                        id="updatePriceOnQuantityChange"
                                        className="form-control"
                                        type="select"
                                        name="updatePriceOnQuantityChange"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="unsubscribeFrequencyTextV2">Unsubscribe Frequency Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="unsubscribeFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="unsubscribeFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="selectDeliverOptionV2">Select Deliver Option Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="selectDeliverOptionV2"
                                        className="form-control"
                                        type="text"
                                        name="selectDeliverOptionV2"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="deliveryEveryFrequencyTextV2">Delivery Every Frequency Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="deliveryEveryFrequencyTextV2"
                                        className="form-control"
                                        type="text"
                                        name="deliveryEveryFrequencyTextV2"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                                <Row>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="subscribeAndSaveInitalV2">Subscribe And Save Initial Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="subscribeAndSaveInitalV2"
                                        className="form-control"
                                        type="text"
                                        name="subscribeAndSaveInitalV2"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="subscribeAndSaveSuccessV2">Subscribe And Save Success Text</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="subscribeAndSaveSuccessV2"
                                        className="form-control"
                                        type="text"
                                        name="subscribeAndSaveSuccessV2"
                                    />
                                    </FormGroup>
                                </Col>
                                <Col xs={12} md={6} className="md-6">
                                    <FormGroup>
                                    <Label for="productPageUnitPriceSelectorV2">Product Page Unit price selector</Label>
                                    <Field
                                        parse={value => value}
                                        render={({input, meta}) => (
                                        <Input {...input}
                                        />
                                        )}
                                        id="productPageUnitPriceSelectorV2"
                                        className="form-control"
                                        type="text"
                                        name="productPageUnitPriceSelectorV2"
                                    />
                                    </FormGroup>
                                </Col>
                                </Row>
                            </CardBody>
                            </Collapse>
                        </Card>
                        <Card className="main-card mt-3">
                            <CardHeader onClick={() => toggleAccordion(6)} aria-expanded={accordionState[6]}
                                aria-controls="widgetLabelSettingsWrapper" style={{ cursor: 'pointer' }} >
                                <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget Customization
                                <span style={{ ...forward_arrow_icon, transform: accordionState[6] ? 'rotate(90deg)' : '' }}>
                                  <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                            </CardHeader>
                            <Collapse
                                isOpen={accordionState[6]}
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

                        {/* WIDGET Appearence */}
                        {/* <Card className="main-card mt-3">
                            <CardHeader
                            onClick={() => (toggleAccordion(1))}
                            aria-expanded={accordionState[1]}
                            aria-controls="widgetLabelSettingsWrapper"
                            style={{cursor: 'pointer'}}>
                            <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget Appearence
                            <span style={{...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : ''}}>
                                  <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                            </CardHeader>
                            <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="widgetLabelSettingsWrapper"
                                    aria-labelledby="WidgetLabel">
                            <CardBody>
                                {Object.keys(data).map((widgetElement, index) => {
                                return (
                                    <div key={data[widgetElement]?.displayName}>
                                    <div style={
                                        {
                                        display: 'flex',
                                        alignItems: 'center',
                                        cursor: 'pointer',
                                        borderBottom: '1px solid rgba(0, 0, 0, 0.1)',
                                        paddingBottom: '1rem',
                                        paddingTop: (index === 0 ? '0' : '1rem'),
                                        }
                                    }
                                        onClick={() => (toggleCssAccordion(index))}
                                        aria-expanded={cssAccordianState[index]}
                                    >
                                        <Brush width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                        <div className="card-title" style={
                                        {
                                            margin: '0',
                                            marginLeft: '1rem'
                                        }
                                        }>{data[widgetElement].displayName}</div>
                                        <span style={{
                                        ...forward_arrow_icon,
                                        transform: cssAccordianState[index] ? 'rotate(90deg)' : ''
                                        }}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                                    </div>
                                    <Collapse isOpen={cssAccordianState[index]} data-parent="#accordion" id={widgetElement}
                                                aria-labelledby={widgetElement}
                                                style={{marginTop: '2rem', paddingBottom: '1rem'}}>
                                        <Row>
                                        {Object.keys(data[widgetElement]).filter((element) => ((typeof data[widgetElement][element]) === 'object')).map((widgetElementProperty) => {
                                            const item = data[widgetElement][widgetElementProperty]
                                            return (
                                            <Col key={item?.id} xs={12} sm={12} md={6} lg={6} className="md-6"
                                                style={{'display': item?.type === 'hidden' ? 'none' : ''}}>
                                                <FormGroup>
                                                <Label for={item?.id}>
                                                    {item?.displayName}
                                                </Label>

                                                {
                                                    item?.mappedFieldId &&
                                                    <WhenFieldChanges
                                                    field={item?.id}
                                                    becomes=""
                                                    set={item?.mappedFieldId}
                                                    to=""
                                                    />
                                                }
                                                <Field
                                                    parse={value => value}
                                                    render={({input, meta}) => (
                                                    <>
                                                        {item?.type === 'input' &&
                                                        <InputGroup>
                                                        <Input {...input} placeholder={item?.placeholder}
                                                                type={item?.validation === 'NUMBER' ? 'number' : 'text'}
                                                                invalid={meta.error && meta.touched ? true : null}/>
                                                        {item?.validation === 'NUMBER' &&
                                                        <InputGroupAddon addonType='append'>px</InputGroupAddon>}
                                                        </InputGroup>
                                                        }

                                                        {item?.type === 'dropdown' &&
                                                        <DropdownList {...input} data={item?.dropdownValues}
                                                                    onChange={(value) => input.onChange(value)}
                                                                    defaultValue={"solid"}
                                                        />
                                                        }

                                                        {item?.type === 'color' &&
                                                        <ColorPicker2 {...input}
                                                                    placeholder={item?.placeholder}
                                                                    onChange={(value) => input.onChange(value)}
                                                        />
                                                        }

                                                        {item?.type === 'hidden' &&
                                                        <Input {...input}
                                                            type="hidden"/>
                                                        }
                                                    </>
                                                    )}
                                                    validate={item?.validation && ((value) => {
                                                    if (item?.validation === 'NUMBER') {
                                                        return validateForNumber(value, item?.displayName);
                                                    }
                                                    })}
                                                    autoComplete="off"
                                                    id={item?.id}
                                                    className="form-control"
                                                    type={item?.type}
                                                    name={item?.id}

                                                />
                                                </FormGroup>
                                            </Col>)
                                        })}
                                        </Row>
                                    </Collapse>
                                    </div>
                                )
                                })}

                            </CardBody>
                            </Collapse>
                        </Card> */}

                            {/* WIDGET Configuration */}
                        <Card className="main-card mt-3">
                            <CardHeader
                            onClick={() => (toggleAccordion(2))}
                            aria-expanded={accordionState[2]}
                            aria-controls="widgetLabelSettingsWrapper"
                            style={{cursor: 'pointer'}}>
                            <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget Configuration
                            <span style={{...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : ''}}>
                                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                                </span>
                            </CardHeader>
                            <Collapse isOpen={accordionState[2]} data-parent="#accordion" id="widgetLabelSettingsWrapper"
                                    aria-labelledby="WidgetLabel">
                            <CardBody>
                                <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                    <h5>Widget Configuration</h5>
                                    <FormText>
                                    <p className="lead text-muted" style={{fontSize: '1rem'}}>
                                        You can easily customize the widget configuration.
                                    </p>
                                    </FormText>
                                </Col>
                                <Col xs={12} sm={12} md={8} lg={8}>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="subscriptionOptionSelectedByDefault"
                                    />
                                    <Label for="subscriptionOptionSelectedByDefault"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Subscription option selected by
                                        default?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="widgetEnabled"
                                    />
                                    <Label for="widgetEnabled" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Widget
                                        enabled?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="widgetEnabledOnSoldVariant"
                                    />
                                    <Label for="widgetEnabledOnSoldVariant" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Widget
                                        enabled on sold out variants?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="showTooltip"
                                    />
                                    <Label for="showTooltip" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show
                                        tooltip?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="showStaticTooltip"
                                    />
                                    <Label for="showStaticTooltip" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show
                                        static
                                        tooltip?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="showAppstleLink"
                                    />
                                    <Label for="showAppstleLink" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show Appstle Link?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="sortByDefaultSequence"
                                    />
                                    <Label for="sortByDefaultSequence" style={{marginBottom: "0rem", marginLeft: "2rem"}}>Sort
                                        by default sequence?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="showSubOptionBeforeOneTime"
                                    />
                                    <Label for="showSubOptionBeforeOneTime"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show Subscription Option Before One
                                        Time?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="showCheckoutSubscriptionBtn"
                                    />
                                    <Label for="showCheckoutSubscriptionBtn"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show Subscription block on Thank
                                        You page?</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="switchRadioButtonWidget"
                                    />
                                    <Label for="switchRadioButtonWidget"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Show Radio buttons for selling plan dropdown.</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="detectVariantFromURLParams"
                                    />
                                    <Label for="detectVariantFromURLParams"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Detect Variant From URL Params.</Label>
                                    </FormGroup>
                                    <FormGroup style={{display: "flex"}}>
                                    <Field
                                        parse={value => value}
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
                                        name="disableQueryParamsUpdate"
                                    />
                                    <Label for="disableQueryParamsUpdate"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Disable Query Params Update.</Label>
                                    </FormGroup>
                                    <FeatureAccessCheck
                                        hasAnyAuthorities={'enableCartWidget'}
                                        upgradeButtonText="Upgrade to Enterprise plan to access Cart widget Settings"
                                    >
                                    <FormGroup style={{display: "flex"}}>
                                        <Field
                                            parse={value => value}
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
                                        name="enableCartWidgetFeature"
                                        />
                                        <Label for="enableCartWidgetFeature"
                                            style={{marginBottom: "0rem", marginLeft: "2rem"}}>Enable Cart Widget Feature. <br/>
                                            <span style={{fontSize: "11px"}}>To configure the Cart widget on you store please <Link
                                                onClick={() => {
                                                    IntercomAPI('showNewMessage', "I would like to configure cart widget in my store. Can you please help?");
                                                }}
                                                >
                                                click here
                                                </Link>{' '} to contact us.<br/>This feature is only available on Enterprise Plan.</span>
                                                </Label>
                                    </FormGroup>

                                    </FeatureAccessCheck>
                                </Col>
                                </Row>
                            </CardBody>
                            </Collapse>
                        </Card>
                        </div>
                        <FormSpy subscription={{values: true}}>
                            {({values}) => {
                            setSubWidgetSettingEntityFormState(values);
                            return <></>;
                            }}
                        </FormSpy>
                    </form>

                    );

                }}
                />

         {/* WIDGET CSS */}
         <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => (toggleAccordion(3))}
                      aria-expanded={accordionState[3]}
                      aria-controls="widgetcss"
                      style={{cursor: 'pointer'}}>
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Widget CSS
                      <span style={{...forward_arrow_icon, transform: accordionState[3] ? 'rotate(90deg)' : ''}}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                        </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[3]} data-parent="#accordion" id="widgetcss"
                              aria-labelledby="WidgetLabel">
                      <CardBody>
                            <SubscriptionCustomCSS
                             customCSSFormSubmitRef = {customCSSFormSubmitRef}/>
                      </CardBody>
                    </Collapse>
                  </Card>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  subWidgetSettingEntity: state.subscriptionWidgetSettings.entity,
  loading: state.subscriptionWidgetSettings.loading,
  updating: state.subscriptionWidgetSettings.updating,
  updateSuccess: state.subscriptionWidgetSettings.updateSuccess,
  shopCustomizationFields: state.shopCustomization.shopCustomizationFields,
  customizationFiledLoading: state.shopCustomization.loading
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity,
  getThemeSettingEntities,
  getThemeSettingEntity,
  updateThemeSettingEntity,
  getEntitiesByCategory,
  completeChecklistItem,
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductPageWidget);
