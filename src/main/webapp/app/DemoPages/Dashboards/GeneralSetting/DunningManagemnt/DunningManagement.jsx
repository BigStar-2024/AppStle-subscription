import React, {Fragment, useEffect, useState} from 'react';
import Loader from 'react-loaders';
//import './setting.scss';
import {
  Card,
  CardBody,
  CardHeader,
  CardFooter,
  Col,
  Collapse,
  FormGroup,
  FormText,
  Input,
  Button,
  InputGroup,
  Label,
  Alert,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row
} from 'reactstrap';
import Switch from 'react-switch';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getEntity,
  reset,
  updateEntity
} from "app/entities/dunning-management/dunning-management.reducer";
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import YoutubeVideoPlayer from '../../Tutorials/YoutubeVideoPlayer';
import { completeChecklistItem } from 'app/entities/onboarding-info/onboarding-info.reducer';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';

const dunningManagement = ({dunningManagementEntity, getEntity, updateEntity, createEntity, completeChecklistItem, ...props}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);

  useEffect(() => {
    getEntity(0)
    completeChecklistItem(OnboardingChecklistStep.DUNNING_CANCELLATION)
  }, [])

  const saveEntity = values => {
      updateEntity(values);
  };

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  let submit;
  return (
    <FeatureAccessCheck
      hasAnyAuthorities={'enableDunningManagement'}
      upgradeButtonText="Upgrade to enable Dunning Management"
    >
       <Fragment>
        <ReactCSSTransitionGroup
          component="div"
          transitionName="TabsAnimation"
          transitionAppear
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}
        >
          <PageTitle
            heading="Dunning Management"
            subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions' target='_blank'> Click here to learn more about Dunning Management setup.</a>"
            icon="lnr-pencil icon-gradient bg-mean-fruit"
            actionTitle="Update"
            enablePageTitleAction
            onActionClick={() => {
              submit();
            }}
            formErrors={formErrors}
            errorsVisibilityToggle={errorsVisibilityToggle}
            onActionUpdating={props.updating}
            updatingText="Updating"
            sticky={true}
            tutorialButton={{
              show: true,
              videos: [
                {
                  title: "Dunning Management",
                  url: "https://www.youtube.com/watch?v=jlmCwVZzViQ",
                }
              ],
              docs: [
                {
                  title: "Dunning Management in Appstle Subscriptions",
                  url: "https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions"
                }
              ]
            }}
          />
          {/* <Alert className="mb-2" color="warning">
            <h6>Need assistance?</h6>
            <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
            <span>
              <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
              <a
                href='https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions'
                target='_blank'
                rel="noopener noreferrer"
                style={{marginLeft: '10px'}}
              >
                Help Documentation
              </a>
            </span>
          </Alert>
          <Modal isOpen={showModal} toggle={toggleShowModal}>
            <ModalHeader toggle={toggleShowModal}>Tutorial Videos</ModalHeader>
            <ModalBody>
              <div className="mt-4 border-bottom pb-4">
                <h6>Dunning Management</h6>
                <YoutubeVideoPlayer
                  url="https://www.youtube.com/watch?v=jlmCwVZzViQ"
                  iframeHeight="100%"
                  divClassName="video-container"
                  iframeClassName="responsive-iframe"
                />
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="link" onClick={toggleShowModal}>Cancel</Button>
            </ModalFooter>
          </Modal> */}
          <Form
            initialValues={dunningManagementEntity}
            onSubmit={saveEntity}
            render={({handleSubmit, form, submitting, pristine, values, errors}) => {
              submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
                if (Object.keys(errors).length) handleSubmit();
                setFormErrors(errors);
                setErrorsVisibilityToggle(!errorsVisibilityToggle);
              }
              return (
                <form onSubmit={handleSubmit}>
                    <Row>
                        <Col md={4}>
                          <h4>Welcome to Dunning Management</h4>
                           <p className='text-muted' >
                            Dunning Management is the pre-set mechanism set by you, the merchant, to address incidents of card expiration, or anything else that would result in involuntary churn of customers.
                            </p>
                            <hr></hr>

                            <p className='text-muted' >
                            With this section, Appstle facilitates you to set and modify the mechanisms such as how often and how many times you want the card to be retried before canceling the subscription or skipping the order.
                            </p>
                            <hr></hr>
                            <p className='text-muted' >
                            A strong dunning management will effectively reduce any involuntary churn, and build relationship with the customers.
                            </p>
                            <hr></hr>
                            <p  className='text-muted' >Read this <a href="https://intercom.help/appstle/en/articles/5060975-dunning-management-in-appstle-subscriptions" target="_blank">doc</a> to know more about dunning management</p>

                        </Col>
                        <Col md={8}>
                          <Card className="card-margin">
                            <CardBody>
                              <Row>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="retryAttempts">Retry Attempts</Label>
                                    <Field
                                      render={({ input, meta }) => (
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} >
                                            <option value="ONE_ATTEMPT">1 attempt</option>
                                            <option value="TWO_ATTEMPTS">2 attempts</option>
                                            <option value="THREE_ATTEMPTS">3 attempts</option>
                                            <option value="FOUR_ATTEMPTS">4 attempts</option>
                                            <option value="FIVE_ATTEMPTS">5 attempts</option>
                                            <option value="SIX_ATTEMPTS">6 attempts</option>
                                            <option value="SEVEN_ATTEMPTS">7 attempts</option>
                                            <option value="EIGHT_ATTEMPTS">8 attempts</option>
                                            <option value="NINE_ATTEMPTS">9 attempts</option>
                                            <option value="TEN_ATTEMPTS">10 attempts</option>
                                        </Input>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please Select Retry Attempts.' : undefined;
                                      }}
                                      type="select"
                                      className="form-control"
                                      name="retryAttempts"
                                    />
                                  </FormGroup>
                                </Col>
                                <Col xs={12} sm={12} md={6} lg={6}>
                                  <FormGroup>
                                    <Label for="daysBeforeRetrying">Day Before Retrying</Label>
                                    <Field
                                      render={({ input, meta }) => (
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} >
                                            <option value="ONE_DAY">1 Day</option>
                                            <option value="TWO_DAYS">2 Days</option>
                                            <option value="THREE_DAYS">3 Days</option>
                                            <option value="FOUR_DAYS">4 Days</option>
                                            <option value="FIVE_DAYS">5 Days</option>
                                            <option value="SIX_DAYS">6 Days</option>
                                            <option value="SEVEN_DAYS">7 Days</option>
                                            <option value="EIGHT_DAYS">8 Days</option>
                                            <option value="NINE_DAYS">9 Days</option>
                                            <option value="TEN_DAYS">10 Days</option>
                                        </Input>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please Select Day before retrying.' : undefined;
                                      }}
                                      type="select"
                                      className="form-control"
                                      name="daysBeforeRetrying"
                                    />
                                  </FormGroup>
                                </Col>
                              </Row>
                              <Row>
                                <Col xs={12} sm={12} md={8} lg={8}>
                                  <FormGroup>
                                    <Label for="maxNumberOfFailures">Once the subscription reaches the maximum number of failures:</Label>
                                    <Field
                                      render={({ input, meta }) => (
                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} >
                                            <option value="CANCEL_SUBSCRIPTION">Cancel Subscription</option>
                                            <option value="PAUSE_SUBSCRIPTION">Pause Subscription</option>
                                            <option value="SKIP_FAILED_ORDER">Skip failed Order</option>
                                        </Input>
                                      )}
                                      validate={value => {
                                        return !value ? 'Please Select Retry Attempts.' : undefined;
                                      }}
                                      type="select"
                                      className="form-control"
                                      name="maxNumberOfFailures"
                                    />
                                  </FormGroup>
                                </Col>
                                </Row>
                            </CardBody>
                          </Card>
                        </Col>
                      </Row>

                </form>
              );
            }}
          />
           {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Dunning Management</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=jlmCwVZzViQ"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
          </HelpPopUp> */}
        </ReactCSSTransitionGroup>
      </Fragment>
    </FeatureAccessCheck>
     );
}

const mapStateToProps = state => ({
  dunningManagementEntity: state.dunningManagement.entity,
  loading: state.dunningManagement.loading,
  updating: state.dunningManagement.updating,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  completeChecklistItem,
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(dunningManagement);
