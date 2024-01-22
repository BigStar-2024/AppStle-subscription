import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class SalesNotification extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with ToastiBar - Sales Popup App</ModalHeader>
        <ModalBody>
          <h6>ToastiBar - Sales Popup App
          </h6>
          <p>ToastiBar - Sales Popup is the best tool to boost your store's sales by displaying recent sales notifications to your store's visitors and encouraging them to purchase a product from your store. ToastiBar - Sales Popup will build trust by letting your store's visitors know what your customers were ordering.</p>
        </ModalBody>
        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Cancel
          </Button>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={() => {
            Intercom('showNewMessage', 'I need some help with ToastiBar - Sales Popup App integration for my shop. Can you please help?');
            this.props.onClose()
          }}>
            Need Help?
          </Button>
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
)(SalesNotification);
