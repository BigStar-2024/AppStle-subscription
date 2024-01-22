import React, {useState, Fragment} from 'react';
import {connect} from 'react-redux';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText, Alert
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import axios from 'axios';
import {toast} from 'react-toastify';
import AddProductModal from "../../Subscriptions/AddProductModal";
import PrdVariantCheckBoxPopup from "../PrdVariantCheckBoxPopup";


const ProductReplaceAutomation = (props) => {
  const [updating, setUpdating] = useState(false);

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"
    axios.put(`/api/bulk-automations/replace-product?oldVariantIds=${values?.variantId1}&newVariantIds=${values?.variantId2}${subscriptionId ? `&contractIds=${values?.subscriptionID}` :  ''}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
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
            mutators={{
              setVariantId1: (args, state, utils) => {
                utils.changeValue(state, 'variantId1', () => args[0])
              },
              setVariantId2: (args, state, utils) => {
                utils.changeValue(state, 'variantId2', () => args[0])
              }
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
                    <Alert color="info">
                      By Replacing product with bulk operation, any applied discount code that might get invalidated by removal of old product, will be removed.
                    </Alert>
                  </Row>
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
                  <Row className="">
                    <Col md={6}>
                      <FormGroup style={{marginBottom: '3px'}}>
                        <Label for="variantId1">Variant Id for Existing Product in Subscription</Label>
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
                            return !value ? 'Please provide variant Id 1.' : undefined;
                          }}
                          id="variantId1"
                          className="form-control"
                          type="text"
                          name="variantId1"
                          placeholder="Enter Product Id / Variant Id 1"
                        />
                      </FormGroup>
                      <FormGroup>
                        <Field
                          name={`variantIdFromModel1`}
                          index={1}
                          methodName="Save"
                          header="Product"
                          render={({input, meta}) => (
                            <PrdVariantCheckBoxPopup
                              value={input.value}
                              onChange={(selectData) => {
                                input.onChange();
                                form.mutators.setVariantId1((selectData.map(item => item?.id))?.join(","))
                              }}
                              isSource={false}
                              totalTitle="select product variant 1"
                              index={1}
                              methodName="Save"
                              buttonLabel="select product variant 1"
                              header="Product Variants"
                            />
                          )}
                        />
                      </FormGroup>

                    </Col>
                    <Col md={6}>
                      <FormGroup style={{marginBottom: '3px'}}>
                        <Label for="variantId2">Variant Id for New Product</Label>
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
                            return !value ? 'Please provide variant Id 2.' : undefined;
                          }}
                          id="variantId2"
                          className="form-control"
                          type="text"
                          name="variantId2"
                          placeholder="Enter Product Id / Variant Id 2"
                        />
                      </FormGroup>
                      <FormGroup>
                        <Field
                          name={`variantIdFromModel2`}
                          index={1}
                          methodName="Save"
                          header="Product"
                          render={({input, meta}) => (
                            <PrdVariantCheckBoxPopup
                              value={input.value}
                              onChange={(selectData) => {
                                input.onChange();
                                form.mutators.setVariantId2((selectData.map(item => item?.id))?.join(","))
                              }}
                              isSource={false}
                              totalTitle="select product variant 2"
                              index={1}
                              methodName="Save"
                              buttonLabel="select product variant 2"
                              header="Product Variants"
                            />
                          )}
                        />
                      </FormGroup>
                    </Col>
                  </Row>
                  <MySaveButton
                    text="Start Product Replace Automation"
                    updating={updating}
                    updatingText={'Processing'}
                    className="btn-danger mb-2"
                  >
                    Start Product Replace Automation
                  </MySaveButton>

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

export default connect(mapStateToProps, mapDispatchToProps)(ProductReplaceAutomation);
