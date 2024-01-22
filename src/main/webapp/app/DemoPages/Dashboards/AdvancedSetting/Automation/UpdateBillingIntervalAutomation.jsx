import React, {useState, Fragment} from 'react';
import {useSelector} from 'react-redux';
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

const UpdateBillingIntervalAutomation = props => {
  const [updating, setUpdating] = useState(false);
  const accountInfo = useSelector(state => state.authentication.account)

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    swal({
      title: "Warning",
      text: "This action will override anchor day",
      icon: "warning",
      buttons: [("Cancel"), ("Confirm")],
      dangerMode: true
    }).then(value => {
      if (value) {
        axios.put(`/api/bulk-automations/billing-interval?${subscriptionId ? `contractIds=${values?.subscriptionID}&` :  ''}allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}&intervalCount=${values?.intervalCount}&interval=${values?.interval}`)
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
                <Col md={7}>
                  <Field
                    type="select"
                    id={`subscriptionsType`}
                    name={`subscriptionsType`}
                    initialValue={`SUBSCRIPTION_ID`}
                    render={({input, meta}) => {
                      return <FormGroup>
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
                    }}
                    className="form-control"
                  />
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
                <Col md={3}>
                  <FormGroup>
                    <Label>Order Frequency</Label>
                    <div style={{display: 'flex'}}>
                      <Field
                        render={({input, meta}) => (
                          <Fragment>
                            <Input {...input} invalid={meta.error && meta.touched ? true : null}/>
                          </Fragment>
                        )}
                        validate={value => {
                          if (!value) {
                            return 'required.';
                          } else {
                            if (value <= 0) {
                              return 'required.';
                            } else {
                              return undefined;
                            }
                          }
                        }}
                        type="number"
                        id="intervalCount"
                        className="mr-2"
                        style={{width: '100px'}}
                        name="intervalCount"
                      />
                      <Field
                        type="select"
                        id="interval"
                        name="interval"
                        initialValue={"DAY"}
                        render={({input, meta}) => (
                          <Input
                            invalid={meta.error && meta.touched ? true : null}
                            style={{width: '100px', marginLeft: "1rem"}}
                            {...input}
                          >
                            <option value="DAY" selected>
                              Days
                            </option>
                            <option value="WEEK">Week</option>
                            <option value="MONTH">Month</option>
                            <option value="YEAR">Year</option>
                          </Input>
                        )}
                        className="form-control"
                      />
                    </div>

                  </FormGroup>
                </Col>
                <Col md={2} style={{marginTop: '2rem'}}>
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

export default UpdateBillingIntervalAutomation;
