/* This example requires Tailwind CSS v2.0+ */
import React, {Fragment} from 'react';
import {Listbox, Transition} from '@headlessui/react';

const people = [
  {
    value: '30Days',
    label: '30 Days'
  },
  {
    value: '60Days',
    label: '60 Days'
  },
  {
    value: '90Days',
    label: '90 Days'
  }
];

function classNames(...classes) {
  return classes.filter(Boolean).join(' ');
}

export const DeliverySelect = ({ sellingPlans, selectedSellingPlan, setSelectedSellingPlan }) => {
  return (
    <Listbox value={selectedSellingPlan} onChange={setSelectedSellingPlan}>
      {({ open }) => (
        <>
          <Listbox.Button
            className="as-mt-1 as-mb-1 as-w-24 as-relative as-w-full as-cursor-default as-rounded-md as-border as-bg-white  as-pl-1 as-pr-2 as-text-left as-shadow-sm focus:as-outline-none focus:as-ring-1 sm:as-text-sm  appstle-subscribe-input-box"
            style={{ minWidth: '120px', width: '100%' }}
          >
            <div className="as-flex as-justify-between">
              <div className={'as-p-1'}>
                <small className="uppercase as-font-bold  appstle-subscribe-frequency-label">Deliver Every</small>
                <span className="as-ml-1 as-block as-truncate as-text-xs  appstle-subscribe-frequency-name">
                  {selectedSellingPlan?.name}
                </span>
              </div>
            </div>
          </Listbox.Button>
          <Transition show={open} as={Fragment} leave="transition ease-in duration-100" leaveFrom="opacity-100" leaveTo="opacity-0">
            <Listbox.Options
              style={{ padding: 0 }}
              className="as-bg-white as-absolute as-divide-y as-w-auto as-z-10 as-mt-1 as-max-h-56 as-w-full as-rounded-md as-py-1 as-text-base as-shadow-lg as-ring-1 as-ring-black as-ring-opacity-5 focus:as-outline-none sm:as-text-sm as-cursor-pointer appstle-subscribe-dropdown-options"
            >
              {sellingPlans.length > 0
                ? sellingPlans.map(plan => (
                    <Listbox.Option
                      key={plan.node.id}
                      className={({ active }) =>
                        classNames(
                          'as-text-gray-900',
                          'as-relative as-cursor-default as-select-none as-py-2 as-pl-3 as-pr-9 as-cursor-pointer appstle-subscribe-dropdown-iten'
                        )
                      }
                      value={plan.node}
                    >
                      {({ selected, active }) => (
                        <div className="as-flex as-items-center">
                          <small className={classNames(selected ? 'as-font-bold' : 'as-font-normal', 'as-ml-3 as-block as-truncate')}>
                            {plan.node.name}
                          </small>
                        </div>
                      )}
                    </Listbox.Option>
                  ))
                : ''}
            </Listbox.Options>
          </Transition>
        </>
      )}
    </Listbox>
  );
};
export default DeliverySelect;
