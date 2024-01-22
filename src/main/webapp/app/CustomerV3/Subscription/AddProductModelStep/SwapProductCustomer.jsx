import React, { useEffect, useState } from 'react';
import Axios from 'axios';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { toast } from 'react-toastify';
import { resetProductOptions } from 'app/entities/fields-render/data-product.reducer';
import Step2 from './Step2';
import { connect, useSelector } from 'react-redux';
import TailwindModal from '../TailwindModal';

import ProductDetail from '../ProductDetails';
import TailwindRadioListGroup from './TailwindRadioListGroups';
import PopupMessaging from '../PopupMessaging';
import { checkIfPreventCancellationBeforeDays } from 'app/shared/util/customer-utils';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

function SwapProductCustomer(props) {
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
    setIsSwapProductModalOpen,
    isSwapProductModalOpen = false,
    isSwapProduct = false,
    setAddProductModalOpen,
    addProductModalOpen = false,
    resetEditProductModal,
    variantId,
  } = props;
  const [isOpened, setIsOpened] = useState(false);
  const [updateInProgress, setUpdateInProgress] = useState(false);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [purchaseOption, setPurchaseOption] = useState('ONE_TIME');
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
  const [selectedProductVariant, setSelectedProductVariant] = useState(null);
  const [secondaryActionButtonText, setSecondaryActionButtonText] = useState('');

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);

  const subscriptionEntities = useSelector(state => state.subscription.entity);

  const resetAddProductModal = () => {
    setIsSwapProductModalOpen(false);
    setAddProductModalOpen && setAddProductModalOpen(false);
  };

  useEffect(() => {
    if (isSwapProductModalOpen || addProductModalOpen) {
      setShowMessaging(false);
      setSuccess(false);
      setError(false);
      if (isSwapProduct) {
        setIsPurchaseOptionModalOpen(false);
        setIsSelectProductModalOpen(true);
      } else {
        setIsSelectProductModalOpen(false);
        setIsPurchaseOptionModalOpen(true);
      }
      setIsProductDetailModalOpen(false);
      setSelectedVariantItems([]);
      setErrors([]);
      setProductData(null);
      setSelectedItemsToAdd(null);
      setQuantity(1);
      setIsQuantityInputInValid(false);
      setSecondaryActionButtonText('');
      setPurchaseOption('SUBSCRIBE');
      if (customerPortalSettingEntity?.addAdditionalProduct && !customerPortalSettingEntity?.addOneTimeProduct) {
        setPurchaseOption('SUBSCRIBE');
        setIsSelectProductModalOpen(true);
        setIsPurchaseOptionModalOpen(false);
      }
      if (!customerPortalSettingEntity?.addAdditionalProduct && customerPortalSettingEntity?.addOneTimeProduct) {
        setPurchaseOption('ONE_TIME');
        setIsSelectProductModalOpen(true);
        setIsPurchaseOptionModalOpen(false);
      }
      setUpdateInProgress(false);
    }
  }, [isSwapProductModalOpen, addProductModalOpen]);

  useEffect(() => {
    resetProductOptions();
  }, [purchaseOption]);

  useEffect(() => {
    setProductData(selectedVariantItems);
  }, [selectedVariantItems]);

  useEffect(() => {
    if ((customerPortalSettingEntity?.addOneTimeProduct && customerPortalSettingEntity?.addAdditionalProduct) || !upcomingOrderId) {
      setPurchaseOption('SUBSCRIBE');
    } else if (customerPortalSettingEntity?.addOneTimeProduct) {
      setPurchaseOption('ONE_TIME');
    } else if (customerPortalSettingEntity?.addAdditionalProduct) {
      setPurchaseOption('SUBSCRIBE');
    }
  }, [customerPortalSettingEntity]);

  useEffect(() => {
    if (isSwapProduct) {
      setPurchaseOption('SUBSCRIBE');
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(true);
    }
  }, [isSwapProduct]);

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
    } else if (selectProduct && purchaseOption == 'SUBSCRIBE' && !isSwapProduct) {
      const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=${quantity}&variantId=${selectProduct?.id}`;
      return Axios.put(requestUrl)
        .then(async res => {
          // setUpdateInProgress(false);
          // toast.success('Contract Updated', options);
          tempError.push({ success: true, item: selectProduct });
          // addProduct(newSelectedItems, tempError);
          if (!newSelectedItems?.length) {
            if (isSwapProduct) {
              // return await deleteProduct();
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
    } else if (selectProduct && isSwapProduct) {
      console.log('swapProductCustomer -> 170');
      const requestUrl = `api/subscription-contract-details/replace-variants-v3`;

      let newVariantId = parseInt(selectProduct?.id?.split('/').pop());
      const requestBody = {
        shop: shopName,
        contractId: contractId,
        oldVariants: [parseInt(variantId)],
        newVariants: {
          [newVariantId]: quantity
        },
        oldLineId: lineId,
      };
      console.log("Selected variant", selectProduct);

      return Axios.post(requestUrl, requestBody)
        .then(async res => {
          tempError.push({ success: true, item: selectProduct });
          setUpdateInProgress(false);
          setIsOpened(false);

          toast.success('Product swapped successfully', options);
          await getCustomerPortalEntity(contractId);
          return res;
        })
        .catch(err => {
          console.log('Error in swapping line items');
          setUpdateInProgress(false);
          getCustomerPortalEntity(contractId);
          toast.error(err.response.data.message, options);
          return err;
        });
    }
  };

  const deleteProduct = async () => {
    // setDeleteInProgress(true);
    // setHandleAlert({ show: false, data: null });
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true&removeDiscount=${true}`;
    return Axios.put(requestUrl)
      .then(async res => {
        getCustomerPortalEntity(contractId);
        setUpdateInProgress(false);
        setIsOpened(false);
        // setDeleteInProgress(false);

        toast.success('Product deleted from contract', options);
        await getCustomerPortalEntity(contractId);
        return res;
      })
      .catch(err => {
        console.log('Error in deleting product');
        setUpdateInProgress(false);
        getCustomerPortalEntity(contractId);
        // setDeleteInProgress(false);
        toast.error(err.response.data.message, options);
        return err;
      });
  };

  const swapProductModalActionHandler = async () => {
    if (isPurchaseOptionModalOpen) {
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(true);
    } else if (isProductDetailModalOpen) {
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
    }
  };

  useEffect(() => {
    if (isProductDetailModalOpen) {
      setIsPurchaseOptionModalOpen(false);
      setIsSelectProductModalOpen(false);
    }
  }, [isProductDetailModalOpen]);

  useEffect(() => {
    getActionMethodText();
    getModalTitle();
    getSecondaryActionText();
  }, [isPurchaseOptionModalOpen, isSelectProductModalOpen, isProductDetailModalOpen]);

  useEffect(() => {
    if (isSwapProductModalOpen || addProductModalOpen) {
      getActionMethodText();
      getModalTitle();
    }
  }, [isSwapProductModalOpen, addProductModalOpen]);

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
      if (!isSwapProduct) {
        setSecondaryActionButtonText(customerPortalSettingEntity?.chooseDifferentProductActionText || 'Choose different product action');
      } else {
        setSecondaryActionButtonText('');
      }
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
    <>
      <TailwindModal
        open={isSwapProductModalOpen || addProductModalOpen}
        setOpen={resetAddProductModal}
        actionMethod={swapProductModalActionHandler}
        actionButtonText={!showMessaging ? actionMethodText : ''}
        updatingFlag={updateInProgress}
        modalTitle={modalTitle}
        secondaryActionMethod={secondaryActionHandler}
        actionButtonInValid={isQuantityInputInValid}
        fullHeightModal={isSelectProductModalOpen}
        secondaryActionButtonText={
          !showMessaging &&
          !checkIfPreventCancellationBeforeDays(
            customerPortalSettingEntity?.preventCancellationBeforeDays,
            subscriptionEntities?.nextBillingDate
          )
            ? secondaryActionButtonText
            : ''
        }
        addBodyBackground={isPurchaseOptionModalOpen || isSelectProductModalOpen}
        ignoreSubscriptionContractFreezeStatus={!isSwapProduct ? true : false}
        className={`as-modal-swap-product ${
          !isPurchaseOptionModalOpen && !isSelectProductModalOpen && isProductDetailModalOpen ? `as-modal-swap-product-detail` : ``
        }`}
        success={success}
      >
        {showMessaging && <PopupMessaging showSuccess={success} showError={error} />}
        {!showMessaging && (
          <>
            {!isSwapProduct && !isProductDetailModalOpen && !isSelectProductModalOpen && isPurchaseOptionModalOpen && (
              <TailwindRadioListGroup
                purchaseOption={purchaseOption}
                setPurchaseOption={setPurchaseOption}
                customerPortalSettingEntity={customerPortalSettingEntity}
              />
            )}
            {!isProductDetailModalOpen &&
              !isPurchaseOptionModalOpen &&
              isSelectProductModalOpen &&
              (isSwapProductModalOpen || addProductModalOpen) && (
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
                  searchBarPlaceholder={
                    isSwapProduct
                      ? customerPortalSettingEntity?.swapProductSearchBarTextV2 || 'Search a product to swap'
                      : customerPortalSettingEntity?.addProductLabelTextV2 || 'Search a product to add'
                  }
                  isSwapProductModalOpen={isSwapProductModalOpen}
                  addProductModalOpen={addProductModalOpen}
                  setSelectedProductVariant={setSelectedProductVariant}
                />
              )}
            {!isPurchaseOptionModalOpen && !isSelectProductModalOpen && isProductDetailModalOpen && (
              <ProductDetail
                productData={productData}
                customerPortalSettingEntity={customerPortalSettingEntity}
                setSelectedItemsToAdd={setSelectedItemsToAdd}
                disableQuantity={false}
                quantity={quantity}
                showProductRadioButtons={false}
                setQuantity={setQuantity}
                isQuantityInputInValid={isQuantityInputInValid}
                setIsQuantityInputInValid={setIsQuantityInputInValid}
                purchaseOption={purchaseOption}
                setPurchaseOption={setPurchaseOption}
                selectedProductVariant={selectedProductVariant}
                oldQuantity={props?.oldProductDetail?.quantity}
                isSwapProduct={isSwapProduct}
              />
            )}
          </>
        )}
      </TailwindModal>
    </>
  );
}

const mapStateToProps = state => ({
  loading: state.subscription.loading
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  resetProductOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(SwapProductCustomer);
