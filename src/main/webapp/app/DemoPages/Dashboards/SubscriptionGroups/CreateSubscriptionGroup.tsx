import React, { useEffect, useState, useRef, useMemo } from 'react';
import Loader from 'react-loaders';
import swal from 'sweetalert';
import { Card, CardBody, Col, FormText, Row } from 'reactstrap';
import { Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { useSelector, useDispatch } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getEntity,
  reset,
  updateEntity,
  getSubscriptionGroupProducts,
  getSubscriptionGroupVariants,
  syncEntity
} from 'app/entities/subscription-group/subscription-group.reducer';
import arrayMutators from 'final-form-arrays';
import _, { values } from 'lodash';
import './CreateSubscriptionGroup.scss';
import CreateSubscriptionGroupPlans from './CreateSubscriptionGroupPlans';
import ActiveProductAlert from '../Subscriptions/ActiveProductAlert';
import { IRootState } from 'app/shared/reducers';
import { useHistory, useRouteMatch } from 'react-router-dom';
import CreateSubscriptionGroupInfo from './CreateSubscriptionGroupInfo';
import { ISubscriptionGroup, SubscriptionPlan, SubscriptionPlanCustomField } from 'app/shared/model/subscription-group.model';
import CreateSubscriptionGroupHelpPopup from './CreateSubscriptionGroupHelpPopup';
import axios from 'axios';
import { OrderBillingType } from 'app/shared/model/enumerations/order-billing-type.model';

export type SubscriptionPlanValues = SubscriptionPlan & {
  formFieldJsonToggle?: boolean;
  formFieldJsonArray?: SubscriptionPlanCustomField[];
};

export type CreateSubscriptionGroupValues = Omit<ISubscriptionGroup, 'productIds' | 'variantIds' | 'subscriptionPlans'> & {
  selectedProducts: {
    productsData: any[];
    variantsData: any[];
  };
  subscriptionPlans: SubscriptionPlanValues[];
};

export const TipParagraph = ({ children }: { children: React.ReactNode }) => (
  <div className="pl-3" style={{ gap: '1em', borderLeft: '2px solid #dee2e6' }}>
    <p>
      <span className="badge badge-pill badge-success mr-2 mb-1" style={{ letterSpacing: '.25em' }}>
        TIP
      </span>
      {children}
    </p>
  </div>
);

const SubscriptionInformation = () => (
  <FormText>
    <h5>Subscription information</h5>
    <p>Name of the subscription plan (since you may have several subscription plans), and the product that you select, for the plan.</p>
    <p style={{ fontSize: '12px' }}>
      <b>Note</b>: Plan name is for internal use only, and will not be visible to your customers.
    </p>
    <TipParagraph>
      To remove the one-time purchase option from the product, go to your{' '}
      <b>
        Shopify Admin {'->'} Products {'->'} click on the product {'->'} scroll down to "Purchase Options" card {'->'} check the box next to
        "Only show this product with these purchase options"
      </b>
      <div className="mt-2">
        Here is a{' '}
        <a href={'https://www.loom.com/share/ad2dd0e321364c03a1221182e8a2c377'} target={'_blank'}>
          video
        </a>{' '}
        to guide you as well.
      </div>
    </TipParagraph>
  </FormText>
);

