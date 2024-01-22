import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import Loader from 'react-loaders';
import moment from 'moment';
import {Card, CardBody, Col, Input, Row, Table,} from 'reactstrap';
import Pagination from "react-js-pagination";
import {JhiItemCount} from "react-jhipster";
import {getFilteredEntities} from "app/entities/activity-log/activity-log.reducer";
import FilterAction from "./FilterAction";
import BlockUi from '@availity/block-ui';
import {ActivityLogEventSource, ActivityLogEventType} from './activityEnum';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {convertToShopTimeZoneDate} from "../Shared/SuportedShopifyTImeZone";
import CustomHtmlToolTip from "../SubscriptionGroups/CustomHtmlToolTip";
import {Help} from "@mui/icons-material";
import ResendEmailItem from "../Shared/ResendEmailItem";
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

var momentTZ = require('moment-timezone');

const SubscriptionActivityList = ({
                                      getFilteredEntities,
                                      activityLogEntities,
                                      loading,
                                      totalItems,
                                      account,
                                      shopInfo,
                                      ...props
                                  }) => {

    const {subscriptionContractID} = props
    const [searchValue, setSearchValue] = useState('');
    const [sortByType, setSortByType] = useState('name');
    const [activePage, setActivePage] = useState(1);
    const [totalRowData, setTotalRowData] = useState(0);
    const [hasData, setHasData] = useState(false);
    const [entityTypeOption, setEntityTypeOption] = useState([]);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [sortByDir, setSortByDir] = useState('asc');
    let initFilter = FilterAction.getFilterObject();
    const [filterVal, setFilterVal] = useState(initFilter);
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const toggle = () => setDropdownOpen(prevState => !prevState);
    const [eventType, setEventType] = useState(null);
    const [activityLogEntitiesData, setActivityLogEntitiesData] = useState([]);
    const [hasEmailResendButton, setHasEmailResendButton] = useState([])

    useEffect(() => {
        setActivityLogEntitiesData(activityLogEntities);
    }, [activityLogEntities]);

    // useEffect(() => {
    //   if(activityLogEntities?.length)
    //   {
    //     let arrayData = [];
    //     activityLogEntities.map(data => {
    //       if(!arrayData.some(ele => ele == data?.entityType))
    //       {
    //         arrayData.push(data.entityType)
    //       }
    //     })
    //     console.log(arrayData);
    //     setEntityTypeOption(arrayData);
    //   }
    // }, [activityLogEntities])

    const handleGridData = (page) => {
        let customQueryParam = `page=${page - 1}&size=${itemsPerPage}&sort=createAt,desc`

        if (subscriptionContractID) {
            if (eventType) {
                customQueryParam = customQueryParam + `&entityId.equals=${subscriptionContractID}&eventType.equals=${eventType}`
            } else {
                customQueryParam = customQueryParam + `&entityId.equals=${subscriptionContractID}`
            }
        }
        getFilteredEntities(customQueryParam)
    }

    useEffect(() => {
        handleGridData(activePage);
    }, [activePage, sortByType, sortByDir, filterVal])

    useEffect(() => {
        setTotalRowData(totalItems);
        if (totalItems > 0) {
            setHasData(true);
        }
    }, [activityLogEntities]);

    useEffect(() => {
        handleGridData(1);

    }, [subscriptionContractID, eventType])

    const getActivityLogDescription = (item) => {

        return (
            getAdditionalInfoFromEvent(item))
        //  + JSON.parse(item?.additionalInfo).(item?.createAt != null ? " to " + moment(item.createAt)?.format("MM/DD/YYYY HH:MM:SS"): ""))
        //  + (item?.createAt != null ? " at " + moment(item.createAt)?.format("MM/DD/YYYY HH:MM:SS"): ""))
    }


    const getAdditionalInfoFromEvent = (item) => {
        let additionDescrription = ""
        switch (item?.eventType) {
            case "NEXT_BILLING_DATE_CHANGE":
                additionDescrription = additionDescrription + `Next billing date changed to <b>${(convertToShopTimeZoneDate(JSON.parse(item?.additionalInfo)?.nextOrderDate.replace('[UTC]', ''), shopInfo.ianaTimeZone))}</b>`
                break;
            case "BILLING_INTERVAL_CHANGE":
                additionDescrription = additionDescrription + `Billing interval changed to <b>${JSON.parse(item?.additionalInfo)?.billingIntervalCount} ${JSON.parse(item?.additionalInfo)?.billingInterval}</b>`
                break;
            case "PRODUCT_QUANTITY_CHANGE":
                if (item?.additionalInfo) {
                    // additionDescrription = additionDescrription + `Quantity changed from <b>${JSON.parse(item?.additionalInfo)?.oldQuantity}</b> to <b>${JSON.parse(item?.additionalInfo)?.newQuantity}</b>`
                    let quantityVariantId = JSON.parse(item?.additionalInfo)?.variantId
                    let quantityChangeName = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === quantityVariantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `<p class="m-0">${quantityChangeName ? `<b>Name: </b>${quantityChangeName}<br/>` : ''}
            <b>Variant Id:</b> ${quantityVariantId?.split('/')?.pop()}<br/>
            <b>Old Quantity:</b> ${JSON.parse(item?.additionalInfo)?.oldQuantity}<br/>
            <b>New Quantity:</b> ${JSON.parse(item?.additionalInfo)?.newQuantity}</p>`
                }
                break;
            case "PRODUCT_ADD":
                if (item?.additionalInfo) {
                    let variantId = JSON.parse(item?.additionalInfo)?.variantId
                    let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.price)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
                }
                break;
            case "PRODUCT_REMOVE":
                if (item?.additionalInfo) {
                    let variantId = JSON.parse(item?.additionalInfo)?.variantId
                    let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
          <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
          <b>Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.price)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/>`
                }
                break;
            case "PRODUCT_PRICE_CHANGE":
                // additionDescrription = additionDescrription + `Price changed from <b>${JSON.parse(item?.additionalInfo)?.oldPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2) : JSON.parse(item?.additionalInfo)?.oldPrice}</b> to <b>${JSON.parse(item?.additionalInfo)?.newPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2) : 0.0}</b>`
                let priceVariantId = JSON.parse(item?.additionalInfo)?.variantId
                let priceChangeName = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === priceVariantId))?.node?.title) || ""
                additionDescrription = additionDescrription + `<p class="m-0">${priceChangeName ? `<b>Name: </b>${priceChangeName}<br/>` : ''}
          <b>Variant Id:</b> ${priceVariantId?.split('/')?.pop()}<br/>
          <b>Old Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/>
          <b>New Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}</p>`
                break;
            case "SYSTEM_UPDATED_DELIVERY_PRICE":

                if(item.status === 'SUCCESS') {
                  additionDescrription = additionDescrription + `Price changed from <b>${JSON.parse(item?.additionalInfo)?.oldShippingPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.oldShippingPrice)?.toFixed(2) : JSON.parse(item?.additionalInfo)?.oldShippingPrice}</b> to <b>${JSON.parse(item?.additionalInfo)?.newShippingPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.newShippingPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.newShippingPrice)}</b>`
                } else {
                  additionDescrription = additionDescrription + `<b>${JSON.parse(item?.additionalInfo)?.reason}</b>`
                }
                break;
            case "MANUAL_DELIVERY_PRICE_UPDATED":
                additionDescrription = additionDescrription + `Price changed from <b>${JSON.parse(item?.additionalInfo)?.oldDeliveryPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.oldDeliveryPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.oldDeliveryPrice)}</b> to <b>${JSON.parse(item?.additionalInfo)?.newDeliveryPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.newDeliveryPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.newDeliveryPrice)}</b>`
                break;
            case "DELIVERY_PRICE_OVERRIDE_CHANGED":
                additionDescrription = additionDescrription + `Delivery price override toggle Turned <b>${JSON.parse(item?.additionalInfo)?.oldValue ? "On" : "Off"}</b> to <b>${JSON.parse(item?.additionalInfo)?.newValue ? "On" : "Off"}</b>`
                break;
            case "BILLING_ATTEMPT_NOTIFICATION":
                additionDescrription = additionDescrription + item?.additionalInfo
                break;
            case "PRODUCT_PRICING_POLICY_CHANGE": {
                let variantId = JSON.parse(item?.additionalInfo)?.variantId
                let newPrice = JSON.parse(item?.additionalInfo)?.newPricingPolicy?.basePrice
                let oldPrice = JSON.parse(item?.additionalInfo)?.oldPricingPolicy?.basePrice
                let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>New Price:</b> ${newPrice ? parseFloat(newPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/>
            <b>Old Price:</b> ${oldPrice ? parseFloat(oldPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/></p>`
            }
                break;
            case "ONE_TIME_PURCHASE_PRODUCT_ADDED":
                if (item?.additionalInfo) {
                    let variantId = JSON.parse(item?.additionalInfo)?.variantId
                    let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `
            <b>Variant Id:</b> ${variantId}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
                }

                break;
            case "ONE_TIME_PURCHASE_PRODUCT_REMOVED":
                if (item?.additionalInfo) {
                    let variantId = JSON.parse(item?.additionalInfo)?.variantId
                    let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `
            <b>Variant Id:</b> ${variantId}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
                }

                break;
            case "PRICE_CHANGE_SYNC":
                if (item?.additionalInfo) {
                    let variantId = JSON.parse(item?.additionalInfo)?.variantId
                    let name = ((props?.subscriptionEntities?.lines?.edges.find((item) => item?.node?.variantId === variantId))?.node?.title) || ""
                    additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>New Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/>
            <b>Old Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}${props?.subscriptionEntities?.deliveryPrice?.currencyCode}<br/></p>`
                }

                break;
            case "PRODUCT_SELLING_PLAN_CHANGE":
                if (item?.additionalInfo) {
                    let addinfo = JSON.parse(item?.additionalInfo)
                    let variantId = addinfo?.variantId
                    let name = addinfo?.title
                    additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
              <b>Variant Id:</b> ${variantId}<br/></p>
              <b>New Selling Plan:</b> ${addinfo?.newSellingPlanName} (${addinfo?.newSellingPlanId})<br/>
              <b>Old Selling Plan:</b> ${addinfo?.oldSellingPlanName} (${addinfo?.oldSellingPlanId})<br/>`
                }

                break;
            default:
                additionDescrription = ActivityLogEventType.find(({type}) => type === item?.eventType)?.description
                break;
        }
        return additionDescrription;
    }

    const handlePagination = activePage => {
        setActivePage(activePage);
    };

    const hasFilter = () => {
        return (_.size(eventType) > 0) || hasData;
    }

    const toTitleCase = function (str) {
        if (str) {
            let name = str?.split("_")?.join(" ")
            return name?.replace(
                /\w\S*/g,
                function (txt) {
                    return txt?.charAt(0)?.toUpperCase() + txt?.substr(1)?.toLowerCase();
                }
            );
        }
    }
    let hasEmailResendButtonLocal = []
    const emailResendButton = function (eventType, index) {
        if (!index) {
            hasEmailResendButtonLocal = []
        }
        ;
        eventType = eventType?.toLowerCase()
        if (eventType?.indexOf("_email") !== -1) {
            if (hasEmailResendButtonLocal?.indexOf(eventType) === -1) {
                hasEmailResendButtonLocal = [...hasEmailResendButtonLocal, eventType]
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    const getEmailEventType = function (eventType) {
        let eventArray = eventType?.split("_");
        eventArray?.shift();
        eventArray?.pop();
        return eventArray?.join("_")
    }

    const formatJSONData = value => {
      try {
        let str = JSON.stringify(JSON.parse(value), undefined, 4);
        if (str.includes('class MessageOut')) {
          str = str.replaceAll('\\n', '\n');
        }
        return str;
      } catch (error) {
        return value;
      }
    }
    return (
        <Fragment>
            {
                (loading && !hasFilter()) ?
                    (<div style={{margin: "10% 0 0 43%"}}
                          className="loader-wrapper d-flex justify-content-center align-items-center">
                        <Loader type="line-scale"/>
                    </div>)
                    :
                    <>


                        {(activityLogEntitiesData && (activityLogEntitiesData?.length > 0 || hasFilter()) ?
                            <Fragment>
                                <Card className="main-card">
                                    <CardBody>
                                        <BlockUi
                                            tag="div"
                                            blocking={loading}
                                            loader={<Loader active type="line-scale"/>}
                                        >
                                            <div style={{width: "38%", marginLeft: 'auto'}}>
                                                <Input
                                                    type="select"
                                                    value={eventType}
                                                    onChange={(e) => setEventType(e.target.value)}>
                                                    <option value="">Select Event Type</option>
                                                    {
                                                        ActivityLogEventType.map(data => <option
                                                            value={data.type}>{data.description}</option>)
                                                    }
                                                </Input>
                                            </div>
                                            <PerfectScrollbar>

                                                <Table className="mb-0 mt-4 text-left">
                                                    <thead>
                                                    <tr>
                                                        <th>Event Type</th>
                                                        {/* <th>Activity Section</th> */}
                                                        <th>Activity</th>
                                                        {/* <th>Updated Info</th> */}
                                                        <th>Source</th>
                                                        <th style={{textAlign: 'center'}}>Status</th>
                                                        <th>Activity On <div
                                                            style={{fontSize: '10px', color: '#767672'}}>(MM/DD/YYYY
                                                            HH:MM)</div></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    {
                                                        (activityLogEntitiesData?.map((item, index) => {
                                                            return (
                                                                <tr key={item?.id}>
                                                                    <td style={{
                                                                        verticalAlign: "top",
                                                                        display: "flex",
                                                                        alignItems: "center"
                                                                    }}>{(toTitleCase(ActivityLogEventType.find(({type}) => type === item?.eventType)?.title) ||
                                                                        <span>{toTitleCase(item?.eventType)}</span>)} {emailResendButton(item?.eventType, index) ?
                                                                        <div style={{minWidth: '246px'}}>
                                                                            <FeatureAccessCheck
                                                                                hasAnyAuthorities={'accessResendEmail'}
                                                                                upgradeButtonText="Upgrade your plan">
                                                                                <ResendEmailItem
                                                                                    type={getEmailEventType(item?.eventType)}
                                                                                    contractId={subscriptionContractID}/>
                                                                            </FeatureAccessCheck></div> : ""}
                                                                            {item?.eventType === "WEBHOOK" ? <span>&nbsp;{(JSON.parse(item?.additionalInfo))?.webhookResponse?.split('\n') .find(data => data.includes('eventType')) ?.split(':')[1] ?.trim() ?? ""}</span>: ''}
                                                                            </td>

                                                                    <td style={{verticalAlign: "top"}}>
                                                                        {item?.eventType === "BILLING_ATTEMPT_NOTIFICATION" ?

                                                                            <p>
                                                                                <b>Billing Attempt
                                                                                    Id:</b> {(JSON.parse((JSON.parse(item?.additionalInfo))?.shopifyResponse))?.id} &nbsp;
                                                                                <CustomHtmlToolTip
                                                                                    interactive
                                                                                    placement="right"
                                                                                    arrow
                                                                                    enterDelay={0}
                                                                                    title={
                                                                                        <div style={{padding: '8px'}}>
                                                                                            <div
                                                                                                style={{textAlign: 'center'}}>
                                                                                                <b>Shopify Response</b>
                                                                                            </div>
                                                                                            <hr style={{borderColor: '#fff'}}/>
                                                                                            <pre className='font-size-md text-white'> {formatJSONData((JSON.parse(item?.additionalInfo))?.shopifyResponse)}</pre>
                                                                                        </div>
                                                                                    }
                                                                                >
                                                                                    <Help style={{fontSize: '1rem'}}/>
                                                                                </CustomHtmlToolTip>
                                                                                <br/>
                                                                                {(JSON.parse((JSON.parse(item?.additionalInfo))?.shopifyResponse))?.error_message ? <>
                                                                                    <b>Error
                                                                                        Message:</b> {(JSON.parse((JSON.parse(item?.additionalInfo))?.shopifyResponse))?.error_message}</> : ''}
                                                                            </p>

                                                                            : <div className='d-flex'>
                                                                                <div>
                                                                                    <div
                                                                                        dangerouslySetInnerHTML={{__html: getActivityLogDescription(item)}}></div>
                                                                                </div>
                                                                                <div className='ml-1'>
                                                                                    {(JSON.parse(item?.additionalInfo)) &&
                                                                                        <CustomHtmlToolTip
                                                                                            interactive
                                                                                            placement="right"
                                                                                            arrow
                                                                                            enterDelay={0}
                                                                                            title={
                                                                                                <div
                                                                                                    style={{padding: '8px'}}>
                                                                                                    <div
                                                                                                        style={{textAlign: 'center'}}>
                                                                                                        <b>Shopify
                                                                                                            Response</b>
                                                                                                    </div>
                                                                                                    <hr style={{borderColor: '#fff'}}/>
                                                                                                    <pre className='font-size-md text-white'> {formatJSONData(item?.additionalInfo)}</pre>
                                                                                                </div>
                                                                                            }
                                                                                        >
                                                                                            <Help
                                                                                                style={{fontSize: '1rem'}}/>
                                                                                        </CustomHtmlToolTip>}

                                                                                </div>
                                                                            </div>
                                                                        }
                                                                    </td>
                                                                    <td style={{verticalAlign: "top"}}>{ActivityLogEventSource.find(({type}) => type == item?.eventSource)?.description}</td>
                                                                    <td style={{
                                                                        verticalAlign: "top",
                                                                        textAlign: "center"
                                                                    }}>
                                                                        {item?.status?.toLowerCase() === "success" ?
                                                                            <div
                                                                                className="badge badge-pill badge-success"> {item?.status}</div>
                                                                            : <div
                                                                                className="badge badge-pill badge-secondary"> {item?.status}</div>
                                                                        }
                                                                    </td>
                                                                    <td style={{verticalAlign: "top"}}>{item?.createAt != null ? (convertToShopTimeZoneDate(item.createAt, shopInfo.ianaTimeZone)) : "-"}
                                                                    </td>
                                                                </tr>
                                                            )
                                                        }))
                                                    }
                                                    </tbody>
                                                </Table>
                                                {
                                                    activityLogEntitiesData?.length === 0 &&
                                                    <div className="text-center m-3">No data available</div>
                                                }
                                                <Row style={{textAlign: 'center'}}>

                                                    <Col md={12}>
                                                        <br/>
                                                        <div style={{display: 'flex', justifyContent: 'center'}}>
                                                            <Pagination
                                                                activePage={activePage}
                                                                itemsCountPerPage={itemsPerPage}
                                                                totalItemsCount={totalRowData}
                                                                // pageRangeDisplayed={5}
                                                                onChange={handlePagination}
                                                            />
                                                        </div>
                                                        <JhiItemCount page={activePage} total={totalRowData}
                                                                      itemsPerPage={itemsPerPage}/>
                                                    </Col>
                                                </Row>

                                            </PerfectScrollbar>
                                        </BlockUi>
                                    </CardBody>
                                </Card>
                            </Fragment>
                            :
                            <Row className="align-items-center welcome-page">
                                <Col sm='5'>
                                    <div>
                                        <h4>Welcome to Activity Log Section</h4>
                                        <p className='text-muted'>
                                            This section give you the complete list of subscription activity and time.
                                            Currently there is no activity log available.
                                        </p>
                                        <p className='text-muted'>If you have any questions at any time, just reach out
                                            to us on <a
                                                href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                                        </p>
                                    </div>
                                </Col>
                            </Row>)
                        }
                    </>
            }
        </Fragment>
    )
}

const mapStateToProps = state => ({
    activityLogEntities: state.activityLog.entities,
    loading: state.activityLog.loading,
    totalItems: state.activityLog.totalItems,
    shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
    getFilteredEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionActivityList);
