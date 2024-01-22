import React, {useEffect, useState} from 'react';
import Product from './Product';
import _ from 'lodash';
import {toast} from 'react-toastify';
import './style.scss';
import axios from 'axios';
import BundleCart from './BundleCartNew';
import SellingPlanList from './SellingPlanList';
import {ArrowRightAltOutlined} from '@mui/icons-material';
import BundleHeader from './BundleHeader';
import { formatPrice } from "./Bundle.util";

function ProductSelection(props) {
  const [products, setProducts] = useState(props.products);
  const [subscriptionBundleSettingsEntity, setSubscriptionBundleSettingsEntity] = useState(props.subscriptionBundleSettingsEntity);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [subscriptionPlans, setSubscriptionPlans] = useState([]);
  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [selectedSellingPlanDisplayName, setSelectedSellingPlanDisplayName] = useState();
  const [cartSliderOpen, setCartSliderOpen] = useState(false);
  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [maxProductCountInValid, setMaxProductCountInValid] = useState(false);
  const [error, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [cartOpen, setCartOpen] = useState(false);
  const [showSellingPlanScreen, setShowSellingPlanScreen] = useState(false);
  const [selectedSellingPlanId, setSelectedSellingPlanId] = useState(null);
  const [subTotal, setSubstotal] = useState(0);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [selectedSortProduct, setSelectedSortProduct] = useState(null);
  const [isCartOpened, setIsCartOpened] = useState(false);

  const [tieredDiscount, setTieredDiscount] = useState(props?.bundleData?.bundle?.tieredDiscount ? JSON.parse(props?.bundleData?.bundle?.tieredDiscount) : []);

  const [totalDiscountForProgressBar, setTotalDiscountForProgressBar] = useState(0);
  const [discounts, setDiscounts] = useState([]);
  const [upcomingDiscountText, setUpcomingDiscountText] = useState([])

  useEffect(() => {
    setTotalDiscountForProgressBar(tieredDiscount.reduce((prev, curr) => prev + curr?.quantity, 0));
    let discounts = tieredDiscount.map(item => ({discount: `${item?.discount}%`, discountType: 'PERCENTAGE', isActive: isDiscountTierActive(item), discountText: renderTieredDiscountText(item)}));
    if (props.bundleData.bundle.minProductCount) {
      discounts.unshift({discount: 'CHECKOUT', discountType: 'CHECKOUT', isActive: isDiscountTierActive({discountBasedOn: 'QUANTITY', quantity: props.bundleData.bundle.minProductCount}), discountText: renderTieredDiscountText({discountBasedOn: 'QUANTITY', quantity: props.bundleData.bundle.minProductCount, isMinOrderQuantity: true})})
    } else if (props.bundleData.bundle.maxProductCount) {
      discounts.unshift({discount: 'CHECKOUT', discountType: 'CHECKOUT', isActive: isDiscountTierActive({discountBasedOn: 'QUANTITY', quantity: props.bundleData.bundle.maxProductCount}), discountText: renderTieredDiscountText({discountBasedOn: 'QUANTITY', quantity: props.bundleData.bundle.maxProductCount, isMaxOrderQuantity: true})})
    } else if (props.bundleData.bundle.minOrderAmount) {
      discounts.push({discount: 'CHECKOUT', discountType: 'CHECKOUT', isActive: isDiscountTierActive({discountBasedOn: 'AMOUNT', quantity: props.bundleData.bundle.minOrderAmount}), discountText: renderTieredDiscountText({discountBasedOn: 'AMOUNT', quantity: props.bundleData.bundle.minOrderAmount, isMinOrderAmount: true})})
    }
    setDiscounts(discounts);
    setUpcomingDiscountText(getCurrentProgressBarText(discounts));
  }, [tieredDiscount, selectedProducts]);

  const isDiscountTierActive = (item) => {
    if (item?.discountBasedOn === 'AMOUNT') {
      return item?.quantity <= subTotal;
    } else if (item?.discountBasedOn === 'QUANTITY') {
      return item?.quantity <= (_.sumBy(selectedProducts, 'quantity'))
    }
  }

  const getCurrentProgressBarText = (discounts) => {
    let upcomingDiscounts = discounts.filter(item => !item?.isActive);
    let upcomingDiscount = upcomingDiscounts.shift();
    let upcomingValidDiscounts = upcomingDiscounts.filter(item => upcomingDiscount?.quantity === item?.quantity);
    if (upcomingValidDiscounts && upcomingValidDiscounts.length) {
      upcomingValidDiscounts.unshift(upcomingDiscount);
      return upcomingValidDiscounts.reduce((prev, curr) => {
        if (!prev) {
          return curr.discountText;
        } else {
          return prev + ' and ' + curr.discountText;
        }
      }, '')
    } else {
      return "🎉 Congrats, All rewards unlocked."
    }

  }


  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  useEffect(() => {
    if (props.bundleData?.subscription?.infoJson) {
      setSubscriptionPlans(JSON.parse(props.bundleData?.subscription?.infoJson)?.subscriptionPlans);
    }
  }, [props.bundleData]);

  useEffect(() => {
    if (cartOpen && !isCartOpened) {
      setIsCartOpened(true);
    }
  }, [cartOpen])

  useEffect(() => {
    if (props.subscriptionBundleSettingsEntity) {
      setSubscriptionBundleSettingsEntity(props.subscriptionBundleSettingsEntity);
    }
  }, [props.subscriptionBundleSettingsEntity]);

  useEffect(() => {
    if (!selectedSellingPlan) {
      setSelectedSellingPlan(subscriptionPlans[0]);
    }
  }, [subscriptionPlans]);

  useEffect(() => {
    setSelectedSellingPlanDisplayName(
      subscriptionPlans.filter(item => item?.id?.split('/')?.pop() === selectedSellingPlan)?.pop()?.frequencyName
    );

    setSelectedSellingPlanId(selectedSellingPlan?.id.split('/').pop());
  }, [selectedSellingPlan]);

  const getDiscountedPrice = price => {
    if (props?.bundleData.bundle.discount && Number(props?.bundleData.bundle.minProductCount) > 0) {
      return Math.round(((100 - Number(props?.bundleData.bundle.discount)) / 100) * price);
    } else {
      return price;
    }
  };

  const addToCart = async () => {
    setIsCheckingOut(true);
    if (props.bundleData?.bundle?.tieredDiscount && JSON.parse(props.bundleData?.bundle?.tieredDiscount).length) {
      fetch(`${location.origin}/cart/clear.js`, {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(res => {
          checkoutBundle(res);
        })
        .catch(err => {
          setErrorMessage(err?.description);
          setError(true);
          setIsCheckingOut(false);
        });
    } else {
      checkoutBundle({});
    }
  };

  let checkoutBundle = res => {
    let addPayload = {items: []};
    if (selectedProducts.length) {
      selectedProducts.forEach(item => {
        var payloadData = {
          id: item?.variant?.id,
          quantity: item?.quantity,
          selling_plan: selectedSellingPlanId
        };
        addPayload?.items?.push(payloadData);
      });
      let config = {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(addPayload)
      };
      fetch(`${location.origin}/cart/add.js`, config)
        .then(res => res.json())
        .catch(err => {
          setErrorMessage(err?.description);
          setError(true);
          setIsCheckingOut(false);
        })
        .then(data => {
          if (data?.items?.length) {
            fetch(`${location.origin}/cart.js`)
              .then(res => res.json())
              .then(async data => {
                await axios
                  .put(`https://subscription-admin.appstle.com/api/subscription-bundlings/discount/${props?.bundleSlug}`, {cart: data})
                  .then(res => {
                    console.log(res.data);
                    var bundleCheckoutData = res.data;
                    var discountCode = bundleCheckoutData.discountCode;
                    var redirectType = props.bundleData?.bundle?.bundleRedirect;
                    var redirectURL = props.bundleData?.bundle?.customRedirectURL;
                    if (redirectType === 'CART' || Shopify?.shop === '222meals.myshopify.com') {
                      if (discountCode === 'appstle_no_discount') {
                        window.location.href = location.origin + '/cart';
                      } else {
                        window.location.href = location.origin + '/discount/' + discountCode + '?redirect=/cart';
                      }
                    } else if (redirectType === 'CHECKOUT') {
                      if (discountCode === 'appstle_no_discount') {
                        window.location.href = location.origin + '/checkout';
                      } else {
                        window.location.href = location.origin + '/discount/' + discountCode + '?redirect=/checkout';
                      }
                    } else if (redirectType === 'CUSTOM') {
                      if (discountCode === 'appstle_no_discount') {
                        window.location.href = redirectURL;
                      } else {
                        fetch(location.origin + '/discount/' + discountCode)
                          .then(function (res) {
                            window.location.href = redirectURL;
                          })
                          .catch(function (err) {
                            window.location.href = redirectURL;
                          });
                      }
                    }
                    // window?.parent?.postMessage(JSON.stringify({
                    //   type: "appstle_message_to_redirect_to_checkout",
                    //   discountCode: res?.data?.discountCode || "appstle_no_discount",
                    //   redirectType: props.bundleData?.bundle?.bundleRedirect,
                    //   redirectURL: props.bundleData?.bundle?.customRedirectURL
                    // }));
                  })
                  .catch(err => {
                    console.log(err);
                    setIsCheckingOut(false);
                    setErrorMessage(err?.description);
                    setError(true);
                  });
              });
          } else if (data?.status && data?.status !== 200) {
            setErrorMessage(data?.description);
            setError(true);
            setIsCheckingOut(false);
          }
        })
        .catch(err => {
          console.log(err);
          setIsCheckingOut(false);
          setErrorMessage(err?.description);
          setError(true);
        });
    } else {
      window?.parent?.postMessage(
        JSON.stringify({
          type: 'appstle_message_to_redirect_to_checkout',
          discountCode: 'appstle_no_discount',
          redirectType: props.bundleData?.bundle?.bundleRedirect,
          redirectURL: props.bundleData?.bundle?.customRedirectURL
        })
      );
    }
  };

  const minProduct = props.bundleData.bundle.minProductCount || 0;
  const maxProduct = props.bundleData.bundle.maxProductCount || 99999999999999999999;
  const minOrderAmount = props.bundleData.bundle.minOrderAmount;


  const onProductAdd = (variant, product, quantity) => {
    let newSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    let idx = _.findIndex(newSelectedProducts, o => o.variant.id == variant.id);
    quantity = Number(quantity);
    if (idx === -1) {
      newSelectedProducts = [
        ...newSelectedProducts,
        {
          product: {...product},
          variant: {...variant},
          quantity: quantity
        }
      ];
    } else {
      newSelectedProducts[idx].quantity += quantity;
    }
    let count = _.sumBy(newSelectedProducts, 'quantity');
    setSelectedProducts(newSelectedProducts);
    // if (count > maxProduct) {
    //   setMaxProductCountInValid(true);
    //   return;
    // } else {
    //   setSelectedProducts(newSelectedProducts);
    // }
    // toast.success("Added to Cart", options);
  };
  const onProductRemove = (variant, product, quantity) => {
    let oldSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    let idx = _.findIndex(oldSelectedProducts, o => o.variant.id == variant.id);
    if (oldSelectedProducts[idx].quantity > 1) {
      oldSelectedProducts[idx].quantity -= 1;
      setSelectedProducts(oldSelectedProducts);
    } else {
      setSelectedProducts(oldProducts => {
        return _.filter(oldProducts, o => {
          return o.variant.id !== variant.id;
        });
      });
    }
  };

  const onProductDelete = variant => {
    let oldSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    setSelectedProducts(oldProducts => {
      return _.filter(oldProducts, o => {
        return o.variant.id !== variant.id;
      });
    });
  };
  const onVariantChange = (product, variant) => {
    product.currentVariant = variant;
    setProducts(oldProducts => {
      return _.map(oldProducts, o => {
        if (o.product.id == product.id) {
          return {product};
        } else {
          return o;
        }
      });
    });
  };
  const onDragStart = (event, item) => {
    event.dataTransfer.setData('productId', item.product.id);
  };
  const onDragOver = event => {
    event.preventDefault();
  };
  const onDrop = (event, dropable) => {
    let productId = event.dataTransfer.getData('productId');
    if (dropable && productId) {
      let productInfo = _.find(products, o => o.product.id == productId);
      if (productInfo) {
        onProductAdd(productInfo.product, productInfo.product.currentVariant);
      }
    }
  };

  useEffect(() => {
    if (selectedSellingPlanId) {
      let prices = selectedProducts.map(item => {
        return (
          getDiscountedPrice(
            item?.variant?.selling_plan_allocations
              ?.filter(sellingPlan => {
                return sellingPlan?.selling_plan_id == Number(selectedSellingPlanId);
              })
              .pop()?.per_delivery_price
          ) * Number(item?.quantity)
        );
      });
      setSubstotal(prices.reduce((a, b) => a + b, 0));
    }
  }, [selectedProducts]);


  useEffect(() => {
    if (selectedSortProduct?.value === "alphabeticallyAZ") {
      const filtered = _.orderBy(products, [function (o) {
        return o?.product?.title;
      }], ["asc"])
      setFilteredProducts(filtered)
    } else if (selectedSortProduct?.value === "alphabeticallyZA") {
      const filtered = _.orderBy(products, [function (o) {
        return o?.product?.title;
      }], ["desc"])
      setFilteredProducts(filtered)
    } else if (selectedSortProduct?.value === "lowToHigh") {
      const filtered = _.orderBy(products, [function (o) {
        return o?.product?.price;
      }], ["asc"])
      setFilteredProducts(filtered)
    } else if (selectedSortProduct?.value === "highToLow") {
      const filtered = _.orderBy(products, [function (o) {
        return o?.product?.price;
      }], ["desc"])
      setFilteredProducts(filtered)
    } else {
      setFilteredProducts(products)
    }
  }, [products, selectedSortProduct])

  const renderTieredDiscountText = item => {
    let text = '';
    if (item?.isMinOrderAmount) {
      let quantityBased = subscriptionBundleSettingsEntity?.minimumToCheckoutV2 || 'Minimum {{quantity}} to checkout';
      text = quantityBased.replace(`{{quantity}}`, item.quantity);
      // return `Minimum ${formatPrice(item?.quantity)} to checkout`; // Labels to be created
    }

    if (item?.isMinOrderQuantity) {
      let quantityBased = subscriptionBundleSettingsEntity?.minimumQuantityToCheckoutV2 || 'Minimum {{quantity}} quantity to checkout';
      text = quantityBased.replace(`{{quantity}}`, item.quantity);
      // return `Minimum ${(item?.quantity)} quantity to checkout`; // Labels to be created
    }

    if (item?.isMaxOrderQuantity) {
      let quantityBased = subscriptionBundleSettingsEntity?.maxiumQuantityToCheckoutV2 || 'Maxium {{quantity}} quantity to checkout';
      text = quantityBased.replace(`{{quantity}}`, item.quantity);
      // return `Maximum ${(item?.quantity)} quantity allowed to checkout`; // Labels to be created
    }
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

console.log(subscriptionBundleSettingsEntity,'subscriptionBundleSettingsEntity')
  return (
    <>
      <div className={`${cartSliderOpen && 'appstle_slider_open'}`}>
        <div className="">
          {showSellingPlanScreen && (
            <div className="as-h-screen as-flex as-items-center as-justify-center as-bg-gray-100">
              <div className="as-w-96">
                <SellingPlanList
                  subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                  subscriptionPlans={subscriptionPlans}
                  setShowSellingPlanScreen={setShowSellingPlanScreen}
                  selected={selectedSellingPlan}
                  setSelected={setSelectedSellingPlan}
                  selectedSortProduct={selectedSortProduct}
                  setSelectedSortProduct={setSelectedSortProduct}
                />
                <button
                  onClick={event => setShowSellingPlanScreen(false)}
                  type="submit"
                  className="as-mt-2 as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-1 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                >
                  Next
                  <ArrowRightAltOutlined className="as-h-3 as-w-3 as-text-white as-ml-1"/>
                </button>
              </div>
            </div>
          )}

          {!showSellingPlanScreen && (
            <div className="">
              {/* <button onClick={() => setCartOpen(true)}
                      className="">
                <ShoppingBagIcon/>
                <span className="badge badge-pill badge-success">{_.sumBy(selectedProducts, 'quantity')}</span>
              </button> */}
              {/* <BundleHeader
                subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                subscriptionPlans={subscriptionPlans}
                setShowSellingPlanScreen={setShowSellingPlanScreen}
                selectedSellingPlan={selectedSellingPlan}
                setSelectedSellingPlan={setSelectedSellingPlan}
                setCartOpen={setCartOpen}
                selectedSortProduct={selectedSortProduct}
                setSelectedSortProduct={setSelectedSortProduct}
              /> */}
              <div
                className="lg:as-container lg:as-mx-auto">
                  <div className="lg:as-grid as-grid-flow-col auto-cols-max">
                  <div className="as-px-6 lg:as-px-0 lg:as-w-[325px] lg:as-mr-8">
                    <BundleCart
                      selectedProducts={selectedProducts}
                      subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                      minProduct={minProduct}
                      maxProduct={maxProduct}
                      minOrderAmount={minOrderAmount}
                      addToCart={addToCart}
                      onProductAdd={onProductAdd}
                      onProductRemove={onProductRemove}
                      selectedSellingPlan={selectedSellingPlan}
                      open={cartOpen}
                      setOpen={setCartOpen}
                      isCheckingOut={isCheckingOut}
                      setIsCheckingOut={setIsCheckingOut}
                      bundleData={props?.bundleData}
                      selectedSellingPlanId={selectedSellingPlanId}
                      subTotal={subTotal}
                      getDiscountedPrice={getDiscountedPrice}
                      onProductDelete={onProductDelete}
                      // tieredDiscount={tieredDiscount}
                      subscriptionPlans={subscriptionPlans}
                      setShowSellingPlanScreen={setShowSellingPlanScreen}
                      setSelectedSellingPlan={setSelectedSellingPlan}
                    />

                  </div>
                  <div className="">
                    <div class="as-bg-[#2b2201]  as-px-6 lg:as-px-3 as-py-2 as-text-white as-relative">
                    <div class="">
                      <div class="">
                        <ul class="as-flex as-items-center as-uppercase">
                          <li class="as-capitalize as-text-sm as-font-bold as-tracking-widest">{subscriptionBundleSettingsEntity?.tieredDiscount}</li>
                          {discounts.map(item => {
                            return <li class="as-ml-3 as-flex as-items-center">
                              <div class={`as-rounded-full as-flex as-items-center as-justify-center as-h-6 as-w-6 as-mr-1 ${item?.isActive ? `as-bg-indigo-600` : `as-bg-indigo-200`}`}>
                                {item?.isActive && <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="as-w-4 as-h-4">
                                  <path fillRule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z" clipRule="evenodd" />
                                </svg>}
                                {!item?.isActive && <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="as-w-4 as-h-4">
                                  <path fillRule="evenodd" d="M12 1.5a5.25 5.25 0 00-5.25 5.25v3a3 3 0 00-3 3v6.75a3 3 0 003 3h10.5a3 3 0 003-3v-6.75a3 3 0 00-3-3v-3c0-2.9-2.35-5.25-5.25-5.25zm3.75 8.25v-3a3.75 3.75 0 10-7.5 0v3h7.5z" clipRule="evenodd" />
                                </svg>}
                              </div>
                              <div class="as-text-xs">
                                <span>{item?.discount}</span>
                              </div>
                            </li>
                          })}
                        </ul>
                        <div class="">
                        <div className={'limit-progress as-w-[80%]'}>
                        {minProduct > 1 ? (
                          <div className={`as-my-2`}>
                            <div className="as-w-full as-bg-[#ffffff1f] as-rounded-full as-overflow-hidden as-h-[10px]">
                              <div
                                className="as-bg-indigo-600 as-text-xs as-font-medium as-text-blue-100 as-text-center as-leading-none as-rounded-l-full as-transition-all as-h-[10px]"
                                style={{ width: `${((_.sumBy(selectedProducts, 'quantity') / totalDiscountForProgressBar) * 100).toFixed(0)}%` }}
                              >
                              </div>
                            </div>
                          </div>
                        ) : (
                          ''
                        )}
                        {minOrderAmount > 1 ? (
                          <div className={`as-my-2`}>
                            <div className="as-w-full as-bg-[#ffffff1f] as-rounded-full as-overflow-hidden as-h-[10px]">
                              <div
                                className="as-bg-indigo-600 as-text-xs as-font-medium as-text-blue-100 as-text-center as-leading-none as-rounded-l-full as-transition-all as-h-[10px]"
                                style={{ width: `${(subTotal/totalDiscountForProgressBar)*100}%` }}
                              >
                              </div>
                            </div>
                          </div>
                        ) : (
                          ''
                        )}
                      </div>
                          <div class="as-text-xs">
                            {upcomingDiscountText}
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="as-absolute as-w-screen as-h-full as-bg-[#2b2201] as-z-[-1] as-inset-0 as-hidden lg:as-block"></div>
                    </div>
                    <div className=" as-grid as-gap-6 as-relative as-bg-[#FFF9EE]  as-px-6 xl:as-px-12 as-py-6 xl:as-py-10 xl:as-pr-8">
                  {filteredProducts?.map((each, idx) => {
                    return (
                      <div className={``} draggable onDragStart={event => onDragStart(event, each)} key={idx}
                          style={{padding: '8px'}}>
                        {subscriptionPlans?.length > 0 && (
                          <Product
                            bundleData={props.bundleData}
                            product={each?.product}
                            onProductAdd={onProductAdd}
                            onProductRemove={onProductRemove}
                            onVariantChange={onVariantChange}
                            showAddProduct
                            showVariant
                            variants={props?.bundleData?.variants}
                            selectedProducts={selectedProducts}
                            subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                            sellingPlanIds={subscriptionPlans?.map(plan => Number(plan?.id?.split('/').pop()))}
                            selectedSellingPlan={selectedSellingPlan}
                            setCartOpen={setCartOpen}
                            isCartOpened={isCartOpened}
                            selectedSellingPlanId={selectedSellingPlanId}
                            minProduct={minProduct}
                            maxProduct={maxProduct}
                          />
                        )}
                      </div>
                    );
                    })}
                    <div className="as-absolute as-w-screen as-h-full as-bg-[#FFF9EE] as-z-[-1] as-inset-0 as-hidden lg:as-block"></div>
                    </div>
                </div>
                  </div>
              </div>
            </div>
          )}
        </div>

        <div
          className="appstle_selected_product_list"
          onDragOver={event => onDragOver(event)}
          onDrop={event => onDrop(event, 'dropable')}
        ></div>
      </div>
    </>
  );
}

export default ProductSelection;
