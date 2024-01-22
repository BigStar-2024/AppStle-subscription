import React, {Fragment, useEffect, useState} from 'react';
import {Button, Col, Input, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFilter} from "@fortawesome/free-solid-svg-icons";
import DateRangePicker from "@wojtekmaj/react-daterange-picker";
import 'react-calendar/dist/Calendar.css';
import '@wojtekmaj/react-daterange-picker/dist/DateRangePicker.css';
import {ActivityLogEventType} from "app/DemoPages/Dashboards/ActivityLog/activityEnum";
import moment from "moment";


const activityLogStatus = [
  {type: 'SUCCESS', description: "Success"},
  {type: 'FAILURE', description: "Failure"},
  {type: 'INFO', description: "Info"},
]
const activityLogEntityType = [
  {type: 'SUBSCRIPTION_BILLING_ATTEMPT', description: "Subscription Billing Attempt"},
  {type: 'SUBSCRIPTION_CONTRACT_DETAILS', description: "Subscription Contract Details"},
]
const activityLogEventSource = [
  {type: 'CUSTOMER_PORTAL', description: "Customer Portal"},
  {type: 'MERCHANT_PORTAL', description: "Merchant Portal"},
  {type: 'SHOPIFY_EVENT', description: "Shopify Event"},
  {type: 'SYSTEM_EVENT', description: "System Event"},
  {type: 'MERCHANT_PORTAL_BULK_AUTOMATION', description: "Merchant Portal Bulk Automation"},
  {type: 'MERCHANT_EXTERNAL_API', description: "Merchant External Api"},
]

const ActivityLogFilter = ({filterVal, ...props}) => {
  const [createdDateRange, setCreatedDateRange] = useState([null, null])
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loaded, setLoaded] = useState(false);
  const [status, setStatus] = useState(null);
  const [eventType, setEventType] = useState(null);
  const [entityType, setEntityType] = useState(null);
  const [eventSource, setEventSource] = useState(null);
  const [entityId, setEntityId] = useState("");

  const toggle = () => setIsModalOpen(!isModalOpen);

  const applyCallback = (date) => {
    let tempDateRange = [null, null];
    if (date) {
      tempDateRange = date;
    }
    setCreatedDateRange(tempDateRange);
  }

  const onApply = () => {
    if (props.onApply) {
      props.onApply({
        fromCreatedDate: createdDateRange[0] ? createdDateRange[0] : undefined,
        toCreatedDate: createdDateRange[1] ? createdDateRange[1] : undefined,
        status: status || undefined,
        eventType: eventType || undefined,
        entityType: entityType || undefined,
        eventSource: eventSource || undefined,
        entityId: entityId || undefined,
      })
    }
    toggle();
  }
  const onReset = () => {
    setStatus(null);
    setEntityType(null);
    setEventType(null);
    setEventSource(null);
    setCreatedDateRange([null, null]);
    setEntityId("");

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
      if (filterVal.status) {
        setStatus(filterVal.status);
      }
      if (filterVal.eventType) {
        setEventType(filterVal.eventType);
      }
      if (filterVal.entityType) {
        setEntityType(filterVal.entityType);
      }
      if (filterVal.eventSource) {
        setEventSource(filterVal.eventSource);
      }
      if (filterVal.entityId) {
        setEntityId(filterVal.entityId);
      }
    }
    setCreatedDateRange(tempCreatedDateRange);
    setLoaded(true);
    return () => {
    }
  }, [filterVal])

  return (
    <Fragment>
      <Button className="btn-lg" color="none" onClick={toggle} style={{border: "1px solid #ced4da"}}>
        Filters <FontAwesomeIcon icon={faFilter}/>
      </Button>
      <Modal isOpen={isModalOpen} toggle={toggle} size={"lg"}>
        <ModalHeader toggle={toggle}/>
        <ModalBody>

          <Row>
            <Col xs={12} sm={12} md={6} lg={6}>
              <Row className="mt-3">
                <Col sm="4">
                  <label className="font-weight-bold">Event Type</label>
                </Col>
                <Col sm="8">
                  <div>
                    <Input
                      type="select"
                      value={eventType}
                      onChange={(e) => setEventType(e.target.value)}>
                      <option value="">All Event Type</option>
                      {
                        ActivityLogEventType.map((data, index) => <option
                          key={index} value={data.type}>{data.description}</option>)
                      }
                    </Input>
                  </div>
                  &nbsp; &nbsp;
                </Col>
              </Row>
              <Row className="mt-3">
                <Col sm="4">
                  <label className="font-weight-bold">Status</label>
                </Col>
                <Col sm="8">
                  <div>
                    <Input
                      type="select"
                      value={status}
                      onChange={(e) => setStatus(e.target.value)}>
                      <option value="">All Status</option>
                      {
                        activityLogStatus.map((data, index) => <option
                          key={index} value={data.type}>{data.description}</option>)
                      }
                    </Input>
                  </div>
                  &nbsp; &nbsp;
                </Col>
              </Row>
            </Col>
            <Col xs={12} sm={12} md={6} lg={6}>
              <Row className="mt-3">
                <Col sm="4">
                  <label className="font-weight-bold">Entity Type</label>
                </Col>
                <Col sm="8">
                  <div>
                    <Input
                      type="select"
                      value={entityType}
                      onChange={(e) => setEntityType(e.target.value)}>
                      <option value="">All Entity Type</option>
                      {
                        activityLogEntityType.map((data, index) => <option
                          key={index} value={data.type}>{data.description}</option>)
                      }
                    </Input>
                  </div>
                  &nbsp; &nbsp;
                </Col>
              </Row>
              <Row className="mt-3">
                <Col sm="4">
                  <label className="font-weight-bold">Event Source Type</label>
                </Col>
                <Col sm="8">
                  <div>
                    <Input
                      type="select"
                      value={eventSource}
                      onChange={(e) => setEventSource(e.target.value)}>
                      <option value="">All Event Source</option>
                      {
                        activityLogEventSource.map((data, index) => <option
                          key={index} value={data.type}>{data.description}</option>)
                      }
                    </Input>
                  </div>
                  &nbsp; &nbsp;
                </Col>
              </Row>
            </Col>
            <Col xs={12} sm={12} md={6} lg={6}>
              <Row className="mt-3">
                <Col sm="4">
                  <label className="font-weight-bold">Event Id</label>
                </Col>
                <Col sm="8">
                  <Input type="text" value={entityId} placeholder="Enter Subscription/Event ID" onChange={(e) => setEntityId(e.target.value)}/>
                </Col>
              </Row>
            </Col>
            <Col xs={12} sm={12} md={6} lg={6}>
              <Row className="mt-3">
                <Col sm="3">
                  <label className="font-weight-bold">Created Date</label>
                </Col>
                <Col sm="9">
                  <DateRangePicker
                    className="form-control"
                    onChange={applyCallback}
                    //format='dd/MM/y'
                    value={createdDateRange}
                  />
                </Col>
              </Row>
            </Col>
          </Row>

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
  );
};

export default ActivityLogFilter;
