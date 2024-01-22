
import React, { useEffect, useState } from 'react';
import { CheckCircleIcon, XCircleIcon } from '@heroicons/react/24/outline'
import { connect, useSelector } from 'react-redux';

export default function PopupMessaging({showSuccess, showError, errorMessage, successMessage}) {
    const customerPortalSettings = useSelector(state => state.customerPortalSettings.entity);
    return (
    <>
        {showSuccess && <div className="as-flex as-items-center">
            <CheckCircleIcon className="as-inline as-h-10 as-w-10 as-ml-1 as-stroke-green-500 as-cursor-pointer" />  
            <div className="as-text-gray-500 as-text-sm as-ml-4" dangerouslySetInnerHTML={{__html: (successMessage || customerPortalSettings?.popUpSuccessMessageV2 || "Success!")}}>
            </div> 
        </div>}
        {showError && <div className="as-flex as-items-center">
            <XCircleIcon  className="as-inline as-h-10 as-w-10 as-ml-1 as-cursor-pointer as-stroke-red-500" />  
            <div className="as-text-gray-500 as-text-sm as-ml-4" dangerouslySetInnerHTML={{__html: (errorMessage || customerPortalSettings?.popUpErrorMessage || "Something went wrong. Please try again.")}}>
            </div> 
        </div>}
    </>

  )
}
