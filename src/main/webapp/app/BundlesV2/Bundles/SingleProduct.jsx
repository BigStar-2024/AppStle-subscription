import React, { useEffect, useState } from 'react';
import { formatPrice } from './Bundle.util';
import { CheckCircle } from '@mui/icons-material';

function SingleProduct(props) {
  const {
    product,
    bundleData,
    setSelectedSingleProduct,
    selectedSingleProduct,
    setSelectedProductSellingPlans,
    setSelectedMasterProductData,
    subscriptionBundleSettingsEntity,
    setSingleProductAllData
  } = props;

  const [productData, setProductData] = useState({});
  const [subscriptionDetails, setSubscriptionDetails] = useState({});
  const [sellingPlanIdsInContext, setSellingPlanIdsInContext] = useState([]);
  const [sellingPlans, setSellingPlans] = useState([]);
  const [showCompareAtPrice, setShowCompareAtPrice] = useState(false);
  const [sellingPlanToDisplay, setSellingPlanToDisplay] = useState({});
  const [isProductSelected, setIsProductSelected] = useState(false);
  const [isReadMore, setIsReadMore]= useState(false);
  const [selectedVariant, setSelectedVariant] = useState(null)



  useEffect(() => {
    fetch(`${Shopify?.routes?.root || '/'}products/${product?.handle}.js`)
      .then(res => res.json())
      .then(data => {
        let productVariant = (data?.variants?.filter(variant => variant?.id === product?.id)).pop();
        if (!productVariant && (product?.id === data?.id)) {
          productVariant = data;
        }
        if(productVariant) {
          setSelectedVariant(productVariant);
        }
        setProductData(data);
        setSingleProductAllData(old => [...old, {...productVariant}])
      });
    // setProductData({"id":8014029029659,"title":"7 Day Meal","handle":"7-day-meal","description":"\u003cdiv class=\"price\"\u003e\n\u003cp\u003ePlease visit a \u003ca href=\"http:\/\/www.teslamotors.com\/findus\/service\"\u003eTesla Service Center\u003c\/a\u003e to purchase.\u003c\/p\u003e\n\u003c\/div\u003e\n\u003cdiv id=\"description\" class=\"body\"\u003e\n\u003cdiv\u003e\n\u003cp\u003ePrice is per tire. Tax will be charged at the time of purchase. Global pricing may vary.\u003c\/p\u003e\n\u003cp\u003eTesla PN:\u00a06005890-00-A\u003c\/p\u003e\n\u003c\/div\u003e\n\u003cdiv\u003e\u003cspan\u003eAll-season tread with wide circumferential grooves offers enhanced wet traction and all-season capabilities.\u00a0The aggressive, asymmetric tread design offers enhanced traction and precise cornering.\u003c\/span\u003e\u003c\/div\u003e\n\u003cul\u003e\n\u003cli\u003e\u003cspan\u003eSize:\u00a0245\/ 45 R19\u003c\/span\u003e\u003c\/li\u003e\n\u003cli\u003e\u003cspan\u003eSidewall Style: Blackwall\u003c\/span\u003e\u003c\/li\u003e\n\u003cli\u003e\u003cspan\u003ePattern: Goodyear Eagle RS-A2\u003c\/span\u003e\u003c\/li\u003e\n\u003cli\u003eCategory: Performance All Season\u00a0\u003c\/li\u003e\n\u003c\/ul\u003e\n\u003cp\u003eCost of installation labor is not included in price, please contact your nearest \u003ca href=\"https:\/\/www.tesla.com\/findus\/service\" target=\"_blank\" rel=\"noopener noreferrer\"\u003e\u003cspan\u003eService Center\u003c\/span\u003e\u003c\/a\u003e\u00a0to confirm the labor rate and availability.\u003c\/p\u003e\n\u003cp\u003e\u003cspan\u003eFREE SHIPPING. As with all parts requiring installation, there are no extra shipping fee to ship to the Service Center. A savings of up to $20 per tire.\u003c\/span\u003e\u00a0\u003c\/p\u003e\n\u003c\/div\u003e","published_at":"2022-11-22T21:12:57+05:30","created_at":"2022-11-22T21:12:57+05:30","vendor":"vendor-name-7","type":"Wheels and Covers","tags":["Wheels and Covers"],"price":18000,"price_min":18000,"price_max":18000,"available":true,"price_varies":false,"compare_at_price":null,"compare_at_price_min":0,"compare_at_price_max":0,"compare_at_price_varies":false,"variants":[{"id":44063723159835,"title":"Default Title","option1":"Default Title","option2":null,"option3":null,"sku":null,"requires_shipping":true,"taxable":true,"featured_image":null,"available":true,"name":"7 Day Meal","public_title":null,"options":["Default Title"],"price":18000,"weight":0,"compare_at_price":null,"inventory_management":null,"barcode":null,"requires_selling_plan":false,"selling_plan_allocations":[{"price_adjustments":[],"price":18000,"compare_at_price":null,"per_delivery_price":18000,"selling_plan_id":688703209755,"selling_plan_group_id":"d91ce22e122d17cea686a042cec7766ae92188f5"},{"price_adjustments":[],"price":18000,"compare_at_price":null,"per_delivery_price":18000,"selling_plan_id":688703242523,"selling_plan_group_id":"d91ce22e122d17cea686a042cec7766ae92188f5"}]}],"images":["\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_RS-A2_logo_1024x1024_ba4ce42e-efda-4230-a87f-a32f9352b808.jpg?v=1669131779","\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_Eagle_1024x1024_85c9fd32-eed0-43f7-8837-84bacf6684aa.jpg?v=1669131779"],"featured_image":"\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_RS-A2_logo_1024x1024_ba4ce42e-efda-4230-a87f-a32f9352b808.jpg?v=1669131779","options":[{"name":"Title","position":1,"values":["Default Title"]}],"url":"\/products\/7-day-meal","media":[{"alt":null,"id":32216569512219,"position":1,"preview_image":{"aspect_ratio":0.962,"height":944,"width":908,"src":"https:\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_RS-A2_logo_1024x1024_ba4ce42e-efda-4230-a87f-a32f9352b808.jpg?v=1669131779"},"aspect_ratio":0.962,"height":944,"media_type":"image","src":"https:\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_RS-A2_logo_1024x1024_ba4ce42e-efda-4230-a87f-a32f9352b808.jpg?v=1669131779","width":908},{"alt":null,"id":32216569544987,"position":2,"preview_image":{"aspect_ratio":1.0,"height":944,"width":944,"src":"https:\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_Eagle_1024x1024_85c9fd32-eed0-43f7-8837-84bacf6684aa.jpg?v=1669131779"},"aspect_ratio":1.0,"height":944,"media_type":"image","src":"https:\/\/cdn.shopify.com\/s\/files\/1\/0679\/8121\/3979\/products\/Goodyear_Eagle_1024x1024_85c9fd32-eed0-43f7-8837-84bacf6684aa.jpg?v=1669131779","width":944}],"requires_selling_plan":false,"selling_plan_groups":[{"id":"d91ce22e122d17cea686a042cec7766ae92188f5","name":"Build A Box","options":[{"name":"Delivery every","position":1,"values":["1MONTH1MONTHMIN_CYCLES=NULLMAX_CYCLES=NULLfalsenullMonthly","1WEEK1WEEKMIN_CYCLES=NULLMAX_CYCLES=NULLfalsenullWeekly"]}],"selling_plans":[{"id":688703209755,"name":"Monthly","description":null,"options":[{"name":"Delivery every","position":1,"value":"1MONTH1MONTHMIN_CYCLES=NULLMAX_CYCLES=NULLfalsenullMonthly"}],"recurring_deliveries":true,"price_adjustments":[]},{"id":688703242523,"name":"Weekly","description":null,"options":[{"name":"Delivery every","position":1,"value":"1WEEK1WEEKMIN_CYCLES=NULLMAX_CYCLES=NULLfalsenullWeekly"}],"recurring_deliveries":true,"price_adjustments":[]}],"app_id":"appstle"}]})
  }, [product]);

  useEffect(() => {
    if (bundleData?.subscription?.infoJson) {
      setSubscriptionDetails(JSON.parse(bundleData?.subscription?.infoJson));
    }
  }, [bundleData]);

  useEffect(() => {
    if (subscriptionDetails?.subscriptionPlans?.length) {
      let sellingPlanIds = [];
      subscriptionDetails?.subscriptionPlans?.forEach(item => {
        sellingPlanIds.push(parseInt(item?.id?.split('/').pop()));
      });
      setSellingPlanIdsInContext(sellingPlanIds);
    }
  }, [subscriptionDetails]);

  useEffect(() => {
    if (sellingPlanIdsInContext?.length && productData?.id) {
      let plans = [];
      let productVariant = (productData?.variants?.filter(variant => variant?.id === product?.id))?.pop();
      if (!productVariant) {
        productVariant = productData?.variants[0]
      }
      productVariant?.selling_plan_allocations.forEach(sellingPlan => {
        if (sellingPlanIdsInContext.indexOf(sellingPlan?.selling_plan_id) !== -1) {
          let sellingPlanGroupId = sellingPlan?.selling_plan_group_id;
          let sellingPlanId = sellingPlan?.selling_plan_id;
          let sellingPlanGroup = productData?.selling_plan_groups
            ?.filter(sellingPlanGroup => sellingPlanGroup?.id === sellingPlanGroupId)
            .pop();
          let sellingPlanItem = sellingPlanGroup?.selling_plans?.filter(selling_plan => selling_plan?.id === sellingPlanId).pop();
          sellingPlan = {
            ...sellingPlan,
            name: sellingPlanItem?.name,
            description: sellingPlanItem?.description,
            variantId: productVariant?.id,
            productId: productData?.id,
            variantName: productVariant?.name,
            productName: productData?.title,
            variantImage: productVariant?.featured_image?.src
          };
          plans.push(sellingPlan);
        }
      });
      setSellingPlans(plans);
    }
  }, [sellingPlanIdsInContext, productData]);

  useEffect(() => {
    if (sellingPlans.length) {
      if (product?.id == productData?.id) {
        let minPriceSellingPlan = sellingPlans?.reduce(function(prev, curr) {
          return prev?.price < curr?.price ? prev : curr;
        });
        setSellingPlanToDisplay(minPriceSellingPlan);
        if (minPriceSellingPlan?.compare_at_price !== minPriceSellingPlan.price) {
          setShowCompareAtPrice(true);
        }
      } else {
        let minPriceSellingPlan = sellingPlans?.find(value => value?.variantId == product?.id);
        setSellingPlanToDisplay(minPriceSellingPlan);
        if (minPriceSellingPlan?.compare_at_price !== minPriceSellingPlan.price) {
          setShowCompareAtPrice(true);
        }
      }
    }
  }, [sellingPlans]);

  useEffect(() => {
    if (selectedSingleProduct) {
      if (selectedSingleProduct?.id === product?.id) {
        setIsProductSelected(true);
        setSelectedProductSellingPlans(sellingPlans);
        setSelectedMasterProductData(productData);
      } else {
        setIsProductSelected(false);
      }
    }
  }, [selectedSingleProduct, sellingPlans, productData, product]);
  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };


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
  },[]);

  return (
    <>
      <div
        onClick={() => {
          if (productData?.available) {
            setSelectedSingleProduct({...product, imageSrc: productData?.featured_image || product?.imageSrc})
          }
        }}
        class={`as-group as-relative as-cursor-pointer as-h-full as-flex as-flex-col as-shadow-md as-rounded-lg as-overflow-hidden hover:as-shadow-lg as-transition-all as-bg-white as-w-[300px] as-m-3 ${isProductSelected &&
          `as-outline-none as-ring-2 as-ring-offset-4 as-ring-indigo-600`}`}
      >
        <div class="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center">
          <ProductCard productData={productData} selectedVariant={selectedVariant} sellingPlanToDisplay={sellingPlanToDisplay} />
        </div>
        <div class="as-mt-4 as-mb-6">
          <h3 class="as-text-gray-900 as-text-lg as-text-center as-px-6">
            {(sellingPlanToDisplay?.productId == product?.id ? productData?.title : (sellingPlanToDisplay?.variantName ? sellingPlanToDisplay?.variantName : productData?.title))}
          </h3>
          {(productData?.vendor && subscriptionBundleSettingsEntity?.enableDisplayProductVendor) &&
            <span className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-vendor-SINGLE-BAB" style={{ textAlign: "center" , fontWeight: "bold"}}>
              <span> {productData?.vendor} </span>
            </span>
          }
          {(productData?.type && subscriptionBundleSettingsEntity?.enableDisplayProductType) &&
            <span className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-type-SINGLE-BAB" style={{ textAlign: "center", fontWeight: "500"}}>
              <span> {productData?.type} </span>
            </span>
          }
          {!subscriptionBundleSettingsEntity?.disableProductDescription &&
            (productData?.description || productData?.body_html) &&
            subscriptionBundleSettingsEntity?.descriptionLength > 0 && (
              <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3" style={{ padding: '5px 15px', textAlign: 'center' }}>
                {extractTextFromHtml(productData?.description || productData?.body_html).length > 0 ? (
                  <>
                    <span>
                      {extractTextFromHtml(productData?.description || productData?.body_html)?.substring(
                        0,
                        isReadMore
                          ? extractTextFromHtml(productData?.description || productData?.body_html).length
                          : subscriptionBundleSettingsEntity?.descriptionLength ?? 200
                      )}
                    </span>
                    {(extractTextFromHtml(productData?.description || productData?.body_html).length >
                      subscriptionBundleSettingsEntity?.descriptionLength ??
                      200) &&
                    subscriptionBundleSettingsEntity?.readLessTextV2 && subscriptionBundleSettingsEntity?.readMoreTextV2 ? (
                      <>
                        {' '}
                        <button
                          className="as-w-full as-border as-border-transparent  as-text-sm sm:as-text-base as-font-medium as-text-black  focus:as-outline-none  sm:as-w-auto sm:as-text-sm ReadMoreButton"
                          onClick={() => setIsReadMore(!isReadMore)}
                        >
                          <span className="as-readMore-elipses">{isReadMore ? '' : '...'}</span>
                          {isReadMore
                            ? subscriptionBundleSettingsEntity?.readLessTextV2 || 'Read Less'
                            : subscriptionBundleSettingsEntity?.readMoreTextV2 || 'Read More'}{' '}
                        </button>
                      </>
                    ) : (
                      <span className="as-readMore-elipses">{(extractTextFromHtml(productData?.description || productData?.body_html).length >
                        subscriptionBundleSettingsEntity?.descriptionLength ??
                        200) &&
                      '...'}</span>
                    )}
                  </>
                ) : (
                  ''
                )}
              </p>
          )}
          <div className="as-mt-1 as-text-center">
            <span className="as-text-sm as-text-gray-500">{formatPrice(sellingPlanToDisplay?.price)}</span>
            {showCompareAtPrice && (
              <span className="as-text-sm as-text-gray-500 as-ml-1 as-line-through">
                {sellingPlanToDisplay?.compare_at_price ? formatPrice(sellingPlanToDisplay?.compare_at_price) : null}
              </span>
            )}
          </div>
          <div className={`as-mt-3 as-grid as-gap-2 as-px-6`}>
            {isProductSelected ? (
              <div
                className={' as-items-center as-justify-center as-selected-single-product-wrapper as-flex as-text-indigo-600'}
                style={{ height: 38 }}
              >
                <CheckCircle className={'as-selected-single-product'} />
                &nbsp;&nbsp;{subscriptionBundleSettingsEntity?.selectedButtonTextV2 || 'Selected'}
              </div>
            ) : (
              <button
                onClick={() => setSelectedSingleProduct(product)}
                disabled={!productData?.available}
                className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
              >
                &nbsp;&nbsp;
                {productData?.available ? (subscriptionBundleSettingsEntity?.selectPlanTextV2 || 'Select') : (subscriptionBundleSettingsEntity?.outOfStockTextV2 || 'Out of stock')}
              </button>
            )}
          </div>
        </div>
      </div>
    </>
  );
}

