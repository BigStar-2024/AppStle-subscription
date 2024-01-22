/* This example requires Tailwind CSS v2.0+ */
import React, { Fragment, useRef, useState } from 'react'
import { Dialog, Transition } from '@headlessui/react'
import Loader from "./Loader"
import { connect, useSelector } from 'react-redux';
import { XMarkIcon } from '@heroicons/react/24/outline'

export default function TailwindModal({open, setOpen, children, actionButtonText, actionMethod, updatingFlag, secondaryActionMethod, secondaryActionButtonText, modalTitle, fullHeightModal, actionButtonInValid, addBodyBackground, secondaryActionUpdatingFlag}) {
  // const [open, setOpen] = useState(true)

  const cancelButtonRef = useRef(null)
  const subscriptionContractFreezeStatus = useSelector(state => state.subscription.subscriptionContractFreezeStatus,);
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);

  return (
    <Transition.Root show={open} as={Fragment}>
      <Dialog as="div" className="as-fixed as-z-[9999999999] as-inset-0 as-overflow-y-auto" initialFocus={cancelButtonRef} onClose={setOpen}>
        <div className="as-flex as-items-end as-justify-center as-min-h-screen as-pt-4 as-px-4 as-pb-20 as-text-center sm:as-block sm:as-p-0">
          <Transition.Child
            as={Fragment}
            enter="as-ease-out as-duration-300"
            enterFrom="as-opacity-0"
            enterTo="as-opacity-100"
            leave="as-ease-in as-duration-200"
            leaveFrom="as-opacity-100"
            leaveTo="as-opacity-0"
          >
            <Dialog.Overlay className="as-fixed as-inset-0 as-bg-gray-500 as-bg-opacity-75 as-transition-opacity as-modal-overlay" />
          </Transition.Child>

          {/* This element is to trick the browser into centering the modal contents. */}
          <span className="as-hidden sm:as-inline-block sm:as-align-middle sm:as-h-screen" aria-hidden="true">
            &#8203;
          </span>
          <Transition.Child
            as={Fragment}
            enter="as-ease-out as-duration-300"
            enterFrom="as-opacity-0 as-translate-y-4 sm:as-translate-y-0 sm:as-scale-95"
            enterTo="as-opacity-100 as-translate-y-0 sm:as-scale-100"
            leave="as-ease-in as-duration-200"
            leaveFrom="as-opacity-100 as-translate-y-0 sm:as-scale-100"
            leaveTo="as-opacity-0 as-translate-y-4 sm:as-translate-y-0 sm:as-scale-95"
          >
            <div className={`as-relative as-inline-block as-align-bottom as-bg-white as-rounded-lg as-text-left as-overflow-hidden as-shadow-xl as-transform as-transition-all sm:as-my-8 sm:as-align-middle sm:as-max-w-4xl ${fullHeightModal ? `as-h-[calc(100vh-3.5rem)]` : `sm:as-max-h-2xl`} sm:as-w-full`}>
              <div className={`as-bg-white ${fullHeightModal ? 'as-overflow-hidden as-flex as-flex-col as-max-h-full as-h-full' : ''}`}>
                <div className="as-px-4 as-pt-5 as-pb-4 sm:as-p-6 sm:as-pb-4 as-flex as-justify-between as-items-center as-border-b ">
                <Dialog.Title as="h3" className={`as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-modal-title as-modal-header ${fullHeightModal ? 'as-flex-shrink-0' : ''}`}>
                    {modalTitle}
                  
                </Dialog.Title>
                <div className="as-ml-3 as-flex h-7 as-items-center">
                          <button
                            type="button"
                            className="-as-m-2 as-p-2 as-text-gray-400 hover:as-text-gray-500"
                            onClick={() => setOpen(false)}
                          >
                            <span className="as-sr-only">Close panel</span>
                            <XIcon className="as-h-6 as-w-6" aria-hidden="true" />
                          </button>
                        </div>
                </div>
                
               
                <div className={`as-px-4 as-pt-5 as-pb-4 sm:as-p-6 sm:as-pb-4 as-modal-body ${fullHeightModal ? 'as-overflow-y-scroll' : ''} ${addBodyBackground ? 'as-bg-gray-100' : ''}`}>
                  {children}
                </div>
               
              </div>      
            </div>
          </Transition.Child>
        </div>
      </Dialog>
    </Transition.Root>
  )
}
