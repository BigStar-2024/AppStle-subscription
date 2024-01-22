import React, {useState} from 'react';
import {ListGroup, ListGroupItem} from 'reactstrap';
import moment from "moment";
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import {
    subscriptionBillingAttemptsFlag
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import {connect} from 'react-redux';
import {aryIannaTimeZones, convertToShopTimeZoneDate} from "./SuportedShopifyTImeZone";
import {toast} from 'react-toastify';

var momentTZ = require('moment-timezone');

const PastOrder = ({subscriptionBillingAttemptsFlag, updatingRetryingNeeded, shopInfo, ...props}) => {

    const [retryingNeeded, setRetryingNeeded] = useState(props?.ordData?.retryingNeeded);

    const options = {
        autoClose: 500,
        position: toast.POSITION.BOTTOM_CENTER
    };

    async function attemptBilling(id) {
        props?.retryBillingAttempt(id)
            .then(res => {
                toast?.success("Attempt Billing triggred.", options);
            }).catch(err => {
            toast?.error("Attempt Billing trigger failed.", options);
        })
    }

    return (
        <>
            <ListGroup flush>
                <ListGroupItem key={props.ordData.id}>
                    <div className="widget-content p-0">
                        <div className="widget-content-wrapper">
                            <div className="widget-content-left mr-3">
                                <div className="widget-content-left">
                                    {
                                        (props.ordData && props.ordData.variantList && props.ordData.variantList.length > 0) ? (props.ordData.variantList.map((prd) => {
                                            return (
                                                prd.productId != null ?
                                                    <div className="d-flex align-items-center">
                                                        <img src={prd?.image}
                                                             width={50}
                                                             style={{borderRadius: "29%", padding: "10px"}}/>
                                                        <a
                                                            href={`https://${props.shopName}/admin/products/${prd.productId?.split('/')[4]}`}
                                                            target="_blank"><span> {prd?.title} {(prd?.variantTitle && prd?.variantTitle !== "-") && "-" + prd?.variantTitle}</span></a>
                                                    </div>
                                                    :
                                                    <div className="text-center p-1"
                                                         style={{background: 'beige'}}>
                                                        <b>  {prd?.title} {(prd?.variantTitle && prd?.variantTitle !== "-") && "-" + prd?.variantTitle}</b> has
                                                        been removed
                                                    </div>
                                            )
                                        })) : (props.subscriptionEntities?.lines?.edges?.map((prd) => {
                                            return (
                                                prd?.node && prd?.node != null && prd?.node?.productId != null ?
                                                    <div className="d-flex align-items-center">
                                                        <img src={prd?.node?.variantImage?.transformedSrc}
                                                             width={50}
                                                             style={{borderRadius: "29%", padding: "10px"}}/>
                                                        <a
                                                            href={`https://${props.shopName}/admin/products/${prd?.node?.productId?.split('/')[4]}`}
                                                            target="_blank"><span> {prd?.node?.title} {(prd?.node?.variantTitle && prd?.node?.variantTitle !== "-") && "-" + prd?.node?.variantTitle}</span></a>
                                                    </div>
                                                    :
                                                    <div className="text-center p-1"
                                                         style={{background: 'beige'}}>
                                                        <b>  {prd?.node?.title} {(prd?.node?.variantTitle && prd?.node?.variantTitle !== "-") && "-" + prd?.node?.variantTitle}</b> has
                                                        been removed
                                                    </div>
                                            )
                                        }))
                                    }
                                </div>
                            </div>
                            <div className="widget-content-left flex2 ml-5">
                                <div>
                                    <span className="widget-heading">Order No. : </span>
                                    <span className="widget-subheading opacity-10 pr-2">
                    <a href={`https://${props.ordData?.shop}/admin/orders/${props.ordData?.orderId}`}
                       target="_blank">{props.ordData?.orderName}</a>
                  </span>
                                </div>
                                <div>
                                    <span className="widget-heading">Order Date : </span>
                                    <span className="widget-subheading opacity-10 pr-2">
                                  {convertToShopTimeZoneDate(props.ordData?.billingDate, shopInfo.ianaTimeZone)}
                                </span>
                                </div>
                                <div>
                                    <span
                                        className="widget-heading">Status : </span> {props.ordData.status == "SUCCESS" ?
                                    <div className="badge badge-success ml-2">{props.ordData.status}</div>
                                    : props.ordData.status == "FAILURE" ?
                                        <div className="badge badge-danger ml-2">{props.ordData.status}</div>
                                        : <div className="badge badge-secondary ml-2">{props.ordData.status}</div>}
                                </div>
                                {props.ordData.status == "FAILURE" ?
                                    <div>
                                        <span className="widget-heading">Error Message : </span>
                                        <span
                                            className="widget-subheading opacity-10 pr-2">{props.ordData?.billingAttemptResponseMessage === null ? '' : JSON.parse(props.ordData?.billingAttemptResponseMessage)?.error_message}</span>
                                    </div>
                                    : ""}
                                <div>
                                    <span className="widget-heading">Last Attempt Date : </span>
                                    <span className="widget-subheading opacity-10 pr-2">
                                          {
                                              props.ordData?.attemptTime == null ?
                                                  convertToShopTimeZoneDate(props.ordData?.billingDate, shopInfo.ianaTimeZone) :
                                                  convertToShopTimeZoneDate(props.ordData?.attemptTime, shopInfo.ianaTimeZone)
                                          }
                                        </span>
                                </div>

                                <div>
                                    <span className="widget-heading">Total Attempts : </span>
                                    <span className="widget-subheading opacity-10 pr-2">
                                       {
                                           props.ordData?.attemptCount === null ? '0' : props.ordData?.attemptCount.toString()
                                       }
                                     </span>
                                </div>

                                {props.ordData?.transactionFailedEmailSentStatus != null ? (
                                    <div>
                                        <span className="widget-heading">Transaction failed email sent status : </span>
                                        <span
                                            className="badge badge-info ml-2">{props.ordData?.transactionFailedEmailSentStatus}</span>
                                    </div>
                                ) : ""}


                                {props.ordData.status == "FAILURE" ?
                                    <div>
                                        <span className="widget-heading">Attempt Needed : </span>
                                        <span
                                            className="widget-subheading opacity-10 pr-2">{props.ordData?.retryingNeeded === true ? 'Yes' : 'No'}</span>
                                    </div>
                                    : ""}
                            </div>
                            {props.ordData.status == 'FAILURE' && retryingNeeded && (
                                <>
                                    <MySaveButton
                                        onClick={() => {
                                            setRetryingNeeded(false)
                                            subscriptionBillingAttemptsFlag(false, props.ordData.id);
                                        }}
                                        updating={updatingRetryingNeeded}
                                        text="Disable Retry"
                                        className="btn-small btn-danger mr-2"
                                    >
                                        Disable Retry
                                    </MySaveButton>
                                </>
                            )}
                            {props.ordData.status == 'FAILURE' && !retryingNeeded && (
                                <>
                                    <MySaveButton
                                        onClick={() => {
                                            setRetryingNeeded(true)
                                            subscriptionBillingAttemptsFlag(true, props.ordData.id);
                                        }}
                                        text="Enable Retry"
                                        updatingText={'Processing..'}
                                        updating={updatingRetryingNeeded}
                                        className="btn-small btn-success mr-2"
                                    >
                                        Enable Retry
                                    </MySaveButton>
                                </>
                            )}
                            {
                                (retryingNeeded === true && (props.ordData?.status === "SKIPPED_INVENTORY_MGMT")) ?
                                    <MySaveButton
                                        onClick={() => attemptBilling(props.ordData.id)}
                                        text="Attempt Billing"
                                        addBuffer={false}
                                        updating={props.updatingBillingAttempt}
                                        updatingText={"Processing.."}
                                        className="btn-small btn-primary"
                                    >
                                        Attempt Billing
                                    </MySaveButton>
                                    : ""
                            }

                            {
                                ((props.ordData?.status === "FAILURE" || props.ordData?.status === "SHOPIFY_EXCEPTION")) ?
                                    <MySaveButton
                                        onClick={() => attemptBilling(props.ordData.id)}
                                        text="Attempt Billing"
                                        addBuffer={false}
                                        updating={props.updatingBillingAttempt}
                                        updatingText={"Processing.."}
                                        className="btn-small btn-primary"
                                    >
                                        Attempt Billing
                                    </MySaveButton>
                                    : ""
                            }

                            {
                                ((props.ordData?.status === "SKIPPED_DUNNING_MGMT")) ?
                                    <MySaveButton
                                        onClick={() => attemptBilling(props.ordData.id)}
                                        text="Attempt Billing"
                                        addBuffer={false}
                                        updating={props.updatingBillingAttempt}
                                        updatingText={"Processing.."}
                                        className="btn-small btn-primary"
                                    >
                                        Attempt Billing
                                    </MySaveButton>
                                    : ""
                            }
                        </div>
                    </div>
                </ListGroupItem>
            </ListGroup>
        </>
    )
}

const mapStateToProps = state => ({
    updatingRetryingNeeded: state.subscriptionBillingAttempt.updatingRetryingNeeded,
    shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
    subscriptionBillingAttemptsFlag
};

export default connect(mapStateToProps, mapDispatchToProps)(PastOrder);

