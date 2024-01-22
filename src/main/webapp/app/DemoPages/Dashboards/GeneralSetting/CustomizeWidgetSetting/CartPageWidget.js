import React, {Fragment, useEffect, useState} from 'react';
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
import {Field, Form} from 'react-final-form';
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
import {data} from './subscription-customisation-trimmed.js'
import {DropdownList} from 'react-widgets';
import ColorPicker2 from '../../Utilities/ColorPicker2.js';
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
import tooltip_title from '../../../../../static/theme/assets/utils/images/widget-setting/tooltip_title.png';
import {IntercomAPI} from 'react-intercom';
import {Link} from 'react-router-dom';

const CartPageWidget = ({subWidgetSettingEntity, getEntities, getEntity, updateEntity, createEntity, ...props}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([false, false, false]);
  const [cssAccordianState, setCssAccordianState] = useState([]);
  const [isPageAccessible, setIsPageAccessible] = useState(true);

   const paymentData = useSelector(state => state.paymentPlan.entity);

  useEffect(() => {
    // getEntities()
    getEntity(0)
    setCssAccordianState([...Object.keys(data).map((el, index) => (index === 0 ? true : false))])
  }, [])

  useEffect(() => {
    console.log(paymentData)
  }, [paymentData])


  const saveEntity = values => {
    updateEntity(values);
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

  let submit;
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
          heading="Cart Widget"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn about customizing your subscription cart widget.</a>"

          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction={isPageAccessible}
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
        />
        <FeatureAccessCheck setIsPageAccessible={setIsPageAccessible} hasAnyAuthorities={'enableCartWidget'} upgradeButtonText="Upgrade to Enterprise plan to access Cart widget Settings">
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
                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card mt-3">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={8} lg={8}>


                            <FormGroup style={{display: "flex"}}>
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
                          </Col>
                        </Row>
                      </CardBody>
                  </Card>
                </div>
              </form>

            );

          }}
        />
        </FeatureAccessCheck>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  subWidgetSettingEntity: state.subscriptionWidgetSettings.entity,
  loading: state.subscriptionWidgetSettings.loading,
  updating: state.subscriptionWidgetSettings.updating,
  updateSuccess: state.subscriptionWidgetSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CartPageWidget);
