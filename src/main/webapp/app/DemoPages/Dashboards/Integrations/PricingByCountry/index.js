import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class PricingByCountry extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Pricing By Country</ModalHeader>
        <ModalBody>
          <h6>About Pricing By Country & Currency</h6>
          <br/>
          <p> Manage higher and lower prices in different countries with our app. Saves expense & headache of creating new stores for different countries. </p>
          <p> Let us know if your pricing country is not working properly with our Appstle widget.</p>
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
)(PricingByCountry);
