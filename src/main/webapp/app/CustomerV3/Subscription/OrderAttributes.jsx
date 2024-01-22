import React, { Fragment, useEffect, useState } from 'react'
import { connect, useSelector } from 'react-redux';
import "react-datepicker/dist/react-datepicker.css";
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';


function OrderAttributes(props) {
    const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity)
    const subscriptionEntities = useSelector(state => state.subscription.entity)
    return (
        <>
       {subscriptionEntities?.customAttributes?.length ? <div className='as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-order-attribute-wrapper'>
            <div className="as-flex as-justify-between as-mb-2">
            <p className="as-text-sm as-text-gray-500 as-card_title as-order-attribute_title">{customerPortalSettingEntity.orderAttributesTextV2 || "Order Attributes"}</p>
            </div>
            {subscriptionEntities?.customAttributes?.map((item, index) => {
                return (
                    <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-order-attribute">
                    {item?.key}: {item?.value}
                    </p>
                );
            })}
        </div> : ''}
        </>
    )
}


const mapStateToProps = state => ({});

const mapDispatchToProps = {
    getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(OrderAttributes);

