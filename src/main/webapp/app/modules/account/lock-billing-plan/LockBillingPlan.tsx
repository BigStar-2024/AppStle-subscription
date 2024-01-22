import React, { useState, useEffect } from 'react';
import { Button, Row, Col, Label } from 'reactstrap';
import axios from 'axios';
import { AvFeedback, AvForm, AvGroup, AvField } from 'availity-reactstrap-validation';
import { toggleLock, getLockComments, setLockComments } from './lock.reducer';
import { toast } from 'react-toastify';

export default function LockBillingPlan() {
  const [fetching, setFetching] = useState(true);
  const [lockFetching, setLockFetching] = useState(true);
  const [planLoading, setPlanLoading] = useState(true);
  const [lockLoading, setLockLoading] = useState(true);
  const [lockCommentsLoading, setLockCommentsLoading] = useState(true);
  const [planName, setPlanName] = useState('');
  const [shopName, setShopName] = useState('');
  const [enableChangeBillingPlan, setEnableChangeBillingPlan] = useState(true);
  const canEnableChangeBillingPlan = enableChangeBillingPlan ? 'CAN' : "CAN'T";
  const [lockComments, setLockCommentsState] = useState('');

  useEffect(() => {
    axios.get('api/account').then(res => {
      setPlanName(res.data.planName);
      setShopName(res.data.login);
      setFetching(false);
      setPlanLoading(false);
    });
  }, []);

  useEffect(() => {
    if (shopName !== null) {
      axios.get(`api/shop-infos-by-shop/${shopName}`).then(res => {
        setEnableChangeBillingPlan(res.data.enableChangeBillingPlan);
        setFetching(false);
        setLockLoading(false);
      });
    }
  }, [shopName]);

  useEffect(() => {
    if (shopName !== null) {
      getLockComments().payload.then(res => {
        let comment;
        if (res.data.body == null || res.data == null) {
          comment = '';
        } else {
          comment = res.data.body;
          comment = comment.replace(/\\n/g, ')n');
        }
        setLockCommentsState(comment);
        setLockFetching(false);
        setLockCommentsLoading(false);
      });
    }
  }, []);

  const handleLockSubmit = () => {
    setLockLoading(true);
    toggleLock()
      .payload.then(() => {
        setEnableChangeBillingPlan(!enableChangeBillingPlan);
        console.log(`Toggled lock to: ${enableChangeBillingPlan}`);
        setLockLoading(false);
        toast.success(`Toggled lock to ${enableChangeBillingPlan} successfully`, { position: toast.POSITION.BOTTOM_RIGHT });
      })
      .catch(() => {
        toast.error('Toggled lock failed', { position: toast.POSITION.BOTTOM_RIGHT });
        setLockLoading(false);
      });
  };

  const handleCommentsSubmit = () => {
    setLockCommentsLoading(true);
    setLockComments(lockComments)
      .payload.then(() => {
        console.log(`Set lock comments to: ${lockComments}`);
        setLockCommentsLoading(false);
        toast.success('Set lock comments successfully', { position: toast.POSITION.BOTTOM_RIGHT });
      })
      .catch(() => {
        toast.error('Set lock comments failed', { position: toast.POSITION.BOTTOM_RIGHT });
        setLockCommentsLoading(false);
      });
  };

  return (
    <Row className="justify-content-center">
      <Col md="4">
        <h5>
          Current Billing Plan: {planLoading && 'Loading...'} {!planLoading && planName}
        </h5>

        <h5>Lock Billing Plan</h5>

        {fetching && <p>Loading...</p>}

        {!fetching && (
          <AvForm onValidSubmit={handleLockSubmit}>
            <AvGroup>
              <Label for="lock">Lock Billing Plan</Label>
              <p>Current status: {lockLoading ? 'Loading...' : canEnableChangeBillingPlan + ' change billing plan'}</p>
              <AvFeedback>This field is required.</AvFeedback>
            </AvGroup>
            {lockLoading ? (
              <Button color="primary" type="submit" disabled>
                Toggling...
              </Button>
            ) : (
              <Button color="primary" type="submit">
                Toggle Lock
              </Button>
            )}
          </AvForm>
        )}

        <br />

        <h5>Lock Comments</h5>

        {lockFetching && <p>Loading...</p>}

        {!lockFetching && (
          <AvForm onValidSubmit={handleCommentsSubmit}>
            <AvGroup>
              <Label for="lockComments"></Label>
              <AvField
                type="textarea"
                name="lockComments"
                id="lockComments"
                value={lockComments}
                onChange={e => {
                  setLockCommentsState(e.target.value);
                }}
              />
              <AvFeedback>This field is required.</AvFeedback>
            </AvGroup>
            {lockCommentsLoading ? (
              <Button color="primary" type="submit" disabled>
                Saving...
              </Button>
            ) : (
              <Button color="primary" type="submit">
                Save Comments
              </Button>
            )}
          </AvForm>
        )}
      </Col>
    </Row>
  );
}
