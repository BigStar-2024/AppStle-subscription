import React from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function Debutify(props) {
  return (
    <Modal isOpen={props.isOpen} size="lg">
      <ModalHeader>Debutify Product Reviews & QA</ModalHeader>
      <ModalBody>
        <h6 className="mb-4">Debutify Reviews makes collecting, managing, and displaying customer photo and video reviews easy.</h6>
        <br />
        <p>
          Let your customers sell for you, and use social proof to build your brand's credibility. With Debutify Reviews, you can easily request, collect, and import photo and video reviews. Plus, you can manage them all in one place and display them beautifully in customizable widgets that match your brand. You also have full control over your reviews, so you can only show the best ones that can help visitors make better buying decisions while protecting your brand's reputation.
        </p>
        <div className="text-center mt-3">
          <EmbeddedExternalLink className="btn btn-primary" href="https://apps.shopify.com/debutify-reviews">
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

export default Debutify;
