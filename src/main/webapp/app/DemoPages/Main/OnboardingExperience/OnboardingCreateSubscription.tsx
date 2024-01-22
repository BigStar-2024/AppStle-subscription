import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Form } from 'react-final-form';
import { createEntity } from 'app/entities/subscription-group/subscription-group.reducer';
import arrayMutators from 'final-form-arrays';
import { Container, Card, CardBody, Button } from 'reactstrap';
import 'app/DemoPages/Dashboards/TaggingRules/TagginRules.scss';
import { OnboardingPage } from '../OnboardingExperience';
import _ from 'lodash';
import { ISubscriptionGroup, defaultValue as defaultSubscriptionGroupValue } from 'app/shared/model/subscription-group.model';
import { IRootState } from 'app/shared/reducers';
import Loader from 'react-loaders';
import { CreateSubscriptionGroupValues } from 'app/DemoPages/Dashboards/SubscriptionGroups/CreateSubscriptionGroup';
import CreateSubscriptionGroupInfo from 'app/DemoPages/Dashboards/SubscriptionGroups/CreateSubscriptionGroupInfo';
import CreateSubscriptionGroupPlans from 'app/DemoPages/Dashboards/SubscriptionGroups/CreateSubscriptionGroupPlans';

const OnboardingCreateSubscription: OnboardingPage = ({ goToNextStep }) => {
  const isCreatingSubscription = useSelector((state: IRootState) => state.subscriptionGroup.loading || state.subscriptionGroup.updating);
  const [selectedProductsAndVariantsData, setSelectedProductsAndVariantsData] = useState({ products: [], variants: [] });
  const dispatch = useDispatch();

  function handleSubmitForm(values: CreateSubscriptionGroupValues) {
    const entity: ISubscriptionGroup = {
      ...values,
      subscriptionPlans: values?.subscriptionPlans?.map(plan => ({
        ...plan,
        formFieldJson: JSON.stringify(plan.formFieldJsonArray),
        id: true,
      })),
      productIds: JSON.stringify(values.selectedProducts.productsData),
      variantIds: JSON.stringify(values.selectedProducts.variantsData),
    };

    (dispatch as (dispatch: any) => Promise<any>)(createEntity(entity))
      .then(result => {
        if (result.value.status == 201) {
          goToNextStep();
        }
      })
      .catch(err => console.log(err));
  }

  return (
    <Container className="py-5">
      <h3>Create Your First Subscription Plan</h3>
      <Form<CreateSubscriptionGroupValues>
        initialValues={{
          ...defaultSubscriptionGroupValue,
          selectedProducts: {
            productsData: [],
            variantsData: [],
          },
        }}
        initialValuesEqual={_.isEqual}
        onSubmit={handleSubmitForm}
        mutators={{ ...arrayMutators }}
        render={({ handleSubmit, invalid }) => {
          return (
            <form onSubmit={handleSubmit}>
              <div className="position-relative">
                <div className="mb-3">
                  <CreateSubscriptionGroupInfo 
                    setSelectedProductsAndVariantsData={setSelectedProductsAndVariantsData}
                    selectedProductsAndVariantsData={selectedProductsAndVariantsData}
                    setCheckProductStatusDraft={() => {}}
                  />
                </div>
                <CreateSubscriptionGroupPlans showAdvancedOptions={false} />
                {isCreatingSubscription && (
                  <div className="position-absolute" style={{ top: 0, height: '100%', width: '100%' }}>
                    <div className="position-absolute bg-secondary" style={{ opacity: 0.5, height: '100%', width: '100%' }}></div>
                    <div className="d-flex flex-column justify-content-center align-items-center" style={{ height: '100%', width: '100%' }}>
                      <Card className="w-50" style={{ minWidth: 'min-content' }}>
                        <CardBody className="d-flex flex-column justify-content-center align-items-center">
                          <h3 className="text-primary mb-1 text-center">Creating Your Subscription</h3>
                          <div className="d-flex justify-content-center align-items-center" style={{ width: '65px', height: '65px' }}>
                            <Loader type="ball-spin-fade-loader" active />
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </div>
                )}
              </div>
              <div className="d-flex w-100 mt-3 justify-content-end">
                <Button
                  size="lg"
                  color="primary"
                  className="px-5"
                  style={{ fontSize: '1rem' }}
                  onClick={handleSubmit}
                  outline={isCreatingSubscription}
                  disabled={isCreatingSubscription || invalid}
                >
                  {isCreatingSubscription ? (
                    <div className="d-flex align-items-end justify-content-center">
                      <span>Creating your subscription</span>
                      <div style={{ transform: 'translateY(-13px) translateX(13px) scale(0.4)', width: '26px' }}>
                        <Loader type="ball-spin-fade-loader" active />
                      </div>
                    </div>
                  ) : (
                    'Next'
                  )}
                </Button>
              </div>
            </form>
          );
        }}
      />
    </Container>
  );
};

export default OnboardingCreateSubscription;
