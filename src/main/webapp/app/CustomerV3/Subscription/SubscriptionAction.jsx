import React, { useState } from 'react';
import moment from 'moment';
import TailwindModal from "./TailwindModal";
import PopupMessaging from "./PopupMessaging";

export default function SubscriptionAction(props) {
    const {
        customerPortalSettingEntity,
        subscriptionEntities,
        updateContractStatus,
        checkIfPreventCancellationBeforeDays,
        subscriptionContractFreezeStatus,
        updatingContractStatus
    } = props

    const [isPauseSubscriptionModalOpen, setIsPauseSubscriptionModalOpen] = useState(false);
    const [isResumeSubscriptionModalOpen, setIsResumeSubscriptionModalOpen] = useState(false);

    const [showMessaging, setShowMessaging] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);

    const subscriptionAction = async (status) => {
        let results = await updateContractStatus(subscriptionEntities?.id?.split('/').pop(), status);
        if (results) {
            setShowMessaging(true);
            if (results?.status === 200) {
                setSuccess(true);
                setError(false);
            } else {
                setSuccess(false);
                setError(true);
            }
        }
    }


    const resetModal = () => {
        setIsPauseSubscriptionModalOpen(false);
        setIsResumeSubscriptionModalOpen(false);
        setShowMessaging(false);
        setSuccess(false);
        setError(false);
    }

    return (
    <>
        {customerPortalSettingEntity?.pauseResumeSub && (subscriptionEntities.status !== "CANCELLED") && <>
            {subscriptionEntities.status == "ACTIVE" &&
            <button
                onClick={() => setIsPauseSubscriptionModalOpen(true)}

                type="button"
                class="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-gray-900 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-gray-900 as-bg-transparent hover:as-bg-gray-900 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-gray-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-gray-900 disabled:as-text-white as-button as-button--pause">
                    {customerPortalSettingEntity?.pauseSubscriptionText}
            </button>}
            {subscriptionEntities.status == "PAUSED" && <button
                onClick={() => setIsResumeSubscriptionModalOpen(true)}
                type="button"
                class="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-green-900 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-green-900 as-bg-transparent hover:as-bg-green-900 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-green-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-green-900 disabled:as-text-white as-button as-button--resume">
                    {customerPortalSettingEntity?.resumeSubscriptionText}
            </button>}
        </>}
    <TailwindModal open={isPauseSubscriptionModalOpen} setOpen={resetModal} actionMethod={() => subscriptionAction("PAUSED")} actionButtonText={!showMessaging ? customerPortalSettingEntity?.confirmCommonText || "Confirm" : ""} updatingFlag={updatingContractStatus} modalTitle={customerPortalSettingEntity?.pauseSubscriptionText} className="as-model-pause-subscription" success={success}>
        {showMessaging && <PopupMessaging
            showSuccess={success}
            showError={error}
        />}
        {!showMessaging && (
            <>
                {(!subscriptionContractFreezeStatus && !checkIfPreventCancellationBeforeDays(
                    customerPortalSettingEntity?.preventCancellationBeforeDays,
                    subscriptionEntities?.nextBillingDate
                    )) && <div className="as-text-sm as-text-gray-500">
                    <p className="as-text-gray-500 as-text-sm">{customerPortalSettingEntity?.areyousureCommonMessageText || "Are you sure?"}</p>
                    </div>}
                {subscriptionContractFreezeStatus ? (
                    <p className="as-text-gray-500 as-text-sm">
                    {customerPortalSettingEntity?.subscriptionContractFreezeMessageV2 ||
                        'Your subscription contract is frozen by your shop owner.'}
                    </p>
                ) : (
                    checkIfPreventCancellationBeforeDays(
                    customerPortalSettingEntity?.preventCancellationBeforeDays,
                    subscriptionEntities?.nextBillingDate
                    ) && (
                    <p className="as-text-gray-500 as-text-sm">
                        {customerPortalSettingEntity?.preventCancellationBeforeDaysMessage?.replaceAll(
                        '{{preventDays}}',
                        `${customerPortalSettingEntity?.preventCancellationBeforeDays}`
                        ) ||
                        `You can not pause/cancel/skip the subscription before the ${customerPortalSettingEntity?.preventCancellationBeforeDays} days from your next order date.`}
                    </p>
                    )
                )}
            </>
        ) }

    </TailwindModal>
    <TailwindModal open={isResumeSubscriptionModalOpen} setOpen={resetModal} actionMethod={() => subscriptionAction("ACTIVE")} actionButtonText={!showMessaging ? customerPortalSettingEntity?.confirmCommonText || "Confirm" : ""} updatingFlag={updatingContractStatus} modalTitle={customerPortalSettingEntity?.resumeSubscriptionText} className="as-model-resume-subscription" success={success}>
        {showMessaging && <PopupMessaging
            showSuccess={success}
            showError={error}
        />}
        {!showMessaging && <div className="as-text-sm as-text-gray-500">
            <p className="as-text-gray-500 as-text-sm">{customerPortalSettingEntity?.areyousureCommonMessageText || "Are you sure?"}</p>
        </div> }
    </TailwindModal>
    </>
  )
}
