import React from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Redirect} from '@shopify/app-bridge/actions';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import {IntercomAPI} from 'react-intercom';

export const Ecomposer = props => {
    return (
      <Modal isOpen={props.isOpen} size="lg">
        <ModalHeader>Integrate with E-Composer</ModalHeader>
        <ModalBody>
          <h6>About E-Composer</h6>
          <br/>
          <p>
            EComposer helps merchants create any page types & sections, fast and simply, using a live drag & drop editor. Support: Landing Pages, Home Pages, Product Pages, Collection Pages, Blog Pages, Standard Pages (About Us, Contact, FAQs, Password page/Coming soon, Sales funnel page, Career page, Troubleshoot, Review page, Flashsale, Wholesale, Lead page, Vendor page, Member page, etc.) and Sections/Global Block, Footer Builder.
            If you need help in integrating please contact our 24/7/365 support team <a href="javascript:void(0)" onClick={() => {
                           IntercomAPI('showNewMessage', "");}}>here</a>.
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
            Close
          </Button>
          {<MySaveButton
            onClick={() => {
              let appUrl = "https://apps.shopify.com/ecomposer";
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
)(Ecomposer);
