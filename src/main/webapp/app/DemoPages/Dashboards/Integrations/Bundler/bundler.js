import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class Bundler extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Bundler</ModalHeader>
        <ModalBody>
          <h6>About Bundler ‑ Product Bundles</h6>
          <br/>
          <p>
          Bundler - Product Bundles will help you boost your sales and increase your average order value by applying discounts on products bought in bundles.
          Bundler is a market disruptor, as it offers bundling functionality to Shopify users for a great price! Appstle
            Subscriptions is by default integrated with Affiliatery.
          </p>
        </ModalBody>
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Bundler);
