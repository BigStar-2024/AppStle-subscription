import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {
  Button,
  Row,
  Col,
  Input,
  Modal, ModalHeader, ModalBody, ModalFooter
} from 'reactstrap';
import _ from "lodash";

import moment from "moment";
import DateRangePicker from '@wojtekmaj/react-daterange-picker';
import "./style.scss"
import PrdVariantRadioPopup from '../AdvancedSetting/PrdVariantRadioPopupSubscriptionFilter';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFilter} from "@fortawesome/free-solid-svg-icons";
import PrdRadioPopup from '../AdvancedSetting/PrdRadioPopupSubscriptionFilter';
import Select from 'react-select'
import 'react-calendar/dist/Calendar.css';
import '@wojtekmaj/react-daterange-picker/dist/DateRangePicker.css';

var momentTZ = require('moment-timezone');

function SubscriptionFilter(props) {
  const {shopInfo, filterVal, subscriptionGrpPlanEntities} = props;
  const [loaded, setLoaded] = useState(false)
  const [createdDateRange, setcreatedDateRange] = useState([null, null])
  const [nextDateRange, setnextDateRange] = useState([null, null]);
  const [status, setStatus] = useState("")
  const [planType, setPlanType] = useState("")
  const [recordType, setRecordType] = useState("")
  const [subscriptionContractId, setSubscriptionContractId] = useState("")
  const [customerName, setCustomerName] = useState("")
  const [orderName, setOrderName] = useState("")
  const [billingPolicyIntervalCount, setBillingPolicyIntervalCount] = useState(null)
  const [billingPolicyInterval, setBillingPolicyInterval] = useState("");
  const [selectedSellingPlan, setSelectedSellingPlan] = useState([]);
  const [productId, setProductId] = useState(undefined);
  const [variantId, setVariantId] = useState(undefined);
  const [minOrderAmount, setMinOrderAmount] = useState("");
  const [maxOrderAmount, setMaxOrderAmount] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);



  const toggle = () => setIsModalOpen(!isModalOpen);

  const statusList = [
    {key: 'active', title: 'Active'},
    {key: 'cancelled', title: 'Cancelled'},
    {key: 'paused', title: 'Paused'},
  ];
  const planTypeList = [
    {key: 'prepaid', title: 'Prepaid Plan'},
    {key: 'non-prepaid', title: 'Pay As You Go Plan'},
  ];
  const subscriptionTypesList = [
    {key: 'DAY', title: 'Days'},
    {key: 'WEEK', title: 'Week'},
    {key: 'MONTH', title: 'Month'},
    {key: 'YEAR', title: 'Year'},
  ];
  const applyCallback = (date) => {
    let tempDateRange = [null, null];
    if (date) {
      tempDateRange = date;
    }
    setcreatedDateRange(tempDateRange);
  }
  const applyNextCallback = (date) => {
    let tempDateRange = [null, null];
    if (date) {
      tempDateRange = date;
    }
    setnextDateRange(tempDateRange);
  }
  const onApply = () => {
    if (props.onApply) {
      props.onApply({
        fromCreatedDate: createdDateRange[0] ? momentTZ(createdDateRange[0]).tz(shopInfo.ianaTimeZone, true).format() : undefined,
        toCreatedDate: createdDateRange[1] ? momentTZ(createdDateRange[1]).tz(shopInfo.ianaTimeZone, true).format() : undefined,
        fromNextDate: nextDateRange[0] ? momentTZ(nextDateRange[0]).tz(shopInfo.ianaTimeZone, true).format() : undefined,
        toNextDate: nextDateRange[1] ? momentTZ(nextDateRange[1]).tz(shopInfo.ianaTimeZone ,true).format() : undefined,
        status: status || undefined,
        planType: planType || undefined,
        recordType: recordType || undefined,
        subscriptionContractId: subscriptionContractId || undefined,
        customerName: customerName || undefined,
        orderName: orderName || undefined,
        billingPolicyIntervalCount: billingPolicyIntervalCount || undefined,
        billingPolicyInterval: billingPolicyInterval || undefined,
        sellingPlanIds: selectedSellingPlan || undefined,
        variantId: variantId || undefined,
        productId: productId || undefined,
        minOrderAmount: minOrderAmount || undefined,
        maxOrderAmount: maxOrderAmount || undefined
      })
    }
    toggle();
  }
  const onReset = () => {
    if (props.onApply) {
      props.onApply({})
    }
    toggle();
  }
  const onCancel = () => {
    toggle();
  }
  useEffect(() => {
    let tempCreatedDateRange = [null, null];
    if (filterVal) {
      if (filterVal.fromCreatedDate) {
        tempCreatedDateRange[0] = moment(filterVal.fromCreatedDate);
      }
      if (filterVal.toCreatedDate) {
        tempCreatedDateRange[1] = moment(filterVal.toCreatedDate);
      }
    }
    setcreatedDateRange(tempCreatedDateRange);
    let tempNextDateRange = [null, null];
    if (filterVal) {
      if (filterVal.fromNextDate) {
        tempNextDateRange[0] = moment(filterVal.fromNextDate);
      }
      if (filterVal.toNextDate) {
        tempNextDateRange[1] = moment(filterVal.toNextDate);;
      }
      if (filterVal.status) {
        setStatus(filterVal.status);
      }
      if (filterVal.planType) {
        setPlanType(filterVal.planType);
      }
      if (filterVal.recordType) {
        setRecordType(filterVal.recordType);
      }
      if (filterVal.subscriptionContractId) {
        setSubscriptionContractId(filterVal.subscriptionContractId);
      }
      if (filterVal.customerName) {
        setCustomerName(filterVal.customerName);
      }
      if (filterVal.orderName) {
        setOrderName(filterVal.orderName);
      }
      if (filterVal.billingPolicyIntervalCount) {
        setBillingPolicyIntervalCount(filterVal.billingPolicyIntervalCount);
      }
      if (filterVal.billingPolicyInterval) {
        setBillingPolicyInterval(filterVal.billingPolicyInterval);
      }
      if(filterVal.sellingPlan)
      {
        setSelectedSellingPlan(filterVal.sellingPlan)
      }
    }
    setnextDateRange(tempNextDateRange);
    setLoaded(true);
    return () => {
    }
  }, [filterVal])


  return (
    <Fragment>
      <Button className="btn-lg" color="none" onClick={toggle} style={{border: "1px solid #ced4da"}}>
        Filters <FontAwesomeIcon icon={faFilter}/>
      </Button>
      <Modal isOpen={isModalOpen} toggle={toggle} size={"xl"}>
        <ModalHeader toggle={toggle}/>
        <ModalBody>
            {loaded &&
              <>
               <Row>
                 <Col sm={12} md={12} lg={5} xl={6}>
                   <Row>
                     <Col sm="4">
                       <label className="font-weight-bold">Subscription Id</label>
                     </Col>
                     <Col sm="8">
                       <Input type="text" name="subscriptionContractId" defaultValue={subscriptionContractId}
                              onChange={(e) => setSubscriptionContractId(e.target.value)}>
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Customer</label>
                     </Col>
                     <Col sm="8">
                       <Input type="text" name="customerName" defaultValue={customerName}
                              onChange={(e) => setCustomerName(e.target.value)}>
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Subscription Type</label>
                     </Col>
                     <Col sm="3">
                       <Input type="number" name="billingPolicyIntervalCount" defaultValue={billingPolicyIntervalCount}
                              onChange={(e) => setBillingPolicyIntervalCount(e.target.value)}>
                       </Input>
                     </Col>
                     <Col sm="5">
                       <Input type="select" name="billingPolicyInterval" defaultValue={billingPolicyInterval}
                              onChange={(e) => setBillingPolicyInterval(e.target.value)}>
                         <option value="">All</option>
                         {
                           subscriptionTypesList.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                         }
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Plan Type</label>
                     </Col>
                     <Col sm="8">
                       <Input type="select" name="planType" defaultValue={planType} onChange={(e) => setPlanType(e.target.value)}>
                         <option value="">All</option>
                         {
                           planTypeList.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                         }
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Selling Plan</label>
                     </Col>
                     <Col sm="8">
                      <Select
                        isMulti
                        name="sellingPlan"
                        options={subscriptionGrpPlanEntities.map(item => ({value: item?.id, label: `${item?.frequencyName} - (${item?.groupName})`}))}
                        onChange={(e) => {
                          setSelectedSellingPlan(e?.map(item => item?.value?.split('/').pop()).join(','))
                        }}
                      />
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Product Id</label>
                     </Col>
                     <Col sm="8">
                       <Input type="text" name="productId" defaultValue={productId}
                              onChange={(e) => {
                                setProductId(e.target.value);
                                }} value={productId}>
                       </Input>
                       <PrdRadioPopup
                              value={productId}
                              onChange={(selectData) => {
                                setProductId(selectData?.id);
                              }}
                              isSource={false}
                              totalTitle="select product"
                              index={1}
                              methodName="Save"
                              buttonLabel="select product"
                              header="Select Product"
                            />
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Variant Id</label>
                     </Col>
                     <Col sm="8">
                       <Input type="text" name="variantId" defaultValue={variantId}
                              onChange={(e) => setVariantId(e.target.value)}  value={variantId}>
                       </Input>
                       <PrdVariantRadioPopup
                              value={variantId}
                              onChange={(selectData) => {
                                setVariantId(selectData.id)
                              }}
                              isSource={false}
                              totalTitle="select product variant"
                              index={1}
                              methodName="Save"
                              buttonLabel="select product variant"
                              header="Product Variants"
                            />
                     </Col>
                   </Row>
                 </Col>
                 <Col sm={12} md={12} lg={7} xl={6}>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Record Type?</label>
                     </Col>
                     <Col sm="8">
                       <Input type="select" name="recordType" defaultValue={recordType} onChange={(e) => setRecordType(e.target.value)}>
                         <option value="">All</option>
                         <option value="imported">Imported</option>
                         <option value="nonImported">Non-Imported</option>
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Order No</label>
                     </Col>
                     <Col sm="8">
                       <Input type="text" name="orderName" defaultValue={orderName} onChange={(e) => setOrderName(e.target.value)}>
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Create Order Date</label>
                     </Col>
                     <Col sm="8">
                       <DateRangePicker
                         className="form-control"
                         onChange={applyCallback}
                         //format='dd/MM/y'
                         value={createdDateRange}
                       />
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Next Order Date</label>
                     </Col>
                     <Col sm="8">
                       <DateRangePicker
                         className="form-control"
                         onChange={applyNextCallback}
                         // format='dd/MM/y'
                         value={nextDateRange}
                       />
                     </Col>

                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Subscription Status</label>
                     </Col>
                     <Col sm="8">
                       <Input type="select" name="sortDir" defaultValue={status} onChange={(e) => setStatus(e.target.value)}>
                         <option value="">All</option>
                         {
                           statusList.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                         }
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Min Order Amount</label>
                     </Col>
                     <Col sm="8">
                       <Input type="number" min="0" name="minOrderAmount" defaultValue={minOrderAmount}
                              onChange={(e) => setMinOrderAmount(e.target.value)}>
                       </Input>
                     </Col>
                   </Row>
                   <Row className="mt-3">
                     <Col sm="4">
                       <label className="font-weight-bold">Max Order Amount</label>
                     </Col>
                     <Col sm="8">
                       <Input type="number" min="0" name="maxOrderAmount" defaultValue={maxOrderAmount}
                              onChange={(e) => setMaxOrderAmount(e.target.value)}>
                       </Input>
                     </Col>
                   </Row>
                 </Col>
               </Row>
                {/* <PrdVariantRadioPopup
            // value={""}
            onChange={(selectData) => console.log(selectData)}
            isSource={false}
            totalTitle="select product variant"
            index={1}
            methodName="Save"
            buttonLabel="select product variant"
            header="Product Variants"
          /> */}
              </>
            }
        </ModalBody>
        <ModalFooter>
          <div>
            <Button color="default" onClick={onReset}>Reset</Button>
          </div>
          <div className="ml-auto">
            <Button color="default" onClick={onCancel}>Cancel</Button>
            <Button color="primary" onClick={onApply}>Apply</Button>
          </div>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity
});


export default connect(mapStateToProps)(SubscriptionFilter)
