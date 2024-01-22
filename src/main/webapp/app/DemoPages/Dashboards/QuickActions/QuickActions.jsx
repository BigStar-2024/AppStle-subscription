import React, {Fragment, useEffect, useState, useRef} from 'react';
import Loader from 'react-loaders';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
//import './setting.scss';
import {
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Col,
  Collapse,
  FormGroup,
  FormFeedback,
  FormText,
  Input,
  Button,
  InputGroup,
  InputGroupText,
  Label,
  Alert,
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
  reset,
  updateEntity,
  getUsedProducts
} from 'app/entities/subscription-group/subscription-group.reducer';
import MultiselectModal from 'app/DemoPages/Dashboards/TaggingRules/components/MultiselectModal';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import arrayMutators from 'final-form-arrays';
import {FieldArray} from 'react-final-form-arrays';
import {OnChange} from 'react-final-form-listeners';
import _ from "lodash";


import {IntercomAPI} from 'react-intercom';
import {Link} from 'react-router-dom';
import axios from 'axios';
import PrdVariantCheckBoxPopup from "app/DemoPages/Dashboards/AdvancedSetting/PrdVariantCheckBoxPopup";
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';
import FAQSection, { FAQEntry } from 'app/DemoPages/Dashboards/Shared/FAQSection'

