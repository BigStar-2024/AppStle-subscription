/* This example requires Tailwind CSS v2.0+ */
import React, { Fragment, useEffect, useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import SelectedProduct from './SelectedProduct';
import _ from 'lodash';
import { formatPrice } from './Bundle.util';
import Loader from './Loader';
import './bundleCard.scss';
import SellingPlanList from "./SellingPlanList";

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
  ...props
}) {
  const [totalPriceFreeShipping] = useState(50000);
  const [eligibleDiscount, setEligibleDiscount] = useState(null);

  const renderDisabled = () => {
    if (minOrderAmount) {
      return (
        _.sumBy(selectedProducts, 'quantity') < minProduct ||
        subTotal / 100 < minOrderAmount ||
        _.sumBy(selectedProducts, 'quantity') > maxProduct ||
        subTotal / 100 < minOrderAmount ||
        isCheckingOut
      );
    } else {
      return _.sumBy(selectedProducts, 'quantity') < minProduct || _.sumBy(selectedProducts, 'quantity') > maxProduct || isCheckingOut;
    }
  };

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
  }, [selectedProducts, subTotal]);

  const renderMinimumAmountText = () => {
    let barText = '';
    if (minOrderAmount && subTotal / 100 < minOrderAmount) {
      let buttonLabel = `Minimum order amount {{minOrderAmount}}.`;
      barText = buttonLabel.replace('{{minOrderAmount}}', minOrderAmount);
    } else {
      // barText = "🎉 Congrats conditions fulfilled"
    }
    return barText;
  };

  const renderLimitText = () => {
    let barText = '';
    if (_.sumBy(selectedProducts, 'quantity') < minProduct) {
      let buttonLabel = subscriptionBundleSettingsEntity?.selectMinimumProductButtonText
        ? subscriptionBundleSettingsEntity.selectMinimumProductButtonText
        : `Please select minimum {{minProduct}} products `;
      barText = buttonLabel.replace(`{{minProduct}}`, minProduct);
    } else if (_.sumBy(selectedProducts, 'quantity') > maxProduct) {
      let buttonLabel = subscriptionBundleSettingsEntity?.selectMaximumProductButtonText
        ? subscriptionBundleSettingsEntity.selectMaximumProductButtonText
        : 'Please select maximum {{maxProduct}}';
      barText = buttonLabel.replace(`{{maxProduct}}`, maxProduct);
    } else {
      // barText = "🎉 Congrats conditions fulfilled"
    }
    return barText;
  };

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
      text = amountBased.replace(`{{amount}}`, item.quantity);
      text = text.replace(`{{percent}}`, item.discount);
    } else if (item?.discountBasedOn === 'QUANTITY') {
      let quantityBased = subscriptionBundleSettingsEntity?.buyQuantityGetDiscount || 'Buy {{quantity}} get {{percent}}% discount';
      text = quantityBased.replace(`{{quantity}}`, item.quantity);
      text = text.replace(`{{percent}}`, item.discount);
    }
    return text;
  };

  return (
    <div className="as-pointer-events-auto">
                  <div className="as-flex as-h-full as-flex-col as-bg-white">
                    <div className="as-flex-1">
                      <div className="">
                        <h3 className="as-text-xl as-font-medium as-text-gray-900">
                          {subscriptionBundleSettingsEntity?.shoppingCart || 'Shopping cart'}
                        </h3>
                        <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3 as-mt-3"> 
                          {/* TODO add label */}
                          {subscriptionBundleSettingsEntity?.bundleDescriptionTextV2 ||" Our bundle of all bundles, The Ultimate Superfood Bundle delivers exactly what it promises - a complete superfood solution. With 7 superfood blends each created to empower every aspect of your health and wellbeing - from immunity and gut health to energy & better focus." }
                        </p>
                      </div>
                      <div className="as-my-6">
                        <h4 className="as-text-md  as-font-medium as-text-gray-900">
                            Your Bundle Summary {/* TODO add label */}
                        </h4>
                        <div className="as-my-3">
                          {!selectedProducts?.length ? <div className="as-w-full as-bg-gray-100 as-mt-3 as-rounded-lg as-border-dashed as-border-current as-border as-flex as-justify-center as-items-center as-flex-col as-p-6">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="as-w-16 as-h-16 as-text-indigo-200">
                              <path fillRule="evenodd" d="M7.5 6v.75H5.513c-.96 0-1.764.724-1.865 1.679l-1.263 12A1.875 1.875 0 004.25 22.5h15.5a1.875 1.875 0 001.865-2.071l-1.263-12a1.875 1.875 0 00-1.865-1.679H16.5V6a4.5 4.5 0 10-9 0zM12 3a3 3 0 00-3 3v.75h6V6a3 3 0 00-3-3zm-3 8.25a3 3 0 106 0v-.75a.75.75 0 011.5 0v.75a4.5 4.5 0 11-9 0v-.75a.75.75 0 011.5 0v.75z" clipRule="evenodd" />
                            </svg>
                            <p className="as-text-center as-text-xs as-text-gray-500 as-mt-3">
                             {subscriptionBundleSettingsEntity?.bundleSummaryTextV2 || "Your bundle is empty. Please add products to your bundles to see bundle summary here." }   {/* TODO add label */}
                            </p>
                          </div> :
                          <ul role="list" className="as-list-disc marker:as-text-indigo-600 as-pl-6">
                            {
                              selectedProducts.map(item => {
                                return <li className="as-text-sm as-text-gray-500 as-my-1">{item.quantity}x {item.product.title} {`${(item?.product?.variants?.length > 1) ? (` - ${item?.variant?.title}`) : ""}`}</li>
                              })
                            }
                          </ul>}
                        </div>
                        
                      </div>

                      {/* <div className="as-mt-8">
                        <div className="as-flow-root">
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
                                  selectedSellingPlanId={selectedSellingPlanId}
                                  getDiscountedPrice={getDiscountedPrice}
                                  onProductDelete={onProductDelete}
                                />
                              );
                            })}
                          </ul>
                        </div>
                      </div> */}
                    </div>

                    <div className="as-mt-4">
                      <div className="as-mb-6 ">
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
                        <p>{formatPrice(subTotal)}</p>
                      </div>
                      <p className="as-mt-0.5 as-text-sm as-text-gray-500">
                        {subscriptionBundleSettingsEntity?.checkoutMessage || 'Shipping and taxes calculated at checkout.'}
                      </p>
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
                          `${subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText || 'Proceed to checkout'}`
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
  );
}
