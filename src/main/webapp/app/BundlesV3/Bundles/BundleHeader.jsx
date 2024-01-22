/* This example requires Tailwind CSS v2.0+ */
import React from 'react';
import SellingPlanList from './SellingPlanList';
import { ShoppingBagIcon } from '@heroicons/react/24/outline';

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
  ...props
}) {
  return (
    <div className="">
      <div className="as-container as-mx-auto lg:as-flex lg:as-justify-between as-py-8">
        <div className="as-min-w-0">
          <h2 className="as-text-2xl as-font-bold as-leading-7 as-text-gray-900 sm:as-text-3xl sm:as-truncate">
            {subscriptionBundleSettingsEntity?.title || 'Build a Box'}
          </h2>
        </div>
        <div className="as-mt-5 as-flex lg:as-mt-0 lg:as-ml-4">
          {/* <SellingPlanList
            subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
            subscriptionPlans={subscriptionPlans}
            setShowSellingPlanScreen={setShowSellingPlanScreen}
            selected={selectedSellingPlan}
            setSelected={setSelectedSellingPlan}
            hideLabel={true}
            selectedSortProduct={selectedSortProduct}
            setSelectedSortProduct={setSelectedSortProduct}
          /> */}
          <span className="sm:as-ml-3">
            <button
              type="button"
              className="as-inline-flex as-items-center as-px-4 as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
              onClick={() => {
                setCartOpen(true);
              }}
            >
              <ShoppingBagIcon className="-as-ml-1 as-mr-2 as-h-5 as-w-5" aria-hidden="true" />
              {subscriptionBundleSettingsEntity?.cart || 'Cart'}
            </button>
          </span>
        </div>
      </div>
    </div>
  );
}
