import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {
    Button,
    Card,
    CardBody,
    FormGroup,
    ListGroup,
    ListGroupItem,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader
} from 'reactstrap';
import moment from 'moment';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import UpdateOrderNoteModal from './UpdateOrderNoteModal';
import {updateEntity} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import DatePicker from 'react-datepicker';
import {addDays} from 'date-fns';
import { convertToShopTimeZoneDate } from './SuportedShopifyTImeZone';
import {toast} from 'react-toastify';
import {
    splitSubscriptionContract
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { isOneTimeProduct } from 'app/shared/util/subscription-utils';
import RescheduleOrder from './RescheduleOrder';

var momentTZ = require('moment-timezone');

const UpcomingOrder = ({
                           updateEntity,
                           updatingBillingAttempt,
                           isPrepaid,
                           upcomingOneTimeVariants,
                           shopInfo,
                           contractId,
                           setBillingIsAttempted,
                           emailTemplateSettingList,
                           splitContractUpdating,
                           splitContractUpdateSuccess,
                           getSubscriptionEntity,
                           getSubscriptionContractDetailsByContractId,
                           getUpcomingOrderEntityList,
                           ...props
                       }) => {
    const [isUpdateOrderNoteOpenFlag, setIsUpdateOrderNoteOpenFlag] = useState(false);
    const updateOrderNoteMethod = async updateCustomerUpcomingOrder => {
        await updateEntity(updateCustomerUpcomingOrder);
        toggleUpdateOrderNoteModal();
    };
    const [isEditShipmentDate, setIsEditShipmentDate] = useState(false);
    const [updatedShipmentDate, setUpdatedShipmentDate] = useState();
    const [openAttemptBillingPopup, setOpenAttemptBillingPopup] = useState(false);
    const [billingAttemptProducts, setBillingAttemptProducts] = useState([]);
    const [asItIs, setAsItIs] = useState(true);
    const [updatingNextOrder, setUpdatingNextOrder] = useState(false);


    const options = {
        autoClose: 500,
        position: toast.POSITION.BOTTOM_CENTER
    };

    async function updateShipmentDateMethod() {
        if (updatedShipmentDate > new Date()) {
            props.ordData.billingDate = updatedShipmentDate.toISOString();
            await updateEntity(props.ordData);
            setIsEditShipmentDate(false);
        }
    }

    async function attemptBilling(id) {
        props
            ?.retryBillingAttempt(id)
            .then(res => {
                setBillingIsAttempted(true);
                toast?.success('Attempt Billing triggred.', options);
            })
            .catch(err => {
                setBillingIsAttempted(false);
                toast?.error('Attempt Billing trigger failed.', options);
            });
    }

    const toggleUpdateOrderNoteModal = () => setIsUpdateOrderNoteOpenFlag(!isUpdateOrderNoteOpenFlag);

    const toggleEditShipmentDateModal = () => setIsEditShipmentDate(!isEditShipmentDate);

    const handleBillingAttemptProducts = lineId => {
        let newProductsIds = [...billingAttemptProducts];
        let index = newProductsIds.findIndex(item => item === lineId);
        if (index > -1) {
            newProductsIds.splice(index, 1);
        } else {
            newProductsIds.push(lineId);
        }
        setBillingAttemptProducts(newProductsIds);
    };

    const processBillingAttempt = () => {
        // if (asItIs || billingAttemptProducts.length === props.subscriptionEntities?.lines?.edges?.length) {
        props.retryBillingAttempt(props.ordData.id);
        setOpenAttemptBillingPopup(false);

        /* } else {
                      props.splitSubscriptionContract(contractId, billingAttemptProducts);
                    }*/
    };

    useEffect(() => {
        if (splitContractUpdateSuccess) {
            setOpenAttemptBillingPopup(false);
        }
    }, [splitContractUpdateSuccess]);

    return (
        <>
            <ListGroup flush>
                <ListGroupItem key={props.ordData.id}>
                    <div className="widget-content p-0">
                        <div className="widget-content-wrapper">
                            <div className="widget-content-left mr-3">
                                <div className="widget-content-left">
                                    {((!props.isFulfillment || !isPrepaid) && props.upcomingSwapProductList && props.upcomingSwapProductList.length > 0)
                                        ? props.upcomingSwapProductList?.map(swapPrd => {
                                            return swapPrd?.productId != null ? (
                                                <div className="d-flex align-items-center">
                                                    <img src={swapPrd?.image} width={50}
                                                         style={{borderRadius: '29%', padding: '10px'}}/>
                                                    <a href={`https://${props.shopName}/admin/products/${swapPrd?.productId?.split('/')[4]}`}
                                                       target="_blank">
                              <span>
                                {' '}
                                  {swapPrd?.productTitle} {swapPrd?.title && '-' + swapPrd?.title}
                              </span>
                                                    </a>
                                                </div>
                                            ) : (
                                                <div className="text-center p-1" style={{background: 'beige'}}>
                                                    <b>
                                                        {' '}
                                                        {swapPrd?.productTitle} {swapPrd?.title && '-' + swapPrd?.title}
                                                    </b>{' '}
                                                    has been removed
                                                </div>
                                            );
                                        })
                                        : !props?.isFulfillment
                                            ? props.subscriptionEntities?.lines?.edges?.map((prd, index) => {
                                                return prd?.node && prd?.node != null && prd?.node?.productId != null ? (
                                                    (!isOneTimeProduct(prd) || (props?.isAttemptBillingDisplayedOnce && isOneTimeProduct(prd))) &&
                                                    <div className="d-flex align-items-center">
                                                        <img
                                                            src={prd?.node?.variantImage?.transformedSrc}
                                                            width={50}
                                                            style={{borderRadius: '29%', padding: '10px'}}
                                                        />
                                                        <a href={`https://${props.shopName}/admin/products/${prd?.node?.productId?.split('/')[4]}`}
                                                           target="_blank">
                              <span>
                                {' '}
                                  {prd?.node?.title}{' '}
                                  {prd?.node?.variantTitle && prd?.node?.variantTitle !== '-' && '-' + prd?.node?.variantTitle}
                              </span>
                                                        </a>
                                                        {isOneTimeProduct(prd) && <span>&nbsp; (one time product)</span>}
                                                    </div>
                                                ) : (
                                                    <div className="text-center p-1" style={{background: 'beige'}}>
                                                        <b>
                                                            {' '}
                                                            {prd?.node?.title}{' '}
                                                            {prd?.node?.variantTitle && prd?.node?.variantTitle !== '-' && '-' + prd?.node?.variantTitle}
                                                        </b>{' '}
                                                        has been removed
                                                    </div>
                                                );
                                            })
                                            : // For prepaid line items
                                            props?.ordData?.lineItems?.edges?.map(prd => {
                                                // if (prd?.node?.lineItem?.contract) {
                                                    return (
                                                        <div className="d-flex align-items-center">
                                                            <img
                                                                src={prd?.node?.image?.url}
                                                        
                                                                width={50}
                                                                style={{borderRadius: '29%', padding: '10px'}}
                                                            ></img>
                                                            {/* <a
                                                                href={`https://${props.shopName}/admin/products/${prd?.node?.lineItem?.productId?.split('/')[4]}`}
                                                                target="_blank"
                                                            > */}
                                                                <span>
                                                                {' '}
                                                                    {prd?.node?.productTitle}{' '}
                                                                    {prd?.node?.variantTitle &&
                                                                        prd?.node?.variantTitle !== '-' &&
                                                                        '-' + prd?.node?.variantTitle}
                                                                </span>
                                                            {/* </a> */}
                                                        </div>
                                                    );
                                                // }
                                            })}

                                    {upcomingOneTimeVariants &&
                                        upcomingOneTimeVariants.length > 0 &&
                                        upcomingOneTimeVariants?.map(item => {
                                            return (
                                                item?.billingAttemptId == props?.ordData?.id && (
                                                    <div className="d-flex align-items-center">
                                                        <img
                                                            src={item?.prdImage || require('./BlankImage.jpg')}
                                                            width={50}
                                                            style={{borderRadius: '29%', padding: '10px'}}
                                                        />
                                                        <a href={`https://${props?.shopName}/admin/products/${item?.prdId}`}
                                                           target="_blank">
                              <span>
                                {`${item?.title}  ${
                                    item?.variantTitle && item?.variantTitle !== '-' && item?.variantTitle !== 'Default Title'
                                        ? '-' + item?.variantTitle
                                        : ``
                                }`}
                              </span>
                                                        </a>{' '}
                                                        &nbsp; (one time product)
                                                    </div>
                                                )
                                            );
                                        })}
                                </div>
                            </div>
                            <div className="widget-content-left ml-5">
                                <div className="widget-heading">Order Date</div>
                                <div className="widget-subheading opacity-10">
                                    {isEditShipmentDate && !isPrepaid ? (
                                        <>
                      <span className="pr-2">
                        {props.isFulfillment ? convertToShopTimeZoneDate(props?.ordData?.fulfillAt, shopInfo.ianaTimeZone) : convertToShopTimeZoneDate(props?.ordData?.billingDate, shopInfo.ianaTimeZone)}
                      </span>
                                            {/* <Button
                                                color="link"
                                                title="Edit Shipment Date"
                                                className="btn-icon btn-icon-only btn-pill"
                                                active
                                                onClick={() => setIsEditShipmentDate(true)}
                                            >
                                                <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                            </Button> */}
                                            <Card className="card-margin" style={{borderRadius: '10%'}}>
                                                <CardBody>
                                                    <DatePicker
                                                        selected={
                                                            updatedShipmentDate
                                                                ? updatedShipmentDate
                                                                : props.isFulfillment
                                                                    ? new Date(props.ordData?.fulfillAt)
                                                                    : new Date(props.ordData?.billingDate)
                                                        }
                                                        onChange={date => setUpdatedShipmentDate(date)}
                                                        timeInputLabel="Time:"
                                                        minDate={addDays(new Date(), 1)}
                                                        dateFormat="MM/dd/yyyy h:mm aa"
                                                        showTimeInput
                                                        timezone={shopInfo?.ianaTimeZone}
                                                    />

                                                    <div style={{display: 'flex', marginTop: '10px'}}>
                                                        <br/>
                                                        <Button
                                                            className="mr-2 btn-icon btn-icon-only"
                                                            color="danger"
                                                            title="Cancel"
                                                            onClick={() => setIsEditShipmentDate(false)}
                                                        >
                                                            <i className="pe-7s-close btn-icon-wrapper"> </i>
                                                        </Button>
                                                        <Button
                                                            className="mr-2 btn-icon btn-icon-only"
                                                            color="success"
                                                            title="Update"
                                                            onClick={() => updateShipmentDateMethod()}
                                                        >
                                                            {updatingBillingAttempt ? 'Processing...' :
                                                                <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                                        </Button>
                                                    </div>
                                                </CardBody>
                                            </Card>
                                        </>
                                    ) : (
                                        <>
                      <span className="pr-2">
                        {props.isFulfillment
                            ? convertToShopTimeZoneDate(props?.ordData?.fulfillAt, shopInfo.ianaTimeZone)
                            : convertToShopTimeZoneDate(props.ordData?.billingDate, shopInfo.ianaTimeZone)}
                      </span>
                                        </>
                                    )}

                                    <div className="pr-2">
                                        Status:{' '}
                                        {props.ordData.status == 'QUEUED' || props.ordData.status == 'SCHEDULED' ? (
                                            <div className="badge badge-primary ml-2">{props.ordData.status}</div>
                                        ) : (
                                            <div className="badge badge-secondary ml-2">{props.ordData.status}</div>
                                        )}
                                    </div>
                                    {!props.isFulfillment ?
                                    <div className="pr-2">
                                        Upcoming order email will be sent on{' '}
                                        <b>
                                            {props.isFulfillment
                                                ? momentTZ(props?.ordData?.fulfillAt)
                                                        .tz(shopInfo?.ianaTimeZone)
                                                        .subtract(
                                                            emailTemplateSettingList?.find(item => item?.emailSettingType === 'UPCOMING_ORDER')
                                                                ?.upcomingOrderEmailBuffer,
                                                            'd'
                                                        )
                                                        .format('MMMM DD, YYYY hh:mm A')
                                                : momentTZ(props.ordData?.billingDate)
                                                        .tz(shopInfo?.ianaTimeZone)
                                                        ?.subtract(
                                                            emailTemplateSettingList?.find(item => item?.emailSettingType === 'UPCOMING_ORDER')
                                                                ?.upcomingOrderEmailBuffer,
                                                            'd'
                                                        )
                                                        .format('MMMM DD, YYYY hh:mm A')}
                                        </b>
                                    </div>:''}

                                    {props.ordData?.upcomingOrderEmailSentStatus != null ? (
                                        <div>
                                            <span
                                                className="widget-heading">Upcoming order email sent status : </span>
                                            <span
                                                className="badge badge-info ml-2">{props.ordData?.upcomingOrderEmailSentStatus}</span>
                                        </div>
                                    ) : ""}
                                </div>
                            </div>

                            <div className="widget-content-right text-right mr-3">
                                {(props.ordData.status == 'QUEUED' || props.ordData.status == 'SCHEDULED') && (
                                    <div className="d-block text-center d-flex">
                                        {!props.isAttemptBillingDisplayedOnce && (
                                            <div>
                                                <Button
                                                    color="link"
                                                    title="Edit Shipment Date"
                                                    className="btn-icon btn-icon-only btn-pill"
                                                    active
                                                    onClick={() => toggleEditShipmentDateModal()}
                                                >
                                                    <i className="lnr lnr-pencil btn-icon-wrapper"></i> Reschedule
                                                </Button>
                                                <RescheduleOrder
                                                    isUpdating={updatingNextOrder}
                                                    modaltitle={!isPrepaid ? "Reschedule Fulfilment" : "Reschedule Order"}
                                                    confirmBtnText="Update"
                                                    cancelBtnText="Cancel"
                                                    ordData={props.ordData}
                                                    updatedShipmentDate={updatedShipmentDate}
                                                    isFulfillment={props?.isFulfillment}
                                                    setUpdatedShipmentDate={setUpdatedShipmentDate}
                                                    shopInfo={shopInfo}
                                                    setUpdatingNextOrder={setUpdatingNextOrder}
                                                    toggleEditShipmentDateModal={toggleEditShipmentDateModal}
                                                    isEditShipmentDate={isEditShipmentDate}
                                                    updateEntity={updateEntity}
                                                    toast={toast}
                                                    getSubscriptionEntity={getSubscriptionEntity}
                                                    contractId={contractId}
                                                    options={options}
                                                    getSubscriptionContractDetailsByContractId={getSubscriptionContractDetailsByContractId}
                                                    getUpcomingOrderEntityList={getUpcomingOrderEntityList}
                                                />
                                            </div>      
                                        )}
                                        {props.ordData.status == 'QUEUED' && props.isAttemptBillingDisplayedOnce && (
                                            <>
                                                <MySaveButton
                                                    // onClick={() => attemptBilling(props.ordData.id)}
                                                    onClick={() => setOpenAttemptBillingPopup(!openAttemptBillingPopup)}
                                                    text="Attempt Billing"
                                                    addBuffer={false}
                                                    updating={updatingBillingAttempt}
                                                    updatingText={'Processing..'}
                                                    className="btn-small btn-success mr-2"
                                                >
                                                    Attempt Billing
                                                </MySaveButton>
                                            </>
                                        )}
                                        {props.ordData.status == 'QUEUED' && !props.isAttemptBillingDisplayedOnce && (
                                            <div>
                                                <Button
                                                    // className="btn-icon btn-icon-only pl-0"
                                                    className="primary mr-2 btn btn-primary d-flex align-items-center btn btn-primary"
                                                    color="link"
                                                    title="Update Order Note"
                                                    onClick={toggleUpdateOrderNoteModal}
                                                >
                                                    Order Note
                                                </Button>
                                                <UpdateOrderNoteModal
                                                    isUpdating={updatingBillingAttempt}
                                                    modaltitle="Update Order Note"
                                                    confirmBtnText="Update"
                                                    cancelBtnText="Cancel"
                                                    initialOrderNote={props.ordData}
                                                    isUpdateOrderNoteOpenFlag={isUpdateOrderNoteOpenFlag}
                                                    toggleUpdateOrderNoteModal={toggleUpdateOrderNoteModal}
                                                    updateOrderNoteMethod={subscriptionContractDetailsModel =>
                                                        updateOrderNoteMethod(subscriptionContractDetailsModel)
                                                    }
                                                />
                                            </div>
                                        )}
                                        <MySaveButton
                                            onClick={() => props.skipShipment(props.ordData.id, props.isFulfillment)}
                                            text={props.isFulfillment ? 'Skip Fulfillment' : 'Skip Order'}
                                            addBuffer={false}
                                            updating={props.updatingSkipOrder}
                                            updatingText={'Processing..'}
                                            className="btn-small btn-info"
                                        >
                                            {props.isFulfillment ? 'Skip Fulfillment' : 'Skip Order'}
                                        </MySaveButton>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </ListGroupItem>
            </ListGroup>

            <Modal size="md" isOpen={openAttemptBillingPopup}
                   toggle={() => setOpenAttemptBillingPopup(!openAttemptBillingPopup)}>
                <ModalHeader
                    toggle={() => setOpenAttemptBillingPopup(!openAttemptBillingPopup)}
                    close={() => setOpenAttemptBillingPopup(!openAttemptBillingPopup)}
                >
                    Attempt Billing
                </ModalHeader>
                <ModalBody className="multiselect-modal-body">
                    <div>
                        <FormGroup tag="fieldset">
                            <legend>
                                Please note, once you confirm Order Now. The Subscription details will be updated within
                                a minute or you may refresh the
                                page after sometime. It is requested that multiple attempts of Order Now should not be
                                placed
                            </legend>
                            {/*<FormGroup check>
                <Label check>
                  <Input checked={asItIs} type="radio" name="radio1" onChange={value => setAsItIs(true)} /> Continue with all products
                </Label>
              </FormGroup>
              <FormGroup check>
                <Label check>
                  <Input disabled={true} checked={!asItIs} type="radio" name="radio1" onChange={value => setAsItIs(false)} />
                  <span className={'text-muted'}>Split/customised the subscription and place order (Coming soon).</span>
                </Label>
              </FormGroup>*/}
                        </FormGroup>
                        {/*<FormGroup className={'ml-5'}>
              {!asItIs &&
                props.subscriptionEntities?.lines?.edges?.map(product => {
                  return product?.node && product?.node != null && product?.node?.productId != null ? (
                    <div className="d-flex align-items-center">
                      <Input
                        id={product?.node?.id}
                        checked={billingAttemptProducts.findIndex(item => item === product?.node?.id) > -1}
                        type="checkbox"
                        onChange={() => handleBillingAttemptProducts(product?.node?.id)}
                      />
                      <img src={product?.node?.variantImage?.transformedSrc} width={50} style={{ borderRadius: '29%', padding: '10px' }} />
                      <a href={`https://${props.shopName}/products/${product?.node?.productId?.split('/')[4]}`} target="_blank">
                        <span>
                          {' '}
                          {product?.node?.title}{' '}
                          {product?.node?.variantTitle && product?.node?.variantTitle !== '-' && '-' + product?.node?.variantTitle}
                        </span>
                      </a>
                    </div>
                  ) : (
                    ''
                  );
                })}
            </FormGroup>*/}
                    </div>
                </ModalBody>

                <ModalFooter>
                    <Button color="danger" onClick={() => setOpenAttemptBillingPopup(!openAttemptBillingPopup)}>
                        Cancel
                    </Button>
                    <Button color="primary" onClick={() => processBillingAttempt()} disabled={splitContractUpdating}>
                        {splitContractUpdating ? (
                            <div className="d-flex">
                                <div className="appstle_loadersmall"/>
                                <span className="ml-2"> Please Wait..</span>
                            </div>
                        ) : (
                            'Order now'
                        )}
                    </Button>
                </ModalFooter>
            </Modal>
        </>
    );
};

const mapStateToProps = state => ({
    updatingBillingAttempt: state.subscriptionBillingAttempt.updatingBillingAttempt,
    splitContractUpdating: state.subscriptionContractDetails.updating,
    splitContractUpdateSuccess: state.subscriptionContractDetails.updateSuccess,
    emailTemplateSettingList: state.emailTemplateSetting.entities,
    shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
    updateEntity,
    splitSubscriptionContract
};

export default connect(mapStateToProps, mapDispatchToProps)(UpcomingOrder);
