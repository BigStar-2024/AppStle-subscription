import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Redirect} from '@shopify/app-bridge/actions';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';

export class CustomerAccountsHub extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Customer Accounts Hub</ModalHeader>
        <ModalBody>
          <h6>About Customer Accounts Hub</h6>
          <br/>
          <p>
          Customer Accounts Hub app helps you create enhanced customer accounts pages. Customer Accounts Hub requires no-coding to install and comes with a 14 Day Free Trial - so give it a try today!
          </p>
          <p>
          Moreover, Appstle integrates it's customers management portal within the dynamic accounts pages by Customer Accounts Hub helping to create a dynamic experience for your users.
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Close
          </Button>
          {<MySaveButton
            onClick={() => {
              let appUrl = "https://apps.shopify.com/customerhub";
              if (window.app) {
                Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
                  url: appUrl,
                  newContext: true,
                });
              } else {
                window.open(appUrl, '_blank');
              }
              this.props.onClose();
            }}
            text="Visit Now"
          />}
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
)(CustomerAccountsHub);
