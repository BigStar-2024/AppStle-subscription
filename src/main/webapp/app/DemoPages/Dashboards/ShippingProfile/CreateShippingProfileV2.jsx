import React, {Fragment, useEffect, useState} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from "app/Layout/AppMain/PageTitle";
import {connect} from "react-redux";
import {useHistory, useParams} from "react-router-dom";
import {
  Button,
  Card,
  CardBody,
  Col,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  Row,
  Alert,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from "reactstrap";
import {Field, Form} from 'react-final-form';
import arrayMutators from "final-form-arrays";
import _ from "lodash";
import {
  defaultDeliveryMethod,
  defaultDeliveryZone,
  getShippingProfileV3,
  reset,
  saveShippingProfileV3,
  updateShippingProfileV3,
} from "app/entities/shipping-profile/shipping-profile.reducer";
import {
  getEntities as getSubscriptionGroups,
  reset as subscriptionGroupsReset
} from "app/entities/subscription-group/subscription-group.reducer";
import {FieldArray} from "react-final-form-arrays";
import Select from "react-select";
import Loader from "react-loaders";
import {getAvailableCarrier, getLocations} from "app/entities/shipping-profile/helper-data.reducer";
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {getShopifyCountries} from "app/shared/util/shopify_countries";
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';

const CreateShippingProfileV2 = props => {
  const params = useParams();
  const [isNew, setIsNew] = useState(!params || !params.id);
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [countries, setCountries] = useState([]);
  const history = useHistory();

  const {
    updating,
    updateSuccess,
    loading,
    locations,
    carriers,
    subscriptionGroupEntities,
    shippingProfileEntity,
    helperActionsLoading,
    subscriptionGroupLoading,
  } = props;

  useEffect(() => {
    const shopifyCountries = getShopifyCountries();
    if (shopifyCountries.length > 0) {
      setCountries(shopifyCountries);
    }
  }, [])

  useEffect(() => {
    if (isNew) {
      props.reset();
      subscriptionGroupsReset();
    } else {
      props.getShippingProfileV3(params.id);
    }
    props.getLocations();
    props.getAvailableCarrier();
    props.getSubscriptionGroups();
  }, [])

  const saveEntity = values => {
    let entity = {...shippingProfileEntity, ...values}
    if (isNew) {
      props.saveShippingProfileV3(entity)
    } else {
      if (entity?.locationInfos) {
        delete entity.locationInfos;
      }
      props.updateShippingProfileV3(entity)
    }
  };

  useEffect(() => {
    if (updateSuccess) {
      history.push(`/dashboards/shipping-profile`)
    }
  }, [updateSuccess]);

  const getSelectedProvinces = (countryData, selectedItems) => {
    const availableProvinces = populateApplicableProvinces(countryData);
    if (availableProvinces.length > 0) {
      let result = availableProvinces.filter(province => selectedItems.some(item => item.value && item.value.toString().includes(province.value)));
      if (result.length > 0) {
        return result;
      } else {
        return [];
      }
    } else {
      return [];
    }
  }

  const populateApplicableProvinces = (countryData) => {
    if (countryData?.availableProvinces) {
      return countryData?.availableProvinces?.map(ele => ({
        value: ele.code, label: `${ele.code} - ${ele.name}`,
      }));
    } else {
      if (countryData.value) {
        let country = countries.find((item) => countryData?.value.toString().includes(item.code));
        if (country) {
          return country?.provinces?.map(ele => ({
            value: ele.code, label: `${ele.code} - ${ele.name}`,
          }));
        } else {
          return [];
        }
      } else {
        return [];
      }
    }
  }

  const populateSubscriptionGroups = (selectedItems) => {
    if (subscriptionGroupEntities.length > 0 && selectedItems.length > 0) {
      let result = subscriptionGroupEntities.filter(group => selectedItems.some(item => {
        if (item.value === group.id || item.value.toString().includes(group.id)) {
          return item
        }
      }));
      if (result.length > 0) {
        return result.map((item, index) => ({
          value: item.id, label: item.groupName
        }))
      } else {
        return [];
      }
    }
  }

  useEffect(() => {
    shippingProfileEntity && shippingProfileEntity?.zones && shippingProfileEntity?.zones.map((zone) => {
      zone?.countries && zone?.countries.map((countryData) => {
        const availableProvinces = populateApplicableProvinces(countryData);
        if (countryData?.provinces.length > 0 && availableProvinces.length > 0) {
          countryData["includeAllProvinces"] = countryData?.provinces.length === availableProvinces.length;
        }
      })
    })
  }, [shippingProfileEntity])

  useEffect(() => {
    let error = {
      name: formErrors?.name, sellingPlanGroups: formErrors?.sellingPlanGroups, locations: formErrors?.locations
    }
    formErrors?.zones && formErrors?.zones.length > 0 && formErrors?.zones?.map((zone) => {
      zone?.deliveryMethods !== undefined && zone?.deliveryMethods?.map((methods) => {
        error = {...error, deliveryMethodsName: methods?.name, carrierServiceId: methods?.carrierServiceId}
      })
      error = {...error, countries: zone?.countries}
    })
    if (Object.keys(error).length > 0) {
      Object.keys(error).map((errKeys) => {
        if (error[errKeys] !== undefined) {
          toast.error(`${error[errKeys]}`, {
            position: "top-right",
            autoClose: 500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
          })
        }
      })
    }
  }, [formErrors])


  const identity = value => value;
  let submit;

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  return (<Fragment>
    <ReactCSSTransitionGroup
      component="div"
      transitionName="TabsAnimation"
      transitionAppear
      transitionAppearTimeout={0}
      transitionEnter={false}
      transitionLeave={false}
    >
      <PageTitle
        subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders' target='blank'> Click here, to learn more about creating and managing shipping profiles.</a>"
        heading={isNew ? 'Create Shipping Profile' : 'Edit Shipping Profile'}
        icon="lnr-gift icon-gradient bg-mean-fruit"
        enablePageTitleAction
        actionTitle="Save"
        onActionClick={() => {
          submit();
        }}
        enableSecondaryPageTitleAction={true}
        secondaryActionTitle="Cancel"
        onSecondaryActionClick={() => {
          props.history.push(`/dashboards/shipping-profile`);
        }}
        formErrors={formErrors}
        errorsVisibilityToggle={errorsVisibilityToggle}
        onActionUpdating={updating}
        sticky={true}
        tutorialButton={{
          show: true,
          videos: [
            {
              title: "How to Create a Shipping Profile",
              url: "https://www.youtube.com/watch?v=AcknZAvk0s8",
            },
            {
              title: "How to Define Delivery Conditions",
              url: "https://www.youtube.com/watch?v=IvIEyR74bvM",
            },
          ],
          docs: [
            {
              title: "How to Create Custom Shipping Profiles for Subscription Orders",
              url: "https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders"
            }
          ]
        }}
      />

      {(loading || helperActionsLoading || subscriptionGroupLoading) ? (<div style={{margin: '10% 0 0 43%'}}
                                                                             className="loader-wrapper d-flex justify-content-center align-items-center">
        <Loader type="line-scale"/>
      </div>) : (<div>
        <CardBody>
          <Row className={"justify-content-center"}>
            <Col md={8}>
              {/* <Alert className="mb-2" color="warning">
                <h6>Need assistance?</h6>
                <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
                <span>
                  <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
                  <a
                    href='https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders'
                    target='_blank'
                    rel="noopener noreferrer"
                    style={{marginLeft: '10px'}}
                  >
                    Help Documentation
                  </a>
              </span>
              </Alert>
              <Modal isOpen={showModal} toggle={toggleShowModal}>
                <ModalHeader toggle={toggleShowModal}>Tutorial Videos</ModalHeader>
                <ModalBody>
                  <div style={{ height: '500px', overflowY: 'scroll' }}>
                    <div className="mt-4 border-bottom pb-4">
                      <h6>How to Create a Shipping Profile</h6>
                      <YoutubeVideoPlayer
                        url="https://www.youtube.com/watch?v=AcknZAvk0s8"
                        iframeHeight="100%"
                        divClassName="video-container"
                        iframeClassName="responsive-iframe"
                      />
                    </div>
                    <div className="py-4 border-bottom">
                    <h6>How to Define Delivery Conditions</h6>
                      <YoutubeVideoPlayer
                        url="https://www.youtube.com/watch?v=IvIEyR74bvM"
                        iframeHeight="100%"
                        divClassName="video-container"
                        iframeClassName="responsive-iframe"
                      />
                    </div>
                  </div>
                </ModalBody>
                <ModalFooter>
                  <Button color="link" onClick={toggleShowModal}>Cancel</Button>
                </ModalFooter>
              </Modal> */}
              <Form
                mutators={{...arrayMutators}}
                initialValues={shippingProfileEntity}
                onSubmit={saveEntity}
                render={({
                           handleSubmit,
                           form,
                           submitting,
                           pristine,
                           values,
                           errors,
                           valid,
                           form: {mutators: {push, pop, update, remove}}
                         }) => {
                  submit = () => {
                    // let productValidation = checkProducts(values.productIds)
                    // let productError = {};
                    // if (!productValidation.isValid) {
                    //   productError = {productIds: productValidation.message}
                    // }
                    let allErrors = _.extend(errors);
                    if (Object.keys(allErrors).length === 0 && allErrors.constructor === Object) {
                      handleSubmit();
                    } else {
                      if (Object.keys(allErrors).length) handleSubmit();
                      setFormErrors(allErrors);
                      setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    }
                  };
                  return (<form onSubmit={handleSubmit}>
                    <Row>
                      <h5>Shipping Information</h5>
                    </Row>

                    {/*Shipping Profile Name*/}
                    <Row>
                      <Card className="mt-3 w-100">
                        <CardBody>
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup>
                                <Label for="name">Shipping Profile Name</Label>
                                <Field
                                  render={({input, meta}) => (<>
                                    <Input {...input} placeholder="Shipping Profile Name"
                                           invalid={meta.error && meta.touched ? true : null}/>
                                    {meta.error && (<div style={{
                                      order: '4', width: '100%', display: meta.error && meta.touched ? 'block' : 'none'
                                    }} className="invalid-feedback">{meta.error}</div>)}
                                    <FormText>
                                      <span>A descriptive name will help you identify this shipping profile option.</span>
                                    </FormText>
                                  </>)}
                                  validate={(value) => {
                                    return !value ? 'Please provide Shipping profile name.' : undefined;
                                  }}
                                  id="name"
                                  className="form-control"
                                  type="text"
                                  name="name"
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                        </CardBody>
                      </Card>
                    </Row>

                    {/*Subscriptions Groups*/}
                    <Row>
                      <Card className="mt-3 w-100">
                        <CardBody>
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup>
                                <Label for="sellingPlanGroups">Subscription Plans</Label>
                                <Field
                                  render={({input, meta}) => (<>
                                    <Select
                                      value={populateSubscriptionGroups(input.value)}
                                      isSelected={false}
                                      onChange={input.onChange}
                                      isMulti
                                      className="basic-multi-select"
                                      classNamePrefix="select"
                                      options={subscriptionGroupEntities.map((item, index) => ({
                                        value: item.id, label: item.groupName
                                      }))}
                                    />
                                    {meta.error && (<div style={{
                                      order: '4', width: '100%', display: meta.error && meta.touched ? 'block' : 'none'
                                    }} className="invalid-feedback">{meta.error}</div>)}
                                    <FormText>
                                        <span>
                                            Each subscription group can be connected to only one delivery profile. If the subscription groups you selected are already in another delivery profile, then they will get removed from the other delivery profile.
                                        </span>
                                    </FormText>
                                  </>)}
                                  validate={(value) => {
                                    return (value && value !== undefined && (Object.keys(value).length > 0 || value.length > 0)) ? undefined : 'Please Select Subscription Groups!';
                                  }}
                                  id="sellingPlanGroups"
                                  className="form-control"
                                  type="select"
                                  name="sellingPlanGroups"
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                        </CardBody>
                      </Card>
                    </Row>

                    {/*Store Locations*/}
                    <Row>
                      <Card className="mt-3 w-100">
                        <CardBody>
                          <Row>
                            <Col xs={12} sm={12} md={12} lg={12}>
                              <FormGroup>
                                <Label for="locations">Store Locations</Label>
                                <Field
                                  render={({input, meta}) => (<>
                                    <Select
                                      value={input.value}
                                      isSelected={false}
                                      onChange={input.onChange}
                                      isMulti
                                      className="basic-multi-select"
                                      classNamePrefix="select"
                                      options={locations.map((item, index) => ({
                                        value: item.id, label: item.name
                                      }))}
                                    />
                                    {meta.error && (<div style={{
                                      order: '4', width: '100%', display: meta.error && meta.touched ? 'block' : 'none'
                                    }} className="invalid-feedback">{meta.error}</div>)}
                                  </>)}
                                  validate={(value) => {
                                    return (value && value !== undefined && (Object.keys(value).length > 0 || value.length > 0)) ? undefined : 'Please Select Store Locations!';
                                  }}
                                  id="locations"
                                  className="form-control"
                                  type="select"
                                  name="locations"
                                />
                              </FormGroup>
                            </Col>
                          </Row>
                        </CardBody>
                      </Card>
                    </Row>

                    {/*Delivery Zones*/}
                    <Row className={"mt-3"}>
                      <Card className=" w-100">
                        <CardBody>
                          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                            <h6 className="text-capitalize">Delivery Zone</h6>
                            <Button
                              type="button"
                              className="d-flex align-items-center appstle_order-detail_update-button "
                              onClick={() => push('zones', defaultDeliveryZone)}
                            >
                              Add Delivery Zone
                            </Button>
                          </div>
                          <FieldArray name="zones">
                            {({fields}) => fields.map((zoneName, zoneIndex) => {
                              return (<Row key={zoneName}>
                                <Col xs={12} sm={12} md={12} lg={12}>
                                  <Card className="mt-3">
                                    <CardBody>
                                      <Row>
                                        <Col xs={12} sm={12} md={12} lg={12}>
                                          <div className="d-flex justify-content-between">
                                            <p>Delivery Zone #{zoneIndex + 1}</p>
                                            <Button
                                              color="danger"
                                              style={{padding: '10px'}}
                                              className="d-flex align-items-center"
                                              onClick={() => fields.remove(zoneIndex)}
                                            >
                                              {' '}
                                              <i className="lnr lnr-trash btn-icon-wrapper"/>
                                            </Button>
                                          </div>
                                        </Col>

                                        {/*Rest Of World*/}
                                        <Col xs={12} sm={12} md={12} lg={12}>
                                          <FormGroup check>
                                            <Field
                                              render={({input, meta}) => {
                                                return (<Label check>
                                                  <Input
                                                    checked={values?.zones[zoneIndex]?.restOfWorld === true}
                                                    onChange={(event) => {
                                                      input.onChange(true);
                                                    }}
                                                    type="radio"
                                                  />{' '}
                                                  WorldWide
                                                </Label>)
                                              }}
                                              className="form-control"
                                              id={`${zoneName}.restOfWorld`}
                                              name={`${zoneName}.restOfWorld`}
                                              parse={identity}
                                            />
                                          </FormGroup>
                                        </Col>

                                        {/*Specific Countries*/}
                                        <Col xs={12} sm={12} md={12} lg={12}>
                                          <FormGroup check>
                                            <Field
                                              render={({input, meta}) => (<Label check>
                                                <Input
                                                  checked={values?.zones[zoneIndex]?.restOfWorld === false}
                                                  onChange={(event) => {
                                                    input.onChange(false);
                                                  }}
                                                  type="radio"
                                                />{' '}
                                                Specific Countries
                                              </Label>)}
                                              className="form-control"
                                              name={`${zoneName}.restOfWorld`}
                                              id={`${zoneName}.restOfWorld`}
                                              parse={identity}
                                            />
                                          </FormGroup>
                                        </Col>

                                        {!values?.zones[zoneIndex]?.restOfWorld && <>
                                          {/*Countries*/}
                                          <Col xs={12} sm={12} md={12} lg={12}>
                                            <FormGroup>
                                              <Field
                                                render={({input, meta}) => (<>
                                                  <Select
                                                    isSelected={false}
                                                    value={input.value}
                                                    onChange={(value) => {
                                                      input.onChange(value);
                                                    }}
                                                    isMulti
                                                    className="basic-multi-select"
                                                    classNamePrefix="select"
                                                    options={countries && countries.map((ele) => ({
                                                      value: ele.code,
                                                      label: `${ele.code} - ${ele.name}`,
                                                      provinces: [],
                                                      availableProvinces: ele?.provinces?.length ? ele.provinces : [],
                                                    }))}
                                                  />
                                                  {meta.error && (<div style={{
                                                    order: '4',
                                                    width: '100%',
                                                    display: meta.error && meta.touched ? 'block' : 'none'
                                                  }} className="invalid-feedback">{meta.error}</div>)}
                                                </>)}
                                                id={`${zoneName}.countries`}
                                                className="form-control"
                                                name={`${zoneName}.countries`}
                                                parse={identity}
                                                type="select"
                                                // validate={(value, meta) => {
                                                //   return (value && value !== undefined && (Object.keys(value).length > 0 || value.length > 0)) ? undefined : "Please select countries!";
                                                // }}
                                              />
                                            </FormGroup>
                                          </Col>
                                          <Col xs={12} sm={12} md={12} lg={12}>
                                            <FieldArray name={`${zoneName}.countries`}>
                                              {({fields}) => fields.map((country, country2Index) => {
                                                const countryData = values?.zones[zoneIndex]?.countries && values?.zones[zoneIndex]?.countries[country2Index];
                                                const provinceLists = populateApplicableProvinces(countryData);

                                                return (<FormGroup>
                                                  <Row>
                                                    <Col xs={12} sm={12} md={6} lg={3}>
                                                      <Label
                                                        for={`${country}.provinces`}>{country2Index + 1}.&nbsp;{countryData.label}</Label>
                                                    </Col>
                                                    {(provinceLists && provinceLists.length > 0) &&
                                                      <Col xs={12} sm={12} md={6} lg={4}>
                                                        <div className="ml-4 ml-md-0 ml-lg-0 ml-xl-0">
                                                          <FormGroup>
                                                            <Field
                                                              name={`${country}.includeAllProvinces`}
                                                              id={`${country}.includeAllProvinces`}
                                                              type="checkbox"
                                                              className="form-control"
                                                              render={({input, meta}) => {
                                                                return (<>
                                                                  <Label>
                                                                    <Input {...input}
                                                                           checked={values?.zones[zoneIndex]?.countries[country2Index]?.includeAllProvinces || false}
                                                                           onChange={(value) => input.onChange(value)}/> Include
                                                                    All provinces
                                                                  </Label>
                                                                </>);
                                                              }}
                                                            />
                                                          </FormGroup>
                                                        </div>
                                                      </Col>}
                                                    {(!values?.zones[zoneIndex]?.countries[country2Index]?.includeAllProvinces && (provinceLists && provinceLists.length > 0)) &&
                                                      <Col xs={12} sm={12} md={12} lg={5}>
                                                        <Field render={({input, meta}) => (<>
                                                          <Select
                                                            value={getSelectedProvinces(countryData, input.value)}
                                                            isSelected={false}
                                                            onChange={(value) => {
                                                              input.onChange(value);
                                                            }}
                                                            isMulti
                                                            className="basic-multi-select"
                                                            classNamePrefix="select"
                                                            options={provinceLists}
                                                            placeholder={`Select Provinces`}
                                                          />
                                                          {meta.error && (<div style={{
                                                            order: '4',
                                                            width: '100%',
                                                            display: meta.error && meta.touched ? 'block' : 'none'
                                                          }}
                                                                               className="invalid-feedback">{meta.error}</div>)}
                                                        </>)}
                                                               id={`${country}.provinces`}
                                                               className="form-control"
                                                               type="text"
                                                               name={`${country}.provinces`}
                                                          // validate={(value, meta) => {
                                                          //   return (value && value !== undefined && (Object.keys(value).length > 0 || value.length > 0)) ? undefined : "Please select provinces!";
                                                          // }}
                                                        />
                                                      </Col>}
                                                  </Row>
                                                </FormGroup>)
                                              })}
                                            </FieldArray>
                                          </Col>
                                        </>}
                                      </Row>
                                      <Row className={"mt-2"}>
                                        <Col xs={12} sm={12} md={12} lg={12}>
                                          <FieldArray name={`${zoneName}.deliveryMethods`}>
                                            {({fields}) => fields.map((deliveryMethodsName, deliveryMethodsIndex) => {
                                              return (<Row key={deliveryMethodsIndex} className={"mt-3"}>
                                                <hr className={"w-100"}/>

                                                <Col md={12}>
                                                  <Row>
                                                    <Col xs={12} sm={12} md={12} lg={12}>
                                                      <div className="d-flex justify-content-between">
                                                        <p>Rate #{deliveryMethodsIndex + 1}</p>
                                                        <i
                                                          className="text-danger lnr lnr-trash btn-icon-wrapper"
                                                          style={{cursor: "pointer"}}
                                                          onClick={() => fields.remove(deliveryMethodsIndex)}/>
                                                      </div>
                                                    </Col>
                                                    {/*Own Rate*/}
                                                    <Col xs={12} sm={12} md={12} lg={12}>
                                                      <FormGroup check>
                                                        <Field
                                                          render={({input, meta}) => (<Label check>
                                                            <Input
                                                              checked={values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.definitionType === "OWN"}
                                                              onChange={(event) => {
                                                                input.onChange("OWN");
                                                              }}
                                                              type="radio"
                                                            />{' '}
                                                            Set up your own rates
                                                          </Label>)}
                                                          className="form-control"
                                                          name={`${deliveryMethodsName}.definitionType`}
                                                          id={`${deliveryMethodsName}.definitionType`}
                                                          parse={identity}
                                                        />
                                                      </FormGroup>
                                                    </Col>
                                                    {/*Carrier*/}
                                                    <Col xs={12} sm={12} md={12} lg={12}>
                                                      <FormGroup check>
                                                        <Field
                                                          render={({input, meta}) => (<Label check>
                                                            <Input
                                                              checked={values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.definitionType === "CARRIER"}
                                                              onChange={(event) => {
                                                                input.onChange("CARRIER");
                                                              }}
                                                              type="radio"
                                                            />{' '}
                                                            Use carrier or app to calculate rates
                                                          </Label>)}
                                                          className="form-control"
                                                          name={`${deliveryMethodsName}.definitionType`}
                                                          id={`${deliveryMethodsName}.definitionType`}
                                                          parse={identity}
                                                        />
                                                      </FormGroup>
                                                    </Col>
                                                  </Row>

                                                  <>
                                                    {values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.definitionType === "CARRIER" ? <>
                                                      {/*Carrier Rate Fields*/}
                                                      <Row>
                                                        <Col xs={12} sm={12} md={12} lg={12}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.carrierServiceId`}>Carrier
                                                              Service</Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <Input
                                                                  {...input}
                                                                  invalid={meta.error && meta.touched ? true : null}
                                                                >
                                                                  <option value={""}>Choose...
                                                                  </option>
                                                                  {carriers?.availableCarrierServices && carriers?.availableCarrierServices?.map((item, index) => (
                                                                    <option
                                                                      key={item?.carrierService?.id}
                                                                      value={item?.carrierService?.id}
                                                                    >
                                                                      {item?.carrierService?.formattedName}
                                                                    </option>))}
                                                                </Input>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              id={`${deliveryMethodsName}.carrierServiceId`}
                                                              name={`${deliveryMethodsName}.carrierServiceId`}
                                                              className="form-control"
                                                              parse={identity}
                                                              type="select"
                                                              validate={(value, meta) => {
                                                                if (!value) {
                                                                  return "Please select Carrier Service!";
                                                                } else {
                                                                  return undefined;
                                                                }
                                                              }}
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                      </Row>
                                                      <div className={"mt-3 mb-2"}>
                                                        <p className="text-capitalize m-0 p-0">HANDLING
                                                          FEE</p>
                                                        <span className="text-muted mt-1">Adjust calculated rates to account for packaging and handling costs.</span>
                                                      </div>
                                                      <Row>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.carrierPercentageFee`}>
                                                              Percentage of rate
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <InputGroup>
                                                                  <Input
                                                                    {...input}
                                                                    placeholder="Percentage %"
                                                                    invalid={meta.error && meta.touched ? true : null}
                                                                  />
                                                                  <InputGroupAddon addonType='append'>%</InputGroupAddon>
                                                                </InputGroup>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              id={`${deliveryMethodsName}.carrierPercentageFee`}
                                                              name={`${deliveryMethodsName}.carrierPercentageFee`}
                                                              className="form-control"
                                                              parse={identity}
                                                              type="number"
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.carrierFixedFee`}>
                                                              Flat Amount
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <InputGroup>
                                                                  <Input
                                                                    {...input}
                                                                    placeholder="Flat Amount"
                                                                    invalid={meta.error && meta.touched ? true : null}
                                                                  />
                                                                  <InputGroupAddon addonType='append'>Amount</InputGroupAddon>
                                                                </InputGroup>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              className="form-control"
                                                              id={`${deliveryMethodsName}.carrierFixedFee`}
                                                              name={`${deliveryMethodsName}.carrierFixedFee`}
                                                              parse={identity}
                                                              type="number"
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                      </Row>
                                                    </> : <>
                                                      {/*Own Rate Fields*/}
                                                      <Row>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label for={`${deliveryMethodsName}.name`}>
                                                              Name
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <Input
                                                                  placeholder="Rate Name"
                                                                  {...input}
                                                                  invalid={meta.error && meta.touched ? true : null}
                                                                />
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              id={`${deliveryMethodsName}.name`}
                                                              name={`${deliveryMethodsName}.name`}
                                                              className="form-control"
                                                              parse={identity}
                                                              type="text"
                                                              validate={(value, meta) => {
                                                                if (!value) {
                                                                  return "Please provide a valid Rate Name.";
                                                                } else {
                                                                  return undefined;
                                                                }
                                                              }}
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.amount`}>
                                                              Price
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <InputGroup>
                                                                  <Input
                                                                    {...input}
                                                                    placeholder="Price"
                                                                  />
                                                                  <InputGroupAddon addonType='append'>Amount</InputGroupAddon>
                                                                </InputGroup>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                                <FormText>
                                                                  <span>Set 0 to give your customers FREE shipping</span>
                                                                </FormText>
                                                              </>)}
                                                              id={`${deliveryMethodsName}.amount`}
                                                              name={`${deliveryMethodsName}.amount`}
                                                              className="form-control"
                                                              parse={identity}
                                                              type="number"
                                                              validate={(value) => {
                                                                if (parseInt(value) >= 0) {
                                                                  return undefined;
                                                                }
                                                                if (value === null || value === undefined || value === "") {
                                                                  return "Please provide a value for price!";
                                                                }
                                                              }}
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                      </Row>

                                                      <Row>
                                                        <p className="ml-3 text-capitalize">Delivery
                                                          Conditions</p>
                                                        <Col xs={12} sm={12} md={12} lg={12}>
                                                          <FormGroup check>
                                                            <Field
                                                              render={({input, meta}) => (<Label check>
                                                                <Input
                                                                  checked={values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.deliveryConditionType === "WEIGHT"}
                                                                  onChange={(event) => {
                                                                    input.onChange("WEIGHT");
                                                                  }}
                                                                  type="radio"
                                                                />{' '}
                                                                Weight
                                                              </Label>)}
                                                              className="form-control"
                                                              name={`${deliveryMethodsName}.deliveryConditionType`}
                                                              id={`${deliveryMethodsName}.deliveryConditionType`}
                                                              parse={identity}
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                        <Col xs={12} sm={12} md={12} lg={12}>
                                                          <FormGroup check>
                                                            <Field
                                                              render={({input, meta}) => (<Label check>
                                                                <Input
                                                                  checked={values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.deliveryConditionType === "PRICE"}
                                                                  onChange={(event) => {
                                                                    input.onChange("PRICE");
                                                                  }}
                                                                  type="radio"
                                                                />{' '}
                                                                Price
                                                              </Label>)}
                                                              className="form-control"
                                                              name={`${deliveryMethodsName}.deliveryConditionType`}
                                                              id={`${deliveryMethodsName}.deliveryConditionType`}
                                                              parse={identity}
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                      </Row>
                                                      {values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.deliveryConditionType === "WEIGHT" ? <>
                                                        <Row>
                                                          <Col xs={12} sm={12} md={6} lg={6}>
                                                            <Row>
                                                              <Col xs={12} sm={12} md={8} lg={8}
                                                                   className="pr-md-0 pr-lg-0">
                                                                <FormGroup>
                                                                  <Label
                                                                    for={`${deliveryMethodsName}.minValue`}>
                                                                    Minimum Weight
                                                                  </Label>
                                                                  <Field
                                                                    render={({input, meta}) => (<>
                                                                      <Input
                                                                        {...input}
                                                                        placeholder="Minimum Weight"
                                                                        invalid={meta.error && meta.touched ? true : null}
                                                                      />
                                                                      {meta.error && (<div
                                                                        style={{
                                                                          order: "4",
                                                                          width: "100%",
                                                                          display: meta.error && meta.touched ? "block" : "none",
                                                                        }}
                                                                        className="invalid-feedback"
                                                                      >
                                                                        {meta.error}
                                                                      </div>)}
                                                                    </>)}
                                                                    id={`${deliveryMethodsName}.minValue`}
                                                                    className="form-control"
                                                                    name={`${deliveryMethodsName}.minValue`}
                                                                    parse={identity}
                                                                    type="number"
                                                                  />
                                                                </FormGroup>
                                                              </Col>
                                                              <Col xs={12} sm={12} md={4} lg={4}
                                                                   className="pl-md-0 pl-lg-0">
                                                                <FormGroup>
                                                                  <Label
                                                                    for={`${deliveryMethodsName}.weightUnit`}>
                                                                    Weight Type
                                                                  </Label>
                                                                  <Field
                                                                    render={({input, meta}) => (<>
                                                                      <Input
                                                                        {...input}
                                                                        invalid={meta.error && meta.touched ? true : null}
                                                                      >
                                                                        <option value="">Choose...
                                                                        </option>
                                                                        <option value="GRAMS">GRAM
                                                                        </option>
                                                                        <option
                                                                          value="KILOGRAMS">KILOGRAMS
                                                                        </option>
                                                                        <option
                                                                          value="OUNCES">OUNCES
                                                                        </option>
                                                                        <option
                                                                          value="POUNDS">POUNDS
                                                                        </option>
                                                                      </Input>
                                                                      {meta.error && (<div
                                                                        style={{
                                                                          order: "4",
                                                                          width: "100%",
                                                                          display: meta.error && meta.touched ? "block" : "none",
                                                                        }}
                                                                        className="invalid-feedback"
                                                                      >
                                                                        {meta.error}
                                                                      </div>)}
                                                                    </>)}
                                                                    id={`${deliveryMethodsName}.weightUnit`}
                                                                    className="form-control"
                                                                    name={`${deliveryMethodsName}.weightUnit`}
                                                                    parse={identity}
                                                                    type="select"
                                                                  />
                                                                </FormGroup>
                                                              </Col>
                                                            </Row>
                                                          </Col>
                                                          <Col xs={12} sm={12} md={6} lg={6}>
                                                            <FormGroup className="mb-2">
                                                              <Label
                                                                for={`${deliveryMethodsName}.maxValue`}>
                                                                Maximum Weight
                                                              </Label>
                                                              <Field
                                                                render={({input, meta}) => (<>
                                                                  <InputGroup>
                                                                    <Input
                                                                      {...input}
                                                                      placeholder="No Limit"
                                                                      invalid={meta.error && meta.touched ? true : null}
                                                                    />
                                                                    <InputGroupAddon addonType='append'>{values.zones[zoneIndex]?.deliveryMethods[deliveryMethodsIndex]?.weightUnit}</InputGroupAddon>
                                                                  </InputGroup>
                                                                  {meta.error && (<div
                                                                    style={{
                                                                      order: "4",
                                                                      width: "100%",
                                                                      display: meta.error && meta.touched ? "block" : "none",
                                                                    }}
                                                                    className="invalid-feedback"
                                                                  >
                                                                    {meta.error}
                                                                  </div>)}
                                                                </>)}
                                                                id={`${deliveryMethodsName}.maxValue`}
                                                                className="form-control"
                                                                name={`${deliveryMethodsName}.maxValue`}
                                                                parse={identity}
                                                                type="number"
                                                              />
                                                            </FormGroup>
                                                          </Col>
                                                        </Row>
                                                      </> : <Row>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.minValue`}>
                                                              Minimum Price
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <InputGroup>
                                                                  <Input
                                                                    {...input}
                                                                    placeholder="Minimum Price"
                                                                    invalid={meta.error && meta.touched ? true : null}
                                                                  />
                                                                  <InputGroupAddon addonType='append'>Amount</InputGroupAddon>
                                                                </InputGroup>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              id={`${deliveryMethodsName}.minValue`}
                                                              className="form-control"
                                                              name={`${deliveryMethodsName}.minValue`}
                                                              parse={identity}
                                                              type="number"
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <FormGroup>
                                                            <Label
                                                              for={`${deliveryMethodsName}.maxValue`}>
                                                              Maximum Price
                                                            </Label>
                                                            <Field
                                                              render={({input, meta}) => (<>
                                                                <InputGroup>
                                                                  <Input
                                                                    {...input}
                                                                    placeholder="No Limit"
                                                                    invalid={meta.error && meta.touched ? true : null}
                                                                  />
                                                                  <InputGroupAddon addonType='append'>Amount</InputGroupAddon>
                                                                </InputGroup>
                                                                {meta.error && (<div
                                                                  style={{
                                                                    order: "4",
                                                                    width: "100%",
                                                                    display: meta.error && meta.touched ? "block" : "none",
                                                                  }}
                                                                  className="invalid-feedback"
                                                                >
                                                                  {meta.error}
                                                                </div>)}
                                                              </>)}
                                                              id={`${deliveryMethodsName}.maxValue`}
                                                              className="form-control"
                                                              name={`${deliveryMethodsName}.maxValue`}
                                                              parse={identity}
                                                              type="number"
                                                            />
                                                          </FormGroup>
                                                        </Col>
                                                      </Row>}
                                                    </>}
                                                  </>
                                                </Col>
                                              </Row>)
                                            })}
                                          </FieldArray>
                                          <div style={{
                                            display: 'flex', justifyContent: 'space-between', alignItems: 'center'
                                          }}>
                                            <h6 className="text-capitalize"></h6>
                                            <Button
                                              type="button"
                                              className="d-flex align-items-center appstle_order-detail_update-button "
                                              onClick={() => push(`${zoneName}.deliveryMethods`, defaultDeliveryMethod)}
                                            >
                                              Add New Rate
                                            </Button>
                                          </div>
                                        </Col>
                                      </Row>
                                    </CardBody>
                                  </Card>
                                </Col>
                              </Row>)
                            })}
                          </FieldArray>
                        </CardBody>
                      </Card>
                    </Row>
                  </form>)
                }}
              />
            </Col>
          </Row>
        </CardBody>
        {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>How to Create a Shipping Profile</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/AcknZAvk0s8" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>How to Define Delivery Conditions</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/IvIEyR74bvM" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
          </HelpPopUp> */}
        <ToastContainer
          position="top-right"
          autoClose={300}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
      </div>)}
    </ReactCSSTransitionGroup>
  </Fragment>);
};

const mapStateToProps = state => ({
  locations: state.helperActions.locations,
  carriers: state.helperActions.carriers,
  helperActionsLoading: state.helperActions.loading,

  subscriptionGroupEntities: state.subscriptionGroup.entities,
  subscriptionGroupLoading: state.subscriptionGroup.loading,

  shippingProfileEntity: state.shippingProfile.entity,
  loading: state.shippingProfile.loading,
  updating: state.shippingProfile.updating,
  updateSuccess: state.shippingProfile.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
  getAvailableCarrier,
  getSubscriptionGroups,
  getShippingProfileV3,
  saveShippingProfileV3,
  updateShippingProfileV3,
  reset,
  subscriptionGroupsReset
};


export default connect(mapStateToProps, mapDispatchToProps)(CreateShippingProfileV2);
