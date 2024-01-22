import React, {useState, useEffect, Fragment, useCallback} from 'react';
import {connect, useSelector} from 'react-redux';
import Loader from 'react-loaders';
import {
  Button,
  Table,
  InputGroup,
  Input,
  Label,
  InputGroupText,
  FormGroup,
  Row,
  Col,
  Card,
  CardBody,
  ListGroup,
  UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle,
  ListGroupItem,
  Pagination,
  PaginationItem,
  PaginationLink
} from 'reactstrap';
import axios from "axios";
import Switch from 'react-switch';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faExternalLinkSquareAlt} from '@fortawesome/free-solid-svg-icons';
import {Field, Form} from 'react-final-form';
import {Link} from "react-router-dom";
import {JhiItemCount, JhiPagination} from "react-jhipster";
import {useHistory, useLocation, useParams} from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  getEntities,
  createEntity,
  reset,
  getEntity,
  updateEntity,
  deleteEntity
} from 'app/entities/product-swap/product-swap.reducer'
import {toast} from 'react-toastify';
import './swapproduct.scss';
import Select, {components} from 'react-select';
import PrdVariantModel from "./PrdVariantModel";
import BlockUi from '@availity/block-ui';
import {getBillingCycleText} from "app/shared/util/customer-utils";

const SwapProduct = ({
                       account,
                       swapProductEntities, swapProductEntity, updateEntity,
                       createEntity, loading, history, updating, ...props
                     }) => {
  const [isOpenFlag, setIsOpenFlag] = useState(false);
  const toggleModal = () => setIsOpenFlag(!isOpenFlag);
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);

  const [isNew, setIsNew] = useState(props.match.params.id ? false : true);
  const [destinationProduct, setDestinationProduct] = useState([]);
  const [sourceProduct, setSourceProduct] = useState([]);
  const toastOption = {
    position: toast.POSITION.BOTTOM_CENTER
  }
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);

  const onSubmit = async (values) => {

    console.log("values" + JSON.stringify(values));

    values.shop = account.login;
    if (values?.checkForEveryRecurringOrder) {
      values = {...values, forBillingCycle: null}
    }
    if (isNew) {
      await createEntity(values);
      history.push(`/dashboards/swap-product`);
    } else {
      values.id = props.match.params.id
      await updateEntity(values);
      history.push(`/dashboards/swap-product`);
    }
  }

  useEffect(() => {
    if (isNew) {
      setDestinationProduct([]);
      setSourceProduct([]);
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  let submit;
  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}>
        <PageTitle
          heading={isNew ? 'Create Product Swap Automation' : 'Edit Product Swap Automation'}
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5213399-how-to-set-up-automatic-product-swap' target='blank'> Click here to know more about product swap automation.</a>"
          icon="pe-7s-repeat icon-gradient bg-mean-fruit"
          enablePageTitleAction
          actionTitle="Save"
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            history.push(`/dashboards/swap-product`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={updating}
          sticky={true}
          tutorialButton={{
            show: true,
            videos: [
              {
                title: "Product Swap Automation - Introduction",
                url: "https://youtu.be/_IN9qQAv5I0",
              },
              {
                title: "Product Swap Automation - Product Cycle",
                url: "https://www.youtube.com/watch?v=wtxfw2icreE",
              },
              {
                title: "Product Swap Automation - Advanced Use",
                url: "https://www.youtube.com/watch?v=81x6rcpmJT8"
              }
            ],
            docs: [
              {
                title: "How to Set Up Automatic Product Swap",
                url: "https://intercom.help/appstle/en/articles/5213399-how-to-set-up-automatic-product-swap"
              }
            ]
          }}
        />
        {loading ? (
          <div style={{margin: '10% 0 0 43%'}}
               className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale"/>
          </div>
        ) : (
          <div>
            <CardBody>
              <Row className="align-items-center" style={{marginLeft: '0px'}}>
                <Col>
                  <div>
                    <h4>Product Swap Automation</h4>
                    <p className='text-muted'>
                      Please note that a product swap rule is honored when all of its source variants are matched in a
                      subscription. For instance, if a swap automation rule contains variant1 and variant2, then both
                      should be available in a subscription. If you would like a variant1 to be matched no matter if
                      another variant2 is present or not, then you should create 2 different automation rule, one for
                      variant1 and another for variant2.
                    </p>
                    <p className='text-muted'> For more details, just reach out to us on <a
                      href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                  </div>
                </Col>
              </Row>
              <Form
                initialValues={swapProductEntity}
                onSubmit={onSubmit}
                render={({
                           handleSubmit,
                           form,
                           submitting,
                           pristine,
                           values,
                           errors,
                           valid
                         }) => {
                  submit = () => {
                    // let desVarError = {};
                    // let srcVarError = {};
                    // if (!JSON.parse(destinationProduct).length > 0) {
                    //   desVarError = {destError: "Please select atleast one destination variant."}
                    // }

                    // if (!JSON.parse(sourceProduct).length > 0) {
                    //   srcVarError = {srcError: "Please select atleast one source variant."}
                    // }

                    // errors ={}

                    // let allErrors = _.extend(errors, desVarError, srcVarError);
                    let allErrors = _.extend(errors);
                    if (Object.keys(allErrors).length === 0 && allErrors.constructor === Object) {
                      handleSubmit();
                    } else {
                      if (Object.keys(allErrors).length) handleSubmit();
                      setFormErrors(allErrors);
                      setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    }
                  }
                  return (
                    <form onSubmit={handleSubmit}>
                      <Card>
                        <CardBody>
                          <Row>
                            <Col md={12}>
                              <FormGroup>
                                <Label for="name">Swap Plan Name</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <>
                                      <Input
                                        {...input}
                                        invalid={meta.error && meta.touched ? true : null}/>
                                      {meta.error && (
                                        <div
                                          style={{
                                            order: '4',
                                            width: '100%',
                                            display: meta.error && meta.touched ? 'block' : 'none'
                                          }}
                                          className="invalid-feedback"
                                        >
                                          {meta.error}
                                        </div>
                                      )}
                                    </>
                                  )}
                                  validate={value => {
                                    return !value ? 'Please provide swap plan name.' : undefined;
                                  }}
                                  id="name"
                                  className="form-control"
                                  type="text"
                                  name="name"
                                  placeholder="Swap Plan Name"
                                />
                              </FormGroup>
                            </Col>

                          </Row>

                          <Row>
                            <Col md={6}>
                              <FormGroup>
                                <Field
                                  name="checkForEveryRecurringOrder"
                                  initialValue={false}
                                  render={({input}) => {
                                    return (
                                      <div style={{display: 'flex', alignItems: 'center'}}>
                                        <Switch
                                          checked={Boolean(input.value)}
                                          onChange={input.onChange}
                                          onColor="#86d3ff"
                                          onHandleColor="#2693e6"
                                          handleDiameter={20}
                                          uncheckedIcon={false}
                                          checkedIcon={false}
                                          boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                          activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                                          height={17}
                                          width={36}
                                          className="mr-2 mb-2"
                                          id="material-switch"
                                        />
                                        <label>
                                          <strong>Apply this automation on all orders?</strong>
                                        </label>

                                      </div>
                                    );
                                  }}
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            <Col md={6}>
                              <FormGroup>
                                <Field
                                  name="carryDiscountForward"
                                  initialValue={customerPortalSettingEntity?.applySellingPlanBasedDiscount}
                                  render={({input}) => {
                                    return (
                                      <div style={{display: 'flex', alignItems: 'center'}}>
                                        <Switch
                                          checked={Boolean(input.value)}
                                          onChange={input.onChange}
                                          onColor="#86d3ff"
                                          onHandleColor="#2693e6"
                                          handleDiameter={20}
                                          uncheckedIcon={false}
                                          checkedIcon={false}
                                          boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                          activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                                          height={17}
                                          width={36}
                                          className="mr-2 mb-2"
                                          id="material-switch"
                                        />
                                        <label title='(For example, you have defined 10% discount on the subscription plan. Do you want that discount to be reflected on the prices of the products that are getting swapped on every recurring basis? If yes, enable the toggle. If no, keep it disabled.)'>
                                          <strong >Keep subscription plan discount?</strong>
                                        </label>

                                      </div>
                                    );
                                  }}
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                          <Row>
                            {!values.checkForEveryRecurringOrder && (<Col md={12}>
                              <FormGroup>
                                <Label for="forBillingCycle">For specific Billing Cycle</Label>
                                <Field
                                  type="select"
                                  id="forBillingCycle"
                                  name="forBillingCycle"
                                  initialValue={"1"}
                                  validate={value => {
                                    if (value) {
                                      if (parseInt(value) < 0) {
                                        return 'Billing Cycle should be greater then 0';
                                      } else {
                                        return undefined;
                                      }
                                    } else {
                                      return 'Billing Cycle is required';
                                    }
                                  }}
                                  render={({input, meta}) => (
                                    <>
                                      <Input
                                        min={1}
                                        {...input}
                                        placeholder="eg. 2"
                                        invalid={meta.error && meta.touched ? true : null}
                                      >
                                        {Array.from({length: 100}, (_, i) => i + 1).map((number) => {
                                          let element = number === 1 ? <option
                                              value={number}>After {getBillingCycleText(number)} billing (When
                                              Subscription is created)</option> :
                                            <option value={number}>After {getBillingCycleText(number)} billing</option>;
                                          return element
                                        })}
                                      </Input>
                                      {meta.error && (
                                        <div
                                          style={{
                                            order: '4',
                                            width: '100%',
                                            display: meta.error && meta.touched ? 'block' : 'none'
                                          }}
                                          className="invalid-feedback"
                                        >
                                          {meta.error}
                                        </div>
                                      )}
                                    </>
                                  )}
                                  className="form-control"
                                />
                              </FormGroup>
                            </Col>)
                            }
                          </Row>

                          <hr/>
                          <Row>
                            <Col md={5}>
                              <FormGroup>
                                <Label for="sourceVariants"><b>Source Variants</b></Label>
                                <Field
                                  name={`sourceVariants`}
                                  validate={value => {
                                    if (!value) {
                                      return 'Please select at least one source product.';
                                    }
                                    if (!JSON.parse(value)?.length > 0) {
                                      return 'Please select at least one source product.';
                                    } else {
                                      return undefined;
                                    }
                                  }}
                                  totalTitle="Select Source Product"
                                  index={1}
                                  methodName="Save"
                                  buttonLabel="Select Source Products"
                                  header="Product"
                                  render={({input, meta}) => (
                                    <PrdVariantModel
                                      value={input.value}
                                      onChange={input.onChange}
                                      isSource={false}
                                      totalTitle="Select Source Product"
                                      index={1}
                                      methodName="Save"
                                      buttonLabel="Select Source Products"
                                      header="Product Variants"
                                      {...input}
                                    />
                                  )}
                                />
                              </FormGroup>
                            </Col>
                            <Col md={2} style={{alignSelf: 'center'}}>
                              <div style={{textAlign: 'center'}} className='rotate-mobile'>
                                <i className="lnr lnr-arrow-right icon-gradient bg-mean-fruit"
                                   style={{fontSize: '48px'}}></i>
                              </div>
                            </Col>
                            <Col md={5}>
                              <FormGroup>
                                <Label for="destinationVariants"><b>Destination Variants</b></Label>
                                {/* <Field
                                  render={({input, meta}) => (
                                    <PrdVariantModel
                                      initialIDs={values?.destinationVariants}
                                      onChange={(e) => setDestinationProduct(e)}
                                      totalTitle="Select Product Variants"
                                      index={1}
                                      methodName="Save"
                                      buttonLabel="Select Destination Variants"
                                      header="Product variants"
                                    />
                                  )}
                                  id="destinationVariants"
                                  className="form-control"
                                  name="destinationVariants"
                                /> */}
                                <Field
                                  name={`destinationVariants`}
                                  validate={value => {
                                    if (!value) {
                                      return 'Please select at least one destination product.';
                                    }
                                    if (!JSON.parse(value)?.length > 0) {
                                      return 'Please select at least one destination product.';
                                    } else {
                                      return undefined;
                                    }
                                  }}
                                  totalTitle="Select Destination Product"
                                  index={1}
                                  methodName="Save"
                                  buttonLabel="Select Destination Product"
                                  header="Product"
                                  render={({input, meta}) => (
                                    <PrdVariantModel
                                      value={input.value}
                                      onChange={input.onChange}
                                      isSource={true}
                                      totalTitle="Select Destination Product"
                                      index={1}
                                      methodName="Save"
                                      buttonLabel="Select Destination Products"
                                      header="Product Variants"
                                      {...input}
                                    />
                                  )}
                                />
                              </FormGroup>

                            </Col>
                          </Row>

                        </CardBody>
                      </Card>
                    </form>)
                }}
              />
            </CardBody>
          </div>
        )
        }
      </ReactCSSTransitionGroup>

    </Fragment>
  )
}

const mapStateToProps = state => ({
  swapProductEntities: state.productSwap.entities,
  swapProductEntity: state.productSwap.entity,
  loading: state.productSwap.loading,
  account: state.authentication.account,
  updating: state.productSwap.updating
});

const mapDispatchToProps = {
  createEntity,
  updateEntity,
  getEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(SwapProduct);
