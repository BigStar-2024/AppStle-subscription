import React from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Redirect} from '@shopify/app-bridge/actions';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';

export const DiscountedPricing = props => {
    return (
      <Modal isOpen={props.isOpen} size="lg">
        <ModalHeader>Integrate with Discounted Pricing</ModalHeader>
        <ModalBody>
          <h6>About Discounted Pricing</h6>
          <br/>
          <p>
            Use Discounted Pricing to allow customers to buy in bulk and be incentivized to add more items to their cart. Easily set up different price tiers and start making offering volume discounts. Volume discounts are fully customizable and unlike other apps we’re optimized for all desktop, mobile and tablet devices; helping you reach even more customers. Volume discounts are fully customizable and take less than a minute to create. Get started in minutes with our no-code discounted pricing editor.
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
            Close
          </Button>
          {<MySaveButton
            onClick={() => {
              let appUrl = "https://apps.shopify.com/discounted-pricing";
              if (window.app) {
                Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
                  url: appUrl,
                  newContext: true,
                });
              } else {
                window.open(appUrl, '_blank');
              }
              props.onClose();
            }}
            text="Visit Now"
          />}
        </ModalFooter>
      </Modal>
    );
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscountedPricing);
