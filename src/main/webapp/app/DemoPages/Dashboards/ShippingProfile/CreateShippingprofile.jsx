import React, {Fragment, useEffect, useRef, useState} from 'react';
import Loader from 'react-loaders';
import axios from 'axios';
//import './setting.scss';0
import {Button, Card, CardBody, Col, FormGroup, Input, Label, Row} from 'reactstrap';
import Switch from 'react-switch';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createShippingEntityLatest,
  getEntity,
  reset,
  updateEntity,
  updateShippingProfile
} from 'app/entities/delivery-profile/delivery-profile.reducer';
import arrayMutators from 'final-form-arrays';
import {FieldArray} from 'react-final-form-arrays';
import _ from 'lodash';
import './CreateShippingProfile.scss';
import {countryCodes} from './CountryCode';
import {Help} from '@mui/icons-material';
import { Tooltip as ReactTooltip } from 'react-tooltip';

const CreateShippingProfile = props => {
  const {shippingProfileEntity, loading, updating, updateShippingProfile} = props;

  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  const [formData, setFormData] = useState({...shippingProfileEntity});
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const shippingProfileRef = useRef(null);
  const [editPlanData, setEditPlanData] = useState();
  const [avlCarrierService, setAvlCarrierService] = useState([]);
  const [avlLocations, setAvlLocations] = useState([]);
  let [deliveryProfile, setDeliveryProfile] = useState({})

  const handleClose = () => {
    props.history.push('/dashboards/shipping-profile');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    axios
      .get('api/data/available-carriers')
      .then(data => {
        console.log(JSON.stringify(data));
        setAvlCarrierService(data?.data?.availableCarrierServices);
      })
      .catch(e => console.log(e));
  }, []);

  useEffect(() => {
    axios
      .get('api/delivery-profiles/get-locations')
      .then(data => {
        console.log(JSON.stringify(data));
        setAvlLocations(data?.data);
      })
      .catch(e => console.log(e));
  }, []);

  useEffect(() => setFormData(shippingProfileEntity), [props]);

  useEffect(() => {
    if (shippingProfileEntity?.deliveryProfile) {
      let locationDetails = [];

      shippingProfileEntity?.deliveryProfile?.profileLocationGroups?.map((locations, locationIndex) => {
        const location = {
          locationId: locations?.locationGroup?.locations?.edges[0]?.node?.id,
          countryInfos: []
        }
        locationDetails.push(location)

        locations?.locationGroupZones?.edges.map((otherInfo) => {
          const zone = otherInfo?.node?.zone;
          const methodDefinitions = otherInfo?.node?.methodDefinitions;

          zone?.countries.map((country, countryIndex) => {
            let provinces = [];
            country?.provinces && country?.provinces.forEach((province) => {
              provinces.push(province?.code);
            })
            locationDetails[locationIndex] = {
              ...locationDetails[locationIndex], countryInfos: [...locationDetails[locationIndex]?.countryInfos, {
                ...(country?.code?.countryCode ? {code: country?.code?.countryCode} : {}),
                restOfWorld: country?.code?.restOfWorld,
                // ...(country?.id ? {id: country?.id} : {}),
                ...(provinces ? {provinceCode: provinces.toString()} : {}),
                // ...(provinces.length > 1 ? {shouldIncludeAllProvince: true} : {}),

                deliveryMethodInfo: methodDefinitions ? methodDefinitions?.edges.map((methodDef, methodIndex) => {
                  const methodConditions = methodDef?.node?.methodConditions
                  const name = methodDef?.node?.name;
                  const methodDefId = methodDef?.node?.id;
                  const amount = methodDef?.node?.rateProvider?.price?.amount;
                  const rateProvider = methodDef?.node?.rateProvider;
                  const isCarrierService = rateProvider?.adaptToNewServicesFlag ? rateProvider?.adaptToNewServicesFlag : false;
                  const carrierServiceId = rateProvider?.carrierService?.id;

                  let deliveryPriceCondition = []
                  let deliveryWeightCondition = []

                  const conditions = () => {
                    methodConditions && methodConditions.map((methodCond) => {
                      const deliveryCondition = methodCond?.operator;
                      const deliveryConditionAmount = methodCond?.conditionCriteria?.amount;
                      const weightUnit = methodCond?.conditionCriteria?.unit;
                      const weight = methodCond?.conditionCriteria?.value;
                      if (deliveryCondition && deliveryConditionAmount) {
                        deliveryPriceCondition.push({
                          deliverCondtion: deliveryCondition,
                          amount: deliveryConditionAmount
                        })
                      }
                      if (deliveryCondition && weightUnit && weight) {
                        deliveryWeightCondition.push({
                          deliveryCondition: deliveryCondition,
                          weightUnit: weightUnit,
                          weight: weight
                        })
                      }
                    })
                  }
                  if (methodConditions !== undefined && methodConditions !== null && methodConditions) {
                    conditions();
                  }

                  return {
                    // ...(methodDefId ? {id: methodDefId} : {}),
                    name,
                    ...(amount ? {amount: amount} : {}),
                    isCarrierService,
                    ...(isCarrierService ? {carrierServiceId: carrierServiceId} : {}),
                    ...(deliveryPriceCondition.length > 0 ? {priceConditions: deliveryPriceCondition} : {}),
                    ...(deliveryWeightCondition.length > 0 ? {weightConditions: deliveryWeightCondition} : {}),
                  }
                }) : []
              }]
            }
          })
        })
      })
      updateShippingProfile({
        id: shippingProfileEntity?.deliveryProfile?.id,
        name: shippingProfileEntity?.deliveryProfile?.name,
        locationInfos: locationDetails
      })
    }
  }, [shippingProfileEntity?.deliveryProfile]);


  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  const saveEntity = values => {
    if (values?.restOfWorld == true) {
      delete values.countryInfos;
    }
    if (isNew) {
      props.createShippingEntityLatest(values);
    } else {
      props.updateEntity(values)
    }
  };


  let [editMode, setEditMode] = useState(false);
  let [viaEditButton, setViaEditButton] = useState(false);
  let submit;
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
        heading={isNew ? 'Create Shipping profile' : 'Edit Shipping profile'}
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
      />
      {loading ? (<div style={{margin: '10% 0 0 43%'}}
                       className="loader-wrapper d-flex justify-content-center align-items-center">
        <Loader type="line-scale"/>
      </div>) : (<div>
        <CardBody>
          <Form
            mutators={{
              ...arrayMutators
            }}
            initialValues={shippingProfileEntity}
            onSubmit={saveEntity}
            render={({
                       handleSubmit, form: {
                mutators: {push, pop, update, remove}
              }, form, submitting, pristine, values, errors, valid
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
                  <Col md="3">
                    <h5>Shipping Information</h5>
                  </Col>
                  <Col md="9">
                    <Card className="card-margin">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup>
                              <Label for="name">Shipping Name</Label>
                              <Field
                                render={({input, meta}) => (<>
                                  <Input {...input} invalid={meta.error && meta.touched ? true : null}/>
                                  {meta.error && (<div
                                    style={{
                                      order: '4', width: '100%', display: meta.error && meta.touched ? 'block' : 'none'
                                    }}
                                    className="invalid-feedback"
                                  >
                                    {meta.error}
                                  </div>)}
                                </>)}
                                validate={value => {
                                  return !value ? 'Please provide Shipping name.' : undefined;
                                }}
                                id="name"
                                className="form-control"
                                type="text"
                                name="name"
                                placeholder="Shipping name"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                      </CardBody>
                    </Card>
                  </Col>
                </Row>
                <hr/>
                <Row>
                  <Col md={3}>
                    <h5>Location Info</h5>
                  </Col>
                  <Col md={9}>
                    <Card style={{marginBottom: '20px'}}>
                      <CardBody>
                        <FieldArray name="locationInfos">
                          {({fields}) => fields.map((locationArray, locationIndex) => (<div>
                            <div style={{textAlign: 'end', marginBottom: '14px', display: 'flex'}}>
                              <Button
                                onClick={() => {
                                  push('locationInfos', undefined);
                                  setEditMode(true);
                                  setEditPlanData('');
                                  setViaEditButton(false);
                                }}
                                size="lg"
                                className="btn-shadow-primary addPlanButton"
                                color="primary"
                              >
                                {values?.['locationInfos']?.length > 0 ? 'Add more Location' : 'Add Location'}
                              </Button>
                              {values?.['locationInfos']?.length > 1 && (<div style={{textAlign: 'right'}}>
                                <Button
                                  style={{padding: '11px 12px'}}
                                  color="danger"
                                  className="ml-2 primary btn btn-primary d-flex align-items-center"
                                  onClick={() => fields.remove(locationIndex)}
                                >
                                  <i className="lnr lnr-trash btn-icon-wrapper"></i>
                                </Button>
                              </div>)}
                            </div>
                            <Label>Location Name</Label>
                            <Field
                              type="select"
                              id={`${locationArray}.locationId`}
                              name={`${locationArray}.locationId`}
                              render={({input, meta}) => (
                                <Input invalid={meta.error && meta.touched ? true : null} {...input}>
                                  <option value="">Select Location</option>
                                  {avlLocations.map(ele => (<option value={ele.id}>{ele.name}</option>))}
                                </Input>)}
                              className="form-control"
                            />
                            {/* <Field
                                    type="select"
                                    id={`${locationArray}.locationId`}
                                    className="mr-2"
                                    name={`${locationArray}.locationId`}
                                    render={({ input, meta }) => (
                                      <>
                                        <Select
                                          // {...input}
                                          onChange={input.onChange}
                                          isSelected={false}
                                          // defaultValue={[options[0]]}
                                          isMulti
                                          isSearchable
                                          options={avlLocations.map(ele => ({
                                            value: ele.id,
                                            label: ele.name,
                                            locationId: ele.id
                                          }))}
                                          className="basic-multi-select"
                                          classNamePrefix="select"
                                        />
                                      </>
                                    )}
                                    validate={value => {
                                      return !value ? 'Please select Location name.' : undefined;
                                    }}
                                  /> */}
                            <hr/>
                            <Row>
                              <Col md="6">
                                <h5>Country Info</h5>
                              </Col>
                              <Col md="6">
                                <div style={{textAlign: 'end', marginBottom: '14px'}}>
                                  <Button
                                    onClick={() => {
                                      push(`${locationArray}.countryInfos`, undefined);
                                      setEditMode(true);
                                      setEditPlanData('');
                                      setViaEditButton(false);
                                    }}
                                    size="lg"
                                    className="btn-shadow-primary addPlanButton"
                                    color="primary"
                                  >
                                    {values?.['locationInfos'][locationIndex]?.['countryInfos']?.length > 0 ? 'Add more Country' : 'Add Country'}
                                  </Button>
                                </div>
                              </Col>
                            </Row>
                            <div>
                              <FieldArray name={`${locationArray}.countryInfos`}>
                                {({fields}) => fields.map((name, index) => (
                                  <Card key={name} style={{marginBottom: '20px'}}>
                                    <CardBody>
                                      <FormGroup style={{display: 'flex', alignItems: 'center'}}>
                                        <label style={{marginRight: '5px'}}>
                                          <b>Rest Of World</b>
                                        </label>
                                        <Field
                                          id={`${name}.restOfWorld`}
                                          name={`${name}.restOfWorld`}
                                          initialValue={false}
                                          render={({input, meta}) => {
                                            return (<Switch
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
                                            />);
                                          }}
                                        />
                                        {!values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.restOfWorld && values?.['locationInfos'][locationIndex]?.['countryInfos']?.length > 1 && (
                                          <div style={{textAlign: 'right', marginLeft: 'auto'}}>
                                            <Button
                                              style={{padding: '11px 12px'}}
                                              color="danger"
                                              className="primary btn btn-primary d-flex align-items-center"
                                              onClick={() => fields.remove(index)}
                                            >
                                              <i className="lnr lnr-trash btn-icon-wrapper"></i>
                                            </Button>
                                          </div>)}
                                      </FormGroup>
                                      {!values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['restOfWorld'] && (<>
                                        <div>
                                          <Label>Country Code</Label>
                                          <Field
                                            type="select"
                                            id={`${name}.code`}
                                            name={`${name}.code`}
                                            render={({input, meta}) => (<Input
                                              invalid={meta.error && meta.touched ? true : null} {...input}>
                                              <option value="">Select Country Code</option>
                                              {countryCodes.map(ele => (<option
                                                value={ele.code}>{ele.name + '(' + ele.code + ')'}</option>))}
                                            </Input>)}
                                            className="form-control"
                                          />
                                        </div>
                                        <FormGroup style={{
                                          display: 'flex', alignItems: 'center', marginTop: '20px'
                                        }}>
                                          <label style={{marginRight: '5px'}}>
                                            <b>Should Include All Province</b>
                                          </label>
                                          <Field
                                            name={`${name}.shouldIncludeAllProvince`}
                                            render={({input, meta}) => {
                                              return (<Switch
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
                                              />);
                                            }}
                                          />
                                        </FormGroup>
                                        <Row>
                                          <Col xs={12} sm={12} md={12} lg={12}>
                                            <div style={{display: 'flex', flexWrap: 'wrap'}}>
                                              <div style={{flexGrow: 1, marginRight: '4%'}}>
                                                {!values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['shouldIncludeAllProvince'] ? (
                                                  <div>
                                                    <ReactTooltip
                                                      html={true}
                                                      id={`${name}.provinceCode`}
                                                      effect="solid"
                                                      delayUpdate={500}
                                                      place="right"
                                                      border={true}
                                                      type="info"
                                                    />
                                                    <Label>Province Code</Label>{' '}
                                                    <Help
                                                      style={{fontSize: '1rem'}}
                                                      data-for={`${name}.provinceCode`}
                                                      data-tip={"<div style='max-width:300px'>Enter a province code separated by comma. ( Ex. GJ,MH,RJ like this )</div>"}
                                                    />
                                                    <Field
                                                      render={({input, meta}) => (<>
                                                        <Input {...input}
                                                               invalid={meta.error && meta.touched ? true : null}/>
                                                        {meta.error && (<div
                                                          style={{
                                                            order: '4',
                                                            width: '100%',
                                                            display: meta.error && meta.touched ? 'block' : 'none'
                                                          }}
                                                          className="invalid-feedback"
                                                        >
                                                          {meta.error}
                                                        </div>)}
                                                      </>)}
                                                      id={`${name}.provinceCode`}
                                                      className="form-control"
                                                      type="text"
                                                      name={`${name}.provinceCode`}
                                                      placeholder="Province Code"
                                                    />
                                                  </div>) : (<></>)}
                                              </div>
                                            </div>
                                          </Col>
                                        </Row>
                                        <hr/>
                                      </>)}
                                      <Row>
                                        <Col md="6">
                                          <h5>Delivery Method Info</h5>
                                        </Col>
                                        <Col md="6">
                                          <div style={{textAlign: 'end', marginBottom: '14px'}}>
                                            <Button
                                              onClick={() => {
                                                push(`${name}.deliveryMethodInfo`, undefined);
                                                setEditMode(true);
                                                setEditPlanData('');
                                                setViaEditButton(false);
                                              }}
                                              size="lg"
                                              className="btn-shadow-primary addPlanButton"
                                              color="primary"
                                              // style={{ display: !errors?.['deliveryMethodInfo'] ? 'inline-block' : 'none' }}
                                            >
                                              {values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo']?.length > 0 ? 'Add more Delivery Method' : 'Add Delivery Method'}
                                            </Button>
                                          </div>
                                        </Col>
                                      </Row>
                                      <div>
                                        <FieldArray name={`${name}.deliveryMethodInfo`}>
                                          {({fields}) => fields.map((deliveryMethodName, deliveryIndex) => (
                                            <Card key={name} style={{marginBottom: '20px'}}>
                                              <CardBody>
                                                <FormGroup
                                                  style={{display: 'flex', alignItems: 'center'}}>
                                                  <label style={{marginRight: '5px'}}>
                                                    <b> Enable Carrier Service</b>
                                                  </label>
                                                  <Field
                                                    name={`${deliveryMethodName}.isCarrierService`}
                                                    initialValue={false}
                                                    render={({input, meta}) => {
                                                      return (<Switch
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
                                                      />);
                                                    }}
                                                  />
                                                </FormGroup>
                                                <Row>
                                                  <Col xs={12} sm={12} md={12} lg={12}>
                                                    <div style={{display: 'flex', flexWrap: 'wrap'}}>
                                                      <div style={{flexGrow: 1, marginRight: '4%'}}>
                                                        <Label>Name</Label>
                                                        <Field
                                                          type="text"
                                                          id={`${deliveryMethodName}.name`}
                                                          className="mr-2"
                                                          name={`${deliveryMethodName}.name`}
                                                          render={({input, meta}) => (<>
                                                            <Input
                                                              {...input}
                                                              style={{
                                                                flexGrow: 1, width: '100%', marginRight: '4%'
                                                              }}
                                                              type="text"
                                                              invalid={meta.error && meta.touched ? true : null}
                                                            />
                                                            {meta.error && (<div
                                                              style={{
                                                                order: '4',
                                                                width: '100%',
                                                                display: meta.error && meta.touched ? 'block' : 'none'
                                                              }}
                                                              className="invalid-feedback"
                                                            >
                                                              {meta.error}
                                                            </div>)}
                                                          </>)}
                                                          validate={(value, meta) => {
                                                            if (!value) {
                                                              return 'Please provide a valid value for the Name.';
                                                            } else {
                                                              return undefined;
                                                            }
                                                          }}
                                                        />
                                                      </div>

                                                      {values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['isCarrierService'] ? (
                                                        <div>
                                                          <Label>Carrier Service</Label>
                                                          <Field
                                                            id={`${deliveryMethodName}.carrierServiceId`}
                                                            className="mr-2"
                                                            name={`${deliveryMethodName}.carrierServiceId`}
                                                            initialValue={avlCarrierService[0]?.carrierService?.id}
                                                            render={({input, meta}) => (<>
                                                              <Input
                                                                {...input}
                                                                className=""
                                                                style={{
                                                                  flexGrow: 1, width: '100%', marginRight: '4%'
                                                                }}
                                                                type="select"
                                                                invalid={meta.error && meta.touched ? true : null}
                                                              >
                                                                {avlCarrierService?.map(({carrierService}, index) => {
                                                                  return (<option value={carrierService?.id}
                                                                                  selected={index == 0}>
                                                                    {carrierService?.formattedName}
                                                                  </option>);
                                                                })}
                                                              </Input>
                                                              {meta.error && (<div
                                                                style={{
                                                                  order: '4',
                                                                  width: '100%',
                                                                  display: meta.error && meta.touched ? 'block' : 'none'
                                                                }}
                                                                className="invalid-feedback"
                                                              >
                                                                {meta.error}
                                                              </div>)}
                                                            </>)}
                                                          />
                                                        </div>) : (<div>
                                                        <Label>Delivery Fee</Label>
                                                        <Field
                                                          type="number"
                                                          id={`${deliveryMethodName}.amount`}
                                                          className="mr-2"
                                                          name={`${deliveryMethodName}.amount`}
                                                          render={({input, meta}) => (<>
                                                            <Input
                                                              {...input}
                                                              className=""
                                                              style={{flexGrow: 1, marginRight: '4%'}}
                                                              type="number"
                                                              invalid={meta.error && meta.touched ? true : null}
                                                            />
                                                            {meta.error && (<div
                                                              style={{
                                                                display: meta.error && meta.touched ? 'block' : 'none'
                                                              }}
                                                              className="invalid-feedback"
                                                            >
                                                              {meta.error}
                                                            </div>)}
                                                          </>)}
                                                          validate={(value, meta) => {
                                                            if (!value) {
                                                              return 'Please provide a valid value for the Delivery Fee.';
                                                            } else if (value < 0) {
                                                              return 'Delivery Fee Must be Greater than 0.';
                                                            } else {
                                                              return undefined;
                                                            }
                                                          }}
                                                        />
                                                      </div>)}

                                                      {values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo']?.length > 1 && (
                                                        <div
                                                          style={{textAlign: 'right', marginTop: '33px'}}>
                                                          <Button
                                                            style={{padding: '11px 12px'}}
                                                            color="danger"
                                                            className="ml-2 primary btn btn-primary d-flex align-items-center"
                                                            onClick={() => fields.remove(deliveryIndex)}
                                                          >
                                                            <i
                                                              className="lnr lnr-trash btn-icon-wrapper"></i>
                                                          </Button>
                                                        </div>)}
                                                    </div>
                                                    <hr/>
                                                    {!values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['isCarrierService'] ? (
                                                      <div style={{
                                                        display: 'flex', flexGrow: 1, flexWrap: 'wrap'
                                                      }}>
                                                        <div style={{flex: 1}}>
                                                                <span style={{fontSize: '18px'}}>
                                                                  <span class="lnr lnr-question-circle"></span> Want to Add Some Delivery
                                                                  Condition ?
                                                                </span>
                                                        </div>
                                                        {values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['priceConditions']?.length > 0 && values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['priceConditions']?.length < 2 ? (
                                                          <Button
                                                            size="lg"
                                                            className="btn-shadow-primary ml-1"
                                                            color="info"
                                                            onClick={() => {
                                                              push(`${deliveryMethodName}.priceConditions`, undefined);
                                                            }}
                                                          >
                                                            Add Price Condition
                                                          </Button>) : values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['weightConditions']?.length > 0 && values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['weightConditions']?.length < 2 ? (
                                                          <Button
                                                            size="lg"
                                                            className="btn-shadow-primary ml-1"
                                                            color="info"
                                                            onClick={() => {
                                                              push(`${deliveryMethodName}.weightConditions`, undefined);
                                                            }}
                                                          >
                                                            Add Weight Condition
                                                          </Button>) : values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['priceConditions']?.length == 2 || values?.['locationInfos'][locationIndex]?.['countryInfos'][index]?.['deliveryMethodInfo'][deliveryIndex]?.['weightConditions']?.length == 2 ? ('') : (<>
                                                          <Button
                                                            size="lg"
                                                            className="btn-shadow-primary ml-1"
                                                            color="info"
                                                            onClick={() => {
                                                              push(`${deliveryMethodName}.priceConditions`, undefined);
                                                            }}
                                                          >
                                                            Add Price Condition
                                                          </Button>
                                                          <Button
                                                            size="lg"
                                                            className="btn-shadow-primary ml-1"
                                                            color="info"
                                                            onClick={() => {
                                                              push(`${deliveryMethodName}.weightConditions`, undefined);
                                                            }}
                                                          >
                                                            Add Weight Condition
                                                          </Button>
                                                        </>)}
                                                      </div>) : ('')}
                                                    {/* PRICE CONDITION START */}
                                                    <FieldArray
                                                      name={`${deliveryMethodName}.priceConditions`}>
                                                      {({fields}) => fields.map((priceName, priceIndex) => (<div
                                                        style={{display: 'flex', flexWrap: 'wrap'}}>
                                                        <FormGroup style={{
                                                          flexGrow: 1, flexWrap: 'wrap', marginRight: '4%'
                                                        }}>
                                                          <Label>Amount</Label>
                                                          <Field
                                                            type="number"
                                                            id={`${priceName}.amount`}
                                                            className="mr-2"
                                                            name={`${priceName}.amount`}
                                                            render={({input, meta}) => (<>
                                                              <Input
                                                                {...input}
                                                                className=""
                                                                style={{
                                                                  flexGrow: 1, marginRight: '4%'
                                                                }}
                                                                type="number"
                                                                invalid={meta.error && meta.touched ? true : null}
                                                              />
                                                              {meta.error && (<div
                                                                style={{
                                                                  order: '4',
                                                                  width: '100%',
                                                                  display: meta.error && meta.touched ? 'block' : 'none'
                                                                }}
                                                                className="invalid-feedback"
                                                              >
                                                                {meta.error}
                                                              </div>)}
                                                            </>)}
                                                            validate={(value, meta) => {
                                                              if (!value) {
                                                                return 'Please provide a valid value for the Amount.';
                                                              } else if (value < 0) {
                                                                return 'Amount Must be Greater than 0.';
                                                              } else {
                                                                return undefined;
                                                              }
                                                            }}
                                                          />
                                                        </FormGroup>

                                                        <FormGroup
                                                          style={{flexGrow: 1, flexWrap: 'wrap'}}>
                                                          <Label>Delivery Condition</Label>
                                                          <Field
                                                            type="select"
                                                            id={`${priceName}.deliverCondtion`}
                                                            name={`${priceName}.deliverCondtion`}
                                                            initialValue="GREATER_THAN_OR_EQUAL_TO"
                                                            className="form-control"
                                                            render={({input, meta}) => (<>
                                                              <Input
                                                                invalid={meta.error && meta.touched ? true : null}
                                                                style={{
                                                                  // width: '48%',
                                                                  // maxWidth: '200px',
                                                                  marginLeft: 'auto', flexGrow: 1
                                                                }}
                                                                {...input}
                                                              >
                                                                <option
                                                                  value="GREATER_THAN_OR_EQUAL_TO">
                                                                  Greater or Equals
                                                                </option>
                                                                <option
                                                                  value="LESS_THAN_OR_EQUAL_TO">Less
                                                                  or Equals
                                                                </option>
                                                              </Input>
                                                              {meta.error && (<div
                                                                style={{
                                                                  order: '4',
                                                                  width: '100%',
                                                                  display: meta.error && meta.touched ? 'block' : 'none'
                                                                }}
                                                                className="invalid-feedback"
                                                              >
                                                                {meta.error}
                                                              </div>)}
                                                            </>)}
                                                          />
                                                        </FormGroup>
                                                        <div style={{
                                                          textAlign: 'right', marginTop: '33px'
                                                        }}>
                                                          <Button
                                                            style={{padding: '11px 12px'}}
                                                            color="danger"
                                                            className="ml-2 primary btn btn-primary d-flex align-items-center"
                                                            onClick={() => fields.remove(priceIndex)}
                                                          >
                                                            <i
                                                              className="lnr lnr-trash btn-icon-wrapper"></i>
                                                          </Button>
                                                        </div>
                                                      </div>))}
                                                    </FieldArray>

                                                    {/* PRICE CONDITION END */}

                                                    {/* WEIGHT CONDITION START */}

                                                    <FieldArray
                                                      name={`${deliveryMethodName}.weightConditions`}>
                                                      {({fields}) => fields.map((weightName, weightindex) => (<div
                                                        style={{display: 'flex', flexWrap: 'wrap'}}>
                                                        <FormGroup
                                                          style={{
                                                            flexGrow: 1,
                                                            flexWrap: 'wrap',
                                                            marginRight: '3%',
                                                            width: '20px'
                                                          }}
                                                        >
                                                          <Label>Weight</Label>
                                                          <Field
                                                            type="number"
                                                            id={`${weightName}.weight`}
                                                            className="mr-2"
                                                            name={`${weightName}.weight`}
                                                            render={({input, meta}) => (<>
                                                              <Input
                                                                {...input}
                                                                className=""
                                                                style={{
                                                                  flexGrow: 1, marginRight: '4%'
                                                                }}
                                                                type="number"
                                                                invalid={meta.error && meta.touched ? true : null}
                                                              />
                                                              {meta.error && (<div
                                                                style={{
                                                                  order: '4',
                                                                  width: '100%',
                                                                  display: meta.error && meta.touched ? 'block' : 'none'
                                                                }}
                                                                className="invalid-feedback"
                                                              >
                                                                {meta.error}
                                                              </div>)}
                                                            </>)}
                                                            validate={(value, meta) => {
                                                              if (!value) {
                                                                return 'Please provide a valid Weight.';
                                                              } else if (value <= 0) {
                                                                return 'Weight must be greater than 0.';
                                                              } else {
                                                                return undefined;
                                                              }
                                                            }}
                                                          />
                                                        </FormGroup>
                                                        <FormGroup style={{
                                                          flexGrow: 1, flexWrap: 'wrap', marginRight: '3%'
                                                        }}>
                                                          <Label>Weight Unit</Label>
                                                          <Field
                                                            type="select"
                                                            id={`${weightName}.weightUnit`}
                                                            name={`${weightName}.weightUnit`}
                                                            render={({input, meta}) => (<Input
                                                              invalid={meta.error && meta.touched ? true : null}
                                                              style={{
                                                                // width: '48%',
                                                                // maxWidth: '200px',
                                                                marginLeft: 'auto', flexGrow: 1
                                                              }}
                                                              {...input}
                                                            >
                                                              <option value="KILOGRAMS">KG</option>
                                                              <option value="GRAMS">GRAM</option>
                                                              <option value="POUNDS">Pound</option>
                                                              <option value="OUNCES">Ounces</option>
                                                              <option value="OTHER">Other</option>
                                                            </Input>)}
                                                            initialValue="KILOGRAMS"
                                                            className="form-control"
                                                          />
                                                        </FormGroup>

                                                        <FormGroup
                                                          style={{flexGrow: 1, flexWrap: 'wrap'}}>
                                                          <Label>Delivery Condition</Label>
                                                          <Field
                                                            type="select"
                                                            id={`${weightName}.deliveryCondition`}
                                                            name={`${weightName}.deliveryCondition`}
                                                            render={({input, meta}) => (<Input
                                                              invalid={meta.error && meta.touched ? true : null}
                                                              style={{
                                                                // width: '48%',
                                                                // maxWidth: '200px',
                                                                marginLeft: 'auto', flexGrow: 1
                                                              }}
                                                              {...input}
                                                            >
                                                              <option
                                                                value="GREATER_THAN_OR_EQUAL_TO">
                                                                Greater or Equals
                                                              </option>
                                                              <option
                                                                value="LESS_THAN_OR_EQUAL_TO">Less
                                                                or Equals
                                                              </option>
                                                            </Input>)}
                                                            initialValue="GREATER_THAN_OR_EQUAL_TO"
                                                            className="form-control"
                                                          />
                                                        </FormGroup>
                                                        <div style={{
                                                          textAlign: 'right', marginTop: '33px'
                                                        }}>
                                                          <Button
                                                            color="danger"
                                                            style={{padding: '11px 12px'}}
                                                            className="ml-2 primary btn btn-primary d-flex align-items-center"
                                                            onClick={() => fields.remove(weightindex)}
                                                          >
                                                            <i
                                                              className="lnr lnr-trash btn-icon-wrapper"/>
                                                          </Button>
                                                        </div>
                                                      </div>))}
                                                    </FieldArray>
                                                    {/* WEIGHT CONDITION END */}
                                                  </Col>
                                                </Row>
                                              </CardBody>
                                            </Card>))}
                                        </FieldArray>
                                      </div>
                                    </CardBody>
                                  </Card>))}
                              </FieldArray>
                            </div>

                            <hr/>
                          </div>))}
                        </FieldArray>
                      </CardBody>
                    </Card>
                  </Col>
                </Row>
              </form>);
            }}
          />
        </CardBody>
      </div>)}
    </ReactCSSTransitionGroup>
  </Fragment>);
};

const mapStateToProps = storeState => ({
  shippingProfileEntity: storeState.deliveryProfile.entity,
  loading: storeState.deliveryProfile.loading,
  updating: storeState.deliveryProfile.updating,
  updateSuccess: storeState.deliveryProfile.updateSuccess
});

const mapDispatchToProps = {
  getEntity, createShippingEntityLatest, updateEntity, reset, updateShippingProfile
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateShippingProfile);
