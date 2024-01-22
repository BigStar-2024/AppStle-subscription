import React, { useCallback, useEffect, useState } from 'react';
import { Button, FormGroup, Input, ListGroup } from 'reactstrap';
import { formatPrice } from 'app/shared/util/customer-utils';
import axios from 'axios';
import { connect, useSelector } from 'react-redux';
import { getProductOptions, getProducts } from 'app/entities/fields-render/data-product.reducer';
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import TailwindLoader from '../Loader';
import ProductItem from './ProductItem';
import Select from 'react-select';
import { MdFilterList } from 'react-icons/md';

const instance = axios.create();

const AddVariantListModel = ({
  getProductOptions,
  getPrdCollectionOptions,
  getPrdVariantOptions,
  productsData,
  productOptions,
  prdCollectionOptions,
  prdVariantOptions,
  getProducts,
  subscriptionEntity,
  gridColumns,
  sellingPlanData,
  currentCycle,
  currentCycleLoaded,
  customerPortalSettingLoaded,
  isSwapProductModalOpen = false,
  setSelectedProductVariant,
  ...props
}) => {
  const {
    value,
    totalTitle,
    purchaseOption,
    setPurchaseOption,
    contractId,
    isMultiSelect,
    searchBarPlaceholder,
    noProductDataMessage,
    setIsProductDetailModalOpen,
    showModuleHeader,
    isProductsListLoading
  } = props;

  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  const subscriptionEntities = useSelector(state => state.subscription.entity);
  const [selectedItems, setSelectedItems] = useState(value ? JSON.parse(value) : []);
  const [fields, setFields] = useState(value ? JSON.parse(value) : []);
  const [cursor, setCursor] = useState(null);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [collection, setCollection] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [infoMessage, setInfoMessage] = useState(false);
  const [isMoreProductLoading, setIsMoreProductLoading] = useState(false);
  const [sellingPlanIds, setSellingPlanIds] = useState([]);
  const [updatedCollection, setUpdatedCollection] = useState();
  const [selectedFilterData, setSelectedFilterData] = useState({ vendor: [], tags: [], productType: [], sorting: [] });
  const [filterURL, setFilterURL] = useState("");
  const [isFilterOpen, setIsFilterOpen] = useState(true);
  const [productFilterConfig, setProductFilterConfig] = useState(null);

  useEffect(() => {
    setCollection([]);
    setUpdatedCollection([]);
    setIsLoading(true);
    setInfoMessage(false);
    if (subscriptionEntities?.lines?.edges?.length) {
      let sellingPlans = subscriptionEntities?.lines?.edges?.map(line => {
        return line?.node?.sellingPlanId;
      });
      if (sellingPlans?.length) {
        setSellingPlanIds([...sellingPlans]);
      }
      loadProducts(
        null,
        false,
        searchValue,
        contractId,
        customerPortalSettingEntity?.productSelectionOption,
        purchaseOption,
        purchaseOption === 'SUBSCRIBE' ? [...new Set(sellingPlans)] : [], filterURL
      );
    }
  }, [purchaseOption]);

  useEffect(() => {
    let sellingPlans = subscriptionEntities?.lines?.edges?.map(line => {
      return line?.node?.sellingPlanId;
    });
    if (sellingPlans?.length) {
      setSellingPlanIds([...sellingPlans]);
    }
  }, [subscriptionEntities]);

  useEffect(() => {
    if (productsData?.pageInfo != {}) {
      setCursor(productsData?.pageInfo?.cursor);
      setNext(productsData?.pageInfo?.hasNextPage);
    }

    if (productsData?.productHandleData) {
      fetchShopifyProducts(productsData?.productHandleData);
    }
    setIsMoreProductLoading(false);
  }, [productsData]);

  const handleSeeMore = (cursor, next) => {
    if (next && cursor) {
      setIsMoreProductLoading(true);
      let sellingPlans = subscriptionEntities?.lines?.edges?.map(line => {
        return line?.node?.sellingPlanId;
      });
      if (sellingPlans?.length) {
        setSellingPlanIds([...sellingPlans]);
      }
      loadProducts(
        cursor,
        next,
        searchValue,
        contractId,
        customerPortalSettingEntity?.productSelectionOption,
        purchaseOption,
        purchaseOption === 'SUBSCRIBE' ? [...new Set(sellingPlans)] : [], filterURL
      );
    }
  };

  const fetchShopifyProducts = async productHandleData => {
    if (productHandleData || productsData?.productHandleData?.length > 0) {
      setIsLoading(true);
      let productsArray = [];
      if (productHandleData) {
        productsArray = [
          ...Object.keys(productHandleData).map(key => {
            if (productHandleData?.[key]?.['handle']) {
              return {
                ...productHandleData[key]
              };
            } else {
              return {
                handle: productHandleData[key]
              };
            }
          })
        ];
      } else {
        setIsLoading(false);
        setInfoMessage(true);
        return;
      }

      if (!productsArray.length) {
        setIsLoading(false);
        setInfoMessage(true);
        return;
      }

      let resultsData = [];
      let globalIndex = 0;
      let collectionData = collection;
      for await (let product of productsArray) {
        globalIndex = globalIndex + 1;
        var prdItem = {};
        const productvariantUrl = `${window?.top?.location.origin}${window?.top?.Shopify?.routes?.root || '/'}products/${product.handle}.js`;
        let productResponse = null;
        try {
          delete instance.defaults.headers.common['Authorization'];
          productResponse = await instance.get(productvariantUrl);
        } catch (err) {
          console.log('Failed to fetched products from shop, may be due to store is password protected !');
        }
        if (productResponse) {
          let productvariants = productResponse.data;
          prdItem.imgSrc = `https:${productvariants?.featured_image}`;
          prdItem.currencyCode = productvariants?.currencyCode;
          prdItem.prdHandleName = productvariants?.handle;
          prdItem.title = productvariants?.title;
          prdItem.requires_selling_plan = productvariants?.requires_selling_plan;
          prdItem.variants = [];
          prdItem.variantLength = productvariants?.variants?.length;
          let sellingPlanIds = subscriptionEntities?.lines?.edges?.map(line => {
            return line?.node?.sellingPlanId;
          });
          productvariants?.variants?.forEach(function(variant, i) {
            let isVariantValid = true;
            if (purchaseOption === 'SUBSCRIBE') {
              let filteredSellingPlan = [];
              if (sellingPlanIds?.indexOf(null) === -1) {
                filteredSellingPlan = sellingPlanIds.map(plan => {
                  return Number(plan?.split('/').pop());
                });
              }
              if (customerPortalSettingEntity?.productSelectionOption === 'PRODUCTS_FROM_ALL_PLANS') {
                if (!variant?.selling_plan_allocations?.length) {
                  isVariantValid = false;
                }
              } else if (customerPortalSettingEntity?.productSelectionOption === 'PRODUCTS_FROM_CURRENT_PLAN') {
                let hasSellingPlan = false;
                variant?.selling_plan_allocations?.forEach(item => {
                  if (filteredSellingPlan?.indexOf(item?.selling_plan_id) !== -1) {
                    hasSellingPlan = true;
                  }
                });
                if (!hasSellingPlan) {
                  isVariantValid = false;
                }
              }
            }

            if (isVariantValid) {
              var item = {...variant};
              if (productvariants.variants.length === 1) {
                item.title = productvariants?.title;
                item.price = productvariants?.price;
              } else if (productvariants.variants.length > 1) {
                item.title = `${variant?.title}`;
                item.price = variant?.price;
              }
              item.id = `gid://shopify/ProductVariant/${variant?.id}`;
              item.uniqueId = variant?.id;
              item.selling_plan_allocations = variant?.selling_plan_allocations;

              if (purchaseOption === 'SUBSCRIBE') {
                if (variant?.available) {
                  prdItem.variants.push(item);
                } else if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                  prdItem.variants.push(item);
                }
              } else {
                if (variant?.available) {
                  prdItem.variants.push(item);
                } else {
                  if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                    prdItem.variants.push(item);
                  }
                }
              }
            }
          });
          prdItem['productData'] = JSON.parse(JSON.stringify(productvariants));
          resultsData.push(prdItem);

          let finalData = [...collectionData, ...resultsData].filter(
            (value, index, self) => index === self.findIndex(t => t?.prdHandleName === value?.prdHandleName)
          );
          collectionData = [...finalData]
        }
      }
      if (collectionData?.length) {
        setCollection([...collectionData]);
        setIsLoading(false);
        setInfoMessage(false);
      } else {
        handleSeeMore(cursor, next);
      }
    } else {
      setIsLoading(false);
      setInfoMessage(true);
    }
  };

  const selectProduct = (product, selectedVariant) => {
    setSelectedItems([{ ...product }]);
    if (setSelectedProductVariant) {
      setSelectedProductVariant(selectedVariant); 
    }
    props.selectedVariantItems({ ...product });
    setIsProductDetailModalOpen(true);
  };

  const handleSearch = event => {
    if (event.target.value !== searchValue) {
      setCollection([]);
      setUpdatedCollection([]);
      setIsLoading(true);
      setInfoMessage(false);
      setSearchValue(event.target.value);
      loadProductDebounce(event.target.value);
    }
  };

  const loadProductDebounce = _.debounce(handleDebounceFn, 1000);

  function handleDebounceFn(searchData) {
    loadProducts(
      null,
      false,
      searchData,
      contractId,
      customerPortalSettingEntity?.productSelectionOption,
      purchaseOption,
      purchaseOption === 'SUBSCRIBE' && !isSwapProductModalOpen ? [...new Set(sellingPlanIds)] : [], filterURL
    );
  }

  const loadProducts = (cursor, next, search, contractId, productSelectionOption, purchaseOption, sellingPlanIds, searchFilterURL) => {
    getProducts(
      `cursor=${cursor}&next=${next}&search=${encodeURIComponent(search)}&contractId=${contractId}&productSelectionOption=${
        purchaseOption === 'ONE_TIME' ? 'ALL_PRODUCTS' : productSelectionOption
      }&purchaseOption=${purchaseOption}&sellingPlanIds=${productSelectionOption === 'PRODUCTS_FROM_CURRENT_PLAN' ? sellingPlanIds : []}
      ${customerPortalSettingEntity?.productsSizePerPageV2 && `&size=${customerPortalSettingEntity?.productsSizePerPageV2}`}${searchFilterURL}`
    );
  };

  const remove = (evt, id) => {
    evt.preventDefault();
    let productsCopy = fields.filter(product => product.id !== id);
    setFields([...productsCopy]);
  };

  const checkIfProductAlreadySelected = itemId => {
    return selectedItems.findIndex(({ uniqueId }) => uniqueId === itemId) != -1 ? true : false;
  };

  const getdiscountedPrice = (varData, prdData, price, applySellingPlanBasedDiscount) => {
    if (isNaN(currentCycle)) {
      return price;
    }
    if (applySellingPlanBasedDiscount) {
      let fulfillments =
        parseInt(subscriptionEntity?.billingPolicy?.intervalCount / subscriptionEntity?.deliveryPolicy?.intervalCount) || 1;
      let contractBillingCycle = {
        intervalCount: subscriptionEntity?.billingPolicy?.intervalCount,
        inerval: subscriptionEntity?.billingPolicy?.interval
      };

      let sellingPlanId = getSellingPlanIdOfCurrentVariant(contractBillingCycle, varData, prdData);

      if (!sellingPlanId) {
        return varData?.price;
      }

      let sellingPlan = sellingPlanData.filter(item => {
        return parseInt(item?.id?.split('/').pop()) === sellingPlanId;
      });

      sellingPlan = sellingPlan.pop();
      let shopifyCycles = [];
      if (sellingPlan?.discountEnabled2) {
        shopifyCycles.push({
          afterCycle: sellingPlan?.afterCycle2,
          discountType: sellingPlan?.discountType2,
          value: sellingPlan?.discountOffer2
        });
      }

      if (sellingPlan?.discountEnabled) {
        shopifyCycles.push({
          afterCycle: sellingPlan?.afterCycle1,
          discountType: sellingPlan?.discountType,
          value: sellingPlan?.discountOffer
        });
      }

      let discountCycles = [];
      if (sellingPlan && sellingPlan?.appstleCycles) {
        discountCycles = [...sellingPlan?.appstleCycles];
      }
      discountCycles.reverse();
      discountCycles = [...discountCycles, ...shopifyCycles];
      discountCycles = discountCycles.filter(cycle => {
        return cycle?.discountType === 'PERCENTAGE' || cycle?.discountType === 'FIXED' || cycle?.discountType === 'PRICE';
      });

      let currentEligiblePricingPolicy = null;

      let currentPrice = 0;
      for (var i = 0; i < discountCycles.length; i++) {
        if (currentCycle >= discountCycles[i]?.afterCycle) {
          currentEligiblePricingPolicy = discountCycles[i];
          break;
        }
      }
      if (currentEligiblePricingPolicy) {
        if (currentEligiblePricingPolicy?.discountType === 'PERCENTAGE') {
          currentPrice = varData?.price * fulfillments * (1 - currentEligiblePricingPolicy?.value / 100);
        } else if (currentEligiblePricingPolicy?.discountType === 'FIXED') {
          currentPrice = varData?.price * fulfillments - (currentEligiblePricingPolicy?.value * 100);
        } else if (currentEligiblePricingPolicy?.discountType === 'PRICE') {
          currentPrice = fulfillments * (currentEligiblePricingPolicy?.value * 100);
        }
      } else {
        currentPrice = varData?.price;
      }
      return currentPrice;
    } else {
      if (subscriptionEntity?.lines?.edges?.length && price) {
        let currentEligiblePricing = null;
        let firstLineItemWithPricingPolicy = subscriptionEntity?.lines?.edges.filter(edge => Boolean(edge?.node?.pricingPolicy))[0];
        let fulfillments =
          parseInt(subscriptionEntity?.billingPolicy?.intervalCount / subscriptionEntity?.deliveryPolicy?.intervalCount) || 1;
        if (firstLineItemWithPricingPolicy) {
          let contractItem = JSON.parse(JSON.stringify(firstLineItemWithPricingPolicy));
          if (contractItem?.node?.pricingPolicy) {
            let pricingPolicy = contractItem?.node?.pricingPolicy;
            if (pricingPolicy?.cycleDiscounts?.length) {
              let reversedAppstleCycle = JSON.parse(JSON.stringify([...pricingPolicy?.cycleDiscounts])).reverse();
              let currentPrice = 0;
              for (var i = 0; i < reversedAppstleCycle.length; i++) {
                if (currentCycle >= reversedAppstleCycle[i]?.afterCycle) {
                  if (!currentEligiblePricing) {
                    currentEligiblePricing = JSON.parse(JSON.stringify(reversedAppstleCycle[i]));
                  }
                }
              }
              if (currentEligiblePricing) {
                if (currentEligiblePricing?.adjustmentType === 'PERCENTAGE') {
                  currentPrice = varData?.price * fulfillments * (1 - currentEligiblePricing?.adjustmentValue?.percentage / 100);
                } else if (currentEligiblePricing?.adjustmentType === 'FIXED') {
                  currentPrice = varData?.price * fulfillments - currentEligiblePricing?.adjustmentValue?.amount;
                } else if (currentEligiblePricing?.adjustmentType === 'PRICE') {
                  currentPrice = fulfillments * varData?.price ;
                }
                return currentPrice || price;
              } else {
                return price;
              }
            } else {
              return price;
            }
          } else {
            return price;
          }
        } else {
          return price;
        }
      } else {
        return price;
      }
    }
  };

  function getSellingPlanIdOfCurrentVariant(contractBillingCycle, varData, prdData) {
    let sellingPlanId = null;
    if (!(prdData?.selling_plan_groups && prdData?.selling_plan_groups?.length)) {
      return sellingPlanId;
    }
    let sellingPlanGroupIds = [];
    if (varData?.selling_plan_allocations && varData?.selling_plan_allocations?.length) {
      varData?.selling_plan_allocations?.forEach(allocation => {
        sellingPlanGroupIds.push(allocation?.selling_plan_group_id);
      });
    }

    sellingPlanGroupIds = sellingPlanGroupIds.filter(function(item, pos) {
      return sellingPlanGroupIds.indexOf(item) === pos;
    });

    let variantSellingPlanGroups = [];
    let productSellingPlans = [];
    prdData?.selling_plan_groups?.forEach(sellingPlanGroup => {
      if (sellingPlanGroupIds?.indexOf(sellingPlanGroup?.id) !== -1) {
        variantSellingPlanGroups.push(sellingPlanGroup);
      } else {
        productSellingPlans.push(sellingPlanGroup);
      }
    });

    [...variantSellingPlanGroups, ...productSellingPlans]?.forEach(sellingPlanGroup => {
      if (!sellingPlanId) {
        sellingPlanGroup?.selling_plans.forEach(sellingPlan => {
          if (!sellingPlanId) {
            var sellingPlanFreqData = sellingPlan?.options[0]?.value;
            if (sellingPlanFreqData) {
              let matchedArray = sellingPlanFreqData.match(/(\d+)(MONTH|DAY|YEAR|WEEK)/g);
              if (matchedArray && (matchedArray[1] === `${contractBillingCycle?.intervalCount}${contractBillingCycle?.inerval}`)) {
                sellingPlanId = sellingPlan?.id;
              }
            }
          }
        });
      }
    });
    return sellingPlanId;
  }

  useEffect(() => {
    if (collection?.length) {
      if (purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag) {
        //Exclude subscription-only products from being added as a one time product, if purcahse option is one time and flag is on then dont allow subscription only product,
        setUpdatedCollection(collection.filter(item => !item.requires_selling_plan));
      } else if (purchaseOption === 'SUBSCRIBE' && customerPortalSettingEntity?.disAllowVariantIdsForSubscriptionProductAdd) {
        // Get the disallowed subscription variant IDs, split them, and trim whitespace
        const ids = (customerPortalSettingEntity?.disAllowVariantIdsForSubscriptionProductAdd || '').split(',').map(id => id.trim());

        // Filter the collection based on disallowed subscription variant IDs
        const updatedCollectionList = (collection || []).map(product => {
          const filteredVariants = (product?.variants || []).filter(v => !ids.includes(v?.uniqueId?.toString()));
          return { ...product, variants: filteredVariants };
        }).filter(product => product?.variants && product?.variants.length > 0);
        setUpdatedCollection(updatedCollectionList);
      } else {
        setUpdatedCollection(collection);
      }
    }
  }, [collection, customerPortalSettingEntity]);

  const checkDisAllowVariantIds = (modifiedPrdData) => {
    let visibleVariants = [];
    if (modifiedPrdData?.variants?.length > 0) {
      visibleVariants = modifiedPrdData?.variants?.filter(a => !customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(a?.uniqueId));
    }
    return visibleVariants;
  }

  const filterToggle = () => {
    setIsFilterOpen(!isFilterOpen);
  };

  const FilterDropdown = data => {
    const { item } = data?.data;
    return (
      <>
        <div>{data?.data?.title}</div>
        {data?.data?.basedOn == 'sorting' ? (
          <Select
            options={item}
            value={selectedFilterData[data?.data?.basedOn]}
            onChange={e => {
              onChangeFilterData(e, data?.data?.basedOn);
            }}
            class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
          />
        ) : (
          <Select
            options={item}
            isMulti={true}
            value={selectedFilterData[data?.data?.basedOn]}
            onChange={e => {
              onChangeFilterData(e, data?.data?.basedOn);
            }}
            class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
          />
        )}
      </>
    );
  };

  const onChangeFilterData = (value, filterType) => {
    let filterData = selectedFilterData;
    if (filterType === 'sorting') {
      filterData[filterType] = [value];
    } else {
      filterData[filterType] = value;
    }
    setSelectedFilterData(filterData);
    buildSearchURL();
  };

  const buildSearchURL = () => {
    let tempURL = "";
    for (var key in selectedFilterData) {
      if (selectedFilterData?.hasOwnProperty(key)) {
        var val = selectedFilterData[key];
        if (val && val?.length > 0) {
          tempURL += "&"+ key + "=" + val?.map(k => k?.value)?.join(",");
        }
      }
    }
    setFilterURL(encodeURI(tempURL));
    changeAdvanceFilter(tempURL);
  }

  const changeAdvanceFilter = (sValue) => {
      setCollection([]);
      setUpdatedCollection([]);
      setIsLoading(true);
      setInfoMessage(false);
      loadProducts(null, false, searchValue, contractId,
        customerPortalSettingEntity?.productSelectionOption, purchaseOption,
        purchaseOption === 'SUBSCRIBE' && !isSwapProductModalOpen ? [...new Set(sellingPlanIds)] : [], sValue
      );
  };


  useEffect(() => {
    if (customerPortalSettingEntity?.productFilterConfig) {
      setProductFilterConfig(JSON.parse(customerPortalSettingEntity?.productFilterConfig));
    }
  }, [customerPortalSettingEntity])

  useEffect(() => {
    if (customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag && !isLoading && purchaseOption == 'ONE_TIME' && !isMoreProductLoading && updatedCollection?.length < 10 && cursor && next) {
      handleSeeMore(cursor, next);
    }
  },[isLoading, isMoreProductLoading, updatedCollection, purchaseOption, customerPortalSettingEntity]);

  
  return (
    <div style={{ fontSize: '15px', padding: '3px' }} className="form-wizard-content">
      <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
        <FormGroup>
          <Input type="hidden" attr="required" value={fields.length} required={true} />
        </FormGroup>
      </ListGroup>

      <div className="multiselect-modal-body">
        <>
          {showModuleHeader && customerPortalSettingEntity?.addAdditionalProduct && customerPortalSettingEntity?.addOneTimeProduct ? (
            <ul
              className="as-flex as-pb-5  as-text-sm as-font-medium as-text-center as-text-gray-500 as-rounded-lg as-divide-x as-divide-gray-200 shadow sm:flex dark:divide-gray-700 dark:text-gray-400">
              {(customerPortalSettingEntity?.selectOneTimePurchaseTabByDefaultV2 === 'true') && <li className="as-w-full">
                <a
                  onClick={() => {
                    if (purchaseOption == 'SUBSCRIBE') {
                      setCursor(null);
                      setNext(false); 
                    }
                    setPurchaseOption('ONE_TIME')
                  }}
                  href="javascript:void(0);"
                  className={`${
                    purchaseOption === 'ONE_TIME' ? `as-active as-bg-indigo-600 as-text-white` : `as-bg-white as-text-gray-900`
                  } as-inline-block as-p-4 as-w-full  hover:text-gray-700 hover:bg-gray-50 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-gray-800 dark:hover:bg-gray-700`}
                >
                  <p>{customerPortalSettingEntity?.oneTimePurchaseTitleCPV2 || 'One Time Purchase'}</p>

                  {/*<p className={`as-inline ${purchaseOption === 'ONE_TIME' ? 'as-text-indigo-100' : 'as-text-gray-500'}`}>
                    {customerPortalSettingEntity?.oneTimePurchaseMessageTextV2 ||
                      'Get your product along with your next subscription order.'}
                  </p>*/}
                </a>
              </li>}
              <li className="as-w-full">
                <a
                  onClick={() => setPurchaseOption('SUBSCRIBE')}
                  href="javascript:void(0);"
                  className={`${
                    purchaseOption === 'SUBSCRIBE' ? `as-active as-bg-indigo-600 as-text-white` : `as-bg-white as-text-gray-900`
                  } as-inline-block as-p-4 as-w-full as-rounded-l-lg focus:ring-4 focus:ring-blue-300  focus:outline-none dark:bg-gray-700 dark:text-white`}
                  aria-current="page"
                >
                  <p>{customerPortalSettingEntity?.addToSubscriptionTitleCPV2 || 'Add to Subscription'}</p>
                  {/*<p className={`as-inline ${purchaseOption === 'SUBSCRIBE' ? 'as-text-indigo-100' : 'as-text-gray-500'}`}>
                    {customerPortalSettingEntity?.applySubscriptionDiscount ? customerPortalSettingEntity?.upSellMessage : ''}
                  </p>*/}
                </a>
              </li>
              {(customerPortalSettingEntity?.selectOneTimePurchaseTabByDefaultV2 === 'false') && <li className="as-w-full">
                <a
                  onClick={() => {
                    if (purchaseOption == 'SUBSCRIBE') {
                      setCursor(null);
                      setNext(false); 
                    }
                    setPurchaseOption('ONE_TIME');
                  }}
                  href="javascript:void(0);"
                  className={`${
                    purchaseOption === 'ONE_TIME' ? `as-active as-bg-indigo-600 as-text-white` : `as-bg-white as-text-gray-900`
                  } as-inline-block as-p-4 as-w-full  hover:text-gray-700 hover:bg-gray-50 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-gray-800 dark:hover:bg-gray-700`}
                >
                  <p>{customerPortalSettingEntity?.oneTimePurchaseTitleCPV2 || 'One Time Purchase'}</p>

                  {/*<p className={`as-inline ${purchaseOption === 'ONE_TIME' ? 'as-text-indigo-100' : 'as-text-gray-500'}`}>
                    {customerPortalSettingEntity?.oneTimePurchaseMessageTextV2 ||
                      'Get your product along with your next subscription order.'}
                  </p>*/}
                </a>
              </li>}

            </ul>
          ) : (
            ''
          )}

          <div className="as-flex as-justify-between">
            {showModuleHeader && (
              <p className="as-text-sm as-font-medium as-text-gray-500 as-py-2 as-mr-80 as-add-product-title">
                {customerPortalSettingEntity?.addToOrderLabelTextV2 || 'Add to order'}
              </p>
            )}
            <div className="as-relative as-text-gray-600 as-flex-grow as-add-product-search-wrapper as-flex">
              <div className='as-relative' style={{flex: 1}}>
              <input
                className="as-w-full as-border-2 as-border-gray-300 as-bg-white as-h-10 as-px-5 as-pr-5 as-rounded-lg as-text-sm focus:as-outline-none as-add-product-search-input"
                type="search"
                name="search"
                placeholder={searchBarPlaceholder}
                onChange={handleSearch}
                value={searchValue}
                style={{ flex: 1 }}
              />
              <button className="as-absolute as-right-0 as-top-0 as-mt-2 as-mr-5 as-add-product-search-button">
                <i className="pe-7s-search as-text-gray-500 as-h-4 as-w-4 as-fill-current as-text-xl as-font-black"></i>
              </button>
              </div>
              {(productFilterConfig && productFilterConfig?.enabled) && 
              <Button onClick={() => filterToggle()}
                className="as-ml-3 as-px-3 as-bg-transparent  as-border-2 as-border-gray-300 as-rounded-lg as-text-sm" > 
                 <MdFilterList color="#000" size={22} />
              </Button>
              }
            </div>
          </div>
          {(productFilterConfig && productFilterConfig?.enabled) && 
            <div className={`as-pt-3 ${!isFilterOpen ? "d-flex": ""} as-gap-2`} hidden={isFilterOpen}>
              {productFilterConfig && productFilterConfig?.filters?.map(filter => (
                <div style={{ width: `${100 / productFilterConfig?.filters?.length}%` }}>
                  <FilterDropdown data={filter} />
                </div>
              ))}
            </div>
          }
          <div>
            <div
              className={`as-mt-2 as-pt-5 as-grid as-gap-4 ${
                gridColumns ? 'as-grid-cols-3' : 'md:as-grid-cols-2'
              } sm:as-pb-6 as-overflow-y-scroll`}
            >
              {updatedCollection &&
                updatedCollection?.length > 0 &&
                updatedCollection?.map((prdData, index) => {
                  return <ProductItem 
                  key={index} 
                  prdData={prdData} 
                  getdiscountedPrice={getdiscountedPrice} 
                  formatPrice={formatPrice} 
                  checkDisAllowVariantIds={checkDisAllowVariantIds} 
                  purchaseOption={purchaseOption} 
                  customerPortalSettingEntity={customerPortalSettingEntity} 
                  selectProduct={selectProduct} 
                  isSwapProductModalOpen={isSwapProductModalOpen}
                  />
                })}
            </div>
            {!isLoading && !isProductsListLoading && next ? (
              <div className="as-flex as-justify-center">
                <button
                  type="button"
                  onClick={() => handleSeeMore(cursor, next)}
                  class="as-items-center as-px-2 as-py-3 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-view-more-products"
                >
                  {isMoreProductLoading ? (
                    <TailwindLoader size="4" />
                  ) : (
                    customerPortalSettingEntity?.seeMoreProductBtnTextV2 || 'See More...'
                  )}
                </button>
              </div>
            ) : null}
          </div>
          {isLoading || isProductsListLoading ? (
            <div className="as-flex as-justify-center as-mt-4">
              <TailwindLoader size="10" />
            </div>
          ) : (
            infoMessage && searchValue && <p className="as-text-gray-500 as-text-sm">{noProductDataMessage}</p>
          )}
        </>
      </div>
    </div>
  );
};

const mapStateToProps = storeState => ({
  productOptions: storeState.prdVariant.prdVariantOptions,
  productsData: storeState.product.productOptions,
  subscriptionEntity: storeState.subscription.entity,
  sellingPlanData: storeState.subscriptionGroup.sellingPlanData,
  currentCycle: storeState.subscriptionGroup.currentCycle,
  currentCycleLoaded: storeState.subscriptionGroup.currentCycleLoaded,
  customerPortalSettingLoaded: storeState.customerPortalSettings.loading,
  isProductsListLoading: storeState.product.loading
});

const mapDispatchToProps = {
  getProductOptions,
  getPrdVariantOptions,
  getProducts
};

export default connect(mapStateToProps, mapDispatchToProps)(AddVariantListModel);
