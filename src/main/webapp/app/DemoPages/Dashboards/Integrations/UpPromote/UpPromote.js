import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class UpPromote extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with UpPromote</ModalHeader>
        <ModalBody>
          <h6>About UpPromote: Affiliate Marketing</h6>
          <br/>
          <p> UpPromote is an automated all-in-one affiliate and influencer marketing solution you ever need.
          You can build a professional affiliate campaign and easily approach potential affiliates with Uppromote.
          Appstle Subscriptions is by default integrated with UpPromote.</p>
          <p> Let us know if your UpPromote is not working properly with our Appstle widget.</p>
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UpPromote);
