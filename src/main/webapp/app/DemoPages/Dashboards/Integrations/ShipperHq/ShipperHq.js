import React, { Component } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import { updateEntity} from '../../../../entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

export class ShipperHq extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiKey: null,
      authCode: null
    };
  }

  componentWillReceiveProps(newProps) {
    if (newProps.apiKey !== this.props.shopInfo.shipperHqApiKey) {
      this.setState({ apiKey: newProps.shopInfo.shipperHqApiKey });
    }

    if (newProps.authCode != this.props.shopInfo.shipperHqAuthCode) {
      this.setState({ authCode: newProps.shopInfo.shipperHqAuthCode });
    }
  }

  saveEntity = values => {
    console.log('saving');
    const { shopInfo } = this.props;
    const entity = {
      ...shopInfo,
      ...values,
      shipperHqApiKey: values.shipperHqApiKey?.trim() || null,
      shipperHqAuthCode: values.shipperHqAuthCode?.trim() || null
    };

    this.props.updateEntity(entity);
  };

  render() {
    const { shopInfo } = this.props;
    const identity = value => value;
    return (
      <Form
        initialValues={shopInfo}
        onSubmit={this.saveEntity}
        render={({ handleSubmit, values }) => {
          return (
            <form onSubmit={handleSubmit}>
              <Modal isOpen={this.props.isOpen} size="lg">
                <ModalHeader>Connect your ShipperHq Account</ModalHeader>
                  <ModalBody>
                  <p>
                    <ol>
                      <li>In ShipperHQ dashboard, under <b>BASIC SETUP</b> go to {<EmbeddedExternalLink href="https://shipperhq.com/ratesmgr/websites" target="_blank">
                        websites section. 
                      </EmbeddedExternalLink>}</li>
                      <li>Click on the website (that needs to be already set in ShipperHQ) which they want to integrate with Appstle Subscriptions app.</li>
                      <li>Scroll down to find <b>API Key</b></li>
                      <li>For <b>Authentication Code</b> just besides API Key field there will be a <b>Generate New Authentication Code</b> button. Click on that to get the Authentication Code.</li>
                      <li>Copy the API Key and Authentication Code, paste it in Appstle Subscriptions to enable the integration.</li>
                      </ol>

                    </p>
                  <br />
                  <img   
                    style={{
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: '#021a40',
                      boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                    }}
                    width={'100%'}
                    src={require('./shipperhq_instruction.png')}
                  />
                  <br />
                    <FormGroup className="mt-3">
                      <Field
                        render={({ input, meta }) => (
                          <div>
                            <Label size="lg">API Key</Label>
                            <Input placeholder="Your API Key" {...input} />
                            {meta.touched && meta.error && <span>{meta.error}</span>}
                          </div>
                        )}
                        name="shipperHqApiKey"
                        parse={identity}
                      />
                    </FormGroup>
                    <FormGroup className="mt-3">
                      <Field
                        render={({ input, meta }) => (
                          <div>
                            <Label size="lg">Auth Code</Label>
                            <Input placeholder="Your Authorization Code" {...input} />
                            {meta.touched && meta.error && <span>{meta.error}</span>}
                          </div>
                        )}
                        name="shipperHqAuthCode"
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
                    Save
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
)(ShipperHq);
