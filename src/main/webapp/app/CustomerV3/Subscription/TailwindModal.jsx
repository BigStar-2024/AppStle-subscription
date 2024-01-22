/* This example requires Tailwind CSS v2.0+ */
import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { LockClosedIcon } from '@heroicons/react/24/outline';
import Loader from './Loader';
import { connect, useSelector } from 'react-redux';

export default function TailwindModal({
  open,
  setOpen,
  children,
  actionButtonText,
  actionMethod,
  updatingFlag,
  secondaryActionMethod,
  secondaryActionButtonText,
  modalTitle,
  fullHeightModal,
  actionButtonInValid,
  addBodyBackground,
  secondaryActionUpdatingFlag,
  ignoreSubscriptionContractFreezeStatus,
  className,
  success,
  cancellationType,
  pauseSubscription,
  pauseSubscriptionBtn,
  cancelSubscriptionBtn,
  afterClose
}) {
  // const [open, setOpen] = useState(true)

  const cancelButtonRef = useRef(null);
  const subscriptionContractFreezeStatus = useSelector(state => state.subscription.subscriptionContractFreezeStatus);
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);

  useEffect(() => {
    if (open) {
      window?.parent?.postMessage('appstle_scroll_iframe_top');
    }
  }, [open])

  return (
    <Transition.Root show={open} as={Fragment} afterLeave={afterClose}>
      <Dialog
        as="div"
        className={` ${className ? className : ''} as-fixed as-inset-0 as-overflow-y-auto`}
        initialFocus={cancelButtonRef}
        onClose={setOpen}
      >
        <div className="as-flex as-items-center as-justify-center as-min-h-screen as-pt-4 as-px-4 as-pb-20 as-text-center sm:as-block sm:as-p-0 as-modal-root">
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
            <div
              className={`as-relative as-inline-block as-align-bottom as-bg-white as-rounded-lg as-text-left as-overflow-hidden as-shadow-xl as-transform as-transition-all sm:as-my-8 sm:as-align-middle sm:as-max-w-xl ${
                fullHeightModal ? `as-h-[calc(100vh-3.5rem)]` : `sm:as-max-h-2xl`
              } sm:as-w-full`}
            >
              <div className={`as-bg-white ${fullHeightModal ? 'as-overflow-hidden as-flex as-flex-col as-max-h-full as-h-full' : ''}`}>
                <Dialog.Title
                  as="div"
                  className={`as-px-4 as-pt-5 as-pb-4 sm:as-p-6 sm:as-pb-4 as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-border-b as-modal-title as-modal-header ${
                    fullHeightModal ? 'as-flex-shrink-0' : ''
                  }`}
                >
                  <div dangerouslySetInnerHTML={{__html: modalTitle}} />
                </Dialog.Title>
                <div
                  className={`as-px-4 as-pt-5 as-pb-4 sm:as-p-6 sm:as-pb-4 as-modal-body ${fullHeightModal ? 'as-overflow-y-scroll' : ''} ${
                    addBodyBackground ? 'as-bg-gray-100' : ''
                  }`}
                >
                  {children}
                </div>
                <div
                      className={`${
                        fullHeightModal ? 'as-mt-auto as-flex-shrink-0' : ''
                      } as-border-t as-px-4 as-py-3 sm:as-px-6 as-grid as-gap-2 as-modal-footer as-modal-cta-wrapper`}
                    >
                {cancellationType === 'CANCEL_AFTER_PAUSE' ? (
                  <>
                  { !success ? <>  
                  <button
                      type="button"
                      className={`as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-green-900 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-green-900 as-bg-transparent hover:as-bg-green-900 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-green-900  disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-green-900 disabled:as-text-white as-button as-button--cancelsub`}
                      onClick={event => pauseSubscription(event)}
                      ref={cancelButtonRef}
                      id={success ? 'success-closeButton' : 'closeButton'}
                    >
                      {!pauseSubscriptionBtn ? `${customerPortalSettingEntity?.acceptButtonTextV2 || 'Accept'}` : <Loader />}
                    </button>
                    <button
                      type="button"
                      className={`as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-red-600 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-red-600 as-bg-transparent hover:as-bg-red-600 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-red-500  disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-red-600 disabled:as-text-white as-button as-button--cancelsub`}
                      onClick={event => actionMethod(event)}
                      ref={cancelButtonRef}
                      id={success ? 'success-closeButton' : 'closeButton'}
                    >
                      {!cancelSubscriptionBtn ?  `${customerPortalSettingEntity?.noCancelTheSubscriptionButtonTextV2 || 'No, cancel the subscription'}` : <Loader />}
                    </button></> : ''}
                  
                    <button
                      type="button"
                      className={`as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-indigo-600  hover:as-bg-indigo-600 hover:as-text-white as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-indigo-600 as-bg-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--secondary as-button_modal-close`}
                      onClick={() => setOpen(false)}
                      ref={cancelButtonRef}
                      id={success ? 'success-closeButton' : 'closeButton'}
                    >
                      {customerPortalSettingEntity?.closeBtnTextV2 || 'Close'}
                    </button>
                  </>
                ) : (
                  <>
                   
                      {actionButtonText && (
                        <button
                          onClick={event => actionMethod(event)}
                          type="submit"
                          disabled={updatingFlag ||
                            actionButtonInValid ||
                            (ignoreSubscriptionContractFreezeStatus ? false : subscriptionContractFreezeStatus) ||
                            secondaryActionUpdatingFlag
                          }
                          className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                        >
                          {(ignoreSubscriptionContractFreezeStatus ? false : subscriptionContractFreezeStatus) && (
                            <LockClosedIcon class="as-h-4 as-w-4 as-text-white as-mr-2" />
                          )}{' '}
                          {!updatingFlag ? actionButtonText : <Loader />}
                        </button>
                      )}
                      {secondaryActionButtonText && (
                        <button
                          type="button"
                          disabled={updatingFlag}
                          className="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-indigo-600  hover:as-bg-indigo-600 hover:as-text-white as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-indigo-600 as-bg-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--secondary as-button_modal-secondary"
                          onClick={event => secondaryActionMethod(event)}
                        >
                          {!secondaryActionUpdatingFlag ? secondaryActionButtonText : <Loader />}
                        </button>
                      )}
                      {!secondaryActionButtonText ? (
                        <button
                          type="button"
                          className={`as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-indigo-600  hover:as-bg-indigo-600 hover:as-text-white as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-indigo-600 as-bg-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--secondary as-button_modal-close`}
                          onClick={() => setOpen(false)}
                          ref={cancelButtonRef}
                          id={success ? 'success-closeButton' : 'closeButton'}
                        >
                          {customerPortalSettingEntity?.closeBtnTextV2 || 'Close'}
                        </button>
                      ) : (
                        <>
                          <p
                            onClick={() => setOpen(false)}
                            ref={cancelButtonRef}
                            class="as-text-center as-cursor-pointer as-text-indigo-600 as-underline as-text-sm as-cta as-cta_modal-close"
                          >
                            {customerPortalSettingEntity?.closeBtnTextV2 || 'Close'}
                          </p>
                        </>
                      )}
                  </>
                )}
                </div>
              </div>
            </div>
          </Transition.Child>
        </div>
      </Dialog>
    </Transition.Root>
  );
}
