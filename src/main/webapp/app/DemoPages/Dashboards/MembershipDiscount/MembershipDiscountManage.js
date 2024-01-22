import React, { Fragment, useEffect, useState, useRef } from 'react';
import Loader from 'react-loaders';
import {
    Card,
    CardBody,
    CardFooter,
    Col,
    Collapse,
    FormGroup,
    FormFeedback,
    FormText,
    Input,
    Button,
    InputGroup,
    InputGroupText,
    Label,
    Modal,
    ModalHeader,
    ModalBody,
    ModalFooter,
    Row
} from 'reactstrap';
import _ from "lodash";
import { Tooltip as ReactTooltip } from "react-tooltip";
import Switch from 'react-switch';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect, useSelector } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { Help } from '@mui/icons-material';
import MultiselectModal from 'app/DemoPages/Dashboards/TaggingRules/components/MultiselectModal';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import { OnChange } from 'react-final-form-listeners';
import {
    createEntity,
    getEntity,
    reset,
    updateEntity,
} from 'app/entities/membership-discount/membership-discount.reducer';
import Creatable from 'react-select/creatable';

function MembershipDiscountManage(props) {
    const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
    const { membershipDiscountEntity, loading, updating, usedProductIds } = props;
    const [formData, setFormData] = useState({ ...membershipDiscountEntity });
    const [formErrors, setFormErrors] = useState(null);
    const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);

    const handleClose = () => {
        props.history.push('/dashboards/membership-discount');
    };
    useEffect(() => {
        if (isNew) {
            props.reset();
        } else {
            props.getEntity(props.match.params.id);
        }
    }, []);
    useEffect(() => {
        if (props.updateSuccess) {
            handleClose();
        }
    }, [props.updateSuccess]);
    useEffect(() => setFormData(membershipDiscountEntity), [props]);
    const saveEntity = values => {
        let params = {
            membershipDiscount: values.membershipDiscount
        };
        params.membershipDiscount.customerTags = "";
        if (values.customerTagList && values.customerTagList?.length > 0) {
            params.membershipDiscount.customerTags = _.map(values.customerTagList, o => o.value).join(",")
        }
        let products = [];
        try {
            _.each(JSON.parse(values.productIds), o => {
                products.push({
                    productId: o.id,
                    productTitle: o.title,
                })
            })
        } catch (error) { }
        params.membershipDiscountProducts = products;
        if (isNew) {
            props.createEntity(params);
        } else {
            props.updateEntity(params);
        }
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
                    heading={isNew ? 'Create Membership Discount' : 'Edit Membership Discount'}
                    icon="pe-7s-network icon-gradient bg-mean-fruit"
                    enablePageTitleAction
                    actionTitle="Save"
                    onActionClick={() => {
                        submit();
                    }}
                    enableSecondaryPageTitleAction={true}
                    secondaryActionTitle="Cancel"
                    onSecondaryActionClick={handleClose}
                    formErrors={formErrors}
                    errorsVisibilityToggle={errorsVisibilityToggle}
                    onActionUpdating={updating}
                />
                {loading ? (
                    <div style={{ margin: '10% 0 0 43%' }}
                        className="loader-wrapper d-flex justify-content-center align-items-center">
                        <Loader type="line-scale" />
                    </div>
                ) : (
                    <div>
                        <CardBody>
                            <Form
                                mutators={{
                                    ...arrayMutators
                                }}
                                initialValues={membershipDiscountEntity}
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
                                        if (Object.keys(errors).length === 0 && errors.constructor === Object) {
                                            handleSubmit();
                                        } else {
                                            if (Object.keys(errors).length) handleSubmit();
                                            setFormErrors(errors);
                                            setErrorsVisibilityToggle(!errorsVisibilityToggle);
                                        }
                                    }

                                    return (
                                        <>

                                            <form onSubmit={handleSubmit}>



                                                <ReactTooltip effect='solid'
                                                    delayUpdate={500}
                                                    html={true}
                                                    place={'right'}
                                                    border={true}
                                                    type={'info'} multiline="true" />

                                                <Row>

                                                    <Col md="4">
                                                        <h5>Membership Discount information</h5>
                                                        <FormText>

                                                            <p>
                                                                Title of the Membership Discount  (since you may have several Membership Discount ),
                                                                and the product that you select, for the Membership Discount .<br /><br />
                                                                <span style={{ fontSize: '12px' }}><b>Note</b>: Membership Discount title is for internal use only, and will not be visible to your customers.</span>
                                                            </p>

                                                        </FormText>
                                                    </Col>
                                                    <Col md="8">

                                                        <Card className="card-margin">
                                                            <CardBody>
                                                                <Row>
                                                                    <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                                        <FormGroup>
                                                                            <Label for="title">Membership Discount Title</Label> <Help style={{ fontSize: '1rem' }}
                                                                                data-tip="<div style='max-width:300px'>Title of the Membership Discount (since you may have several Membership Discount), and the product that you select, for the Membership Discount.</div>" />
                                                                            <Field
                                                                                render={({ input, meta }) => <Input {...input}
                                                                                    invalid={meta.error && meta.touched ? true : null} />}
                                                                                validate={value => {
                                                                                    return !value ? 'Please provide Membership Discount Title.' : undefined;
                                                                                }}
                                                                                id="title"
                                                                                className="form-control"
                                                                                type="text"
                                                                                name="membershipDiscount.title"
                                                                                placeholder="Membership Discount Title"
                                                                            />
                                                                        </FormGroup>
                                                                    </Col>
                                                                </Row>


                                                                <Row>
                                                                    <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                                        <FormGroup>
                                                                            <Label for="discount">Discount (%)</Label>
                                                                            <Field
                                                                                render={({ input, meta }) => <Input {...input}
                                                                                    invalid={meta.error && meta.touched ? true : null} />}
                                                                                validate={value => {
                                                                                    return !value ? 'Please provide discount.' : undefined;
                                                                                }}
                                                                                id="discount"
                                                                                className="form-control"
                                                                                type="number"
                                                                                name="membershipDiscount.discount"
                                                                                placeholder="Membership Discount"
                                                                            />
                                                                        </FormGroup>
                                                                    </Col>
                                                                </Row>

                                                                <Row>
                                                                    <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                                        <FormGroup>
                                                                            <Label for="customerTagList">Customer Tags</Label>
                                                                            <Field
                                                                                render={({ input, meta }) => (
                                                                                    <Creatable
                                                                                        {...input}
                                                                                        isMulti
                                                                                        placeholder="Add tags..."
                                                                                        options={values.customerTagList}
                                                                                    />
                                                                                )}
                                                                                initialValue={values.customerTagList}
                                                                                name={`customerTagList`}
                                                                            />
                                                                        </FormGroup>
                                                                    </Col>
                                                                </Row>

                                                                <Row>
                                                                    <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                                                                        <FormGroup>
                                                                            <Label for="Products">Products</Label>
                                                                            <Field
                                                                                name={`productIds`}
                                                                                totalTitle="Select Product"
                                                                                index={1}
                                                                                methodName="Save"
                                                                                buttonLabel="Select Products"
                                                                                header="Product"
                                                                                render={({ input, meta }) => (
                                                                                    <MultiselectModal
                                                                                        value={input.value}
                                                                                        onChange={input.onChange}
                                                                                        totalTitle="Select Product"
                                                                                        index={1}
                                                                                        methodName="Save"
                                                                                        buttonLabel="Select Products"
                                                                                        header="Product"
                                                                                        hideValidation
                                                                                        {...input}
                                                                                    />
                                                                                )}
                                                                            />
                                                                        </FormGroup>
                                                                    </Col>
                                                                </Row>
                                                            </CardBody>
                                                        </Card>


                                                    </Col>
                                                </Row>
                                            </form>
                                        </>
                                    );
                                }}
                            />
                        </CardBody>
                    </div>

                )
                }
            </ReactCSSTransitionGroup>
        </Fragment>
    )
}
const mapStateToProps = storeState => ({
    membershipDiscountEntity: storeState.membershipDiscount.entity,
    loading: storeState.membershipDiscount.loading,
    updating: storeState.membershipDiscount.updating,
    updateSuccess: storeState.membershipDiscount.updateSuccess
});

const mapDispatchToProps = {
    getEntity,
    updateEntity,
    createEntity,
    reset
};

export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountManage)
