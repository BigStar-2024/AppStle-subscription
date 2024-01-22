import React, { Fragment, useState } from 'react'
import { connect, useSelector } from 'react-redux';
import moment from "moment";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Input, Label, FormGroup, FormFeedback, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencilAlt, faLock } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import Axios from 'axios';
import { motion, AnimatePresence } from "framer-motion"
import { getLocaleDate } from 'app/shared/util/customer-utils';
import swal from 'sweetalert'
import { DateTime } from "luxon";
const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
};

function EditBillingDate(props) {
    const { contractId, shopName, getCustomerPortalEntity, subscriptionContractFreezeStatus } = props;
    const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity)
    const subscriptionEntities = useSelector(state => state.subscription.entity);
    let [updateInProgress, setUpdateInProgress] = useState(false);
    const [editMode, setEditMode] = useState(false)
    const [selectedDate, setSelectedDate] = useState(null)
    const updateDate = () => {
        setUpdateInProgress(true);
        const requestUrl = `api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${selectedDate.toISOString()}&isExternal=true`;
        console.log("requestUrl", requestUrl);
        Axios
            .put(requestUrl)
            .then(res => {
                console.log(res);
                props.setNextOrderDateChangedResponce(res.data);
                getCustomerPortalEntity(contractId);
                setUpdateInProgress(false);
                toast.success('Contract Updated', options);
                setEditMode(!editMode)
            })
            .catch(err => {
                setUpdateInProgress(false);
                toast.error(err.response.data.message, options);
            });
    }
    const onDateChange = (date) => {
        setSelectedDate(date);
    }
    return (
        <div>
            <Fragment>
                <Row>
                    <Col md={8}>
                        <p>
                            <b>{customerPortalSettingEntity?.nextOrderText}:&nbsp;</b>
                            {DateTime.fromISO(subscriptionEntities?.nextBillingDate).toFormat(customerPortalSettingEntity?.dateFormat)}
                            {!subscriptionContractFreezeStatus ?
                                (subscriptionEntities.status == 'ACTIVE' && customerPortalSettingEntity?.changeNextOrderDate && !editMode &&
                                    <FontAwesomeIcon icon={faPencilAlt} style={{ marginLeft: '10px', cursor: "pointer", bottom: "2px",
                                        position: "relative" }} onClick={() => {
                                        setSelectedDate(moment(subscriptionEntities?.nextBillingDate).toDate());
                                        setEditMode(true)
                                    }} />)
                                :
                                <FontAwesomeIcon
                                    icon={faLock} style={{ marginLeft: '10px', cursor: "pointer", bottom: "2px",
                                    position: "relative" }} />
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
                            className="mt-1">
                                <FormGroup>
                                    <DatePicker
                                        className="form-control"
                                        selected={selectedDate}
                                        onChange={(date) => onDateChange(date)}
                                        dateFormat={customerPortalSettingEntity?.dateFormat || 'dd-MM-yyyy'}
                                        minDate={new Date()}
                                    />
                                </FormGroup>
                                <div className="mt-2 mb-3 d-flex justify-content-end">
                                    <Button className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => updateDate()}>
                                        {!updateInProgress && customerPortalSettingEntity?.updateChangeOrderBtnTextV2}
                                        {updateInProgress && <div className="appstle_loadersmall" />}
                                    </Button>
                                    <Button className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center" onClick={() => setEditMode(!editMode)}>
                                        {customerPortalSettingEntity?.cancelChangeOrderBtnTextV2}
                                    </Button>
                                </div>
                            </motion.div>
                        }
                        </AnimatePresence>
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

export default connect(mapStateToProps, mapDispatchToProps)(EditBillingDate);

