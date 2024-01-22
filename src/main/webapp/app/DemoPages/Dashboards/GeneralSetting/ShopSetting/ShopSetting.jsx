import React, { Fragment, useEffect, useRef, useState } from 'react';
import Loader from 'react-loaders';
//import './setting.scss';
import {
  Card,
  CardBody,
  Col,
  FormGroup,
  Input,
  InputGroup,
  Label,
  Row
} from 'reactstrap';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { data } from './shop-setting-fields.js';
import ColorPicker2 from '../../Utilities/ColorPicker2.js';
import { getEntity, updateEntity, createEntity, reset } from 'app/entities/shop-info/shop-info.reducer';
import InputWithVariables from './InputWithVariables';


const ShopSetting = ({
  emailTemplateSettingEntity,
  getEntity,
  updateEntity,
  createEntity,
  loading,
  reset,
  history,
  ...props
}) => {

  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);

  const saveEntity = async (values) => {
      await updateEntity(values);
  };

  useEffect(() => {
    if(formErrors) {
      if (Object.keys(formErrors)?.length === 0) {
        setErrorsVisibilityToggle(false);
      }
    }
  },[formErrors])

  const getDropDownValues = (id) => {
    let dropdown = [{name: 'Customer ID', value: 'customer.id'}, {name: 'Subscription Contract ID', value: 'subscriptionContract.id'}, {name: 'First Order ID', value: 'firstOrder.id'}, {name: 'Selling Plan IDs', value: 'sellingPlan.id'}, {name: 'Selling Plan Names', value: 'sellingPlan.name'}];
    if ((id === "firstTimeOrderTag") || (id === "recurringOrderTag")) {
      dropdown.push({name: 'Recurring Cycle', value: 'subscriptionContract.currentCycle'})
    }
    return dropdown
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
          heading="Tag Settings"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5190111-how-to-tag-recurring-orders-and-subscribing-customers' target='blank'>Learn more about Tag Settings here.</a>"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            history.push(`/`);
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
                title: "How to Tag Recurring Orders and Subscribing Customers",
                url: "https://intercom.help/appstle/en/articles/5190111-how-to-tag-recurring-orders-and-subscribing-customers"
              }
            ]
          }}
        />
        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <>
          <Row className="align-items-center" style={{marginLeft:'0px'}}>
                <Col>
                  <div>
                    <h4>Welcome to Tag Settings</h4>
                    <p className='text-muted'>
                    This section is for tagging and distinctly marking your subscription order and subscribing customers from regular one-time purchasers and non-subscription customers.
                 </p>
                    <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                      href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                  </div>
                </Col>
              </Row>
          <Form
            initialValues={emailTemplateSettingEntity}
            onSubmit={saveEntity}
            mutators={{
              addVariablesToTextArea: ([name, value], state, utils) => {
                utils.changeValue(state, name, () => value);
              }
            }}
            render={({ handleSubmit, form, submitting, pristine, values, errors }) => {
              submit =
                Object.keys(errors).length === 0 && errors.constructor === Object
                  ? () => {setFormErrors(errors); handleSubmit()}
                  : () => {
                      if (Object.keys(errors).length) handleSubmit();
                      setFormErrors(errors);
                      setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    };
              return (
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <Card className="main-card p-2">
                      <CardBody>
                        <Row>
                          <Col xs={12}>
                            {Object.keys(data).map((shopSettingElement, index) => {
                              return (
                                <div key={data[shopSettingElement].displayName}>
                                  <h5
                                    style={{
                                      marginTop: '1.5rem',
                                      marginBottom: '0.75rem',
                                      color: '#545cd8'
                                    }}
                                  >
                                    {data[shopSettingElement].displayName}
                                  </h5>
                                  <p>{data[shopSettingElement]?.helpText && (
                                      <div
                                        style={{ color: 'inherit' }}
                                        dangerouslySetInnerHTML={{ __html: data[shopSettingElement]?.helpText }}
                                      />
                                    )}</p>
                                    <div>
                                      <hr style={{ marginTop: '0.75rem' }} />
                                    </div>

                                  <Row>
                                    {Object.keys(data[shopSettingElement])
                                      .filter(element => typeof data[shopSettingElement][element] === 'object')
                                      .map(shopSettingElementProperty => {
                                        const item = data[shopSettingElement][shopSettingElementProperty];
                                        return (
                                          <Col
                                            key={item?.id}
                                            xs={12}
                                            sm={12}
                                            md={4}
                                            className="md-6"
                                          >
                                            {item?.type === 'inputWithVariables' && <InputWithVariables
                                              label={item?.displayName}
                                              fieldName={item?.id}
                                              selectDropdown={getDropDownValues(item?.id)}
                                              formState={values}
                                              addVariablesToTextArea={form.mutators.addVariablesToTextArea}
                                              helpText={item?.helpText}
                                              maxLength={parseInt(item?.maxLength)}
                                            />}
                                          </Col>
                                        );
                                      })}
                                  </Row>
                                </div>
                              );
                            })}
                          </Col>
                        </Row>
                      </CardBody>
                    </Card>
                  </div>
                </form>
              );
            }}
          />
          </>
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  emailTemplateSettingEntity: state.shopInfo.entity,
  loading: state.shopInfo.loading,
  updating: state.shopInfo.updating,
  updateSuccess: state.shopInfo.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

export default connect(mapStateToProps, mapDispatchToProps)(ShopSetting);
