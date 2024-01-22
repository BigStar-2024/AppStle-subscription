import React, { Component, Fragment } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Field, Form } from 'react-final-form';
import Switch from 'react-switch';
import { Card, CardBody, Col, Container, FormGroup, FormText, Input, Label, Row, Alert } from 'reactstrap';
import MySaveButton from '../Utilities/MySaveButton';
import { getEntity, updateEntity } from 'app/entities/activity-updates-settings/activity-updates-settings.reducer';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";

export class ActivityUpdatesSummaryReports extends Component {
  constructor(props) {
    super(props);
    this.state = {
      invalidEmailCount: false
    };
  }
  componentDidMount() {
    this.props.getEntity();
  }
  saveEntity = values => {
    const { activityUpdatesSettingsEntity } = this.props;
    const entity = {
      ...activityUpdatesSettingsEntity,
      ...values
    };
    this.props.updateEntity(entity);
  };

  handleValidationSummaryReportDeliverToEmail = (emails, state, utils) => {
    if (emails !== undefined && emails !== null && emails !== '') {
      let emailList = emails.split(','); //splitting as list
      const emailPattern = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/; //email simple pattern

      let invalidEmails = 0;
      emailList = emailList
        .filter(item => {
          // filtering only valid emails
          if (emailPattern.test(item)) {
            return item;
          } else {
            invalidEmails = invalidEmails + 1;
            return item;
          }
        })
        .map(item => item.trim()); //trimming if there is any space
      this.setState({
        invalidEmailCount: invalidEmails
      });
      let finalEmailList = '';
      emailList.map((item, index) => {
        //creating a valid email list as string
        finalEmailList = emailList.length === index + 1 ? finalEmailList.concat(item) : finalEmailList.concat(item + ',');
      });
      utils.changeValue(state, 'summaryReportDeliverToEmail', () => finalEmailList); // updating field value with pure clean email list
    }
  };

  render() {
    const { activityUpdatesSettingsEntity } = this.props;
    const { invalidEmailCount } = this.state;

    const identity = value => value;
    let submitForm;
    return (
      <FeatureAccessCheck
        hasAnyAuthorities={'enableSummaryReports'}
        upgradeButtonText="Upgrade to enable activity update summary reports"
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
            heading="Activity Updates Summary Report"
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
          <Container fluid>
            <Card className="main-card mb-3">
              <CardBody>
                <Form
                  initialValues={activityUpdatesSettingsEntity}
                  onSubmit={this.saveEntity}
                  mutators={{
                    //mutating or updating email field
                    emailsProcess: (args, state, utils) => {
                      const emails = state.formState.values.summaryReportDeliverToEmail; //taking all data from state
                      this.handleValidationSummaryReportDeliverToEmail(emails, state, utils);
                    }
                  }}
                  render={({ handleSubmit, pristine, form, submitting, values }) => {
                    submitForm = handleSubmit;

                    return (
                      <form onSubmit={handleSubmit}>
                        <CardBody>
                          <FormGroup row>
                            
                            <Col style={{display: "flex"}}  sm={10}>
                            <Label for="notificationRate" sm={2}>
                              <strong>Enabled :</strong>
                            </Label>
                              <Field
                                render={({ input }) => {
                                  return (
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
                                        className="mr-2 mb-2"
                                        id="material-switch"
                                      />
                                    </div>
                                  );
                                }}
                                name="summaryReportEnabled"
                              />
                            </Col>
                            {activityUpdatesSettingsEntity?.summaryReportProcessing ? (
                              <Col sm={2}>
                              <Alert color="warning">
                                <div style={{ fontSize: '18px', textAlign: 'center' }}>Processing</div>
                              </Alert>
                              </Col>
                            ) : ('')}
                          </FormGroup>

                          {/* <FormGroup>
                        <Label>
                          <strong>Send Report</strong>
                        </Label>
                        <Field
                          render={({ input, meta }) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="select">
                                  <option value="DAILY">Daily</option>
                                  <option value="WEEKLY">Weekly</option>
                                </Input>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="summaryReportFrequency"
                          validate={required}
                        />
                      </FormGroup> */}
                          <FormGroup>
                            <Field
                              render={({ input, meta }) => (
                                <div>
                                  <Label>
                                    <strong>Deliver to</strong>
                                  </Label>
                                  <Input {...input} onBlur={form.mutators.emailsProcess} />
                                  {meta.touched && meta.error && <span>{meta.error}</span>}
                                </div>
                              )}
                              placeholder="16px"
                              name="summaryReportDeliverToEmail"
                              // validate={composeValidators(required, mustBeEmail)}
                              parse={identity}
                            />
                            <FormText color="warning">
                              {invalidEmailCount > 0 ? 'Invalid email Found, Invalid email may fail to send Notification' : ''}
                            </FormText>
                          </FormGroup>
                          <Row>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label>
                                  <strong>Report Frequency</strong>
                                </Label>
                                <Field
                                  render={({ input, meta }) => {
                                    return (
                                      <div>
                                        <Input {...input} className="mb-2" type="select">
                                          <option value="DAILY">DAILY</option>
                                          <option value="WEEKLY">WEEKLY</option>
                                          <option value="MONTHLY">MONTHLY</option>
                                          <option value="QUARTERLY">QUARTERLY</option>
                                        </Input>
                                        {/* {meta.touched && meta.error && <span>{meta.error}</span>} */}
                                      </div>
                                    );
                                  }}
                                  name="summaryReportFrequency"
                                  // validate={required}
                                />
                                <FormText>{"Choosing a Report Frequency will send report only for the specified time period. For example, Weekly Report Frequency will only have reports from the previous week."}</FormText>
                              </FormGroup>
                            </Col>
                          </Row>
                          {/* <FormGroup>
                        <Label>
                          <strong>Report Summary</strong>
                        </Label>
                        <Field
                          render={({ input, meta }) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="select">
                                  <option value="ALL_TIME">All Time</option>
                                  <option value="LAST_1_DAY">Last 1 Day</option>
                                  <option value="LAST_7_DAYS">Last 7 Days</option>
                                  <option value="LAST_30_DAYS">Last 30 Days</option>
                                  <option value="LAST_90_DAYS">Last 90 Days</option>
                                </Input>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="summaryReportTimePeriod"
                          validate={required}
                        />
                      </FormGroup> */}
                        </CardBody>
                        {/*<CardFooter>
                              <MySaveButton updating={this.props.updating} />
                            </CardFooter>*/}
                      </form>
                    );
                  }}
                />
              </CardBody>
            </Card>
          </Container>
        </ReactCSSTransitionGroup>
      </Fragment>
      </FeatureAccessCheck>
    );
  }
}

const mapStateToProps = ({ activityUpdatesSettings }) => ({
  activityUpdatesSettingsEntity: activityUpdatesSettings.entity,
  loading: activityUpdatesSettings.loading,
  updating: activityUpdatesSettings.updating,
  updateSuccess: activityUpdatesSettings.updateSuccess
});

const mapDispatchToProps = { getEntity, updateEntity };

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ActivityUpdatesSummaryReports);
