import React, {useState, Fragment} from 'react';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col,
  FormText
} from 'reactstrap';
import axios from 'axios';
import {Field, Form} from 'react-final-form';
import {toast} from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import swal from 'sweetalert';

const UpdateNextBillingDateTimeAutomation = props => {
  const [updating, setUpdating] = useState(false);

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    swal({
      title: 'Are you sure?',
      text: "This action will override anchor day",
      icon: 'warning',
      buttons: true,
      dangerMode: true
    }).then(value => {
      if (value) {
        axios.put(`/api/bulk-automations/update-next-billing-date-time?hour=${values?.hour}&minute=${values?.minute}&zonedOffsetHours=${values?.zonedOffsetHours}${subscriptionId ? `&contractIds=${values?.subscriptionID}` : ''}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
        .then(async res => {
          await props?.getAutomationStatus()
          toast.success("Bulk update process triggered !!");
          setUpdating(false)
        }).catch(error => {
            console.log(error)
            toast.error(error?.response?.data?.message || 'Something went wrong.');
            setUpdating(false)
          }
        )
      }
    })
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
                <Col md={3}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="hour">Next Billing Time (Hour)</Label>
                    <Field
                      render={({input, meta}) => (
                        <>
                          <Input
                            max={24}
                            min={0}
                            placeholder="eg. 5"
                            {...input}
                            invalid={meta.error && (meta.touched || meta.active) ? true : null}/>
                          {meta.error && (
                            <div
                              style={{
                                order: '4',
                                width: '100%',
                                display: meta.error && (meta.touched || meta.active) ? 'block' : 'none'
                              }}
                              className="invalid-feedback"
                            >
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      validate={value => {
                        return !value ? 'Please provide proper hour.' : (value > 25 || value < 0 ? "Hour is between 0 to 24" : undefined);
                      }}
                      id="hour"
                      className="form-control"
                      type="number"
                      name="hour"
                    />
                  </FormGroup>
                </Col>
                <Col md={3}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="minute">Next Billing Time (Minute)</Label>
                    <Field
                      render={({input, meta}) => (
                        <>
                          <Input
                            max={59}
                            min={0}
                            placeholder="eg. 30"
                            {...input}
                            invalid={meta.error && (meta.touched || meta.active) ? true : null}/>
                          {meta.error && (
                            <div
                              style={{
                                order: '4',
                                width: '100%',
                                display: meta.error && (meta.touched || meta.active) ? 'block' : 'none'
                              }}
                              className="invalid-feedback"
                            >
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      validate={value => {
                        return !value ? 'Please provide proper minute.' : (value > 60 || value < 0 ? "Minute is between 0 to 59" : undefined);
                      }}
                      id="minute"
                      className="form-control"
                      type="number"
                      name="minute"
                    />
                  </FormGroup>
                </Col>
                <Col md={3}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="zonedOffsetHours">Zoned Offset Hours</Label>
                    <Field
                      render={({input, meta}) => (
                        <>
                          <Input
                            placeholder="eg. -1"
                            {...input}
                            invalid={meta.error && (meta.touched || meta.active) ? true : null}/>
                          {meta.error && (
                            <div
                              style={{
                                order: '4',
                                width: '100%',
                                display: meta.error && (meta.touched || meta.active) ? 'block' : 'none'
                              }}
                              className="invalid-feedback"
                            >
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      validate={value => {
                        return !value ? 'Please provide zoned Offset Hours.' : undefined;
                      }}
                      id="zonedOffsetHours"
                      className="form-control"
                      type="text"
                      name="zonedOffsetHours"
                    />
                    <FormText color="muted">
                      eg. For USA (GMT-4), United Kingdom (GMT+1)
                    </FormText>
                  </FormGroup>
                </Col>
                <Col md={3} style={{marginTop: '2rem'}}>
                  <MySaveButton
                    text="Start Automation"
                    updatingText={'Processing'}
                    updating={updating}
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

export default UpdateNextBillingDateTimeAutomation;
