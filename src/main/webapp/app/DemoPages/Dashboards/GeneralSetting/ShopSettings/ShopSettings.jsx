import React, {Fragment, useEffect, useState} from 'react';
import {
  Alert,
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect, useSelector} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {getEntity, updateEntity} from "app/entities/shop-info/shop-info.reducer";
import { Tooltip as ReactTooltip } from "react-tooltip";
import {Help} from "@mui/icons-material";
import Switch from 'react-switch';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import API from "../API/API";
import {CopyToClipboard} from "react-copy-to-clipboard";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCopy} from "@fortawesome/free-solid-svg-icons";
import {getLocations} from "app/entities/shipping-profile/helper-data.reducer";
import Select from "react-select";
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

const ShopSettings = ({shopInfoEntity, getEntity, updateEntity, createEntity, history, storeLocations, getLocations, missingAccessScopes, ...props}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const paymentData = useSelector(state => state.paymentPlan.entity);

  const [storeLocationIds, setStoreLocationIds] = useState([]);

  useEffect(() => {
    getLocations()
  }, []);

  useEffect(() => {
    if (shopInfoEntity?.inventoryLocations && storeLocations && storeLocations?.length > 0) {
      let storedIdList = shopInfoEntity?.inventoryLocations?.split(",");
      let list = storeLocations?.map(item => ({ value: item.id, label: item.name }))?.filter(val => storedIdList?.includes(val?.value));
      setStoreLocationIds(list);
    }
  },[shopInfoEntity, storeLocations])

  const saveEntity = values => {
    let locationIdList = storeLocationIds?.map(location => location.value)?.join(",");

    const entity = {
      ...shopInfoEntity,
      ...values,
      changeNextOrderDateOnBillingAttempt: values["changeNextOrderDateOnBillingAttempt"] === null ? true : values["changeNextOrderDateOnBillingAttempt"],
      zoneOffsetHours: values["zoneOffsetHours"] ? values["zoneOffsetHours"] : null,
      zoneOffsetMinutes: values["zoneOffsetMinutes"] ? values["zoneOffsetMinutes"] : null,
      localOrderHour: values["localOrderHour"] ? values["localOrderHour"] : null,
      localOrderMinute: values["localOrderMinute"] ? values["localOrderMinute"] : null,
      localOrderDayOfWeek: values["localOrderDayOfWeek"] ? values["localOrderDayOfWeek"] : null,
      inventoryLocations: locationIdList
    }
    updateEntity(entity);
  };
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
          heading="Shop Settings"
          // subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn more about your shop settings</a>"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            history.push(`/`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
        />
        <Form
          initialValues={shopInfoEntity}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
              if (Object.keys(errors).length) handleSubmit();
              setFormErrors(errors);
              setErrorsVisibilityToggle(!errorsVisibilityToggle);
            }
            return (
              <form onSubmit={handleSubmit}>
                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card">
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup>
                            <Label for="moneyFormat">Money Format</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                       invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              // validate={value => {
                              //   return !value ? 'Please provide money formate.' : undefined;
                              // }}
                              id="moneyFormat"
                              className="form-control"
                              type="text"
                              name="moneyFormat"
                              placeholder="Money Format"
                            />
                            <FormText>If you update your store money format settings, please
                              update it here as
                              well. Please ensure that you don't use double quotes
                              anywhere in this field. You can
                              replace double quotes with single quote. <br/> Example:
                              {`<span class='class-name'>\${{amount}}</span>`}</FormText>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup>
                            <ReactTooltip
                              html={true}
                              id="currency"
                              effect="solid"
                              delayUpdate={500}
                              place="right"
                              border={true}
                              type="info"
                            />
                            <Label for="currency">Currency Code</Label> <Help
                            style={{fontSize: '1rem'}}
                            data-for="currency"
                            data-tip={"<div style='max-width:300px'>Enter a currency code like this. ( Ex. USD )</div>"}/>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                       invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              validate={value => {
                                return !value ? 'Please provide currency.' : undefined;
                              }}
                              id="currency"
                              className="form-control"
                              type="text"
                              name="currency"
                            />
                            <FormText>If you update your store currency settings, please
                              update it here as
                              well.</FormText>
                          </FormGroup>
                        </Col>
                      </Row>
                      <Row>
                        <Col xs={6}>
                          <FormGroup>
                            <Label for="manageSubscriptionsUrl">Proxy path prefix</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                       invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              validate={value => {
                                return !value ? 'Please provide Manage Subscriptions Url.' : undefined;
                              }}
                              id="manageSubscriptionsUrl"
                              className="form-control"
                              type="text"
                              name="manageSubscriptionsUrl"
                              placeholder="Manage Subscriptions Url"
                            />
                            <FormText>Before you make changes here, make sure to change the
                              url in the app proxy
                              settings</FormText>
                          </FormGroup>
                        </Col>
                        <Col xs={6}>
                          <FormGroup>
                            <Label for="publicDomain">Public Domain</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                       invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              validate={value => {
                                return !value ? 'Please provide Public Domain.' : undefined;
                              }}
                              id="publicDomain"
                              className="form-control"
                              type="text"
                              name="publicDomain"
                              placeholder="Public Domain"
                            />
                            <FormText></FormText>
                          </FormGroup>
                        </Col>
                        <Col xs={12}>
                          <FormGroup>
                            <Label for="customerPortalMode">Customer Portal Mode</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}>
                                  <option value="IFRAME">Old Customer portal -
                                    IFRAME
                                  </option>
                                  <option value="NO_IFRAME">Old Customer portal - Non
                                    IFRAME
                                  </option>
                                  <option value="V3">New Customer Portal</option>
                                  <option value="V3_IFRAME">New Customer Portal - IFRAME</option>
                                </Input>
                              )}
                              id="customerPortalMode"
                              className="form-control"
                              type="select"
                              name="customerPortalMode"
                              placeholder="Customer Portal Mode"
                            />
                            <FormText>Choose your type/version of customer portal</FormText>
                          </FormGroup>
                        </Col>
                        <Col xs={12}>
                          <API/>
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>

                  <Card className="mt-3">
                    <CardBody>
                      <Row>
                        <Col xs={10}>
                          <FormGroup>
                            <Label>Store Front Access Token</Label>
                            <Field
                              render={({input, meta}) => (
                                <InputGroup>
                                  <Input {...input}
                                         onChange={(event) => input.onChange(event.target.value)}
                                         invalid={meta.error && meta.touched ? true : null}/>

                                  <InputGroupAddon>
                                    <CopyToClipboard text={input.value}>
                                      <Button color="primary">
                                        <FontAwesomeIcon icon={faCopy}/>
                                      </Button>
                                    </CopyToClipboard>
                                  </InputGroupAddon>
                                </InputGroup>
                              )}
                              parse={identity}
                              id="storeFrontAccessToken"
                              className="form-control"
                              type="text"
                              name="storeFrontAccessToken"
                              placeholder="Store Front Access Token"
                            />
                          </FormGroup>
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>


                  <Card className="mt-3">
                    <CardHeader>Order and Shipping settings</CardHeader>
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="transferOrderNotesToSubscription"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to sync Order Notes from initial order to Subscription Contract</b>
                            </label>
                          </FormGroup>
                        </Col>

                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="disableShippingPricingAutoCalculation"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Disable Automatic Shipping Calculation from Shopify</b>
                            </label>
                          </FormGroup>
                        </Col>
                      </Row>


                      <Row>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="transferOrderNoteAttributesToSubscription"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to sync Order Note Attributes from initial order to Subscription Contract</b>
                            </label>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="transferOrderLineItemAttributesToSubscription"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable app to sync Line Item Attributes from initial order to Subscription Contract</b>
                            </label>
                          </FormGroup>
                        </Col>


                      </Row>

                      <Row>
                      <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="carryForwardLastOrderNote"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to carry forward last order note</b>
                            </label>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="enableChangeFromNextBillingDate"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to re-calculate next billing date when billing interval is changed</b>
                            </label>
                          </FormGroup>
                        </Col>
                      </Row>
                      <Row>
                      < Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="allowLocalDelivery"
                              initialValue={false}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Allow local delivery</b>
                            </label>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="allowLocalPickup"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Allow local pickup</b>
                            </label>
                          </FormGroup>
                        </Col>
                      </Row>
                      <Row>
                      <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="changeNextOrderDateOnBillingAttempt"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to re-calculate next billing date on manual billing attempt</b>
                            </label>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="enablePauseContractsAfterMaximumOrders"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable Pause Contracts After Completing Maximum Number Of Orders</b>
                            </label>
                          </FormGroup>
                        </Col>
                      </Row>
                      <Row>
                      <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="overwriteAnchorDay"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Enable App to overwrite Anchor Day when next billing date or billing interval is changed</b>
                            </label>
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={6} lg={6}>
                          <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                            <Field
                              name="keepLineAttributes"
                              initialValue={true}
                              render={({input, meta}) => {
                                return (
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
                                );
                              }}
                            />
                            <label style={{marginRight: '5px'}}>
                              <b>Keep existing attributes on product replace</b>
                            </label>
                          </FormGroup>
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>
                  <Card className="mt-3">
                    <CardHeader>Inventory Settings</CardHeader>
                      <CardBody>
                        <Row>
                          <Col xs={12}>
                            <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                              <Field
                                name="enableInventoryCheck"
                                initialValue={false}
                                render={({input, meta}) => {
                                  return (
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
                                  );
                                }}
                              />
                              <label style={{marginRight: '5px'}}>
                                <b>Enable Inventory Check (If enabled, orders will not be placed if the products are out of stock or have been removed.)</b>
                              </label>
                            </FormGroup>
                          </Col>
                          {values?.enableInventoryCheck &&
                            <Col xs={12}>
                              {missingAccessScopes?.includes('read_inventory') ?
                                (
                                <Alert className="mb-0" color="warning">
                                  If you want to use location based inventory check, then plese&nbsp;
                                  <EmbeddedExternalLink href={`https://subscription-admin.appstle.com/oauth/subscription/authenticate?shop=${shopInfoEntity.shop}`}>grant permissions here.</EmbeddedExternalLink>
                                </Alert>
                                )
                                :(<>
                                  <FormGroup>
                                      <label>
                                        Select Inventory Location(s)
                                      </label>
                                      <Select
                                        value={storeLocationIds}
                                        isSelected={false}
                                        onChange={(values) => {
                                          setStoreLocationIds(values)
                                        }}
                                        isMulti
                                        className="basic-multi-select"
                                        classNamePrefix="select"
                                        options={storeLocations.map((item, index) => ({
                                          value: item.id, label: item.name
                                        }))}
                                      />
                                  </FormGroup>
                                  <FormText>
                                    <strong>Note:</strong> If no location(s) are selected, inventory availability will be checked at all locations.
                                  </FormText>
                                </>)
                              }

                            </Col>
                          }
                        </Row>
                    </CardBody>
                  </Card>
                  <Card className="mt-3">
                    <CardHeader>
                      Order Timing Section
                    </CardHeader>
                    <CardBody>
                      {/* <Row>
                        <Col xs={12} md={6}>
                          <FormGroup className="">
                            <Label for="nextOrderDateAttributeKey">Order Attribute Key for Next Order Date</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                    placeholder="Next Order Date"
                                    invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              id="nextOrderDateAttributeKey"
                              className="form-control"
                              type="text"
                              name="nextOrderDateAttributeKey"
                            />
                            <FormText>If you are allowing your customers to choose next order date from store and passing it as an order attribute,
                              &nbsp;then provide that key over here, so we will schedule next order date based on that after subsciption contract is created.</FormText>
                          </FormGroup>
                        </Col>
                        <Col xs={12} md={6}>
                          <FormGroup className="">
                            <Label for="nextOrderDateAttributeFormat">Date-Time format of Order Attribute Value for Next Order Date</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}
                                    placeholder="yyyy-MM-dd'T'HH:mm:ss"
                                    invalid={meta.error && meta.touched ? true : null}/>
                              )}
                              id="nextOrderDateAttributeFormat"
                              className="form-control"
                              type="text"
                              name="nextOrderDateAttributeFormat"

                            />
                            <FormText>If no Date-Time format provided, by default ISO format (<strong>yyyy-MM-dd'T'HH:mm:ss</strong>) will be used.</FormText>
                          </FormGroup>
                        </Col>
                      </Row> */}
                      <Row>
                        {/* <Col xs={12} md={6} lg={3}>
                          <FormGroup className="">
                            <Label for="zoneOffsetHours">Shop's Zone Offset Hours</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}>
                                  <option value='' selected>Select Zone Offset Hours
                                  </option>
                                  {
                                    new Array(19 - (-18)).fill().map((d, i) => i + (-18)).map((hour) => (
                                      <option value={hour}>{hour} hour</option>
                                    ))
                                  }
                                </Input>
                              )}
                              id="zoneOffsetHours"
                              className="form-control"
                              type="select"
                              name="zoneOffsetHours"
                              placeholder="Select Zone Offset Hours"
                              validate={(value, allValues, meta) => {
                                if ((allValues["localOrderMinute"] || allValues["zoneOffsetMinutes"] || allValues["localOrderHour"]) && !allValues["zoneOffsetHours"]) {
                                  return "Zone Offset Hours is Required."
                                } else {
                                  return undefined
                                }
                              }}
                            />
                          </FormGroup>
                        </Col>
                        <Col xs={12} md={6} lg={3}>
                          <FormGroup className="">
                            <Label for="zoneOffsetMinutes">Shop's Zone Offset
                              Minutes</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}>
                                  <option value='' selected>Select Zone Offset
                                    Minute
                                  </option>
                                  {[...Array(60).keys()].map((minute) => (
                                    <option value={minute}>{minute} Minute</option>
                                  ))
                                  }
                                </Input>

                              )}
                              id="zoneOffsetMinutes"
                              className="form-control"
                              type="select"
                              name="zoneOffsetMinutes"
                              placeholder="Select Zone Offset Minute"
                              validate={(value, allValues, meta) => {
                                if ((allValues["localOrderMinute"] || allValues["zoneOffsetHours"] || allValues["localOrderHour"]) && !allValues["zoneOffsetMinutes"]) {
                                  return "Zone Offset Minutes is Required."
                                } else {
                                  return undefined
                                }
                              }}
                            />
                          </FormGroup>
                        </Col> */}
                        <Col xs={12} md={6} lg={3}>
                          <FormGroup className="mb-1">
                            <Label for="localOrderHour">Local Order Hour (<strong>{shopInfoEntity.ianaTimeZone}</strong>)</Label>
                            <Field
                              render={({input, meta}) => (
                                <div>
                                  <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                    <option value='' selected>Select Local Order Hour
                                    </option>
                                    {
                                      [...Array(24).keys()].map((hour) => (
                                        <option value={hour}>{hour} hour</option>
                                      ))
                                    }
                                  </Input>
                                  {meta.error && meta.touched && <span className="text-danger">{meta.error}</span>}
                                </div>
                              )}
                              id="localOrderHour"
                              className="form-control"
                              type="select"
                              name="localOrderHour"
                              placeholder="Select Local Order Hour"
                              validate={(value, allValues, meta) => {
                                if (allValues["localOrderMinute"] && !allValues["localOrderHour"]) {
                                  return "Local Order Hour is required."
                                } else {
                                  return undefined
                                }
                              }}
                            />
                          </FormGroup>
                        </Col>
                        <Col xs={12} md={6} lg={3}>
                          <FormGroup className="mb-1">
                            <Label for="localOrderMinute">Local Order Minute (<strong>{shopInfoEntity.ianaTimeZone}</strong>)</Label>
                            <Field
                              render={({input, meta}) => (
                                <div>
                                  <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                                    <option value='' selected>Select Local Order
                                      Minute
                                    </option>
                                    {[...Array(60).keys()].map((minute) => (
                                      <option value={minute}>{minute} Minute</option>
                                    ))
                                    }
                                  </Input>
                                  {meta.error && meta.touched && <span className="text-danger">{meta.error}</span>}
                                </div>
                              )}
                              id="localOrderMinute"
                              className="form-control"
                              type="select"
                              name="localOrderMinute"
                              placeholder="Select Local Order Minute"
                              validate={(value, allValues, meta) => {
                                if (allValues["localOrderHour"] && !allValues["localOrderMinute"]) {
                                  return "Local Order Minute is required."
                                } else {
                                  return undefined
                                }
                              }}
                            />
                          </FormGroup>
                        </Col>
                        {/* <Col xs={12} md={6} lg={3}>
                          <FormGroup className="">
                            <Label for="localOrderDayOfWeek">Order Day of the Week</Label>
                            <Field
                              render={({input, meta}) => (
                                <Input {...input}>
                                  <option value='' selected>-- Select --</option>
                                  <option value='MONDAY'>Monday</option>
                                  <option value='TUESDAY'>Tuesday</option>
                                  <option value='WEDNESDAY'>Wednesday</option>
                                  <option value='THURSDAY'>Thursday</option>
                                  <option value='FRIDAY'>Friday</option>
                                  <option value='SATURDAY'>Saturday</option>
                                  <option value='SUNDAY'>Sunday</option>
                                </Input>
                              )}
                              id="localOrderDayOfWeek"
                              className="form-control"
                              type="select"
                              name="localOrderDayOfWeek"
                              placeholder="Select Day of Week"
                            />
                          </FormGroup>
                        </Col> */}
                      </Row>
                      <Row>
                        <Col xs={12} md={12}>
                          <FormText>
                            <strong>Note:</strong> If your set <strong>Local Order hour</strong> and <strong>Local Order Minute</strong>
                            then all recurring orders will be placed at that local time based on your shop timezone.
                          </FormText>
                          <FormText>If you need any help setting up your store's order time,
                            please contact us <a
                              href="javascript:window.Intercom('showNewMessage')">here</a>.</FormText>
                        </Col>

                      </Row>
                    </CardBody>
                  </Card>
                  <Card className="mt-3">
                    <CardHeader>Auto Sync Settings</CardHeader>
                    <CardBody>
                      <FeatureAccessCheck
                        hasAnyAuthorities={'enableAutoSync'}
                        upgradeButtonText="Upgrade to Enterprise plan to access Auto Sync Settings"
                      >
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                              <Field
                                name="priceSyncEnabled"
                                initialValue={false}
                                render={({input, meta}) => {
                                  return (
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
                                  );
                                }}
                              />
                              <label style={{marginRight: '5px'}}>
                                <b>Automatically propagate PRICE changes to products in
                                  subscriptions</b>
                                <br/>
                                <FormText><strong>Note: </strong>This will maintain
                                  existing discounts as per pricing policy</FormText>
                              </label>
                            </FormGroup>
                          </Col>
                        </Row>

                        {/* <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                              <Field
                                name="discountSyncEnabled"
                                initialValue={false}
                                render={({input, meta}) => {
                                  return (
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
                                  );
                                }}
                              />
                              <label style={{marginRight: '5px'}}>
                                <b>Automatically propagate Discount changes to product prices in subscriptions</b>
                              </label>
                            </FormGroup>
                          </Col>

                        </Row> */}
                      </FeatureAccessCheck>
                    </CardBody>
                  </Card>
                </div>
              </form>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  shopInfoEntity: state.shopInfo.entity,
  loading: state.shopInfo.loading,
  updating: state.shopInfo.updating,
  updateSuccess: state.shopInfo.updateSuccess,
  storeLocations: state.helperActions.locations,
  missingAccessScopes: state.shopInfo.missingAccessScopes
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  getLocations
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopSettings);
