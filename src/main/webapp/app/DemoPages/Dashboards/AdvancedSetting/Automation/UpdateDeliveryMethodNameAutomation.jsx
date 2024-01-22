import React, {useState, Fragment} from 'react';
import {connect} from 'react-redux';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText
} from 'reactstrap';
import axios from "axios";
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {toast} from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';


const UpdateDeliveryMethodNameAutomation = (props) => {

  const [updating, setUpdating] = useState(false);
  const [formValues, setFormValues] = useState(null);


  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    const encodedDeliveryMethodName = encodeURIComponent(values?.deliveryMethodName);
    axios.put(`/api/bulk-automations/delivery-method?deliveryMethodName=${encodedDeliveryMethodName}${subscriptionId ? `&contractIds=${values?.subscriptionID}` :  ''}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
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
        {
          <Form
            onSubmit={onSubmit}
            initialValues={formValues}
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
                    <Col md={8}>
                      <FormGroup style={{marginBottom: '3px'}}>
                        <Label for="deliveryMethodName">Delivery Method Name</Label>
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
                            return !value ? 'Please provide delivery method name.' : undefined;
                          }}
                          id="deliveryMethodName"
                          className="form-control"
                          type="text"
                          name="deliveryMethodName"
                          placeholder="Enter delivery Method Name"
                        />
                      </FormGroup>
                    </Col>

                    <Col md={4}>
                      <div style={{marginTop: '34px'}}>
                        <MySaveButton
                          text="Start Delivery Method Name Automation"
                          updating={updating}
                          updatingText={'Processing'}
                          className="btn-danger"

                        >
                          Start Delivery Method Name Automation
                        </MySaveButton>
                      </div>
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

export default connect(mapStateToProps, mapDispatchToProps)(UpdateDeliveryMethodNameAutomation);
