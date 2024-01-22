import React, { Fragment } from 'react';
import { Listbox, Transition } from '@headlessui/react';

function classNames(...classes) {
  return classes.filter(Boolean).join(' ');
}

const SelectComponent = ({ variants, selected, selectedVariantHandler }) => {
  return (
    <Listbox value={selected} onChange={selectedVariantHandler}>
      {({ open }) => (
        <>
          <div className="as-relative">
            <Listbox.Button
              style={{ lineHeight: '2px' }}
              className="as-relative as-w-full as-cursor-default as-rounded-md as-border as-border-gray-300 as-bg-white as-py-2 as-pl-3 as-pr-10 as-text-left as-shadow-sm focus:as-border-[#4a9951] focus:as-outline-none focus:as-ring-1 focus:as-ring-[#4a9951] sm:as-text-sm"
            >
              <span className="as-flex as-items-center">
                <img src={selected?.image?.src} alt="" className="as-h-6 as-w-6 as-flex-shrink-0 as-rounded-full" />
                <span className="as-ml-1 as-block as-truncate as-text-xs">{selected.title}</span>
              </span>
              <span className="as-pointer-events-none as-absolute as-inset-y-0 as-right-0 as-ml-3 as-flex as-items-center as-pr-2">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40 30" fill="black" class="as-w-6 as-h-6">
                  <path
                    fill-rule="evenodd"
                    d="M12.53 16.28a.75.75 0 01-1.06 0l-7.5-7.5a.75.75 0 011.06-1.06L12 14.69l6.97-6.97a.75.75 0 111.06 1.06l-7.5 7.5z"
                    clip-rule="evenodd"
                  />
                </svg>
              </span>
            </Listbox.Button>

            <Transition show={open} as={Fragment} leave="transition ease-in duration-100" leaveFrom="opacity-100" leaveTo="opacity-0">
              <Listbox.Options
                className="as-absolute as-z-10 as-mt-1 as-max-h-56 as-w-full as-overflow-auto as-rounded-md as-bg-white as-py-1 as-text-base as-shadow-lg as-ring-1 as-ring-black as-ring-opacity-5 focus:as-outline-none sm:as-text-sm"
                style={{ maxHeight: 'none' }}
              >
                {variants.map(item => (
                  <Listbox.Option
                    key={item.id}
                    className={({ active }) =>
                      classNames(
                        active ? 'as-text-white as-bg-[#4a9951]' : 'as-text-gray-900',
                        'as-relative as-cursor-default as-select-none as-py-2 as-pl-3 as-pr-9'
                      )
                    }
                    value={item.id}
                  >
                    {({ selected, active }) => (
                      <>
                        <div className="as-flex as-items-center">
                          <img src={item?.image?.src} alt="" className="as-h-6 as-w-6 as-flex-shrink-0 as-rounded-full" />
                          <span className={classNames(selected ? 'as-font-semibold' : 'as-font-normal', 'as-ml-3 as-block as-truncate')}>
                            {item.title}
                          </span>
                        </div>
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
  );
};
export default SelectComponent;
