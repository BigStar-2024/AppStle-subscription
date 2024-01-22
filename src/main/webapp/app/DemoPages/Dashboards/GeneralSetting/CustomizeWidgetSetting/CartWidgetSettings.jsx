import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
    Card,
    CardBody,
    Col,
    FormGroup,
    Input,
    Label,
    Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import Switch from 'react-switch';
import {getEntity, updateEntity} from 'app/entities/cart-widget-settings/cart-widget-settings.reducer';

const CartWidgetSetting = ({
    getEntity,
    updateEntity,
    cartWidgetSettingsEntity,
    ...props}) => {

    const [formErrors, setFormErrors] = useState(null);
    const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
    
    useEffect(() => {
        getEntity(0)
    }, [])

    const saveEntity = values => {
        updateEntity(values);
    };

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
                    heading="Cart widget Settings"
                    icon="lnr-pencil icon-gradient bg-mean-fruit"
                    actionTitle="Save"
                    enablePageTitleAction
                    onActionClick={() => {
                        submit();
                    }}
                    formErrors={formErrors}
                    errorsVisibilityToggle={errorsVisibilityToggle}
                    onActionUpdating={props.updating}
                    updatingText="Updating"
                    sticky={true}
                />
                <Form
                    initialValues={cartWidgetSettingsEntity}
                    onSubmit={saveEntity}
                    render={({handleSubmit, form, submitting, pristine, values, errors}) => {
                        submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
                        if (Object.keys(errors).length) handleSubmit();
                        setFormErrors(errors);
                        setErrorsVisibilityToggle(!errorsVisibilityToggle);
                        }
                        return (
                            <form onSubmit={handleSubmit}>
                                <Card className="main-card p-3">
                                    <CardBody>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <Field
                                                render={({input, meta}) => (
                                                    <Switch
                                                        checked={(Boolean(input.value))}
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
                                                name="enable_cart_widget_settings"
                                                />
                                                <Label for="enable_cart_widget_settings" style={{marginBottom:"0rem", marginLeft:"2rem"}}>Enable Cart Widget Settings</Label>  
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                    <Label for="cartRowSelector">Cart Row Selector</Label>
                                                    <Field
                                                    render={({input, meta}) => (
                                                        <Input {...input}
                                                            invalid={meta.error && meta.touched ? true : null}/>
                                                    )}
                                                    id="cartRowSelector"
                                                    className="form-control"
                                                    type="textarea"
                                                    name="cartRowSelector"
                                                    />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                <Label for="cartRowPlacement">Cart Row Placement</Label>
                                                <Field
                                                    render={({ input, meta }) => (
                                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} >
                                                            <option value=""></option>
                                                            <option value="BEFORE">Before</option>
                                                            <option value="AFTER">After</option>
                                                            <option value="FIRST_CHILD">First Child</option>
                                                            <option value="LAST_CHILD">Last Child</option>
                                                        </Input>
                                                      )}
                                                      type="select"
                                                      className="form-control"
                                                      name="cartRowPlacement"
                                                />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                    <Label for="cartLineItemSelector">Cart Line Item Selector</Label>
                                                    <Field
                                                    render={({input, meta}) => (
                                                        <Input {...input}
                                                            invalid={meta.error && meta.touched ? true : null}/>
                                                    )}
                                                    id="cartLineItemSelector"
                                                    className="form-control"
                                                    type="textarea"
                                                    name="cartLineItemSelector"
                                                    />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                <Label for="cartLineItemPlacement">Cart Line Item Placement</Label>
                                                <Field
                                                    render={({ input, meta }) => (
                                                        <Input {...input} invalid={meta.error && meta.touched ? true : null} >
                                                            <option value=""></option>
                                                            <option value="BEFORE">Before</option>
                                                            <option value="AFTER">After</option>
                                                            <option value="FIRST_CHILD">First Child</option>
                                                            <option value="LAST_CHILD">Last Child</option>
                                                        </Input>
                                                      )}
                                                      type="select"
                                                      className="form-control"
                                                      name="cartLineItemPlacement"
                                                />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                    <Label for="cartFormSelector">Cart Form Selector</Label>
                                                    <Field
                                                    render={({input, meta}) => (
                                                        <Input {...input}
                                                            invalid={meta.error && meta.touched ? true : null}/>
                                                    )}
                                                    id="cartFormSelector"
                                                    className="form-control"
                                                    type="textarea"
                                                    name="cartFormSelector"
                                                    />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                <FormGroup>
                                                    <Label for="appstelCustomeSelector">Appstel Custome Selector</Label>
                                                    <Field
                                                    render={({input, meta}) => (
                                                        <Input {...input}
                                                            invalid={meta.error && meta.touched ? true : null}/>
                                                    )}
                                                    id="appstelCustomeSelector"
                                                    className="form-control"
                                                    type="textarea"
                                                    name="appstelCustomeSelector"
                                                    />
                                                </FormGroup>
                                            </Col>
                                        </Row>
                                    </CardBody>
                                </Card>
                            </form>
                        );
                    }}
                />
            </ReactCSSTransitionGroup>
        </Fragment>
    );
}

const mapStateToProps = state => ({
  cartWidgetSettingsEntity: state.cartWidgetSettings.entity,
  updating: state.cartWidgetSettings.updating,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity
};

export default connect(
mapStateToProps,
mapDispatchToProps
)(CartWidgetSetting);