const ProductCard = ({productData, selectedVariant, sellingPlanToDisplay}) => {
  const [slideIndex, setSlideIndex] = useState(1)

function plusSlides(n, id) {
  showSlides((slideIndex + n), id);
}
function showSlides(n, id) {
  let i;
  let currentSlide = n
  let slides = document.getElementsByClassName(`mySlides__${id}`);
  if (n > slides.length) {
    currentSlide = 1
  }
  if (n < 1) {
    currentSlide = slides.length
  }
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = 'none';
  }
  slides[currentSlide - 1].style.display = 'block';
  setSlideIndex(currentSlide)
}

return (
  <>
    {sellingPlanToDisplay?.variantImage ? (
      <div className="container">
        <div key={1} className={`mySlides mySlides__`} style={{ display: 'block' }}>
          <img className="vertical-align-middle appstle-image-height" src={sellingPlanToDisplay?.variantImage} style={{ width: '100%' }} />
        </div>
      </div>
    ) : (
      <>
        {productData?.images && productData?.images.length > 1 ? (
          <>
            <div className="container">
              {productData?.images.map((image, index) => {
                return (
                  <div key={index} className={`mySlides mySlides__${productData?.id}`} style={{ display: index == 0 ? 'block' : 'none' }}>
                    <img className="vertical-align-middle appstle-image-height" src={image} style={{ width: '100%' }} />
                  </div>
                );
              })}
            </div>
            {productData?.images && productData?.images.length > 1 ? (
              <>
                <a className="appstle-slider-prev" onClick={() => plusSlides(-1, productData?.id)}>
                  ❮
                </a>
                <a className="appstle-slider-next" onClick={() => plusSlides(1, productData?.id)}>
                  ❯
                </a>
              </>
            ) : null}
          </>
        ) : (
          <>
            <div
              style={{
                backgroundImage: `url(${productData?.featured_image ? productData?.featured_image : require('./BlankImage.jpg')})`,
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
                backgroundSize: 'cover',
                filter: 'blur(10px)',
                position: 'absolute',
                width: '100%',
                top: 0,
                left: '0',
                zIndex: '0',
                height: '100%',
                display: 'block'
              }}
              className="blurredBackgroundProductImage"
            />
            <img
              src={productData?.featured_image ? productData?.featured_image : require('./BlankImage.jpg')}
              alt=""
              class="as-h-full as-relative"
            />
          </>
        )}
      </>
    )}
  </>
);
};

export default SingleProduct;
