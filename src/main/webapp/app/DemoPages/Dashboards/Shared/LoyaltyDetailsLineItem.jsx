import React, { useState, useEffect } from 'react';
import {getEntity} from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import axios from 'axios';
import { toast } from 'react-toastify';
import './loader.scss';
import { Help } from "@mui/icons-material";
import { Table, TableBody, TableCell, TableRow } from "@mui/material";
import { formatPrice } from 'app/shared/util/customer-utils';


const LoyaltyDetailsLineItem = ({billingCycle, discount, discountType, freeTrail, currencyCode, freeVariantId, ...props}) => {
  // const { contractId, lineId, shop, currentVariant } = props;
  const [perkText, setPerkText] = useState("")
  const [productName, setProductName] = useState("");
  const [fetchInProgress, setFetchInProgress] = useState(false);

  useEffect(() => {
    setPerkText(getPerkText(getBillingCycleText(billingCycle), discount, discountType, freeTrail));
}, [billingCycle, discount, discountType, freeTrail, productName]);

useEffect(() => {
  if(freeVariantId) {
    setFetchInProgress(true)
    axios.get(`/api/data/variant?variantId=${freeVariantId}`)
    .then((res) => {
      let variantTitle = res?.data?.title;
      axios.get(`/api/data/product?productId=${res?.data?.["product_id"]}`)
      .then(resp => {
        setProductName(`${resp?.data?.title}${(variantTitle !== "-" && variantTitle !== "Default Title") ? ` - ${variantTitle}` : ""}`)
        setFetchInProgress(false);
      })
    })
  }
}, [freeVariantId])

  function getPerkText(billingCycle, discount, discountType, freeTrail) {
    if (freeTrail) {
      return  `Get <span class="appstle-loyalty-free-trial-discount">${discount} <span class="appstle-loyalty-free-trial-discount-count" style="text-transform: lowercase;">${discountType}${discount > 1 ? 's' : ''}</span></span> <span class="appstle-loyalty-free-trial-text">free trial.</span>`
    } else {
      if (discountType === "PERCENTAGE") {
        return  `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">${discount + "% off"}</span></span>.`
      } else if (discountType === "SHIPPING") {
        return  `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">${`shipping at ${discount} ${currencyCode}`}</span></span>.`
      } else if (discountType === "FIXED") {
      return  `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">${discount + " " + currencyCode + " off"}</span></span>.`
    } else if (discountType === "PRICE") {
      return  `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get at <span class="appstle-loyalty-discount-amount">${discount + " " + currencyCode}</span></span>.`
    } else if (discountType === "FREE_PRODUCT") {
      return  `After <span class="appstle-loyalty-billing-cycle"><span class="appstle-loyalty-billing-cycle-count">${billingCycle}</span> billing cycle</span>, <span class="appstle-loyalty-discount">get <span class="appstle-loyalty-discount-amount">FREE PRODUCT (${productName})</span></span>.`
    }
    }
  }

  function getBillingCycleText(billingCycle) {
    if (parseInt(billingCycle)) {
      var j = billingCycle % 10,
      k = billingCycle % 100;
      if (j == 1 && k != 11) {
          return billingCycle + `<sup>st</sup>`;
      }
      if (j == 2 && k != 12) {
          return billingCycle + `<sup>nd</sup>`;
      }
      if (j == 3 && k != 13) {
          return billingCycle + `<sup>rd</sup>`;
      }
      return billingCycle + `<sup>th</sup>`;
    }
    return billingCycle;
  }




  return (
      <TableRow>
        {fetchInProgress ? <TableCell> <div className="appstle_loadersmall" /> </TableCell>: <TableCell dangerouslySetInnerHTML={{__html: perkText}}></TableCell>}
      </TableRow>
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(LoyaltyDetailsLineItem);
