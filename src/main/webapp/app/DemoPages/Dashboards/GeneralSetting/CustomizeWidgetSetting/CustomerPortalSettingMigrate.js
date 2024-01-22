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
import Tabs, {TabPane} from 'rc-tabs';
import TabContent from 'rc-tabs/lib/SwipeableTabContent';
import ScrollableInkTabBar from 'rc-tabs/lib/ScrollableInkTabBar';
import Switch from 'react-switch';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getEntity,
  getEntities,
  reset,
  updateEntity
} from "app/entities/customer-portal-settings/customer-portal-settings.reducer";
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import "./customer-setting.scss";

const customerPortalSettingMigrate = ({
                                 customerPortalSettingEntity,
                                 getEntities,
                                 getEntity,
                                 updateEntity,
                                 createEntity,
                                 ...props
                               }) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([false, false, false, false]);
  const [isApplySubscriptionDiscount, setIsApplySubscriptionDiscount] = useState(false);

  useEffect(() => {
    // getEntities()
    getEntity(0)
  }, [])

  const saveEntity = values => {
    updateEntity(values);
  };

  useEffect(() => {
    setIsApplySubscriptionDiscount(customerPortalSettingEntity?.applySubscriptionDiscount);
  }, [customerPortalSettingEntity])

  const updateApplySubscriptionDiscount = () => {
    customerPortalSettingEntity.applySubscriptionDiscount = !isApplySubscriptionDiscount;
    setIsApplySubscriptionDiscount(customerPortalSettingEntity.applySubscriptionDiscount);
  }

  const toggleAccordion = (tab) => {
    const state = accordionState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
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
          heading="Customer Portal Settings"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn more about your customer portal settings</a>"
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
        />
        <Form
          initialValues={customerPortalSettingEntity}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
              if (Object.keys(errors).length) handleSubmit();
              setFormErrors(errors);
              setErrorsVisibilityToggle(!errorsVisibilityToggle);
            }
            return (
              <>
                <Row className="align-items-center" style={{marginLeft: '0px'}}>
                  <Col>
                    <div>
                      <h4>Welcome to Customer Portal Settings</h4>
                      <p className='text-muted'>
                        Customer Portal refers to the page your customers will use,
                        to manage their subscriptions. This section will help you to customize your customer portal, and
                        make is easy and intuitive as possible, to provide a seamless experience to your Shoppers. This
                        section will help you customize your Customer Portal, and make it as easy and intuitive as
                        possible.
                      </p>
                      <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                        href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                    </div>
                  </Col>
                </Row>
                <form onSubmit={handleSubmit}>

                    <Card className="main-card">
                        <CardBody>

                          {/* <Tabs tabsWrapperClass="card-header" {...tabConfig} /> */}
                          <Tabs defaultActiveKey="1" className="cp-setting-tabs"
                                renderTabBar={() => <ScrollableInkTabBar/>}
                                renderTabContent={() => <TabContent/>}>
                            <TabPane tab="Add Product" key="1">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="orderFrequencyText">Order Frequency Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide order Frequency Text.' : undefined;
                                      }}
                                      id="orderFrequencyText"
                                      className="form-control"
                                      type="text"
                                      name="orderFrequencyText"
                                      placeholder="Order Frequency Text"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="amountLblV2">Amount Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="choosePurchaseOptionLabelTextV2">Select Purchase Option Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="addProductFinishedMessageTextV2">Add Product Finished Message Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}/>
                                      )}
                                      id="retriveMagicLinkTextV2"
                                      className="form-control"
                                      type="text"
                                      name="retriveMagicLinkTextV2"
                                    />
                                  </FormGroup>

                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="validEmailMessageV2">Please enter valid email Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}/>
                                      )}
                                      id="validEmailMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="validEmailMessageV2"
                                    />
                                  </FormGroup>

                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="contractErrorMessageTextV2">Contract Error Message Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="viewAttributeLabelTextV2">View Attribute Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="swapProductBtnTextV2">Swap Product button text</Label>
                                    <Field
                                      initialValue="swap product"
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                            </TabPane>
                            <TabPane tab="Payment Detail" key="2">

                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="updatePaymentMessageV2">Update Payment Message</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'The request has been sent to your email for change your payment details. If you do not receive mail send another request.' : undefined;
                                      }}
                                      id="updatePaymentMessageV2"
                                      className="form-control"
                                      type="text"
                                      name="updatePaymentMessageV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subTotalLabelTextV2">Subtotal Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                <Col xs={12} sm={12} md={6} lg={6}>
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
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cardExpiryTextV2">Card Expiry Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="creditCardTextV2">Credit Card Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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


                            </TabPane>
                            <TabPane tab="Cancel Subscription" key="3">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancelSubscriptionBtnText">Cancel Subscription Button Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      Prepaid
                                      Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="cancelSubscriptionConfirmPayAsYouGoText">Cancel Subscription Confirm Pay
                                      As
                                      You Go Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="cancelSubscriptionPayAsYouGoButtonText">Cancel Subscription Pay As You
                                      Go
                                      Button Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>

                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="cancelAccordionTitle">Accordion Title for Cancel Subscription</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>

                            </TabPane>
                            <TabPane tab="Upcoming Order" key="4">

                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="viewMoreText">View More Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingOrderChangePopupSuccessTitleText">Upcoming Order Success Popup
                                      Title Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="upcomingOrderChangePopupSuccessDescriptionText">Upcoming Order Success
                                      Popup Description Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="upcomingOrderChangePopupSuccessClosebtnText">Upcoming Order Success
                                      Popup
                                      Close button Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="upcomingOrderChangePopupFailureDescriptionText">Upcoming Order Failure
                                      Popup Description Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="upcomingOrderChangePopupFailureClosebtnText">Upcoming Order Failure
                                      Popup
                                      Close button Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="upcomingFulfillmentText">Upcoming Fulfillment Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row></TabPane>
                            <TabPane tab="Shipping Address" key="5">
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="firstNameLabelText">First Name Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row></TabPane>
                            <TabPane tab="Order Info" key="6">
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="cancelChangeOrderBtnTextV2">Change Order Cancel Button</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide change order cancel button text' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide Search Field Label.' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                <Col xs={12} sm={12} md={6} lg={6}>
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
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="subscriptionNoText">Subscription Number Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide subscription Number Text' : undefined;
                                      }}
                                      id="subscriptionNoText"
                                      className="form-control"
                                      type="text"
                                      name="subscriptionNoText"
                                    />
                                    <FormText>{"cutomized variables eg. {{subscriptionFrequency}}"}</FormText>
                                  </FormGroup>
                                </Col>
                              </Row>

                              <Row>

                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="portalLoginLinkText">Account Link Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="monthText">Month Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="yearText">Year Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="seeMoreDetailsText">See More Details Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pauseSubscriptionText">Pause Subscription Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="goBackButtonText">Go Back Button Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="provinceCodeLabelText">Province Code Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="pleaseWaitLoaderText">Please Wait Loader Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="greetingText">Greeting Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="deleteProductTitleText">Delete Product Title Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="variantLblText">Variant Label Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="failureText">Failure Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                              </Row>
                            </TabPane>
                            <TabPane tab="Advanced" key="7">
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="discountCouponAppliedText">Discount Coupon Applied Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="unableToUpdateSubscriptionStatusMessageText">Unable To Update
                                      Subscription
                                      Status Message Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="cancelSubscriptionMinimumBillingIterationsMessage">Cancel Subscription
                                      Minimum Billing Iterations Message</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide cancel freeze subscription update message' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide subscription contract freeze message' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Your magic link is expired.Please access Subscription Portal by logging into your account using same email that you used to buy subscription.' : undefined;
                                      }}
                                      id="expiredTokenText"
                                      className="form-control"
                                      type="text"
                                      name="expiredTokenText"
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide prevent cancel/pause before Days Message' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide initial discount note description' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please provide after Cycle discount note description' : undefined;
                                      }}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}
                                               invalid={meta.error && meta.touched ? true : null}/>
                                      )}
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
                                    <Label for="addToOrderLabelTextV2">Add to order</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input}/>
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                    <Label for="confirmAddProductV2">Confirm Add Product Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
                                      // validate={value => {
                                      //   return !value ? 'Please enter valid text.' : undefined;
                                      // }}
                                      id="confirmAddProductV2"
                                      className="form-control"
                                      type="text"
                                      name="confirmAddProductV2"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={4} lg={4}>
                                  <FormGroup>
                                    <Label for="discountDetailsTitleTextV2">Discount Details Title Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup>
                                    <Label for="orderNowDescriptionText">Order Now Description Text</Label>
                                    <Field
                                      render={({input, meta}) => (
                                        <Input {...input} />
                                      )}
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
                              </Row>
                            </TabPane>
                          </Tabs>
                        </CardBody>
                    </Card>
                </form>
              </>
            )
              ;
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  loading: state.customerPortalSettings.loading,
  updating: state.customerPortalSettings.updating,
  updateSuccess: state.customerPortalSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(customerPortalSettingMigrate);
