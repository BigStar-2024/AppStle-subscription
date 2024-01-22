import React from 'react';
import {Button, Col, Input, Label, Modal, ModalBody, ModalHeader, Row} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import './loader.scss';


const UpdateCustomerInfoModel = (props) => {
    const {
        isUpdating,
        modaltitle,
        confirmBtnText,
        cancelBtnText,
        isUpdateCustomerInfoOpenFlag,
        toggleUpdateCustomerInfoModal,
        updateCustomerInfoMethod,
        initialCustomerInfo
    } = props;

    const onSubmit = values => {
        updateCustomerInfoMethod(values);
    };

    return (
        <div>
            <Modal isOpen={isUpdateCustomerInfoOpenFlag} size='lg' toggle={toggleUpdateCustomerInfoModal}>
                <ModalHeader toggle={toggleUpdateCustomerInfoModal}>{modaltitle}</ModalHeader>
                <ModalBody>
                    <div style={{padding: '22px'}}>
                        <Form
                            initialValues={initialCustomerInfo}
                            onSubmit={onSubmit}
                            render={({handleSubmit, form, submitting, pristine, values}) => (
                                <form onSubmit={handleSubmit}>
                                    <Row>
                                        <Col md={12}>
                                            <Label for="customerFirstName">First Name</Label>
                                            <Field
                                                render={({input, meta}) => (
                                                    <>
                                                        <Input {...input}
                                                               invalid={meta.error && meta.touched ? true : null}/>
                                                    </>
                                                )}
                                                id="customerFirstName"
                                                className="form-control"
                                                type="text"
                                                name="customerFirstName"
                                            />
                                        </Col>
                                        <Col md={12}>
                                            <Label for="customerLastName">Last Name</Label>
                                            <Field
                                                render={({input, meta}) => (
                                                    <>
                                                        <Input {...input}
                                                               invalid={meta.error && meta.touched ? true : null}/>
                                                    </>
                                                )}
                                                id="customerLastName"
                                                className="form-control"
                                                type="text"
                                                name="customerLastName"
                                            />
                                        </Col>
                                        <Col md={12}>
                                            <Label for="customerEmail">Email</Label>
                                            <Field
                                                render={({input, meta}) => (
                                                    <>
                                                        <Input {...input}
                                                               invalid={meta.error && meta.touched ? true : null}/>
                                                    </>
                                                )}
                                                id="customerEmail"
                                                className="form-control"
                                                type="text"
                                                name="customerEmail"
                                            />
                                        </Col>
                                    </Row>

                                    <div style={{marginTop: '21px', display: 'flex', justifyContent: 'flex-end'}}>
                                        <Button
                                            size="lg"
                                            className="btn-shadow-primary"
                                            color="danger"
                                            type="button"
                                            onClick={toggleUpdateCustomerInfoModal}
                                        >
                                            Cancel
                                        </Button>&nbsp;
                                        <Button
                                            style={{marginRight: '12px'}}
                                            size="lg"
                                            className="btn-shadow-primary"
                                            color="primary"
                                            type="submit"
                                        >
                                            {isUpdating ?
                                                <div className="d-flex align-items-center">
                                                    <div className="appstle_loadersmall"/>
                                                    <span className="ml-2 font-weight-light"> Please Wait</span>
                                                </div>
                                                : confirmBtnText}
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

export default UpdateCustomerInfoModel;
