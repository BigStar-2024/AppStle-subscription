import React, {useState, Fragment} from 'react';
import {useSelector} from 'react-redux';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText
} from 'reactstrap';
import axios from 'axios';
import {Field, Form} from 'react-final-form';
import {toast} from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";


const ReplaceRemovedVariantAutomation = props => {
  const [updating, setUpdating] = useState(false);
  const accountInfo = useSelector(state => state.authentication.account)

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    axios.put(`/api/bulk-automations/replace-removed-variants?newVariantId=${values?.newVariantId}&${values?.title && `&title=${encodeURIComponent(values?.title)}`}${subscriptionId ? `&contractIds=${values?.subscriptionID}` :  ''}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
    .then(async res => {
      await props?.getAutomationStatus()
      toast.success("Bulk update process triggered !!");
      setUpdating(false)})
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
        mutators={{
          setVariantId: (args, state, utils) => {
            console.log(args)
            utils.changeValue(state, 'newVariantId', () => args[0])
          },
        }}
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
                    <Label for="newVariantId">Removed Variant title</Label>
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
                      id="title"
                      className="form-control"
                      type="text"
                      name="title"
                      placeholder="Enter title"
                    />
                  </FormGroup>
                </Col>
                <Col md={4}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="newVariantId">New Variant Id</Label>
                    <Field
                      render={({input, meta}) => (
                        <>
                          <Input
                            {...input}
                            placeholder="eg. 111111"
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
                        return !value ? 'Please provide variant Id.' : undefined;
                      }}
                      id="newVariantId"
                      className="form-control"
                      type="text"
                      name="newVariantId"
                    />
                  </FormGroup>
                  <FormGroup>
                    <Field
                      name={`variantIdFromModel`}
                      index={1}
                      methodName="Save"
                      header="Product"
                      render={({input, meta}) => (
                        <PrdVariantRadioPopup
                          value={input.value}
                          onChange={(selectData) => {
                            input.onChange();
                            form.mutators.setVariantId(selectData.id)
                          }}
                          isSource={false}
                          totalTitle="select product variant"
                          index={1}
                          methodName="Save"
                          buttonLabel="select product variant"
                          header="Product Variants"
                        />
                      )}
                    />
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

export default ReplaceRemovedVariantAutomation;
