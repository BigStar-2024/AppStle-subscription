import React from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export function ReferralCandy(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Integrate with Referral Candy</ModalHeader>
      <FeatureAccessCheck hasAnyAuthorities={'accessReferralCandyIntegration'} upgradeButtonText="Upgrade your plan">
        <ModalBody>
          <h6>About Referral Candy</h6>
          <br />
          <p>
            Referral Candy helps Shopify and Shopify Plus stores attract new customers, increase sales and grow their brand with referral
            and affiliate programs, ideal for stores selling one-off and subscription products. A referral program is the best way to retain
            customers and reward them when they refer their friends. Appstle Subscriptions and Referral Candy work together to retain your
            customers.
          </p>
        </ModalBody>
      </FeatureAccessCheck>

      <ModalFooter>
        <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(ReferralCandy);
