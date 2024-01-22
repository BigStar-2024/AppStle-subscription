import React, {Component} from 'react';
import {Field, Form} from 'react-final-form';
import {connect} from 'react-redux';
import {FormGroup, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import { updateEntity } from 'app/entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';

export class AppstleLoyaltyIntegration extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiKey: null
    };
  }

  componentWillReceiveProps(newProps) {
    if (newProps.apiKey !== this.props.shopInfo.appstleLoyaltyApiKey) {
      this.setState({ apiKey: newProps.shopInfo.appstleLoyaltyApiKey });
    }
  }

  componentDidMount() {
  }

  saveEntity = values => {
    const {shopInfo} = this.props;
    const entity = {
      ...shopInfo,
      ...values,
      appstleLoyaltyApiKey: values.appstleLoyaltyApiKey?.trim() || null
    };

    this.props.updateEntity(entity);

    if (entity.appstleLoyaltyApiKey != null && entity.appstleLoyaltyApiKey !== '') {
      this.props.onClose();
    }
  };

  render() {
    const {shopInfo} = this.props;
    return (
      <Form
        initialValues={shopInfo}
        onSubmit={this.saveEntity}
        render={({handleSubmit, pristine, form, submitting, values}) => {
          return (
            <form onSubmit={handleSubmit}>
              <Modal isOpen={this.props.isOpen} size="lg">
                <ModalHeader>Connect your Appstle Loyalty Account</ModalHeader>
                <ModalBody>
                  <h6>
                    Go to your Appstle Loyalty App's API screen and find API key
                  </h6>
                  <br/>
                  <img
                    style={{
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: '#021a40',
                      boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                    }}
                    width={'100%'}
                    src={require('./appstleLoyalty_instructions.png')}
                  />
                  <br/>
                  <br/>

                  <FormGroup>
                     <Field
                      render={({input, meta}) => (
                        <div>
                          <Label size="lg">API Key</Label>
                          <Input placeholder="API Key" {...input} />
                          {meta.touched && meta.error && <span>{meta.error}</span>}
                        </div>
                      )}
                      name="appstleLoyaltyApiKey"
                    />
                  </FormGroup>
                </ModalBody>
                <ModalFooter>
                  <Button className=" mr-2 btn btn-wide btn-shadow" onClick={this.props.onClose}>
                    Cancel
                  </Button>
                  <MySaveButton
                    onClick={() => {
                      this.saveEntity(values);
                    }}
                    updating={this.props.updating}
                  />
                </ModalFooter>
              </Modal>
            </form>
          );
        }}
      />
    )
      ;
  }
}

const mapStateToProps = state => ({
    shopInfo: state.shopInfo.entity,
    updating: state.shopInfo.updating,
});

const mapDispatchToProps = {
  updateEntity,
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AppstleLoyaltyIntegration);
