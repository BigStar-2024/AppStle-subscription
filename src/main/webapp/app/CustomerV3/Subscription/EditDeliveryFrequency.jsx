import React, { Fragment, useState } from 'react'
import { connect, useSelector } from 'react-redux';
import { Button, Input, Label, FormGroup, FormFeedback, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import Axios from 'axios';
import { motion, AnimatePresence } from "framer-motion"

const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
};

function EditDeliveryFrequency(props) {
    const { contractId, shopName, getCustomerPortalEntity } = props;
    const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity)
    const subscriptionEntities = useSelector(state => state.subscription.entity);
    let [updateInProgress, setUpdateInProgress] = useState(false);
    const [editMode, setEditMode] = useState(false)
    const [isCountValid, setCountValid] = useState(true)
    const [selectedIntervalCount, setSelectedIntervalCount] = useState(null)
    const [selectedInterval, setSelectedInterval] = useState(null)
    let [countInputBlurred, setCountInputBlurred] = useState(false);
    const validateNumber = value => {
        const type = typeof value;
        if (type === 'undefined') {
            return undefined;
        } else if (!value.trim()) {
            return `field value is required`;
        } else if (isNaN(value)) {
            return `field value should be a number`;
        } else if (parseInt(value) < 1) {
            return `field value should be greater or equal to 0.`;
    } else if (parseInt(value) < parseInt(subscriptionEntities?.billingPolicy?.intervalCount) || parseInt(subscriptionEntities?.billingPolicy?.intervalCount % parseInt(value) != 0) ) {
        return `field value should be less or equal or multiple to order frequency.`;
    } else {
            return undefined;
        }
    };
    const countChangeHandler = value => {
        setSelectedIntervalCount(value);
        if (!countInputBlurred) return;
        if (validateNumber(value)) {
            setCountValid(false);
        } else {
            setCountValid(true);
        }
    };
    const countBlurHandler = value => {
        setSelectedIntervalCount(value);
        setCountInputBlurred(true);
        if (validateNumber(value)) {
            setCountValid(false);
        } else {
            setCountValid(true);
        }
    };
    const update = () => {
        let timer = setTimeout(() => {
            if (selectedInterval && selectedIntervalCount) {
                setUpdateInProgress(true);
                const requestUrl = `api/subscription-contracts-update-delivery-interval?contractId=${contractId}&deliveryInterval=${selectedInterval}&deliveryIntervalCount=${selectedIntervalCount}&isExternal=true`;
                Axios
                    .put(requestUrl)
                    .then(res => {
                        getCustomerPortalEntity(contractId);
                        setUpdateInProgress(false);
                        toast.success('Contract Updated', options);
                        setEditMode(!editMode)
                    }, () => {

                        setUpdateInProgress(false);
                    })
                    .catch(err => {
                        setUpdateInProgress(false);
                        toast.error('Contract Update Failed', options);
                    });
            }
            clearTimeout(timer)
        }, 100);
    }
    let frequencyIntervalTranslate = {
        "week": customerPortalSettingEntity?.weekText,
        "day": customerPortalSettingEntity?.dayText,
        "month": customerPortalSettingEntity?.monthText,
        "year": customerPortalSettingEntity?.yearText,
    };
    const getFrequencyTitle = (interval) => {
        return frequencyIntervalTranslate[interval.toLowerCase(interval)] ? frequencyIntervalTranslate[interval.toLowerCase()] : interval
    }
    const frequencyOptions = [
        { key: "DAY", title: customerPortalSettingEntity?.dayText },
        { key: "WEEK", title: customerPortalSettingEntity?.weekText },
        { key: "MONTH", title: customerPortalSettingEntity?.monthText },
        { key: "YEAR", title: customerPortalSettingEntity?.yearText },
    ];
    return (
        <div>
            <Fragment>
                <Row>
                    <Col md={8}>
                        <p>
                            <p style={{marginBottom: '0.55rem'}}>
                                <b>{customerPortalSettingEntity.deliveryFrequencyText || "Delivery Frequency"}: </b> {' '}
                                {subscriptionEntities?.deliveryPolicy.intervalCount + ' ' + (getFrequencyTitle(subscriptionEntities.deliveryPolicy.interval))}{' '}
                                {subscriptionEntities.status == 'ACTIVE' && customerPortalSettingEntity?.changeOrderFrequency && !editMode &&
                                    <FontAwesomeIcon icon={faPencilAlt} style={{ marginLeft: '10px', cursor: "pointer", bottom: "2px",
                                    position: "relative" }} onClick={() => {
                                        setSelectedIntervalCount(subscriptionEntities?.deliveryPolicy.intervalCount);
                                        setSelectedInterval(subscriptionEntities.deliveryPolicy.interval)
                                        setCountValid(true)
                                        setEditMode(true)
                                    }} />
                                }
                            </p>

                            <AnimatePresence initial={false}>
                            {
                              editMode && <motion.div
                              animate="open"
                              exit="collapsed"
                              initial="collapsed"
                              variants={{
                                open: { opacity: 1, height: "auto", scale: 1 },
                                collapsed: { opacity: 0, height: 0, scale: 0.95 }
                            }}
                            transition={{ duration: 0.5}}
                            >
                                <FormGroup>
                                    <Row>
                                        <Col md={5}>
                                            <Input
                                                value={selectedIntervalCount}
                                                invalid={!isCountValid}
                                                type="number"
                                                onInput={event => countChangeHandler(event.target.value)}
                                                onBlur={event => countBlurHandler(event.target.value)}
                                                onChange={event => countChangeHandler(event.target.value)}
                                                className="mt-1"
                                            />
                                        </Col>
                                        <Col md={7}>
                                            <Input
                                                type="select"
                                                className="mt-1"
                                                value={selectedInterval}
                                                onChange={event => {
                                                    setSelectedInterval(event.target.value);
                                                }}
                                            >
                                                {frequencyOptions.map(opt => {
                                                    return (
                                                        <option key={opt.key} value={opt?.key} >
                                                            {opt?.title}
                                                        </option>
                                                    );
                                                })}
                                            </Input>
                                        </Col>
                                    </Row>
                                </FormGroup>
                                <div className="mt-2 mb-3 d-flex justify-content-end">
                                    <Button className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => update()}>
                                        {!updateInProgress && customerPortalSettingEntity?.updateFreqBtnText}
                                        {updateInProgress && <div className="appstle_loadersmall" />}
                                    </Button>
                                    <Button className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center" onClick={() => setEditMode(!editMode)}>
                                        {customerPortalSettingEntity?.cancelFreqBtnTextV2}
                                    </Button>
                                </div>
                            </motion.div>
                          }
                        </AnimatePresence>
                        </p>
                    </Col>
                </Row>

            </Fragment>
        </div>
    )
}


const mapStateToProps = state => ({});

const mapDispatchToProps = {
    getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(EditDeliveryFrequency);
