import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class Transcy extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Transcy</ModalHeader>
        <ModalBody>
          <h6>About Transcy: Language Translate & Currency</h6>
          <br/>
          <p> Transcy was born to automate translating your Shopify store content into multi-languages, display price in visitor's currency.
          By showing visitors that you value their culture and region is a very effective way to increase satisfaction and conversion rates.
          Appstle Subscriptions is by default integrated with Transcy.</p>
          <p> Let us know if your Transcy is not working properly with our Appstle widget.</p>
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
)(Transcy);
