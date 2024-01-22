import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function MageNative(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Mobile App Builder ‑ MageNative</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Easy Bundles: Upsell bundle products & cross sell, gift box, build a box.</h6>
        <br />
        <p>
            We empower Shopify merchants to develop android and iOS apps for their web stores and scale business goals exponentially. Create an app for your Shopify store with features like easy drag and drop, push notifications, app only discounts, WhatsApp and messenger support, barcode scanner for inventory management, augmented reality, with 18+ integrations and much more as per your needs. Join us and drive success for your business.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/magenative-app">
            Visit Now
          </EmbeddedExternalLink>
        </div>
      </ModalBody>
      <ModalFooter>
        <Button className=" mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
          Cancel
        </Button>
      </ModalFooter>
    </Modal>
  );
}

export default MageNative;
