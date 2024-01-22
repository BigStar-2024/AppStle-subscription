import React, { useState, Fragment } from 'react';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText
} from 'reactstrap';
import axios from 'axios';
import { Field, Form } from 'react-final-form';
import { toast } from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';

const UpdateDeliveryPriceAutomation = props => {
  const [updating, setUpdating] = useState(false);

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    axios.put(`/api/bulk-automations/delivery-price?deliveryPrice=${values?.deliveryPrice}${subscriptionId ? `&contractIds=${values?.subscriptionID}` :  ''}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
      .then(async res => {
        await props?.getAutomationStatus()
        toast.success("Bulk update process triggered !!");
        setUpdating(false)
      })
      .catch(error => {
          console.log(error)
          toast.error(error?.response?.data?.message || 'Something went wrong.');
          setUpdating(false)
        }
      )
  }

  return (
    <Fragment>
      <Form
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
          return (
            <form onSubmit={handleSubmit}>
              <Row>
                <Field
                  type="select"
                  id={`subscriptionsType`}
                  name={`subscriptionsType`}
                  initialValue={`SUBSCRIPTION_ID`}
                  render={({input, meta}) => {
                    return <Col md={6}> <FormGroup>
                      <Label for={`subscriptionsType`}><b>Select Subscriptions Type</b></Label>
                      <Input
                        invalid={meta.error && meta.touched ? true : null}
                        {...input}
                        style={{flexGrow: 1}}

                      >
                        <option value="SUBSCRIPTION_ID">Subscription ID</option>
                        <option value="ALL_SUBSCRIPTIONS">All Subscriptions</option>
                      </Input>
                    </FormGroup>
                    </Col>
                  }}
                  className="form-control"
                />
                <Col md={6}>
                  {
                    values?.subscriptionsType === "SUBSCRIPTION_ID" && <FormGroup style={{marginBottom: '3px'}}>
                      <Label for="subscriptionID">Comma-Separated Subscription Ids</Label>
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
                          return !value ? 'Please provide subscription Id.' : undefined;
                        }}
                        id="subscriptionID"
                        className="form-control"
                        type="text"
                        name="subscriptionID"
                        placeholder="Enter subscription Id"
                      />
                      <FormText color="muted">
                        Please enter comma-separated subscription IDs. eg. 111111, 222222, 333333, ...
                      </FormText>
                    </FormGroup>
                  }
                </Col>
              </Row>
              <Row>
                <Col md={6}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="deliveryPrice">Delivery Price</Label>
                    <Field
                      render={({ input, meta }) => (
                        <>
                          <Input
                            placeholder="eg. 10.00"
                            {...input}
                            invalid={meta.error && meta.touched ? true : null} />
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
                        return !value ? 'Please provide delivery price.' : undefined;
                      }}
                      id="deliveryPrice"
                      className="form-control"
                      type="number"
                      name="deliveryPrice"
                    />
                    {/* <FormText color="muted">
                                    </FormText> */}
                  </FormGroup>
                </Col>
                <Col md={6} style={{marginTop: '2rem'}}>
                  <MySaveButton
                    text="Start Automation"
                    updatingText={'Processing'}
                    updating ={updating}
                    className="btn-primary"
                  >
                    Start Automation
                  </MySaveButton>
                </Col>
              </Row>
            </form>)
        }}
      />
    </Fragment>
  );
};

export default UpdateDeliveryPriceAutomation;