const SubscriptionFrequencyInfo = () => (
  <FormText>
    <h5>Subscription Frequency - Order Frequency & Billing Frequency</h5>
    <p>
      <b>Order/Delivery Frequency</b> refers to the recurring order fulfillment frequency.
    </p>
    <p>
      <b>Billing Frequency</b> refers to how often the customer will be billed.
    </p>
    <p className="font-weight-bold">We support 3 kind of subscription billing plans:</p>
    <hr style={{ borderColor: '#fff' }} />
    <p>
      <b>1. Pay As You Go:</b> This plan type charges the customer just for the 'immediately next' recurring order or delivery.
    </p>
    <p>
      <b>2. Prepaid One-time Plan:</b> This plan type allows you to receive the payment for multiple periods of future orders at the same
      time.
    </p>
    <p className="font-italic">
      Please note that in ‘Prepaid One-time Plan’, the contract pauses at the end of the billing cycle, and needs to be manually renewed by
      the customer.
    </p>
    <p>
      <b>3. Prepaid Auto-renew:</b> This plan is very similar to the Prepaid One-time Plan, except that the contract does not pause at the
      end of the billing cycle, and auto renews, until the customer expressly cancels the subscription.
    </p>
    <TipParagraph>
      You can edit the various text labels appearing in the subscription widget in{' '}
      <b>
        Website Widgets {'->'} Product Page {'->'} Widget Labels.
      </b>
    </TipParagraph>
    <TipParagraph>
      To change discount after specific number of orders, checkout this{' '}
      <a href={'https://www.youtube.com/watch?v=dwONFEPDQYo'} target={'_blank'}>
        video
      </a>
    </TipParagraph>
    <TipParagraph>
      To learn more about setting cutoff days, checkout this{' '}
      <a href={'https://youtu.be/Sc2DI31ow5o'} target={'_blank'}>
        video
      </a>{' '}
      for weekly cutoff days, or this{' '}
      <a href={'https://youtu.be/FVyb76d7X9Y'} target={'_blank'}>
        video
      </a>{' '}
      for monthly cutoff days
    </TipParagraph>
  </FormText>
);

export interface IProductData {
  preSelectedProduct: any[];
  productPageInfo: { cursor: any; hasNextPage: Boolean };
  productLoading: Boolean;
  preSelectedVariant: any[];
  variantPageInfo: { cursor: any; hasNextPage: Boolean };
  variantLoading: Boolean;
}

