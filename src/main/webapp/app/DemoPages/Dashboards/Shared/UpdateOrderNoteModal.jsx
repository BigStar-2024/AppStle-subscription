import React, {Fragment, useEffect, useState} from 'react';
import {
    Col,
    Input,
    Button,
    Label,
    Modal,
    ModalHeader,
    ModalBody,
    Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import './loader.scss';

const UpdateOrderNoteModal = (props) => {
    const {
      isUpdating,
      modaltitle,
      confirmBtnText,
      cancelBtnText,
      isUpdateOrderNoteOpenFlag,
      toggleUpdateOrderNoteModal,
      updateOrderNoteMethod,
      initialOrderNote,
      initialShippingAddress
    } = props;
  
    const onSubmit = values => {
        updateOrderNoteMethod(values);
    };
    return (
        <div>
          <Modal isOpen={isUpdateOrderNoteOpenFlag} size='lg' toggle={toggleUpdateOrderNoteModal}>
            <ModalHeader toggle={toggleUpdateOrderNoteModal}>{modaltitle}</ModalHeader>
            <ModalBody>
              <div style={{ padding: '22px' }}>
                <Form
                    initialValues={initialOrderNote}
                    onSubmit={onSubmit}
                    render={({ handleSubmit, form, submitting, pristine, values }) => (
                        <form onSubmit={handleSubmit}>
                            <Row>
                                <Col md={12}>
                                    <Label for="orderNote">Order Note</Label>
                                    <Field
                                        render={({input, meta}) => (
                                            <>
                                            <Input {...input}
                                                    invalid={meta.error && meta.touched ? true : null}/>
                                            </>
                                        )}
                                        id="orderNote"
                                        className="form-control"
                                        type="textarea"
                                        name="orderNote"
                                    />
                                </Col>
                            </Row>                           
                        <div style={{marginTop: '21px', display: 'flex', justifyContent: 'center'}}>
                            <Button
                            style={{marginRight: '12px'}}
                            size="lg" 
                            className="btn-shadow-primary" 
                            color="primary" 
                            type="submit" 
                            // disabled={submitting}
                            >
                            {isUpdating ? 
                                <div className="d-flex align-items-center">
                                    <div className="appstle_loadersmall" /> 
                                    <span className="ml-2 font-weight-light"> Please Wait</span>
                                </div> 
                                : confirmBtnText}
                            </Button>
                            <Button 
                                size="lg" 
                                className="btn-shadow-primary" 
                                color="danger"
                                type="button"
                                onClick={toggleUpdateOrderNoteModal}
                            >
                            cancel
                            </Button>
                        </div>
                        </form>
                    )}
                    />
              </div>
            </ModalBody>
          </Modal>
        </div>
      );
    }
    
    export default UpdateOrderNoteModal;