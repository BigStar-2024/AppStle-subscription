import React from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export function MonsterCart(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Integrate with Monster Cart</ModalHeader>
      <FeatureAccessCheck hasAnyAuthorities={'accessMonsterCartIntegration'} upgradeButtonText="Upgrade your plan">
        <ModalBody>
          <h6>About Monster Cart</h6>
          <br />
          <p>
            Monster Cart Upsell + Free Gifts helps boost revenue using a slide cart drawer with unlock offers and free gift rewards.
            Customize your cart to match your brand, and offer upsells and free gifts to increase your average order value. Monster Cart and
            Appstle Subscriptions integrate together to increase your revenue and customer lifetime value.
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

export default connect(mapStateToProps, mapDispatchToProps)(MonsterCart);
