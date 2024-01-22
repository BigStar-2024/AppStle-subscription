import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { Card, CardBody, CardHeader, Col, Collapse, FormGroup, FormText, Input, Label, Row } from 'reactstrap';
import { Field, Form } from 'react-final-form';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import ColorPicker2 from 'app/DemoPages/Dashboards/Utilities/ColorPicker2';
import { ChevronForward } from 'react-ionicons';
import { getBundleSettingsByShop, updateEntity } from 'app/entities/bundle-setting/bundle-setting.reducer';
import ShopCustomizationField from '../../GeneralSetting/ShopCustomization/ShopCustomizationField';
import { getEntitiesByCategory } from 'app/entities/shop-customization/shop-customization.reducer';

const BundleSettings = props => {
  const { getEntitiesByCategory, shopCustomizationFields} = props;
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([true, false, false, false]);
  const [cssAccordianState, setCssAccordianState] = useState([]);

  const toggleAccordion = tab => {
    const prevState = accordionState;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  };

  useEffect(() => {
    getEntitiesByCategory("BUNDLING")
    props.getBundleSettingsByShop();
  }, []);

  const toggleCssAccordion = tab => {
    const prevState = cssAccordianState;
    const state = prevState.map((x, index) => (tab === index ? !x : x));
    setCssAccordianState(state);
  };

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  const saveEntity = values => {
    if (values.id) {
      props.updateEntity(values);
    }
  };

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
          heading="Bundle Settings"
          subheading=""
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props?.updating}
          updatingText="Updating"
          sticky={true}
        />
        <Form
          initialValues={props.bundleSetting}
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
              <form onSubmit={handleSubmit}>
                <ReactTooltip effect="solid" delayUpdate={500} html={true} place={'right'} border={true} type={'info'} multiline="true" />
                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card">
                    <CardHeader
                      onClick={() => toggleAccordion(0)}
                      aria-expanded={accordionState[0]}
                      aria-controls="BundleWidgetSettings"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Bundle Widget Settings
                      <span style={{ ...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="BundleWidgetSettings" aria-labelledby="BundleWidget">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="selector">Selector</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Enter Selector" invalid={meta.error && meta.touched ? true : null} />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'selector'}
                                className="form-control"
                                name={'selector'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup>
                              <label>
                                <strong>Placement</strong>
                              </label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <div>
                                      <Input {...input} className="mb-2" type="select">
                                        <option value="BEFORE">Before</option>
                                        <option value="AFTER">After</option>
                                        <option value="FIRST_CHILD">First Child</option>
                                        <option value="LAST_CHILD">Last Child</option>
                                      </Input>
                                      {meta.touched && meta.error && <span>{meta.error}</span>}
                                    </div>
                                  );
                                }}
                                name="placement"
                                initialValue="AFTER"
                              />
                            </FormGroup>
                          </Col>

                          {/* <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="actionButtonColor">Action Button Color</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <ColorPicker2
                                        {...input}
                                        placeholder={'Click here to choose a color.'}
                                        onChange={value => input.onChange(value)}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'actionButtonColor'}
                                className="form-control"
                                name={'actionButtonColor'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup>
                              <Label for="actionButtonFontColor">Action Button Font Color</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <>
                                    <ColorPicker2
                                      {...input}
                                      placeholder={'Click here to choose a color.'}
                                      onChange={value => input.onChange(value)}
                                    />
                                  </>
                                )}
                                validate={() => {}}
                                autoComplete="off"
                                id={'actionButtonFontColor'}
                                className="form-control"
                                type={'color'}
                                name={'actionButtonFontColor'}
                              />
                            </FormGroup>
                          </Col> */}
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="variant">Variant</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Enter Variant" invalid={meta.error && meta.touched ? true : null} />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'variant'}
                                className="form-control"
                                name={'variant'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="deliveryFrequency">Delivery Frequency</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Delivery Frequency"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'deliveryFrequency'}
                                className="form-control"
                                name={'deliveryFrequency'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="perDelivery">Per Delivery</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Per Delivery"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'perDelivery'}
                                className="form-control"
                                name={'perDelivery'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup>
                              <label>
                                <strong>Redirect To</strong>
                              </label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <div>
                                      <Input {...input} className="mb-2" type="select">
                                        <option value="CART">CART</option>
                                        <option value="CHECKOUT">CHECKOUT</option>
                                      </Input>
                                      {meta.touched && meta.error && <span>{meta.error}</span>}
                                    </div>
                                  );
                                }}
                                name="redirectTo"
                                initialValue="CART"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        {/* <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="productTitleColor">Product Title Color</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <>
                                    <ColorPicker2
                                      {...input}
                                      placeholder={'Click here to choose a color.'}
                                      onChange={value => input.onChange(value)}
                                    />
                                  </>
                                )}
                                validate={() => {}}
                                autoComplete="off"
                                id={'productTitleColor'}
                                className="form-control"
                                type={'color'}
                                name={'productTitleColor'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="productPriceColor">Product Price Color</Label>
                              <Field
                                render={({ input, meta }) => (
                                  <>
                                    <ColorPicker2
                                      {...input}
                                      placeholder={'Click here to choose a color.'}
                                      onChange={value => input.onChange(value)}
                                    />
                                  </>
                                )}
                                validate={() => {}}
                                autoComplete="off"
                                id={'productPriceColor'}
                                className="form-control"
                                type={'color'}
                                name={'productPriceColor'}
                              />
                            </FormGroup>
                          </Col>
                        </Row> */}

                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2 mt-2">
                              <Field
                                render={({ input, meta }) => (
                                  <Label for="showOnProductPage">
                                    <Input
                                      {...input}
                                      checked={input.checked}
                                      id="showOnProductPage"
                                      onChange={value => input.onChange(value)}
                                    />
                                    Show On Product Page
                                    <FormText>
                                      <span>Uncheck if you don't want to show bundle widget on product pages automatically.</span>
                                    </FormText>
                                  </Label>
                                )}
                                id={'showOnProductPage'}
                                className="form-control"
                                type="checkbox"
                                name={'showOnProductPage'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2">
                              <Field
                                render={({ input, meta }) => (
                                  <Label for="showMultipleOnProductPage">
                                    <Input
                                      {...input}
                                      checked={input.checked}
                                      id="showMultipleOnProductPage"
                                      onChange={value => input.onChange(value)}
                                    />
                                    Show Multiple On Product Page
                                    <FormText>
                                      <span>
                                        Check if you want to display all bundles relevant to the current product on product pages.
                                        Displaying similar bundles might confuse your user.
                                      </span>
                                    </FormText>
                                  </Label>
                                )}
                                id={'showMultipleOnProductPage'}
                                className="form-control"
                                type="checkbox"
                                name={'showMultipleOnProductPage'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        {/* <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2">
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <Label for="oneTimeDiscount">
                                      <Input
                                        {...input}
                                        id="oneTimeDiscount"
                                        checked={input.checked}
                                        onChange={value => input.onChange(value)}
                                      />
                                      One Time Discount
                                    </Label>
                                  );
                                }}
                                id={'oneTimeDiscount'}
                                className="form-control"
                                type="checkbox"
                                name={'oneTimeDiscount'}
                              />
                            </FormGroup>
                          </Col>
                        </Row> */}
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2">
                              <Field
                                render={({ input, meta }) => (
                                  <Label for="showDiscountInCart">
                                    <Input
                                      {...input}
                                      checked={input.checked}
                                      id="showDiscountInCart"
                                      onChange={value => input.onChange(value)}
                                    />
                                    Show Discount In Cart
                                    <FormText>
                                      <span>Enable or disable the discount in Cart.</span>
                                    </FormText>
                                  </Label>
                                )}
                                id={'showDiscountInCart'}
                                className="form-control"
                                type="checkbox"
                                name={'showDiscountInCart'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2">
                              <Field
                                render={({ input, meta }) => (
                                  <Label for="showDiscountPopup">
                                    <Input
                                      {...input}
                                      checked={input.checked}
                                      id="showDiscountPopup"
                                      onChange={value => input.onChange(value)}
                                    />
                                    Show Discount Popup
                                  </Label>
                                )}
                                id={'showDiscountPopup'}
                                className="form-control"
                                type="checkbox"
                                name={'showDiscountPopup'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        {/* <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup check className="mb-2">
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <Label for="showProductPrice">
                                      <Input
                                        {...input}
                                        checked={input.checked}
                                        id="showProductPrice"
                                        onChange={value => input.onChange(value)}
                                      />
                                      Show On Product Price
                                    </Label>
                                  );
                                }}
                                id={'showProductPrice'}
                                className="form-control"
                                type="checkbox"
                                name={'showProductPrice'}
                              />
                            </FormGroup>
                          </Col>
                        </Row> */}
                      </CardBody>
                    </Collapse>
                  </Card>

                  <Card className="main-card mt-3">
                    <CardHeader
                      onClick={() => toggleAccordion(1)}
                      aria-expanded={accordionState[1]}
                      aria-controls="widgetcss"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Bundle Discount Popup Labels
                      <span style={{ ...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="widgetcss" aria-labelledby="WidgetLabel">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="discountPopupHeader">Discount Popup Header</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Discount Popup Header"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'discountPopupHeader'}
                                className="form-control"
                                name={'discountPopupHeader'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="discountPopupAmount">Discount Popup Amount</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Discount Popup Amount"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'discountPopupAmount'}
                                className="form-control"
                                name={'discountPopupAmount'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="discountPopupCheckoutMessage">Discount Popup Checkout Message</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Discount Popup Checkout Message"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'discountPopupCheckoutMessage'}
                                className="form-control"
                                name={'discountPopupCheckoutMessage'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="discountPopupBuy">Discount Popup Buy</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Discount Popup Buy"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'discountPopupBuy'}
                                className="form-control"
                                name={'discountPopupBuy'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="discountPopupNo">Discount Popup No</Label>
                              <Field
                                render={({ input, meta }) => {
                                  return (
                                    <>
                                      <Input
                                        {...input}
                                        placeholder="Enter Discount Popup No"
                                        invalid={meta.error && meta.touched ? true : null}
                                      />
                                    </>
                                  );
                                }}
                                validate={() => {}}
                                autoComplete="off"
                                id={'discountPopupNo'}
                                className="form-control"
                                name={'discountPopupNo'}
                              />
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
                      aria-controls="widgetcss"
                      style={{ cursor: 'pointer' }}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Bundle CSS
                      <span style={{ ...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : '' }}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[2]} data-parent="#accordion" id="widgetcss" aria-labelledby="WidgetLabel">
                      <CardBody>
                        <FormGroup>
                          <Field
                            render={({ input, meta }) => (
                              <Fragment>
                                <Input {...input} rows="20" />
                              </Fragment>
                            )}
                            id="customCss"
                            className="form-control"
                            type="textarea"
                            name="customCss"
                          />
                        </FormGroup>
                      </CardBody>
                    </Collapse>
                  </Card>
                  <Card className="main-card mt-3">
                      <CardHeader onClick={() => toggleAccordion(3)} aria-expanded={accordionState[3]}
                        aria-controls="widgetLabelSettingsWrapper" style={{ cursor: 'pointer' }} >
                        <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Bundle Customization
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
                </div>
              </form>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};
const mapStateToProps = state => ({
  bundleSetting: state.bundleSetting.entity,
  updating: state.bundleSetting.updating,
  shopCustomizationFields: state.shopCustomization.shopCustomizationFields
});

const mapDispatchToProps = {
  getBundleSettingsByShop,
  updateEntity,
  getEntitiesByCategory
};

export default connect(mapStateToProps, mapDispatchToProps)(BundleSettings);
