/* This example requires Tailwind CSS v2.0+ */
import React, { useEffect, useState } from 'react';
import { ShoppingBagIcon } from '@heroicons/react/24/outline';
import { KeyboardArrowLeft } from '@mui/icons-material';
import Select from 'react-select';

function classNames(...classes) {
  return classes.filter(Boolean).join(' ');
}

export default function BundleHeader({
  subscriptionBundleSettingsEntity,
  subscriptionPlans,
  setShowSellingPlanScreen,
  selectedSellingPlan,
  setSelectedSellingPlan,
  setCartOpen,
  selectedSortProduct,
  setSelectedSortProduct,
  searchValue,
  setSearchValue,
  onClickGoBackButton,
  redirectHistory,
  selectedFilterData,
  setSelectedFilterData,
  ...props
}) {
  const [productFilterConfig, setProductFilterConfig] = useState(null);

  const onChangeFilterData = (value, filterType) => {
    setSelectedFilterData(prevFilterData => ({
      ...prevFilterData,
      [filterType]: filterType === 'sorting' ? [value] : value,
    }));
  };

  useEffect(() => {
    setSearchValue(searchValue);
  }, [selectedFilterData]);

  useEffect(() => {
    if (subscriptionBundleSettingsEntity?.productFilterConfig) {
      setProductFilterConfig(JSON.parse(subscriptionBundleSettingsEntity?.productFilterConfig));
    }
  }, [subscriptionBundleSettingsEntity]);

  const [scroll, setScroll] = useState(false);
  useEffect(() => {
    window.addEventListener('scroll', () => {
      if (subscriptionBundleSettingsEntity?.enableSkieyBABHeader) {
        setTimeout(() => {
          setScroll(window.scrollY > 156);
        }, 700);
      } else {
        setScroll(false);
      }
    });
  }, []);

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
    <div className={`as-shadow  ${scroll ? "bab-subscription-top-sticky bab-subscription-top-sticky--open" : ""}`}>
      <div className="as-container as-mx-auto  as-py-4 as-px-6">
        <div className="as-flex as-justify-between as-items-center">
          <div className="as-min-w-0" style={{ display: 'flex' }}>
            <div className="as-text-2xl as-font-bold as-leading-7 as-text-gray-900 sm:as-text-3xl  as-px-6" dangerouslySetInnerHTML={{__html: subscriptionBundleSettingsEntity?.title || 'Build-A-Box'}} />
            {!subscriptionBundleSettingsEntity?.hideProductSearchBox && (
              <div className="input-holder bab-search-text-box" style={{ flex: 1 }}>
                <input
                  type="text"
                  className="as-w-full as-border-2 as-border-gray-300 as-bg-white as-h-10 as-px-5 as-pr-5 as-rounded-lg as-text-sm focus:as-outline-none as-add-product-search-input"
                  placeholder={subscriptionBundleSettingsEntity?.typeToSearchPlaceholderTextV2 || 'Type to search'}
                  value={searchValue}
                  style={{ boxShadow: 'none' }}
                  onChange={e => {
                    setSearchValue(e.target.value);
                  }}
                />
              </div>
            )}
          </div>{' '}
          <div className="as-flex lg:as-mt-0 lg:as-ml-4 as-items-center">
            <button
              type="button"
              className="as-inline-flex as-items-center as-px-4 as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-mr-5"
              onClick={() => {
                setCartOpen(true);
              }}
            >
              <ShoppingBagIcon className="-as-ml-1 as-mr-2 as-h-5 as-w-5" aria-hidden="true" />
              {subscriptionBundleSettingsEntity?.cart || 'Cart'}
            </button>
            {redirectHistory && redirectHistory?.length > 0 && (
              <button
                class="appstle-bab-go-back-button as-flex as-items-center"
                onClick={() => {
                  onClickGoBackButton();
                }}
              >
                <KeyboardArrowLeft style={{ height: '18px' }} /> {subscriptionBundleSettingsEntity?.goBackButtonText || `Go Back`}
              </button>
            )}
          </div>
        </div>

        {productFilterConfig && productFilterConfig?.enabled && (
          <div className={`as-flex as-gap-2  as-px-5 filterDropdown`}>
            {productFilterConfig &&
              productFilterConfig?.filters?.map(filter => (
                <div className='as-pt-4 filterDropdownWidth'>
                  <FilterDropdown data={filter} />
                </div>
              ))}
          </div>
        )}
      </div>
    </div>
  );
}
