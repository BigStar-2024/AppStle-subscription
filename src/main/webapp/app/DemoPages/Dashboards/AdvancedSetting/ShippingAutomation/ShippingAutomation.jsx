import React, {useState, Fragment} from 'react';
import {connect} from 'react-redux';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import axios from 'axios';
import {toast} from 'react-toastify';


const ShippingAutomation = (props) => {
  const [updating, setUpdating] = useState(false);
  const [isSubIdNotValid, setIsSubIdNotValid] = useState(false);

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID";
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS";

    axios.put(`/api/bulk-automations/update-delivery-method-type?${subscriptionId ? `contractIds=${values?.subscriptionID}&` : ''}allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}&fromDeliveryType=${values?.fromDeliveryType}&toDeliveryType=${values?.toDeliveryType}`)
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
     <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}>
        {
          <Form
            onSubmit={onSubmit}
            initialValues={null}
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
                              setIsSubIdNotValid(!value);
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
                    <FormGroup>
                      <Label>From Shipping Type</Label>
                      <Field
                        type ="select"
                        render={({ input, meta }) => (
                          <>
                            <Input
                              type ="select"
                              {...input}
                              invalid={meta.error && meta.touched ? true : null} >
                              <option value="" selected>Select action</option>
                              <option value="SHIPPING">Standard Shipping</option>
                              <option value="LOCAL">Local Delivery</option>
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
                        validate={value => {
                          return !value ? 'Please select a shipping type.' : undefined;
                        }}
                        id="fromDeliveryType"
                        name="fromDeliveryType"
                      />
                    </FormGroup>
                  </Col>
                  <Col md={6}>
                    <FormGroup>
                      <Label>To Shipping Type</Label>
                      <Field
                        type ="select"
                        render={({ input, meta }) => (
                          <>
                            <Input
                              type ="select"
                              {...input}
                              invalid={meta.error && meta.touched ? true : null} >
                              <option value="" selected>Select action</option>
                              <option value="SHIPPING">Standard Shipping</option>
                              <option value="LOCAL">Local Delivery</option>
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
                        validate={value => {
                          return !value ? 'Please select a shipping type.' : undefined;
                        }}
                        id="toDeliveryType"
                        name="toDeliveryType"
                      />
                    </FormGroup>
                  </Col>
                  </Row>
                  <Row>
                      <Col md={5} style={{marginTop: '2rem'}}>
                      <MySaveButton
                          text="Start Automation"
                          updating={updating}
                          updatingText={'Processing'}
                          className="btn-primary"
                          disabled = {(values?.subscriptionsType === "SUBSCRIPTION_ID" && isSubIdNotValid)}
                        >
                          Start Automation
                        </MySaveButton>
                      </Col>
                  </Row>

                </form>)
            }}
          />
        }
      </ReactCSSTransitionGroup>
    </Fragment>
  )
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(ShippingAutomation);