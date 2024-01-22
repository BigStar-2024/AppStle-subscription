import React, { useState, useEffect } from 'react';
import {getEntity} from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import Axios from 'axios';
import { toast } from 'react-toastify';
import './loader.scss';
import { Table, TableBody, TableCell, TableRow } from "@mui/material";
import { formatPrice } from 'app/shared/util/customer-utils';
import LoyaltyDetailsLineItem from "./LoyaltyDetailsLineItem";


const LoyaltyDetails = ({sellingPlan, sellingPlanData, currencyCode, text, ...props}) => {
  // const { contractId, lineId, shop, currentVariant } = props;

  const [modalShow, setModalShow] = useState(false);
  const [loyaltyTableData, setLoyaltyTableData] = useState(null)

  const toggleModal = () => {
    setModalShow(!modalShow);
  };

  useEffect(() => {
  if (sellingPlan && sellingPlanData) {
    createLoyaltyTableData(sellingPlan, sellingPlanData)
  }
}, [sellingPlan, sellingPlanData]);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };



  const createLoyaltyTableData = (sellingPlan, sellingPlanData) => {
    var tableData = [];
    var sellingPlanData = sellingPlanData?.find(function(item) {return item?.id === sellingPlan});
    var output = null;
    if (sellingPlanData?.freeTrialEnabled) {
      tableData.push(<LoyaltyDetailsLineItem billingCycle={0} discount={sellingPlanData.freeTrialCount} discountType={sellingPlanData.freeTrialInterval} freeTrail={sellingPlanData?.freeTrialEnabled} currencyCode={currencyCode} />)
    }
    if (sellingPlanData?.afterCycle2) {
      tableData.push(<LoyaltyDetailsLineItem billingCycle={sellingPlanData.afterCycle2} discount={sellingPlanData.discountOffer2} discountType={sellingPlanData.discountType2} freeTrail={false} currencyCode={currencyCode} />)
    }
    if (sellingPlanData?.appstleCycles?.length) {
        sellingPlanData?.appstleCycles?.forEach(function(cycle) {
        tableData.push(<LoyaltyDetailsLineItem billingCycle={cycle.afterCycle} discount={cycle.value} discountType={cycle.discountType} freeVariantId={cycle?.freeVariantId} freeTrail={false} currencyCode={currencyCode} />)
    })
    }
    setLoyaltyTableData(tableData)
  }




  return (
    <span className="ml-auto mr-3">
      {loyaltyTableData?.length ? <span
        onClick={toggleModal}
        style={{color: "#13b5ea", textDecoration: "underline", cursor: "pointer", fontSize: "14px", marginLeft: "8px"}}>
        { text || "Loyalty Details"}
      </span> : ""}
      <Modal isOpen={modalShow} toggle={toggleModal}>
        {/* <ModalHeader toggle={toggle} close={closeBtn}>{header}</ModalHeader> */}
        <ModalHeader toggle={toggleModal}>
           <div style={{fontSize: '19px', fontWeight: '500'}}>Loyalty Details</div>
        </ModalHeader>
        <ModalBody style={{padding: "0"}}>
          <Table width="100%">
            <TableBody>
              {
                loyaltyTableData
              }
            </TableBody>
          </Table>
        </ModalBody>
      </Modal>
    </span>
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(LoyaltyDetails);
