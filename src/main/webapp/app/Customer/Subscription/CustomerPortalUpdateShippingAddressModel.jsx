import React, {Fragment, useEffect, useState} from 'react';
import {
  Col,
  Input,
  Button,
  Label,
  Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import '../../DemoPages/Dashboards/Shared/loader.scss';


const CustomerPortalUpdateShippingAddressModel = (props) => {
  const {
    isUpdating,
    confirmBtnText,
    cancelBtnText,
    toggleShippingModal,
    updateShippingAddressMethod,
    initialShippingAddress,
    shippingLabelData
  } = props;

  const onSubmit = values => {
    updateShippingAddressMethod(values);
  };

  return (
    <div>
            <Form
                initialValues={initialShippingAddress}
                onSubmit={onSubmit}
                render={({ handleSubmit, form, submitting, pristine, values }) => (
                    <form onSubmit={handleSubmit}>
                        <Row>
                            <Col className="mt-3" md={6}>
                                <Label for="firstname">{shippingLabelData?.firstNameLabelText}</Label>
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
                            <Col className="mt-3" md={6}>
                            <Label for="lastName">{shippingLabelData?.lastNameLabelText}</Label>
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
                        <Row>
                            <Col className="mt-3" md={6}>
                                <Label for="phone">{shippingLabelData?.phoneLabelText}</Label>
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
                            <Col className="mt-3" md={6}>
                            <Label for="city">{shippingLabelData?.cityLabelText}</Label>
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
                        <Row>
                            <Col className="mt-3" md={6}>
                                <Label for="address1">{shippingLabelData?.address1LabelText}</Label>
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
                            <Col className="mt-3" md={6}>
                            <Label for="address2">{shippingLabelData?.address2LabelText}</Label>
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
                        <Row>
                        <Col className="mt-3" md={6}>
                                <Label for="company">{shippingLabelData?.companyLabelText ? shippingLabelData?.companyLabelText : "Company"}</Label>
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
                                    // return !value ? 'Please provide province.' : undefined;
                                    // }}
                                    id="company"
                                    className="form-control"
                                    type="text"
                                    name="company"
                                />
                            </Col>
                            <Col className="mt-3" md={6}>
                                <Label for="province">{shippingLabelData?.provinceLabelText}</Label>
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
                                    // return !value ? 'Please provide province.' : undefined;
                                    // }}
                                    id="province"
                                    className="form-control"
                                    type="text"
                                    name="province"
                                />
                            </Col>
                            <Col className="mt-3" md={6}>
                                <Label for="provinceCode">{shippingLabelData?.provinceCodeLabelText ? shippingLabelData?.provinceCodeLabelText : "Province Code" }</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>

                                        </>
                                    )}
                                    id="provinceCode"
                                    className="form-control"
                                    type="text"
                                    name="provinceCode"
                                />
                            </Col>


                        <Col className="mt-3" md={4}>
                            <Label for="zip">{shippingLabelData?.zipLabelText}</Label>
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
                            <Col className="mt-3" md={4}>
                            <Label for="country">{shippingLabelData?.countryLabelText}</Label>
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
                                    // return !value ? 'Please provide country.' : undefined;
                                    // }}
                                    id="country"
                                    className="form-control"
                                    type="text"
                                    name="country"
                                />
                            </Col>
                            <Col className="mt-3" md={4}>
                            <Label for="countryCode">{shippingLabelData?.countryCodeLabelText ? shippingLabelData?.countryCodeLabelText : "Country Code"}</Label>
                                <Field
                                    render={({input, meta}) => (
                                        <>
                                        <Input {...input}
                                                invalid={meta.error && meta.touched ? true : null}/>

                                        </>
                                    )}

                                    id="countryCode"
                                    className="form-control"
                                    type="text"
                                    name="countryCode"
                                />
                            </Col>
                        </Row>

                    <div style={{marginTop: '21px', display: 'flex', justifyContent: 'center'}}>
                        <Button
                        style={{marginRight: '12px'}}
                        size="lg"
                        className="appstle_order-detail_update-button"
                        type="submit"
                        // disabled={submitting}
                        >
                        {isUpdating ?
                            <div className="d-flex align-items-center">
                                <div className="appstle_loadersmall" />
                                <span className="ml-2 font-weight-light"> Please Wait</span>
                            </div>
                            : shippingLabelData?.updateEditShippingButtonText}
                        </Button>
                        <Button
                            size="lg"
                            className="appstle_order-detail_cancel-button"
                            type="button"
                            onClick={toggleShippingModal}
                        >
                        {shippingLabelData?.cancelEditShippingButtonText}
                        </Button>
                    </div>
                    </form>
                )}
                />
          </div>
  );
}

export default CustomerPortalUpdateShippingAddressModel;
