/* This example requires Tailwind CSS v2.0+ */
import { Fragment, useState, useEffect } from 'react'
import { Listbox, Transition } from '@headlessui/react'
import { CheckIcon, ChevronUpDownIcon } from '@heroicons/react/20/solid'
import React from 'react';
import { CalendarIcon } from '@heroicons/react/24/outline';

function classNames(...classes) {
  return classes.filter(Boolean).join(' ')
}

export default function SellingPlanList({selectedSortProduct, setSelectedSortProduct, subscriptionPlans, setShowSellingPlanScreen, selected, setSelected, hideLabel, subscriptionBundleSettingsEntity, ...props}) {

  const sortProduct = [
    {
      id: 0,
      name: "Alphabetically A-Z",
      value: "alphabeticallyAZ"
    },
    {
      id: 1,
      name: "Alphabetically Z-A",
      value: "alphabeticallyZA"
    },
    {
      id: 2,
      name: "Price, Low to High",
      value: "lowToHigh"
    },
    {
      id: 3,
      name: "Price, High to Low",
      value: "highToLow"
    },
  ]
  useEffect(function() {
    if (!selected) {
      setSelected(subscriptionPlans[0])
    }
  }, [subscriptionPlans])

  useEffect(function() {
    if (!selectedSortProduct) {
     // setSelectedSortProduct(sortProduct[0])
    }
  }, [sortProduct])

  return (
    <>
    {/* <Listbox value={selectedSortProduct} onChange={setSelectedSortProduct}>
      {({ open }) => (
        <>
          {!hideLabel && <Listbox.Label className="as-block as-text-sm as-font-medium as-text-gray-700">Select Delivery Fequency</Listbox.Label>}
          <div className={`${!hideLabel ? 'as-mt-1' : ''} as-relative as-flex as-w-96`}>
            <Listbox.Button className="as-relative as-w-full as-bg-white as-border as-border-gray-300 as-rounded-md as-shadow-sm as-pl-3 as-pr-10 as-py-2 as-text-left as-cursor-default focus:as-outline-none focus:as-ring-1 focus:as-ring-indigo-500 focus:as-border-indigo-500 sm:as-text-sm w-[300px]"  style={{marginRight: "11px"}}>
              <span className="as-flex as-items-center">
                <span className="as-ml-3 as-block as-truncate">{selectedSortProduct?.name}</span>
              </span>
              <span className="as-ml-3 as-absolute as-inset-y-0 as-right-0 as-flex as-items-center as-pr-2 as-pointer-events-none">
                <ChevronUpDownIcon className="as-h-5 as-w-5 as-text-gray-400" aria-hidden="true" />
              </span>
            </Listbox.Button>

            <Transition
              show={open}
              as={Fragment}
              leave="as-transition as-ease-in as-duration-100"
              leaveFrom="as-opacity-100"
              leaveTo="as-opacity-0"
            >
              <Listbox.Options className="as-absolute as-z-10 as-mt-1 as-w-full as-bg-white as-shadow-lg as-max-h-56 as-rounded-md as-py-1 as-text-base as-ring-1 as-ring-black as-ring-opacity-5 as-overflow-auto focus:as-outline-none sm:as-text-sm">
                {sortProduct.map((sort,index) => (
                  <Listbox.Option
                    key={index}
                    className={({ active }) =>
                      classNames(
                        active ? 'as-text-white as-bg-indigo-600' : 'as-text-gray-900',
                        'as-cursor-default as-select-none as-relative as-py-2 as-pl-3 as-pr-9'
                      )
                    }
                    value={sort}
                  >
                    {({ selected, active }) => (
                      <>
                        <div className="as-flex as-items-center">
                          <span
                            className={classNames((selectedSortProduct?.id === index) ? 'as-font-semibold' : 'as-font-normal', 'as-ml-3 as-block as-truncate')}
                          >
                            {sort?.name}
                          </span>
                        </div>
                        {(selectedSortProduct?.id === index) ? (
                          <span
                            className={classNames(
                              active ? 'as-text-white' : 'as-text-indigo-600',
                              'as-absolute as-inset-y-0 as-right-0 as-flex as-items-center pr-4'
                            )}
                          >
                            <CheckIcon className="as-h-5 as-w-5" aria-hidden="true" />
                          </span>
                        ) : null}
                      </>
                    )}
                  </Listbox.Option>
                ))}
              </Listbox.Options>
            </Transition>
          </div>
        </>
      )}
    </Listbox> */}
    <Listbox value={selected} onChange={setSelected}>
      {({ open }) => (
        <>
          {!hideLabel && <Listbox.Label className="as-block as-text-sm as-font-medium as-text-gray-700 as-flex as-items-center as-mb-2">
            <CalendarIcon className="as-h-6 as-w-6 as-mr-2" aria-hidden="true" />
            {subscriptionBundleSettingsEntity?.selectedFrequencyLabelText}
          </Listbox.Label>}
          <div className={`${!hideLabel ? 'as-mt-1' : ''} as-relative as-flex`}>
            <Listbox.Button className="as-relative as-w-full as-bg-white as-border as-border-gray-300 as-rounded-md as-shadow-sm as-pl-3 as-pr-10 as-py-2 as-text-left as-cursor-default focus:as-outline-none focus:as-ring-1 focus:as-ring-indigo-500 focus:as-border-indigo-500 sm:as-text-sm w-[300px]">
              <span className="as-flex as-items-center">
                <span className="as-ml-3 as-block as-truncate">{selected?.frequencyName}</span>
              </span>
              <span className="as-ml-3 as-absolute as-inset-y-0 as-right-0 as-flex as-items-center as-pr-2 as-pointer-events-none">
                <ChevronUpDownIcon className="as-h-5 as-w-5 as-text-gray-400" aria-hidden="true" />
              </span>
            </Listbox.Button>

            <Transition
              show={open}
              as={Fragment}
              leave="as-transition as-ease-in as-duration-100"
              leaveFrom="as-opacity-100"
              leaveTo="as-opacity-0"
            >
              <Listbox.Options className="as-absolute as-z-10 as-mt-1 as-w-full as-bg-white as-shadow-lg as-max-h-56 as-rounded-md as-py-1 as-text-base as-ring-1 as-ring-black as-ring-opacity-5 as-overflow-auto focus:as-outline-none sm:as-text-sm">
                {subscriptionPlans.map((plan) => (
                  <Listbox.Option
                    key={plan?.id?.split("/").pop()}
                    className={({ active }) =>
                      classNames(
                        active ? 'as-text-white as-bg-indigo-600' : 'as-text-gray-900',
                        'as-cursor-default as-select-none as-relative as-py-2 as-pl-3 as-pr-9'
                      )
                    }
                    value={plan}
                  >
                    {({ selected, active }) => (
                      <>
                        <div className="as-flex as-items-center">
                          <span
                            className={classNames(selected ? 'as-font-semibold' : 'as-font-normal', 'as-ml-3 as-block as-truncate')}
                          >
                            {plan?.frequencyName}
                          </span>
                        </div>

                        {selected ? (
                          <span
                            className={classNames(
                              active ? 'as-text-white' : 'as-text-indigo-600',
                              'as-absolute as-inset-y-0 as-right-0 as-flex as-items-center pr-4'
                            )}
                          >
                            <CheckIcon className="as-h-5 as-w-5" aria-hidden="true" />
                          </span>
                        ) : null}
                      </>
                    )}
                  </Listbox.Option>
                ))}
              </Listbox.Options>
            </Transition>
          </div>
        </>
      )}
    </Listbox>
    </>
  )
}
