import React, {Fragment, useEffect, useState} from 'react';
import {
    Col,
    Input,
    Button,
    Label,
    Modal,
    ModalHeader,
    ModalBody,
    Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import './loader.scss';
import {addDays} from 'date-fns';
import DatePicker from 'react-datepicker';
import Axios from 'axios';

const RescheduleOrder = (props) => {
    const {
      isUpdating,
      modaltitle,
      confirmBtnText,
      updatedShipmentDate,
      setUpdatedShipmentDate,
      shopInfo,
      toggleEditShipmentDateModal,
      isEditShipmentDate,
      setUpdatingNextOrder,
      toast,
      getSubscriptionEntity,
      contractId,
      options,
      getSubscriptionContractDetailsByContractId,
      getUpcomingOrderEntityList,
    } = props;
  
    const nextOrderDateupdate = async () => {
        const requestUrl = `api/subscription-billing-attempts`;
        return Axios.put(requestUrl, {...props?.ordData,billingDate: updatedShipmentDate.toISOString() })
          .then(async res => {
            // setUpdateInProgress(false);
            await getSubscriptionEntity(contractId);
            await getSubscriptionContractDetailsByContractId(contractId);
            await getUpcomingOrderEntityList(contractId);
            setUpdatingNextOrder(false)
            toggleEditShipmentDateModal();
            toast.success('Next order date updated', options);
            
            return res;
          })
          .catch(err => {
            setUpdatingNextOrder(false);
            toast.error(err.response.data.message, options);
            return res;
          });
      };
      const nextOrderDateReschedule = async () => {
        const requestUrl = `api/subscription-contract-details/subscription-fulfillment/reschedule?fulfillmentId=${props?.ordData?.id}&deliveryDate=${updatedShipmentDate.toISOString()}`;
        // console.log('nextOrderData', {...nextOrderData,billingDate: selectedDateForNextOrder.toISOString() });
        return Axios.put(requestUrl)
          .then(async res => {
            // setUpdateInProgress(false);
            await getSubscriptionEntity(contractId);
            await getSubscriptionContractDetailsByContractId(contractId);
            await getUpcomingOrderEntityList(contractId);
            setUpdatingNextOrder(false)
            toggleEditShipmentDateModal();
            toast.success('Next order date updated', options);
            
            return res;
          })
          .catch(err => {
            setUpdatingNextOrder(false);
            toast.error(err.response.data.message, options);
            return res;
          });
      };
      const nextOrderDateSubscriptionHandler = async () => {
        setUpdatingNextOrder(true)
        let results = props?.isFulfillment ? await nextOrderDateReschedule() : await nextOrderDateupdate();
        if (results) {
          if (results?.status !== 200) {
            setNextOrderDateSuccess(false);
            setNextOrderDateError(true);
          }
        }
      };
    return (
        <div>
          <Modal isOpen={isEditShipmentDate} size='lg' toggle={toggleEditShipmentDateModal}>
            <ModalHeader toggle={toggleEditShipmentDateModal}>{modaltitle}</ModalHeader>
            <ModalBody>
              <div style={{ padding: '22px' }}>
              <Row>
                                <Col md={12}>
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
                                    timezone={shopInfo.ianaTimeZone}
                                />
                                </Col>
                            </Row>                           
                        <div style={{marginTop: '21px', display: 'flex', justifyContent: 'center'}}>
                            <Button
                            style={{marginRight: '12px'}}
                            size="lg" 
                            className="btn-shadow-primary" 
                            color="primary" 
                            type="submit" 
                            // disabled={submitting}
                            onClick={nextOrderDateSubscriptionHandler}
                            >
                            {isUpdating ? 
                                <div className="d-flex align-items-center">
                                    <div className="appstle_loadersmall" /> 
                                    <span className="ml-2 font-weight-light"> Please Wait</span>
                                </div> 
                                : confirmBtnText}
                            </Button>
                            <Button 
                                size="lg" 
                                className="btn-shadow-primary" 
                                color="danger"
                                type="button"
                                onClick={toggleEditShipmentDateModal}
                            >
                            cancel
                            </Button>
                        </div>
              </div>
            </ModalBody>
          </Modal>
        </div>
      );
    }
    
    export default RescheduleOrder;