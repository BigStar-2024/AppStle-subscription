import React, { useState, useEffect } from 'react';
import {getEntity} from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Input, Card, Row, Col, Label, CardBody } from 'reactstrap';
import { Form, Field } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import axios from 'axios';
import { toast } from 'react-toastify';


import './loader.scss';
import { Mail, Repeat, Send } from "@mui/icons-material";
import { Toolbar, Tooltip } from "@mui/material";

const ResendEmails = ({getEntity, contractId, lineId, shop, currentVariant, type, ...props}) => {

  const [isResendInProgress, setIsResendInProgress] = useState(false);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };
  const toTitleCase = function(str) {
    return str.replace(
      /\w\S*/g,
      function(txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      }
    );
  }

  const resendEmail = (type) => {
    setIsResendInProgress(true);
    axios.get(`api/subscription-contract-details/resend-email?emailSettingType=${type}&contractId=${contractId}`)
    .then((res) => {
      setIsResendInProgress(false);
      toast.success(res?.data || "Email triggered successfully", options)
    })
    .catch((err) => {
      setIsResendInProgress(false);
      toast.error(err?.response?.data || "Failed to trigger email", options)
    })
  }



  return (
    <div color="primary" onClick={() => resendEmail(type)} className="ml-2" style={{cursor: "pointer"}} >
    {!isResendInProgress ? <Tooltip title="Resend Email" arrow placement="top">
    <Mail style={{color: "#545cd8"}} title="Resend Email" />

    </Tooltip> :
    <>
      <div className="appstle_loadersmall"/>
    </>}
  </div>

  );
};

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(ResendEmails);
