import React, { useEffect, useRef, useState } from 'react';
import _ from 'lodash';
import { MinusIcon, PlusIcon } from '@heroicons/react/24/outline';
import TailwindLoader from './../Subscription/Loader';
function EditSingleBundleProductContent(props) {
  const {
    product,
    renderSelectedProduct,
    handleAddRemoveProduct,
    entity,
    handleChangeEditProductQuantity,
    totalProductsAdded,
    applicableSettings,
    isProductSwap,
    prodcutToSwap,
    removeSwapProduct,
    customerPortalSettingEntity
  } = props;

  const [qty, setQty] = useState(entity?.quantity || 0)
  const [isProductSwapClick, setIsProductSwapClick] =useState(false)
  useEffect(() => {
    if(!isProductSwap){
      if (qty === 0) {
        handleAddRemoveProduct(false, product)
      } else {
        handleChangeEditProductQuantity(qty, product);
      }
    }
  }, [qty])

  const [productDetail, setProductDetail] = useState(null);
  useEffect(() => {
    if (product?.productHandle) {
      getProductDetail();
    }
  }, [product]);

  const getProductDetail = async () => {
    try {
      const response = await fetch(`${Shopify?.routes?.root || '/'}products/${product?.productHandle}.js`);

      if (response.ok) {
        const productData = await response.json();
        setProductDetail(productData);
      }
    } catch (error) {
      console.error('Error fetching product detail:', error);
      // Handle the error appropriately, e.g., display an error message to the user
    }
  };

  return (
    <>
    <div class="as-group as-relative as-cursor-pointer as-h-full as-flex as-flex-col as-shadow-md as-rounded-lg as-overflow-hidden hover:as-shadow-lg as-transition-all as-bg-white">
              <div
                class="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
              >
                <div
                  style={{
                    backgroundImage: `url(${product?.imageSrc})`,
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
                />
                <img src={product?.imageSrc} alt="" class="as-h-full as-relative" />
              </div>
              <div class="as-mt-4 as-mb-6">
                <h3 class="as-text-gray-900 as-text-lg as-text-center as-px-6">
                  <a href={`#`} target="_blank">
                    {/* <span aria-hidden="true" class="as-absolute as-inset-0"></span> */}
                    {product?.title}
                  </a>
                </h3>
                {/* <input
                    className="!as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-rounded as-border-gray-300 as-focus:ring-blue-500 as-focus:ring-2 as-hidden"
                    id={product?.id}
                    checked={renderSelectedProduct(product)}
                    type="checkbox"
                    onChange={(event) => {
                      handleAddRemoveProduct(true, product)
                    }}
                    style={{width: '16px'}}
                  /> */}
                {/* <p class="as-mt-1 as-text-sm as-text-gray-500">Black</p> */}
              </div>
      
              <>
                { !isProductSwap ? 
                  <div className="as-mt-auto as-mb-6">
                      <div className="as-flex as-justify-center as-items-center as-mb-3 as-mt-auto as-px-6">
                         {qty === 0 ? <>
                          <button
                          data-action="increment"
                          disabled={totalProductsAdded >= (applicableSettings.maxQuantity ? applicableSettings.maxQuantity : 99999999999999999)}
                          class="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                          onClick={() => {
                            handleAddRemoveProduct(true, product)
                            setQty(1)
                          }}
                        >
                          {'Add'}
                        </button>
                         </> : <div className="as-border-gray-200 as-flex as-flex-row as-rounded-lg as-relative as-items-center">
                                <button
                                  data-action="decrement"
                                  class="as-inline-flex as-justify-center as-items-center as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                                  onClick={() => {
                                    setQty(old => old - 1 >= 0 ? old - 1 : 0)
                                  }}
                                >
                                  <MinusIcon className="as-h-3 as-w-3" />
                                </button>
                                <div
                                  className="as-border-gray-300 as-border-3 as-w-8 as-justify-center as-outline-none focus:as-outline-none as-text-center  as-font-semibold as-text-xs hover:as-text-black focus:as-text-black  md:as-text-basecursor-default as-flex as-items-center as-text-gray-700"
                                  name="custom-input-number"
                                >
                                   {qty || 0}
                                  {/* <input
                                    ref={quantityRef}
                                    className={"as-hidden focus:as-ring-indigo-500 focus:as-border-indigo-500 as-w-full as-p-2.5 sm:as-text-sm as-border-gray-300 as-rounded-md form-control"}
                                    value={qty || 1}
                                    type="number"
                                    onChange={event => handleChangeEditProductQuantity(event.target.value, product)}
                                  /> */}
                                </div>
                                <button
                                  data-action="increment"
                                  disabled={totalProductsAdded >= (applicableSettings.maxQuantity ? applicableSettings.maxQuantity : 99999999999999999)}
                                  class="as-inline-flex as-justify-center as-items-center  as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                                  onClick={() => {
                                    setQty(old => old + 1)
                                  }}
                                >
                                  <PlusIcon className="as-h-3 as-w-3" />
                                </button>
                          </div>}
                      </div>
                  </div> : <div className="as-mt-auto as-mb-6">
                      <div className="as-flex as-justify-center as-items-center as-mb-3 as-mt-auto as-px-6">
                        {isProductSwapClick ? <TailwindLoader size="10"/> :  <button
                          data-action="decrement"
                          disabled={productDetail && !productDetail?.available}
                          class="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                          onClick={() => {
                            setIsProductSwapClick(true)
                            removeSwapProduct(prodcutToSwap.product, product)                       
                          }}
                        >
                          {productDetail && !productDetail?.available ? `${customerPortalSettingEntity?.outOfStockButtonTextV2 || 'Out Of Stock'}` : `${customerPortalSettingEntity?.addProductSwapButtonTextV2 || 'Confirm Swap'}`}
                        </button>}
                         
                        
                      </div>
                  </div>
                }
              </>
      
            </div>

    </>
  );
}

export default EditSingleBundleProductContent;