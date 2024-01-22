import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

type UpCartProps = {
  isOpen: boolean;
  onClose: () => void;
};

const UpCart = ({ isOpen, onClose }: UpCartProps) => (
  <Modal isOpen={isOpen} size="lg">
    <ModalHeader>UpCart—Cart Drawer Cart Upsell</ModalHeader>
    <ModalBody>
      <h6 className="mb-4">UpCart lets you create a custom, on-brand slide cart with upsells and add-ons to boost AOV.</h6>
      <p>
        UpCart is a full featured drawer cart builder that can automatically help you setup and customize an incredible cart experience. Our
        carts are designed to look and feel on-brand to the rest of your site. Keep your customers satisfied by providing an easy-to-use
        cart experience. Moreover, use UpCart to increase your average order value with our upselling and cross-selling module and add-ons
        module. Create a sticky cart button to boost conversion rates and reduce cart abandonment.
      </p>
      <div className="text-center mt-3">
        <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/upcart-cart-builder">
          Visit Now
        </EmbeddedExternalLink>
      </div>
    </ModalBody>
    <ModalFooter>
      <Button className=" mr-2 btn btn-wide btn-shadow" onClick={onClose}>
        Cancel
      </Button>
    </ModalFooter>
  </Modal>
);

export default UpCart;
