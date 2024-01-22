import SelectComponent from '../../../Components/selectComponent';
import React, { useState } from 'react';

const BundleCard = ({
  detail,
  price,
  rating,
  title,
  lastPrice,
  src,
  variants,
  quantityCounter,
  quantity,
  index,
  onChangeVariant,
  productId,
  counterButton,
  productAddHandler
}) => {
  const [selected, setSelected] = useState(variants[0]);
  const selectedVariantHandler = variantID => {
    let selectedVariant = variants.find(item => item.id === variantID);
    if (selectedVariant) {
      setSelected(selectedVariant);
      onChangeVariant(productId, selectedVariant, index);
    }
  };
  return (
    <>
      <div className="as-w-full ">
        <div className="as-flex as-items-center as-mt-2 ">
          <div className="as-w-72">
            <img class="as-rounded-t-lg" src={src} alt="" />
          </div>
          <div>
            <div class="as-p-5 as-max-w-sm as-px-8">
              <h5
                class="as-mb-2 as-text-2xm as-font-bold as-tracking-tight as-text-gray-900 dark:as-text-white"
                style={{
                  fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif'
                }}
              >
                {title}
              </h5>
              <div
                class=" as-text-black-500 dark:as-text-black-400 as-mr-4 as-mb-4 as-font-bold as-font-sans as-font-base"
                style={{
                  fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif'
                }}
              >
                ${price}
              </div>
              <p
                class="as-mb-3 as-font-normal as-text-gray-700 dark:as-text-gray-400 "
                style={{
                  display: 'inlineBlock',
                  height: '58px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  fontSize: '14px',
                  lineHeight: '20px',
                  letterSpacing: '.17px',
                  fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif'
                }}
              >
                {detail}
              </p>
              {variants?.length > 1 ? (
                <SelectComponent variants={variants} selected={selected} selectedVariantHandler={selectedVariantHandler} />
              ) : null}
              {/* {!counterButton ? (
                <div className="as-mt-3">
                  <button
                    type="button"
                    onClick={() => productAddHandler(location, index, productId)}
                    style={{paddingTop:'8px', paddingBottom:'8px'}}
                    class=" as-font-bold as-uppercase as-justify-center as-rounded as-w-24 as-text-white as-bg-[#4a9951] hover:as-bg-[#4a9951]/90 focus:as-ring-4 focus:as-outline-none focus:as-ring-[#4a9951]/50 as-font-medium as-text-sm  as-text-center as-inline-flex as-items-center dark:focus:as-ring-[#4a9951]/55"
                  >
                    {location === 'subscribe' ? location : 'Add'}
                  </button>
                </div>
              ) : null} */}
              {/* {counterButton ? ( */}
              <div className="as-flex as-mt-2">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="#4a9951"
                  class="as-w-6 as-h-6 as-mr-1"
                  onClick={() => {
                    quantityCounter('dicrement', index, productId);
                  }}
                >
                  <path
                    fill-rule="evenodd"
                    d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm3 10.5a.75.75 0 000-1.5H9a.75.75 0 000 1.5h6z"
                    clip-rule="evenodd"
                  />
                </svg>
                <span className="font-light text-black-500 dark:text-black-400 as-font-bold">{quantity}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="#4a9951"
                  class="as-w-6 as-h-6 as-ml-1"
                  onClick={() => {
                    quantityCounter('increment', index, productId);
                  }}
                >
                  <path
                    fill-rule="evenodd"
                    d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zM12.75 9a.75.75 0 00-1.5 0v2.25H9a.75.75 0 000 1.5h2.25V15a.75.75 0 001.5 0v-2.25H15a.75.75 0 000-1.5h-2.25V9z"
                    clip-rule="evenodd"
                  />
                </svg>
              </div>
              {/* ) : null} */}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default BundleCard;
