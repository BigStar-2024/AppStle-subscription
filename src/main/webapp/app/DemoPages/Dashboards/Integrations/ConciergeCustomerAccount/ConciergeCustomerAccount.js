import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class ConciergeCustomerAccount extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Concierge Customer Accounts</ModalHeader>
        <ModalBody>
          <h6>Customer account page, Wishlist, Social Login</h6>
          <br/>
          <p> Customer account page quite often does not receive as much attention as other pages even by
             the biggest stores. As a result, the account page stays boring and virtually useless. 
             But why should that be the case? Why not give your customers a more sophisticated place to interact with you. Well, now you can do that easily with Concierge Customer Accounts by Froonze.</p>
          {/* <p> Let us know if your Transcy is not working properly with our Appstle widget.</p> */}
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
)(ConciergeCustomerAccount);
