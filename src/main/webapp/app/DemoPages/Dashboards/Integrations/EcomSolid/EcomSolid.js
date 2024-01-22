import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';

export class EcomSolid extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with EcomSolid</ModalHeader>
        <ModalBody>
          <h6>EcomSolid Theme & Page Builder
          </h6>
          <p>Whether you are a seasoned business or just starting out, selling online can be daunting. Even before building your site, you need to buy a theme, choose integrations, and balance monthly fees... every decision can be an expensive one.</p>
          <p>But it doesn’t have to be! EcomSolid is a page builder with themes so you can design every aspect of your website, no code needed. With +100 custom blocks, hide header & footer, a huge portfolio of templates, and tons of sale-bosting add-ons, conversion-focused branded stores can be set up instantly all in one app.</p>
          <p>Appstle Subscriptions is by default integrated with EcomSolid.</p><br/>
          <p> Let us know if your EcomSolid is not working properly with our Appstle widget.</p>
        </ModalBody>
        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Cancel
          </Button>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={() => {
            Intercom('showNewMessage', 'I need some help with EcomSolid integration for my shop. Can you please help?');
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
)(EcomSolid);
