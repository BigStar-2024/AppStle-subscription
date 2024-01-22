import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Redirect} from '@shopify/app-bridge/actions';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';

export class FlitsAccountsHub extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Flits: Customer Account Page</ModalHeader>
        <ModalBody>
          <h6>About Flits: Customer Account Page</h6>
          <br/>
          <p>
            Flits is the first Shopify app that allows merchants to enhance their existing customer account page to an engaging and comprehensive account page. The account page offers features like Profile, Order history, Delivery addresses, Re-order, Recently viewed items, Contact us button (within order history).
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Close
          </Button>
          {<MySaveButton
            onClick={() => {
              let appUrl = "https://apps.shopify.com/flits";
              if (window.app) {
                Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
                  url: appUrl,
                  newContext: true,
                });
              } else {
                window.open(appUrl, '_blank');
              }
              this.props.onClose();
            }}
            text="Visit Now"
          />}
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FlitsAccountsHub);
