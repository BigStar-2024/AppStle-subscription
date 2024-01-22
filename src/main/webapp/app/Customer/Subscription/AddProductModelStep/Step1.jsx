import React, { useState, useEffect } from 'react';
import { FormGroup } from 'reactstrap';

function Step1(props) {
    let {setPurchaseOption, purchaseOption, customerPortalSettingEntity, upcomingOrderId} = props;
       return  <div style={{ fontSize: "15px", padding: "7px" }} className="form-wizard-content">
       <div className="plans">
         <div className="title">{customerPortalSettingEntity.choosePurchaseOptionLabelTextV2 || "Choose a Purchase Option"}</div>
         <div className="plansWrapper">
            {customerPortalSettingEntity?.addAdditionalProduct && <label className="plan basic-plan mr-3" for="isSubscriptionPurchase">
                <input
                type="radio"
                id="isSubscriptionPurchase"
                name="purchaseOption"
                value="SUBSCRIBE"
                checked={purchaseOption == "SUBSCRIBE"}
                onClick={(event) => setPurchaseOption(event.target.value)}
                 />
            <div className="plan-content">
            <img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/discount-2045_57GSxj6bEsg.png?updatedAt=1632475086786" alt="" />

                <div className="plan-details">
                <span>{customerPortalSettingEntity?.addToSubscriptionTitleCPV2 || "Add to Subscription"}</span>
                <p>{customerPortalSettingEntity?.applySubscriptionDiscount && customerPortalSettingEntity?.upSellMessage}</p>
                </div>
            </div>
            </label>}
            {customerPortalSettingEntity?.addOneTimeProduct && upcomingOrderId && <label className="plan complete-plan" for="isOneTimePurchase">
                <input
                    type="radio"
                    id="isOneTimePurchase"
                    name="purchaseOption"
                    label="One time purchase"
                    value="ONE_TIME"
                    checked={purchaseOption == "ONE_TIME"}
                    onClick={(event) => setPurchaseOption(event.target.value)}
                />
                <div className="plan-content">
                <img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/shipping-2050_7nIMMvG_4hy.png?updatedAt=1632475174135" alt="" />
                    <div className="plan-details">
                        <span>{customerPortalSettingEntity?.oneTimePurchaseTitleCPV2 || "One Time Purchase"}</span>
                        <p>{customerPortalSettingEntity?.oneTimePurchaseMessageTextV2 || "Get your product along with your next subscription order."}</p>
                    </div>
                </div>
            </label>}
         </div>
       </div>
     </div>
}

export default Step1;
