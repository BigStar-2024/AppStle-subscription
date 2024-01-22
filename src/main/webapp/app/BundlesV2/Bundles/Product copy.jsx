import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import { MinusIcon, PlusIcon } from '@heroicons/react/24/outline';
import { Input } from 'reactstrap';

function ProductCopy(props) {
  const {
    product,
    onProductAdd,
    onProductRemove,
    selectedProducts,
    maxProduct,
    subscriptionBundleSettingsEntity
  } = props;

  const [isProductAvailable, setIsProductAvailable] = useState(true);

  const [selectedProductItem, setSelectedProductItem] = useState(null);
  const [isMaxProductLimitReached, setIsMaxProductLimitReached] = useState(false);

  const [selectedVariant, setSelectedVariant] = useState({});

  const [productData, setProductData] = useState({});
  const [productImage, setProductImage] = useState('');
  const [isReadMore, setIsReadMore]= useState(false);
  const [sku, setSku] = useState('');

  useEffect(() => {
    setIsMaxProductLimitReached(_.sumBy(selectedProducts, 'quantity') >= maxProduct);
  }, [selectedProducts]);

  useEffect(() => {
    // Find the selected product item that matches the selected variant
    const item = selectedProducts?.find(item => item?.variant?.id === selectedVariant?.id);
  
    // Update the selectedProductItem state accordingly
    setSelectedProductItem(item || null);
  }, [selectedProducts, selectedVariant]);

  useEffect(() => {
    if (product?.productHandle) {
      fetch(`${Shopify?.routes?.root || '/'}products/${product?.productHandle}.js`)
        .then(res => res.json())
        .then(data => setProductData(data));
    }
  }, [product]);

  useEffect(() => {
    if (product?.productHandle && Object.keys(productData).length !== 0) {
      if (product?.type == 'VARIANT') {
        let variant = productData?.variants?.find(variant => variant.id == product?.id);  
        if (variant) {
          setIsProductAvailable(variant?.available);
          setProductImage(variant?.featured_image ? variant?.featured_image?.src : productData?.featured_image);  
        }
        setSku(variant?.sku)
      } else {
        if (productData?.featured_image) {
          setProductImage(productData?.featured_image);
        }
        setIsProductAvailable(productData?.available);
        setSku(productData?.variants[0]?.sku)
      }
    } else {
      setProductImage(product?.imageSrc);
    }
  }, [productData]);

  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };

  useEffect(() => {
    if (subscriptionBundleSettingsEntity?.isMergeIntoSingleBABVariantDropdown) {
      if (product?.variants?.length > 0) {
        const firstVariant = product.variants[0];
        setSelectedVariant({
          ...product,
          id: Number(firstVariant.id) || product.id,
          title: firstVariant.name,
          variants: null,
        });
      }
    } else {
      setSelectedVariant({ ...product });
    }
  }, [product]);
  
  const handleVariantChange = (id) => {
    const variant = product?.variants?.find((variant) => variant.id == id);
    if (variant) {
      setSelectedVariant({
        ...product,
        id: Number(id) || product.id,
        title: variant.name,
        variants: null,
      });
    }
  };
  
  return (
    <>
     {<div className={`as-group as-relative as-cursor-pointer as-h-full as-flex as-flex-col as-shadow-md as-rounded-lg as-overflow-hidden hover:as-shadow-lg as-transition-all as-bg-white ${isProductAvailable ? 'as-product-bab-available' : 'as-product-bab-not-available'}`}>
      {subscriptionBundleSettingsEntity?.enableRedirectToProductPage ?
      <a href={`https://${Shopify.shop}/${productData?.url}`} target="_blank">
        <div
            className="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
          >
            <div
              style={{
                backgroundImage: `url(${productImage ? productImage : require('./BlankImage.jpg')})`,
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
            <img src={productImage ? productImage : require('./BlankImage.jpg')} alt="" className="as-h-full as-relative" />
          </div>
      </a> :
      <div
            className="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
          >
            <div
              style={{
                backgroundImage: `url(${productImage ? productImage : require('./BlankImage.jpg')})`,
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
            <img src={productImage ? productImage : require('./BlankImage.jpg')} alt="" className="as-h-full as-relative" />
       </div>
       }
        <div className="as-mt-4 as-mb-6">
          <h3 className="as-text-gray-900 as-text-lg as-text-center as-px-6">
            {subscriptionBundleSettingsEntity?.enableRedirectToProductPage ? (
              <a href={`https://${Shopify.shop}/${productData?.url}`} target="_blank">
                {product.title}
              </a>
            ) : (
              <>{product.title}</>
            )}
          </h3>
          {((!subscriptionBundleSettingsEntity?.disableProductDescription) && (productData?.description || productData?.body_html) && (subscriptionBundleSettingsEntity?.descriptionLength > 0)) && (
            <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3" style={{"padding": "5px 15px", textAlign: "center"}}>
              {extractTextFromHtml(productData?.description || productData?.body_html).length > 0 ? 
                  <>
                <span>  
                  {
                    extractTextFromHtml(productData?.description || productData?.body_html)?.substring(0, isReadMore ? extractTextFromHtml(productData?.description || productData?.body_html).length : subscriptionBundleSettingsEntity?.descriptionLength ?? 200 )
                  }
                </span>
                  {
                    ((extractTextFromHtml(productData?.description || productData?.body_html).length > subscriptionBundleSettingsEntity?.descriptionLength ?? 200) && (subscriptionBundleSettingsEntity?.readLessTextV2 && subscriptionBundleSettingsEntity?.readMoreTextV2)) ?
                    <> 
                      {' '}
                      <button className="as-w-full as-border as-border-transparent  as-text-sm sm:as-text-base as-font-medium as-text-black  focus:as-outline-none  sm:as-w-auto sm:as-text-sm ReadMoreButton" onClick={() => setIsReadMore(!isReadMore)}><span className="as-readMore-elipses">{ isReadMore ? '' :  '...' }</span>{ isReadMore ? subscriptionBundleSettingsEntity?.readLessTextV2 :  subscriptionBundleSettingsEntity?.readMoreTextV2  } </button>
                    </> : <span className="as-readMore-elipses">{((extractTextFromHtml(productData?.description || productData?.body_html).length > subscriptionBundleSettingsEntity?.descriptionLength ?? 200) && '...')}</span>
                  }
                  </>
              : ""}
            </p>
          )}
          {(productData?.vendor && subscriptionBundleSettingsEntity?.enableDisplayProductVendor) &&
            <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-vendor" style={{ textAlign: "center" , fontWeight: "bold"}}>
              <span> {productData?.vendor} </span>
            </p>
          }
          {(productData?.type && subscriptionBundleSettingsEntity?.enableDisplayProductType) &&
            <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-type" style={{ textAlign: "center", fontWeight: "500"}}>
              <span> {productData?.type} </span>
            </p>
          }
          {subscriptionBundleSettingsEntity?.isMergeIntoSingleBABVariantDropdown && product?.variants?.length > 1 && (
            <div className="as-mb-3 as-mt-auto  as-px-3 lg:as-px-6">
              <div className="d-flex" style={{ alignItems: 'center' }}>
                <Input
                  value={selectedVariant?.id}
                  type="select"
                  onChange={event => {
                    handleVariantChange(event.target.value);
                  }}
                  className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                >
                  {product?.variants?.map((variant, index) => {
                    return (
                      <option key={variant?.id} value={variant?.id}>
                        {variant?.title}
                      </option>
                    );
                  })}
                </Input>
              </div>
            </div>
          )}
        </div>
        <>
          {
            <div className="as-mt-auto as-mb-6">
              <div className="as-flex as-justify-center as-items-center as-mb-3 as-mt-auto as-px-6">
                <div className="as-border-gray-200 as-flex as-flex-row as-rounded-lg as-relative as-items-center">
                  {selectedProductItem?.quantity ? <>
                    <button
                      data-action="decrement"
                      className="as-inline-flex as-justify-center as-items-center as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                      onClick={() => {
                        if (onProductRemove) {
                          onProductRemove({ ...selectedVariant }, {}, -1);
                        }
                      }}
                    >
                      <MinusIcon className="as-h-3 as-w-3" />
                    </button>
                    <div
                      className="as-border-gray-300 as-border-3 as-w-8 as-justify-center as-outline-none focus:as-outline-none as-text-center  as-font-semibold as-text-xs hover:as-text-black focus:as-text-black  md:as-text-basecursor-default as-flex as-items-center as-text-gray-700"
                      name="custom-input-number"
                    >
                      {selectedProductItem?.quantity || 0}
                    </div>
                    <button
                      data-action="increment"
                      disabled={isMaxProductLimitReached}
                      className="as-inline-flex as-justify-center as-items-center  as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                      onClick={() => {
                        if (onProductAdd) {
                          onProductAdd({ ...selectedVariant, imageSrc: productImage, sku: sku }, {}, 1);
                        }
                      }}
                    >
                      <PlusIcon className="as-h-3 as-w-3" />
                    </button>
                  </> : <button
                    disabled={isMaxProductLimitReached || !isProductAvailable}
                    data-action="increment"
                    className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-sm lg:as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                    onClick={() => {
                      if (onProductAdd) {
                        onProductAdd({ ...selectedVariant, imageSrc: productImage, sku: sku }, {}, 1);
                      }
                    }}
                  >
                    {isProductAvailable ? `${subscriptionBundleSettingsEntity?.addButtonText || 'Add'}` : `${subscriptionBundleSettingsEntity?.outOfStockTextV2 || 'Out of stock'}`}
                  </button>
                }
                </div>
              </div>
            </div>
          }
        </>
      </div>}
    </>
  );
}

export default ProductCopy;