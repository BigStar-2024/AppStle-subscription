import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import MySaveButton from '../../Utilities/MySaveButton';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck'

export class Zapier extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with Zapier</ModalHeader>
        <FeatureAccessCheck hasAnyAuthorities={'accessZapierIntegration'} upgradeButtonText="Upgrade your plan">
          <ModalBody>
            <h6>Automate your tasks with this powerful integration with Zapier.</h6>
            <br />
            <MySaveButton
              className="d-block mx-auto"
              onClick={() => {
                window.open('https://zapier.com/developer/public-invite/139151/ec74fdad391ade30ac1f3c91fffbc518/', '_blank');
                // this.saveEntity(values);
                this.props.onClose();
              }}
              updating={this.props.updating}
              text="Enable Zapier"
            />
          </ModalBody>
        </FeatureAccessCheck>
        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Cancel
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
)(Zapier);