const QuickActions = props => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  const {subscriptionGroupEntity, loading, updating, usedProductIds} = props;
  const [formData, setFormData] = useState({...subscriptionGroupEntity});
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  const [formSubmissionInProgress, setFormSubmissionInProgress] = useState(false)
  const handleClose = () => {
    props.history.push('/dashboards/membership-plan');
  };


  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  const saveEntity = values => {
    setFormSubmissionInProgress(true);
    if (values?.["rulesJson"]) {
      values = {...values, rulesJson: JSON.stringify(values?.["rulesJson"])}
    }
    if (values?.["formFieldsJson"]) {
      values = {...values, formFieldsJson: JSON.stringify(values?.["formFieldsJson"])}
    }
    if (isNew) {
      props.createEntity(values);
    } else {
      props.updateEntity(values);
    }
  };


  const checkProducts = (productsString) => {
    let validation = {
      isValid: true,
      invalidProducts: [],
      message: ""
    }
    try {
      let products = JSON.parse(productsString);
      validation.isValid = products.length <= 250;
      validation.message = "Shopify doesn't allow more than 240 products in a subscription plan. Please remove " + (products.length - 250) + " products.";
    } catch (error) {
    }
    return validation;

  }
  let submit;
  let updateExternal;


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
          <>
            <PageTitle
              heading={'Quick Actions'}
              icon="pe-7s-network icon-gradient bg-mean-fruit"
              // enablePageTitleAction
              actionTitle="Save"
              onActionClick={() => {
                submit();
              }}
              enableSecondaryPageTitleAction={false}
              secondaryActionTitle="Cancel"
              onSecondaryActionClick={() => {
                props.history.push(`/dashboards/membership-plan`);
              }}
              formErrors={formErrors}
              errorsVisibilityToggle={errorsVisibilityToggle}
              onActionUpdating={updating}
              sticky={true}
              tutorialButton={{
                show: true,
                docs: [
                  {
                    title: "Quick Action Settings to Control Churn",
                    url: "https://intercom.help/appstle/en/articles/6410143-quick-action-settings-to-control-churn"
                  }
                ]
              }}
            />
            <FeatureAccessCheck
              hasAnyAuthorities={'enableQuickActions'}
              upgradeButtonText="Upgrade to Enterprise plan to enable Quick Actions"
            >
            <div>
              <CardBody>
                <Form
                  mutators={{
                    ...arrayMutators,
                    setProductIds: (args, state, utils) => {
                      utils.changeValue(state, 'productIds', () => JSON.stringify(args[0]))
                    },
                    setVariantIds: (args, state, utils) => {
                      utils.changeValue(state, 'variantIds', () => args[0])
                    }
                  }}
                  initialValues={subscriptionGroupEntity}
                  onSubmit={saveEntity}
                  render={({
                             handleSubmit,
                             form: {
                               mutators: {push, pop, update, remove}
                             },
                             form,
                             submitting,
                             pristine,
                             values,
                             errors,
                             valid
                           }) => {
                    submit = () => {
                      let productValidation = checkProducts(values.productIds)
                      let productError = {};
                      if (!productValidation.isValid) {
                        productError = {productIds: productValidation.message}
                      }
                      let allErrors = _.extend(errors, productError);
                      if (Object.keys(errors).length === 0 && errors.constructor === Object) {
                        handleSubmit();
                      } else {
                        if (Object.keys(errors).length) handleSubmit();
                        setFormErrors(errors);
                        setErrorsVisibilityToggle(!errorsVisibilityToggle);
                      }
                    }

                    updateExternal = update;
                    return (
                      <>
                        <form onSubmit={handleSubmit}>
                          <Row>
                            <Col md={12}>

                              <Card className="mb-3">
                                <CardBody>
                                  <Field
                                    type="select"
                                    id={`quickAction`}
                                    name={`quickAction`}
                                    initialValue={`activate`}
                                    render={({input, meta}) => {
                                      return <FormGroup>
                                        <Label for={`quickAction`}><b>Select Quick Action</b></Label>
                                        <Input
                                          invalid={meta.error && meta.touched ? true : null}
                                          {...input}
                                          style={{flexGrow: 1}}

                                        >
                                          <option value="activate">Activate Subscription</option>
                                          <option value="add_product">Add Product</option>
                                          <option value="apply_discount">Apply Discount</option>
                                          <option value="activate__apply_discount">Activate and Apply Discount</option>
                                          <option value="activate__add_product">Activate and Add Product</option>
                                          <option value="add_product__apply_discount">Add Product and Apply Discount
                                          </option>
                                          <option value="activate__add_product__apply_discount">Activate, Add Product
                                            and Apply Discount
                                          </option>
                                        </Input>
                                      </FormGroup>
                                    }}
                                    className="form-control hide"
                                  />

                                  {((values?.quickAction === "add_product") ||
                                    (values?.quickAction === "activate__add_product") ||
                                    (values?.quickAction === "add_product__apply_discount") ||
                                    (values?.quickAction === "activate__add_product__apply_discount")) && <Field
                                    type="text"
                                    id={`variantIds`}
                                    name={`variantIds`}
                                    render={({input, meta}) => {
                                      return <>
                                        <FormGroup style={{flexGrow: 1}}>
                                          <Label for={`variantIds`}><b>Variant Id</b></Label>
                                          <Input
                                            invalid={meta.error && meta.touched ? true : null}
                                            {...input}
                                          />
                                          <div
                                            style={{
                                              display: meta.error && meta.touched ? 'block' : 'none'
                                            }}
                                            className="invalid-feedback"
                                          >
                                            {meta.error}
                                          </div>
                                        </FormGroup>
                                        <Field
                                          name={`variantId`}
                                          index={1}
                                          methodName="Save"
                                          header="Variant Id"
                                          render={({input, meta}) => {
                                            return <PrdVariantCheckBoxPopup
                                              value={input.value}
                                              onChange={(selectData) => {
                                                input.onChange();
                                                form.mutators.setVariantIds((selectData.map(item => item?.id))?.join(","))
                                              }}
                                              isSource={false}
                                              totalTitle="Select Product Variant"
                                              index={1}
                                              methodName="Save"
                                              buttonLabel="Select Product Variant"
                                              header="Product Variants"
                                            />
                                          }}
                                          className="form-control"
                                          validate={value => {
                                            return !value ? 'Please provide Variant Id' : undefined;
                                          }}
                                        />
                                      </>
                                    }}
                                    className="form-control"
                                    validate={value => {
                                      return !value ? 'Please provide Variant Id' : undefined;
                                    }}
                                  />
                                  }

                                  {((values?.quickAction === "apply_discount") ||
                                    (values?.quickAction === "activate__apply_discount") ||
                                    (values?.quickAction === "add_product__apply_discount") ||
                                    (values?.quickAction === "activate__add_product__apply_discount")) && <Field
                                    type="text"
                                    id={`discountCode`}
                                    name={`discountCode`}
                                    render={({input, meta}) => {
                                      return <FormGroup style={{flexGrow: 1}}>
                                        <Label for={`discountCode`}><b>Discount Code</b></Label>
                                        <Input
                                          invalid={meta.error && meta.touched ? true : null}
                                          {...input}
                                        />
                                        <div
                                          style={{
                                            display: meta.error && meta.touched ? 'block' : 'none'
                                          }}
                                          className="invalid-feedback"
                                        >
                                          {meta.error}
                                        </div>
                                      </FormGroup>
                                    }}
                                    className="form-control"

                                    validate={value => {
                                      return !value ? 'Please provide Discount Code' : undefined;
                                    }}
                                  />}

                                  <Field
                                    type="textarea"
                                    id={`quickActionUrl`}
                                    name={`quickActionUrl`}
                                    render={({input, meta}) => {
                                      return <FormGroup style={{flexGrow: 1}}>
                                        <Label for={`quickActionUrl`}><b>Quick Action Url</b></Label>
                                        <Input
                                          disabled={true}
                                          invalid={meta.error && meta.touched ? true : null}
                                          {...input}
                                          value={`https://${props?.shopInfo.shop}/${props?.shopInfo?.manageSubscriptionsUrl}?token=<TOKEN>${new URLSearchParams(values).toString() ? `&${new URLSearchParams(values).toString()}` : ``}#subscriptions/<SUBSCRIPTION_ID>/detail`}
                                        />
                                        <div
                                          style={{
                                            display: meta.error && meta.touched ? 'block' : 'none'
                                          }}
                                          className="invalid-feedback"
                                        >
                                          {meta.error}
                                        </div>
                                      </FormGroup>
                                    }}
                                    className="form-control"
                                  />

                                </CardBody>
                              </Card>


                            </Col>
                          </Row>

                          <Row>
                            <Col md={12}>
                              <FAQSection title="FAQs" isAllInitialOpen>
                                <FAQEntry question="What are Quick Actions?">
                                  Quick Actions allow your customers to preform a series of predefined actions in one click.
                                  Each quick action link will log the customer in to their subscription using Appstle's Magic Link login token.
                                  Next, a series of actions will take place automatically depending on your selection above.
                                </FAQEntry>
                                <FAQEntry question="How do I send a Quick Action link?">
                                  <p>
                                    It is required that a Quick Action link be sent by email. This is because Appstle
                                    needs to be able to securely provide the most up to date login token for each
                                    customer. Currently it is only possible to send an action link within Appstle's
                                    email templates or our integration with Klaviyo.
                                  </p>
                                  <p>
                                    To send a functional Quick Action link you'll need to replace with the Appstle
                                    login token from either Appstle's email templates or Klaviyo.
                                    <b>
                                      If you are using Appstle's email templates, replace {'<token>'} with {'{{customer.token}}'}
                                    </b>.
                                    If you are using Klaviyo you will need to find the Appstle login token variable from within the
                                    Klaviyo event. Learn how to find variables from within a Klaviyo event
                                    <a
                                      href={'https://help.klaviyo.com/hc/en-us/articles/115002779071-About-Using-Event-Variables-to-Personalize-Flows'}
                                      target={'_blank'}
                                    >
                                      here
                                    </a>.
                                  </p>
                                </FAQEntry>
                              </FAQSection>
                            </Col>
                          </Row>
                        </form>
                      </>
                    );
                  }}
                />
              </CardBody>
            </div>
            </FeatureAccessCheck>
          </>
        </ReactCSSTransitionGroup>
      </Fragment>
  )
    ;
};

const mapStateToProps = storeState => ({
  shopInfo: storeState.shopInfo.entity
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(QuickActions);
