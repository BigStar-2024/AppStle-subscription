import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Modal, ModalBody, ModalHeader} from 'reactstrap';
import {updateEntity} from "app/entities/shop-info/shop-info.reducer";


const PaymentUpgradeModal = (props) => {
  const [paymentModal, setPaymentModal] = useState(true)

  const handleToggle = () => {
    setPaymentModal(!paymentModal);
    const entity = {...props.shopInfo}
    entity.onBoardingPaymentSeen = true;
    props.updateEntity(entity)
  }

  return (
    <Fragment>
      {
        !props.shopInfo?.onBoardingPaymentSeen && props.shopPaymentInfoEntity && props.shopPaymentInfoEntity.paymentSettings && !props.shopPaymentInfoEntity?.paymentSettings?.supportedDigitalWallets?.includes("SHOPIFY_PAY") &&
        <div>
          <Modal isOpen={paymentModal} toggle={handleToggle}>
            <ModalHeader toggle={handleToggle}>
              <h6><b>Important Note:</b></h6>
            </ModalHeader>
            <ModalBody>
              <p>
                Shopify currently requires all merchants looking to install any subscription app to use
                Shopify
                Payments, Stripe, Paypal express gateway, Apple Pay, Shop Pay or Authorized.Net only.
                Please&nbsp;
                <a href={`https://${props.account?.login}/admin/settings/payments`} target="blank">click
                  here</a> to
                update your payment gateway.
              </p>
              <p>
                You can see all qualifying criteria for Shopify subscriptions API here: <a target="_blank"
                                                                                           href="https://help.shopify.com/en/manual/products/subscriptions/setup#eligibility-requirements">Eligibility
                Criteria</a>
              </p>
            </ModalBody>
          </Modal>
        </div>
      }
    </Fragment>
  );
};

const mapStateToProp = state => ({
  account: state.authentication.account,
  shopPaymentInfoEntity: state.shopPaymentInfo.entities,
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  updateEntity
};

export default connect(mapStateToProp, mapDispatchToProps)(PaymentUpgradeModal);
