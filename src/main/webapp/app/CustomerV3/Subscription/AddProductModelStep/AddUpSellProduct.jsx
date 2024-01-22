import React, { useEffect, useState } from 'react';
import Axios from 'axios';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { toast } from 'react-toastify';
import { resetProductOptions } from 'app/entities/fields-render/data-product.reducer';
import Step2 from './Step2';
import { connect } from 'react-redux';
import TailwindModal from '../TailwindModal';

import ProductDetail from '../ProductDetails';
import PopupMessaging from '../PopupMessaging';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

function AddUpSellProduct(props) {
  const {
    shopName,
    contractId,
    getCustomerPortalEntity,
    sellingPlanIds,
    upcomingOrderId,
    formStep,
    setFormStep,
    loading,
    lineId,
    resetProductOptions,
    customerPortalSettingEntity,
    subscriptionContractFreezeStatus,
    isSwapProduct,
    forcePurchaseOption
  } = props;
  const [isOpened, setIsOpened] = useState(false);
  const [updateInProgress, setUpdateInProgress] = useState(false);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [purchaseOption, setPurchaseOption] = useState(forcePurchaseOption || 'ONE_TIME');
  const [errors, setErrors] = useState([]);
  const [isProductDetailModalOpen, setIsProductDetailModalOpen] = useState(false);
  const [productData, setProductData] = useState(null);
  const [selectedItemsToAdd, setSelectedItemsToAdd] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [isQuantityInputInValid, setIsQuantityInputInValid] = useState(false);
  const [isSelectProductModalOpen, setIsSelectProductModalOpen] = useState(false);
  const [isPurchaseOptionModalOpen, setIsPurchaseOptionModalOpen] = useState(true);
  const [actionMethodText, setActionMethodText] = useState(customerPortalSettingEntity?.continueTextV2 || 'Continue');
  const [modalTitle, setModalTitle] = useState(customerPortalSettingEntity?.choosePurchaseOptionLabelTextV2 || 'Select Purchase Option');
  const [secondaryActionButtonText, setSecondaryActionButtonText] = useState('');
  const [selectedProductVariant, setSelectedProductVariant] = useState(null);

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    resetProductOptions();
  }, [purchaseOption]);

  useEffect(() => {
    setProductData(selectedVariantItems);
  }, [selectedVariantItems]);

  useEffect(() => {
    if (forcePurchaseOption) {
      setPurchaseOption(forcePurchaseOption);
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(true);
      // return;
    }
    if (customerPortalSettingEntity?.addOneTimeProduct && customerPortalSettingEntity?.addAdditionalProduct) {
      if (customerPortalSettingEntity?.selectOneTimePurchaseTabByDefaultV2 === 'true') {
        setPurchaseOption('ONE_TIME')
      } else {
        setPurchaseOption('SUBSCRIBE');
      }
    } else if (customerPortalSettingEntity?.addOneTimeProduct && !customerPortalSettingEntity?.addAdditionalProduct) {
      setPurchaseOption('ONE_TIME');
    } else if (!customerPortalSettingEntity?.addOneTimeProduct && !customerPortalSettingEntity?.addAdditionalProduct) {
      setPurchaseOption('SUBSCRIBE');
    }
  }, [customerPortalSettingEntity, forcePurchaseOption]);

  const addProduct = async (selectedItems, tempError) => {
    if (!tempError) {
      tempError = [];
    }
    setUpdateInProgress(true);
    let newSelectedItems = JSON.parse(JSON.stringify(selectedItems));
    let selectProduct = newSelectedItems?.shift();
    if (!selectProduct) {
      // props.toggle();
      setErrors([...tempError]);
      getCustomerPortalEntity(contractId);
      return;
    } else if (selectProduct && purchaseOption == 'ONE_TIME') {
      let oneTimeUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=${quantity}&variantId=${selectProduct?.id}&isOneTimeProduct=true`;

      return Axios.put(oneTimeUrl)
        .then(async res => {
          tempError.push({ success: true, item: selectProduct });
          // addProduct(newSelectedItems, tempError);
          // toast.success('Contract Updated', options);
          if (!newSelectedItems?.length) {
            if (isSwapProduct) {
              return await deleteProduct();
            } else {
              setUpdateInProgress(false);
              await getCustomerPortalEntity(contractId);
              return res;
            }
          } else {
            await getCustomerPortalEntity(contractId);
            return res;
          }
        })
        .catch(async err => {
          console.log(err);
          tempError.push({ success: false, item: selectProduct, err: err?.response?.data?.message });
          await getCustomerPortalEntity(contractId);
          // addProduct(newSelectedItems, tempError);
          return err;
          // setUpdateInProgress(false);
          // props.toggle();
          // toast.error('Contract Update Failed', options);
        });
    } else if (selectProduct && purchaseOption == 'SUBSCRIBE') {
      const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=${quantity}&variantId=${selectProduct?.id}`;
      return Axios.put(requestUrl)
        .then(async res => {
          // setUpdateInProgress(false);
          // toast.success('Contract Updated', options);
          tempError.push({ success: true, item: selectProduct });
          // addProduct(newSelectedItems, tempError);
          if (!newSelectedItems?.length) {
            if (isSwapProduct) {
              return await deleteProduct();
            } else {
              setUpdateInProgress(false);
              await getCustomerPortalEntity(contractId);
              return res;
            }
          } else {
            await getCustomerPortalEntity(contractId);
            return res;
          }
        })
        .catch(err => {
          console.log(err);
          tempError.push({ success: false, item: selectProduct, err: err?.response?.data?.message });
          // addProduct(newSelectedItems, tempError);
          return err;
          //   props.toggle();
          // setUpdateInProgress(false);
          // toast.error('Contract Update Failed', options);
        });
    }
  };

  const deleteProduct = () => {
    // setDeleteInProgress(true);
    // setHandleAlert({ show: false, data: null });
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true&removeDiscount=${true}`;
    Axios.put(requestUrl)
      .then(res => {
        getCustomerPortalEntity(contractId);
        setUpdateInProgress(false);
        setIsOpened(false);
        // setDeleteInProgress(false);

        toast.success('Product deleted from contract', options);
      })
      .catch(err => {
        console.log('Error in deleting product');
        setUpdateInProgress(false);
        getCustomerPortalEntity(contractId);
        // setDeleteInProgress(false);
        toast.error(err.response.data.message, options);
      });
  };

  const addItems = async selectedItemsToAdd => {
    setIsPurchaseOptionModalOpen(false);
    setIsSelectProductModalOpen(false);
    let results = await addProduct(selectedItemsToAdd);
    if (results) {
      setShowMessaging(true);
      if (results?.status === 200) {
        setSuccess(true);
        setError(false);
      } else {
        setSuccess(false);
        setError(true);
      }
    }
  };

  useEffect(() => {
    if (isProductDetailModalOpen) {
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(false);
      setShowMessaging(false);
    }
  }, [isProductDetailModalOpen]);

  useEffect(() => {
    getActionMethodText();
    getModalTitle();
    getSecondaryActionText();
  }, [isPurchaseOptionModalOpen, isSelectProductModalOpen, isProductDetailModalOpen]);

  const getActionMethodText = () => {
    if (isPurchaseOptionModalOpen) {
      setActionMethodText(customerPortalSettingEntity?.continueTextV2 || 'Continue');
    } else if (isSelectProductModalOpen) {
      setActionMethodText('');
    } else if (isProductDetailModalOpen) {
      if (isSwapProduct) {
        setActionMethodText(customerPortalSettingEntity?.confirmSwapText || 'Confirm Swap');
      } else {
        setActionMethodText(customerPortalSettingEntity?.confirmAddProduct || 'Confirm Add Product');
      }
    }
  };

  const getModalTitle = () => {
    if (isPurchaseOptionModalOpen) {
      setModalTitle(customerPortalSettingEntity?.choosePurchaseOptionLabelTextV2 || 'Select Purchase Option');
    } else if (isSelectProductModalOpen) {
      setModalTitle(customerPortalSettingEntity?.selectProductLabelTextV2 || 'Select Product');
    } else if (isProductDetailModalOpen) {
      if (isSwapProduct) {
        setModalTitle(customerPortalSettingEntity?.swapProductBtnTextV2 || 'Swap Product');
      } else {
        setModalTitle(customerPortalSettingEntity?.addProductButtonTextV2 || 'Add Product');
      }
    }
  };

  const getSecondaryActionText = () => {
    if (isPurchaseOptionModalOpen) {
      setSecondaryActionButtonText('');
    } else if (isSelectProductModalOpen) {
      setSecondaryActionButtonText(customerPortalSettingEntity?.chooseDifferentProductActionText || 'Choose different product action');
    } else if (isProductDetailModalOpen) {
      setSecondaryActionButtonText(customerPortalSettingEntity?.chooseDifferentProductText || 'Choose different product');
    }
  };

  const secondaryActionHandler = () => {
    if (isSelectProductModalOpen) {
      setIsSelectProductModalOpen(false);
      setIsProductDetailModalOpen(false);
      setIsPurchaseOptionModalOpen(true);
    } else if (isProductDetailModalOpen) {
      setIsProductDetailModalOpen(false);
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(true);
    }
  };

  return (
    <div className="as-mt-8">
      <Step2
        isMultiSelect={false}
        purchaseOption={purchaseOption}
        setPurchaseOption={setPurchaseOption}
        sellingPlanIds={sellingPlanIds}
        contractId={contractId}
        updateInProgress={updateInProgress}
        selectedVariantItems={setSelectedVariantItems}
        customerPortalSettingEntity={customerPortalSettingEntity}
        totalTitle={customerPortalSettingEntity?.selectProductLabelTextV2 || 'Select Source Product'}
        methodName={customerPortalSettingEntity?.saveButtonTextV2 || 'Save'}
        noProductDataMessage={
          customerPortalSettingEntity?.noProductFoundMessageV2 || 'There are no product available on given search result'
        }
        setIsProductDetailModalOpen={setIsProductDetailModalOpen}
        isSwapProduct={isSwapProduct}
        gridColumns={'3'}
        searchBarPlaceholder={
          isSwapProduct
            ? customerPortalSettingEntity?.swapProductSearchBarTextV2 || 'Search a product to swap'
            : customerPortalSettingEntity?.addProductLabelTextV2 || 'Search a product to add'
        }
        showModuleHeader={true}
        setSelectedProductVariant={setSelectedProductVariant}
      />
      <TailwindModal
        open={isProductDetailModalOpen}
        setOpen={setIsProductDetailModalOpen}
        actionMethod={() => addItems(selectedItemsToAdd)}
        actionButtonText={!showMessaging ? actionMethodText : ''}
        updatingFlag={updateInProgress}
        modalTitle={modalTitle}
        actionButtonInValid={isQuantityInputInValid || (purchaseOption === 'ONE_TIME' && productData?.requires_selling_plan)}
        fullHeightModal={isSelectProductModalOpen}
        addBodyBackground={isPurchaseOptionModalOpen || isSelectProductModalOpen}
        ignoreSubscriptionContractFreezeStatus={true}
        className="as-modal-add-up-sell-products"
        success={success}
      >
        {showMessaging && <PopupMessaging showSuccess={success} showError={error} />}
        {!showMessaging && !isPurchaseOptionModalOpen && !isSelectProductModalOpen && isProductDetailModalOpen && (
          <ProductDetail
            productData={productData}
            customerPortalSettingEntity={customerPortalSettingEntity}
            setSelectedItemsToAdd={setSelectedItemsToAdd}
            disableQuantity={false}
            showProductRadioButtons={true}
            quantity={quantity}
            setQuantity={setQuantity}
            isQuantityInputInValid={isQuantityInputInValid}
            setIsQuantityInputInValid={setIsQuantityInputInValid}
            purchaseOption={purchaseOption}
            setPurchaseOption={setPurchaseOption}
            selectedProductVariant={selectedProductVariant}
          />
        )}
      </TailwindModal>
    </div>
  );
}

const mapStateToProps = state => ({
  loading: state.subscription.loading
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  resetProductOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(AddUpSellProduct);
