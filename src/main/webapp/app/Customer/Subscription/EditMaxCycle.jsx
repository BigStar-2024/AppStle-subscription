import React, { Fragment, useState } from 'react';
import { connect, useSelector } from 'react-redux';
import moment from 'moment';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Button, Input, Label, FormGroup, FormFeedback, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import Axios from 'axios';
import { motion, AnimatePresence } from 'framer-motion';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

function EditMaxCycle(props) {
  const { contractId, shopName, getCustomerPortalEntity } = props;
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  customerPortalSettingEntity.maxCycleText = 'Max Cycle';
  const subscriptionEntities = useSelector(state => state.subscription.entity);
  let [updateInProgress, setUpdateInProgress] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [isCountValid, setCountValid] = useState(true);
  const [maxCycleCount, setMaxCycleCount] = useState(null);
  const [selectedInterval, setSelectedInterval] = useState(null);
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
    } else {
      return undefined;
    }
  };
  const countChangeHandler = value => {
    setMaxCycleCount(value);
    if (!countInputBlurred) return;
    if (validateNumber(value)) {
      setCountValid(false);
    } else {
      setCountValid(true);
    }
  };
  const countBlurHandler = value => {
    setMaxCycleCount(value);
    setCountInputBlurred(true);
    if (validateNumber(value)) {
      setCountValid(false);
    } else {
      setCountValid(true);
    }
  };
  const update = () => {
    let timer = setTimeout(() => {
      if (maxCycleCount) {
        setUpdateInProgress(true);
        const requestUrl = `api/subscription-contracts-update-max-cycles?contractId=${contractId}&maxCycles=${maxCycleCount}&isExternal=true`;
        Axios.put(requestUrl)
          .then(
            res => {
              getCustomerPortalEntity(contractId);
              setUpdateInProgress(false);
              toast.success('Contract Updated', options);
              setEditMode(!editMode);
            },
            () => {
              setUpdateInProgress(false);
            }
          )
          .catch(err => {
            setUpdateInProgress(false);
            toast.error('Contract Update Failed', options);
          });
      }
      clearTimeout(timer);
    }, 100);
  };

  return (
    <div>
      <Fragment>
        <Row>
          <Col md={8}>
            <p>
              <p style={{ marginBottom: '0.55rem' }}>
                <b>{customerPortalSettingEntity?.maxCycleText}: </b> {subscriptionEntities?.billingPolicy?.maxCycles}{' '}
                {subscriptionEntities.status == 'ACTIVE' && customerPortalSettingEntity?.changeOrderFrequency && !editMode && (
                  <FontAwesomeIcon
                    icon={faPencilAlt}
                    style={{ marginLeft: '10px', cursor: 'pointer', bottom: '2px', position: 'relative' }}
                    onClick={() => {
                      setMaxCycleCount(subscriptionEntities?.billingPolicy?.maxCycles);
                      setCountValid(true);
                      setEditMode(true);
                    }}
                  />
                )}
              </p>

              <AnimatePresence initial={false}>
                {editMode && (
                  <motion.div
                    animate="open"
                    exit="collapsed"
                    initial="collapsed"
                    variants={{
                      open: { opacity: 1, height: 'auto', scale: 1 },
                      collapsed: { opacity: 0, height: 0, scale: 0.95 }
                    }}
                    transition={{ duration: 0.5 }}
                  >
                    <FormGroup>
                      <Row>
                        <Col md={5}>
                          <Input
                            value={maxCycleCount}
                            invalid={!isCountValid}
                            type="number"
                            onInput={event => countChangeHandler(event.target.value)}
                            onBlur={event => countBlurHandler(event.target.value)}
                            onChange={event => countChangeHandler(event.target.value)}
                            className="mt-1"
                          />
                        </Col>
                        <Col md={7}>
                          <div className="d-flex">
                            <Button className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => update()}>
                              {!updateInProgress && customerPortalSettingEntity?.updateFreqBtnText}
                              {updateInProgress && <div className="appstle_loadersmall" />}
                            </Button>
                            <Button
                              className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center"
                              onClick={() => setEditMode(!editMode)}
                            >
                              {customerPortalSettingEntity?.cancelFreqBtnTextV2}
                            </Button>
                          </div>
                        </Col>
                      </Row>
                    </FormGroup>
                  </motion.div>
                )}
              </AnimatePresence>
            </p>
          </Col>
        </Row>
      </Fragment>
    </div>
  );
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(EditMaxCycle);
