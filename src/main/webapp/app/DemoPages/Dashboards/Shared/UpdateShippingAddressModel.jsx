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
import Switch from 'react-switch';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getEntity,
  reset,
  updateEntity
} from "app/entities/dunning-management/dunning-management.reducer";
import './loader.scss';
import axios from 'axios';
import { shopify_countries_List } from 'app/shared/util/shopify_countries';


const UpdateShippingAddressModel = (props) => {
  const {
    isUpdating,
    modaltitle,
    confirmBtnText,
    cancelBtnText,
    isUpdateShippingOpenFlag,
    toggleShippingModal,
    updateShippingAddressMethod,
    initialShippingAddress,
    subscriptionEntities
  } = props;

  const onSubmit = values => {
    values = {
        ...values,
        methodType: shippingMethod,
        locationId: shippingMethod === "PICK_UP" ? selectedPickupLocation?.id ? selectedPickupLocation?.id : (selectedPickupLocation?.split("/").pop()) : ""
    }
    updateShippingAddressMethod(values);
  };

  const [shippingMethod, setShippingMethod] = useState("SHIPPING")
  const [avlLocations, setAvlLocations] = useState([]);
  const [pickupLocations, setPickupLocations] = useState([]);
  const [selectedPickupLocation, setSelectedPickupLocation] = useState(null)

  useEffect(() => {
    if (subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodShipping") {
        setShippingMethod("SHIPPING")
    } else if (subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodLocalDelivery") {
        setShippingMethod("LOCAL")
    } else if (subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodPickup") {
        setShippingMethod("PICK_UP")
    }
  }, [subscriptionEntities])

  useEffect(() => {
    axios
      .get('api/delivery-profiles/get-locations')
      .then(data => {
        setAvlLocations(data?.data);
      })
      .catch(e => console.log(e));
  }, []);

  useEffect(() => {
    setPickupLocations(avlLocations.filter(item => item.pickupEnabled))
  }, [avlLocations])

  useEffect(() => {
    setSelectedPickupLocation(pickupLocations[0])
  }, [pickupLocations])

  return (
    <div>
      <Modal isOpen={isUpdateShippingOpenFlag} size='lg' toggle={toggleShippingModal}>
        <ModalHeader toggle={toggleShippingModal}>{modaltitle}</ModalHeader>
        <ModalBody>
          <div style={{ padding: '22px' }}>
            <Row className="mb-2">
                <Col>
                    <Label for="methodType">Shipping Method</Label>
                    <Input onChange={(event) => setShippingMethod(event.target.value)} type="select" value={shippingMethod}>
                        <option value="SHIPPING">Shipping</option>
                        <option value="LOCAL">Local Delivery</option>
                        <option value="PICK_UP">Pickup</option>
                    </Input>
                </Col>
            </Row>
            {shippingMethod === "PICK_UP" && <Row className="mb-2">
                {pickupLocations?.length ? <Col>
                    <Label for="locationId">Pickup Location</Label>
                    <Input onChange={(event) => setSelectedPickupLocation(event.target.value)} type="select" value={selectedPickupLocation}>
                        {pickupLocations?.map(item => {
                                return <option value={item.id}>{item.name}</option>
                        })}
                    </Input>
                </Col> : <Col>
                        No Pickup location setup.
                </Col>}
            </Row>}
            <Form
                initialValues={initialShippingAddress}
                onSubmit={onSubmit}
                render={({ handleSubmit, form, submitting, pristine, values }) => (
                    <form onSubmit={handleSubmit}>
                    {shippingMethod !== "PICK_UP" && <>
                        <Row>
                            <Col md={6}>
                                <Label for="firstname">First Name</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide first name.' : undefined;
                                    // }}
                                    id="firstName"
                                    className="form-control"
                                    type="text"
                                    name="firstName"
                                />
                            </Col>
                            <Col md={6}>
                            <Label for="lastName">Last Name</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide last name.' : undefined;
                                    // }}
                                    id="lastName"
                                    className="form-control"
                                    type="text"
                                    name="lastName"
                                />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col md={6}>
                                <Label for="phone">Phone No</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide phone no.' : undefined;
                                    // }}
                                    id="phone"
                                    className="form-control"
                                    type="text"
                                    name="phone"
                                />
                            </Col>
                            <Col md={6}>
                            <Label for="city">City</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide city name.' : undefined;
                                    // }}
                                    id="city"
                                    className="form-control"
                                    type="text"
                                    name="city"
                                />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col md={6}>
                                <Label for="address1">Address 1</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide address1.' : undefined;
                                    // }}
                                    id="address1"
                                    className="form-control"
                                    type="text"
                                    name="address1"
                                />
                            </Col>
                            <Col md={6}>
                            <Label for="address2">Address 2</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide address2.' : undefined;
                                    // }}
                                    id="address2"
                                    className="form-control"
                                    type="text"
                                    name="address2"
                                />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                        <Col md={6}>
                                <Label for="company">Company</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        </>
                                    )}
                                    id="company"
                                    className="form-control"
                                    type="text"
                                    name="company"
                                />
                            </Col>
                            {/* <Col md={6}>
                                <Label for="province">Province</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        </>
                                    )}
                                    id="province"
                                    className="form-control"
                                    type="text"
                                    name="province"
                                />
                            </Col> */}
                            <Col md={6}>
                            <Label for="zip">Zip</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>
                                        {/* {meta.error && (
                                            <div
                                            class="invalid-feedback"
                                            style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                            >
                                            {meta.error}
                                            </div>
                                        )} */}
                                        </>
                                    )}
                                    // validate={value => {
                                    // return !value ? 'Please provide zip.' : undefined;
                                    // }}
                                    id="zip"
                                    className="form-control"
                                    type="text"
                                    name="zip"
                                />
                            </Col>
                        </Row>
                        <Row>

                            {/* <Col md={4}>
                            <Label for="country">Country</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>

                                        </>
                                    )}

                                    id="country"
                                    className="form-control"
                                    type="text"
                                    name="country"
                                />
                            </Col> */}
                            <Col md={6}>
                            <Label for="countryCode">Country Code</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <div>
                                        <select
                                            {...input}
                                            id="countryCode"
                                            class="form-control">
                                            {Object.keys(shopify_countries_List)?.map(item => {
                                                return (<option value={shopify_countries_List[item]['code']}>{item} ({shopify_countries_List[item]['code']})</option>)
                                            })}
                                        </select>
                                        </div>
                                    )}
                                    id="countryCode"
                                    className="form-control"
                                    type="select"
                                    name="countryCode"
                                />
                            </Col>
                            <Col md={6}>
                                <Label for="provinceCode">Province Code</Label>
                                <Field
                                    render={({input, meta}) =>  {
                                        let countryName = ''
                                        Object.keys(shopify_countries_List).forEach(item => {
                                            if (shopify_countries_List[item]?.code === values?.countryCode) {
                                                countryName = item;
                                            }
                                        })
                                        return (
                                            <div>
                                            <select
                                                {...input}
                                                id="provinceCode"
                                                class="form-control">
                                                {shopify_countries_List[countryName]?.['provinces'] && Object.keys(shopify_countries_List[countryName]?.['provinces'])?.map(item => {
                                                    return (<option value={shopify_countries_List[countryName]['provinces'][item]['code']}>{item} ({shopify_countries_List[countryName]['provinces'][item]['code']})</option>)
                                                })}
                                            </select>
                                            </div>
                                        )
                                    }}
                                    // validate={value => {
                                    // return !value ? 'Please provide province Code.' : undefined;
                                    // }}
                                    id="provinceCode"
                                    className="form-control"
                                    type="text"
                                    name="provinceCode"
                                />
                            </Col>
                        </Row>
                    </>}

                    <div style={{marginTop: '21px', display: 'flex', justifyContent: 'center'}}>
                        <Button
                        style={{marginRight: '12px'}}
                        size="lg"
                        className="btn-shadow-primary"
                        color="primary"
                        type="submit"
                        // disabled={submitting}
                        >
                        {isUpdating ?
                            <div className="d-flex align-items-center">
                                <div className="appstle_loadersmall" />
                                <span className="ml-2 font-weight-light"> Please Wait</span>
                            </div>
                            : confirmBtnText}
                        </Button>
                        <Button
                            size="lg"
                            className="btn-shadow-primary"
                            color="danger"
                            type="button"
                            onClick={toggleShippingModal}
                        >
                        cancel
                        </Button>
                    </div>
                    </form>
                )}
                />
          </div>
        </ModalBody>
      </Modal>
    </div>
  );
}

export default UpdateShippingAddressModel;
