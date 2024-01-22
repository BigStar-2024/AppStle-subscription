import React, { Fragment, useEffect, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';

import { Card, CardBody, CardHeader, Col, Collapse, FormGroup, Input, Label, Row } from 'reactstrap';

import { Field, Form } from 'react-final-form';
import Switch from 'react-switch';
import { ChevronForward } from 'react-ionicons';

import { getEntities, getEntity, updateEntity } from 'app/entities/subscription-bundle-settings/subscription-bundle-settings.reducer';
import HTMLTextEditor from '../../../Forms/Components/WysiwygEditor/HTMLTextEditor';
import ShopCustomizationField from '../ShopCustomization/ShopCustomizationField';
import { getEntitiesByCategory } from 'app/entities/shop-customization/shop-customization.reducer';
import ManageFilterData from '../CustomizeWidgetSetting/ManageFilterData';
import Loader from 'react-loaders';
import { Help } from '@mui/icons-material';
import { Tooltip as ReactTooltip } from 'react-tooltip';

const subscriptionBundleSetting = ({
  subscriptionBundleSettingEntity,
  getEntities,
  getEntity,
  updateEntity,
  createEntity,
  updating,
  loading,
  getEntitiesByCategory,
  shopCustomizationFields,
  customizationFiledLoading,
  ...props
}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [productFilterConfig, setProductFilterConfig] = useState({ enabled: false, filters: [] });

  useEffect(() => {
    // getEntities()
    getEntitiesByCategory('BUILD_A_BOX');
    getEntity(0);
  }, []);

  const saveEntity = values => {
    const formValues = {
      ...values,
      productFilterConfig: JSON.stringify(productFilterConfig)
    };
    updateEntity(formValues).then(response => {
      getEntity(0);
    });
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

  const [accordionState, setAccordionState] = useState([false, false, false, false, false]);
  const identity = value => value;
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
          heading="Build-A-Box Settings"
          // subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn more about your customer portal settings</a>"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={updating || loading}
          sticky={true}
        />
        <Form
          initialValues={subscriptionBundleSettingEntity}
          onSubmit={saveEntity}
          render={({ handleSubmit, form, submitting, pristine, values, errors }) => {
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
                <Row className="align-items-center" style={{ marginLeft: '0px' }}>
                  <Col>
                    <div>
                      <h4>Welcome to Build-A-Box Settings</h4>
                      <p className="text-muted">
                        In this section, you can finalize the 'labels' or the 'text' of the information your customers will see, when they
                        build their customized subscription boxes.
                      </p>
                      <p className="text-muted">
                        For more help you can follow{' '}
                        <a href="https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box-subscription-bundling">
                          this doc
                        </a>{' '}
                        or if you have any questions at any time, just reach out to us on{' '}
                        <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>.
                      </p>
                    </div>
                  </Col>
                </Row>
                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card">
                    <CardHeader
                      onClick={() => toggleAccordion(0)}
                      aria-expanded={accordionState[0]}
                      aria-controls="widgetLabelSettingsWrapper"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Build-A-Box Labels
                      <span style={{ ...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse
                      isOpen={accordionState[0]}
                      data-parent="#accordion"
                      id="widgetLabelSettingsWrapper"
                      aria-labelledby="WidgetLabel"
                    >
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="title">Title</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide selected frequency label text.' : undefined;
                                }}
                                id="title"
                                className="form-control"
                                type="text"
                                name="title"
                                placeholder="Add title"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="selectedFrequencyLabelText">Selected Frequency Label Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide selected frequency label text.' : undefined;
                                }}
                                id="selectedFrequencyLabelText"
                                className="form-control"
                                type="text"
                                name="selectedFrequencyLabelText"
                                placeholder="Selected Frequency Label Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="viewProduct">View Product</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide view product label text.' : undefined;
                                }}
                                id="viewProduct"
                                className="form-control"
                                type="text"
                                name="viewProduct"
                                placeholder="View Product"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="productDetails">Product Details</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide product details label text.' : undefined;
                                }}
                                id="productDetails"
                                className="form-control"
                                type="text"
                                name="productDetails"
                                placeholder="Product details"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="editQuantity">Edit Quantity</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit quantity label text.' : undefined;
                                }}
                                id="editQuantity"
                                className="form-control"
                                type="text"
                                name="editQuantity"
                                placeholder="Edit quantity"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="addButtonText">Add Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide add button text.' : undefined;
                                }}
                                id="addButtonText"
                                className="form-control"
                                type="text"
                                name="addButtonText"
                                placeholder="Add Button Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="cart">Cart Button</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit cart label text.' : undefined;
                                }}
                                id="cart"
                                className="form-control"
                                type="text"
                                name="cart"
                                placeholder="Cart"
                              />
                            </FormGroup>
                          </Col>{' '}
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="shoppingCart">Shopping Cart Label</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit shoppingCart label text.' : undefined;
                                }}
                                id="shoppingCart"
                                className="form-control"
                                type="text"
                                name="shoppingCart"
                                placeholder="Shopping Cart Label"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="tieredDiscount">Tiered Discount</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Tiered Discount label text.' : undefined;
                                }}
                                id="tieredDiscount"
                                className="form-control"
                                type="text"
                                name="tieredDiscount"
                                placeholder="Tiered Discount"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="spendAmountGetDiscount">Spend Amount Get Discount</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Tiered Discount label text.' : undefined;
                                }}
                                id="spendAmountGetDiscount"
                                className="form-control"
                                type="text"
                                name="spendAmountGetDiscount"
                                placeholder="Spend Amount Get Discount"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="buyQuantityGetDiscount">Buy Quantity Get Discount</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Tiered Discount label text.' : undefined;
                                }}
                                id="buyQuantityGetDiscount"
                                className="form-control"
                                type="text"
                                name="buyQuantityGetDiscount"
                                placeholder="Buy Quantity Get Discount"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="subtotal">Subtotal</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Subtotal label text.' : undefined;
                                }}
                                id="subtotal"
                                className="form-control"
                                type="text"
                                name="subtotal"
                                placeholder="Subtotal"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="checkoutMessage">Checkout Message</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Checkout Message label text.' : undefined;
                                }}
                                id="checkoutMessage"
                                className="form-control"
                                type="text"
                                name="checkoutMessage"
                                placeholder="Checkout Message"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="proceedToCheckoutButtonText">Proceed To Checkout Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide proceed to checkout button text.' : undefined;
                                }}
                                id="proceedToCheckoutButtonText"
                                className="form-control"
                                type="text"
                                name="proceedToCheckoutButtonText"
                                placeholder="Proceed To Checkout Button Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="continueShopping">Continue Shopping</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide edit Continue Shopping label text.' : undefined;
                                }}
                                id="continueShopping"
                                className="form-control"
                                type="text"
                                name="continueShopping"
                                placeholder="Continue Shopping"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="selectMinimumProductButtonText">Select Minimum Product Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide select minimum product button text.' : undefined;
                                }}
                                id="selectMinimumProductButtonText"
                                className="form-control"
                                type="text"
                                name="selectMinimumProductButtonText"
                                placeholder="Select Minimum Product Button Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="saveDiscountText"> Save Discount Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} />}
                                id="saveDiscountText"
                                className="form-control"
                                type="text"
                                name="saveDiscountText"
                                placeholder="Enter save discount text"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="productsToProceedText">Products To Proceed Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide products to proceed text.' : undefined;
                                }}
                                id="productsToProceedText"
                                className="form-control"
                                type="text"
                                name="productsToProceedText"
                                placeholder="Products To Proceed Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="removeItem">Remove Item</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide ok button text.' : undefined;
                                }}
                                id="removeItem"
                                className="form-control"
                                type="text"
                                name="removeItem"
                                placeholder="Remove Item"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="myDeliveryText">My Delivery Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide my delivery text.' : undefined;
                                }}
                                id="myDeliveryText"
                                className="form-control"
                                type="text"
                                name="myDeliveryText"
                                placeholder="My Delivery Text"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="failedToAddTitleText">Failed To Add Title Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide failed to add title text.' : undefined;
                                }}
                                id="failedToAddTitleText"
                                className="form-control"
                                type="text"
                                name="failedToAddTitleText"
                                placeholder="Failed To Add Title Text"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="okBtnText">Okay Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide okay button text.' : undefined;
                                }}
                                id="okBtnText"
                                className="form-control"
                                type="text"
                                name="okBtnText"
                                placeholder="Okay Button Text"
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="failedToAddMsgText">Failed To Add Message Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide failed to add message text.' : undefined;
                                }}
                                id="failedToAddMsgText"
                                className="form-control"
                                type="text"
                                name="failedToAddMsgText"
                                placeholder="Failed To Add Message Text"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="variantNotAvailable">Variant Not Available Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                validate={value => {
                                  return !value ? 'Please provide variant not available.' : undefined;
                                }}
                                id="variantNotAvailable"
                                className="form-control"
                                type="text"
                                name="variantNotAvailable"
                                placeholder="variant not available"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="productPriceFormatField">Product Price Format</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} placeholder="eg. {{price}}/month" />}
                                id="productPriceFormatField"
                                className="form-control"
                                type="text"
                                name="productPriceFormatField"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="basePriceFormatFieldV2">Base Price Format</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} placeholder="eg. {{unit_price}}/{{quantity_unit}}" />}
                                id="basePriceFormatFieldV2"
                                className="form-control"
                                type="text"
                                name="basePriceFormatFieldV2"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="typeToSearchPlaceholderTextV2">Type to search</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} placeholder="Please provide type to search." />}
                                id="typeToSearchPlaceholderTextV2"
                                className="form-control"
                                type="text"
                                name="typeToSearchPlaceholderTextV2"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} md={4}>
                            <FormGroup>
                              <Label for="descriptionLength">Product Description Length</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <Input {...input} placeholder="eg. 200" invalid={meta.error && meta.touched ? true : null} />
                                )}
                                validate={value => {
                                  return !value ? 'Please provide product description Length.' : undefined;
                                }}
                                id="descriptionLength"
                                className="form-control"
                                type="text"
                                name="descriptionLength"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} md={4}>
                            <FormGroup>
                              <Label for="currencySwitcherClassName">Currency Switcher Class Name</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} placeholder="eg. money" />}
                                id="currencySwitcherClassName"
                                className="form-control"
                                type="text"
                                name="currencySwitcherClassName"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} md={4}>
                            <FormGroup>
                              <Label for="goBackButtonText">Go Back Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} placeholder="eg. Enter Go Back Button Text" />}
                                id="goBackButtonText"
                                className="form-control"
                                type="text"
                                parse={identity}
                                name="goBackButtonText"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup>
                              <div style={{ alignItems: 'center' }}>
                                <Label for="bundleTopHtml">Build-A-Box Top Html</Label>
                                <HTMLTextEditor
                                  defaultValue={values?.bundleTopHtml}
                                  addHandler={value => form?.change('bundleTopHtml', value)}
                                />
                              </div>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleTopHtml"
                                className="form-control"
                                type="textarea"
                                name="bundleTopHtml"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup>
                              <div style={{ alignItems: 'center' }}>
                                <Label for="bundleBottomHtml">Build-A-Box Bottom HTML</Label>
                                <HTMLTextEditor
                                  defaultValue={values?.bundleBottomHtml}
                                  addHandler={value => form?.change('bundleBottomHtml', value)}
                                />
                              </div>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleBottomHtml"
                                className="form-control"
                                type="textarea"
                                name="bundleBottomHtml"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="minimumToCheckoutV2">Minimum Quantity To Checkout Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="minimumToCheckoutV2"
                                className="form-control"
                                type="text"
                                name="minimumToCheckoutV2"
                                placeholder="Add minimum {{Quantity}} to checkout"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="maxiumQuantityToCheckoutV2">Maximum Quantity To Checkout Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="maxiumQuantityToCheckoutV2"
                                className="form-control"
                                type="text"
                                name="maxiumQuantityToCheckoutV2"
                                placeholder="Add Maxium {{Quantity}} to checkout"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="bundleSummaryTextV2">Bundle Summary Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleSummaryTextV2"
                                className="form-control"
                                type="text"
                                name="bundleSummaryTextV2"
                                placeholder="Add Summary for bundles"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          {/* <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="bundleDescriptionTextV2">Bundle Description Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleDescriptionTextV2"
                                className="form-control"
                                type="text"
                                name="bundleDescriptionTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col> */}
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="bundleCheckoutTextV2">Bundle Checkout Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleCheckoutTextV2"
                                className="form-control"
                                type="text"
                                name="bundleCheckoutTextV2"
                                placeholder="Add Checkout text for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="minimumToCheckoutV2">Minimum to Checkout Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="minimumToCheckoutV2"
                                className="form-control"
                                type="text"
                                name="minimumToCheckoutV2"
                                placeholder="Add minimum To Checkout"
                              />
                            </FormGroup>
                          </Col>
                        </Row>

                        {/* new labels for bundles start  */}

                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="selectPlanTextV2">Select Plan Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="selectPlanTextV2"
                                className="form-control"
                                type="text"
                                name="selectPlanTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="chooseProductsTextV2">Choose Products Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="chooseProductsTextV2"
                                className="form-control"
                                type="text"
                                name="chooseProductsTextV2"
                                placeholder="Add Checkout text for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="reviewBundleTextV2">Bundle Review Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="reviewBundleTextV2"
                                className="form-control"
                                type="text"
                                name="reviewBundleTextV2"
                                placeholder="Add minimum To Checkout"
                              />
                            </FormGroup>
                          </Col>
                        </Row>

                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="nextStepButtonTextV2">Next Step Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="nextStepButtonTextV2"
                                className="form-control"
                                type="text"
                                name="nextStepButtonTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="choosePlanLifeStyleTextV2">Choose Plan Lifestyle Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="choosePlanLifeStyleTextV2"
                                className="form-control"
                                type="text"
                                name="choosePlanLifeStyleTextV2"
                                placeholder="Add Checkout text for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="selectedButtonTextV2">Select Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="selectedButtonTextV2"
                                className="form-control"
                                type="text"
                                name="selectedButtonTextV2"
                                placeholder="Add minimum To Checkout"
                              />
                            </FormGroup>
                          </Col>
                        </Row>

                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="choosePlanLifeStyleDescriptionTextV2">Choose Plan Lifestyle Description Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="choosePlanLifeStyleDescriptionTextV2"
                                className="form-control"
                                type="text"
                                name="choosePlanLifeStyleDescriptionTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="reviewOrderTextV2">Review Order Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="reviewOrderTextV2"
                                className="form-control"
                                type="text"
                                name="reviewOrderTextV2"
                                placeholder="Add Checkout text for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="reviewOrdeDescriptionTextV2">Review Order Description Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="reviewOrdeDescriptionTextV2"
                                className="form-control"
                                type="text"
                                name="reviewOrdeDescriptionTextV2"
                                placeholder="Add minimum To Checkout"
                              />
                            </FormGroup>
                          </Col>
                        </Row>

                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="bundleOnModalTextV2">Bundle Text On Modal</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="bundleOnModalTextV2"
                                className="form-control"
                                type="text"
                                name="bundleOnModalTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="contentsOnModalTextV2">Contents Text On Modal</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="contentsOnModalTextV2"
                                className="form-control"
                                type="text"
                                name="contentsOnModalTextV2"
                                placeholder="Add Checkout text for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="previousStepButtonTextV2">Previous Step Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="previousStepButtonTextV2"
                                className="form-control"
                                type="text"
                                name="previousStepButtonTextV2"
                                placeholder="Add minimum To Checkout"
                              />
                            </FormGroup>
                          </Col>
                        </Row>

                        <Row>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="addToCartButtonTextV2">Add To Cart Button Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="addToCartButtonTextV2"
                                className="form-control"
                                type="text"
                                name="addToCartButtonTextV2"
                                placeholder="Add Description for bundles"
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="readMoreTextV2">Read More Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="readMoreTextV2"
                                className="form-control"
                                type="text"
                                name="readMoreTextV2"
                                placeholder="Read More Text"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="readLessTextV2">Read Less Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="readLessTextV2"
                                className="form-control"
                                type="text"
                                name="readLessTextV2"
                                placeholder="Read Less Text"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="perDeliveryLabelTextV2">Per delivery label text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="perDeliveryLabelTextV2"
                                className="form-control"
                                type="text"
                                name="perDeliveryLabelTextV2"
                                placeholder="Per Delivery Label Text"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="outOfStockTextV2">Out of stock label text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="outOfStockTextV2"
                                className="form-control"
                                type="text"
                                name="outOfStockTextV2"
                                placeholder="Out of stock label text"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="pleaseWaitLabelTextV2">Please wait label text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="pleaseWaitLabelTextV2"
                                className="form-control"
                                type="text"
                                name="pleaseWaitLabelTextV2"
                                placeholder="Please wait label text"
                                parse={identity}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="minimumOrderAmountLabelTextV2">Minimum Order Amount Label Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="minimumOrderAmountLabelTextV2"
                                className="form-control"
                                type="text"
                                name="minimumOrderAmountLabelTextV2"
                                placeholder=""
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="loadMoreTextV2">Load More Label Text</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="loadMoreTextV2"
                                className="form-control"
                                type="text"
                                name="loadMoreTextV2"
                                placeholder=""
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="numberOfProductsPerPageV2">Number of products per page</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                    {Array.from({ length: 100 }, (_, i) => i + 1).map(item => {
                                      return <option value={item}>{item}</option>;
                                    })}
                                  </Input>
                                )}
                                id="numberOfProductsPerPageV2"
                                className="form-control"
                                type="select"
                                name="numberOfProductsPerPageV2"
                                placeholder=""
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="selectedProgressLabelTextV2">Selected Label text (Progress Bar)</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="selectedProgressLabelTextV2"
                                className="form-control"
                                type="text"
                                name="selectedProgressLabelTextV2"
                                placeholder=""
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={4} lg={4}>
                            <FormGroup>
                              <Label for="clearCartV2">Clear Cart</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                <option value="default">Default</option>
                                <option value="enabled">Enabled</option>
                                <option value="disabled">Disabled</option>
                              </Input>}
                                id="clearCartV2"
                                className="form-control"
                                type="select"
                                name="clearCartV2"
                                placeholder=""
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup>
                              <Label for="rightSidebarHTML">Right Sidebar HTML</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="rightSidebarHTML"
                                className="form-control"
                                type="textarea"
                                name="rightSidebarHTML"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup>
                              <Label for="leftSidebarHTML">Left Sidebar HTML</Label>
                              <Field
                                render={({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched ? true : null} />}
                                id="leftSidebarHTML"
                                className="form-control"
                                type="textarea"
                                name="leftSidebarHTML"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => toggleAccordion(1)}
                      aria-expanded={accordionState[1]}
                      aria-controls="widgetLabelSettingsWrapper"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Build-A-Box Permissions
                      <span style={{ ...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse
                      isOpen={accordionState[1]}
                      data-parent="#accordion"
                      id="widgetLabelSettingsWrapper"
                      aria-labelledby="WidgetLabel"
                    >
                      <CardBody>
                        <Row>
                          <Col>
                            <FormGroup
                              style={{
                                display: 'flex',
                                alignItems: 'center'
                              }}
                            >
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableShowProductBasePrice"
                              />
                              <label for="enableShowProductBasePrice" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable to show the unit price of the product.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup
                              style={{
                                display: 'flex',
                                alignItems: 'center'
                              }}
                            >
                              <Field
                                render={({ input, meta }) => (
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
                              <label for="enableRedirectToProductPage" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Allow product details page redirection on product image click.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup
                              style={{
                                display: 'flex',
                                alignItems: 'center'
                              }}
                            >
                              <Field
                                render={({ input, meta }) => (
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
                                name="isBundleWithoutScroll"
                              />
                              <label for="isBundleWithoutScroll" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Disable scrolling
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="disableProductDescription"
                              />
                              <label for="disableProductDescription" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Disable Product Description.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableDisplayProductVendor"
                              />
                              <label for="enableDisplayProductVendor" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable Display Product Vendor
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableDisplayProductType"
                              />
                              <label for="enableDisplayProductType" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable Display Product Type
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup
                              style={{
                                display: 'flex',
                                alignItems: 'center'
                              }}
                            >
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableCustomAdvancedFields"
                              />
                              <label for="enableCustomAdvancedFields" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable Custom Advanced Fields
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup
                              style={{
                                display: 'flex',
                                alignItems: 'center'
                              }}
                            >
                              <Field
                                render={({ input, meta }) => (
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
                                name="hideProductSearchBox"
                              />
                              <label for="hideProductSearchBox" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Hide Product Search Box
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableProductDetailButton"
                              />
                              <label for="enableProductDetailButton" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable the Quick View Product Details button.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableClearCartSelectedProducts"
                              />
                              <label for="enableClearCartSelectedProducts" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable Clear Cart Selected Products.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                            < ReactTooltip
                                    html={true}
                                    id="enableSkieyBABHeader"
                                    effect="solid"
                                    delayUpdate={500}
                                    place="right"
                                    border={true}
                                    type="info"
                                  />
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableSkieyBABHeader"
                              />
                              <label for="enableSkieyBABHeader" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                               Enable Sticky BAB Header
                              </label>
                              <Help style={{fontSize: '1rem', marginLeft: "1rem"}}
                                    data-for="enableSkieyBABHeader"
                                    data-tip={"<div style='max-width:350px'><p>Please Note: If you enable Sticky BAB headers, you will need to add <b>Custom CSS</b> based on your header size and requirements.</p></div>"}/>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                            < ReactTooltip
                                    html={true}
                                    id="enableOpeningSidebar"
                                    effect="solid"
                                    delayUpdate={500}
                                    place="right"
                                    border={true}
                                    type="info"
                                  />
                              <Field
                                render={({ input, meta }) => (
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
                                name="enableOpeningSidebar"
                              />
                              <label for="enableOpeningSidebar" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enable the automatic opening of the sidebar cart only when the maximum product limit is reached.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <FormGroup style={{ display: 'flex', alignItems: 'center' }}>
                              <Field
                                render={({ input, meta }) => (
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
                                name="isMergeIntoSingleBABVariantDropdown"
                              />
                              <label for="isMergeIntoSingleBABVariantDropdown" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                Enabling the feature will display variants in the dropdown in a single BAB.
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => toggleAccordion(2)}
                      aria-expanded={accordionState[2]}
                      aria-controls="widgetLabelSettingsWrapper"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Build a Box Customization
                      <span style={{ ...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
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
                          {shopCustomizationFields.map((field, index) => (
                            <>
                              {field?.type == 'TOGGLE' ? (
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <FormGroup className={'d-flex align-items-center'}>
                                    <Field
                                      render={({ input, meta }) => <ShopCustomizationField field={field} input={input} />}
                                      id={`shopCustomizationData[${index}]`}
                                      className="form-control"
                                      type="text"
                                      name={`shopCustomizationData[${index}]`}
                                    />
                                    <Label for={field?.id} style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                                      {' '}
                                      {field?.label}{' '}
                                    </Label>
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
                              )}
                            </>
                          ))}
                        </Row>
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => toggleAccordion(3)}
                      aria-expanded={accordionState[3]}
                      aria-controls="widgetLabelSettingsWrapper"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Build a Box Product Filter Menu
                      <span style={{ ...forward_arrow_icon, transform: accordionState[3] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse
                      isOpen={accordionState[3]}
                      data-parent="#accordion"
                      id="widgetLabelSettingsWrapper"
                      aria-labelledby="WidgetLabel"
                    >
                      <CardBody>
                        <ManageFilterData
                          values={values}
                          isEnabledAndCondtionToggle={true}
                          settingEntity={subscriptionBundleSettingEntity}
                          productFilterConfig={productFilterConfig}
                          module={'SubscriptionBundleSetting'}
                          setProductFilterConfig={setProductFilterConfig}
                        />
                      </CardBody>
                    </Collapse>
                  </Card>
                </div>
              </>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  subscriptionBundleSettingEntity: state.subscriptionBundleSettings.entity,
  loading: state.subscriptionBundleSettings.loading,
  updating: state.subscriptionBundleSettings.updating,
  updateSuccess: state.subscriptionBundleSettings.updateSuccess,
  shopCustomizationFields: state.shopCustomization.shopCustomizationFields,
  customizationFiledLoading: state.shopCustomization.loading
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity,
  getEntitiesByCategory
};

export default connect(mapStateToProps, mapDispatchToProps)(subscriptionBundleSetting);
