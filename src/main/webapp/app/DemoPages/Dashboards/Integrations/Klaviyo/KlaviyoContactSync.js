import React, { Component, useState, useEffect } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, FormText } from 'reactstrap';
// getKlaviyoList export does not exist
import { /*getKlaviyoList,*/ updateEntity } from '../../../../entities/shop-info/shop-info.reducer';
import { required } from '../../Utilities/FormValidators';
import MySaveButton from '../../Utilities/MySaveButton';
import { Bounce, toast } from 'react-toastify';
import axios from 'axios';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';

const KlaviyoContactSync = ({ ...props }) => {
  const [klavioListIds, setKlavioListIds] = useState([]);
  const [syncing, setSyncing] = useState(false);

  useEffect(
    function() {
      if (formValues?.klaviyoApiKey) {
        getLists(formValues?.klaviyoApiKey);
      }
    },
    [formValues]
  );

  const getLists = async apiKey => {
    axios
      .get(`/api/klaviyo/lists?klaviyoApiKey=${apiKey}`)
      .then(res => setKlavioListIds(res.data))
      .catch(err => console.log(err));
  };

  const syncExistingContacts = () => {
    setSyncing(true);
    axios
      .get(`/api/klaviyo/add-all-members-to-list`)
      .then(res => {
        setKlavioListIds(res.data);
        setSyncing(false);
        toast.success('Contacts successfully synced to Klaviyo List!');
      })
      .catch(err => {
        console.log(err);
        setSyncing(false);
        toast.error('Error while syncing Contacts');
      });
  };

  const saveEntity = values => {
    console.log('saving');
    const { shopInfo } = props;
    const entity = {
      ...shopInfo,
      ...values,
      klaviyoApiKey: values.klaviyoApiKey?.trim() || null,
    };

    props.updateEntity(entity);

    if (entity.klaviyoList != null && entity.klaviyoList !== '') {
      props.onClose();
    } else {
      //At this point, we should show a banner asking user to select list. Something like, "Please select a mailing list from the dropdown menu. If downdown is empty, then it means your API key is invalid."
    }
  };
  let formValues;

  return (
    <Form
      initialValues={props?.shopInfo}
      onSubmit={saveEntity}
      render={({ handleSubmit, pristine, form, submitting, values }) => {
        formValues = values;
        return (
          <form onSubmit={handleSubmit}>
            <Modal isOpen={props.isOpen} size="lg">
              <ModalHeader>Connect your Klaviyo Account</ModalHeader>
              <FeatureAccessCheck hasAnyAuthorities={'accessKlaviyoContactSync'} upgradeButtonText="Upgrade your plan">
                <ModalBody>
                  <h6>
                    Go to your Klaviyo{' '}
                    <a href="https://www.klaviyo.com/account#api-keys-tab" target="_blank">
                      account section
                    </a>{' '}
                    and create a new API key.
                  </h6>
                  <br />
                  <img
                    style={{
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: '#021a40',
                      boxShadow: '0 0 25px rgba(0, 0, 0, .2)',
                    }}
                    width={620}
                    src={require('./klaviyo_instruction.png')}
                  />
                  <br />

                  <FormGroup>
                    <Field
                      render={({ input, meta }) => (
                        <div>
                          <Label size="lg">Enter your API Key</Label>
                          <Input placeholder="Your API Key" {...input} />
                          {meta.touched && meta.error && <span>{meta.error}</span>}
                        </div>
                      )}
                      name="klaviyoApiKey"
                    />
                  </FormGroup>

                  <br />

                  {values.klaviyoApiKey ? (
                    <div>
                      <FormGroup>
                        <Label size="lg">Select your list</Label>
                        <Field
                          render={({ input, meta }) => {
                            return (
                              <div>
                                <Input {...input} className="mb-2" type="select">
                                  <option />
                                  {klavioListIds?.map(option => {
                                    return (
                                      <option key={option.list_id} value={option.list_id}>
                                        {option.list_name}
                                      </option>
                                    );
                                  })}
                                </Input>
                                {meta.touched && meta.error && <span>{meta.error}</span>}
                              </div>
                            );
                          }}
                          name="klaviyoListId"
                        />
                      </FormGroup>
                    </div>
                  ) : null}

                  {props?.shopInfo.klaviyoApiKey && props?.shopInfo.klaviyoListId ? (
                    <div className="mt-3 text-center">
                      <FormText>
                        <strong>Note:</strong> Please save your Klaviyo Private API key and Klaviyo List first and then sync contacts.
                      </FormText>
                      <FormGroup className="text-center mt-2">
                        <MySaveButton
                          className="btn-warning"
                          text="Sync Existing Contacts"
                          onClick={() => {
                            syncExistingContacts();
                          }}
                          updatingText="Syncing..."
                          updating={syncing}
                        />
                      </FormGroup>
                    </div>
                  ) : null}
                </ModalBody>
              </FeatureAccessCheck>
              <ModalFooter>
                <Button className="mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    saveEntity(values);
                  }}
                  updating={props.updating}
                >
                  Save
                </MySaveButton>
              </ModalFooter>
            </Modal>
          </form>
        );
      }}
    />
  );
};

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  updating: state.shopInfo.updating,
  klaviyoLists: state.shopInfo.klaviyoLists,
});

const mapDispatchToProps = {
  updateEntity,
  //getKlaviyoList,
};

export default connect(mapStateToProps, mapDispatchToProps)(KlaviyoContactSync);
