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

const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
};

function EditOrderNote(props) {
    const { contractId, shopName, getCustomerPortalEntity, orderNote, pageName, id, subscriptionContractFreezeStatus } = props;
    const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity)
    const subscriptionEntities = useSelector(state => state.subscription.entity)
    let [updateInProgress, setUpdateInProgress] = useState(false);
    const [editMode, setEditMode] = useState(false)
    const [orderNoteLocal, setOrderNoteLocal] = useState(orderNote)
    const updateOrderNote = () => {
      setUpdateInProgress(true);
      if (pageName == 'UPCOMIG_ORDER') {
        const requestUrl = `api/subscription-billing-attempts-update-order-note/${id}?orderNote=${orderNoteLocal}&isExternal=true`;
        console.log('requestUrl', requestUrl);
        Axios.put(requestUrl)
          .then(res => {
            setUpdateInProgress(false);
            toast.success('Upcoming Order Updated', options);
            setEditMode(!editMode);
          })
          .catch(err => {
            setUpdateInProgress(false);
            toast.error('Upcoming Order Failed', options);
          });
      } else {
        const requestUrl = `api/subscription-contracts-update-order-note/${contractId}?orderNote=${orderNoteLocal}&isExternal=true`;
        console.log('requestUrl', requestUrl);
        Axios.put(requestUrl)
          .then(res => {
            getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            toast.success('Contract Updated', options);
            setEditMode(!editMode);
          })
          .catch(err => {
            setUpdateInProgress(false);
            toast.error('Contract Update Failed', options);
          });
      }
    };

    const orderNoteChangeHandler = (note) => {
        setOrderNoteLocal(note);
    }
    return (
        <div>
            <Fragment>
                <Row>
                    <Col md={8}>
                        <p className="orderNoteTextWrapper">
                            <b>{customerPortalSettingEntity?.orderNoteText}:</b> {orderNoteLocal}
                            {!subscriptionContractFreezeStatus ?
                                (customerPortalSettingEntity?.orderNoteFlag && !editMode &&
                                <FontAwesomeIcon icon={faPencilAlt} style={{ marginLeft: '10px', cursor: "pointer", bottom: "2px",
                                    position: "relative" }} onClick={() => {
                                    setOrderNoteLocal(orderNoteLocal);
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
                                    <Input
                                    value={orderNoteLocal}
                                    type="text"
                                    onChange={event => orderNoteChangeHandler(event.target.value)}
                                    className="mt-1"
                                />
                                </FormGroup>
                                <div className="mt-2 mb-3 d-flex justify-content-end">
                                    <Button className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => updateOrderNote()}>
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

export default connect(mapStateToProps, mapDispatchToProps)(EditOrderNote);

