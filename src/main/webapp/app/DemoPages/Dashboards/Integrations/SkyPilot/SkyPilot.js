import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

export class SkyPilot extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Sky Pilot</ModalHeader>
        <ModalBody>
          <h6>About Sky Pilot </h6>
          <br />
          <p>
            Watch your digital product sales take flight when you sell and deliver music, videos, books, or any digital files instantly and
            without limits. Deliver automatically and give your customers immediate access to the content they have purchased. Benefit from
            an entirely customizable, on-brand delivery experience. Give your customers entirely frictionless experience when you house and
            deliver digital downloads right within your store. Protect your files with enterprise-grade security.
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Close
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(SkyPilot);
