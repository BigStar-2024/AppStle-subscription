import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function EasyBundles(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Easy Bundles | Bundle Builder</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Easy Bundles: Upsell bundle products & cross sell, gift box, build a box.</h6>
        <br />
        <p>
          Easy Bundles is pre integrated Recurpay which means all you have to do is enable a toggle to start showing the product
          subscription options in the bundle summary after the customer builds their bundle. Do reach out to their excellent support to seek
          any assistance if required.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/bundle-builder">
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

export default EasyBundles;
