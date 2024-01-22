import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

type MultiVariantsProps = {
  isOpen: boolean;
  onClose: () => void;
};

const MultiVariants = ({ isOpen, onClose }: MultiVariantsProps) => (
  <Modal isOpen={isOpen} size="lg">
    <ModalHeader>MultiVariants ‑ Bulk Order</ModalHeader>
    <ModalBody>
      <h6 className="mb-4">List variants in a beautiful table/grid layout. Let customers bulk order multiple variants easily.</h6>
      <p>
        Easy bundle order form with mix and match variants bulk add to cart. Let customers mix & match variants (ex.Red-S[12], Red-L[24]),
        build a bundle, and place orders with inventory tracking. Variant table display B2C/B2B/wholesale ordering process is simple for
        multiple variant products. Easy Order Process &gt; Increase Sales. Minimum/maximum order quantity limit per variant or combination
        of variants. Restrict min/max number of variants per product.{' '}
      </p>
      <div className="text-center mt-3">
        <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/multivariants">
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

export default MultiVariants;
