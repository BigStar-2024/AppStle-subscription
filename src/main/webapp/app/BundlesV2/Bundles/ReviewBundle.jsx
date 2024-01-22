import React, {useEffect, useState} from 'react';
import {toast} from 'react-toastify';
import './style.scss';
import ReviewBundleSellingPlanList from "./ReviewBundleSellingPlanList";
import {formatPrice} from "./Bundle.util";
import {ArrowRight} from '@mui/icons-material';
import { FormGroup, Input, Label } from 'reactstrap';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

function ReviewBundle(props) {
  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [selectedSellingPlanDisplayName, setSelectedSellingPlanDisplayName] = useState();
  const [formFieldJson, setFormFieldJson] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [propertyDetails, setPropertyDetails]  = useState(null);

  const [showCompareAtPrice, setShowCompareAtPrice] = useState(false);

  const [isAddToCartInProgress, setIsAddToCartInProgress] = useState(false);
  const [cartErrorMessage, setCartErrorMessage] = useState(null)

  useEffect(() => {
    if (!selectedSellingPlan) {
      setSelectedSellingPlan(props?.selectedProductSellingPlans[0]);
    }
  }, [props?.selectedProductSellingPlans]);

  useEffect(() => {
    setSelectedSellingPlanDisplayName(
      props?.selectedProductSellingPlans?.filter(item => item?.id === selectedSellingPlan)?.pop()?.name
    );

    if (selectedSellingPlan?.compare_at_price !== selectedSellingPlan?.price) {
      setShowCompareAtPrice(true);
    }

    setPropertyDetails(null);
    setFormFieldJson(null);
    if (props?.subscriptionPlans && selectedSellingPlan && props?.subscriptionBundleSettingsEntity?.enableCustomAdvancedFields) {
      let filterFormFieldJson = props?.subscriptionPlans.filter(item => item?.id?.split('/')?.pop() == selectedSellingPlan?.selling_plan_id)?.pop()?.formFieldJson;
      if (filterFormFieldJson) {
        setFormFieldJson(JSON.parse(filterFormFieldJson));
      }
    }

  }, [selectedSellingPlan]);


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

  const getDateFormat = (formatJson) => {
    let f = JSON.parse(formatJson);
    return f?.dateFormat?.replace("mm", "MM") || "dd-MM-yyyy";
  };

  const handleDateChange = (e) => {
    setSelectedDate(e);
  }

  const handleTextChange = (e) => {
    let data = propertyDetails || {};
    data[e.target.name] = e.target.value;
    setPropertyDetails({...data});
  }

  // This code retrieves variant information only if the selected item is a specific variant, rather than the entire product.
  const [selectedVariantInfo, setSelectedVariantInfo] = useState(null);
  useEffect(() => {
    setSelectedVariantInfo(props?.selectedMasterProductData?.variants?.find(variant => variant?.id === props?.selectedSingleProduct?.id))
  },[props?.selectedSingleProduct, props?.selectedMasterProductData]);


  let checkoutBundle = async () => {
    setIsAddToCartInProgress(false)
    let defaultVariant = selectedVariantInfo ? selectedVariantInfo : props?.selectedMasterProductData?.variants[0];

    let addPayload = {
      items: [
        {
          id: defaultVariant?.id,
          quantity: 1,
          selling_plan: selectedSellingPlan.selling_plan_id,
          "properties": {
            "products": (props?.selectedProducts.map(item => `${item?.quantity}x ${item?.variant?.title}`)).join(', '),
            "_appstle-bb-id": props?.bundleData?.bundle?.uniqueRef,
            "_appstle-bb-product-sku": (props?.selectedProducts.map(item => (item?.variant?.sku || ''))).join(','),
            ...propertyDetails
          }
        }
      ],
    };
    let config = {
      method: 'POST', // or 'PUT'
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(addPayload)
    }
    await fetch(`${Shopify?.routes?.root}cart/clear.js`);
    let cartResponse = await fetch(`${Shopify?.routes?.root}cart/add.js`, config);
    if (cartResponse.ok) {
      location.href = `${Shopify?.routes?.root}cart`;
      window?.parent?.postMessage(JSON.stringify({
        type: "appstle_message_to_redirect_to_checkout",
        discountCode: "appstle_no_discount",
        redirectType: props.bundleData?.bundle?.bundleRedirect,
        redirectURL: props.bundleData?.bundle?.customRedirectURL
      }));
    } else {
      let cartError = await cartResponse.json();
      setIsAddToCartInProgress(false);
      setCartErrorMessage(cartError?.description)
    }
  }
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

  useEffect(() => {
    window.scrollTo({top: 0, behavior: 'smooth'});
  }, []);

  useEffect(()=> {
    try {
      if (props?.shopInfo?.buildBoxVersion === "V2_IFRAME") {
        let rootDiv = document.getElementById("root").scrollHeight || 0;
        if (rootDiv > 0) {
          top.document.getElementById("appstle_iframe").style.height = (rootDiv + 100) + 'px';
        }
        top?.window?.scrollTo({top: 0, behavior: 'smooth'});
      }
    } catch (error) {
     console.log(error);
    }
  },[])

  return (
    <>
        {
          <div className="as-container as-mx-auto as-px-4">
            <div className="as-review-wrapper">
              <div className="as-review-title-wrapper">
                <h2 class="as-text-2xl as-mb-3">{props?.subscriptionBundleSettingsEntity?.reviewOrderTextV2 || "Review Order"}</h2>
              </div>
              <div className="">
                <p className="as-review-description as-text-sm as-mb-6">
                {props?.subscriptionBundleSettingsEntity?.reviewOrdeDescriptionTextV2 || "Check your items and select your required frequency plan"}

                </p>
              </div>
              <div className="as-grid lg:as-grid-cols-2 as-gap-4">
              <div className="">
                  <h4
                    className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title as-review-bundle-content-title as-mb-6">
                      {props?.subscriptionBundleSettingsEntity?.contentsOnModalTextV2 || "Contents"}
                  </h4>
                  <div className="as-bg-white as-p-8 as-rounded-2xl as-shadow-md">

                    <ul className="as-grid as-gap-4 as-grid-cols-1">
                      {
                        props?.selectedProducts?.map(item => {
                          return <li
                            className="as-text-sm as-my-1 as-review-bundle-content-item">
                              <div className="as-flex">
                                <div
                                  className="as-relative as-flex-shrink-0 as-flex as-items-center">
                                  <img
                                    src={item?.variant?.imageSrc}
                                    className="as-rounded-md as-object-cover as-object-center as-h-[72px] as-w-[72px] as-border as-border-gray-200 "
                                  />
                                  <div class="as-border as-border-gray-200 as-translate-x-1/2 as-translate-y-[-50%] as-text-xs as-h-6 as-w-6 as-z-1 as-flex as-review-bundle-price as-absolute as-right-0 as-top-0 as-rounded-full as-shadow-lg as-p-2 as-bg-white as-z-1 as-items-center as-justify-center">
                                          {item?.quantity}
                                  </div>
                                </div>
                                <div className="as-ml-4 as-flex as-flex-1 as-flex-col">
                                  <h3 className="as-review-bundle-title as-text-gray-900">
                                    {item?.variant?.title}
                                  </h3>
                                </div>
                              </div>
                            </li>
                        })
                      }
                    </ul>

                  </div>

                  </div>
                <div className="as-pl-12 as-review-items-wrapper">
                <h4
                    className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title as-review-bundle-content-title as-mb-6">
                     {props?.subscriptionBundleSettingsEntity?.bundleOnModalTextV2 || "Bundle"}
                  </h4>
                  <div className="as-bg-white as-p-8 as-rounded-2xl as-shadow-md">
                    <div className="">
                    {(formFieldJson && formFieldJson?.length > 0) ?
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
                              {field?.type == "date" && <FormGroup><DatePicker selected={selectedDate} filterDate={(current) => enableDate(current, field)} dateFormat={getDateFormat(field?.config)} onChange={(e) => handleDateChange(e)} minDate={getMinDate(field)} /> </FormGroup>}
                              {field?.type == "text" && <FormGroup><Input name={field.name} onChange={(e) => handleTextChange(e, field)} className="inputText"/></FormGroup> }
                            </div>
                          ) : null
                        }
                      <ReviewBundleSellingPlanList
                        subscriptionBundleSettingsEntity={props?.subscriptionBundleSettingsEntity}
                        subscriptionPlans={props?.selectedProductSellingPlans}
                        selected={selectedSellingPlan}
                        setSelected={setSelectedSellingPlan}
                      />
                    </div>
                    <hr class="as-my-8 as-h-px as-bg-gray-200 as-border-0"/>
                    <div>
                      {/* <h4 className="as-leading-6 as-font-medium as-text-gray-700 as-modal-title">


                      </h4> */}
                      <div className="as-flex">
                        <div
                           className="as-relative as-flex-shrink-0 as-flex as-items-center">
                          <img
                            src={selectedVariantInfo?.featured_image?.src ? selectedVariantInfo?.featured_image?.src : props?.selectedSingleProduct?.imageSrc || props?.selectedMasterProductData?.featured_image}
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
                      <hr class="as-my-8 as-h-px as-bg-gray-200 as-border-0"/>
                      <div className="as-flex as-justify-between as-items-center">
                        <span className="as-text-gray-900">{props?.subscriptionBundleSettingsEntity?.subtotal || "Items Total"}</span>
                        <span className="as-text-2xl as-text-gray-900 as-font-bold as-review-bundle-sale-price">
                          {formatPrice(selectedSellingPlan?.price)}
                        </span>
                      </div>
                      {cartErrorMessage ? <p className='as-text-red-800 as-my-2 as-text-sm'>{cartErrorMessage}</p> : ''}
                      {!(props?.bundleData?.bundle?.buildABoxType !== "SINGLE_PRODUCT") &&
                        <div className="as-mt-6 as-flex as-text-center as-justify-center as-items-center">
                          {/* {<p onClick={() => {
                              props?.setIsReviewBundleModalOpen(false);
                          }}
                              class="as-text-center as-cursor-pointer as-text-indigo-600 as-text-sm as-cta as-cta_modal-close">
                              <ArrowLeft/>
                              {props?.subscriptionBundleSettingsEntity?.previousStepButtonTextV2 || 'Previous Step'}
                          </p>} */}
                          <button
                            onClick={() => {
                              checkoutBundle();
                            }}
                            disabled={!selectedSellingPlan}
                            className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-6 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500  sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                          >
                          {props?.subscriptionBundleSettingsEntity?.addToCartButtonTextV2 || 'Add to Cart'}
                            <ArrowRight/>
                          </button>

                        </div>
                  }
                    </div>
                  </div>


                </div>

              </div>


            </div>
          </div>
        }
    </>
  );
}

export default ReviewBundle;
