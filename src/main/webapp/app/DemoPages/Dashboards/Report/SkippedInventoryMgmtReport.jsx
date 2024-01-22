import React, { Component, Fragment, useEffect, useState } from 'react';
import {
  Row,
  Col,
  Card,
  CardBody,
  CardHeader,
  Collapse,
  Input,
  Table,
  Button,
  Label,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  FormFeedback
} from 'reactstrap';
import { getSkippedInventoryMgmtOrderEntityForReport, updateAttemptBillingEntity } from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import Pagination from 'react-js-pagination';
import FilterAction from './FilterAction';
import BlockUi from '@availity/block-ui';
import Loader from 'react-loaders';
import { JhiItemCount, JhiPagination } from 'react-jhipster';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import SweetAlert from 'sweetalert-react';
import axios from 'axios';
import { connect, useSelector } from 'react-redux';
import { Link } from "react-router-dom";
import moment from 'moment';
import { aryIannaTimeZones } from 'app/shared/util/customer-utils';
import { convertToShopTimeZoneDate } from '../Shared/SuportedShopifyTImeZone';
import { toast } from 'react-toastify';
var momentTZ = require('moment-timezone');

const SkippedInventoryMgmtReport = ({ getSkippedInventoryMgmtOrderEntityForReport, loading, shopInfo, skipOrderEntities, updateAttemptBillingEntity, ...props }) => {
  let initFilter = FilterAction.getFilterObject();

  const [activePage, setActivePage] = useState(1);
  const [filterVal, setFilterVal] = useState(initFilter);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [totalRowData, setTotalRowData] = useState(0);
  const { totalItems } = useSelector(state => state.subscriptionBillingAttempt);
  const [hasData, setHasData] = useState(false);
  let [isModalOpen, setIsModalOpen] = useState(false);
  let [emailValidity, setEmailValidity] = useState(true);
  let [emailSendingProgress, setEmailSendingProgress] = useState(false);
  let [blurred, setBlurred] = useState(false);
  let [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  let [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  let [emailFailAlert, setEmailFailAlert] = useState(false);

  const handleGridData = () => {
    getSkippedInventoryMgmtOrderEntityForReport(activePage - 1, itemsPerPage, filterVal, 'SKIPPED_INVENTORY_MGMT');
  };
  useEffect(() => {
    handleGridData();
  }, [activePage, filterVal]);

  useEffect(() => {
    setTotalRowData(totalItems);
    if (totalItems > 0) {
      setHasData(true);
    }
  }, [skipOrderEntities]);

  const handlePagination = activePage => {
    setActivePage(activePage);
  };

  const hasFilter = () => {
    return _.size(filterVal) > 0 || hasData;
  };

  const cleanupBeforeModalClose = () => {
    setEmailSendingProgress(false);
    setEmailValidity(true);
    setIsModalOpen(!isModalOpen);
    setBlurred(false);
    setInputValueForTestEmailId('');
  };

  const checkEmailValidity = emailId => {
    if (
      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
        emailId
      )
    ) {
      setEmailValidity(true);
      return true;
    } else {
      setEmailValidity(false);
      return false;
    }
  };

  const sendNextOrderMail = emailId => {
    if (checkEmailValidity(emailId)) {
      setEmailSendingProgress(true);
      axios
        .get(`api/subscription-billing-attempts/past-orders/export`, {
          params: {
            emailId: emailId,
            status: 'SKIPPED_INVENTORY_MGMT'
          }
        })
        .then(response => {
          cleanupBeforeModalClose();
          setEmailSuccessAlert(true);
        })
        .catch(error => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
        });
    }
  };

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  const getSellingPlanName = contractDetailsJSON => {
    return contractDetailsJSON && contractDetailsJSON.length > 0
    ? contractDetailsJSON[0]?.sellingPlanName
    : null;
  }

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
};

  const updateAttemptBilling = (id, shopName, contractId) => {
    updateAttemptBillingEntity(id, shopName, contractId).then(res => {
     toast?.success("Attempt Billing triggred.", options);
    }).catch(err => {
       toast?.error("Attempt Billing trigger failed.", options);
     });
 };

  return (
    <CardBody>
      {loading && !hasFilter() ? (
        <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
          <Loader type="line-scale" />
        </div>
      ) : skipOrderEntities && (skipOrderEntities?.length > 0 || hasFilter()) ? (
        <Fragment>
          <Card className="main-card">
            <CardBody>
              {skipOrderEntities && skipOrderEntities?.length > 0 && (
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                  <Button color="primary" className="ladda-button undefined btn btn-shadow " onClick={() => setIsModalOpen(!isModalOpen)}>
                    <span>Export</span>
                  </Button>
                </div>
              )}
              <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
                <ModalHeader>Export Skipped Order List</ModalHeader>
                <ModalBody>
                  <Label>Email id</Label>
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
                    placeholder="Please enter email id here"
                  />
                  <FormFeedback>Please Enter a valid email id</FormFeedback>
                  <br />
                </ModalBody>
                <ModalFooter>
                  <Button
                    color="secondary"
                    onClick={() => {
                      cleanupBeforeModalClose();
                    }}
                  >
                    Cancel
                  </Button>
                  <MySaveButton
                    onClick={() => {
                      sendNextOrderMail(inputValueForTestEmailId);
                    }}
                    text="Send Email"
                    updating={emailSendingProgress}
                    updatingText={'Sending'}
                  />
                </ModalFooter>
              </Modal>
              <SweetAlert
                title="Export Request Submitted"
                confirmButtonColor=""
                show={emailSuccessAlert}
                text="Export may take time based on the number of subscriptions in your store. Rest assured, once it's processed, it will be emailed to you."
                type="success"
                onConfirm={() => setEmailSuccessAlert(false)}
              />
              <SweetAlert
                title="Failed"
                confirmButtonColor=""
                show={emailFailAlert}
                text="Please try again."
                type="error"
                onConfirm={() => setEmailFailAlert(false)}
              />
              <BlockUi tag="div" blocking={loading} loader={<Loader active type="line-scale" />}>
                <div>
                  <Table className="mb-0 mt-4" hover>
                    <thead>
                      <tr>
                        <th>Contract Id</th>
                        <th>Billing Date</th>
                        <th>Customer</th>
                        <th>Selling Plan</th>
                        <th>Status</th>
                        <th>Retry</th>
                      </tr>
                    </thead>
                    <tbody>
                      {skipOrderEntities?.map(item => {
                        return (
                          <tr key={item?.subscriptionBillingAttempt?.id}>
                            <td><Link to={`/dashboards/subscription/${item?.subscriptionBillingAttempt?.contractId}/detail`}> #{item?.subscriptionBillingAttempt?.contractId}</Link></td>
                            <td>
                              {convertToShopTimeZoneDate(item?.subscriptionBillingAttempt?.billingDate, shopInfo.ianaTimeZone)}{' '}
                            </td>
                            <td>
                              <a style={{color: "#545cd8", cursor: "pointer"}} onClick={() =>{window.top.location.href = `https://${shopInfo.shop}/admin/customers/${item?.subscriptionContractDetails?.customerId}`}} target={'_blank'}>{item?.subscriptionContractDetails?.customerName}</a> <br></br>{item?.subscriptionContractDetails?.customerEmail}
                            </td>
                            <td>
                              {getSellingPlanName(JSON.parse(item?.subscriptionContractDetails?.contractDetailsJSON))}
                            </td>
                            <td>
                              <div className="badge badge-pill badge-secondary"> {item?.subscriptionBillingAttempt?.status}</div>
                            </td>
                            <td>
                              {
                            <MySaveButton
                                onClick={() => updateAttemptBilling(item?.subscriptionBillingAttempt?.id, item?.subscriptionBillingAttempt?.shop, item?.subscriptionBillingAttempt?.contractId)}
                                text="Retry Billing"
                                addBuffer={false}
                                updating={props.updatingBillingAttempt}
                                updatingText={"Processing.."}
                                className="btn-small btn-primary"
                                >
                                Retry Billing
                              </MySaveButton>}
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </Table>
                  {skipOrderEntities?.length == 0 && <div className="text-center m-3">No data available</div>}
                  <Row style={{ textAlign: 'center' }}>
                    <Col md={12}>
                      <br />
                      <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <Pagination
                          activePage={activePage}
                          itemsCountPerPage={itemsPerPage}
                          totalItemsCount={totalRowData}
                          // pageRangeDisplayed={5}
                          onChange={handlePagination}
                        />
                      </div>
                      <JhiItemCount page={activePage} total={totalRowData} itemsPerPage={itemsPerPage} />
                    </Col>
                  </Row>
                </div>
              </BlockUi>
            </CardBody>
          </Card>
        </Fragment>
      ) : (
        <></>
      )}
    </CardBody>
  );
};

const mapStateToProps = state => ({
  skipOrderEntities: state.subscriptionBillingAttempt.skipBillingAttemptInventoryMgmt,
  shopInfo: state.shopInfo.entity,
  updatingBillingAttempt: state.subscriptionBillingAttempt.updatingBillingAttempt
});

const mapDispatchToProps = {
  getSkippedInventoryMgmtOrderEntityForReport,
  updateAttemptBillingEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(SkippedInventoryMgmtReport);
