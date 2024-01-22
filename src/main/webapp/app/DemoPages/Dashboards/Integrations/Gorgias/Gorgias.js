import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export class Gorgias extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Gorgias</ModalHeader>
        <FeatureAccessCheck hasAnyAuthorities={'accessGorgiasIntegration'} upgradeButtonText="Upgrade your plan">
          <ModalBody>
            <h6>About Gorgias.</h6>
            <br />
            <p>
              Gorgias is a help desk (help center or customer support) for Ecommerce stores that allows your customer service team to manage
              all of your support, customer service in one place. Appstle Subscriptions is by default integrated with Gorgias as the orders
              are placed on Shopify and they can be refunded via Gorgias.
            </p>
          </ModalBody>
        </FeatureAccessCheck>

        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Close
          </Button>
          {/*<MySaveButton
            onClick={() => {
              window.open('https://apps.shopify.com/affiliatery', '_blank');
              this.props.onClose();
            }}
            text="Visit Now"
          />*/}
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(Gorgias);
