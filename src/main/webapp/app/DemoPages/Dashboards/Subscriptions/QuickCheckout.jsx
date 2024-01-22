import React, {Fragment, useEffect, useState} from 'react';
import Loader from 'react-loaders';
import {
  Card,
  CardBody,
  Col,
  Collapse,
  FormGroup,
  Input,
  Button,
  InputGroup,
  InputGroupText,
  InputGroupAddon,
  Label,
  Row,
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {ChevronRight} from '@mui/icons-material';
import {
  createEntity,
  getEntity,
  reset,
  updateEntity,
  getUsedProducts
} from 'app/entities/subscription-group/subscription-group.reducer';
import arrayMutators from 'final-form-arrays';
import {FieldArray} from 'react-final-form-arrays';
import _ from "lodash";
import AddProductModal from "./AddProductModal";
import "./datepicker.scss";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCopy, faMoneyBill} from "@fortawesome/free-solid-svg-icons";
import axios from 'axios';
import {toast} from 'react-toastify';
import QuickCheckoutProductItem from "./QuickCheckoutProductItem";
import {CopyToClipboard} from "react-copy-to-clipboard";
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

const QuickCheckout = props => {
  const {subscriptionGroupEntity, loading, updating, usedProductIds} = props;
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [createSubscriptionInProgress, setCreateSubscriptionInProgress] = useState(false);
  const [checkoutFormState, setCheckoutFormState] = useState(null)
  const [showShipping, setShowShipping] = useState(false)
  const [showContactInfo, setShowContactInfo] = useState(false);
  const [showDiscountInfo, setShowDiscountInfo] = useState(false);
  const [showCheckoutInfo, setShowCheckoutInfo] = useState(false);
  const [showCustomAttributes, setShowCustomAttributes] = useState(false);
  const [checkoutFullURL, setCheckoutFullURL] = useState("");
  const [copied, setCopied] = useState(false);


  const handleClose = () => {
    props.history.push('/dashboards/subscriptions');
  };

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  useEffect(() => {

    setCopied(false);

    let cartParams = '';

    if (checkoutFormState?.["lines"]?.length) {
      let products = []
      checkoutFormState?.["lines"].forEach((item, index) => {
        products.push(`items[${index}][id]=${item?.variantId}&items[${index}][quantity]=${item?.quantity}${item?.sellingPlanId ? `&items[${index}][selling_plan]=${item?.sellingPlanId}` : ''}`)
      })
      cartParams = products.join("&");
    }

    let checkoutParamsMap = {};

    if (checkoutFormState?.["checkout"] && checkoutFormState?.["checkout"]?.["email"]) {
      checkoutParamsMap["checkout[email]"] = checkoutFormState["checkout"]["email"]
    }

    let discountCode = "";
    if (checkoutFormState?.["discount"]?.length) {
      discountCode = checkoutFormState["discount"]
    }

    if (checkoutFormState?.["payment"]?.length) {
      checkoutParamsMap["payment"] = checkoutFormState["payment"]
    } else {
      delete checkoutParamsMap["payment"]
    }

    if (checkoutFormState?.["checkout"] && checkoutFormState?.["checkout"]?.["shipping_address"]) {
      Object.keys(checkoutFormState?.["checkout"]["shipping_address"])?.forEach((key) => {
        checkoutParamsMap[`checkout[shipping_address][${key}`] = checkoutFormState?.["checkout"]?.["shipping_address"][key]
      })
    }

    if (checkoutFormState?.["attributes"]?.length) {
      checkoutFormState?.["attributes"]?.forEach((item) => {
        if(item && item.name && item.name.trim().length > 0) {
          checkoutParamsMap[item?.name] = item?.value || ''
        }
      })
    }

    let checkoutParams = Object.entries(checkoutParamsMap).map(([key, value]) => `${key}=${encodeURIComponent(value)}`).join('&');
    let return_to_checkout = `&return_to=${encodeURIComponent(`/checkout?${checkoutParams || ''}${(discountCode && discountCode.length > 0) && `&discount=${encodeURIComponent(discountCode)}`}`)}`
    let fullURL = `https://${props?.shopInfo?.shop}/cart/clear?return_to=${encodeURIComponent(`/cart/add?${cartParams}${return_to_checkout}`)}`;

    setCheckoutFullURL(fullURL);

  }, [checkoutFormState])

  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  const saveEntity = values => {
    setCreateSubscriptionInProgress(true);
    axios.post('/api/subscription-contract-details/create-subscription-contract', values).then(res => {
      setCreateSubscriptionInProgress(false);
      toast.success('Subscription contract created', options);
      props.history.push(`/dashboards/subscription/${res.data?.id?.split("/").pop()}/detail`);
    }).catch(err => {
      setCreateSubscriptionInProgress(false);
      toast.error(err?.response?.data?.message, options);
    })
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

  let [editMode, setEditMode] = useState(false);
  let [viaEditButton, setViaEditButton] = useState(false);
  let submit;

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

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
          heading={"Quick Checkout"}
          icon="pe-7s-network icon-gradient bg-mean-fruit"
          // enablePageTitleAction
          actionTitle="Save"
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            props.history.push(`/dashboards/subscriptions`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={createSubscriptionInProgress}
          sticky={true}
          tutorialButton={{
            show: true,
            docs: [
              {
                title: "Dunning Management in Appstle Subscriptions",
                url: "https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions"
              }
            ],
            videos: [
              {
                title: "Quick Checkout",
                url: "https://www.youtube.com/watch?v=7ctZj-UbwUw",
              }
            ],
          }}
        />
        <FeatureAccessCheck hasAnyAuthorities={'accessQuickCheckout'}
        upgradeButtonText="Upgrade your plan">
        {loading ? (
          <div style={{margin: '10% 0 0 43%'}}
               className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale"/>
          </div>
        ) : (
          <div>
            <CardBody>
              <Form
                mutators={{
                  ...arrayMutators,
                  setProductIds: (args, state, utils) => {
                    utils.changeValue(state, 'productIds', () => JSON.stringify(args[0]))
                  },
                  setVariantIds: (args, state, utils) => {
                    utils.changeValue(state, 'variantIds', () => JSON.stringify(args[0]))
                  },
                  setValue: ([field, value], state, {changeValue}) => {
                    changeValue(state, field, () => value)
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

                  return (
                    <>
                      <form onSubmit={handleSubmit}>
                        {values?.["lines"]?.length ? <>
                            <Card className="mt-4">
                              <CardBody>
                                <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                                  <h5 style={{margin: "0"}}><b>Products</b></h5>
                                </div>
                                <hr/>
                                <FieldArray name="lines">
                                  {({fields}) =>
                                    fields.map((name, idx) => (
                                      <QuickCheckoutProductItem
                                        values={values}
                                        key={idx}
                                        errors={errors}
                                        idx={idx}
                                        name={name}
                                        pop={pop}
                                        update={update}
                                        remove={remove}
                                        fields={fields}
                                        setCheckoutFormState={setCheckoutFormState}
                                      />
                                    ))
                                  }
                                </FieldArray>
                                <AddProductModal
                                  selectedProductVarIds={values?.["lines"]}
                                  value={JSON.stringify([])}
                                  onChange={value => {
                                    console.log(value);
                                  }}
                                  totalTitle="Add Product"
                                  index={1}
                                  buttonLabel="Change Product"
                                  header="Change Product"
                                  addHandler={(data) => {
                                    form.mutators.setValue('lines', data)
                                  }}
                                />
                              </CardBody>
                            </Card>

                            <Card className="mt-4">
                              <CardBody>
                                <div style={{
                                  display: "flex",
                                  justifyContent: "space-between",
                                  alignItems: "center",
                                  cursor: "pointer"
                                }} onClick={() => {
                                  setShowContactInfo(!showContactInfo)
                                }}>
                                  <h5 style={{margin: "0"}}><b>Contact information</b></h5>
                                  {<ChevronRight style={{
                                    transform: !showContactInfo ? "rotate(0deg)" : "rotate(90deg)",
                                    transition: "all 0.2s"
                                  }}/>}
                                </div>
                                <Collapse isOpen={showContactInfo}>
                                  <hr/>
                                  <Row>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.email">Email</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.email"
                                        className="form-control"
                                        type="text"
                                        name="checkout.email"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.phone">Phone</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.phone"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.phone"
                                      />
                                    </Col>
                                  </Row>
                                </Collapse>
                              </CardBody>
                            </Card>
                            <Card className="mt-4">
                              <CardBody>
                                <div style={{
                                  display: "flex",
                                  justifyContent: "space-between",
                                  alignItems: "center",
                                  cursor: "pointer"
                                }} onClick={() => {
                                  setShowShipping(!showShipping)
                                }}>
                                  <h5 style={{margin: "0"}}><b>Shipping Details</b></h5>
                                  {<ChevronRight style={{
                                    transform: !showShipping ? "rotate(0deg)" : "rotate(90deg)",
                                    transition: "all 0.2s"
                                  }}/>}
                                </div>
                                <Collapse isOpen={showShipping}>
                                  <hr/>
                                  <Row>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.first_name">First Name</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.first_name"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.first_name"
                                        validate={value => {
                                          if (!value) {
                                            return 'Please provide a valid value for First Name';
                                          } else {
                                            return undefined;
                                          }
                                        }}
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.last_name">Last Name</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.last_name"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.last_name"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3 d-none">
                                      <Label for="deliveryPhone">Phone No</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="deliveryPhone"
                                        className="form-control"
                                        type="text"
                                        name="deliveryPhone"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.address1">Address 1</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.address1"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.address1"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.address2">Address 2</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.address2"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.address2"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.city">City</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.city"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.city"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.province">State / Province</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.province"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.province"
                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.zip">Zip</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.zip"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.zip"

                                      />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                      <Label for="checkout.shipping_address.country">Country Code</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="checkout.shipping_address.country"
                                        className="form-control"
                                        type="text"
                                        name="checkout.shipping_address.country"
                                      />
                                    </Col>
                                    <Col md={8} className="mb-3 d-none">
                                      <FormGroup>
                                        <Label for={`deliveryPriceAmount`}>Shipping Price</Label>
                                        <Field
                                          render={({input, meta}) => (
                                            <InputGroup
                                              style={{flexGrow: 1}}>
                                              <InputGroupAddon addonType="prepend">
                                                <InputGroupText>
                                                  <FontAwesomeIcon icon={faMoneyBill}/>
                                                </InputGroupText>
                                              </InputGroupAddon>
                                              <>
                                                <Input
                                                  {...input}
                                                  className=""
                                                  type="string"
                                                  invalid={meta.error && meta.touched ? true : null}
                                                />
                                                {meta.error && (
                                                  <div
                                                    class="invalid-feedback"
                                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                                  >
                                                    {meta.error}
                                                  </div>
                                                )}
                                              </>
                                            </InputGroup>
                                          )}
                                          type="string"
                                          id={`deliveryPriceAmount`}
                                          className="mr-2"
                                          name={`deliveryPriceAmount`}
                                          validate={value => {
                                            if (!value) {
                                              return 'Please provide a valid value for Shipping Price';
                                            } else {
                                              return undefined;
                                            }
                                          }}
                                        />
                                      </FormGroup>
                                    </Col>
                                  </Row>
                                </Collapse>
                              </CardBody>
                            </Card>
                            <Card className="mt-4">
                              <CardBody>
                                <div style={{
                                  display: "flex",
                                  justifyContent: "space-between",
                                  alignItems: "center",
                                  cursor: "pointer"
                                }} onClick={() => {
                                  setShowDiscountInfo(!showDiscountInfo)
                                }}>
                                  <h5 style={{margin: "0"}}><b>Discount</b></h5>
                                  {<ChevronRight style={{
                                    transform: !showDiscountInfo ? "rotate(0deg)" : "rotate(90deg)",
                                    transition: "all 0.2s"
                                  }}/>}
                                </div>
                                <Collapse isOpen={showDiscountInfo}>
                                  <hr/>
                                  <Row>
                                    <Col md={6} className="mb-3">
                                    <Label for="discount">Discount Code</Label>
                                      <Field
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input}
                                                   invalid={meta.error && meta.touched ? true : null}/>
                                          </>
                                        )}
                                        id="discount"
                                        className="form-control"
                                        type="text"
                                        name="discount"
                                      />
                                    </Col>
                                  </Row>
                                </Collapse>
                              </CardBody>
                            </Card>
                            <Card className="mt-4">
                              <CardBody>
                                <div style={{
                                  display: "flex",
                                  justifyContent: "space-between",
                                  alignItems: "center",
                                  cursor: "pointer"
                                }} onClick={() => {
                                  setShowCheckoutInfo(!showCheckoutInfo)
                                }}>
                                  <h5 style={{margin: "0"}}><b>Payment</b></h5>
                                  {<ChevronRight style={{
                                    transform: !showCheckoutInfo ? "rotate(0deg)" : "rotate(90deg)",
                                    transition: "all 0.2s"
                                  }}/>}
                                </div>
                                <Collapse isOpen={showCheckoutInfo}>
                                  <hr/>
                                  <Row>
                                    <Col md={6} className="mb-3">
                                      <Field
                                        id="payment"
                                        className="custom-control-input"
                                        type="checkbox"
                                        name="payment"
                                        render={({input, meta}) => (
                                          <>
                                            <Input {...input} type="checkbox" id="payment" label="Shop Pay" value="shop_pay"/>
                                          </>
                                        )}
                                        value="shop_pay"
                                      />
                                      {/* <Label className="custom-control-label" for="checkout.payment">Shop Pay</Label> */}
                                    </Col>
                                  </Row>
                                </Collapse>
                              </CardBody>
                            </Card>
                            <Card className="mt-4">

                              <CardBody>
                                <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                                  <h5 style={{margin: "0"}}><b>Checkout URL</b></h5>
                                  <div>
                                      <CopyToClipboard
                                        text={checkoutFullURL}
                                        onCopy={() => setCopied(true)}
                                      >
                                        <Button color="primary">
                                          {copied ? <><FontAwesomeIcon icon={faCopy}/>&nbsp; Copied </> :
                                            <><FontAwesomeIcon icon={faCopy}/> Copy</>}
                                        </Button>
                                      </CopyToClipboard>
                                  </div>
                                </div>
                                <hr/>
                                <code>
                                  {checkoutFullURL}
                                </code>
                              </CardBody>
                            </Card>
                          </> :
                          (<Row className="align-items-center welcome-page">
                            <Col sm='5'>
                              <div>
                                <h4>Welcome to Quick Checkout Wizard</h4>
                                <p className='text-muted'>
                                  The Quick Checkout Wizard allows you to create direct checkout links for specific
                                  variants with the selected auto-charging selling plans. You can then send these links
                                  to your customers in email campaigns, newsletters or publish them somewhere in your
                                  store.
                                  When the customer clicks on this link, they will get taken directly to the checkout
                                  with the selected subscription products in it and they will be able to pay for the
                                  order.
                                </p>
                                <p className='text-muted'>If you have any questions at any time, just reach out to us
                                  on <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                                <AddProductModal
                                  selectedProductVarIds={values?.["lines"]}
                                  value={JSON.stringify([])}
                                  onChange={value => {
                                    console.log(value);
                                  }}
                                  totalTitle="Add Product"
                                  index={1}
                                  buttonLabel="Add Products"
                                  header="Select Product"
                                  addHandler={(data) => {
                                    form.mutators.setValue('lines', data)
                                  }}
                                />
                                <br/> <br/>
                              </div>
                            </Col>
                          </Row>)}
                      </form>
                    </>
                  );
                }}
              />
            </CardBody>
          </div>
        )}
        </FeatureAccessCheck>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = storeState => ({
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  usedProductIds: storeState.subscriptionGroup.usedProductIds,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess,
  shopInfo: storeState.shopInfo.entity,
});

const mapDispatchToProps = {
  getEntity,
  getUsedProducts,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(QuickCheckout);
