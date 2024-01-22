import PageTitle from 'app/Layout/AppMain/PageTitle';
import createDecorator from 'final-form-focus';
import React, { Component, Fragment } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import {
  Alert,
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  Collapse,
  Container,
  FormGroup,
  Input,
  InputGroup,
  InputGroupText,
  Row,
  Table
} from 'reactstrap';
import { getEmailCustomDomain, getEntity, updateEntity } from 'app/entities/shop-info/shop-info.reducer';
import { mustBeDomain } from '../Utilities/FormValidators';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';
import { faCheckCircle, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export class EmailDomainSettings extends Component {
  constructor(props) {
    super(props);
    this.toggleAccordion = this.toggleAccordion.bind(this);

    this.state = {
      collapse: false,
      accordion: [true, true, true, true],
      status: 'Closed'
    };
  }

  componentDidMount() {
    this.props.getEntity(-1);
    this.props.getEmailCustomDomain();
  }

  saveEntity = values => {
    const { shopInfo } = this.props;
    const entity = {
      ...shopInfo,
      ...values
    };

    this.props.updateEntity(entity);
  };

  toggleAccordion(tab) {
    const prevState = this.state.accordion;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    this.setState({
      accordion: state
    });
  }

  render() {
    const { shopInfo, customDomain, loading, updating } = this.props;
    const identity = value => value;
    const focusOnError = createDecorator();

    let submitForm;
    return (
      <FeatureAccessCheck hasAnyAuthorities={'enableCustomEmailDomain'} upgradeButtonText="Upgrade to enable email domain settings">
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
              heading="Custom Email Domain"
              subheading="This setting is for using your own domain as the verified sender of emails to your customers. This setting is recommended if you want to improve the delivery rate of your emails, and avoid emails going to the spam folder."
              icon="lnr-pencil icon-gradient bg-mean-fruit"
              actionTitle="Update"
              enablePageTitleAction
              onActionClick={() => {
                submitForm();
              }}
              onActionUpdating={this.props.updating}
              updatingText="Updating"
              sticky={true}
            />
            { customDomain && customDomain?.sending_dns_records?.every(v => v.valid === 'valid') &&
              <Alert color="success">
                <h4 className="alert-heading">Your custom email domain is verified!</h4>
                <p>
                  Now all emails will be sent using your custom email domain.
                </p>
              </Alert>
            }
            { customDomain && !customDomain?.sending_dns_records?.every(v => v.valid === 'valid') &&
              <Alert color="warning">
                <h4 className="alert-heading">Your custom email domain is not verified yet!</h4>
                <p>
                  Until it gets verified, all emails will be sent using <strong>Appstle's</strong> domain.
                </p>
              </Alert>
            }
            <Form
              initialValues={shopInfo}
              decorators={[focusOnError]}
              onSubmit={this.saveEntity}
              render={({ handleSubmit, pristine, form, submitting, values }) => {
                submitForm = handleSubmit;
                return (
                  <form onSubmit={handleSubmit}>
                    <Card className="mb-2">
                      <CardBody>
                        <Container fluid>
                          <FormGroup>
                            <Field
                              render={({ input, meta }) => (
                                <div>
                                  <label>
                                    <strong>Custom domain</strong>
                                  </label>
                                  <InputGroup>
                                    <InputGroupText
                                      style={{
                                        color:
                                          customDomain !== null &&
                                          customDomain.sending_dns_records &&
                                          customDomain.sending_dns_records.every(v => v.valid === 'valid')
                                            ? 'green'
                                            : 'orange'
                                      }}
                                    >
                                      <FontAwesomeIcon
                                        icon={
                                          customDomain !== null &&
                                          customDomain.sending_dns_records &&
                                          customDomain.sending_dns_records.every(v => v.valid === 'valid')
                                            ? faCheckCircle
                                            : faExclamationCircle
                                        }
                                      />
                                    </InputGroupText>
                                    <Input {...input} placeholder="Example: subscription.mydomain.com" />
                                  </InputGroup>
                                  {meta.touched && meta.error && <span>{meta.error}</span>}
                                </div>
                              )}
                              name="emailCustomDomain"
                              parse={identity}
                              validate={mustBeDomain}
                            />
                          </FormGroup>
                          {customDomain && customDomain.message && (
                            <Alert color="danger">
                              <h5 className="alert-heading">Response from email service provider:</h5>
                              <p>
                                {customDomain.message}
                              </p>
                            </Alert>
                          )}
                          <Row className="align-items-start welcome-page-with-video">
                            <Col sm="7">
                              <div>
                                <h4>Welcome to Custom Email Domain</h4>
                                <h7>
                                  We recommend using a subdomain with our app, such as "<b>subscription.mydomain.com</b>". Please note that
                                  using a subdomain will not affect your ability to send email from your root domain (eg. "
                                  <b>you@mydomain.com</b>").
                                  <br />
                                  <br />
                                  This setting requires you to update your DNS records to verify that you are an authorized sender for this
                                  domain.{' '}
                                  <a
                                    href="https://intercom.help/appstle/en/articles/5347192-how-to-set-up-a-custom-email-domain-subscriptions"
                                    target="_blank"
                                  >
                                    Learn more
                                  </a>
                                </h7>
                                <Card style={{ width: '75%', borderRadius: '18px', height: "auto", aspectRatio: "16 / 9" }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                                  <CardBody className="top-elem">
                                    <div className="text-center pb-3">How to set up custom email domain</div>
                                    <div className="text-center">
                                      <YoutubeVideoPlayer 
                                        url="https://youtu.be/m5tOE5gNV-o" 
                                        divClassName="video-container"
                                        iframeHeight="100%"
                                      />
                                    </div>
                                  </CardBody>
                                </Card>
                                <p className="text-muted">
                                  If you have any questions at any time, just reach out to us on{' '}
                                  <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                                </p>
                                <br /> <br />
                              </div>
                            </Col>
                          </Row>
                        </Container>

                        {/*<CardFooter>*/}
                        {/*  <MySaveButton updating={this.props.updating} />*/}
                        {/*</CardFooter>*/}
                      </CardBody>
                    </Card>
                  </form>
                );
              }}
            />

            {customDomain && customDomain.domain && (
              <Card>
                <CardHeader>Follow these steps to verify your domain</CardHeader>
                <CardBody>
                  <div id="accordion" className="accordion-wrapper mb-3">
                    <Card>
                      <CardHeader id="headingOne">
                        <Button
                          block
                          color="link"
                          className="text-left m-0 p-0"
                          onClick={() => this.toggleAccordion(0)}
                          aria-expanded={this.state.accordion[0]}
                          aria-controls="collapseOne"
                        >
                          <h5 className="m-0 p-0">1. Go to your DNS provider</h5>
                        </Button>
                      </CardHeader>
                      <Collapse isOpen={this.state.accordion[0]} data-parent="#accordion" id="collapseOne" aria-labelledby="headingOne">
                        <CardBody>
                          Go to the DNS provider that you use to manage <b>{customDomain.domain.name}</b> and add the following DNS records.
                          <br />
                          <br />
                          {/*Need a step-by-step walk through? Click here
                        <br />
                        <br />
                        Prefer to watch a video? We got you covered.
                        <br />
                        <br /> */}
                          Common providers include:
                          <ul>
                            <li>
                              <a
                                href="https://support.godaddy.com/help/article/7925/adding-or-editing-txt-records?locale=en"
                                target="_blank"
                              >
                                GoDaddy
                              </a>
                            </li>
                            <li>
                              <a
                                href="https://www.namecheap.com/support/knowledgebase/article.aspx/579/2237/which-record-type-option-should-i-choose-for-the-information-im-about-to-enter"
                                target="_blank"
                              >
                                NameCheap
                              </a>
                            </li>
                            {/* <li>Network Solutions</li> */}
                            {/* <li>Rackspace Email & Apps</li> */}
                            {/* <li>Rackspace Cloud DNS</li> */}
                            <li>
                              <a href="http://docs.aws.amazon.com/Route53/latest/DeveloperGuide/R53Console.html" target="_blank">
                                Amazon Route 53
                              </a>
                            </li>
                            <li>
                              <a href="https://support.google.com/domains/answer/3290350" target="_blank">
                                Google Domains
                              </a>
                            </li>
                            {/* <li>Digital Ocean</li> */}
                          </ul>
                        </CardBody>
                      </Collapse>
                    </Card>
                    <Card>
                      <CardHeader className="b-radius-0" id="headingTwo">
                        <Button
                          block
                          color="link"
                          className="text-left m-0 p-0"
                          onClick={() => this.toggleAccordion(1)}
                          aria-expanded={this.state.accordion[1]}
                          aria-controls="collapseTwo"
                        >
                          <h5 className="m-0 p-0">2. Add DNS records for sending</h5>
                        </Button>
                      </CardHeader>
                      <Collapse isOpen={this.state.accordion[1]} data-parent="#accordion" id="collapseTwo">
                        <CardBody>
                          <Table responsive hover className="mb-0">
                            <thead>
                              <tr>
                                <th></th>
                                <th>Type</th>
                                <th>Hostname</th>
                                <th>Enter This Value</th>
                                <th>Current Value</th>
                              </tr>
                            </thead>
                            <tbody>
                              {customDomain.sending_dns_records.map(value => {
                                if (value.record_type === 'TXT') {
                                  return (
                                    <tr key={value.name}>
                                      <td style={{ width: '25px', color: value.valid === 'valid' ? 'green' : 'orange' }}>
                                        <FontAwesomeIcon icon={value.valid === 'valid' ? faCheckCircle : faExclamationCircle} />
                                      </td>
                                      <td>{value.record_type}</td>
                                      <td>{value.name}</td>
                                      <td style={{ maxWidth: '150px' }}>{value.value}</td>
                                      <td style={{ maxWidth: '150px' }}>{value.cached[0]}</td>
                                    </tr>
                                  );
                                }
                              })}
                            </tbody>
                          </Table>
                        </CardBody>
                      </Collapse>
                    </Card>
                    <Card>
                      <CardHeader id="headingThree">
                        <Button
                          block
                          color="link"
                          className="text-left m-0 p-0"
                          onClick={() => this.toggleAccordion(2)}
                          aria-expanded={this.state.accordion[2]}
                          aria-controls="collapseThree"
                        >
                          <h5 className="m-0 p-0">3. Add DNS records for tracking</h5>
                        </Button>
                      </CardHeader>
                      <Collapse isOpen={this.state.accordion[2]} data-parent="#accordion" id="collapseThree">
                        <CardBody>
                          <Table responsive hover className="mb-0">
                            <thead>
                              <tr>
                                <th></th>
                                <th>Type</th>
                                <th>Hostname</th>
                                <th>Enter This Value</th>
                                <th>Current Value</th>
                              </tr>
                            </thead>
                            <tbody>
                              {customDomain.sending_dns_records.map(value => {
                                if (value.record_type === 'CNAME') {
                                  return (
                                    <tr key={value.name}>
                                      <td style={{ width: '25px', color: value.valid === 'valid' ? 'green' : 'orange' }}>
                                        <FontAwesomeIcon icon={value.valid === 'valid' ? faCheckCircle : faExclamationCircle} />
                                      </td>
                                      <td>{value.record_type}</td>
                                      <td>{value.name}</td>
                                      <td>{value.value}</td>
                                      <td>{value.cached[0]}</td>
                                    </tr>
                                  );
                                }
                              })}
                            </tbody>
                          </Table>
                        </CardBody>
                      </Collapse>
                    </Card>

                    <Card>
                      <CardHeader id="headingThree">
                        <Button
                          block
                          color="link"
                          className="text-left m-0 p-0"
                          onClick={() => this.toggleAccordion(3)}
                          aria-expanded={this.state.accordion[3]}
                          aria-controls="collapseThree"
                        >
                          <h5 className="m-0 p-0">4. Wait for your domain to verify</h5>
                        </Button>
                      </CardHeader>
                      <Collapse isOpen={this.state.accordion[3]} data-parent="#accordion" id="collapseThree">
                        <CardBody>
                          Once you make the above DNS changes it can take 24-48hrs for those changes to propagate. We will email you to let
                          you know once your domain is verified.
                          <br />
                          {/* <br /> */}
                          {/* <Button color="primary">Verify DNS Settings</Button> */}
                        </CardBody>
                      </Collapse>
                    </Card>
                  </div>
                </CardBody>
              </Card>
            )}
          </ReactCSSTransitionGroup>
        </Fragment>
      </FeatureAccessCheck>
    );
  }
}

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  updating: state.shopInfo.updating,
  customDomain: state.shopInfo.customDomain,
  loading: state.shopInfo.loading
});

const mapDispatchToProps = { getEmailCustomDomain, getEntity, updateEntity };

export default connect(mapStateToProps, mapDispatchToProps)(EmailDomainSettings);
