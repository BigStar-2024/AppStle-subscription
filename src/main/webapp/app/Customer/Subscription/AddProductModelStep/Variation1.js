import React, { Fragment, useState, useEffect } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {
  Row,
  Col
} from 'reactstrap';
import { connect, useSelector } from 'react-redux';

import MultiStep from './Wizard';
import Axios from 'axios';

import Step1 from './Step1';
import Step2 from './Step2';
import Step4 from './Step4';
import { toast } from 'react-toastify';
import {
  getCustomerPortalEntity,
} from 'app/entities/subscriptions/subscription.reducer';
import {resetProductOptions} from 'app/entities/fields-render/data-product.reducer';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};


function FormWizardVar1(props) {
  const { shopName, contractId, getCustomerPortalEntity, sellingPlanIds, upcomingOrderId, formStep, setFormStep, loading, resetProductOptions, customerPortalSettingEntity } = props;
  const [updateInProgress, setUpdateInProgress] = useState(false);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [purchaseOption, setPurchaseOption] = useState("SUBSCRIBE");
  const [errors, setErrors] = useState([])

  useEffect(() => {
    resetProductOptions();
  }, [purchaseOption])

  useEffect(() => {
    if ((customerPortalSettingEntity?.addOneTimeProduct && customerPortalSettingEntity?.addAdditionalProduct) || !upcomingOrderId) {
      setPurchaseOption("SUBSCRIBE");
    } else if (customerPortalSettingEntity?.addOneTimeProduct) {
      setPurchaseOption("ONE_TIME");
    } else if (customerPortalSettingEntity?.addAdditionalProduct) {
      setPurchaseOption("SUBSCRIBE");
    }
  }, [customerPortalSettingEntity])

  let steps = [
    { name: `${customerPortalSettingEntity?.purchaseOptionLabelTextV2 || "Purchase Option"}`, component: <Step1
      setPurchaseOption={setPurchaseOption}
      purchaseOption={purchaseOption}
      upcomingOrderId ={upcomingOrderId}
      customerPortalSettingEntity={customerPortalSettingEntity}
    /> },
    { name: `${customerPortalSettingEntity?.selectProductLabelTextV2 || "Select Products"}`, component: <Step2
      purchaseOption ={purchaseOption}
      sellingPlanIds = {sellingPlanIds}
      contractId={contractId}
      updateInProgress ={updateInProgress}
      selectedVariantItems ={setSelectedVariantItems}
      customerPortalSettingEntity={customerPortalSettingEntity}
      totalTitle="Select Source Product"
      methodName="Save"
      searchBarPlaceholder = {customerPortalSettingEntity?.addProductLabelTextV2}
      noProductDataMessage = {customerPortalSettingEntity?.noProductFoundMessageV2 || "There are no product available on given search result"}
    /> },
    { name: `${customerPortalSettingEntity?.finishLabelTextV2 || "Finish"}`, component: <Step4
      customerPortalSettingEntity={customerPortalSettingEntity}
      errors={errors}
      purchaseOption ={purchaseOption}
    /> }
  ];

  const addProduct = (selectedItems, tempError) => {
    if (!tempError) {
      tempError = [];
    }
    setUpdateInProgress(true);
    let newSelectedItems = JSON.parse(JSON.stringify(selectedItems))
    let selectProduct = newSelectedItems?.shift();
    if(!selectProduct)
    {
        setUpdateInProgress(false);
        // props.toggle();
        setErrors([...tempError])
        getCustomerPortalEntity(contractId);
        return;
    }
    else if(selectProduct && purchaseOption == "ONE_TIME")
    {
        let oneTimeUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${selectProduct?.id}&isOneTimeProduct=true`;

        Axios.put(oneTimeUrl)
        .then(res => {
            tempError.push({success: true, item: selectProduct})
            addProduct(newSelectedItems, tempError);
          // toast.success('Contract Updated', options);
          if(!newSelectedItems?.length) {
            setUpdateInProgress(false);
            }
          }
        )
        .catch(err => {
            console.log(err);
            tempError.push({success: false, item: selectProduct, err: err?.response?.data?.message})
            addProduct(newSelectedItems, tempError);
            // setUpdateInProgress(false);
            // props.toggle();
          // toast.error('Contract Update Failed', options);
        });
    }
    else if (selectProduct && purchaseOption == "SUBSCRIBE") {
      const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${selectProduct?.id}`;
      Axios.put(requestUrl)
        .then(res => {
            // setUpdateInProgress(false);
            // toast.success('Contract Updated', options);
            tempError.push({success: true, item: selectProduct})
            addProduct(newSelectedItems, tempError);
            if(!newSelectedItems?.length) {
              getCustomerPortalEntity(contractId);
              setUpdateInProgress(false);
            }
          }
        )
        .catch(err => {
            console.log(err);
            tempError.push({success: false, item: selectProduct, err: err?.response?.data?.message})
            addProduct(newSelectedItems, tempError);
          //   props.toggle();
          // setUpdateInProgress(false);
          // toast.error('Contract Update Failed', options);
        });
    }
  };

  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
        style={{backgroundColor: "rgba(0,0,0,.03)"}}
      >
        <div>
          <Row>
            <Col>
              <div className="forms-wizard-vertical card-body" style={{paddingTop: "30px"}}>
                <MultiStep
                  showNavigation
                  steps={steps}
                  selectedVariantItems={selectedVariantItems}
                  addProduct={addProduct}
                  updateInProgress ={updateInProgress}
                  loading={loading}
                  errors={errors}
                  setErrors={setErrors}
                  purchaseOption={purchaseOption}
                  customerPortalSettingEntity={customerPortalSettingEntity}
                />
              </div>
            </Col>
          </Row>
        </div>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  loading: state.subscription.loading,
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  resetProductOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(FormWizardVar1);
