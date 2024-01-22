import React, { useState } from 'react';
import { Field } from 'react-final-form';
import { Card, CardBody, FormGroup, Input, Label } from 'reactstrap';
import ProductVariantModalV2 from '../TaggingRules/components/ProductVariantModelV2';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';

const PlanNameField = () => (
  <FormGroup>
    <Label for="groupName" className="d-flex align-items-center" style={{ gap: '.25rem' }}>
      Plan name
      <HelpTooltip>
        <p>Name of the subscription plan (since you may have several subscription plans), and the product that you select, for the plan.</p>
        <p className="mb-0">Example: 'Pet Food Weekly Subscription'</p>
      </HelpTooltip>
    </Label>
    <Field
      id="groupName"
      name="groupName"
      validate={value => {
        return !!value ? undefined : 'Please provide plan name.';
      }}
    >
      {({ input, meta }) => (
        <>
          <Input {...input} className="form-control" placeholder="Subscription Plan Name" invalid={meta.error && meta.touched} />
          {meta.error && meta.touched && <span className="invalid-feedback">{meta.error}</span>}
        </>
      )}
    </Field>
  </FormGroup>
);

const SelectProductsField = ({ setCheckProductStatusDraft, loadMoreProductsData, loadMoreVariantsData, productDataInfo, setSelectedProductsAndVariantsData, selectedProductsAndVariantsData }) => {
  function checkProductStatus(products: any) {
    if (products?.filter((product: any) => product.status !== 'ACTIVE').length > 0) {
      setCheckProductStatusDraft(true);
    } else {
      setCheckProductStatusDraft(false);
    }
  }
  const [selectedProductFilterValue, setSelectedProductFilterValue] = useState('product');
  return (
    <FormGroup>
      <Field
        name="selectedProducts"
        validate={selectedProducts => {
          const products = selectedProducts?.productsData;

          if (products?.length > 5000) {
            return (
              "Shopify doesn't allow more than 5000 products in a subscription plan. Please remove " +
              (products.length - 5000) +
              ' products.'
            );
          }

          if (!products?.length && !selectedProducts?.variantsData?.length) {
            return 'Please select at least one product.';
          }

          return undefined;
        }}
      >
        {({ input, meta }) => {
          return (
            <>
              <ProductVariantModalV2
                setSelectedProductsAndVariantsData={setSelectedProductsAndVariantsData}
                selectedProductsAndVariantsData={selectedProductsAndVariantsData}
                id="selectedProducts"
                buttonLabel="Select Products"
                header="Product"
                checkProductStatus={checkProductStatus}
                isCollectionButtonEnable={true}
                selectedProductFilter={true}
                selectedProductFilterValue={selectedProductFilterValue}
                isCreateSubscription={true}
                loadMoreProductsData={loadMoreProductsData}
                loadMoreVariantsData={loadMoreVariantsData}
                productDataInfo={productDataInfo}
                selectedProductIds={JSON.stringify(input.value?.productsData ?? [])}
                selectedProductVarIds={JSON.stringify(input.value?.variantsData ?? [])}
                onChange={(productsData: any, variantsData: any) => {
                  const event = {
                    target: {
                      value: {
                        productsData,
                        variantsData
                      }
                    }
                  };
                  input.onChange(event);
                }}
              />
              {meta.error && (meta.touched || !meta.pristine) && <span className="invalid-feedback d-inline">{meta.error}</span>}
            </>
          );
        }}
      </Field>
    </FormGroup>
  );
};

const CreateSubscriptionGroupInfo = ({ setCheckProductStatusDraft, loadMoreProductsData, loadMoreVariantsData, productDataInfo, setSelectedProductsAndVariantsData, selectedProductsAndVariantsData }) => {
  return (
    <>
      <Card>
        <CardBody>
          <PlanNameField />
        </CardBody>
      </Card>
      <Card className="card-margin mt-3">
        <CardBody>
          <SelectProductsField
            setCheckProductStatusDraft={setCheckProductStatusDraft}
            loadMoreProductsData={loadMoreProductsData}
            loadMoreVariantsData={loadMoreVariantsData}
            productDataInfo={productDataInfo}
            setSelectedProductsAndVariantsData={setSelectedProductsAndVariantsData}
            selectedProductsAndVariantsData={selectedProductsAndVariantsData}
          />
        </CardBody>
      </Card>
    </>
  );
};

export default CreateSubscriptionGroupInfo;
