import React, {Fragment, useEffect, useState} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from "app/Layout/AppMain/PageTitle";
import { Tooltip as ReactTooltip } from "react-tooltip";
import {Card, CardBody, CardHeader, Col, Collapse, FormGroup, Input, Label, Row} from "reactstrap";
import {Field, Form} from "react-final-form";
import {connect} from 'react-redux';
import { ChevronForward } from "react-ionicons";
import {
  createEntity,
  getAppstleMenuLabelsByShop,
  updateEntity
} from "app/entities/appstle-menu-labels/appstle-menu-labels.reducer";
import {data} from "app/DemoPages/Dashboards/GeneralSetting/CustomizeWidgetSetting/subscription-customisation-trimmed";

const AppstleMenu = (props) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [accordionState, setAccordionState] = useState([false, false, false]);
  const [cssAccordianState, setCssAccordianState] = useState([]);

  useEffect(() => {
    props.getAppstleMenuLabelsByShop();
    setCssAccordianState([...Object.keys(data).map((el, index) => (index === 0 ? true : false))])
  }, [])

  const toggleAccordion = tab => {
    const prevState = accordionState;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  };

  const toggleCssAccordion = (tab) => {
    const prevState = cssAccordianState;
    const state = prevState.map((x, index) => (tab === index ? !x : x));
    setCssAccordianState(state);
  }

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  const saveEntity = values => {
    props.updateEntity(values);
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
          heading="Appstle Menu"
          subheading=""
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Save"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props?.updating}
          updatingText="Updating"
          sticky={true}
        />
        <Form
          initialValues={props.appstleMenuLabels}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
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
                <ReactTooltip effect="solid" delayUpdate={500} html={true} place={'right'} border={true} type={'info'}
                              multiline="true"/>

                <div id="accordion" className="accordion-wrapper mb-3">
                  <Card className="main-card">
                    <CardHeader
                      onClick={() => toggleAccordion(0)}
                      aria-expanded={accordionState[0]}
                      aria-controls="SubscribeShipping"
                      style={{cursor: 'pointer'}}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Labels
                      <span style={{...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : ''}}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="SubscribeShipping"
                              aria-labelledby="SubscribeShipping">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="seeMore">See More</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Enter see more value"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'seeMore'}
                                className="form-control"
                                name={'seeMore'}
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="noDataFound">No data found</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'noDataFound'}
                                className="form-control"
                                name={'noDataFound'}
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="productDetails">Product Details</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'productDetails'}
                                className="form-control"
                                name={'productDetails'}
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="editQuantity">Edit Quantity</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'editQuantity'}
                                className="form-control"
                                name={'editQuantity'}
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="addToCart">Add to cart</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'addToCart'}
                                className="form-control"
                                name={'addToCart'}
                              />
                            </FormGroup>
                          </Col>

                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="productAddedSuccessfully">Product added to cart successfully</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'productAddedSuccessfully'}
                                className="form-control"
                                name={'productAddedSuccessfully'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="wentWrong">Something went wrong</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'wentWrong'}
                                className="form-control"
                                name={'wentWrong'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="results">Results</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'results'}
                                className="form-control"
                                name={'results'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="adding">Adding</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'adding'}
                                className="form-control"
                                name={'adding'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="subscribe">Subscribe</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'subscribe'}
                                className="form-control"
                                name={'subscribe'}
                              />
                            </FormGroup>
                          </Col>
                          <Col xs={12} sm={12} md={6} lg={6}>
                            <FormGroup className="mb-2">
                              <Label for="notAvailable">Not Available</Label>
                              <Field
                                render={({input, meta}) => {
                                  return (
                                    <>
                                      <Input {...input} placeholder="Add no data found Message"
                                             invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  );
                                }}
                                validate={() => {
                                }}
                                autoComplete="off"
                                id={'notAvailable'}
                                className="form-control"
                                name={'notAvailable'}
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                      </CardBody>
                    </Collapse>
                  </Card>


                  <Card className="main-card mt-2">
                    <CardHeader
                      onClick={() => toggleAccordion(1)}
                      aria-expanded={accordionState[1]}
                      aria-controls="SubscribeShipping"
                      style={{cursor: 'pointer'}}
                    >
                      <i className="header-icon lnr-store icon-gradient bg-plum-plate"> </i> Custom CSS
                      <span style={{...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : ''}}>
                        <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="SubscribeShipping"
                              aria-labelledby="SubscribeShipping">
                      <CardBody>
                        <Row>
                          <Col xs={12} sm={12} md={12} lg={12}>
                            <FormGroup className="mb-2">
                              <Label for="customCss">Custom CSS</Label>
                              <Field
                                render={({input, meta}) => (
                                  <Fragment>
                                    <Input {...input} rows="15"/>
                                  </Fragment>
                                )}
                                id="customCss"
                                className="form-control"
                                type="textarea"
                                name="customCss"
                              />
                            </FormGroup>
                          </Col>
                        </Row>
                      </CardBody>
                    </Collapse>
                  </Card>
                </div>
              </form>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  appstleMenuLabels: state.appstleMenuLabels.entity,
  updating: state.appstleMenuLabels.updating
});

const mapDispatchToProps = {
  getAppstleMenuLabelsByShop,
  createEntity,
  updateEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenu);
