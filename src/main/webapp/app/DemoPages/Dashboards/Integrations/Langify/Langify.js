import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class Langify extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Langify</ModalHeader>
        <ModalBody>
          <h6>About Langify</h6>
          <br/>
          <p> Translating your shopify store has never been this easy. After you have installed langify you can start
          translating your content into further languages without adding a single line of code.
          Appstle Subscriptions is by default integrated with Langify.</p>
          <p> Let us know if your Langify is not working properly with our Appstle widget.</p>
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
)(Langify);
