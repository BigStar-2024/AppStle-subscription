import React, { useState, Fragment} from 'react';
import { connect, useSelector } from 'react-redux';
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col,
  FormText
} from 'reactstrap';
import axios from 'axios';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import Axios from 'axios';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";
import { toast } from 'react-toastify';

const AddProductAutomation = props => {
  const [updating, setUpdating] = useState(false);
  const [isSubIdNotValid, setIsSubIdNotValid] = useState(false);

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    const productType = values?.productType;

    axios.put(`/api/bulk-automations/add-product?${subscriptionId ? `contractIds=${values?.subscriptionID}&` :  ''}productType=${productType}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}&variantId=${values?.variantId}${values?.price ? `&price=${values?.price}` :  ''}`)
      .then(async res => {
        await props?.getAutomationStatus()
        toast.success("Bulk update process triggered !!");
        setUpdating(false);
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
          mutators={{
            setVariantId: (args, state, utils) => {
              console.log(args)
              utils.changeValue(state, 'variantId', () => args[0])
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
                      <Label>Product Type</Label>
                      <Field
                        type ="select"
                        render={({ input, meta }) => (
                          <>
                            <Input
                              type ="select"
                              {...input}
                              invalid={meta.error && meta.touched ? true : null} >
                              <option value="" selected>Select action</option>
                              <option value="ONE_TIME_PRODUCT">One Time Product</option>
                              <option value="FREE_PRODUCT">Free Product</option>
                              <option value="SUBSCRIPTION_PRODUCT">Subscription Product</option>
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
                          return !value ? 'Please select an action.' : undefined;
                        }}
                        id="productType"
                        name="productType"
                      />
                    </FormGroup>
                  </Col>
                  <Col md={6}>
                  {
                    (values?.productType === "ONE_TIME_PRODUCT" || values?.productType === "SUBSCRIPTION_PRODUCT") &&  <FormGroup style={{marginBottom: '3px'}}>
                        <Label for="price">Price for product</Label>
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
                          id="price"
                          className="form-control"
                          type="number"
                          name="price"
                        />
                        <FormText color="muted">
                          If price not entered then variant's current price will be fetched from shop.
                        </FormText>
                      </FormGroup>
                  }
                  </Col>
                  </Row>
                  <Row>
                  <Col md={4}>
                  <FormGroup style={{marginBottom: '3px'}}>
                    <Label for="newVariantId">Variant Id</Label>
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
                      id="variantId"
                      className="form-control"
                      type="text"
                      name="variantId"
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
                  <Col md={5} style={{marginTop: '2rem'}}>
                    <MySaveButton
                      text="Start Automation"
                      updatingText={'Processing'}
                      updating ={updating}
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
    </Fragment>
  );
};

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(AddProductAutomation);
