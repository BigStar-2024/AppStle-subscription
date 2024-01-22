import React, {Fragment, useEffect, useState} from 'react';
import {connect} from "react-redux";
import PageTitle from "app/Layout/AppMain/PageTitle";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {
  getFilteredActivityLogEntities,
  getFilteredEntities
} from "app/entities/activity-log/activity-log.reducer";
import {Card, CardBody, Col, Input, Row, Table, Modal, ModalHeader, ModalBody, ModalFooter, Button, Label, FormFeedback} from "reactstrap";
import Loader from "react-loaders";
import PerfectScrollbar from "react-perfect-scrollbar";
import CustomHtmlToolTip from "app/DemoPages/Dashboards/SubscriptionGroups/CustomHtmlToolTip";
import {Help} from "@mui/icons-material";
import {ActivityLogEventSource, ActivityLogEventType} from "app/DemoPages/Dashboards/ActivityLog/activityEnum";
import {aryIannaTimeZones} from "app/DemoPages/Dashboards/Shared/SuportedShopifyTImeZone";
import momentTZ from "moment-timezone";
import moment from "moment";
import Pagination from "react-js-pagination";
import {JhiItemCount} from "react-jhipster";
import FilterAction from "app/DemoPages/Dashboards/ActivityLog/FilterAction";
import ActivityLogFilter from "app/DemoPages/Dashboards/EventData/ActivityLogFilter";
import BlockUi from '@availity/block-ui';
import Chip from "@mui/material/Chip";
import {Link, useLocation, useHistory} from "react-router-dom";
import queryString from "query-string";
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import axios from 'axios';
import SweetAlert from 'sweetalert-react';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import { convertToShopTimeZoneDate } from '../Shared/SuportedShopifyTImeZone';

