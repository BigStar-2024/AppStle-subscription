import React, {useEffect, useState} from 'react';
import Product from './Product';
import _ from 'lodash';
import {toast} from 'react-toastify';
import './style.scss';
import axios from 'axios';
import BundleCart from './BundleCartNew';
import SellingPlanList from './SellingPlanList';
import {ArrowLeft, ArrowRight, ArrowRightAltOutlined} from '@mui/icons-material';
import BundleHeader from './BundleHeader';
import ProductCopy from "./Product copy";
import TailwindModal from "./TailwindModal";
import ReviewBundle from "./ReviewBundle";
import Loader from './Loader';
import Select from 'react-select';
// import {cartData} from './dummyCartData';
import { formatPrice, getCurrentConvertedCurrencyPrice } from './Bundle.util';

function ProductSelection(props) {
  const [products, setProducts] = useState(props.products);
  const [subscriptionBundleSettingsEntity, setSubscriptionBundleSettingsEntity] = useState(props.subscriptionBundleSettingsEntity);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [preSelectedProducts, setPreSelectedProducts] = useState([]);
  const [subscriptionPlans, setSubscriptionPlans] = useState([]);
  const [subscriptionGroupPlans, setSubscriptionGroupPlans] = useState([]);
  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [subscriptionPlanProductInfoMap, setSubscriptionPlanProductInfoMap] = useState(null);
  const [subscriptionPlanVariantMap, setSubscriptionPlanVariantMap] = useState(null);
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
  const [isModalOpen, setIsModalOpen] = useState(true)
  const [redirectHistory, setRedirectHistory] = useState([])
  const [isClickedBackButton, setIsClickedBackButton] = useState(false)
  const [eligibleDiscount, setEligibleDiscount] = useState(null);
  const [searchValue, setSearchValue] = useState('');
  const [selectedFilterData, setSelectedFilterData] = useState({ vendor: [], tags: [], productType: [] });
  const [propertyDetails, setPropertyDetails] = useState(null);
  const [productFilterConfig, setProductFilterConfig] = useState(null);


  const [minOrderAmount, setMinOrderAmount] = useState(null);
  const [tieredDiscount, setTieredDiscount] = useState(null);
  const [minProduct, setMinProduct] = useState(0);
  const [finalFilteredProduct, setFinalFilteredProduct] = useState([]);
  const [maxProduct, setMaxProduct] = useState(99999999999999999999);
  const [discountedSubTotal, setDiscountedSubTotal] = useState(0);
  const [cartItems, setCartItems] = useState([]);
  const [existingCartItems, setExistingCartItems] = useState([]);

  const [minQtyButtonText, setMinQtyButtonText] = useState('');
  const [minAmountButtonText, setMinAmountButtonText] = useState('');

  const [isRightShowSidebar, setIsRightShowSidebar] = useState(false);
  const [isLeftShowSidebar, setIsLeftShowSidebar] = useState(false);

  useEffect(() => {
    document.documentElement.scrollTop = 0;
  }, [])

  useEffect(() => {
    if (showSellingPlanScreen) {
      document.querySelector('html').classList.add('appstle-selling-plan-screen')
    } else {
      document.querySelector('html').classList.remove('appstle-selling-plan-screen')
    }
  }, [showSellingPlanScreen])


  useEffect(()=>{
    setProducts(props.products)
  },[props.products])

  useEffect(()=>{
    if (props?.iframeSelectedFilterData && props?.shopInfo?.buildBoxVersion == "V2_IFRAME") {
      setSelectedFilterData(props?.iframeSelectedFilterData);
    }
  },[props?.iframeSelectedFilterData])

  useEffect(() => {
    if (props?.iframeSelectedFilterData && props?.shopInfo?.buildBoxVersion == 'V2_IFRAME') {
      setSelectedFilterData(props?.iframeSelectedFilterData);
    }
  }, [props?.iframeSelectedFilterData]);

  useEffect(() => {
    let data = filteredProducts?.filter(each => productSearch(props?.selectedSingleProduct ? each : each?.product));
    if (selectedFilterData?.sorting && selectedFilterData?.sorting?.length > 0) {
      data = sortData(data, selectedFilterData?.sorting[0]?.value);
    }
    setFinalFilteredProduct([...data]);
  }, [filteredProducts, selectedFilterData, searchValue]);

  const sortData = (filteredProductsData, sortingType) => {
    let sortingOptions = {
      '1': { key: 'product.title', order: 'asc' },
      '2': { key: 'product.title', order: 'desc' },
      '3': { key: 'product.price', order: 'asc' },
      '4': { key: 'product.price', order: 'desc' }
    };

    if (props?.selectedSingleProduct && props?.shopInfo?.buildBoxVersion == 'V2_IFRAME' && props?.bundleData?.bundle?.buildABoxType == "SINGLE_PRODUCT") {
      sortingOptions = {
        '1': { key: 'title', order: 'asc' },
        '2': { key: 'title', order: 'desc' },
        '3': { key: 'price', order: 'asc' },
        '4': { key: 'price', order: 'desc' }
      };
    }



    const sortingOption = sortingOptions[sortingType];

    if (sortingOption) {
      const { key, order } = sortingOption;
      return _.orderBy(filteredProductsData, [key], [order]);
    } else {
      return filteredProductsData;
    }
  };

  useEffect(() => {
    if (props.bundleData?.subscription?.infoJson) {
      setSubscriptionPlans(JSON.parse(props.bundleData?.subscription?.infoJson)?.subscriptionPlans);
      setSubscriptionGroupPlans(props.bundleData?.subscriptionGroupPlans);
      setSubscriptionPlanProductInfoMap(props.bundleData?.subscriptionPlanProductInfoMap);
      setSubscriptionPlanVariantMap(props.bundleData?.subscriptionPlanVariantMap);
    }

    setMinProduct(props?.selectedSingleProductBundleData?.minQuantity || props.bundleData.bundle?.minProductCount || 0);
    setMaxProduct(props?.selectedSingleProductBundleData?.maxQuantity || props.bundleData.bundle?.maxProductCount || 99999999999999999999);
    setMinOrderAmount(props.bundleData.bundle?.minOrderAmount);
    setTieredDiscount(props.bundleData.bundle?.tieredDiscount);
  }, [props.bundleData]);

  useEffect(() => {
    if (cartOpen && !isCartOpened) {
      setIsCartOpened(true);
    }
  }, [cartOpen]);

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

  useEffect(() => {
    let sortedDiscounts = [];
    if (tieredDiscount) {
      const applicableDiscountList = JSON.parse(tieredDiscount);
      const updatedTieredDiscounts = [];
      applicableDiscountList.forEach(item => {
        updatedTieredDiscounts.push({ ...item, quantity: parseInt(item.quantity), discount: parseInt(item.discount) });
      });
      const quantityDataFilter = _.filter(updatedTieredDiscounts, function(item) {
        return item.discountBasedOn === 'QUANTITY' && ((_.sumBy(selectedProducts, 'quantity') >= parseInt(item.quantity)) || item.quantity === 1);
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
          if (JSON.stringify(eligibleDiscount) !== JSON.stringify(appliedDiscount)) {
            setEligibleDiscount(appliedDiscount);
          }
        } else {
          if (eligibleDiscount !== null) {
            setEligibleDiscount(null);
          }
        }
      } else {
        if (eligibleDiscount !== null) {
          setEligibleDiscount(null);
        }
      }
    }
  }, [selectedProducts, subTotal]);

  const getDiscountedPrice = (price, skipDiscountCalculation, selectedSellingPlan, eligibleDiscount) => {
    if (selectedSellingPlan) {
      let billingPeriod = 1;
      if (selectedSellingPlan?.planType != "PAY_AS_YOU_GO") {
        billingPeriod = selectedSellingPlan?.billingFrequencyCount / selectedSellingPlan?.frequencyCount;
      }
      price = price * billingPeriod;
    }
      if (eligibleDiscount && eligibleDiscount?.discount && !skipDiscountCalculation) {
        return Math.round(((100 - Number(eligibleDiscount?.discount)) / 100) * price);
      } else {
        return price;
      }
  };

  const getDiscountedPerDeliveryPrice = (price, eligibleDiscount) => {
    if (eligibleDiscount && eligibleDiscount?.discount) {
      return Math.round(((100 - Number(eligibleDiscount?.discount)) / 100) * price);
    } else {
      return price;
    }
  }


  const onClickGoBackButton = () => {
    if (redirectHistory && redirectHistory?.length > 0) {
      setIsClickedBackButton(true)
      let popRedirectHistory = redirectHistory[redirectHistory.length - 1];
      props.setToken(popRedirectHistory.bundleSlug);
      if (props?.shopInfo?.buildBoxVersion !== "V2_IFRAME") {
        window.history.replaceState(null, '', '/apps/subscriptions?action=bundle&token=' + popRedirectHistory.bundleSlug);
      }
    }
  }

  useEffect(() => {
    props.setRedirectHistoryForIframe(redirectHistory);
  },[redirectHistory]);

  useEffect(() => {
    if (props?.isClickGoBackButton) {
      onClickGoBackButton();
      props?.setIsClickGoBackButton(false);
    }
  },[props.isClickGoBackButton]);

  useEffect(() => {
    if (subscriptionBundleSettingsEntity?.enableClearCartSelectedProducts) {
      setCartItems([]);
    } else {
      syncCartItems();
    }

    if (subscriptionBundleSettingsEntity?.rightSidebarHTML) {
      setIsRightShowSidebar(true);
    }

    if (subscriptionBundleSettingsEntity?.leftSidebarHTML) {
      setIsLeftShowSidebar(true);
    }

  },[subscriptionBundleSettingsEntity])

  const syncCartItems = async () => {
    let cartItems = await getCartData();
    setCartItems(cartItems)
  }

  const getCartData = async () => {
    let cartData = await fetch('/cart.js');
    cartData = await cartData.json();
    return cartData.items;
  }

  useEffect(() => {
    if (!selectedProducts?.length && !preSelectedProducts?.length && cartItems?.length) {
      let currentBundleItems = cartItems?.filter(item => item?.properties?.['_appstle-bb-id'] === props?.bundleData?.bundle?.uniqueRef);
      setExistingCartItems(currentBundleItems);
      if (subscriptionPlans?.length && currentBundleItems?.length) {
        let cartItemsSellingPlanId = currentBundleItems[0]?.selling_plan_allocation?.selling_plan?.id;
        let selectedPlan = subscriptionPlans?.filter(plan => plan?.id?.includes(cartItemsSellingPlanId)).pop();
        setSelectedSellingPlan(selectedPlan);
      }
    }
  }, [cartItems, subscriptionPlans, selectedProducts, preSelectedProducts])

  useEffect(() => {
    if (!selectedProducts?.length && !preSelectedProducts?.length && existingCartItems?.length && (props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT")) {
      let products = [];
      existingCartItems.forEach(item => {
        let product = (filteredProducts?.filter(product => {
          if (product?.product?.id == item?.product_id) {
            return true;
          }})).pop();
        let variant = (product?.product?.variants?.filter(variant => variant?.id == item?.variant_id)).pop();
        let productToAdd = {
          product: {...product?.product},
          variant: {...variant},
          quantity: item?.quantity,
          bundleId: props?.bundleData?.bundle?.uniqueRef,
          selling_plan: setSellingPlanId(product?.product),
          eligibleDiscount: JSON.parse(JSON.stringify(eligibleDiscount)),
          selectedSellingPlan: JSON.parse(JSON.stringify(selectedSellingPlan)),
        }
        products.push(productToAdd)
      })
      setSelectedProducts([...selectedProducts, ...products]);
    }
  }, [selectedSellingPlan, existingCartItems, selectedProducts, preSelectedProducts, filteredProducts])

  useEffect(() => {
    if (isClickedBackButton && props?.bundleData?.bundle?.uniqueRef) {
      if (redirectHistory && redirectHistory?.length > 0) {
        let popRedirectHistory = redirectHistory.pop();
        let tempPreSelectedProducts = [].concat(preSelectedProducts);
        if (selectedProducts && selectedProducts?.length > 0) {
          tempPreSelectedProducts = [...preSelectedProducts, ...selectedProducts];
        }
        setSelectedSellingPlan(popRedirectHistory.selectedSellingPlan);
        setSelectedSellingPlanId(popRedirectHistory.selling_plan)
        setSelectedProducts(tempPreSelectedProducts?.filter(product => product?.bundleId == popRedirectHistory.bundleSlug));
        setPreSelectedProducts(tempPreSelectedProducts?.filter(product => product?.bundleId != popRedirectHistory.bundleSlug));
      }
      setIsClickedBackButton(false);
    }
  }, [props?.bundleData?.bundle?.uniqueRef])

  const addToCart = async () => {
    setIsCheckingOut(true);
    if ((subscriptionBundleSettingsEntity?.clearCartV2 === 'default') && (props.bundleData?.bundle?.customRedirectURL ? ((props.bundleData?.bundle?.customRedirectURL.indexOf('&token=') === -1)  && (props.bundleData?.bundle?.customRedirectURL.indexOf('/bb/') !== -1)) : true)) {
      fetch(`${Shopify?.routes?.root}cart/clear.js`, {
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
      if ((subscriptionBundleSettingsEntity?.clearCartV2 === 'default') || (subscriptionBundleSettingsEntity?.clearCartV2 === 'disabled')) {
        checkoutBundle({});
      } else if ((subscriptionBundleSettingsEntity?.clearCartV2 === 'enabled')) {
        await fetch(`${Shopify?.routes?.root}cart/clear.js`);
        checkoutBundle({});
      } else {
        checkoutBundle({});
      }
    }
  };

  let checkoutBundle = async res => {
    let addPayload = {items: []};
    var redirectType = props.bundleData?.bundle?.bundleRedirect;
    var redirectURL = props.bundleData?.bundle?.customRedirectURL;
    if ((redirectType === 'CUSTOM' && ((redirectURL.indexOf('&token=') !== -1) || (redirectURL.indexOf('/bb/') !== -1))) || (selectedProducts.length + preSelectedProducts.length)) {
      [...preSelectedProducts, ...selectedProducts].forEach(item => {

        let sellingPlan = item?.selling_plan;
        if (sellingPlan && sellingPlan.id) {
          sellingPlan = sellingPlan.id.split('/').pop();
        }

        var payloadData = {
          id: item?.variant?.id,
          quantity: item?.quantity,
          selling_plan: sellingPlan,
          "properties": {
            "_appstle-bb-id": item?.bundleId,
            "_appstle-bb-product-sku": item?.variant?.sku
          }
        };
        if (propertyDetails) {
          payloadData.properties = { ...propertyDetails, ...payloadData.properties }
        }
        addPayload?.items?.push(payloadData);
      });
      if (redirectType === 'CUSTOM' && ((redirectURL.indexOf('&token=') !== -1) || (redirectURL.indexOf('/bb/') !== -1))) {
        let tokenArr = null;
        if (redirectURL.indexOf('&token=') !== -1) {
          tokenArr = redirectURL.split('&token=');
        } else if (redirectURL.indexOf('/bb/') !== -1) {
          tokenArr = redirectURL.split('/bb/');
          tokenArr[1] = tokenArr[1].replace('#', '').replace('/', '');
        }

        if (tokenArr.length > 1) {
          if (props?.bundleSlug) {
            setRedirectHistory(redirectHistory => [...redirectHistory, {bundleSlug: props.bundleSlug, selectedSellingPlan: selectedSellingPlan, selling_plan: selectedSellingPlanId}])
          }
          props.setToken(tokenArr[1]);

          let tempPreSelectedProducts = [...preSelectedProducts, ...selectedProducts];

          let selectedNextBundleProduct = [];
          if (tempPreSelectedProducts && tempPreSelectedProducts?.length > 0) {
            selectedNextBundleProduct = tempPreSelectedProducts?.filter(product => product?.bundleId == tokenArr[1]);
          }

          if (selectedNextBundleProduct?.length > 0) {
            setPreSelectedProducts(tempPreSelectedProducts?.filter(product => product?.bundleId != tokenArr[1]));
            setSelectedProducts(selectedNextBundleProduct);
            setSelectedSellingPlan(selectedNextBundleProduct[0]?.selectedSellingPlan);
            setSelectedSellingPlanId(selectedNextBundleProduct[0]?.selling_plan);
          } else {
            setPreSelectedProducts(preSelectedProducts => [...preSelectedProducts, ...selectedProducts]);
            setSelectedProducts([]);
            setSelectedSellingPlan(null);
          }

          if (props?.shopInfo?.buildBoxVersion !== "V2_IFRAME") {
            window.scrollTo({ top: 0, behavior: 'smooth' });
            window.history.replaceState(null, '', '/apps/subscriptions?action=bundle&token=' + tokenArr[1]);
          }
        }
        setIsCheckingOut(false);
        setCartOpen(false);
        if ((props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT") && (props?.shopInfo?.buildBoxVersion === "V2_IFRAME")) {
          props?.setShowProductSelectionScreen(true);
          props?.setIsReviewBundleModalOpen(false);
        }
      } else {
        let config = {
          method: 'POST', // or 'PUT'
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(addPayload)
        };
        let cartResponse = await fetch(`${Shopify?.routes?.root}cart/add.js`, config);
        if (cartResponse?.ok) {
          let parsedCartResponse = await cartResponse.json();
          if (parsedCartResponse?.items?.length) {
            fetch(`${Shopify?.routes?.root}cart.js`)
              .then(res => res.json())
              .then(async data => {
                await axios
                  .put(`/api/subscription-bundlings/discount/${props?.bundleSlug}`, {cart: data})
                  .then(res => {
                  console.log(res.data);
                    var bundleCheckoutData = res.data;
                    var discountCode = bundleCheckoutData.discountCode;
                    var redirectType = props.bundleData?.bundle?.bundleRedirect;
                    var redirectURL = props.bundleData?.bundle?.customRedirectURL;
                    if (props?.shopInfo?.buildBoxVersion !== "V2_IFRAME") {
                      if (redirectType === 'CART' || Shopify?.shop === '222meals.myshopify.com') {
                        if (discountCode === 'appstle_no_discount') {
                          window.location.href = Shopify?.routes?.root + 'cart';
                        } else {
                          window.location.href = Shopify?.routes?.root + 'discount/' + discountCode + `?redirect=${Shopify?.routes?.root}cart`;
                        }
                      } else if (redirectType === 'CHECKOUT') {
                        if (discountCode === 'appstle_no_discount') {
                          window.location.href = Shopify?.routes?.root + 'checkout';
                        } else {
                          window.location.href = Shopify?.routes?.root + 'discount/' + discountCode + `?redirect=${Shopify?.routes?.root }checkout`;
                        }
                      } else if (redirectType === 'CUSTOM') {
                        if (discountCode === 'appstle_no_discount') {
                          window.location.href = redirectURL;
                        } else {
                          fetch(Shopify?.routes?.root + 'discount/' + discountCode)
                            .then(function (res) {
                              window.location.href = redirectURL;
                            })
                            .catch(function (err) {
                              window.location.href = redirectURL;
                            });
                        }
                      }
                    } else {
                      window?.parent?.postMessage(JSON.stringify({
                        type: "appstle_message_to_redirect_to_checkout",
                        discountCode: res?.data?.discountCode || "appstle_no_discount",
                        redirectType: props.bundleData?.bundle?.bundleRedirect,
                        redirectURL: props.bundleData?.bundle?.customRedirectURL
                      }));
                    }

                  }).catch(err => {
                    console.log(err);
                    setIsCheckingOut(false);
                    setErrorMessage(err?.description);
                    setError(true);
                  });
              });
          } else {
            setErrorMessage(parsedCartResponse?.description);
            setError(true);
            setIsCheckingOut(false);
          }
        } else {
          let cartError = await cartResponse.json();
          setErrorMessage(cartError?.description);
          setError(true);
          setIsCheckingOut(false);
        }
      }
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

  const onProductAdd = (variant, product, quantity, data) => {

    let newSelectedProducts = JSON.parse(JSON.stringify(selectedProducts));
    let idx = _.findIndex(newSelectedProducts, o => (o.variant.id == variant.id) && (o.bundleId == props?.bundleData?.bundle?.uniqueRef));

    let newPreSelectedProducts = [];
    let preSelectedIdx = -1;

    if (data && data?.bundleId) {
      idx = _.findIndex(newSelectedProducts, o => (o.variant.id == variant.id) && (o.bundleId == data?.bundleId));

      newPreSelectedProducts = JSON.parse(JSON.stringify(preSelectedProducts))
      preSelectedIdx = _.findIndex(newPreSelectedProducts, o => (o.variant.id == variant.id) && (o.bundleId === data?.bundleId));
    }

    quantity = Number(quantity);
    if (idx === -1) {
      if (preSelectedIdx === -1) {
        newSelectedProducts = [
          ...newSelectedProducts,
          {
            product: {...product},
            variant: {...variant},
            quantity: quantity,
            bundleId: props?.bundleData?.bundle?.uniqueRef,
            selling_plan: setSellingPlanId(product),
            eligibleDiscount: JSON.parse(JSON.stringify(eligibleDiscount)),
            selectedSellingPlan: JSON.parse(JSON.stringify(selectedSellingPlan)),
          }
        ];
        setSelectedProducts(newSelectedProducts);
      } else {
        newPreSelectedProducts[preSelectedIdx].quantity += quantity;
        setPreSelectedProducts(newPreSelectedProducts);
      }
    } else {
      newSelectedProducts[idx].quantity += quantity;
      setSelectedProducts(newSelectedProducts);
    }
  };

 const onProductRemove = (variant, product, quantity, data) => {
  let selectedIdx = selectedProducts.findIndex((o) => (o.variant.id === variant.id) && (o.bundleId == props?.bundleData?.bundle?.uniqueRef));
  let preSelectedIdx = -1;
  if (data) {
    selectedIdx = selectedProducts.findIndex((o) => (o.variant.id === variant.id) && (o.bundleId === data?.bundleId));
    preSelectedIdx = preSelectedProducts.findIndex((o) => (o.variant.id === variant.id) && (o.bundleId === data?.bundleId));
  }

  if (selectedIdx >= 0) {
    if (selectedProducts[selectedIdx].quantity > 1) {
      selectedProducts[selectedIdx].quantity -= 1;
      setSelectedProducts([...selectedProducts]); // Ensure a new reference
    } else {
      setSelectedProducts((oldProducts) => oldProducts.filter((o) => o.variant.id !== variant.id));
    }
  } else if (preSelectedIdx >= 0) {
    if (preSelectedProducts[preSelectedIdx].quantity > 1) {
      preSelectedProducts[preSelectedIdx].quantity -= 1;
      setPreSelectedProducts([...preSelectedProducts]); // Ensure a new reference
    } else {
      setPreSelectedProducts((oldProducts) => oldProducts.filter((o) => o.variant.id !== variant.id));
    }
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
      let newSubTotal =  calculateSubTotal(true);
      setSubstotal(newSubTotal);
    }
  }, [selectedProducts,preSelectedProducts]);

  useEffect(() => {
    if (selectedSellingPlanId) {
      let newSubTotal =  calculateSubTotal(false);
      setDiscountedSubTotal(newSubTotal);
    }
  }, [selectedProducts, preSelectedProducts]);

  const calculateSubTotal = (skipDiscount) => {
      let previousSelectedProductPrices = preSelectedProducts.map(item => {
        return (
          getDiscountedPrice(
            item?.variant?.selling_plan_allocations
              ?.filter(sellingPlan => {
                return sellingPlan?.selling_plan_id == Number(item.selling_plan);
              })
              .pop()?.per_delivery_price, skipDiscount, item?.selectedSellingPlan, item?.eligibleDiscount
          ) * Number(item?.quantity)
        );
      })
       let currentSelectedProductPrices = selectedProducts.map(item => {
         return (
           getDiscountedPrice(
             item?.variant?.selling_plan_allocations
               ?.filter(sellingPlan => {
                 return sellingPlan?.selling_plan_id == Number(item.selling_plan);
               })
               .pop()?.per_delivery_price, skipDiscount, item?.selectedSellingPlan, item?.eligibleDiscount
           ) * Number(item?.quantity)
         );
       });
       let allSeletedProduct = [...previousSelectedProductPrices, ...currentSelectedProductPrices];
       return (allSeletedProduct.reduce((a, b) => a + b, 0))
  }

  useEffect(() => {
    let newSelectedProducts = selectedProducts?.map(item => {
      return {
        ...item,
        selling_plan: setSellingPlanId(item?.product),
        eligibleDiscount: JSON.parse(JSON.stringify(eligibleDiscount)),
        selectedSellingPlan: JSON.parse(JSON.stringify(selectedSellingPlan)),
        bundleId: props?.bundleData?.bundle?.uniqueRef,
        // buyPrice: item?.variant?.price
        // buyPrice: (getDiscountedPrice(
        //   item?.variant?.selling_plan_allocations?.filter(sellingPlan => {
        //       return sellingPlan?.selling_plan_id == Number(setSellingPlanId(item?.product));
        //     })
        //     .pop()?.per_delivery_price
        //   ) * Number(item?.quantity)
        // ),
        // perDeliveryBuyPrice: (getDiscountedPerDeliveryPrice(
        //   item?.variant?.selling_plan_allocations?.filter(sellingPlan => {
        //       return sellingPlan?.selling_plan_id == Number(setSellingPlanId(item?.product));
        //     })?.pop()?.per_delivery_price
        //   ) * Number(item?.quantity)
        // )
      }
    })
    setSelectedProducts([...newSelectedProducts])
  }, [eligibleDiscount, selectedSellingPlanId])

  useEffect(() => {
    if (selectedSortProduct?.value === 'alphabeticallyAZ') {
      const filtered = _.orderBy(
        products,
        [
          function (o) {
            return o?.product?.title;
          }
        ],
        ['asc']
      );
      setFilteredProducts(filtered);
    } else if (selectedSortProduct?.value === 'alphabeticallyZA') {
      const filtered = _.orderBy(
        products,
        [
          function (o) {
            return o?.product?.title;
          }
        ],
        ['desc']
      );
      setFilteredProducts(filtered);
    } else if (selectedSortProduct?.value === 'lowToHigh') {
      const filtered = _.orderBy(
        products,
        [
          function (o) {
            return o?.product?.price;
          }
        ],
        ['asc']
      );
      setFilteredProducts(filtered);
    } else if (selectedSortProduct?.value === 'highToLow') {
      const filtered = _.orderBy(
        products,
        [
          function (o) {
            return o?.product?.price;
          }
        ],
        ['desc']
      );
      setFilteredProducts(filtered);
    } else {
      setFilteredProducts(products);
    }
  }, [products, selectedSortProduct]);

  useEffect(() => {
    props?.setIsNextButtonValid((_.sumBy(selectedProducts, 'quantity') <= maxProduct) && (_.sumBy(selectedProducts, 'quantity') >= minProduct))
  }, [selectedProducts])

  const getPlanData = (plan) => {
    if (!selectedSellingPlan) {
      return false;
    }
    let compareFields = ['frequencyInterval','frequencyCount','billingFrequencyInterval','billingFrequencyCount','planType','discountOffer','discountOffer2','discountEnabled','discountEnabled2','discountEnabledMasked','discountEnabled2Masked','frequencyType','specificMonthValue','specificDayEnabled','maxCycles','minCycles','cutOff','prepaidFlag','freeTrialEnabled','freeTrialCount','memberExclusiveTags','memberInclusiveTags'];
    for (const field of compareFields) {
      if (selectedSellingPlan[field] !== plan[field]) {
        return false;
      }
    }
    return true;
  }

  const setSellingPlanId = (product) => {
    if(subscriptionGroupPlans && subscriptionGroupPlans.length) {
      let plans = subscriptionGroupPlans.filter(subscriptionGrp => ((subscriptionGrp?.productIds && subscriptionGrp?.productIds?.includes(product?.id)) || (subscriptionGrp?.variantProductIds && subscriptionGrp?.variantProductIds?.includes(product?.id)))).map(grp =>
        JSON.parse(grp?.infoJson)?.subscriptionPlans?.filter(a => getPlanData(a))?.pop()?.id.split('/').pop()
      );
      if(plans && plans.length) {
        return plans?.pop();
      } else {
        return selectedSellingPlanId;
      }
    } else {
      return selectedSellingPlanId;
    }
  };

  const productSearch = (e) => {
    const isSearchValuePresent = Boolean(searchValue);
    const isVendorFilterPresent = selectedFilterData?.vendor?.length > 0;
    const isProductTypeFilterPresent = selectedFilterData?.productType?.length > 0;
    const isTagsFilterPresent = selectedFilterData?.tags?.length > 0;

    if (
      !isSearchValuePresent &&
      !isVendorFilterPresent &&
      !isProductTypeFilterPresent &&
      !isTagsFilterPresent
    ) {
      return true; // No search value or filters applied, return true for all products
    }

    if (productFilterConfig?.isEnableAndCondtion) {
      return (
        (!isSearchValuePresent ||
          (e?.title?.toLowerCase()?.includes(searchValue?.toLowerCase()) ||
          e?.description?.toLowerCase()?.includes(searchValue?.toLowerCase()))) &&
        (!isVendorFilterPresent ||
          collectSelectedFilterData('vendor')?.includes(e?.vendor)) &&
        (!isProductTypeFilterPresent ||
          collectSelectedFilterData('productType')?.includes(e?.type)) &&
        (!isTagsFilterPresent ||
          e?.tags?.some(tag => collectSelectedFilterData('tags')?.includes(tag)))
      );
    } else {
      return (
        (isSearchValuePresent &&
          (e?.title?.toLowerCase()?.includes(searchValue?.toLowerCase()) ||
            e?.description?.toLowerCase()?.includes(searchValue?.toLowerCase()))) ||
        (isVendorFilterPresent &&
          collectSelectedFilterData('vendor')?.includes(e?.vendor)) ||
        (isProductTypeFilterPresent &&
          collectSelectedFilterData('productType')?.includes(e?.type)) ||
        (isTagsFilterPresent &&
          e?.tags?.some(tag => collectSelectedFilterData('tags')?.includes(tag)))
      );
    }
  };

  useEffect(() => {
    if (subscriptionBundleSettingsEntity?.productFilterConfig) {
      setProductFilterConfig(JSON.parse(subscriptionBundleSettingsEntity?.productFilterConfig));
    }
  }, [subscriptionBundleSettingsEntity]);

  const collectSelectedFilterData = key => {
    return selectedFilterData[key]?.map(value => value.value);
  }

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
      return (minProduct ? (_.sumBy(selectedProducts, 'quantity') < minProduct) : false) || (maxProduct ? (_.sumBy(selectedProducts, 'quantity') > maxProduct) : false) || isCheckingOut;
    }
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
    setMinQtyButtonText(barText)
    return barText;
  };

  const renderMinimumAmountText = () => {
    let barText = '';
    if (minOrderAmount && subTotal / 100 < minOrderAmount) {
      let buttonLabel = subscriptionBundleSettingsEntity?.minimumOrderAmountLabelTextV2 || `Minimum order amount {{minOrderAmount}}.`;
      barText = buttonLabel.replace('{{minOrderAmount}}', formatPrice(getCurrentConvertedCurrencyPrice(minOrderAmount*100)));
    } else {
      // barText = "🎉 Congrats conditions fulfilled"
    }
    setMinAmountButtonText(barText)
    return barText;
  };

  const onChangeFilterData = (value, filterType) => {
    setSelectedFilterData(prevFilterData => ({
      ...prevFilterData,
      [filterType]: filterType === 'sorting' ? [value] : value,
    }));
  };

  const FilterDropdown = data => {
    const { item } = data?.data;
    return (
      <>
        <Select
          options={item}
          isMulti={data?.data?.basedOn != 'sorting'}
          placeholder={data?.data?.title}
          value={selectedFilterData[data?.data?.basedOn]}
          onChange={e => {
            onChangeFilterData(e, data?.data?.basedOn);
          }}
          class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
        />
      </>
    );
  };

  return (
    <>
      <div className={`${cartSliderOpen && 'appstle_slider_open'}`}>
        <div className={`${!props?.isBundleTypeClassic && `as-bg-gray-100 as-py-8`}`}>
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
              {((props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT") && !(props?.shopInfo?.buildBoxVersion === "V2_IFRAME")) && <BundleHeader
                subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                subscriptionPlans={subscriptionPlans}
                setShowSellingPlanScreen={setShowSellingPlanScreen}
                selectedSellingPlan={selectedSellingPlan}
                setSelectedSellingPlan={setSelectedSellingPlan}
                setCartOpen={setCartOpen}
                selectedSortProduct={selectedSortProduct}
                setSelectedSortProduct={setSelectedSortProduct}
                setSearchValue={setSearchValue}
                onClickGoBackButton={onClickGoBackButton}
                redirectHistory={redirectHistory}
                setSelectedFilterData={setSelectedFilterData}
                selectedFilterData={selectedFilterData}
              />}

              {props.shopInfo?.passwordEnabled != null && props?.shopInfo?.passwordEnabled ? (
                <div
                  className="as-bg-orange-100 as-border-t-4 as-border-orange-500 as-rounded-b as-text-orange-900 as-px-4 as-py-3 as-shadow-md"
                  role="alert"
                >
                  <div className="as-flex">
                    <div className="as-py-1">
                      <svg
                        className="as-fill-current as-h-6 as-w-6 as-text-orange-500 as-mr-4"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 20 20"
                      >
                        <path
                          d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z"/>
                      </svg>
                    </div>
                    <div>
                      <p className="as-font-bold">Store is password protected.</p>
                      <p className="as-text-sm">
                        Please make sure the store is not password protected as some features,
                        such as showing the products list and the product search, won't function
                        properly.
                      </p>
                    </div>
                  </div>
                </div>
              ) : (
                ''
              )}
              {!props?.isReviewBundleModalOpen && (
                <>
                <div className="as-choseProduct-title-wrapper as-container as-mx-auto as-px-4">
                        <div className="as-flex">
                          <h2 class="as-single-product-title as-text-2xl as-mb-3 as-pr-5">
                            {props?.bundleData?.bundle?.chooseProductsText || props?.subscriptionBundleSettingsEntity?.chooseProductsTextV2 || 'Choose Products'}
                          </h2>
                          {!subscriptionBundleSettingsEntity?.hideProductSearchBox &&
                            props?.bundleData?.bundle?.buildABoxType == 'SINGLE_PRODUCT' && (
                              <div className="input-holder bab-search-text-box" style={{ flex: 1 }}>
                                <input
                                  type="text"
                                  className="as-w-full as-border-2 as-border-gray-300 as-bg-white as-h-10 as-px-5 as-pr-5 as-rounded-lg as-text-sm focus:as-outline-none as-add-product-search-input"
                                  placeholder="Type to search"
                                  value={searchValue}
                                  style={{ boxShadow: 'none' }}
                                  onChange={e => {
                                    setSearchValue(e.target.value);
                                  }}
                        />
                      </div>
                            )}
                        </div>
                        {productFilterConfig &&
                          productFilterConfig?.enabled &&
                          props?.bundleData?.bundle?.buildABoxType == 'SINGLE_PRODUCT' && (
                            <div className={`as-flex as-gap-2  filterDropdown`}>
                              {productFilterConfig &&
                                productFilterConfig?.filters?.map(filter => (
                                  <div className="as-pt-4 filterDropdownWidth">
                                    <FilterDropdown data={filter} />
                                  </div>
                                ))}
                            </div>
                          )}
                      </div>

                  <div className={`box-wrap as-container as-flex as-flex-wrap as-mx-auto`}>
                  {isLeftShowSidebar && (
                      <div className={`custom-left-sidebar custom-sidebar as-px-4 as-py-5`} dangerouslySetInnerHTML={{__html: subscriptionBundleSettingsEntity?.leftSidebarHTML}}></div>
                    )}
                    
                    <div className={`top-section as-grow ${(!isLeftShowSidebar && !isRightShowSidebar) && 'as-basis-full'} ${(isLeftShowSidebar && isRightShowSidebar) ? 'both-sidebar' : ((isLeftShowSidebar || isRightShowSidebar) && 'with-sidebar')}`}>

                      <div className={'limit-progress as-mt-4 as-container as-mx-auto as-px-4'}>
                        {minProduct > 1 ? (
                          <div className={`as-my-5`}>
                            <div className="as-w-full as-bg-gray-200 as-rounded-full as-overflow-hidden">
                              <div
                                className="as-bg-indigo-600 as-text-xs as-font-medium as-text-blue-100 as-text-center as-leading-none as-rounded-l-full as-transition-all"
                                style={{
                                  width: `${
                                    ((_.sumBy(selectedProducts, 'quantity') / minProduct) * 100).toFixed(0) > 100
                                      ? 100
                                      : ((_.sumBy(selectedProducts, 'quantity') / minProduct) * 100).toFixed(0)
                                  }%`
                                }}
                              >
                                &nbsp;
                              </div>
                            </div>
                            <div className="as-text-xs as-text-right as-mt-2 as-text-gray-600">
                              {_.sumBy(selectedProducts, 'quantity')}/{minProduct}{' '}
                              {subscriptionBundleSettingsEntity?.selectedButtonTextV2 || 'Selected'}
                            </div>
                          </div>
                        ) : (
                          ''
                        )}
                      </div>

                      <div className={`as-mt-2 as-py-5 as-grid as-gap-4 as-grid-cols-2 ${(isLeftShowSidebar && isRightShowSidebar) ? 'lg:as-grid-cols-2' : ((isLeftShowSidebar || isRightShowSidebar) ? 'lg:as-grid-cols-3' : 'lg:as-grid-cols-4')} as-container as-mx-auto as-px-4`}>
                        {props?.bundleData?.bundle?.buildABoxType !== 'SINGLE_PRODUCT'
                          ? finalFilteredProduct?.map((each, idx) => {
                              return (
                                <>
                                  {each?.product?.available ? (
                                    <div
                                      className={``}
                                      draggable
                                      onDragStart={event => onDragStart(event, each)}
                                      key={idx}
                                      style={{ padding: '6px' }}
                                    >
                                      {subscriptionPlans?.length > 0 && selectedSellingPlanId && (
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
                                          selectedSellingPlanId={setSellingPlanId(each?.product)}
                                          minProduct={minProduct}
                                          maxProduct={maxProduct}
                                          eligibleDiscount={eligibleDiscount}
                                          key={each?.product?.id}
                                        />
                                      )}
                                    </div>
                                  ) : (
                                    ''
                                  )}
                      </>
                    );
                            })
                          : finalFilteredProduct.map(product => {
                              return (
                                <ProductCopy
                                  onProductAdd={onProductAdd}
                                  onProductRemove={onProductRemove}
                                  bundleData={props.bundleData}
                                  product={product}
                                  selectedProducts={selectedProducts}
                                  minProduct={minProduct}
                                  maxProduct={maxProduct}
                                  subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
                                  key={product?.id}
                                />
                              );
                            })}
                      </div>
                    </div>

                    {isRightShowSidebar && (
                      <div className={`custom-right-sidebar custom-sidebar as-px-4 as-py-5`} dangerouslySetInnerHTML={{__html: subscriptionBundleSettingsEntity?.rightSidebarHTML}}></div>
                    )}
                  </div>

                  {(!(props?.bundleData?.bundle?.buildABoxType !== 'SINGLE_PRODUCT') ||
                    props?.shopInfo?.buildBoxVersion === 'V2_IFRAME') && (
                    <div className="as-mt-6 as-flex as-text-center as-justify-center as-px-4 as-items-center">
                      {!(props?.bundleData?.bundle?.buildABoxType !== 'SINGLE_PRODUCT') && (
                        <p
                          onClick={() => {
                            props?.setShowProductSelectionScreen(false);
                          }}
                          class="as-mx-2 as-text-center as-cursor-pointer as-text-indigo-600 as-text-sm as-cta as-cta_modal-close"
                        >
                          <ArrowLeft />
                          {props?.subscriptionBundleSettingsEntity?.previousStepButtonTextV2 || 'Previous Step'}
                        </p>
                      )}
                      <button
                        disabled={!props?.isNextButtonValid}
                        onClick={() => {
                          props?.setIsReviewBundleModalOpen(true);
                          //   setTimeout(() => {
                          //     if (window.parent.top.document.querySelector('#appstle_iframe')) {
                          //         window.parent.top.document.querySelector('#appstle_iframe').style.height = '100vh'
                          //         window.parent.top.dispatchEvent(new Event('resize'));
                          //     }
                          // }, 10)
                        }}
                        className="as-mx-2 as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                      >
                        {props?.subscriptionBundleSettingsEntity?.nextStepButtonTextV2 || 'Next Step'}
                        <ArrowRight />
                      </button>
                    </div>
                  )}
                  {props?.bundleData?.bundle?.buildABoxType === 'CLASSIC' && (
                    <div className="as-mb-12 as-flex as-text-center as-justify-center as-px-4 as-items-center">
                      <button
                        type="button"
                        className="as-mt-6 as-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-bg-indigo-600 as-px-6 as-py-3 as-text-base as-font-medium as-text-white as-shadow-sm hover:as-bg-indigo-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                        onClick={() => {
                          checkoutBundle();
                        }}
                        disabled={renderDisabled()}
                      >
                        {isCheckingOut ? (
                          <Loader />
                        ) : (
                          `${minQtyButtonText ||
                            minAmountButtonText ||
                            props.bundleData?.bundle?.proceedToCheckoutButtonText ||
                            props?.subscriptionBundleSettingsEntity?.proceedToCheckoutButtonText ||
                            'Proceed to checkout'}`
                        )}
                      </button>
                    </div>
                  )}
                </>
              )}
            </div>
          )}
          {(props?.isReviewBundleModalOpen && (props?.bundleData?.bundle?.buildABoxType === "SINGLE_PRODUCT")) &&
             <ReviewBundle
              selectedProducts={selectedProducts}
              shopInfo={props?.shopInfo}
              setIsReviewBundleModalOpen={props?.setIsReviewBundleModalOpen}
              isReviewBundleModalOpen={props?.isReviewBundleModalOpen}
              selectedSingleProduct={props?.selectedSingleProduct}
              selectedProductSellingPlans={props?.selectedProductSellingPlans}
              subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
              selectedMasterProductData={props?.selectedMasterProductData}
              bundleData={props.bundleData}
              subscriptionPlans={subscriptionPlans}
             />
          }
        </div>
        {((props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT")) && <BundleCart
          preSelectedProducts={preSelectedProducts}
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
          discountedSubTotal={discountedSubTotal}
          getDiscountedPrice={getDiscountedPrice}
          getDiscountedPerDeliveryPrice={getDiscountedPerDeliveryPrice}
          onProductDelete={onProductDelete}
          tieredDiscount={tieredDiscount}
          subscriptionPlans={subscriptionPlans}
          setShowSellingPlanScreen={setShowSellingPlanScreen}
          setSelectedSellingPlan={setSelectedSellingPlan}
          shopInfo={props?.shopInfo}
          isReviewBundleModalOpen={props?.isReviewBundleModalOpen}
          checkoutBundle={checkoutBundle}
          setPropertyDetails={setPropertyDetails}
          propertyDetails={propertyDetails}
          errorMessage={errorMessage}
          renderDisabled={renderDisabled}
          renderLimitText={renderLimitText}
          minQtyButtonText={minQtyButtonText}
          setMinAmountButtonText={setMinAmountButtonText}
          minAmountButtonText={minAmountButtonText}
          renderMinimumAmountText={renderMinimumAmountText}
        />}
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
