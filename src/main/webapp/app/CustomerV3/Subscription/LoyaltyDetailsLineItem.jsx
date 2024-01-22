import React, { useState, useEffect } from 'react';
import {getEntity} from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import axios from 'axios';
import { toast } from 'react-toastify';
// import './loader.scss';
import { formatPrice } from 'app/shared/util/customer-utils';
import Loader from "./Loader";


const LoyaltyDetailsLineItem = ({billingCycle, discount, discountType, freeTrail, currencyCode, freeVariantId, customerPortalSettingEntity, ...props}) => {
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
      return  (customerPortalSettingEntity?.freeTrialLoyaltyTextV2 || `Get {{discount}} {{discountType}} free trial.`).replace('{{discount}}', discount).replace('{{discountType}}', discountType)
    } else {
      if (discountType === "PERCENTAGE") {
        return  (customerPortalSettingEntity?.percentageLoyaltyTextV2 || `After {{billingCycle}} billing cycle, get {{discount}}% off.`).replace('{{discount}}', discount).replace('{{billingCycle}}', billingCycle)
      } else if (discountType === "SHIPPING") {
        return  (customerPortalSettingEntity?.shippingAmountLoyaltyTextV2 || `After {{billingCycle}} billing cycle, get shipping at {{discount}}.`).replace('{{discount}}', discount).replace('{{billingCycle}}', billingCycle)
      } else if (discountType === "FIXED") {
        return  (customerPortalSettingEntity?.amountOffLoyaltyTextV2 || `After {{billingCycle}} billing cycle, get {{discount}} off.`).replace('{{discount}}', discount).replace('{{billingCycle}}', billingCycle)
      } else if (discountType === "PRICE") {
        return  (customerPortalSettingEntity?.amountOffLoyaltyTextV2 || `After {{billingCycle}} billing cycle, get at {{discount}}.`).replace('{{discount}}', discount).replace('{{billingCycle}}', billingCycle)
      } else if (discountType === "FREE_PRODUCT") {
        return  (customerPortalSettingEntity?.freeProductLoyaltyTextV2 || `After {{billingCycle}} billing cycle, get FREE PRODUCT ({{productName}})`).replace('{{productName}}', productName).replace('{{billingCycle}}', billingCycle)
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
    <tr class="as-bg-white as-border-b">
      {fetchInProgress ? <td scope="row" class="as-px-6 as-py-4"><Loader /></td> :  <td scope="row" class="as-px-6 as-py-4" dangerouslySetInnerHTML={{__html: perkText}}></td>}
    </tr>
  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(LoyaltyDetailsLineItem);
