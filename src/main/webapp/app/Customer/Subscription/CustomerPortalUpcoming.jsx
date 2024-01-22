import React, {useState, useEffect} from 'react';
import Axios from "axios";
import {
  Container,
  Table,
  InputGroup,
  Input,
  InputGroupText,
  Label,
  FormGroup,
  Row,
  Col,
  Card,
  Collapse,
  CardHeader,
  CardFooter,
  Alert,
  CardBody,
  Badge,
  ListGroup,
  UncontrolledButtonDropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  ListGroupItem,
  Pagination,
  PaginationItem,
  PaginationLink,
  Button,
  Popover, PopoverHeader, PopoverBody
} from 'reactstrap';
import moment from "moment";
import {useSelector} from "react-redux";
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import EditOrderNote from './EditOrderNote';
import {getLocaleDate} from 'app/shared/util/customer-utils';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPencilAlt, faTrash, faCheck, faTimes, faLock} from '@fortawesome/free-solid-svg-icons';
import {motion, AnimatePresence} from "framer-motion"
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {
  customerPortalupdateEntity as updateEntity
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import {connect} from 'react-redux';
import {
  deleteOneOffProducts, getCustomerPortalEntity
} from 'app/entities/subscriptions/subscription.reducer';
import OneOffProductsItem from "./OneOffProductsItem";
import {DateTime} from "luxon";

const CustomerPortalUpcoming = ({
                                  updateEntity,
                                  deleteOneOffProducts,
                                  getCustomerPortalEntity,
                                  oneoffDeleteInProgress,
                                  indexKey,
                                  subscriptionContractFreezeStatus,
                                  ...props
                                }) => {
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  let [handleAlert, setHandleAlert] = useState({show: false, data: null});
  // custom variable
  let frequencyIntervalTranslate = {
    "week": customerPortalSettingEntity?.weekText,
    "day": customerPortalSettingEntity?.dayText,
    "month": customerPortalSettingEntity?.monthText,
    "year": customerPortalSettingEntity?.yearText,
  };
  const getFrequencyTitle = (interval) => {
    return frequencyIntervalTranslate[interval.toLowerCase(interval)] ? frequencyIntervalTranslate[interval.toLowerCase()] : interval
  }

  let [updateInProgress, setUpdateInProgress] = useState(false);
  const [editMode, setEditMode] = useState(false)
  const [selectedDate, setSelectedDate] = useState(null)
  const [skipOrderError, setSkipOrderError] = useState(null);
  const [skipShipmentInProgress, setSkipShipmentInProgress] = useState(false);

  async function updateDate() {
    setUpdateInProgress(true);
    props.ordData.billingDate = selectedDate.toISOString();
    await updateEntity(props.ordData);
    setUpdateInProgress(false);
    setEditMode(!editMode)
  }

  const onDateChange = (date) => {
    setSelectedDate(date);
  }

  // const deleteOneTimeVariants = async (variantId, contractId, billingID) => {
  //   await deleteOneOffProducts(variantId, contractId, billingID, window?.appstle_api_key);
  // }

  const skipShipment = async () => {
    setSkipOrderError("");
    setSkipShipmentInProgress(true);
    let result = await props.skipShipment(props?.ordData?.id, props?.isPrepaid)
    if (result?.response?.data?.message) {
      setSkipOrderError(result?.response?.data?.message);
    }
    setSkipShipmentInProgress(false);
  }

  return (
    <>
      <Card style={{boxShadow: '0 0px 4px 0 rgb(0 0 0 / 15%)', marginTop: '16px'}}>
        <CardBody>
          <Row>
            <Col md={3} style={{display: "flex", flexWrap: "wrap"}}>
              {
                props.upcomingSwapProductList && props.upcomingSwapProductList.length > 0 ?
                  props?.upcomingSwapProductList?.map(swapPrd => {
                    return (swapPrd?.productId != null &&
                      <img
                        src={swapPrd?.image}
                        alt=""
                        style={{
                          width: "48%",
                          height: "80px",
                          padding: "1%",
                          objectFit: "cover"
                        }}
                      />
                    )
                  })
                  :
                  props.subscriptionEntities.lines?.edges.map(contractItem =>
                    contractItem.node.productId !== null &&
                    (
                      <img
                        src={contractItem.node?.variantImage?.transformedSrc}
                        alt=""
                        style={{
                          width: "48%",
                          height: "100px",
                          padding: "1%",
                          objectFit: "cover"
                        }}
                      />
                    ))
              }
              {
                !props.isPrepaid && props?.upcomingOneTimeVariants &&
                props?.upcomingOneTimeVariants?.map(data =>
                  data?.billingID == props?.ordData?.id && (
                    <img
                      src={data?.prdImage}
                      alt=""
                      style={{
                        maxHeight: '100%',
                        alignSelf: 'flex-start',
                        padding: '5px',
                        borderRadius: '2px',
                        flexGrow: 1,
                        maxWidth: '58px',
                        marginBottom: "15px"
                      }}
                    />
                  ))
              }
            </Col>
            <Col md={7}>
              <div className="d-flex align-items-center">
                {
                  props.isPrepaid ?
                    <><b>{customerPortalSettingEntity?.nextDeliveryDate}:</b>&nbsp;
                      {DateTime.fromISO(props.ordData?.fulfillAt).toFormat(customerPortalSettingEntity?.dateFormat)}
                    </>
                    :
                    <><b>{customerPortalSettingEntity?.nextOrderDateLbl}:</b>&nbsp;
                      {DateTime.fromISO(props.ordData?.billingDate).toFormat(customerPortalSettingEntity?.dateFormat)}
                    </>
                }
                {props.ordData.status === "QUEUED" || props.ordData.status === "SCHEDULED" ?
                  <span
                    className={`appstle_badge appstle_QUEUED`}>{customerPortalSettingEntity?.queueBadgeTextV2}</span>
                  :
                  props.ordData.status === "OPEN" ?
                    <span
                      className={`appstle_badge appstle_SKIPPED`}>{customerPortalSettingEntity?.openBadgeText}</span> :
                    <span
                      className={`appstle_badge appstle_SKIPPED`}>{props?.isPrepaid ? (props.ordData.status === "CLOSED" ? customerPortalSettingEntity?.closeBadgeText : customerPortalSettingEntity?.fulfilledText || "fulfilled") : props.ordData.status === "SKIPPED" && customerPortalSettingEntity?.skipBadgeTextV2}</span>}

                {!subscriptionContractFreezeStatus ?
                  ((customerPortalSettingEntity.updateShipmentBillingDate && indexKey) ?
                    <>
                      {(!editMode && !props.isPrepaid) &&
                        <FontAwesomeIcon icon={faPencilAlt} style={{
                          marginLeft: '10px', cursor: "pointer", bottom: "2px",
                          position: "relative"
                        }} onClick={() => {
                          setSelectedDate(moment(props.ordData?.billingDate).toDate());
                          setEditMode(true)
                        }}/>
                      }
                    </> : <></>)
                  :
                  <FontAwesomeIcon
                    className='ml-2'
                    icon={faLock}/>
                }
              </div>
              {
                (customerPortalSettingEntity.updateShipmentBillingDate && indexKey) ?
                  <AnimatePresence initial={false}>
                    {
                      editMode && <motion.div
                        animate="open"
                        exit="collapsed"
                        initial="collapsed"
                        variants={{
                          open: {opacity: 1, height: "auto", scale: 1},
                          collapsed: {opacity: 0, height: 0, scale: 0.95}
                        }}
                        transition={{duration: 0.5}}
                        className="mt-1">
                        <FormGroup>
                          <DatePicker
                            className="form-control"
                            selected={selectedDate}
                            onChange={(date) => onDateChange(date)}
                            dateFormat={customerPortalSettingEntity?.dateFormat || 'dd-MM-yyyy'}
                            minDate={new Date()}
                          />
                        </FormGroup>
                        <div className="mt-2 mb-3 d-flex justify-content-end">
                          <Button className="appstle_order-detail_update-button d-flex align-items-center"
                                  onClick={() => updateDate()}>
                            {!updateInProgress && customerPortalSettingEntity?.updateChangeOrderBtnTextV2}
                            {updateInProgress && <div className="appstle_loadersmall"/>}
                          </Button>
                          <Button className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center"
                                  onClick={() => setEditMode(!editMode)}>
                            {customerPortalSettingEntity?.cancelChangeOrderBtnTextV2}
                          </Button>
                        </div>
                      </motion.div>
                    }
                  </AnimatePresence> : <></>
              }

              <div className="mt-3">
                {customerPortalSettingEntity?.orderNoteFlag && props.ordData.status == "QUEUED" &&
                  // <b>Order Note :</b>{props.ordData?.orderNote}
                  <EditOrderNote subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                 shopName={props.shopName} orderNote={props.ordData.orderNote}
                                 pageName={"UPCOMIG_ORDER"}
                                 id={props.ordData.id}/>
                }
              </div>
              <p>
                <b>{customerPortalSettingEntity?.orderFrequencyTextV2}:</b>
                <span>{customerPortalSettingEntity?.everyLabelTextV2}</span> {props.subscriptionEntities?.billingPolicy.intervalCount} {' '}
                <span>{getFrequencyTitle(props.subscriptionEntities.billingPolicy.interval)}</span>{' '}
              </p>
              <b>{customerPortalSettingEntity?.productLabelTextV2}</b>
              {props.upcomingSwapProductList && props.upcomingSwapProductList.length > 0 ?
                props?.upcomingSwapProductList?.map(swapPrd => (
                  swapPrd?.productId !== null &&
                  <div className="appstle_font_size" style={{marginTop: '5px', color: '#13b5ea'}}>
                    {swapPrd?.productTitle}
                    {swapPrd?.title ? ' - ' + swapPrd?.title : ''}
                    {swapPrd?.quantity > 1 ? (
                      <span style={{marginLeft: '8px'}}>x {swapPrd?.quantity}</span>
                    ) : (
                      ''
                    )}
                  </div>
                ))
                : props.subscriptionEntities.lines?.edges.map(contractItem => (
                  contractItem.node.productId !== null &&
                  <div className="appstle_font_size" style={{marginTop: '5px', color: '#13b5ea'}}>
                    {contractItem.node?.title}
                    {(contractItem?.node?.variantTitle && contractItem?.node?.variantTitle !== "-" && contractItem?.node?.variantTitle !== "Default Title") && ('-' + contractItem?.node?.variantTitle)}
                    {contractItem.node?.quantity > 1 ? (
                      <span style={{marginLeft: '8px'}}>x {contractItem.node?.quantity}</span>
                    ) : (
                      ''
                    )}
                  </div>
                ))}

              {
                (!props.isPrepaid &&
                  props?.upcomingOneTimeVariants &&
                  props?.upcomingOneTimeVariants?.length &&
                  (props?.upcomingOneTimeVariants.some(function (item) {
                    return item?.billingID == props?.ordData?.id
                  }))) ?
                  props?.upcomingOneTimeVariants?.map(item => (
                    (item?.billingID == props?.ordData?.id) &&
                    <OneOffProductsItem
                      id={item?.id}
                      contractId={props?.contractId}
                      billingId={item?.billingID}
                      item={item}
                    />
                  )) : ""
              }
              {(props?.upcomingOneTimeVariants?.length &&
                props?.upcomingOneTimeVariants.some(function (item) {
                  return item?.billingID == props?.ordData?.id
                })) ?
                <div
                  style={{marginTop: "25px"}}>{customerPortalSettingEntity?.oneTimePurchaseNoteTextV2 || "* indicates One Time Purchases"}</div> : ""}
              {skipOrderError ? <div className="mt-3" style={{color: "red"}}>{skipOrderError}</div> : ""}
            </Col>
            <Col md={2} style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}
                 id={`popover-skip-col${indexKey}`}>
              {customerPortalSettingEntity?.showShipment &&
                ((props.ordData.status == "QUEUED" || props.ordData.status == "SCHEDULED") &&
                  <>
                    <Popover
                      container={document.getElementById(`popover-skip-col${indexKey}`)}
                      placement="bottom" isOpen={handleAlert.show}
                      target={`popover-skip-confirm${indexKey}`}
                      toggle={() => setHandleAlert({show: false, data: null})}>
                      <PopoverHeader style={{
                        fontWeight: 'bold',
                        color: '#eb3023'
                      }}>{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || "Are you sure?"}</PopoverHeader>
                      <PopoverBody className="d-flex justify-content-center align-items-center">
                        <Button
                          style={{
                            padding: '8px 10px',
                            backgroundColor: 'white',
                            color: '#eb3023',
                            border: '1px solid #eb3023'
                          }}
                          className="d-flex align-items-center mr-1"
                          onClick={() => {
                            skipShipment(props?.ordData?.id, props?.isPrepaid);
                            setHandleAlert({show: false, data: null})
                          }}
                        >
                          <FontAwesomeIcon icon={faCheck}/>
                        </Button>
                        <Button
                          style={{padding: '8px 10px'}}
                          className="appstle_order-detail_update-button d-flex align-items-center"
                          onClick={() => setHandleAlert({show: false, data: null})}
                        >
                          <FontAwesomeIcon icon={faTimes}/>
                        </Button>
                      </PopoverBody>
                    </Popover>
                    <div className="d-block text-center">
                      <MySaveButton
                        id={`popover-skip-confirm${indexKey}`}
                        onClick={() => setHandleAlert({show: true, data: null})}
                        text={customerPortalSettingEntity?.skipOrderButtonText}
                        disabled={subscriptionContractFreezeStatus || props.checkIfPreventCancellationBeforeDays(customerPortalSettingEntity?.preventCancellationBeforeDays, props?.subscriptionEntities?.nextBillingDate)}
                        lockedIcon={subscriptionContractFreezeStatus || props.checkIfPreventCancellationBeforeDays(customerPortalSettingEntity?.preventCancellationBeforeDays, props?.subscriptionEntities?.nextBillingDate)}
                        updating={skipShipmentInProgress}
                        updatingText={"Processing.."}
                        className="appstle_skipOrderButton"
                      >
                        {customerPortalSettingEntity?.skipOrderButtonText}
                      </MySaveButton>
                    </div>
                  </>
                )
              }
            </Col>
          </Row>
        </CardBody>
      </Card>
    </>
  )
}

const mapStateToProps = state => ({
  // updatingBillingAttempt: state.subscriptionBillingAttempt.updatingBillingAttempt
  oneoffDeleteInProgress: state.subscription.oneoffDeleteInProgress
});

const mapDispatchToProps = {
  updateEntity,
  deleteOneOffProducts,
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalUpcoming);
