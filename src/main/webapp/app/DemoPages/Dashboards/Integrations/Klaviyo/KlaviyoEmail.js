import React, { Component } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Card, CardBody, Row, Col } from 'reactstrap';
import { updateEntity, createKlavioSampleTemplate} from '../../../../entities/shop-info/shop-info.reducer';
import YoutubeVideoPlayer from '../../Tutorials/YoutubeVideoPlayer';
import { required } from '../../Utilities/FormValidators';
import MySaveButton from '../../Utilities/MySaveButton';
import SavingDisclaimer from '../../Utilities/SavingDisclaimer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

export class KlaviyoEmail extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiKey: null,
      klaviyoTemplate: null
    };
  }

  componentWillReceiveProps(newProps) {
    if (newProps.apiKey !== this.props.shopInfo.klaviyoApiKey) {
      this.setState({ apiKey: newProps.shopInfo.klaviyoApiKey });
    }

    if (newProps.klaviyoTemplate) {
      this.setState({ klaviyoTemplate: newProps.klaviyoTemplate });
    }
  }

  componentDidMount() {
  }

  saveEntity = values => {
    console.log('saving');
    const { shopInfo } = this.props;
    const entity = {
      ...shopInfo,
      ...values,
      klaviyoApiKey: values.klaviyoApiKey?.trim() || null
    };

    this.props.updateEntity(entity);

    if (entity.klaviyoTemplateId != null && entity.klaviyoTemplateId !== '') {
      this.props.onClose();
    } else {
      // At this point, we should show a banner asking user to select template. Something like, "Please select a mailing template from the dropdown menu. If downdown is empty, then it means your API key is invalid."
    }
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
                <ModalHeader>Connect your Klaviyo Account</ModalHeader>
                <FeatureAccessCheck hasAnyAuthorities={'accessKlaviyoEmailIntegration'} upgradeButtonText="Upgrade your plan">
                  <ModalBody>
                    <h6>
                      Go to your Klaviyo{' '}
                      <a href="https://www.klaviyo.com/account#api-keys-tab" target="_blank">
                        account section
                      </a>{' '}
                      and create a new API key.
                    </h6>
                    <br />
                    <Row>
                      <Col md={7}>
                      <img
                      style={{
                        borderWidth: '1px',
                        borderStyle: 'solid',
                        borderColor: '#021a40',
                        boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                      }}
                      width={'100%'}
                      src={require('./klaviyo_instruction.png')}
                    />
                      </Col>
                      <Col md={5} style={{alignSelf: 'center'}}>
                      <Card style={{ marginLeft: 'auto', borderRadius: '18px'}} className="card-border card-hover-shadow-2x">
                            <CardBody className="top-elem">
                            {/* <div className='text-center pb-3'>Product Swap Automation - Product Cycle</div> */}
                              <div className="text-center">
                                <YoutubeVideoPlayer url={'https://youtu.be/pg_Q5MJxr7s'}/>
                              </div>
                            </CardBody>
                          </Card>
                      </Col>
                    </Row>


                    <br />

                    <FormGroup className="mt-3">
                      <Field
                        render={({ input, meta }) => (
                          <div>
                            <Label size="lg">Public API Key / Site ID</Label>
                            <Input placeholder="Your Public API Key / Site ID" {...input} />
                            {meta.touched && meta.error && <span>{meta.error}</span>}
                          </div>
                        )}
                        name="klaviyoPublicApiKey"
                        parse={identity}
                      />
                    </FormGroup>
                    <FormGroup className="mt-3">
                      <Field
                        render={({ input, meta }) => (
                          <div>
                            <Label size="lg">Private API Key</Label>
                            <Input placeholder="Your Private API Key" {...input} />
                            {meta.touched && meta.error && <span>{meta.error}</span>}
                          </div>
                        )}
                        name="klaviyoApiKey"
                        parse={identity}
                      />
                    </FormGroup>

                    <br />

                    {this.state.apiKey ? (
                      <div>

                        <FormGroup className="text-center">
                          <MySaveButton
                            className="btn-warning"
                            text="Create Sample Template"
                            onClick={() => {
                              this.props.createKlavioSampleTemplate()
                            }}
                            updating={this.props.updating}
                          />
                          <br />
                          {this.state.klaviyoTemplate && this.state.klaviyoTemplate.id  ? (
                            <div className="alert alert-success"><b>{this.state.klaviyoTemplate.name}</b> created in your Klaviyo account</div>
                          ) : null}


                        </FormGroup>

                        {/*<SavingDisclaimer
                          text={
                            <span>
                              Refer to{' '}
                              <a href="https://docs.appikon.com/en/articles/4717871-email-template-variables" target="_blank">
                                this link
                              </a>{' '}
                              to learn how to insert product, shop or variant data in the product template.
                            </span>
                          }
                        />*/}
                      </div>
                    ) : null}
                  </ModalBody>
                </FeatureAccessCheck>
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
  updating: state.shopInfo.updating,
  klaviyoTemplate: state.shopInfo.klaviyoTemplate
});

const mapDispatchToProps = {
  updateEntity,
  createKlavioSampleTemplate
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(KlaviyoEmail);
