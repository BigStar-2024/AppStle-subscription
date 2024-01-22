import React, { Fragment, useEffect, useState } from 'react';
import Loader from 'react-loaders';
//import './setting.scss';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  Row,
  Spinner
} from 'reactstrap';
import { Field, Form, FormSpy } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import { OnChange } from 'react-final-form-listeners';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { data } from './email-customization-fields.js';
import ColorPicker2 from '../Utilities/ColorPicker2.js';
import Switch from 'react-switch';
import {
  createEntity,
  fetchEmailTemplatePreviewHTML,
  getEntity,
  reset,
  resetEmailTemplateSettings,
  updateEntity
} from 'app/entities/email-template-setting/email-template-setting.reducer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import defaultHTMLContent from './baseMailTemplateHTML';
import swal from 'sweetalert';

const EmailTemplateDetails = ({
  emailTemplateSettingEntity,
  getEntity,
  updateEntity,
  createEntity,
  loading,
  reset,
  history,
  fetchEmailTemplatePreviewHTML,
  emailTemplatePreviewHTML,
  emailTemplatePreviewLoading,
  resetEmailTemplateSettingLoading,
  resetEmailTemplateSettings,
  ...props
}) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  // const { subscriptionGroupEntity, loading, updating } = props;
  // const [formData, setFormData] = useState({ ...emailTemplateSettingEntity });
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [formState, setFormState] = useState({});
  const [iframeUrl, setIframeUrl] = useState('');

  useEffect(() => {
    getEntity(props.match.params.id);
  }, []);

  useEffect(() => {
    setIframeUrl(getBlobURL(emailTemplatePreviewHTML, 'text/html'));
  }, [emailTemplatePreviewHTML]);

  const previewEmailTemplate = data => {
    fetchEmailTemplatePreviewHTML(data);
  };

  useEffect(() => {
    if (!emailTemplatePreviewLoading && emailTemplateSettingEntity && emailTemplateSettingEntity.id) {
      previewEmailTemplate(emailTemplateSettingEntity);
    }
  }, [emailTemplateSettingEntity]);

  const getBlobURL = (code, type) => {
    const blob = new Blob([code], { type });
    return URL.createObjectURL(blob);
  };

  const handleClose = () => {
    history.push('/dashboards/email-templates');
  };

  //   useEffect(() => setFormData(subscriptionGroupEntity), [props]);
  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  useEffect(() => {
    if (resetEmailTemplateSettingLoading === false) getEntity(props.match.params.id);
  }, [resetEmailTemplateSettingLoading]);

  const saveEntity = values => {
    if (isNew) {
      createEntity(values);
    } else {
      updateEntity(values);
    }
  };

  const validateForNumber = (value, field) => (value ? (isNaN(value) ? `${field} - Must be a number` : undefined) : undefined);

  const onChangeCustomEmailHTML = (form, event) => {
    if (event.target.checked) {
      if (emailTemplateSettingEntity?.html) {
        form.change('html', emailTemplateSettingEntity?.html);
      } else {
        form.change('html', defaultHTMLContent);
      }
    } else {
      if (emailTemplateSettingEntity?.html) {
        swal({
          content: {
            element: 'span',
            attributes: {
              innerHTML:
                "<h5>Are you sure you want to disabled?</h5><br><p>If you disable <b>Custom Email HTML</b> and update the email template, your saved <b>Custom Email HTML</b>  data will be lost.</p>",
              className: ' my-0 px-0'
            }
          },
          icon: 'warning',
          buttons: ['Cancel', 'Confirm'],
          dangerMode: true,
        }).then(value => {
          value ? form.change('html', '') : "";
        });
      } else {
        form.change('html', '');
      }
    }
  }

  let submit;
  return (
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
          heading={isNew ? 'Create Email Template' : 'Edit Email Template'}
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5129455-how-to-customize-emails' target='blank'> Customize email template. Make it your own.</a>"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            history.push(`/dashboards/email-templates`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
          tutorialButton={{
            show: true,
            docs: [
              {
                title: "How to Customize Emails",
                url: "https://intercom.help/appstle/en/articles/5129455-email-template-customization"
              }
            ]
          }}
        />
        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <Form
            initialValues={emailTemplateSettingEntity}
            onSubmit={saveEntity}
            render={({ handleSubmit, form, submitting, pristine, values, errors }) => {
              submit =
                Object.keys(errors).length === 0 && errors.constructor === Object
                  ? handleSubmit
                  : () => {
                      if (Object.keys(errors).length) handleSubmit();
                      setFormErrors(errors);
                      setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    };
              return (
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <Card className="main-card p-2">
                      <CardHeader>
                        <div className="btn-actions-pane-right">
                          <Button
                            className={'mr-1'}
                            disabled={emailTemplatePreviewLoading}
                            color="warning"
                            onClick={() => previewEmailTemplate(values)}
                          >
                            {emailTemplatePreviewLoading ? (
                              <>
                                <Spinner size="sm" color="light" />
                                &nbsp;{'Loading..'}
                              </>
                            ) : (
                              'Preview'
                            )}
                          </Button>
                          <Button
                            color="danger"
                            onClick={() => resetEmailTemplateSettings(props.match.params.id, emailTemplateSettingEntity?.emailSettingType)}
                          >
                            {resetEmailTemplateSettingLoading ? (
                              <>
                                <Spinner size="sm" color="light" />
                                &nbsp;{'Loading..'}
                              </>
                            ) : (
                              'Reset Email Template'
                            )}
                          </Button>
                        </div>
                      </CardHeader>
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            {Object.keys(data).map((emailTemplateElement, index) => {
                              return (
                                <div key={data[emailTemplateElement].displayName + index}>
                                  <h5
                                    style={{
                                      marginTop: '5.5rem',
                                      marginBottom: '0.75rem',
                                      color: '#545cd8',
                                      display: 'inline',
                                      width: '100px'
                                    }}
                                  >
                                    {data[emailTemplateElement].displayName}
                                    {data[emailTemplateElement].helpText && (
                                      <div dangerouslySetInnerHTML={{ __html: data[emailTemplateElement].helpText }} />
                                    )}
                                    <hr style={{ marginTop: '0.75rem' }} />
                                  </h5>

                                  <Row>
                                    {Object.keys(data[emailTemplateElement])
                                      .filter(element => typeof data[emailTemplateElement][element] === 'object')
                                      .map(emailTemplateElementProperty => {
                                        const item = data[emailTemplateElement][emailTemplateElementProperty];
                                        return (
                                          <Col
                                            key={item?.id}
                                            xs={12}
                                            sm={12}
                                            className="md-6"
                                            style={{ display: item?.type === 'hidden' ? 'none' : '' }}
                                          >
                                            {item?.id === 'sendBCCEmailFlag' ? (
                                              <>
                                                <FormGroup style={{ display: 'flex' }}>
                                                  <Field
                                                    render={({ input, meta }) => (
                                                      <Switch
                                                        checked={Boolean(input.value)}
                                                        onColor="#3ac47d"
                                                        onChange={input.onChange}
                                                        handleDiameter={20}
                                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                                        height={17}
                                                        width={36}
                                                        id="material-switch"
                                                      />
                                                    )}
                                                    className="form-control"
                                                    name="sendBCCEmailFlag"
                                                  />
                                                  <Label
                                                    for="sendBCCEmailFlag"
                                                    style={{
                                                      marginBottom: '0rem',
                                                      marginLeft: '2rem'
                                                    }}
                                                  >
                                                    Only Send this email as Admin notification?
                                                  </Label>
                                                  {data[emailTemplateElement].helpText && (
                                                    <div dangerouslySetInnerHTML={{ __html: data[emailTemplateElement].helpText }} />
                                                  )}
                                                </FormGroup>
                                                <p className="text-muted">
                                                  Note: If you are enabling the flag then you must have to provide a Admin notification /
                                                  BCC Email.
                                                </p>
                                              </>
                                            ) : (
                                              <FormGroup>
                                                <Label for={item?.id}>
                                                  {typeof item?.displayName === 'string'
                                                    ? item?.displayName
                                                    : item?.displayName?.[emailTemplateSettingEntity?.emailSettingType]}
                                                </Label>
                                                {item?.helpText && <div dangerouslySetInnerHTML={{ __html: item?.helpText }} />}
                                                <Field
                                                  render={({ input, meta }) => (
                                                    <>
                                                      {item?.type === 'input' && (
                                                        <InputGroup>
                                                          <Input
                                                            {...input}
                                                            placeholder={item?.placeholder}
                                                            type={item?.validation === 'NUMBER' ? 'number' : 'text'}
                                                            invalid={meta.error && meta.touched ? true : null}
                                                          />
                                                          {item?.validation === 'NUMBER' && (
                                                            <InputGroupAddon addonType='append'>px</InputGroupAddon>
                                                          )}
                                                        </InputGroup>
                                                      )}
                                                      {item?.type === 'select' && (
                                                        <Input {...input} type="select">
                                                          {item?.options?.map(option => (
                                                            <option key={option} value={option?.value}>
                                                              {option?.label}
                                                            </option>
                                                          ))}
                                                        </Input>
                                                      )}

                                                      {values?.upcomingOrderEmailBuffer != null && item?.id == 'upcomingOrderEmailBuffer' && (
                                                        <Input {...input} type="select">
                                                          {item?.dropdownValues.map((option, index) => (
                                                            <option key={index} value={option + 1}>
                                                              {option + 1}
                                                            </option>
                                                          ))}
                                                        </Input>
                                                      )}

                                                      {item?.type === 'color' && (
                                                        <ColorPicker2
                                                          {...input}
                                                          placeholder={item?.placeholder}
                                                          onChange={value => input.onChange(value)}
                                                        />
                                                      )}

                                                      {item?.type === 'textarea' && (
                                                        <Input
                                                          {...input}
                                                          placeholder={item?.placeholder}
                                                          type="textarea"
                                                          rows="8"
                                                          onChange={value => input.onChange(value)}
                                                        />
                                                      )}

                                                      {item?.type === 'hidden' && <Input {...input} type="hidden" />}
                                                    </>
                                                  )}
                                                  validate={
                                                    item?.validation &&
                                                    (value => {
                                                      if (item?.validation === 'NUMBER') {
                                                        return validateForNumber(value, item?.displayName);
                                                      }
                                                    })
                                                  }
                                                  autoComplete="off"
                                                  id={item?.id}
                                                  className="form-control"
                                                  type={item?.type}
                                                  name={item?.id}
                                                />
                                                <OnChange name={`sendBCCEmailFlag`}>
                                                  {(value, previous) => {
                                                    if (value && !values.bccEmail) {
                                                      errors.bccEmail = 'Bcc Email required';
                                                    } else {
                                                      delete errors.bccEmail;
                                                    }
                                                  }}
                                                </OnChange>
                                                <OnChange name={`bccEmail`}>
                                                  {(value, previous) => {
                                                    if (!value && values.sendBCCEmailFlag) {
                                                      errors.bccEmail = 'Bcc Email required';
                                                    } else {
                                                      delete errors.bccEmail;
                                                    }
                                                  }}
                                                </OnChange>

                                                {item?.hint && <FormText>{item?.hint}</FormText>}
                                              </FormGroup>
                                            )}
                                          </Col>
                                        );
                                      })}
                                  </Row>
                                </div>
                              );
                            })}
                            <FeatureAccessCheck
                              hasAnyAuthorities={'enableCustomEmailHtml'}
                              upgradeButtonText="Upgrade to enable custom html"
                            >
                              <div>
                                <div className="d-flex w-100 justify-content-between">
                                  <FormGroup check>
                                    <Field
                                      render={({ input, meta }) => (
                                        <Label check>
                                          <Input
                                            {...input}
                                            checked={values?.html ? true : false}
                                            onChange={event => onChangeCustomEmailHTML(form, event)}
                                          />
                                          <h5 style={{ color: '#545cd8' }}> Custom Email HTML </h5>
                                        </Label>
                                      )}
                                      type="checkbox"
                                      className="form-control"
                                      name="enableCustomEmailHTML"
                                    />
                                  </FormGroup>
                                  <a
                                    target="_blank"
                                    href="https://intercom.help/appstle/en/articles/5129455-how-to-customize-emails"
                                    style={{
                                      fontSize: '12px',
                                      color: '#545cd8'
                                    }}
                                  >
                                    Learn how to customize emails
                                  </a>
                                </div>
                                <hr style={{ marginTop: '0.75rem' }} />

                                <Row>
                                  <Col xs={12} sm={12} className="md-6">
                                    <FormGroup>
                                      <Label for={'html'}>HTML</Label>

                                      <Field
                                        render={({ input, meta }) => (
                                          <Input
                                            {...input}
                                            placeholder={'Please enter Email HTML'}
                                            type="textarea"
                                            rows="8"
                                            onChange={value => input.onChange(value)}
                                          />
                                        )}
                                        autoComplete="off"
                                        id={'html'}
                                        className="form-control"
                                        type={'textarea'}
                                        name={'html'}
                                      />
                                    </FormGroup>
                                  </Col>
                                </Row>
                              </div>
                            </FeatureAccessCheck>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6} style={{ alignItems: 'stretch' }}>
                            <>
                              <h5
                                style={{
                                  marginTop: '1.5rem',
                                  marginBottom: '0.75rem',
                                  color: '#545cd8'
                                }}
                              >
                                Preview
                                <div>
                                  <hr style={{ marginTop: '0.75rem' }} />
                                </div>
                              </h5>
                              {emailTemplatePreviewLoading ? (
                                <>
                                  <Spinner size="sm" color="light" />
                                  &nbsp;{'Loading..'}
                                </>
                              ) : (
                                <div className="previewEmail" dangerouslySetInnerHTML={{ __html: emailTemplatePreviewHTML }} />
                              )}
                            </>
                          </Col>
                        </Row>
                      </CardBody>
                    </Card>
                  </div>
                  <FormSpy subscription={{ values: true }}>
                    {({ values }) => {
                      setFormState(values);
                      return <></>;
                    }}
                  </FormSpy>
                </form>
              );
            }}
          />
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  emailTemplateSettingEntity: state.emailTemplateSetting.entity,
  loading: state.emailTemplateSetting.loading,
  updating: state.emailTemplateSetting.updating,
  updateSuccess: state.emailTemplateSetting.updateSuccess,
  emailTemplatePreviewHTML: state.emailTemplateSetting.emailTemplatePreviewHTML,
  emailTemplatePreviewLoading: state.emailTemplateSetting.emailTemplatePreviewLoading,
  resetEmailTemplateSettingLoading: state.emailTemplateSetting.resetEmailTemplateSetting
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
  fetchEmailTemplatePreviewHTML,
  resetEmailTemplateSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(EmailTemplateDetails);
