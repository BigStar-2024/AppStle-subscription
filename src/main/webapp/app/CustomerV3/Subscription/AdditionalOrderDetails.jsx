import React from 'react'
import {connect, useSelector} from 'react-redux';
import "react-datepicker/dist/react-datepicker.css";
import {getCustomerPortalEntity} from 'app/entities/subscriptions/subscription.reducer';
import {DateTime} from "luxon";


function AdditionalOrderDetails(props) {
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity)
  const subscriptionEntities = useSelector(state => state.subscription.entity)

  let frequencyIntervalTranslate = {
    "week": customerPortalSettingEntity?.weekText,
    "day": customerPortalSettingEntity?.dayText,
    "month": customerPortalSettingEntity?.monthText,
    "year": customerPortalSettingEntity?.yearText,
    "weeks": customerPortalSettingEntity?.weeksTextV2,
    "days": customerPortalSettingEntity?.daysTextV2,
    "months": customerPortalSettingEntity?.monthsTextV2,
    "years": customerPortalSettingEntity?.yearsTextV2,
  };
  const getFrequencyTitle = (interval, intervalCount) => {
    let intervalText = parseInt(intervalCount) > 1 ? `${interval}s` : interval;
    return frequencyIntervalTranslate[intervalText?.toLowerCase(intervalText)] ? frequencyIntervalTranslate[intervalText?.toLowerCase()] : intervalText;
  }
  
  return (
    <div
      className='as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-additional-order-details'>
      <div className="as-flex as-justify-between as-mb-2">
        <p
          className="as-text-sm as-text-gray-500 as-card_title as-additional-order-details_title">{customerPortalSettingEntity.additionalOrderDetailsTextV2 || "Additional Order Details"}</p>
      </div>
      <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-min-cycles">
        <span className="as-label"
          dangerouslySetInnerHTML={{__html: customerPortalSettingEntity.orderCreatedDateTextV2 || "Order Created Date <span className='colon-symbol'>:</span>"}}/>&nbsp;
        <span className="as-value">{subscriptionEntities?.createdAt ? DateTime.fromISO(subscriptionEntities?.createdAt).toFormat(customerPortalSettingEntity?.dateFormat) : '-'}</span>
      </p>
      <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-min-cycles">
        <span className="as-label"
          dangerouslySetInnerHTML={{__html: customerPortalSettingEntity.minimumNumberOfOrderHeadingTextV2 || "Minimum Number of Orders <span className='colon-symbol'>:</span>"}}/>&nbsp;
        <span className="as-value">{subscriptionEntities?.billingPolicy?.minCycles ? subscriptionEntities?.billingPolicy?.minCycles : '-'}</span>
      </p>
      <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-max-cycles">
        <span className="as-label"
          dangerouslySetInnerHTML={{__html: customerPortalSettingEntity.maximumNumberOfOrderHeadingTextV2 || "Maximum Number of Orders <span className='colon-symbol'>:</span>"}}/>&nbsp;
        <span className="as-value">{subscriptionEntities?.billingPolicy?.maxCycles ? subscriptionEntities?.billingPolicy?.maxCycles : '-'}</span>
      </p>
    </div>
  )
}


const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AdditionalOrderDetails);

