import React, { Component } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Card, CardBody, Row, Col } from 'reactstrap';
import { updateEntity } from 'app/entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';

export class Mailchimp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiKey: null
    };
  }

  componentWillReceiveProps(newProps) {

  }

  componentDidMount() {
  }

  saveEntity = values => {
    const { shopInfo } = this.props;
    const entity = {
      ...shopInfo,
      ...values
    };

    this.props.updateEntity(entity);
    this.props.onClose();
  };

  render() {
    const { shopInfo } = this.props;
    const identity = value => value;
    return (
      <Form
        initialValues={shopInfo}
        onSubmit={this.saveEntity}
        render={({ handleSubmit, pristine, form, submitting, values }) => {
          return (
            <form onSubmit={handleSubmit}>
              <Modal isOpen={this.props.isOpen} size="lg">
                <ModalHeader>Connect your Mailchimp Account</ModalHeader>
                <ModalBody>
                  <h6>
                    Go to your Mailchimp{' '}
                    <a href="https://admin.mailchimp.com/account/api/" target="_blank">
                      API keys section
                    </a>{' '}
                    {"and create a new Mailchimp (Mandrill) API key."}
                  </h6>
                  <h6>Mailchimp integration works only if you're on a paid plan with Mailchimp.</h6>
                  <br />
                  <img
                    style={{
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: '#021a40',
                      boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                    }}
                    width={'100%'}
                    src={require('./mailchimp_instruction.png')}
                  />
                  <br />

                  <FormGroup className="mt-3">
                    <Field
                      render={({ input, meta }) => (
                        <div>
                          <Label size="lg">API Key</Label>
                          <Input placeholder="Your Mailchimp (Mandrill) API Key" {...input} />
                          {meta.touched && meta.error && <span>{meta.error}</span>}
                        </div>
                      )}
                      name="mailchimpApiKey"
                      parse={identity}
                    />
                  </FormGroup>
                  <br />
                </ModalBody>
                <ModalFooter>
                  <Button className="mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
                    Cancel
                  </Button>
                  <MySaveButton
                    onClick={() => {
                      this.saveEntity(values);
                    }}
                    updating={this.props.updating}
                  >
                  </MySaveButton>
                </ModalFooter>
              </Modal>
            </form>
          );
        }}
      />
    );
  }
}

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  updating: state.shopInfo.updating
});

const mapDispatchToProps = {
  updateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Mailchimp);