const CreateSubscriptionGroup = () => {
  const subscriptionGroupEntity = useSelector((state: IRootState) => state.subscriptionGroup.entity);
  const isLoading = useSelector((state: IRootState) => state.subscriptionGroup.loading);
  const isUpdating = useSelector((state: IRootState) => state.subscriptionGroup.updating);
  const syncLoading = useSelector((state: IRootState) => state.subscriptionGroup.syncLoading);

  const sellingPlanProductsData = useSelector((state: IRootState) => state.subscriptionGroup.sellingPlanProductsData);
  const sellingPlanVariantsData = useSelector((state: IRootState) => state.subscriptionGroup.sellingPlanVariantsData);

  const isUpdateSuccess = useSelector((state: IRootState) => state.subscriptionGroup.updateSuccess);
  const dispatch = useDispatch();

  const [formErrors, setFormErrors] = useState();
  const [checkProductStatusDraft, setCheckProductStatusDraft] = useState(false);
  const [selectedProductsAndVariantsData, setSelectedProductsAndVariantsData] = useState({ products: [], variants: [] });
  const submit = useRef(() => {});

  const [productDataInfo, setProductDataInfo] = useState<IProductData>({
    preSelectedProduct: [],
    productPageInfo: { cursor: null, hasNextPage: false },
    productLoading: false,
    preSelectedVariant: [],
    variantPageInfo: { cursor: null, hasNextPage: false },
    variantLoading: false
  });

  const history = useHistory();
  const match = useRouteMatch<any>();

  const isNew = !match.params || !match.params.id;
  const isDuplicate = match.params.duplicateid;

  const initialValues = useMemo(() => {
    const selectedProducts = {
      productsData: productDataInfo?.preSelectedProduct,
      variantsData: productDataInfo?.preSelectedVariant
    };

    const initialValues: CreateSubscriptionGroupValues = {
      ...subscriptionGroupEntity,
      selectedProducts
    };

    initialValues.subscriptionPlans = initialValues?.subscriptionPlans?.map(plan => {
      try {
        plan.formFieldJsonArray = JSON.parse(plan.formFieldJson);
      } catch (e) {}

      if (!Array.isArray(plan.formFieldJsonArray)) {
        plan.formFieldJsonArray = [];
      }

      plan.formFieldJsonToggle = plan.formFieldJsonArray.length > 0;

      return plan;
    });

    return initialValues;
  }, [subscriptionGroupEntity, productDataInfo]);

  useEffect(() => {
    const parseJSONList = (jsonString) => {
      try {
        return JSON.parse(jsonString);
      } catch (err) {
        return [];
      }
    };
  
    const products = parseJSONList(subscriptionGroupEntity?.productIds);
    const variants = parseJSONList(subscriptionGroupEntity?.variantIds);
  
    setSelectedProductsAndVariantsData({ products, variants });
  }, [subscriptionGroupEntity]);  

  const handleClose = () => {
    history.push('/dashboards/subscription-plan');
  };

  useEffect(() => {
    if (isNew && !isDuplicate) {
      dispatch(reset());
    } else {
      dispatch(getEntity(match.params.id || match.params.duplicateid));
      dispatch(getSubscriptionGroupProducts(match.params.id || isDuplicate, false, null));
      dispatch(getSubscriptionGroupVariants(match.params.id || isDuplicate, false, null));
    }
  }, []);

  useEffect(() => {
    if (isUpdateSuccess) handleClose();
  }, [isUpdateSuccess]);

  useEffect(() => {
    setProductDataInfo({
      ...productDataInfo,
      preSelectedVariant: sellingPlanVariantsData?.products,
      variantPageInfo: sellingPlanVariantsData?.pageInfo
    });
  }, [sellingPlanVariantsData]);

  useEffect(() => {
    setProductDataInfo({
      ...productDataInfo,
      preSelectedProduct: sellingPlanProductsData?.products,
      productPageInfo: sellingPlanProductsData?.pageInfo
    });
  }, [sellingPlanProductsData]);

  const loadMoreProductsData = () => {
    const requestUrl = `api/v2/subscription-groups/products/${match.params.id}?cursor=${productDataInfo?.productPageInfo?.cursor}&next=${
      productDataInfo?.productPageInfo?.hasNextPage
    }&cacheBuster=${new Date().getTime()}`;
    setProductDataInfo({
      ...productDataInfo,
      productLoading: true
    });
    axios
      .get(requestUrl)
      .then(res => {
        if (res?.data && res?.data?.products) {
          setProductDataInfo({
            ...productDataInfo,
            preSelectedProduct: [...productDataInfo?.preSelectedProduct, ...res?.data?.products],
            productPageInfo: res?.data?.pageInfo,
            productLoading: false
          });
        }
      })
      .catch(err => {
        console.log(err);
      });
  };

  const loadMoreVariantsData = () => {
    const requestUrl = `api/v2/subscription-groups/variants/${match.params.id}?cursor=${productDataInfo?.variantPageInfo?.cursor}&next=${
      productDataInfo?.variantPageInfo?.hasNextPage
    }&cacheBuster=${new Date().getTime()}`;
    setProductDataInfo({
      ...productDataInfo,
      variantLoading: true
    });
    axios
      .get(requestUrl)
      .then(res => {
        if (res?.data && res?.data?.products) {
          setProductDataInfo({
            ...productDataInfo,
            preSelectedVariant: [...productDataInfo?.preSelectedVariant, ...res?.data?.products],
            variantPageInfo: res?.data?.pageInfo,
            variantLoading: false
          });
        }
      })
      .catch(err => {
        console.log(err);
      });
  };

  async function saveEntity(values: CreateSubscriptionGroupValues) {
    const subscriptionPlansList = values?.subscriptionPlans?.map((plan) => {
      const billingCycles =
        plan.planType === OrderBillingType.PREPAID
          ? { maxCycles: 1, minCycles: null }
          : { maxCycles: plan.maxCycles, minCycles: plan.minCycles };
    
      return {
        ...plan,
        formFieldJson: JSON.stringify(plan.formFieldJsonArray),
        id: true,
        ...billingCycles,
      };
    });
    
    let entity: ISubscriptionGroup = {
      ...values,
      subscriptionPlans: [...subscriptionPlansList],
      productIds: JSON.stringify(values.selectedProducts.productsData),
      variantIds: JSON.stringify(values.selectedProducts.variantsData),
    };

    if (isNew) {
      if (isDuplicate) {
        entity = { ...entity, id: `` };
      }
      dispatch(createEntity(entity));
    } else {
      return swal({
        content: {
          element: 'span',
          attributes: {
            innerHTML:
              "<p>The changes you have made within the subscription plan won't affect the existing subscriptions. Features such as the pricing policy, the prices of the products, the cut off days, the plan type, and similar features will remain the same.</p>" +
              '<p>To illustrate, Shopify treats subscriptions exactly like orders. Just as changing the pricing of products within Shopify does not affect already placed orders, making changes within the subscription plan will also not affect the existing subscriptions.</p>' +
              "<p>Should you wish to have these changes reflect in your existing subscriptions as well, you can check out the array of Bulk Automations we have from <a onclick='swal.close()' href='#/dashboards/subscription-automation'> Automation > Bulk Automations > Automations.</a> </p>",
            className: 'swal-text my-0 px-0',
          },
        },
        icon: 'warning',
        buttons: ['Cancel', 'Confirm'],
        dangerMode: true,
      }).then(value => {
        if (value) {
          dispatch(updateEntity(entity));
        }
      });
    }
  }

  const triggerSyncEntity = () => {
    syncEntity(match.params.id, dispatch).then(data => {
      if (data?.value?.status == 200) {
        swal({
          title: 'Syncing',
          text: 'Subscription plan product sync started. Will take time depending on the size of the product and variant.',
          icon: 'success'
        });
      }
    });
  };

  return (
    <>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading={isNew ? 'Create Subscription Plan' : 'Edit Subscription Plan'}
          icon="pe-7s-network icon-gradient bg-mean-fruit"
          enablePageTitleAction
          actionTitle="Save"
          onActionClick={submit.current}
          onThirdActionClick={() => triggerSyncEntity()}
          enableThirdPageTitleAction={!isNew}
          onThirdActionUpdating={syncLoading}
          thirdUpdatingText={'Syncing'}
          thirdActionTitle={'Sync Product Info'}
          enableSecondaryPageTitleAction
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            history.push(`/dashboards/subscription-plan`);
          }}
          thirdActionTooltip={'Click here if you want to synchronize the updated product information from Shopify to the App.'}
          formErrors={formErrors}
          onActionUpdating={isUpdating}
          sticky
        />
        {isLoading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" active />
          </div>
        ) : (
          <Form<CreateSubscriptionGroupValues>
            mutators={{
              ...arrayMutators
            }}
            initialValues={initialValues}
            onSubmit={values => saveEntity(values)}
            render={({ handleSubmit, form, errors }) => {
              setFormErrors(errors);
              submit.current = handleSubmit;
              return (
                <>
                  <form onSubmit={handleSubmit}>
                    <Row>
                      <Col md="4">
                        <SubscriptionInformation />
                        {checkProductStatusDraft ? <ActiveProductAlert /> : ''}
                      </Col>
                      <Col md="8">
                        <CreateSubscriptionGroupInfo
                          setCheckProductStatusDraft={setCheckProductStatusDraft}
                          loadMoreProductsData={loadMoreProductsData}
                          loadMoreVariantsData={loadMoreVariantsData}
                          productDataInfo={productDataInfo}
                          setSelectedProductsAndVariantsData={setSelectedProductsAndVariantsData}
                          selectedProductsAndVariantsData={selectedProductsAndVariantsData}
                        />
                      </Col>
                    </Row>
                    <hr />
                    <Row>
                      <Col md="4">
                        <SubscriptionFrequencyInfo />
                      </Col>
                      <Col md="8">
                        <CreateSubscriptionGroupPlans />
                      </Col>
                    </Row>
                  </form>
                </>
              );
            }}
          />
        )}
        <CreateSubscriptionGroupHelpPopup />
      </ReactCSSTransitionGroup>
    </>
  );
};

export default CreateSubscriptionGroup;
