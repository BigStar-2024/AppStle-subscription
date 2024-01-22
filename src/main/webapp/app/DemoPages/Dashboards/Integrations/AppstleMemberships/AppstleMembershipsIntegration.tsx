import React, { Component, useEffect, useState } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { FormGroup, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { updateEntity } from 'app/entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';

export function AppstleMembershipsIntegration({ shopInfo, updating, updateEntity, ...props }) {
  const [apiKey, setApiKey] = useState(null);

  useEffect(() => {
    if (props.apiKey !== shopInfo.appstleMembershipsApiKey) {
      setApiKey(props.shopInfo.appstleMembershipsApiKey);
    }
  }, [props.apiKey, shopInfo.appstleMembershipsApiKey]);

  const saveEntity = values => {
    const entity = {
      ...shopInfo,
      ...values,
      appstleMembershipsApiKey: values.appstleMembershipsApiKey?.trim() || null
    };

    props.updateEntity(entity);

    if (entity.appstleMembershipsApiKey != null && entity.appstleMembershipsApiKey !== '') {
      props.onClose();
    }
  };

  return (
    <Form
      initialValues={shopInfo}
      onSubmit={saveEntity}
      render={({ handleSubmit, pristine, form, submitting, values }) => {
        return (
          <form onSubmit={handleSubmit}>
            <Modal isOpen={props.isOpen} size="lg">
              <ModalHeader>Connect your Appstle Memberships Account</ModalHeader>
              <ModalBody>
                <h6>Go to your Appstle Memberships app's API screen and find API key</h6>
                <br />
                <img
                  style={{
                    borderWidth: '1px',
                    borderStyle: 'solid',
                    borderColor: '#021a40',
                    boxShadow: '0 0 25px rgba(0, 0, 0, .2)'
                  }}
                  width={'100%'}
                  src={require('./membership_api_key.png')}
                />
                <br />
                <br />

                <FormGroup>
                  <Field
                    render={({ input, meta }) => (
                      <div>
                        <Label size="lg">API Key</Label>
                        <Input placeholder="API Key" {...input} />
                        {meta.touched && meta.error && <span>{meta.error}</span>}
                      </div>
                    )}
                    name="appstleMembershipsApiKey"
                  />
                </FormGroup>
              </ModalBody>
              <ModalFooter>
                <Button className=" mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    saveEntity(values);
                  }}
                  updating={props.updating}
                />
              </ModalFooter>
            </Modal>
          </form>
        );
      }}
    />
  );
}

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  updating: state.shopInfo.updating
});

const mapDispatchToProps = {
  updateEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMembershipsIntegration);
