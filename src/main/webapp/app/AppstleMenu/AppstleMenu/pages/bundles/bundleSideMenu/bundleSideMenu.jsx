import React, { useEffect, useState } from 'react';

const BundleSideMenu = ({ collection, selectedCollection, selectedProductsForAdd, dropDownValue }) => {
  const [totalPrice, setTotalPrice] = useState(0);
  useEffect(() => {
    let sum = 0;
    if (selectedProductsForAdd?.length) {
      selectedProductsForAdd?.map(item => {
        let quantity = item.currentVariant.unitPriceMeasurement.quantityValue;
        let eachProductPrice = quantity * Number(item.currentVariant.price);
        sum = sum + eachProductPrice;
      });
      setTotalPrice(sum);
    } else setTotalPrice(0);
  }, [selectedProductsForAdd]);
  return (
    <div className="as-w-80">
      <span
        className="as-uppercase as-ml-2 as-ext-sm as-font-medium as-text-gray-900 as-text-sm "
        style={{ fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif' }}
      >
        Choose your Collection
      </span>
      <select
        id="countries"
        onChange={e => selectedCollection(e.target.value)}
        value={dropDownValue}
        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500 focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
      >
        <option selected>Select you collect</option>
        {collection?.map(item => {
          return <option value={item.title}>{item.title}</option>;
        })}
      </select>
      {/* <p class="font-light text-gray-500 dark:text-gray-400 indent-8 as-mt-5">
        Our bundle of all bundles, The Ultimate Superfood Bundle delivers exactly what it promises - a complete superfood solution. With 7
        superfood blends each created to empower every aspect of your health and wellbeing - from immunity and gut health to energy & better
        focus.
      </p> */}
      <div className="as-mt-10">
        {/* {selectedProductsForAdd?.length
          ? selectedProductsForAdd.map(item => {
              return (
                <div className="as-flex as-mt-5 as-justify-content-center">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="-15 0 40 25" fill="#4a9951" class="as-w-10 as-h-6">
                    <path
                      fill-rule="evenodd"
                      d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm3.059 8.062a.75.75 0 10-.993-1.124 12.785 12.785 0 00-3.209 4.358L9.53 12.22a.75.75 0 00-1.06 1.06l2.135 2.136a.75.75 0 001.24-.289 11.264 11.264 0 013.214-4.815z"
                      clip-rule="evenodd"
                    />
                  </svg>
                  <span className="as-font-light as-text-black-500 dark:as-text-gray-400 as-ml-2 as-text-sm">{item.title}</span>
                </div>
              );
            })
          : null} */}
      </div>
      <hr className="as-mt-4"></hr>
      <div className="as-mt-4">
        <div class="as-flex as-items-center as-mb-4">
          <input
            id="green-radio-1"
            type="radio"
            value=""
            name="colored-radio"
            class="as-w-4 as-h-4 as-text-[#4a9951] as-bg-gray-100 as-border-gray-300 focus:as-ring-[#4a9951] dark:focus:as-ring-green-600 dark:as-ring-offset-gray-800 focus:as-ring-2 dark:as-bg-gray-700 dark:as-border-gray-600"
          />
          <label
            for="green-radio-1"
            class="as-ml-2 as-text-md as-font-medium as-text-gray-900 dark:as-text-gray-300"
            style={{
              fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif'
            }}
          >
            Subscribe & Save ${totalPrice} GBP
          </label>
        </div>
        <div class="as-flex as-items-center">
          <div class="as-flex as-items-center as-mr-4">
            <input
              id="green-radio-2"
              type="radio"
              value=""
              name="colored-radio"
              class="as-w-4 as-h-4 as-text-[#4a9951] as-bg-gray-100 as-border-gray-300 focus:as-ring-[#4a9951] dark:focus:as-ring-green-600 dark:as-ring-offset-gray-800 focus:as-ring-2 dark:as-bg-gray-700 dark:as-border-gray-600"
            />
            <label
              for="green-radio-2"
              class="as-ml-2 as-text-md as-font-medium as-text-gray-900 dark:as-text-gray-300"
              style={{
                fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif'
              }}
            >
              One Time Order
            </label>
          </div>
        </div>
      </div>
      <div className="as-mt-4 as-flex">
        <p
          class="font-light text-black-500 dark:text-black-400 as-mt-5 as-font-bold"
          style={{ fontFamily: -'apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen-Sans,Ubuntu,Cantarell,Helvetica Neue,sans-serif' }}
        >
          ${totalPrice} USD
        </p>
        {/* <span className="font-light text-black-500 dark:text-black-400 as-mt-5 as-ml-2 as-line-through">£198 </span> */}
      </div>
      <div className="as-mt-4">
        <button
          type="button"
          class=" as-uppercase as-justify-center as-rounded-full as-font-bold as-w-full as-text-white as-bg-[#4a9951] hover:as-bg-[#4a9951]/90 focus:as-ring-4 focus:as-outline-none focus:as-ring-[#4a9951]/50 as-font-medium as-text-sm as-px-4 as-py-4 as-text-center as-inline-flex as-items-center dark:focus:as-ring-[#4a9951]/55"
        >
          Add bundle to cart
        </button>
      </div>
    </div>
  );
};
export default BundleSideMenu;
