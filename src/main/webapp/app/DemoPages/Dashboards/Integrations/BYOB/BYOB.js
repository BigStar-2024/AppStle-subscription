import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function BYOB(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>BYOB ‑ Build Your Own Bundles</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Let customers build their own bundles on a single page. Boost AOV. Sync with inventory.</h6>
        <br />
        <p>
          With BYOB, you can set up custom bundle pages to boost AOV. All bundles exist as products with our custom template. The template style can be customized in the theme editor. Your customers can mix and match products (combo) on a single page. We offer 2 order formats to sync with inventory systems and avoid discount conflicts! Try our sample bundles on the demo store now! p.s Manual installation of the template is required. Please contact us for assistance.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/byob-build-your-own-bundle">
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
export default BYOB;
