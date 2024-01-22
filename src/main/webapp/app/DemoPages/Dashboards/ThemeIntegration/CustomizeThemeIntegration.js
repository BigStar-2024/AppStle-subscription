import React, {Component, Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {Field, Form} from 'react-final-form';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import {
  Alert, Card, CardBody,
  FormGroup, FormText, Input,
  Modal, ModalHeader, ModalBody, ModalFooter,
  Row, Col
} from 'reactstrap';
import {getEntitiesLite} from 'app/entities/theme-code/theme-code.reducer';
import {getEntities, updateEntity, generateScriptsForTheme} from 'app/entities/theme-settings/theme-settings.reducer';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {THEME_APP_EXTENSION_HANDLE, THEME_APP_EXTENSION_UUID} from "app/config/constants";
import {ShopifyThemeInstallationVersion} from "app/shared/model/enumerations/shopify-theme-installation-version.model";
import {IntercomAPI} from 'react-intercom';
import { getAllThemeDetails } from 'app/modules/account/liquid-editor/assets';
import MySaveButton from '../Utilities/MySaveButton';

export class CustomizeThemeIntegration extends Component {
  constructor(props) {
    super(props);
    this.form = React.createRef();

    this.state = {
      showThemeModal: false,
      unpublishedThemes: []
    };
  }

  componentDidMount() {
    this.props.getEntities();
    this.props.getEntitiesLite();
    this.props.getAllThemeDetails();
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.allThemes.length > 0) {
      const unpublishedThemes = nextProps.allThemes.filter(theme => theme.role != 'main');
      this.setState({unpublishedThemes: unpublishedThemes});
    }
  }

  toggleThemeModal = () => {
    this.setState({ showThemeModal: !this.state.showThemeModal });
  };

  saveEntity = values => {
    const {themeSettingsEntity} = this.props;
    if (!values?.themeName) {
      values.themeName = 'alchemy'
    }
    const entity = {
      ...themeSettingsEntity,
      ...values
    };
    this.props.updateEntity(entity);
  };

  render() {
    const {themeSettingsEntity, themeCodeList, updating, allThemes, themeDetailsLoading} = this.props;
    const identity = value => value;
    let submitForm;
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
            heading="Theme Integration"
            subheading="When you switch your theme in Shopify, please update it here as well."
            icon="lnr-pencil icon-gradient bg-mean-fruit"
            actionTitle="Save"
            enablePageTitleAction
            onActionClick={() => {
              submitForm();
            }}
            onActionUpdating={updating}
            enableSecondaryPageTitleAction
            secondaryActionTitle="Try On Unpublished Themes"
            onSecondaryActionClick={() => this.setState({ showThemeModal: true })}
            sticky={true}
            tutorialButton={{
              show: true,
              docs: [
                {
                  title: "How to install Appstle Subscriptions on a draft theme",
                  url: "https://intercom.help/appstle/en/articles/8070523-how-to-install-appstle-subscriptions-on-a-draft-theme"
                }
              ]
            }}
          />
          <Alert color="danger">
          If you are making any changes in your theme, please re-check if the integration of subscriptions is working correctly. As in changes in themes can break the integration.
          </Alert>
          <Form
            initialValues={themeSettingsEntity}
            onSubmit={this.saveEntity}
            render={({handleSubmit, pristine, form, submitting, values}) => {
              submitForm = handleSubmit;
              let stepText = themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2 ? (<>Step
                2:</>) : null;
              return (
                <form onSubmit={handleSubmit}>
                  <Card className="main-card p-3">
                    <CardBody>
                      <FormGroup>

                        {themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2 ? (
                          <>
                            <h6>
                              <b>Step 1</b>
                            </h6>

                            <div>
                              <div className="mt-2">
                                <h6>
                                  Please click{' '}
                                  <a
                                    href={`https://${themeSettingsEntity.shop}/admin/themes/current/editor?context=apps&appEmbed=gid://shopify/OnlineStoreThemeAppEmbed/${THEME_APP_EXTENSION_HANDLE}?app_embed_uuid=${THEME_APP_EXTENSION_UUID}`}
                                    target="_blank"
                                  >
                                    <b>here</b>
                                  </a>{' '}
                                  to activate embedded block of subscription widget from your theme settings. You can
                                  de-activate it anytime.
                                </h6>
                              </div>
                            </div>

                            <div className="divider"/>
                          </>

                        ) : null}


                        <label>
                          <strong>{stepText} Select Theme</strong>
                        </label>
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="select">
                                  {themeCodeList && themeCodeList?.map(option => {
                                    return <option value={option.themeName}>{option.themeNameFriendly}</option>;
                                  })}
                                </Input>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="themeName"/>
                      </FormGroup>
                      <Alert className="mb-2" color="warning">
                        <h6>Can't find your theme?</h6>
                        <br/>
                        <p>We are always looking to add support for more themes.</p>
                        <p>
                          If you'd like to see support for your theme included{', '}
                          <Link
                            onClick={() => {
                              Intercom('showNewMessage', 'I need some help with theme integration for my shop. Can you please help?');
                            }}
                          >
                            contact us
                          </Link>{' '}
                          with the name of your theme and store.
                        </p>
                      </Alert>

                      <FormGroup>
                        <label>
                          <strong>Selector</strong>
                        </label>
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="textarea"/>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="selectedSelector"
                          parse={identity}
                        />
                        <FormText>Required only when the default positioning of the widget doesn't work on your theme.
                          The selector refers to jQuery selector. If you are not sure, please <Link
                            onClick={() => {
                              IntercomAPI('showNewMessage', "I would like to configure cart widget in my store. Can you please help?");
                            }}
                          >
                            contact us
                          </Link>{' '} and we would be happy to configure it for you.</FormText>
                      </FormGroup>

                      <FormGroup>
                        <label>
                          <strong>Subscription Link Selector</strong>
                        </label>
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="textarea"/>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="subscriptionLinkSelector"
                          parse={identity}
                        />
                      </FormGroup>

                      <FormGroup>
                        <label>
                          <strong>Cart Hidden Attributes Selector</strong>
                        </label>
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="textarea"/>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="cartHiddenAttributesSelector"
                          parse={identity}
                        />
                      </FormGroup>

                      <FormGroup>
                        <label>
                          <strong>Placement</strong>
                        </label>
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="select">
                                  <option value="BEFORE">Before</option>
                                  <option value="AFTER">After</option>
                                  <option value="FIRST_CHILD">First Child</option>
                                  <option value="LAST_CHILD">Last Child</option>
                                </Input>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="placement"
                          initialValue="BEFORE"
                        />
                      </FormGroup>
                    </CardBody>
                  </Card>
                </form>
              );
            }}
          />
          <Modal isOpen={this.state.showThemeModal} toggle={this.toggleThemeModal}>
            <ModalHeader toggle={this.toggleThemeModal}>
              <div style={{fontSize: '19px', fontWeight: '500'}}>Try on unpublished themes</div>
            </ModalHeader>
            <ModalBody style={{padding: "0"}}>
            {themeDetailsLoading ? (
                <div
                  style={{ margin: '10% 0 0 43%', flexDirection: 'column' }}
                  className="loader-wrapper d-flex justify-content-center align-items-center"
                >
                  <div className="appstle_preloader appstle_loader--big"></div>
                </div>
              ) : (
                <>
                  <div className="divider"/>
                  <div className="container clearfix">
                    {this.state.unpublishedThemes?.map((theme, i) =>
                      <>
                        <Row key={'theme_'+ theme.id} className="mt-1 mb-1">
                          <Col xs={8}>{theme.name}</Col>
                          <Col xs={4}>
                            <MySaveButton
                            text="Install"
                            updatingText={'Installing ..'}
                            updating={updating}
                            className="btn-primary"
                            onClick={() => this.props.generateScriptsForTheme(theme.id)}
                            />
                          </Col>
                        </Row>
                        <div className="divider"/>
                      </>
                      )
                    }
                  </div>
                </>
              )}
            </ModalBody>
            <ModalFooter></ModalFooter>
          </Modal>
        </ReactCSSTransitionGroup>
      </Fragment>
    );
  }
}

const mapStateToProps = ({themeSettings, themeCode, assets}) => ({
  themeSettingsEntity: themeSettings.entity,
  loading: themeSettings.loading,
  updating: themeSettings.updating,
  updateSuccess: themeSettings.updateSuccess,
  themeCodeList: themeCode.entities,
  allThemes: assets.allThemes,
  themeDetailsLoading: assets.loading
});

const mapDispatchToProps = {
  getEntities,
  updateEntity,
  getEntitiesLite,
  getAllThemeDetails,
  generateScriptsForTheme
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomizeThemeIntegration);
