import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function MBCBundle(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>MBC Bundle Products</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Advanced Bundle Products App which help to Enhance Sales with multiple Upsell and Discount options</h6>
        <br />
        <p>
        MBC Bundle Products allow you to create visually appealing bundles that cater to your customers' preferences with advanced customization. Enhance your AOV and drive revenue effortlessly. Whether you want to offer Buy-One-Get-One deals, quantity discounts, build-your-own bundles, this user-friendly app has got you covered. It seamlessly integrates with your theme. Gain valuable insights through AI-driven recommendations and track your top-selling products.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/bundle-products-mbc?st_source=autocomplete">
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
export default MBCBundle;
