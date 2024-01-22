import React, { Fragment, useEffect, useRef, useState } from 'react';
import Loader from 'react-loaders';
import { CardBody, Col, Row } from 'reactstrap';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import { Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { createEntity, getEntity, getUsedProducts, reset, updateEntity } from 'app/entities/subscription-group/subscription-group.reducer';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import _ from 'lodash';
import './CreateSubscriptionGroup.scss';
import LoyaltyItem from './LoyaltyItem';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import HelpPopUp from 'app/DemoPages/Components/HelpPopUp/HelpPopUp';

const CreateSubscriptionGroup = props => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  const { subscriptionGroupEntity, loading, updating, usedProductIds } = props;
  const [formData, setFormData] = useState({ ...subscriptionGroupEntity });
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const subscriptionPlansRef = useRef(null);
  const [editPlanData, setEditPlanData] = useState();
  const handleClose = () => {
    props.history.push('/dashboards/subscription-plan');
  };
  const [tooltipOpen, setTooltipOpen] = useState(false);

  const toggle = () => setTooltipOpen(!tooltipOpen);
  useEffect(() => {
    // props.getUsedProducts(isNew ? null : props.match.params.id);
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    setFormData(subscriptionGroupEntity);
  }, [props]);

  useEffect(() => {
    if (props.updateSuccess) handleClose();
  }, [props.updateSuccess]);

  const saveEntity = values => {
    if (isNew) {
      props.createEntity(values);
    } else {
      props.updateEntity(values);
    }
  };

  const scrollToPlan = index => {
    // const yCordinate = document.querySelector('#subscriptionPlans' + index).getBoundingClientRect().y;
    // const screenHeight = screen.height;
    // const elementHeight = document.querySelector('#subscriptionPlans' + index).getBoundingClientRect().height;
    // if (screenHeight - (yCordinate + elementHeight) < 550) {
    //   scroll(
    //     0,
    //     (document.documentElement.scrollTop || document.body.scrollTop) + Math.abs(screenHeight - (yCordinate + elementHeight) - 550)
    //   );
    // }
  };

  const checkProducts = productsString => {
    let validation = {
      isValid: true,
      invalidProducts: [],
      message: ''
    };
    try {
      let products = JSON.parse(productsString);
      validation.isValid = true;
      validation.message = "";
    } catch (error) {}
    return validation;
  };

  let [editMode, setEditMode] = useState(false);
  let [viaEditButton, setViaEditButton] = useState(false);
  let submit;
  return (
    <Fragment>
      <FeatureAccessCheck

 hasAnyAuthorities={'accessSubscriberLoyaltyFeatures'}
 upgradeButtonText="Upgrade your plan"
      >
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading={'Setup Loyalty Plan'}
          icon="pe-7s-network icon-gradient bg-mean-fruit"
          enablePageTitleAction
          actionTitle="Save"
          onActionClick={() => {
            submit();
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Cancel"
          onSecondaryActionClick={() => {
            props.history.push(`/dashboards/subscription-plan`);
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={updating}
        />
        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <div>
            <CardBody>
              <Form
                mutators={{
                  ...arrayMutators,
                  setProductIds: (args, state, utils) => {
                    utils.changeValue(state, 'productIds', () => JSON.stringify(args[0]));
                  },
                  setVariantIds: (args, state, utils) => {
                    utils.changeValue(state, 'variantIds', () => JSON.stringify(args[0]));
                  },
                  setValue: ([field, value], state, { changeValue }) => {
                    changeValue(state, field, () => value);
                  }
                }}
                initialValues={subscriptionGroupEntity}
                onSubmit={saveEntity}
                render={({
                  handleSubmit,
                  form: {
                    mutators: { push, pop, update, remove }
                  },
                  form,
                  submitting,
                  pristine,
                  values,
                  errors,
                  valid
                }) => {
                  submit = () => {
                    let productValidation = checkProducts(values.productIds);
                    let productError = {};
                    if (!productValidation.isValid) {
                      productError = { productIds: productValidation.message };
                    }
                    let allErrors = _.extend(errors, productError);
                    if (Object.keys(errors).length === 0 && errors.constructor === Object) {
                      handleSubmit();
                    } else {
                      if (Object.keys(errors).length) handleSubmit();
                      setFormErrors(errors);
                      setErrorsVisibilityToggle(!errorsVisibilityToggle);
                    }
                  };

                  return (
                    <>
                      <form onSubmit={handleSubmit}>
                        <ReactTooltip
                          effect="solid"
                          delayUpdate={500}
                          html={true}
                          place={'right'}
                          border={true}
                          type={'info'}
                          multiline="true"
                        />
                        <Row>
                          {/* <Col md="4">
                          </Col> */}
                          <Col>
                            <div
                              ref={subscriptionPlansRef}
                              class={`${viaEditButton ? 'viaEditButton' : ''} ${
                                values?.['subscriptionPlans']?.length === 1 || editMode ? 'onEditMode' : ''
                              }`}
                            >
                              <FieldArray name="subscriptionPlans">
                                {({ fields }) =>
                                  fields.map((name, index) => (
                                    <LoyaltyItem name={name} key={name} index={index} values={values} update={update} form={form} />
                                  ))
                                }
                              </FieldArray>
                            </div>
                          </Col>
                        </Row>
                      </form>
                    </>
                  );
                }}
              />
            </CardBody>
          </div>
        )}
        {
            <HelpPopUp>
              <div className="mt-4 border-bottom pb-4">
                <h5>Loyalty - Shipping Price</h5>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                <iframe width="560" height="315" src="https://www.youtube.com/embed/ER5i3fAjHTQ" title="YouTube video player"
                frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
                </div>
              </div>
              <div className="py-4 border-bottom">
                <h5>Loyalty - Amount Off</h5>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                <iframe width="560" height="315" src="https://www.youtube.com/embed/Mj3IiqcFuSE" title="YouTube video player"
                frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
                </div>
              </div>
              <div className="py-4 border-bottom">
                <h5>Loyalty - Free Product</h5>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                <iframe width="560" height="315" src="https://www.youtube.com/embed/8oFpQq6lkDg" title="YouTube video player"
                frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
                </div>
              </div>
              <div className="py-4 border-bottom">
                <h5>Loyalty - Percentage Discount</h5>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                <iframe width="560" height="315" src="https://www.youtube.com/embed/dwONFEPDQYo" title="YouTube video player"
                frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
                </div>
              </div>
            </HelpPopUp>
          }
      </ReactCSSTransitionGroup>
      </FeatureAccessCheck>
    </Fragment>
  );
};

const mapStateToProps = storeState => ({
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  usedProductIds: storeState.subscriptionGroup.usedProductIds,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  getUsedProducts,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateSubscriptionGroup);
