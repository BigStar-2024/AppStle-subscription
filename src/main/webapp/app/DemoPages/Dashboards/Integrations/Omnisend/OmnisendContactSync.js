import React, {Component} from 'react';
import {Field, Form} from 'react-final-form';
import {connect} from 'react-redux';
import {FormGroup, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, FormText} from 'reactstrap';
// import {getEntity, updateEntity} from 'app/entities/shop-info/shop-info.reducer';
import { updateEntity, createKlavioSampleTemplate} from '../../../../entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';
import {required} from "app/DemoPages/Dashboards/Utilities/FormValidators";
import Switch from 'react-switch';

export class OmnisendContactSync extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiKey: null,
      klaviyoTemplate: null
    };
  }

  componentWillReceiveProps(newProps) {

  }

  componentDidMount() {
  }

  saveEntity = values => {
    const {shopInfo} = this.props;
    const entity = {
      ...shopInfo,
      ...values
    };

    this.props.updateEntity(entity);
    this.props.onClose();
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
                <ModalHeader>Connect your Omnisend Account</ModalHeader>
                <ModalBody>
                  <h6>
                    Go to your Omnisend account section and create a new API key.
                  </h6>
                  <br/>
                  <img
                    style={{
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: '#021a40',
                      boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                    }}
                    width={620}
                    src={require('./omnisend_instruction.png')}
                  />
                  <br/>
                  <br/>

                  <FormGroup>

                    {/* <b className="d-block mb-3">Enable</b>

                    <Field
                      render={({input, meta}) => (
                        <div>
                          <Switch
                            checked={input.value}
                            onChange={input.onChange}
                            onColor="#86d3ff"
                            onHandleColor="#2693e6"
                            handleDiameter={30}
                            uncheckedIcon={false}
                            checkedIcon={false}
                            boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                            activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                            height={20}
                            width={48}
                            className="ml-3"
                            id="material-switch"
                          />
                          {meta.touched && meta.error && <span>{meta.error}</span>}
                        </div>
                      )}
                      name="yotpoEnabled"
                      validate={required}
                    /> */}


                     <Field
                      render={({input, meta}) => (
                        <div>
                          <Label size="lg">API Key</Label>
                          <Input placeholder="API Key" {...input} />
                          {meta.touched && meta.error && <span>{meta.error}</span>}
                        </div>
                      )}
                      name="omniSendApiKey"
                      validate={required}
                    />
                  <p><a href='https://api-docs.omnisend.com/v4.0/reference/overview' target='_blank'>Click here </a> to refer a omnisend document</p>
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
)(OmnisendContactSync);
