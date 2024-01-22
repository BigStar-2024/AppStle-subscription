/* This example requires Tailwind CSS v2.0+ */
import React, { Fragment, useEffect, useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { XMarkIcon } from '@heroicons/react/24/outline';
import SelectedProduct from './SelectedProduct';
import _, { forEach } from 'lodash';
import { formatPrice, getCurrentConvertedCurrencyPrice } from './Bundle.util';
import Loader from './Loader';
import './bundleCard.scss';
import SellingPlanList from "./SellingPlanList";
import { FormGroup, Input, Label } from 'reactstrap';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const products = [
  {
    id: 1,
    name: 'Throwback Hip Bag',
    href: '#',
    color: 'Salmon',
    price: '$90.00',
    quantity: 1,
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-01.jpg',
    imageAlt: 'Salmon orange fabric pouch with match zipper, gray zipper pull, and adjustable hip belt.'
  },
  {
    id: 2,
    name: 'Medium Stuff Satchel',
    href: '#',
    color: 'Blue',
    price: '$32.00',
    quantity: 1,
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg',
    imageAlt: 'Front of satchel with blue canvas body, black straps and handle, drawstring top, and front zipper pouch.'
  }
  // More products...
];

export default function BundleCart({
  open,
  setOpen,
  preSelectedProducts,
  selectedProducts,
  subscriptionBundleSettingsEntity,
  minProduct,
  maxProduct,
  addToCart,
  onProductAdd,
  onProductRemove,
  selectedSellingPlan,
  isCheckingOut,
  setIsCheckingOut,
  selectedSellingPlanId,
  getDiscountedPrice,
  subTotal,
  onProductDelete,
  minOrderAmount,
  tieredDiscount,
  subscriptionPlans,
  setShowSellingPlanScreen,
  setSelectedSellingPlan,
  checkoutBundle,
  setPropertyDetails,
  propertyDetails,
  errorMessage,
  discountedSubTotal,
  getDiscountedPerDeliveryPrice,
  renderDisabled,
  renderLimitText,
  minQtyButtonText,
  minAmountButtonText,
  setMinAmountButtonText,
  renderMinimumAmountText,
  ...props
}) {
  const [totalPriceFreeShipping] = useState(50000);
  const [eligibleDiscount, setEligibleDiscount] = useState(null);

  
  
  const [formFieldJson, setFormFieldJson] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [baseTotal, setBaseTotal] = useState(0);



  useEffect(() => {
    window.addEventListener('AppstleSubscription:BuildABox:OpenAppstleCartDrawer', () => setOpen(true));
  }, [])

  useEffect(() => {
    setPropertyDetails(null);
    if (selectedSellingPlan?.formFieldJson && subscriptionBundleSettingsEntity?.enableCustomAdvancedFields) {
      setFormFieldJson(JSON.parse(selectedSellingPlan?.formFieldJson));
    } else {
      setFormFieldJson(null);
    }
  }, [selectedSellingPlan]);

  const handleDateChange = (e) => {
    setSelectedDate(e);
  }

  const handleTextChange = (e) => {
    let data = propertyDetails || {};
    data[e.target.name] = e.target.value;
    setPropertyDetails({...data});
  }

  useEffect(()=> {
    if (formFieldJson && formFieldJson?.length > 0) {
      let dateFieldList = formFieldJson.filter(v => v.type === "date");
      let selectFieldList = formFieldJson.filter(v => v.type === "select");
      if (dateFieldList?.length > 0) {
        
        let enabledDays = [];
        if (typeof dateFieldList[0]?.enabledDays == "string") {
          enabledDays = JSON.parse(dateFieldList[0]?.enabledDays)?.map(value => Number(value.value))?.sort(); 
        } else {
          enabledDays = dateFieldList[0]?.enabledDays?.map(value => Number(value.value))?.sort(); 
        }

        let nextOrderMinimumThreshold = 0;
        if (dateFieldList[0]?.nextOrderMinimumThreshold && !isNaN(Number(dateFieldList[0]?.nextOrderMinimumThreshold))) {
          nextOrderMinimumThreshold = Number(dateFieldList[0]?.nextOrderMinimumThreshold); 
        }
        
        let currentDate = new Date();
        currentDate = new Date(currentDate.setDate(new Date().getDate() + nextOrderMinimumThreshold));
        if (enabledDays && enabledDays?.length > 0) {
          currentDate = getMatchingDate(enabledDays, currentDate);
        }
        setSelectedDate(currentDate);
      }
      if (selectFieldList?.length > 0) {
        selectFieldList.forEach(element => {
          let selectOptionList = element?.selectOptions?.split(",");
          if (selectOptionList?.length > 0) {
            let data = propertyDetails || {};
            data[element?.name] = element?.selectOptions?.split(",")[0];
            setPropertyDetails({...data}); 
          }
        });
      }
    }
  }, [formFieldJson]);

  const getMatchingDate = (dayList, currentDate) => {
    const currentDay = currentDate.getDay();
    if (dayList.includes(currentDay)) {
      return currentDate;
    } else {
      const nextMatchingDay = dayList.find(day => day > currentDay) || dayList[0];
      const daysAhead = (nextMatchingDay - currentDay + 7) % 7;
      const nextDate = new Date(currentDate.getTime() + daysAhead * 24 * 60 * 60 * 1000);
      return nextDate;
    }
  }
  
  useEffect(()=> {
    if (selectedDate) {
      let date = new Date(selectedDate?.toUTCString());
      let data = propertyDetails || {};
      data["_order-date"] = date?.toISOString();
      setPropertyDetails({...data}); 
    }
  }, [selectedDate]);

  useEffect(() => {
    let sortedDiscounts = [];
    if (tieredDiscount) {
      const applicableDiscountList = JSON.parse(tieredDiscount);
      const updatedTieredDiscounts = [];
      applicableDiscountList.forEach(item => {
        updatedTieredDiscounts.push({ ...item, quantity: parseInt(item.quantity), discount: parseInt(item.discount) });
      });
      const quantityDataFilter = _.filter(updatedTieredDiscounts, function(item) {
        return item.discountBasedOn === 'QUANTITY' && _.sumBy(selectedProducts, 'quantity') >= parseInt(item.quantity);
      });
      const eligibleQuantityDiscount = _.maxBy(quantityDataFilter, 'quantity');
      if (eligibleQuantityDiscount) {
        sortedDiscounts.push(eligibleQuantityDiscount);
      }

      const amountDataFilter = _.filter(updatedTieredDiscounts, function(item) {
        return item.discountBasedOn === 'AMOUNT' && subTotal / 100 >= parseInt(item.quantity);
      });

      const eligibleAmountDiscount = _.maxBy(amountDataFilter, 'discount');
      if (eligibleAmountDiscount) {
        sortedDiscounts.push(eligibleAmountDiscount);
      }

      if (sortedDiscounts.length > 0) {
        const appliedDiscount = _.maxBy(sortedDiscounts, 'discount');
        if (appliedDiscount != undefined && appliedDiscount !== null) {
          setEligibleDiscount(appliedDiscount);
        } else {
          setEligibleDiscount(null);
        }
      } else {
        setEligibleDiscount(null);
      }
    }
    renderLimitText();
    renderMinimumAmountText();
  }, [selectedProducts, subTotal]);


  let totalAmountSelected = 0;
  if (minOrderAmount) {
    totalAmountSelected = ((subTotal / 100 / minOrderAmount) * 100).toFixed(0);
  }

  let productSelectedRemaning = 0;
  if (minProduct) {
    productSelectedRemaning = ((_.sumBy(selectedProducts, 'quantity') / minProduct) * 100).toFixed(0);
  }

  const renderSelected = item => {
    return eligibleDiscount != undefined &&
      eligibleDiscount !== null &&
      parseInt(eligibleDiscount.quantity) === parseInt(item.quantity) &&
      parseInt(eligibleDiscount.discount) === parseInt(item.discount)
      ? 'as-text-indigo-600'
      : '';
  };

  const renderTieredDiscountText = item => {
    let text = '';
    if (item?.discountBasedOn === 'AMOUNT') {
      let amountBased = subscriptionBundleSettingsEntity?.spendAmountGetDiscount || 'Spend {{amount}} get {{percent}}% discount';
      text = amountBased.replace(`{{amount}}`,  formatPrice(getCurrentConvertedCurrencyPrice(parseInt(item.quantity) * 100)));
      text = text.replace(`{{percent}}`, item.discount);
    } else if (item?.discountBasedOn === 'QUANTITY') {
      let quantityBased = subscriptionBundleSettingsEntity?.buyQuantityGetDiscount || 'Buy {{quantity}} get {{percent}}% discount';
      text = quantityBased.replace(`{{quantity}}`, item.quantity);
      text = text.replace(`{{percent}}`, item.discount);
    }
    return text;
  };

  const getDateFormat = (formatJson) => {
    let f = JSON.parse(formatJson);
    return f?.dateFormat?.replace("mm", "MM") || "dd-MM-yyyy";
  };

  const enableDate = (current, field) => {
    if (field?.enabledDays && field?.enabledDays?.length > 0) {
      let enabledDays = [];
      if (typeof field?.enabledDays == "string") {
        enabledDays = JSON.parse(field?.enabledDays)?.map(value => Number(value.value))?.sort(); 
      } else {
        enabledDays = field?.enabledDays?.map(value => Number(value.value))?.sort(); 
      }
      return enabledDays?.includes(new Date(current).getDay());
    }
    return true;
  }

  const getMinDate = (field) =>{
    let currentDate = new Date();
    if (field?.nextOrderMinimumThreshold && !isNaN(Number(field?.nextOrderMinimumThreshold))) {
      return new Date(currentDate.setDate(new Date().getDate() + Number(field?.nextOrderMinimumThreshold)));
    }
    return currentDate;
  }

  useEffect(()=> {
    try {
      if (props?.isReviewBundleModalOpen && (props?.shopInfo?.buildBoxVersion === "V2_IFRAME") && (props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT")) {
        let rootDiv = document.getElementById("root").scrollHeight || 0;
        if (rootDiv > 0) {
          top.document.getElementById("appstle_iframe").style.height = (rootDiv + 100) + 'px'; 
        } 
        top?.window?.scrollTo({top: 0, behavior: 'smooth'}); 
      }
    } catch (error) {
     console.log(error); 
    }
  },[props?.isReviewBundleModalOpen]);

  useEffect(() => {
    setBaseTotal(0);
    if (subscriptionBundleSettingsEntity?.showCompareAtPrice) {
      const calculateSubtotal = (products) => {
        return products.reduce((total, product) => {
          const price = product?.variant?.price || 0;
          const quantity = product?.quantity || 0;
          return total + price * quantity;
        }, 0);
      };

      const subtotal = calculateSubtotal(selectedProducts);
      const preSubtotal = calculateSubtotal(preSelectedProducts);

      if (discountedSubTotal > 0 && (preSubtotal + subtotal) > discountedSubTotal) {
        setBaseTotal(preSubtotal + subtotal);
      }
    }
  }, [subscriptionBundleSettingsEntity, selectedProducts, preSelectedProducts, discountedSubTotal]);
  
  return (
    <>
      {!(props?.shopInfo?.buildBoxVersion === "V2_IFRAME") && <Transition.Root show={open} as={Fragment}>
        <Dialog as="div" className="as-relative as-z-10" onClose={setOpen}>
          <Transition.Child
            as={Fragment}
            enter="as-ease-in-out as-duration-500"
            enterFrom="as-opacity-0"
            enterTo="as-opacity-100"
            leave="as-ease-in-out as-duration-500"
            leaveFrom="as-opacity-100"
            leaveTo="as-opacity-0"
          >
            <div className="as-fixed as-inset-0 as-bg-gray-500 as-bg-opacity-75 as-transition-opacity" />
          </Transition.Child>

          <div className="as-fixed as-inset-0 as-overflow-hidden">
            <div className="as-absolute as-inset-0 as-overflow-hidden">
              <div className="as-pointer-events-none as-fixed as-inset-y-0 as-right-0 as-flex as-max-w-full as-pl-10">
                <Transition.Child
                  as={Fragment}
                  enter="as-transform as-transition as-ease-in-out as-duration-500 sm:as-duration-700"
                  enterFrom="as-translate-x-full"
                  enterTo="as-translate-x-0"
                  leave="as-transform as-transition as-ease-in-out as-duration-500 sm:as-duration-700"
                  leaveFrom="as-translate-x-0"
                  leaveTo="as-translate-x-full"
                >
                  <Dialog.Panel className="as-pointer-events-auto as-w-screen as-max-w-md">
                    <div className="as-flex as-h-full as-flex-col as-overflow-y-scroll as-bg-white as-shadow-xl">
                      <div className="as-flex-1 as-overflow-y-auto as-py-6 as-px-4 sm:as-px-6">
                        <div className="as-flex as-items-start as-justify-between">
                          <Dialog.Title className="as-text-lg as-font-medium as-text-gray-900">
                            {subscriptionBundleSettingsEntity?.shoppingCart || 'Shopping cart'}
                          </Dialog.Title>
                          <div className="as-ml-3 as-flex h-7 as-items-center">
                            <button
                              type="button"
                              className="-as-m-2 as-p-2 as-text-gray-400 hover:as-text-gray-500"
                              onClick={() => setOpen(false)}
                            >
                              <span className="as-sr-only">Close panel</span>
                              <XMarkIcon className="as-h-6 as-w-6" aria-hidden="true" />
                            </button>
                          </div>
                        </div>

                        <div className={'limit-progress as-mt-4'}>
                          {minProduct > 1 ? (
                            <div className={`as-my-5`}>
                              <p
                                className={`as-mt-1 as-mb-1 as-text-center as-text-xs ${
                                  _.sumBy(selectedProducts, 'quantity') > maxProduct ? 'as-text-red-700' : 'as-text-gray-500'
                                }`}
                              >
                                {minQtyButtonText}
                              </p>
                              <div className="as-w-full as-bg-gray-200 as-rounded-full as-overflow-hidden">
                                <div
                                  className="as-bg-indigo-600 as-text-xs as-font-medium as-text-blue-100 as-text-center as-leading-none as-rounded-l-full"
                                  style={{ width: `${productSelectedRemaning > 100 ? 100 : productSelectedRemaning}%` }}
                                >
                                  {_.sumBy(selectedProducts, 'quantity') > maxProduct
                                    ? 'Limit Exceed'
                                    : `${productSelectedRemaning > 100 ? 100 : productSelectedRemaning}%`}
                                </div>
                              </div>
                            </div>
                          ) : (
                            ''
                          )}
                          {minOrderAmount > 1 ? (
                            <div className={`as-my-5`}>
                              <p className={`as-mt-1 as-mb-1 as-text-center as-text-xs as-text-gray-500`}>{minAmountButtonText}</p>
                              <div className="as-w-full as-bg-gray-200 as-rounded-full as-overflow-hidden">
                                <div
                                  className="as-bg-indigo-600 as-text-xs as-font-medium as-text-blue-100 as-text-center as-leading-none as-rounded-l-full"
                                  style={{ width: `${totalAmountSelected > 100 ? 100 : totalAmountSelected}%` }}
                                >
                                  {totalAmountSelected > 100 ? 100 : totalAmountSelected}%
                                </div>
                              </div>
                            </div>
                          ) : (
                            ''
                          )}
                        </div>

                        {tieredDiscount && JSON.parse(tieredDiscount)?.length ? (
                          <div>
                            <table className="as-w-full as-text-sm as-text-left as-text-gray-500 as-bundle-discount-table">
                              <thead className="as-text-xs as-text-gray-700 as-uppercase as-bg-gray-50 as-bundle-discount-table-head  as-border">
                                <tr>
                                  <th className="as-px-4 as-py-2 as-font-extrabold">
                                    {subscriptionBundleSettingsEntity?.tieredDiscount || 'Tiered Discount'}
                                  </th>
                                  {/* <th className="as-px-6 as-py-3">Discount(%)</th> */}
                                </tr>
                              </thead>
                              <tbody>
                                {tieredDiscount != null && tieredDiscount.length > 0
                                  ? JSON.parse(tieredDiscount).map((item, index) => (
                                      <tr key={index} className="as-bg-white as-border-b hover:as-bg-gray-50 as-border-l as-border-r">
                                        <td className={`as-px-4 as-py-2 as-text-xs ${renderSelected(item)}`}>
                                          {renderTieredDiscountText(item)}
                                        </td>
                                      </tr>
                                    ))
                                  : ''}
                              </tbody>
                            </table>
                          </div>
                        ) : (
                          ''
                        )}
                        {/*{
                          <div className='custom-progress-bar'>
                            {((subTotal / 100) / totalPriceFreeShipping) >= 100 ?
                              <div className='bar-text'>You got a <b>FREE SHIPPING</b> and a <b>FREE GIFT!</b></div> :
                              <div className='bar-text'>You
                                are <b>€{Math.trunc(((Math.abs(((subTotal * 100) / totalPriceFreeShipping) >= 100 ? 100 : ((subTotal * 100) / totalPriceFreeShipping) - 100) * totalPriceFreeShipping) / 100))}</b> away
                                from <b>FREE SHIPPING</b> and a <b>FREE GIFT!</b></div>}
                            <div class="as-w-full as-bg-gray-200 as-rounded-full as-h-2.5 as-mb-3 as-mt-3 ">
                              <div class="as-h-2.5 as-rounded-full custom-bar-line"
                                  style={{width: `${((subTotal * 100) / totalPriceFreeShipping) >= 100 ? 100 : ((subTotal * 100) / totalPriceFreeShipping)}%`}}></div>
                            </div>
                            <div className='bar-text'>Free Shipping Bar</div>
                          </div>}*/}
                        <div className="as-mt-8">
                          <div className="as-flow-root">
                            <div className="as-mb-4" style={{background: "#f3f3f3"}}>
                              <ul role="list" className="-as-my-6 as-divide-y as-divide-gray-200">
                                {preSelectedProducts?.map((each, idx) => {
                                  return (
                                    <SelectedProduct
                                      bundleData={props.bundleData}
                                      product={each.product}
                                      variant={each.variant}
                                      quantity={each.quantity}
                                      showRemoveProduct
                                      showQuantity
                                      selectedSellingPlan={selectedSellingPlan}
                                      subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                                      selectedSellingPlanId={each.selling_plan}
                                      getDiscountedPrice={getDiscountedPrice}
                                      onProductDelete={onProductDelete}
                                      eligibleDiscount={eligibleDiscount}
                                      selectedProductData={each}
                                      getDiscountedPerDeliveryPrice={getDiscountedPerDeliveryPrice}
                                    />
                                  );
                                })}
                              </ul>
                            </div>
                            <div>
                              <ul role="list" className="-as-my-6 as-divide-y as-divide-gray-200">
                                {selectedProducts?.map((each, idx) => {
                                  return (
                                    <SelectedProduct
                                      bundleData={props.bundleData}
                                      product={each.product}
                                      variant={each.variant}
                                      quantity={each.quantity}
                                      onProductAdd={onProductAdd}
                                      showRemoveProduct
                                      showQuantity
                                      onProductRemove={onProductRemove}
                                      selectedSellingPlan={selectedSellingPlan}
                                      subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                                      selectedSellingPlanId={each.selling_plan}
                                      getDiscountedPrice={getDiscountedPrice}
                                      onProductDelete={onProductDelete}
                                      eligibleDiscount={eligibleDiscount}
                                      selectedProductData={each}
                                      getDiscountedPerDeliveryPrice={getDiscountedPerDeliveryPrice}
                                    />
                                  );
                                })}
                            </ul>
                            </div>
                          </div>
                        </div>
                      </div>

                      <div className="as-border-t as-border-gray-200 as-py-6 as-px-4 sm:as-px-6">
                        {(formFieldJson && formFieldJson.length > 0) ? 
                          formFieldJson.map(field => 
                            <div className='as-mb-6' style={{width: "100%"}}>
                              <Label className={'as-block as-text-sm as-font-medium as-text-gray-700'}>{field?.label}</Label>
                              {field?.type == "select" && 
                                <FormGroup>
                                  <select name={field.name} className='inputText' onChange={(e) => handleTextChange(e, field)}>
                                    {field?.selectOptions?.split(",")?.map(value => {
                                      return (<option key={value} value={value}>{value}</option>);
                                    })} 
                                  </select>
                                </FormGroup>
                              }
                              {field?.type == "date" && <FormGroup><DatePicker filterDate={(current) => enableDate(current, field)} selected={selectedDate} dateFormat={getDateFormat(field?.config)} onChange={(e) => handleDateChange(e)} minDate={getMinDate(field)} /></FormGroup>}
                              {field?.type == "text" && <FormGroup><Input name={field.name} onChange={(e) => handleTextChange(e, field)} className="inputText"/></FormGroup> }
                            </div>
                          ) : null
                        }
                        
                        <div className="as-mb-6">
                          <SellingPlanList
                            subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                            subscriptionPlans={subscriptionPlans}
                            setShowSellingPlanScreen={setShowSellingPlanScreen}
                            selected={selectedSellingPlan}
                            setSelected={setSelectedSellingPlan}
                            hideLabel={false}
                            // selectedSortProduct={selectedSortProduct}
                            // setSelectedSortProduct={setSelectedSortProduct}
                          />
                        </div>

                        <div className="as-flex as-justify-between as-text-base as-font-medium as-text-gray-900">
                          <p>{subscriptionBundleSettingsEntity?.subtotal || 'Subtotal'}</p>
                          <p className="transcy-money">
                          {
                            subscriptionBundleSettingsEntity?.productPriceFormatField ?
                              subscriptionBundleSettingsEntity.productPriceFormatField.replace(`{{price}}`, formatPrice(getCurrentConvertedCurrencyPrice(discountedSubTotal)))
                              : formatPrice(getCurrentConvertedCurrencyPrice(discountedSubTotal))
                          }
                          </p>
                        </div>
                        <p className="as-mt-0.5 as-text-sm as-text-gray-500">
                          {subscriptionBundleSettingsEntity?.checkoutMessage || 'Shipping and taxes calculated at checkout.'}
                        </p>
                        {errorMessage ? <p className='as-text-red-800 as-my-2 as-text-sm'>{errorMessage}</p> : ''}
                        <button
                          type="button"
                          className="as-w-full as-mt-6 as-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-bg-indigo-600 as-px-6 as-py-3 as-text-base as-font-medium as-text-white as-shadow-sm hover:as-bg-indigo-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                          onClick={() => {
                            addToCart();
                          }}
                          disabled={renderDisabled()}
                        >
                          {isCheckingOut ? (
                            <Loader />
                          ) : (
                            `${minQtyButtonText || minAmountButtonText || props.bundleData?.bundle?.proceedToCheckoutButtonText || subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || 'Proceed to checkout'}`
                          )}
                        </button>
                        <div className="as-mt-6 as-flex as-justify-center as-text-center as-text-sm as-text-gray-500">
                          <p>
                            or{' '}
                            <button
                              type="button"
                              className="as-font-medium as-text-indigo-600 hover:as-text-indigo-500"
                              onClick={() => setOpen(false)}
                            >
                              {subscriptionBundleSettingsEntity?.continueShopping || 'Continue Shopping'}
                              <span aria-hidden="true"> &rarr;</span>
                            </button>
                          </p>
                        </div>
                      </div>
                    </div>
                  </Dialog.Panel>
                </Transition.Child>
              </div>
            </div>
          </div>
        </Dialog>
      </Transition.Root>}
      {(props?.isReviewBundleModalOpen && (props?.shopInfo?.buildBoxVersion === "V2_IFRAME") && (props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT")) && <div className="as-container as-mx-auto as-px-4">
              <div className="as-review-wrapper as-py-4">
                <div className="as-review-title-wrapper">
                  <h2 class="as-text-2xl as-mb-3">{subscriptionBundleSettingsEntity?.reviewOrderTextV2 || "Review Order"}</h2>
                </div>
                <div className="">
                  <p className="as-review-description as-text-sm as-mb-6">
                  {subscriptionBundleSettingsEntity?.reviewOrdeDescriptionTextV2 || "Check your items and select your required frequency plan"}

                  </p>
                </div>
                <div className="as-grid lg:as-grid-cols-2 as-gap-4">
                <div className="">
                    <h4
                      className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title as-review-bundle-content-title as-mb-6">
                        {subscriptionBundleSettingsEntity?.contentsOnModalTextV2 || "Contents"}
                    </h4>
                    <div className="as-bg-white as-p-8 as-rounded-2xl as-shadow-md">

                      <ul className="as-grid as-gap-4 as-grid-cols-1">
                        {preSelectedProducts?.map((each, idx) => {
                            return (
                              <SelectedProduct
                                bundleData={props.bundleData}
                                product={each.product}
                                variant={each.variant}
                                quantity={each.quantity}
                                onProductAdd={onProductAdd}
                                showRemoveProduct
                                showQuantity
                                onProductRemove={onProductRemove}
                                selectedSellingPlan={selectedSellingPlan}
                                subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                                selectedSellingPlanId={each.selling_plan}
                                getDiscountedPrice={getDiscountedPrice}
                                onProductDelete={onProductDelete}
                                eligibleDiscount={eligibleDiscount}
                                selectedProductData={each}
                            />);
                        })}
                        {selectedProducts?.map((each, idx) => {
                          return (
                            <SelectedProduct
                              bundleData={props.bundleData}
                              product={each.product}
                              variant={each.variant}
                              quantity={each.quantity}
                              onProductAdd={onProductAdd}
                              showRemoveProduct
                              showQuantity
                              onProductRemove={onProductRemove}
                              selectedSellingPlan={selectedSellingPlan}
                              subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                              selectedSellingPlanId={each.selling_plan}
                              getDiscountedPrice={getDiscountedPrice}
                              onProductDelete={onProductDelete}
                              eligibleDiscount={eligibleDiscount}
                              selectedProductData={each}
                            />
                          );
                        })}
                      </ul>

                    </div>

                    </div>
                  <div className="as-pl-12 as-review-items-wrapper">
                  <h4
                      className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title as-review-bundle-content-title as-mb-6">
                      {subscriptionBundleSettingsEntity?.bundleOnModalTextV2 || "Bundle"}
                    </h4>
                    <div className="as-bg-white as-p-8 as-rounded-2xl as-shadow-md">
                    {tieredDiscount && JSON.parse(tieredDiscount)?.length ? (
                          <div>
                            <table className="as-w-full as-text-sm as-text-left as-text-gray-500 as-bundle-discount-table">
                              <thead className="as-text-xs as-text-gray-700 as-uppercase as-bg-gray-50 as-bundle-discount-table-head  as-border">
                                <tr>
                                  <th className="as-px-4 as-py-2 as-font-extrabold">
                                    {subscriptionBundleSettingsEntity?.tieredDiscount || 'Tiered Discount'}
                                  </th>
                                  {/* <th className="as-px-6 as-py-3">Discount(%)</th> */}
                                </tr>
                              </thead>
                              <tbody>
                                {tieredDiscount != null && tieredDiscount.length > 0
                                  ? JSON.parse(tieredDiscount).map((item, index) => (
                                      <tr key={index} className="as-bg-white as-border-b hover:as-bg-gray-50 as-border-l as-border-r">
                                        <td className={`as-px-4 as-py-2 as-text-xs ${renderSelected(item)}`}>
                                          {renderTieredDiscountText(item)}
                                        </td>
                                      </tr>
                                    ))
                                  : ''}
                              </tbody>
                            </table>
                          </div>
                        ) : (
                          ''
                        )}
                      <div className="">
                        <SellingPlanList
                              subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                              subscriptionPlans={subscriptionPlans}
                              setShowSellingPlanScreen={setShowSellingPlanScreen}
                              selected={selectedSellingPlan}
                              setSelected={setSelectedSellingPlan}
                              hideLabel={false}
                              // selectedSortProduct={selectedSortProduct}
                              // setSelectedSortProduct={setSelectedSortProduct}
                          />
                      </div>
                      <hr class="as-my-8 as-h-px as-bg-gray-200 as-border-0"/>
                      <div>
                        {/* <h4 className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title">


                        </h4> */}
                        {/* <div className="as-flex">
                          <div
                            className="as-relative as-flex-shrink-0 as-flex as-items-center">
                            <img
                              src={props?.selectedSingleProduct?.imageSrc}
                              className="as-rounded-md as-object-cover as-object-center as-h-[72px] as-w-[72px] as-border as-border-gray-200 "
                            />
                            <div class="as-border as-border-gray-200 as-translate-x-1/2 as-translate-y-[-50%] as-text-xs as-h-6 as-w-6 as-z-1 as-flex as-review-bundle-price as-absolute as-right-0 as-top-0 as-rounded-full as-shadow-lg as-p-2 as-bg-white as-z-1 as-items-center as-justify-center">
                                            {1}
                            </div>
                          </div>
                          <div className="as-ml-4 as-flex as-flex-1 as-flex-col">
                            <h3 className="as-review-bundle-title as-text-gray-900">
                              {props?.selectedSingleProduct?.title}
                            </h3>
                            <div class="as-review-bundle-price as-mt-1">
                                      <span className="as-text-sm as-text-gray-500 as-review-bundle-sale-price">
                                        {formatPrice(selectedSellingPlan?.price)}
                                      </span>
                              {showCompareAtPrice && <span
                                className="as-text-sm as-text-gray-500 as-ml-1 as-line-through as-review-bundle-compare-price">
                                        {selectedSellingPlan?.compare_at_price && formatPrice(selectedSellingPlan?.compare_at_price)}
                                      </span>}
                            </div>

                          </div>
                        </div>
                        <hr class="as-my-8 as-h-px as-bg-gray-200 as-border-0"/> */}
                        <div className="as-flex as-justify-between as-text-base as-font-medium as-text-gray-900">
                          <p>{subscriptionBundleSettingsEntity?.subtotal || 'Subtotal'}</p>
                          <div className="as-flex">
                          {subscriptionBundleSettingsEntity?.showCompareAtPrice && baseTotal > 0 && (
                            <p className="as-line-through as-mr-2 as-text-sm as-text-gray-500 transcy-money as-bab-base-total">
                              {subscriptionBundleSettingsEntity?.productPriceFormatField
                                ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                                    `{{price}}`,
                                    formatPrice(getCurrentConvertedCurrencyPrice(baseTotal)),
                                  )
                                : formatPrice(getCurrentConvertedCurrencyPrice(baseTotal))}
                            </p>
                          )}
                          <p className="transcy-money as-text-sm">
                            {subscriptionBundleSettingsEntity?.productPriceFormatField
                              ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                                  `{{price}}`,
                                  formatPrice(getCurrentConvertedCurrencyPrice(discountedSubTotal)),
                                )
                              : formatPrice(getCurrentConvertedCurrencyPrice(discountedSubTotal))}
                          </p>
                          </div>
                        </div>
                        <p className="as-mt-0.5 as-text-sm as-text-gray-500">
                          {subscriptionBundleSettingsEntity?.checkoutMessage || 'Shipping and taxes calculated at checkout.'}
                        </p>
                        {errorMessage ? <p className='as-text-red-800 as-my-2 as-text-sm'>{errorMessage}</p> : ''}
                        <button
                          type="button"
                          className="as-w-full as-mt-6 as-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-bg-indigo-600 as-px-6 as-py-3 as-text-base as-font-medium as-text-white as-shadow-sm hover:as-bg-indigo-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                          onClick={() => {
                            checkoutBundle();
                          }}
                          disabled={renderDisabled()}
                        >
                          {isCheckingOut ? (
                            <Loader />
                          ) : (
                            `${minQtyButtonText || minAmountButtonText || props.bundleData?.bundle?.proceedToCheckoutButtonText || subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || 'Proceed to checkout'}`
                          )}
                        </button>
                        {/* <div className="as-mt-6 as-flex as-justify-center as-text-center as-text-sm as-text-gray-500">
                          <p>
                            or{' '}
                            <button
                              type="button"
                              className="as-font-medium as-text-indigo-600 hover:as-text-indigo-500"
                              onClick={() => setOpen(false)}
                            >
                              {subscriptionBundleSettingsEntity?.continueShopping || 'Continue Shopping'}
                              <span aria-hidden="true"> &rarr;</span>
                            </button>
                          </p>
                        </div> */}
                      </div>
                    </div>


                  </div>

                </div>


              </div>
      </div>}
    </>
  );
}
