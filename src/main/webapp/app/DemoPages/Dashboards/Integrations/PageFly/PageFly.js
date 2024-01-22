import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import MySaveButton from '../../Utilities/MySaveButton';

export class PageFly extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Modal isOpen={this.props.isOpen} size="lg">
        <ModalHeader>Integrate with PageFly</ModalHeader>
        <ModalBody>
          <h6>PageFly Landing Page Builder</h6>
          <p>Craft sales-boosting pages, no code needed. Enjoy unwavering page speed - your success, simplified.</p>
          <p>
          To integrate with PageFly, {' '}
            <a href="https://pagef.ly/1up9nv" target="blank">
              click here
            </a>{' '}
            to see PageFly's help article.
          </p>
        </ModalBody>
        <ModalFooter>
          <Button className="mb-2 mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
            Cancel
          </Button>
          <Button
            className="mb-2 mr-2 btn btn-wide btn-shadow"
            onClick={() => {
              Intercom('showNewMessage', 'I need some help with PageFly integration for my shop. Can you please help?');
              this.props.onClose();
            }}
          >
            Need Help?
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(PageFly);
