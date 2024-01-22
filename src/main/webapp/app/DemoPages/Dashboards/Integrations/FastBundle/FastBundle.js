import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function FastBundle(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Fast Bundle | Product Bundles</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Easily create almost all types of product bundles and combo products and boost your revenue</h6>
        <br />
        <p>
          You spend lots of money on marketing your business and bring new users to your shop. Bundling helps you make the most out of your customers. We help you to create different types of product bundles with different options of displaying them and increase your conversion rate and your average order value (AOV). The only thing you have to do is creating meaningful bundles and show them at the right place. FYI, we are open to create customized solutions for your business in this context.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/fast-bundle-product-bundles">
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
export default FastBundle;
