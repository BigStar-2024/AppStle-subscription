import React, { Fragment, useEffect, useState } from 'react';
import Loader from 'react-loaders';
import {
  Card,
  CardBody,
  Col,
  Collapse,
  FormGroup,
  Input,
  Button,
  InputGroup,
  InputGroupText,
  InputGroupAddon,
  Label,
  Row,
  CardHeader,
} from 'reactstrap';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { CalendarToday, ChevronRight, Help, MailOutline } from '@mui/icons-material';
import {
  createEntity,
  getEntity,
  reset,
  updateEntity,
  getUsedProducts,
  updateStateEntity,
  getSellingPlans as getAllSellingPlansList,
} from 'app/entities/subscription-group/subscription-group.reducer';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import { OnChange } from 'react-final-form-listeners';
import _ from 'lodash';
import CustomHtmlToolTip from '../SubscriptionGroups/CustomHtmlToolTip';
import AddProductModal from './AddProductModal';
import DatePickerWithTimezone from '../Shared/DatePickerWithTimezone';
import { addDays } from 'date-fns';
import './datepicker.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHashtag, faMoneyBill } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import { toast } from 'react-toastify';
import SearchCustomerModal from 'app/DemoPages/Dashboards/Subscriptions/SearchCustomerModal';
import { customerDetailsReset } from 'app/entities/customers/customer.reducer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import { TipParagraph } from '../SubscriptionGroups/CreateSubscriptionGroup';

