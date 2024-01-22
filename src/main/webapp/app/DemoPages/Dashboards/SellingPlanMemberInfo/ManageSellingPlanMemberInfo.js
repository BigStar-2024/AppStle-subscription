import React, { Fragment, useEffect, useState } from 'react';
import Loader from 'react-loaders';
import {
    Card,
    CardHeader,
    CardBody,
    Col,
    FormGroup,
    FormText,
    Label,
    Row
} from 'reactstrap';
import Switch from 'react-switch';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import arrayMutators from 'final-form-arrays';
import { OnChange } from 'react-final-form-listeners';
import _ from "lodash";
import Creatable from 'react-select/creatable';
import '../SubscriptionGroups/CreateSubscriptionGroup.scss';
import axios from "axios";
import { cleanEntity } from 'app/shared/util/entity-utils';
import { toast } from 'react-toastify';
import {required} from "app/DemoPages/Dashboards/Utilities/FormValidators";
const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
};


function ManageSellingPlanMemberInfo(props) {
    const subscriptionId = props.match.params.subscriptionId;
    const sellingPlanId = props.match.params.sellingPlanId;
    const [formErrors, setFormErrors] = useState(null);
    const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState(false);
    const [entity, setEntity] = useState(null);
    const [pageTitle, setPageTitle] = useState(null);
    const [subscriptionInfo, setSubscriptionInfo] = useState(null);

    const defaultData = {
        subscriptionId: parseInt(subscriptionId),
        sellingPlanId: parseInt(sellingPlanId),
        enableMemberInclusiveTag: false,
        memberInclusiveTags: "",
        enableMemberExclusiveTag: false,
        memberExclusiveTags: "",
    }
    let submit;

    const handleClose = () => {
        setUpdating(false)
        props.history.push('/dashboards/advanced-selling-plans');
    };
    const createEntity = (params) => {
        axios.post('api/selling-plan-member-infos', params).then((resp) => {
            toast.success('Selling plan information saved successfully', options);
            handleClose();
        })
    }
    const updateEntity = (params) => {
        axios.put('api/selling-plan-member-infos', params).then((resp) => {
            toast.success('Selling plan information saved successfully', options);
            handleClose();
        })
    }
    const saveEntity = (values) => {
        let params = { ...values };
        params.memberInclusiveTags = "";
        params.memberExclusiveTags = "";
        if (params.enableMemberInclusiveTag) {
            params.memberInclusiveTags = _.map(params.memberOnlyInclusiveTagsList, o => o.value).join(",");
        }

        if (params.enableMemberExclusiveTag) {
            params.memberExclusiveTags = _.map(params.memberExclusiveTagsList, o => o.value).join(",")
        }
        params = _.omit(params, ['memberOnlyInclusiveTagsList', 'memberExclusiveTagsList'])
        setUpdating(true)
        params = cleanEntity(params);
        if (params.id) {
            updateEntity(params)
        } else {
            createEntity(params)
        }
    };
    useEffect(() => {
        axios.get('api/v2/subscription-groups/' + subscriptionId).then((resp) => {
            let info = resp.data;
            setSubscriptionInfo(info);
            let foundSellingPlan = _.find(info.subscriptionPlans, o => {
                return _.includes(o.id,sellingPlanId);
            })
            if(foundSellingPlan){
                setPageTitle(`${foundSellingPlan.frequencyName} - ${info.groupName}`)
            }
        })
        axios.get('api/selling-plan-member-infos/' + sellingPlanId).then(resp => {
            let sellingPlanDetails = resp.data;
            if (sellingPlanDetails.enableMemberInclusiveTag && sellingPlanDetails.memberInclusiveTags) {
                sellingPlanDetails.memberOnlyInclusiveTagsList = _.map(_.split(sellingPlanDetails.memberInclusiveTags, ","), v => {
                    return { label: v, value: v }
                })
            }
            if (sellingPlanDetails.enableMemberExclusiveTag && sellingPlanDetails.memberExclusiveTags) {
                sellingPlanDetails.memberExclusiveTagsList = _.map(_.split(sellingPlanDetails.memberExclusiveTags, ","), v => {
                    return { label: v, value: v }
                })
            }

            setEntity(sellingPlanDetails);
            setLoading(false);
        }, () => {
            let sellingPlanDetails = defaultData;
            setEntity(sellingPlanDetails)
            setLoading(false);

        })

    }, [])
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
                    heading={'Edit Selling Plan Member Information'}
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
                <div>
                    {
                        loading ? <div style={{ margin: '10% 0 0 43%' }}
                            className="loader-wrapper d-flex justify-content-center align-items-center">
                            <Loader type="line-scale" />
                        </div> :
                            <CardBody>
                                <Form
                                    mutators={{
                                        ...arrayMutators
                                    }}
                                    initialValues={entity}
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
                                                    <Row>

                                                        <Col md="4">
                                                            <h5>Selling Plan Member information</h5>
                                                            {/*<FormText>
                                                                <p>Set up tags to allow to exclude member from this selling plans</p>
                                                            </FormText>*/}
                                                        </Col>
                                                        <Col md="8">
                                                            <Card className="card-margin">
                                                                <CardHeader>{pageTitle}</CardHeader>
                                                                <CardBody>
                                                                    <Row>
                                                                        <Col xs={12} sm={12} md={12} lg={12}>

                                                                            <FormGroup>
                                                                                <Field
                                                                                    render={({ input }) => {
                                                                                        return (
                                                                                            <div>
                                                                                                <label>
                                                                                                    <strong>Customers must have this tag to subscribe to products in this plan:</strong>
                                                                                                </label>
                                                                                                <br />
                                                                                                <Switch
                                                                                                    checked={Boolean(input.value)}
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
                                                                                    name={`enableMemberInclusiveTag`}
                                                                                    initialValue={false}
                                                                                />
                                                                                <OnChange name={`enableMemberInclusiveTag`}>
                                                                                    {(value, previous) => {
                                                                                        let newVal = {
                                                                                            ...values, enableMemberInclusiveTag: value
                                                                                        }
                                                                                        if (value) {
                                                                                            newVal.enableMemberExclusiveTag = false;
                                                                                        }
                                                                                        form.initialize(newVal)
                                                                                    }}
                                                                                </OnChange>
                                                                                {values.enableMemberInclusiveTag && (
                                                                                    <Fragment>
                                                                                        <Label for={`memberOnlyInclusiveTags`}>
                                                                                            <strong>Member inclusive tags (optional)</strong>
                                                                                        </Label>
                                                                                        <FormGroup >
                                                                                            <Field
                                                                                                render={({ input, meta }) => (
                                                                                                    <Creatable
                                                                                                        {...input}
                                                                                                        isMulti
                                                                                                        placeholder="Add tags..."
                                                                                                        options={values.memberOnlyInclusiveTagsList}
                                                                                                    />
                                                                                                )}
                                                                                                initialValue={values.memberOnlyInclusiveTagsList}
                                                                                                name={`memberOnlyInclusiveTagsList`}
                                                                                            />
                                                                                        </FormGroup>
                                                                                    </Fragment>
                                                                                )}
                                                                            </FormGroup>

                                                                            <FormGroup>
                                                                                <Field
                                                                                    render={({ input }) => {
                                                                                        return (
                                                                                            <div>
                                                                                                <label>
                                                                                                    <strong>Is Non-Member only?</strong>
                                                                                                </label>
                                                                                                <br />
                                                                                                <Switch
                                                                                                    checked={Boolean(input.value)}
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
                                                                                    name={`enableMemberExclusiveTag`}
                                                                                    initialValue={false}
                                                                                />
                                                                                <OnChange name={`enableMemberExclusiveTag`}>
                                                                                    {(value, previous) => {
                                                                                        let newVal = {
                                                                                            ...values, enableMemberExclusiveTag: value
                                                                                        }
                                                                                        if (value) {
                                                                                            newVal.enableMemberInclusiveTag = false;
                                                                                        }
                                                                                        form.initialize(newVal)
                                                                                    }}
                                                                                </OnChange>
                                                                                {values.enableMemberExclusiveTag && (
                                                                                    <Fragment>
                                                                                        <Label for={`memberExclusiveTags`}>
                                                                                            <strong>Member exclusive tags</strong>
                                                                                        </Label>
                                                                                        <FormGroup >
                                                                                            <Field
                                                                                                render={({ input, meta }) => (
                                                                                                    <div>
                                                                                                        <Creatable
                                                                                                            {...input}
                                                                                                            invalid={meta.error && meta.touched ? true : null}
                                                                                                            isMulti
                                                                                                            placeholder="Add tags..."
                                                                                                            options={values.memberExclusiveTagsList}
                                                                                                        />
                                                                                                        {meta.error && (
                                                                                                            <div
                                                                                                            style={{
                                                                                                                order: '4',
                                                                                                                width: '100%',
                                                                                                                display: meta.error && meta.touched ? 'block' : 'none'
                                                                                                            }}
                                                                                                            className="invalid-feedback"
                                                                                                            >
                                                                                                            {meta.error}
                                                                                                            </div>
                                                                                                        )}
                                                                                                    </div>
                                                                                                )}
                                                                                                validate={value => {
                                                                                                    return !value ? 'Please provide atleast one exclusive tag' : undefined;
                                                                                                  }}
                                                                                                initialValue={values.memberExclusiveTagsList}
                                                                                                name={`memberExclusiveTagsList`}
                                                                                            />
                                                                                        </FormGroup>
                                                                                    </Fragment>
                                                                                )}
                                                                            </FormGroup>

                                                                        </Col></Row>

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

                    }
                </div>
            </ReactCSSTransitionGroup>
        </Fragment>
    )
}

export default ManageSellingPlanMemberInfo
