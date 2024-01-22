import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class OneClickUpsell extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with One Click Upsell ‑ Zipify OCU</ModalHeader>
        <ModalBody>
          <h6>About One Click Upsell</h6>
          <br/>
          <p>
          Increase your revenue and boost average order value overnight — without a designer or developer! No need for your customers to go back through Checkout or re-enter any information.
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
)(OneClickUpsell);