const EventData = ({activityLogEntities, loading, totalItems, shopInfo, getFilteredEntities, getFilteredActivityLogEntities, exporting}) => {
  const [activityLogEntitiesData, setActivityLogEntitiesData] = useState([]);
  const [activePage, setActivePage] = useState(1);
  const [totalRowData, setTotalRowData] = useState(0);
  const [hasData, setHasData] = useState(false);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortByDir, setSortByDir] = useState('desc');
  let initFilter = FilterAction.getFilterObject();
  const [filterVal, setFilterVal] = useState(initFilter);
  let [isModalOpen, setIsModalOpen] = useState(false);
  let [emailValidity, setEmailValidity] = useState(true);
  let [emailSendingProgress, setEmailSendingProgress] = useState(false);
  let [blurred, setBlurred] = useState(false);
  let [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  let [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  let [emailFailAlert, setEmailFailAlert] = useState(false);
  const [allowExport, setAllowExport] = useState(false);
  const [failMessage, setFailMessage]= useState('');
  const [defaultEmail, setDefaultEmail] = useState('');

  const location = useLocation();
  const history = useHistory();

  const sortTypes = [
    {key: 'desc', title: 'Descending'},
    {key: 'asc', title: 'Ascending'},
  ]

  const handleGridData = (page) => {
    const sortField = `createAt,${sortByDir}`
    getFilteredActivityLogEntities(page - 1, itemsPerPage, sortField, filterVal)
  }

  useEffect(() => {
    if (totalRowData) {
      setAllowExport(true)
    }
  }, [totalRowData])

  useEffect(() => {
    handleGridData(activePage);
  }, [activePage, sortByDir, filterVal])

  useEffect(() => {
    setActivityLogEntitiesData(activityLogEntities);
  }, [activityLogEntities]);

  useEffect(() => {
    setTotalRowData(totalItems);
    if (totalItems > 0) {
      setHasData(true);
    }
  }, [activityLogEntities]);

  useEffect(() => {
    if (!location.state?.from) {
      const filterStatus = queryString.stringify(filterVal);
      history.push(`${location.pathname}?page=${activePage}&size=${itemsPerPage}${filterStatus ? `&${filterStatus}` : ""}`);
    } else {
      const filterStatus = queryString.stringify(filterVal);
      setActivePage(1);
      history.push(`${location.pathname}?page=${activePage}&size=${itemsPerPage}${filterStatus ? `&${filterStatus}` : ""}`);
    }
  }, [activePage, itemsPerPage, filterVal]);

  const onApplyFilter = (filter) => {
    setFilterVal(filter)
    setActivePage(1)
    FilterAction.setFilterObject(filter);
  }

  const hasFilter = () => {
    return hasData;
  }

  const handlePagination = activePage => {
    setActivePage(activePage);
  };

  const checkEmailValidity = (emailId) => {
    if (/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      .test(emailId)) {
      setEmailValidity(true);
      return true
    } else {
      setEmailValidity(false)
      return false;
    }
  }

  const cleanupBeforeModalClose = () => {
    setEmailSendingProgress(false);
    setEmailValidity(true);
    setIsModalOpen(!isModalOpen);
    setBlurred(false);
    setInputValueForTestEmailId('');
  }

  const getActivityLogDescription = (item) => {
    return (
      getAdditionalInfoFromEvent(item))
    //  + JSON.parse(item?.additionalInfo).(item?.createAt != null ? " to " + moment(item.createAt)?.format("MM/DD/YYYY HH:MM:SS"): ""))
    //  + (item?.createAt != null ? " at " + moment(item.createAt)?.format("MM/DD/YYYY HH:MM:SS"): ""))
  }

  const apiUrl = 'api/activity-logs/export/all';

  const sendFilteredSubscriptionMail = (emailId) => {
    if (checkEmailValidity(emailId)) {
      setEmailSendingProgress(true);
      var params = prepareQueryParams(emailId, filterVal)
      axios.get(`${apiUrl}${params ? `?${params}` : ''}`
      ).then(() => {
        cleanupBeforeModalClose();
        setEmailSuccessAlert(true);
      })
        .catch(error => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
          setFailMessage(error.response.data.message ? error.response.data.message : 'Something bad happened. Please try again.');
        })
    }
  }

  const prepareQueryParams = (emailId, filter) => {
    const params = [];
    if (filter) {
      if (filter.entityType) {
        params.push(encodeURIComponent('entityType.in') + '=' + encodeURIComponent(filter.entityType));
      }

      if (filter.eventSource) {
        params.push(encodeURIComponent('eventSource.in') + '=' + encodeURIComponent(filter.eventSource));
      }
      if (filter.eventType) {
        params.push(encodeURIComponent('eventType.in') + '=' + encodeURIComponent(filter.eventType));
      }

      if (filter.status) {
        params.push(encodeURIComponent('status.in') + '=' + encodeURIComponent(filter.status));
      }

      if (filter.entityId) {
        params.push(encodeURIComponent('entityId.in') + '=' + encodeURIComponent(filter.entityId));
      }

      if (filter.fromCreatedDate) {
        params.push(
          encodeURIComponent('createAt.greaterThanOrEqual') + '=' + encodeURIComponent(new Date(filter.fromCreatedDate).toISOString())
        );
      }

      if (filter.toCreatedDate) {
        params.push(encodeURIComponent('createAt.lessThanOrEqual') + '=' + encodeURIComponent(new Date(filter.toCreatedDate).toISOString()));
      }

      params.push(encodeURIComponent('cacheBuster') + '=' + encodeURIComponent(`${new Date().getTime()}`));
      params.push(encodeURIComponent('email') + '=' + encodeURIComponent(emailId))
      return params.join('&');
    }
  };

  const getAdditionalInfoFromEvent = (item) => {
    let additionDescrription = ""
    switch (item?.eventType) {
      case "NEXT_BILLING_DATE_CHANGE":
        additionDescrription = additionDescrription + `Next billing date changed to <b>${convertToShopTimeZoneDate(JSON.parse(item?.additionalInfo)?.nextOrderDate.replace('[UTC]', ''), shopInfo.ianaTimeZone)}</b>`
        break;
      case "BILLING_INTERVAL_CHANGE":
        additionDescrription = additionDescrription + `Billing interval changed to <b>${JSON.parse(item?.additionalInfo)?.billingIntervalCount} ${JSON.parse(item?.additionalInfo)?.billingInterval}</b>`
        break;
      case "PRODUCT_QUANTITY_CHANGE":
        if (item?.additionalInfo) {
          // additionDescrription = additionDescrription + `Quantity changed from <b>${JSON.parse(item?.additionalInfo)?.oldQuantity}</b> to <b>${JSON.parse(item?.additionalInfo)?.newQuantity}</b>`
          let quantityVariantId = JSON.parse(item?.additionalInfo)?.variantId
          let quantityChangeName = ""
          additionDescrription = additionDescrription + `<p class="m-0">${quantityChangeName ? `<b>Name: </b>${quantityChangeName}<br/>` : ''}
            <b>Variant Id:</b> ${quantityVariantId?.split('/')?.pop()}<br/>
            <b>Old Quantity:</b> ${JSON.parse(item?.additionalInfo)?.oldQuantity}<br/>
            <b>New Quantity:</b> ${JSON.parse(item?.additionalInfo)?.newQuantity}</p>`
        }
        break;
      case "PRODUCT_ADD":
        if (item?.additionalInfo) {
          let variantId = JSON.parse(item?.additionalInfo)?.variantId
          let name = ""
          additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.price)?.toFixed(2)}${' '}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
        }
        break;
      case "PRODUCT_REMOVE":
        if (item?.additionalInfo) {
          let variantId = JSON.parse(item?.additionalInfo)?.variantId
          let name = ""
          additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
          <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
          <b>Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.price)?.toFixed(2)}${' '}<br/>`
        }
        break;
      case "PRODUCT_PRICE_CHANGE":
        // additionDescrription = additionDescrription + `Price changed from <b>${JSON.parse(item?.additionalInfo)?.oldPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2) : JSON.parse(item?.additionalInfo)?.oldPrice}</b> to <b>${JSON.parse(item?.additionalInfo)?.newPrice > 0 ? parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2) : 0.0}</b>`
        let priceVariantId = JSON.parse(item?.additionalInfo)?.variantId
        let priceChangeName = ""
        additionDescrription = additionDescrription + `<p class="m-0">${priceChangeName ? `<b>Name: </b>${priceChangeName}<br/>` : ''}
          <b>Variant Id:</b> ${priceVariantId?.split('/')?.pop()}<br/>
          <b>Old Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}<br/>
          <b>New Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}</p>`
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
        let name = ""
        additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>New Price:</b> ${newPrice ? parseFloat(newPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}<br/>
            <b>Old Price:</b> ${oldPrice ? parseFloat(oldPrice)?.toFixed(2) : parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}<br/></p>`
            }
        break;
      case "ONE_TIME_PURCHASE_PRODUCT_ADDED":
        if (item?.additionalInfo) {
          let variantId = JSON.parse(item?.additionalInfo)?.variantId
          let name = ""
          additionDescrription = additionDescrription + `
            <b>Variant Id:</b> ${variantId}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
        }

        break;
      case "ONE_TIME_PURCHASE_PRODUCT_REMOVED":
        if (item?.additionalInfo) {
          let variantId = JSON.parse(item?.additionalInfo)?.variantId
          let name = ""
          additionDescrription = additionDescrription + `
            <b>Variant Id:</b> ${variantId}<br/>
            <b> Quantity:</b> ${JSON.parse(item?.additionalInfo)?.quantity}</p>`
        }

        break;
      case "PRICE_CHANGE_SYNC":
        if (item?.additionalInfo) {
          let variantId = JSON.parse(item?.additionalInfo)?.variantId
          let name = ""
          additionDescrription = additionDescrription + `<p class="m-0">${name ? `<b>Name: </b>${name}<br/>` : ''}
            <b>Variant Id:</b> ${variantId?.split('/')?.pop()}<br/>
            <b>New Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.newPrice)?.toFixed(2)}${' '}<br/>
            <b>Old Price:</b> ${parseFloat(JSON.parse(item?.additionalInfo)?.oldPrice)?.toFixed(2)}${' '}<br/></p>`
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

  useEffect(() => {
    console.log("Get account info effect")
    axios.get("api/account").then(res => {
      setDefaultEmail(res?.data?.email);
    }).catch(err => {
      console.log("Error getting account email")
    })
  }, [defaultEmail]);

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
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading={'Events'}
          icon="lnr-gift icon-gradient bg-mean-fruit"
          enablePageTitleAction={true}
          actionTitle="Export"
          updatingText="Exporting"

          onActionClick={() => {
            setIsModalOpen(!isModalOpen);
          }}
          onActionUpdating={exporting}
          sticky={true}
        />
                    <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
                    <FeatureAccessCheck
          hasAnyAuthorities={'enableExportActivityLog'}
          upgradeButtonText="Upgrade to manage subscription"
          tooltip="To access export activity log, upgrade your plan"
          designType={'LOCK'}>
            {
              <>
          <ModalHeader>Export Activity Logs</ModalHeader>
          <ModalBody>
            {allowExport ? <>
            <Label>Email ID</Label>
            <Input
              type="email"
              invalid={!emailValidity}
              onBlur={event => {
                setInputValueForTestEmailId(event.target.value);
                checkEmailValidity(event.target.value);
                setBlurred(true);
              }}
              onInput={event => {
                if (blurred) {
                  setInputValueForTestEmailId(event.target.value);
                  checkEmailValidity(event.target.value);
                }
              }}
              placeholder="Please enter email ID here"
            />
            <FormFeedback>Please Enter a valid email ID</FormFeedback>
            <br/>
            </> : <p>
                A minimum of 1 and maximum 1000 activity logs can be exported at a time. Please use log filter criteria to reduce/increase the search results.
              </p>}

          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={() => {
              cleanupBeforeModalClose()
            }}>
              Cancel
            </Button>
            {allowExport && <MySaveButton
              onClick={() => {
                sendFilteredSubscriptionMail(defaultEmail)
                console.log("Sending email to " + defaultEmail)
              }}
              text="Use Default Email"
              updating={emailSendingProgress}
              updatingText={'Sending'}
              className="btn-secondary"
            />}
            {allowExport && <MySaveButton
              onClick={() => {
                sendFilteredSubscriptionMail(inputValueForTestEmailId)
              }}
              text="Send Email"
              updating={emailSendingProgress}
              updatingText={'Sending'}
            />}
          </ModalFooter></>}
        </FeatureAccessCheck>
        </Modal>
        <SweetAlert
          title="Export Request Submitted"
          confirmButtonColor=""
          show={emailSuccessAlert}
          text="Export may take time based on the number of activity logs in your store. Rest assured, once it's processed, it will be emailed to you."
          type="success"
          onConfirm={() => setEmailSuccessAlert(false)}
        />
        <SweetAlert
          title="Failed"
          confirmButtonColor=""
          show={emailFailAlert}
          text={failMessage}
          type="error"
          onConfirm={() => setEmailFailAlert(false)}
        />
        {
          (loading && !hasFilter()) ? <div style={{margin: "10% 0 0 43%"}}
                                           className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale"/>
          </div> : <>

            {
              (activityLogEntitiesData && (activityLogEntitiesData?.length > 0 || hasFilter())) ? <>
                <Card className="main-card">
                  <CardBody>
                    <Row>
                      <Col xs={12} sm={12} md={7} lg={7}>
                        {
                          Object.keys(filterVal).map((keys, index) => {
                            return (
                              <Fragment key={index}>
                                {
                                  (filterVal[keys] && filterVal[keys] !== undefined && filterVal[keys] !== null) ? <Chip
                                    size="small"
                                    label={
                                      filterVal[keys] ?
                                        <>
                                          {
                                            (keys === "fromCreatedDate" || keys === "toCreatedDate") ? <><b>{_.startCase(keys)}</b>:&nbsp;<span>{new Date(filterVal[keys]).toDateString()}</span></> :
                                              <><b>{_.startCase(keys)}</b>:&nbsp;<span>{_.startCase(filterVal[keys].toLowerCase())}</span></>
                                          }
                                        </>
                                        : ""
                                    }
                                    // onDelete={() => handleChipFilterDelete(keys)}
                                    clickable
                                    variant="outlined"
                                    color="primary"
                                    style={{margin: "2px 2px"}}
                                  /> : ""
                                }
                              </Fragment>
                            )
                          })
                        }
                      </Col>
                      <Col xs={12} sm={12} md={5} lg={5}>
                        <div className="mt-3 mb-md-0 mb-lg-0 mb-xl-0" style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end'}}>
                          <div>
                            <b>Sort: &nbsp;&nbsp;</b>
                          </div>
                          <div style={{width: "40%"}}>
                            <Input type="select" name="sortDir" defaultValue={sortByDir}
                                   onChange={(e) => setSortByDir(e.target.value)}>
                              {
                                sortTypes.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                              }
                            </Input>
                          </div>
                          &nbsp; &nbsp;
                          <div>
                            <ActivityLogFilter filterVal={filterVal} onApply={onApplyFilter} />
                          </div>
                        </div>
                      </Col>
                    </Row>

                    <PerfectScrollbar>
                      <BlockUi
                        tag="div"
                        blocking={loading}
                        loader={<Loader active type="line-scale"/>}
                      >
                        <div>
                          <Table className="mb-0 mt-4 text-left" responsive>
                            <thead>
                            <tr>
                              <th>Event Id</th>
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
                              activityLogEntitiesData && activityLogEntitiesData.map((item, index) => {
                                return (
                                  <tr key={item?.id}>
                                    <td style={{verticalAlign: "top"}}>
                                      {
                                        (item?.entityType && item?.entityType === "SUBSCRIPTION_CONTRACT_DETAILS") ? <Link to={{
                                          pathname: `/dashboards/subscription/${item?.entityId}/detail`,
                                          state: {
                                            from: `${location.search}`,
                                            pathname: location?.pathname
                                          }
                                        }}>#{item?.entityId}</Link> : <span style={{color: "#0000EE"}}>#{item?.entityId}</span>
                                      }
                                    </td>
                                    <td style={{verticalAlign: "top", display: "flex", alignItems: "center"}}>
                                      {
                                        (toTitleCase(ActivityLogEventType.find(({type}) => type === item?.eventType)?.title) ||
                                          <span>{toTitleCase(item?.eventType)}</span>)
                                      }
                                    </td>
                                    <td style={{verticalAlign: "top"}}>
                                      {
                                        item?.eventType === "BILLING_ATTEMPT_NOTIFICATION" ?
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
                                              <div dangerouslySetInnerHTML={{__html: getActivityLogDescription(item)}}/>
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
                                    <td style={{verticalAlign: "top"}}>
                                      {ActivityLogEventSource.find(({type}) => type == item?.eventSource)?.description}
                                    </td>
                                    <td style={{verticalAlign: "top", textAlign: "center"}}>
                                      {item?.status?.toLowerCase() === "success" ?
                                        <div
                                          className="badge badge-pill badge-success"> {item?.status}</div>
                                        : <div
                                          className="badge badge-pill badge-secondary"> {item?.status}</div>
                                      }
                                    </td>
                                    <td style={{verticalAlign: "top"}}>
                                      {item?.createAt != null ? (aryIannaTimeZones.includes(shopInfo?.shopTimeZone.substring(12))
                                        ? momentTZ(item?.createAt)
                                          .tz(shopInfo?.shopTimeZone.substring(12))
                                          .format('MMMM DD, YYYY hh:mm A')
                                        : moment(item?.createAt)?.format('MMMM DD, YYYY hh:mm A')) : "-"}
                                    </td>
                                  </tr>
                                )
                              })
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
                                  onChange={handlePagination}
                                />
                              </div>
                              <JhiItemCount page={activePage} total={totalRowData} itemsPerPage={itemsPerPage}/>
                            </Col>
                          </Row>
                        </div>
                      </BlockUi>
                    </PerfectScrollbar>
                  </CardBody>
                </Card>
              </> : <>
                <Row className="align-items-center welcome-page">
                  <Col sm='5'>
                    <div>
                      <h4>Welcome to Activity Log Section</h4>
                      <p className='text-muted'>
                        This section give you the complete list of subscription activity and time.
                        Currently there is no activity log available.
                      </p>
                      <p className='text-muted'>If you have any questions at any time, just reach out
                        to us on <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                      </p>
                    </div>
                  </Col>
                </Row>
              </>
            }
          </>
        }
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  activityLogEntities: state.activityLog.entities,
  loading: state.activityLog.loading,
  totalItems: state.activityLog.totalItems,
  shopInfo: state.shopInfo.entity,
  exporting: state.activityLog.exporting,
});

const mapDispatchToProps = {
  getFilteredEntities,
  getFilteredActivityLogEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(EventData);

