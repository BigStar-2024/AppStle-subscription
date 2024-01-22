import React from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export function GemPages(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Integrate with GemPages Landing Page Builder</ModalHeader>
      <FeatureAccessCheck hasAnyAuthorities={'accessReferralCandyIntegration'} upgradeButtonText="Upgrade your plan">
        <ModalBody>
          <h6>About GemPages Landing Page Builder</h6>
          <br />
          <p>
            GemPages is a one-stop storefront customization solution with a visual editor and AI-powered features to help you build pages as
            you envision. GemPages and Appstle Subscriptions work together to help you create a seamless customer journey from the moment
            they land on your store to the moment they subscribe to your products.
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

export default connect(mapStateToProps, mapDispatchToProps)(GemPages);
