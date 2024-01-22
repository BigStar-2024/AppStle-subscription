import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import MySaveButton from '../../Utilities/MySaveButton';

export class AffiliateMarket extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Affiliate Marketing</ModalHeader>
        <ModalBody>
          <h6>About Affiliate Marketing & Referral.</h6>
          <br/>
          <p>
            Affiliatery is an all-in-one complete affiliate and referral marketing tool for your Shopify store. With
            Affiliatery you can start your affiliate program just a click away.
            Allow social media influencers to become your referral partners and generate revenue for you. Appstle
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
)(AffiliateMarket);
