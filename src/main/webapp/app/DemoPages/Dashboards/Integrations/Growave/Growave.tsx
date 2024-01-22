import React from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export function Growave(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Integrate with Growave</ModalHeader>
      <FeatureAccessCheck hasAnyAuthorities={'accessGrowaveIntegration'} upgradeButtonText="Upgrade your plan">
        <ModalBody>
          <h6>About Growave</h6>
          <br />
          <p>
            Growave is a versatile, feature-rich app that helps to increase repeat purchases and retention with loyalty program, VIP tiers,
            and rewards. Use Growave to acquire more customers organically through referral program and save on ad spend, implement
            wishlists and trigger emails to boost sales, and collect photo & product reviews on autopilot. Growave integrates seamlessly
            with Appstle Subscriptions to help you grow your business.
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

export default connect(mapStateToProps, mapDispatchToProps)(Growave);