const CreateSubscription = props => {
  const { subscriptionGroupEntity, loading, updating, usedProductIds, getAllSellingPlansList, sellingPlanData } = props;
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [createSubscriptionInProgress, setCreateSubscriptionInProgress] = useState(false);
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [currencies, setCurrencies] = useState([]);

  const [loadingProductPrice, setLoadingProductPrice] = useState(false);

  const [sellingPlansList, setSellingPlansList] = useState([]);

  useEffect(() => {
    getAllSellingPlansList();
  }, []);

  useEffect(() => {
    if (sellingPlanData && sellingPlanData?.length > 0) {
      setSellingPlansList(sellingPlanData?.map(v => ({ label: v?.frequencyName + ' - (' + v?.groupName + ')', value: v?.id })));
    }
  }, [sellingPlanData]);

  const handleClose = () => {
    props.history.push('/dashboards/subscriptions');
  };

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER,
  };

  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  useEffect(() => {
    axios
      .get('/api/data/currencies')
      .then(res => {
        setCurrencies(res.data);
      })
      .catch(err => {
        setCurrencies([]);
      });
  }, []);

  function getTimeZoneDate(nextBillingDate) {
    try {
      const localDateTime = new Date();

      const newYorkDateTime = new Date(localDateTime.toLocaleString('en-US', { timeZone: props?.shopInfo?.ianaTimeZone }));
      const timeDiff = localDateTime - newYorkDateTime;

      // Add a time difference in milliseconds to the nextBillingDate
      const updatedNextBillingDate = new Date(nextBillingDate.getTime() + timeDiff);

      // Now, you can log the information if needed
      // console.log("Local DateTime: ", localDateTime, localDateTime.toUTCString());
      // console.log("New York DateTime: ", newYorkDateTime, newYorkDateTime.toUTCString());
      // console.log("Time Difference in Hours: ", timeDiff / (1000 * 60 * 60));
      // console.log("Updated Next Billing Date: ", updatedNextBillingDate, updatedNextBillingDate.toUTCString());

      return updatedNextBillingDate;
    } catch (error) {
      return nextBillingDate;
    }
  }

  const saveEntity = values => {
    setCreateSubscriptionInProgress(true);
    let entity = values;
    if (values?.nextBillingDate) {
      entity = { ...entity, nextBillingDate: getTimeZoneDate(values?.nextBillingDate) };
    }

    axios
      .post('/api/subscription-contract-details/create-subscription-contract', entity)
      .then(res => {
        setCreateSubscriptionInProgress(false);
        toast.success('Subscription contract created', options);
        props.reset();
        props.customerDetailsReset();
        props.history.push(`/dashboards/subscription/${res.data?.id?.split('/').pop()}/detail`);
      })
      .catch(err => {
        setCreateSubscriptionInProgress(false);
        toast.error(err?.response?.data?.message, options);
      });
  };

  const getVariantPrice = (variants, setValue, values) => {
    let cc = values?.currencyCode ? values.currencyCode : props?.shopInfo?.currency;
    let variantIdList = variants?.map(item => item?.variantId);
    let selectedSellingPlan = null;

    if (values?.sellingPlan) {
      selectedSellingPlan = sellingPlanData?.find(item => item?.id == values?.sellingPlan);
    }

    if (variantIdList && variantIdList.length > 0) {
      let url = `/api/data/get-variant-detail-by-ids?variantIds=${variantIdList.join()}`;

      axios.get(url).then(res => {
        if (res?.data?.length > 0) {
          let list = variants?.map(item => {
            let presentment_prices = res?.data?.find(v => v.id === item.variantId)?.presentment_prices;

            if (presentment_prices && presentment_prices?.length > 0) {
              let priceList = presentment_prices?.map(prices => prices?.price);
              let currencyPrice = priceList?.find(p => p?.currency_code == cc)?.price || item?.unitPrice;

              return { ...item, prices: priceList, unitPrice: currencyPrice, sellingPlanId: values?.sellingPlan || null };
            } else {
              return { ...item };
            }
          });

          setValue('lines', [...list]);
        }
        setLoadingProductPrice(false);
      });
    }
  };

  const checkProducts = productsString => {
    let validation = {
      isValid: true,
      invalidProducts: [],
      message: '',
    };
    try {
      let products = JSON.parse(productsString);
      validation.isValid = products.length <= 250;
      validation.message =
        "Shopify doesn't allow more than 240 products in a subscription plan. Please remove " + (products.length - 250) + ' products.';
    } catch (error) {}
    return validation;
  };

  let submit;
  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading={'Create Subscription Manually'}
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/8337592-manually-create-a-subscription' target='blank'> Click here to know more about manually creating a subscription.</a>"
          icon="pe-7s-network icon-gradient bg-mean-fruit"
          enablePageTitleAction
          actionTitle="Save"
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            props.history.push(`/dashboards/subscriptions`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={createSubscriptionInProgress}
          sticky={true}
          tutorialButton={{
            show: true,
            videos: [
              {
                title: 'Create Subscription',
                url: 'https://www.youtube.com/watch?v=4ViJlPmWSSA',
              },
              {
                title: 'Subscriptions and Filtering',
                url: 'https://www.youtube.com/watch?v=QDEP-0jLWek',
              },
              {
                title: 'Export Subscription Data',
                url: 'https://www.youtube.com/watch?v=AvohNGVCX6E',
              },
              {
                title: 'Shopify POS Workarounds',
                url: 'https://youtu.be/XmbcrNkHEf8',
              },
            ],
            docs: [
              {
                title: 'Manually Create a Subscription',
                url: 'https://intercom.help/appstle/en/articles/8337592-manually-create-a-subscription',
              },
              {
                title: 'Subscription Management',
                url: 'https://intercom.help/appstle/en/collections/2776380-subscription-management',
              },
            ],
          }}
        />
        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <div>
            <CardBody>
              <FeatureAccessCheck hasAnyAuthorities={'accessManualSubscriptionCreation'} upgradeButtonText="Upgrade your plan to">
                <Form
                  mutators={{
                    ...arrayMutators,
                    setProductIds: (args, state, utils) => {
                      utils.changeValue(state, 'productIds', () => JSON.stringify(args[0]));
                    },
                    setVariantIds: (args, state, utils) => {
                      utils.changeValue(state, 'variantIds', () => JSON.stringify(args[0]));
                    },
                    setValue: ([field, value], state, { changeValue }) => {
                      changeValue(state, field, () => value);
                    },
                  }}
                  initialValues={subscriptionGroupEntity}
                  onSubmit={saveEntity}
                  render={({
                    handleSubmit,
                    form: {
                      mutators: { push, pop, update, remove },
                    },
                    form,
                    submitting,
                    pristine,
                    values,
                    errors,
                    valid,
                  }) => {
                    submit = () => {
                      let productValidation = checkProducts(values.productIds);
                      let productError = {};
                      if (!productValidation.isValid) {
                        productError = { productIds: productValidation.message };
                      }
                      let allErrors = _.extend(errors, productError);
                      if (Object.keys(errors).length === 0 && errors.constructor === Object) {
                        handleSubmit();
                      } else {
                        if (Object.keys(errors).length) handleSubmit();
                        setFormErrors(errors);
                        setErrorsVisibilityToggle(!errorsVisibilityToggle);
                      }
                    };
                    useEffect(() => {
                      if (
                        props.customerPaymentDetails &&
                        props.customerPaymentDetails?.edges?.length > 0 &&
                        values?.paymentMethodId !== undefined &&
                        values?.paymentMethodId
                      ) {
                        for (let i = 0; i <= props.customerPaymentDetails?.edges?.length; i++) {
                          const paymentDetails = props.customerPaymentDetails?.edges[i];
                          if (values?.paymentMethodId !== null && values?.paymentMethodId === paymentDetails?.node?.id.split('/').pop()) {
                            props.updateStateEntity({
                              ...values,
                              deliveryCity: paymentDetails?.node?.instrument?.billingAddress?.city,
                              deliveryAddress1: paymentDetails?.node?.instrument?.billingAddress?.address1,
                              deliveryAddress2: paymentDetails?.node?.instrument?.billingAddress?.address2,
                              province: paymentDetails?.node?.instrument?.billingAddress?.province,
                              deliveryProvinceCode: paymentDetails?.node?.instrument?.billingAddress?.provinceCode,
                              deliveryZip: paymentDetails?.node?.instrument?.billingAddress?.zip,
                              country: paymentDetails?.node?.instrument?.billingAddress?.country,
                              deliveryCountryCode: paymentDetails?.node?.instrument?.billingAddress?.countryCode,
                            });
                            break;
                          }
                        }
                      }
                    }, [values?.paymentMethodId, props.customerPaymentDetails]);

                    return (
                      <>
                        <form onSubmit={handleSubmit}>
                          <Card>
                            <CardBody>
                              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h5 style={{ margin: '0' }}>
                                  <b>Selling Plan</b>
                                </h5>
                              </div>
                              <hr />
                              <div style={{ display: 'flex', justifyContent: 'space-between', gap: '10px' }}>
                                <FormGroup className="w-100">
                                  <Label for={`planType`} style={{ alignSelf: 'flex-end' }}>
                                    Selling Plan
                                  </Label>
                                  <Field
                                    type="select"
                                    id={`sellingPlan`}
                                    name={`sellingPlan`}
                                    render={({ input, meta }) => (
                                      <Input
                                        onChange={value => {
                                          input.change(value);
                                        }}
                                        options={sellingPlansList}
                                        invalid={meta.error && meta.touched ? true : null}
                                        {...input}
                                        style={{ flexGrow: 1 }}
                                      >
                                        <option value={''}>Select Selling Plan</option>
                                        {sellingPlansList?.map(v => (
                                          <option value={v?.value}>{v?.label}</option>
                                        ))}
                                      </Input>
                                    )}
                                    className="form-control"
                                  />
                                  <OnChange name={`sellingPlan`}>
                                    {(value, previous) => {
                                      let sellingPlan = sellingPlanData?.find(v => v.id === value);
                                      if (sellingPlan) {
                                        console.log(sellingPlan);
                                        form.change('planType', sellingPlan?.planType);
                                        form.change('billingIntervalCount', sellingPlan?.billingFrequencyCount);
                                        form.change('billingIntervalType', sellingPlan?.billingFrequencyInterval);

                                        form.change('deliveryIntervalCount', sellingPlan?.frequencyCount);
                                        form.change('deliveryIntervalType', sellingPlan?.frequencyInterval);

                                        form.change('minCycles', sellingPlan?.minCycles);
                                        form.change('maxCycles', sellingPlan?.maxCycles);

                                        form.change('frequencyName', sellingPlan?.frequencyName);
                                        form.change('frequencyDescription', sellingPlan?.frequencyDescription);
                                      } else {
                                        form.change('planType', 'PAY_AS_YOU_GO');
                                        form.change('billingIntervalCount', '');
                                        form.change('billingIntervalType', 'DAY');

                                        form.change('deliveryIntervalCount', '');
                                        form.change('deliveryIntervalType', 'DAY');

                                        form.change('minCycles', '');
                                        form.change('maxCycles', '');

                                        form.change('frequencyName', '');
                                        form.change('frequencyDescription', '');
                                      }
                                    }}
                                  </OnChange>
                                </FormGroup>
                                <FormGroup className="w-100">
                                  <Label for={`planType`} style={{ alignSelf: 'flex-end' }}>
                                    Plan Type:
                                  </Label>
                                  <CustomHtmlToolTip
                                    interactive
                                    placement="right"
                                    arrow
                                    enterDelay={0}
                                    title={
                                      <div style={{ padding: '8px' }}>
                                        <div style={{ textAlign: 'center' }}>
                                          <b>We support 3 kind of plans</b>
                                        </div>
                                        <hr style={{ borderColor: '#fff' }} />
                                        <b>Pay As You Go:</b> This plan type charges the customer just for the 'immediately next' recurring
                                        order or delivery.
                                        <br />
                                        <br />
                                        <b>Prepaid One-time Plan:</b> This plan type allows you to receive the payment for multiple periods
                                        of future orders at the same time.
                                        <br />
                                        <br />
                                        <b>Please note</b> that in ‘Prepaid One-time Plan’, the contract pauses at the end of the billing
                                        cycle, and needs to be manually renewed by the customer.
                                        <br />
                                        <br />
                                        <b>Prepaid Auto-renew:</b> This plan is very similar to the Prepaid One-time Plan, except that the
                                        contract does not pause at the end of the billing cycle, and auto renews, until the customer
                                        expressly cancels the subscription.
                                      </div>
                                    }
                                  >
                                    <Help style={{ fontSize: '1rem' }} />
                                  </CustomHtmlToolTip>

                                  <Field
                                    type="select"
                                    id={`planType`}
                                    name={`planType`}
                                    render={({ input, meta }) => (
                                      <Input
                                        invalid={meta.error && meta.touched ? true : null}
                                        {...input}
                                        style={{
                                          flexGrow: 1,
                                        }}
                                      >
                                        <option value="PAY_AS_YOU_GO">Pay As You Go (Auto-renew)</option>
                                        <option value="PREPAID">Prepaid One-Time</option>
                                        <option value="ADVANCED_PREPAID">Prepaid Auto-renew</option>
                                      </Input>
                                    )}
                                    initialValue="PAY_AS_YOU_GO"
                                    className="form-control"
                                  />
                                  <OnChange name={`planType`}>
                                    {(value, previous) => {
                                      if (value === 'PREPAID') {
                                        form.mutators.setValue('maxCycles', 1);
                                        form.mutators.setValue('minCycles', null);
                                      }
                                    }}
                                  </OnChange>
                                </FormGroup>
                                <FormGroup style={{ width: '48%' }}>
                                  <Label for={`nextBillingDate`}>Next Order Date</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <InputGroup style={{ flexGrow: 1 }}>
                                          <InputGroupAddon addonType="prepend">
                                            <InputGroupText>
                                              <CalendarToday />
                                            </InputGroupText>
                                          </InputGroupAddon>
                                          <DatePickerWithTimezone
                                            selected={input?.value}
                                            onChange={date => input.onChange(date)}
                                            timeInputLabel="Time:"
                                            minDate={addDays(new Date(), 1)}
                                            dateFormat="MM/dd/yyyy h:mm aa"
                                            timezone={props?.shopInfo.ianaTimeZone}
                                            showTimeInput
                                            {...input}
                                          />
                                        </InputGroup>
                                        {meta.error && (
                                          <div
                                            style={{
                                              order: '4',
                                              width: '100%',
                                              display: meta.error && meta.touched ? 'block' : 'none',
                                            }}
                                            className="invalid-feedback"
                                          >
                                            {meta.error}
                                          </div>
                                        )}
                                      </>
                                    )}
                                    type="string"
                                    id={`nextBillingDate`}
                                    className="mr-2"
                                    name={`nextBillingDate`}
                                    validate={(value, allValues, meta) => {
                                      if (!value) {
                                        return 'Please provide a valid value for the Next Order Date.';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </FormGroup>
                              </div>

                              <FormGroup>
                                <Label className="mt-0 mb-0" for={`billingIntervalCount`} style={{ alignSelf: 'flex-end' }}>
                                  {values?.['planType'] != 'PAY_AS_YOU_GO' ? 'Billing frequency' : 'Billing frequency'}
                                </Label>
                                <Help
                                  style={{ fontSize: '1rem' }}
                                  data-tip={
                                    values?.['planType'] != 'PAY_AS_YOU_GO'
                                      ? "<div style='max-width:500px'>The time gap between two fulfillments within the subscription prepaid order. The number should be a minimum of 1, and the timeline can be days/weeks/months/years. For instance, if you have mentioned fulfillment frequency as 1 month[and billing frequency is 12 months], the product/service needs to be fulfilled every month till the end billing frequency that is 12 months.</div>"
                                      : "<div style='max-width:500px'>The time gap between two order deliveries within the subscription plan. The number should be a minimum of 1, and the timeline can be days/weeks/months/years. For instance, if we have a subscription plan setup for 1 week and the first order is placed today, the next order will be automatically placed exactly after a week. The cycle will continue every week.</div>"
                                  }
                                />
                                <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <InputGroup style={{ flexGrow: 1, width: '48%', marginRight: '4%' }}>
                                          <InputGroupAddon addonType="prepend">
                                            <InputGroupText>Every</InputGroupText>
                                          </InputGroupAddon>
                                          <Input {...input} className="" type="number" invalid={meta.error && meta.touched ? true : null} />
                                        </InputGroup>
                                        {meta.error && (
                                          <div
                                            style={{
                                              order: '4',
                                              width: '100%',
                                              display: meta.error && meta.touched ? 'block' : 'none',
                                            }}
                                            className="invalid-feedback"
                                          >
                                            {meta.error}
                                          </div>
                                        )}
                                      </>
                                    )}
                                    validate={(value, allValues, meta) => {
                                      if (!value) {
                                        return 'Please provide a valid value for the Billing frequency.';
                                      } else if (parseInt(value) <= 0) {
                                        return 'Delivery frequency cannot be less than 0.';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                    type="number"
                                    id={`billingIntervalCount`}
                                    className="mr-2"
                                    name={`billingIntervalCount`}
                                  />
                                  <OnChange name={`billingIntervalCount`}>
                                    {(value, previous) => {
                                      let defaultSpecificDayValue = null;
                                      if (values?.['billingIntervalCount'] != null) {
                                        form.mutators.setValue('deliveryIntervalCount', values?.['billingIntervalCount']);
                                      } else {
                                        form.mutators.setValue('deliveryIntervalCount', null);
                                      }
                                    }}
                                  </OnChange>
                                  <Field
                                    type="select"
                                    id={`billingIntervalType`}
                                    name={`billingIntervalType`}
                                    render={({ input, meta }) => {
                                      return (
                                        <Input
                                          invalid={meta.error && meta.touched ? true : null}
                                          style={{
                                            width: '48%',
                                            maxWidth: '200px',
                                            marginLeft: 'auto',
                                            flexGrow: 1,
                                          }}
                                          {...input}
                                        >
                                          <option value="">Please select</option>
                                          <option value="DAY">Day(s)</option>
                                          <option value="WEEK">Week(s)</option>
                                          <option value="MONTH">Month(s)</option>
                                          <option value="YEAR">Year(s)</option>
                                        </Input>
                                      );
                                    }}
                                    initialValue="DAY"
                                    validate={(value, allValues, meta) => {
                                      if (!value) {
                                        return 'Please provide a valid value for the frequency.';
                                      } else {
                                        if (value) {
                                          if (allValues['subscriptionPlans']?.length === 1) {
                                            return undefined;
                                          }
                                          let flag = false;
                                          allValues['subscriptionPlans'].forEach((plan, i) => {
                                            if (!allValues?.['billingIntervalCount']) {
                                              return undefined;
                                            }

                                            if (
                                              plan?.['billingIntervalCount'] + plan?.['billingIntervalType'] ===
                                                allValues?.['billingIntervalCount'] + value &&
                                              i !== index
                                            ) {
                                              flag = true;
                                            }
                                          });
                                          return undefined;
                                        }
                                      }
                                    }}
                                    className="form-control"
                                  />
                                  <OnChange name={`billingIntervalType`}>
                                    {(value, previous) => {
                                      let defaultSpecificDayValue = null;
                                      form.mutators.setValue('deliveryIntervalType', values?.['billingIntervalType']);
                                      if (values?.['frequencyType'] === 'ON_SPECIFIC_DAY' && values?.['billingIntervalType'] === 'WEEK') {
                                        defaultSpecificDayValue = 1;
                                      }
                                      form.mutators.setValue('specificDayEnabled', false);
                                      form.mutators.setValue('specificDayValue', defaultSpecificDayValue);
                                      form.mutators.setValue('frequencyType', 'ON_PURCHASE_DAY');
                                      form.mutators.setValue('cutOff', null);
                                    }}
                                  </OnChange>
                                </div>
                              </FormGroup>

                              <FormGroup>
                                {values?.['planType'] != 'PAY_AS_YOU_GO' && (
                                  <div style={values?.['planType'] != 'PAY_AS_YOU_GO' ? { display: 'block' } : { display: 'none' }}>
                                    <ReactTooltip
                                      html={true}
                                      id="billingfreqTool"
                                      effect="solid"
                                      delayUpdate={500}
                                      place="right"
                                      border={true}
                                      type="info"
                                    />
                                    <Label>Delivery Period:</Label>
                                    <Help
                                      style={{ fontSize: '1rem' }}
                                      data-for="billingfreqTool"
                                      data-tip="<div style='max-width:300px'>This is the number of deliveries that the customers can pay in advance for. For example, if you set the delivery frequency as 1 week, and if the billing timeframe is 12 weeks, and if , your customers will be charged for 12 weeks, but receive the deliveries once a week, for 12 weeks.</div>"
                                    />
                                    <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                                      <Field
                                        render={({ input, meta }) => (
                                          <>
                                            <Input
                                              {...input}
                                              className=""
                                              style={{ flexGrow: 1, width: '48%', marginRight: '4%' }}
                                              type="number"
                                              invalid={meta.error && meta.touched ? true : null}
                                            />
                                            {meta.error && (
                                              <div
                                                style={{
                                                  order: '4',
                                                  width: '100%',
                                                  display: meta.error && meta.touched ? 'block' : 'none',
                                                }}
                                                className="invalid-feedback"
                                              >
                                                {meta.error}
                                              </div>
                                            )}
                                          </>
                                        )}
                                        validate={(value, meta) => {
                                          if (value) {
                                            if (
                                              values?.['billingIntervalCount'] &&
                                              parseInt(values?.['billingIntervalCount']) < parseInt(value)
                                            ) {
                                              return 'Delivery Period should be less than the billing frequency.';
                                            }

                                            if (
                                              values?.['billingIntervalCount'] &&
                                              parseInt(values?.['billingIntervalCount']) % parseInt(value) != 0
                                            ) {
                                              return 'Delivery Period duration must be a multiple of the billing frequency duration.';
                                            }
                                          } else {
                                            return undefined;
                                          }
                                        }}
                                        type="number"
                                        id={`deliveryIntervalCount`}
                                        className="mr-2"
                                        name={`deliveryIntervalCount`}
                                      />
                                      <OnChange name={`deliveryIntervalCount`}>
                                        {(value, previous) => {
                                          let defaultSpecificDayValue = null;
                                          if (values?.['deliveryIntervalCount'] != null) {
                                            form.mutators.setValue('deliveryIntervalType', values?.['billingIntervalType']);
                                          } else {
                                            form.mutators.setValue('deliveryIntervalType', null);
                                          }
                                        }}
                                      </OnChange>
                                      <Field
                                        type="select"
                                        id={`deliveryIntervalType`}
                                        name={`deliveryIntervalType`}
                                        render={({ input, meta }) => (
                                          <Input
                                            disabled
                                            style={{
                                              width: '48%',
                                              maxWidth: '200px',
                                              marginLeft: 'auto',
                                              flexGrow: 1,
                                            }}
                                            {...input}
                                          >
                                            <option value="DAY">Day(s)</option>
                                            <option value="WEEK">Week(s)</option>
                                            <option value="MONTH">Month(s)</option>
                                            <option value="YEAR">Year(s)</option>
                                          </Input>
                                        )}
                                        className="form-control"
                                        initialValue="DAY"
                                      />
                                    </div>
                                  </div>
                                )}
                              </FormGroup>
                            </CardBody>
                            {values?.['planType'] != 'PREPAID' && (
                              <>
                                <Collapse isOpen={showAdvanced}>
                                  <hr className="m-0" />
                                  <CardBody style={{ background: '#f6f6f6' }}>
                                    <Row>
                                      <Col md={6} sm={6} xs={12}>
                                        <FormGroup>
                                          <ReactTooltip
                                            html={true}
                                            id="minNumOrder"
                                            effect="solid"
                                            delayUpdate={500}
                                            place="right"
                                            border={true}
                                            type="info"
                                          />
                                          <Label for={`minCycles`}>
                                            <b>Minimum Number</b> of{' '}
                                            {values?.['planType'] != 'PAY_AS_YOU_GO' ? 'billing Iterations' : 'Orders'}
                                          </Label>
                                          <Help
                                            style={{ fontSize: '1rem' }}
                                            data-for="minNumOrder"
                                            data-tip={
                                              values?.['planType'] != 'PAY_AS_YOU_GO'
                                                ? "<div style='max-width:300px'>Minimum number of billing iteration you want to bind your customers with, before they can cancel their subscription. Default value is one (the very first billing iteration).</div>"
                                                : "<div style='max-width:300px'>Minimum number of orders you want to bind your customers with, before they can cancel their subscription. Default value is one (the very first order).</div>"
                                            }
                                          />
                                          <Field
                                            render={({ input, meta }) => (
                                              <InputGroup style={{ flexGrow: 1 }}>
                                                <InputGroupAddon addonType="prepend">
                                                  <InputGroupText>
                                                    <FontAwesomeIcon icon={faHashtag} />
                                                  </InputGroupText>
                                                </InputGroupAddon>
                                                <>
                                                  <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                                  {meta.error && (
                                                    <div
                                                      class="invalid-feedback"
                                                      style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                    >
                                                      {meta.error}
                                                    </div>
                                                  )}
                                                </>
                                              </InputGroup>
                                            )}
                                            validate={(value, allValues, meta) => {
                                              if (value != null) {
                                                if (value <= 0) {
                                                  return 'Please provide a valid value for min cycle.';
                                                } else if (
                                                  allValues?.['maxCycles'] &&
                                                  parseInt(value) > parseInt(allValues?.['maxCycles'])
                                                ) {
                                                  return 'Min recurring period must be less than or equal the max cycle period';
                                                } else {
                                                  return undefined;
                                                }
                                              } else {
                                                return undefined;
                                              }
                                            }}
                                            initialValue={null}
                                            id={`minCycles`}
                                            className="form-control"
                                            type="number"
                                            name={`minCycles`}
                                          />
                                        </FormGroup>
                                      </Col>
                                      <Col md={6} sm={6} xs={12}>
                                        <FormGroup>
                                          <ReactTooltip
                                            html={true}
                                            id="maxNumOrder"
                                            effect="solid"
                                            delayUpdate={500}
                                            place="right"
                                            border={true}
                                            type="info"
                                          />
                                          <Label for={`maxCycles`}>
                                            <b>Maximum Number</b> of{' '}
                                            {values?.['planType'] != 'PAY_AS_YOU_GO' ? 'billing Iterations' : 'Orders'}
                                          </Label>
                                          <Help
                                            style={{ fontSize: '1rem' }}
                                            data-for="maxNumOrder"
                                            data-tip={
                                              values?.['planType'] != 'PAY_AS_YOU_GO'
                                                ? "<div style='max-width:300px'>Maximum number of billing iteration that will be fulfilled as a part of the subscription plan, after which it will automatically expire. Default value is infinity.</div>"
                                                : "<div style='max-width:300px'>Maximum number of orders that will be fulfilled as a part the subscription plan. After that, the subscription will automatically expire, and the customer will have to purchase a new subscription. This field can be left blank if you do not want the subscription to ever expire.</div>"
                                            }
                                          />
                                          <Field
                                            render={({ input, meta }) => (
                                              <InputGroup style={{ flexGrow: 1 }}>
                                                <InputGroupAddon addonType="prepend">
                                                  <InputGroupText>
                                                    <FontAwesomeIcon icon={faHashtag} />
                                                  </InputGroupText>
                                                </InputGroupAddon>
                                                <>
                                                  <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                                  {meta.error && (
                                                    <div
                                                      class="invalid-feedback"
                                                      style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                    >
                                                      {meta.error}
                                                    </div>
                                                  )}
                                                </>
                                              </InputGroup>
                                            )}
                                            validate={(value, allValues, meta) => {
                                              if (value != null) {
                                                if (value <= 0) {
                                                  return 'Please provide a valid value for max cycle.';
                                                } else if (
                                                  allValues?.['minCycles'] &&
                                                  parseInt(value) < parseInt(allValues?.['minCycles'])
                                                ) {
                                                  return 'Max recurring period must be equal or greater than the min cycle period';
                                                } else {
                                                  return undefined;
                                                }
                                              } else {
                                                return undefined;
                                              }
                                            }}
                                            initialValue={null}
                                            id={`maxCycles`}
                                            className="form-control"
                                            type="number"
                                            name={`maxCycles`}
                                          />
                                        </FormGroup>
                                      </Col>
                                      <Col md={6} sm={6} xs={12}>
                                        <FormGroup>
                                          <Label className="mt-0" for={`frequencyName`}>
                                            Frequency Plan Name:
                                          </Label>
                                          <Help
                                            style={{ fontSize: '1rem' }}
                                            data-tip="<div style='max-width:300px'>This is the name which your customer will see on the product details page. It could be a common name such as ‘Weekly’ or ‘Monthly’, or a more customized name.</div>"
                                          />
                                          <Field
                                            render={({ input, meta }) => (
                                              <>
                                                <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                                {meta.error && (
                                                  <div
                                                    class="invalid-feedback"
                                                    style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                  >
                                                    {meta.error}
                                                  </div>
                                                )}
                                              </>
                                            )}
                                            id={`frequencyName`}
                                            className="form-control"
                                            type="text"
                                            name={`frequencyName`}
                                          />
                                        </FormGroup>
                                      </Col>
                                      <Col md={6} sm={6} xs={12}>
                                        <FormGroup>
                                          <Label className="mt-0" for={`frequencyDescription`}>
                                            Frequency Plan Description:
                                          </Label>
                                          <Help
                                            style={{ fontSize: '1rem' }}
                                            data-tip="<div style='max-width:300px'>This is the description which your customer will see on the product details page.</div>"
                                          />
                                          <Field
                                            render={({ input, meta }) => (
                                              <>
                                                <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                                {meta.error && (
                                                  <div
                                                    class="invalid-feedback"
                                                    style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                  >
                                                    {meta.error}
                                                  </div>
                                                )}
                                              </>
                                            )}
                                            id={`frequencyDescription`}
                                            className="form-control"
                                            type="text"
                                            name={`frequencyDescription`}
                                          />
                                        </FormGroup>
                                      </Col>
                                    </Row>
                                  </CardBody>
                                </Collapse>
                                <CardHeader
                                  onClick={() => setShowAdvanced(old => !old)}
                                  style={{ justifyContent: 'center', cursor: 'pointer', background: '#eee' }}
                                >
                                  {!showAdvanced ? 'Show Advanced Options' : 'Collapse Advanced Options'}
                                  {
                                    <ChevronRight
                                      style={{
                                        transform: !showAdvanced ? 'rotate(0deg)' : 'rotate(-90deg)',
                                        transition: 'all 0.2s',
                                      }}
                                    />
                                  }
                                </CardHeader>
                              </>
                            )}
                          </Card>

                          <Card className="mt-4">
                            <CardBody>
                              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h5 style={{ margin: '0' }}>
                                  <b>Subscription Currency</b>
                                </h5>
                              </div>
                              <hr />
                              <FormGroup>
                                <Field
                                  type="select"
                                  id={`currencyCode`}
                                  name={`currencyCode`}
                                  render={({ input, meta }) => (
                                    <Input
                                      style={{
                                        width: '48%',
                                        maxWidth: '200px',
                                        flexGrow: 1,
                                      }}
                                      {...input}
                                    >
                                      <option value={props?.shopInfo.currency} selected>
                                        {props?.shopInfo.currency}
                                      </option>
                                      {currencies &&
                                        currencies.length > 0 &&
                                        currencies.map(currencyItem => (
                                          <option value={currencyItem.currency}>{currencyItem.currency}</option>
                                        ))}
                                    </Input>
                                  )}
                                  className="form-control"
                                  initialValue=""
                                />
                                <OnChange name={`currencyCode`}>
                                  {(value, previous) => {
                                    let update = [];
                                    values?.['lines']?.map((item, index) => {
                                      let price = 0;
                                      item?.prices?.map(p => {
                                        if (p?.currency_code === value) {
                                          price = p.price;
                                        }
                                      });
                                      update.push({ ...item, unitPrice: price == 0 ? item.price : price });
                                    });
                                    form.mutators.setValue('lines', update);
                                  }}
                                </OnChange>
                              </FormGroup>
                            </CardBody>
                          </Card>

                          <Card className="mt-4">
                            <CardBody>
                              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h5 style={{ margin: '0' }}>
                                  <b>Products</b>
                                </h5>
                              </div>
                              <hr />
                              <FieldArray name="lines">
                                {({ fields }) =>
                                  fields.map((name, idx) => (
                                    <Row style={{ marginBottom: '10px' }}>
                                      <Col sm={5}>
                                        <Field
                                          render={({ input, meta }) => (
                                            <Input
                                              {...input}
                                              className=""
                                              style={{ flexGrow: 1, width: '48%', marginRight: '4%' }}
                                              type="hidden"
                                              invalid={meta.error && meta.touched ? true : null}
                                            />
                                          )}
                                          type="hidden"
                                          id={`variantId`}
                                          className="mr-2"
                                          name={`${name}.variantId`}
                                        />
                                        <div style={{ display: 'flex' }}>
                                          <div style={{ width: '55px' }}>
                                            <img src={values?.['lines']?.[idx]?.imageSrc} style={{ width: '100%' }}></img>
                                          </div>
                                          <div style={{ marginLeft: '10px' }}>
                                            <div>{values?.['lines']?.[idx]?.title}</div>
                                            <div style={{ fontSize: '12px', opacity: '0.8' }}>
                                              Variant Id: {values?.['lines']?.[idx]?.variantId}
                                            </div>
                                          </div>
                                        </div>
                                      </Col>
                                      <Col sm={6}>
                                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                          <FormGroup style={{ width: '33%' }}>
                                            <Label for={`${name}.quantity`}>Quantity</Label>
                                            <Field
                                              render={({ input, meta }) => (
                                                <InputGroup style={{ flexGrow: 1 }}>
                                                  <InputGroupAddon addonType="prepend">
                                                    <InputGroupText>
                                                      <FontAwesomeIcon icon={faHashtag} />
                                                    </InputGroupText>
                                                  </InputGroupAddon>
                                                  <>
                                                    <Input
                                                      {...input}
                                                      className=""
                                                      type="number"
                                                      invalid={meta.error && meta.touched ? true : null}
                                                    />
                                                    {meta.error && (
                                                      <div
                                                        class="invalid-feedback"
                                                        style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                      >
                                                        {meta.error}
                                                      </div>
                                                    )}
                                                  </>
                                                </InputGroup>
                                              )}
                                              type="number"
                                              id={`quantity`}
                                              className="mr-2"
                                              name={`${name}.quantity`}
                                              validate={value => {
                                                if (!value || Number(value) < 1) {
                                                  return 'Please provide a valid value for Quantity';
                                                } else {
                                                  return undefined;
                                                }
                                              }}
                                            />
                                          </FormGroup>
                                          <FormGroup style={{ width: '49%' }}>
                                            <Label for={`${name}.unitPrice`}>Unit Price</Label>
                                            <Field
                                              render={({ input, meta }) => (
                                                <InputGroup style={{ flexGrow: 1 }}>
                                                  <InputGroupAddon addonType="prepend">
                                                    <InputGroupText>
                                                      <FontAwesomeIcon icon={faMoneyBill} />
                                                    </InputGroupText>
                                                  </InputGroupAddon>
                                                  <>
                                                    <Input
                                                      {...input}
                                                      className=""
                                                      type="string"
                                                      invalid={meta.error && meta.touched ? true : null}
                                                    />
                                                    {meta.error && (
                                                      <div
                                                        class="invalid-feedback"
                                                        style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                                      >
                                                        {meta.error}
                                                      </div>
                                                    )}
                                                  </>
                                                </InputGroup>
                                              )}
                                              type="string"
                                              id={`unitPrice`}
                                              className="mr-2"
                                              name={`${name}.unitPrice`}
                                            />
                                          </FormGroup>
                                        </div>
                                      </Col>
                                      <Col sm={1}>
                                        <FormGroup>
                                          <Label>&nbsp;</Label>
                                          <Button
                                            size="sm"
                                            className="btn-shadow-primary"
                                            color="danger"
                                            style={{ height: '38px', flexGrow: '1', display: 'block', width: '100%' }}
                                            onClick={() => {
                                              fields.remove(idx);
                                            }}
                                          >
                                            Delete
                                          </Button>
                                        </FormGroup>
                                      </Col>
                                    </Row>
                                  ))
                                }
                              </FieldArray>
                              <AddProductModal
                                selectedProductVarIds={values?.['lines']}
                                value={JSON.stringify([])}
                                onChange={value => {
                                  console.log(value);
                                }}
                                totalTitle="Add Product"
                                index={1}
                                buttonLabel="Add Products"
                                header="Select Product"
                                sellingPlanIds={values?.sellingPlan}
                                isFilterBySellingPlan={true}
                                addHandler={data => {
                                  setLoadingProductPrice(true);
                                  getVariantPrice(data, form.mutators.setValue, values);
                                }}
                                loadingProductPrice={loadingProductPrice}
                              />
                            </CardBody>
                          </Card>

                          <Card className="mt-4">
                            <CardBody>
                              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h5 style={{ margin: '0' }}>
                                  <b>Customer Details</b>
                                </h5>
                                <SearchCustomerModal buttonLabel="Search Customers" header="Select Customers" entity={values} />
                              </div>
                              <hr />
                              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                                <FormGroup style={{ width: '48%' }}>
                                  <Label for={`customerId`}>Customer ID</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <InputGroup style={{ flexGrow: 1 }}>
                                        <InputGroupAddon addonType="prepend">
                                          <InputGroupText>
                                            <FontAwesomeIcon icon={faHashtag} />
                                          </InputGroupText>
                                        </InputGroupAddon>
                                        <>
                                          <Input {...input} className="" type="number" invalid={meta.error && meta.touched ? true : null} />
                                          {meta.error && (
                                            <div
                                              class="invalid-feedback"
                                              style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                            >
                                              {meta.error}
                                            </div>
                                          )}
                                        </>
                                      </InputGroup>
                                    )}
                                    type="number"
                                    id={`customerId`}
                                    className="mr-2"
                                    name={`customerId`}
                                    validate={value => {
                                      if (value == null) {
                                        return 'Please provide a valid value for Customer Id';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </FormGroup>
                                <FormGroup style={{ width: '48%' }}>
                                  <Label for={`customerEmail`}>Email ID</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <InputGroup style={{ flexGrow: 1 }}>
                                        <InputGroupAddon addonType="prepend">
                                          <InputGroupText>
                                            <MailOutline />
                                          </InputGroupText>
                                        </InputGroupAddon>
                                        <>
                                          <Input {...input} className="" type="string" invalid={meta.error && meta.touched ? true : null} />
                                          {meta.error && (
                                            <div
                                              class="invalid-feedback"
                                              style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                            >
                                              {meta.error}
                                            </div>
                                          )}
                                        </>
                                      </InputGroup>
                                    )}
                                    type="string"
                                    id={`customerEmail`}
                                    className="mr-2"
                                    name={`customerEmail`}
                                  />
                                </FormGroup>
                                <FormGroup style={{ width: '48%' }}>
                                  <Label for={`paymentMethodId`}>Payment Method ID</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <InputGroup style={{ flexGrow: 1 }}>
                                        <InputGroupAddon addonType="prepend">
                                          <InputGroupText>
                                            <FontAwesomeIcon icon={faHashtag} />
                                          </InputGroupText>
                                        </InputGroupAddon>
                                        <>
                                          <Input
                                            {...input}
                                            className=""
                                            type="select"
                                            onChange={e => input.onChange(e.target.value)}
                                            invalid={meta.error && meta.touched ? true : null}
                                          >
                                            <option value="">Please Choose Payment Method ID</option>
                                            {props.customerPaymentMethods.map((payment, index) => (
                                              <option value={payment?.id} key={index}>
                                                {payment?.paymentName} - {payment?.maskedNumber} ({payment?.paymentType})
                                              </option>
                                            ))}
                                          </Input>
                                          {meta.error && (
                                            <div
                                              class="invalid-feedback"
                                              style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                            >
                                              {meta.error}
                                            </div>
                                          )}
                                        </>
                                      </InputGroup>
                                    )}
                                    type="select"
                                    id={`paymentMethodId`}
                                    className="mr-2"
                                    name={`paymentMethodId`}
                                    validate={value => {
                                      if (!value) {
                                        return 'Please choose a Payment method Id';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </FormGroup>
                                <FormGroup style={{ width: '48%' }}>
                                  <Label for={`status`}>Status</Label>
                                  <Field
                                    type="select"
                                    id={`status`}
                                    name={`status`}
                                    render={({ input, meta }) => (
                                      <Input
                                        invalid={meta.error && meta.touched ? true : null}
                                        {...input}
                                        style={{
                                          flexGrow: 1,
                                        }}
                                      >
                                        <option value="">Please Choose Status</option>
                                        <option value="ACTIVE">ACTIVE</option>
                                        <option value="PAUSED">PAUSED</option>
                                        <option value="CANCELLED">CANCELLED</option>
                                      </Input>
                                    )}
                                    initialValue="ACTIVE"
                                    className="form-control"
                                  />
                                </FormGroup>
                              </div>
                              <TipParagraph>
                                For merchants who wish to create subscriptions via physical commerce, if a customer has purchased a
                                subscription before, and you have their payment info on record, you can fill out the fields here to create a
                                new subscription for them. If a customer does not have payment info on record, ask for their customer
                                details (i.e. name, shipping info, etc.) and purchase their subscription product in your storefront. For
                                more information, refer to the dedicated help doc{' '}
                                <a
                                  href="https://intercom.help/appstle/en/articles/8055489-appstle-subscriptions-and-shopify-pos"
                                  target="_blank"
                                  rel="noopener noreferrer"
                                >
                                  here.
                                </a>
                              </TipParagraph>
                            </CardBody>
                          </Card>

                          <Card className="mt-4">
                            <CardBody>
                              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h5 style={{ margin: '0' }}>
                                  <b>Shipping Details</b>
                                </h5>
                              </div>
                              <hr />
                              <Row>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryFirstName">First Name</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryFirstName"
                                    className="form-control"
                                    type="text"
                                    name="deliveryFirstName"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for First Name';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryLastName">Last Name</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryLastName"
                                    className="form-control"
                                    type="text"
                                    name="deliveryLastName"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Last Name';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryPhone">Phone No</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryPhone"
                                    className="form-control"
                                    type="text"
                                    name="deliveryPhone"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Phone';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryCity">City</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryCity"
                                    className="form-control"
                                    type="text"
                                    name="deliveryCity"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for City';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryAddress1">Address 1</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryAddress1"
                                    className="form-control"
                                    type="text"
                                    name="deliveryAddress1"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Address 1';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={6} className="mb-3">
                                  <Label for="deliveryAddress2">Address 2</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryAddress2"
                                    className="form-control"
                                    type="text"
                                    name="deliveryAddress2"
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="company">Company</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="company"
                                    className="form-control"
                                    type="text"
                                    name="company"
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="province">Province</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="province"
                                    className="form-control"
                                    type="text"
                                    name="province"
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="deliveryProvinceCode">Province Code</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryProvinceCode"
                                    className="form-control"
                                    type="text"
                                    name="deliveryProvinceCode"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Province code';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="deliveryZip">Zip</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryZip"
                                    className="form-control"
                                    type="text"
                                    name="deliveryZip"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Zip code';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="country">Country</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="country"
                                    className="form-control"
                                    type="text"
                                    name="country"
                                  />
                                </Col>
                                <Col md={4} className="mb-3">
                                  <Label for="deliveryCountryCode">Country Code</Label>
                                  <Field
                                    render={({ input, meta }) => (
                                      <>
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} />
                                      </>
                                    )}
                                    id="deliveryCountryCode"
                                    className="form-control"
                                    type="text"
                                    name="deliveryCountryCode"
                                    validate={value => {
                                      if (!value) {
                                        return 'Please provide a valid value for Country Code';
                                      } else {
                                        return undefined;
                                      }
                                    }}
                                  />
                                </Col>
                                <Col md={8} className="mb-3">
                                  <FormGroup>
                                    <Label for={`deliveryPriceAmount`}>Shipping Price</Label>
                                    <Field
                                      render={({ input, meta }) => (
                                        <InputGroup style={{ flexGrow: 1 }}>
                                          <InputGroupAddon addonType="prepend">
                                            <InputGroupText>
                                              <FontAwesomeIcon icon={faMoneyBill} />
                                            </InputGroupText>
                                          </InputGroupAddon>
                                          <>
                                            <Input
                                              {...input}
                                              className=""
                                              type="string"
                                              invalid={meta.error && meta.touched ? true : null}
                                            />
                                            {meta.error && (
                                              <div
                                                class="invalid-feedback"
                                                style={{ display: meta.error && meta.touched ? 'block' : 'none' }}
                                              >
                                                {meta.error}
                                              </div>
                                            )}
                                          </>
                                        </InputGroup>
                                      )}
                                      type="string"
                                      id={`deliveryPriceAmount`}
                                      className="mr-2"
                                      name={`deliveryPriceAmount`}
                                      validate={value => {
                                        if (!value) {
                                          return 'Please provide a valid value for Shipping Price';
                                        } else {
                                          return undefined;
                                        }
                                      }}
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                            </CardBody>
                          </Card>
                        </form>
                      </>
                    );
                  }}
                />
              </FeatureAccessCheck>
            </CardBody>
          </div>
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = storeState => ({
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  usedProductIds: storeState.subscriptionGroup.usedProductIds,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess,
  shopInfo: storeState.shopInfo.entity,
  customerPaymentMethods: storeState.subscriptionGroup.customerPaymentMethods,
  customer: storeState.customer.customer,
  customerDetails: storeState.customer.customerDetails,
  customerPaymentDetails: storeState.customer.customerPaymentDetails,
  sellingPlanData: storeState.subscriptionGroup.sellingPlanData,
});

const mapDispatchToProps = {
  getEntity,
  getUsedProducts,
  updateEntity,
  createEntity,
  reset,
  updateStateEntity,
  customerDetailsReset,
  getAllSellingPlansList,
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateSubscription);
