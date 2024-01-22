import React, { useState, ChangeEvent } from 'react';
import { Button, Input, Label, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { Field, Form } from 'react-final-form';
import { useSelector, useDispatch } from 'react-redux';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { updateEntity, getEntities as getEmailEntities } from 'app/entities/email-template-setting/email-template-setting.reducer';
import Checkbox from '@mui/material/Checkbox';
import { IRootState } from 'app/shared/reducers';
import { IEmailTemplateSetting } from 'app/shared/model/email-template-setting.model';

type EmailAdminNotificationProps = {
  item: IEmailTemplateSetting;
};

const EmailAdminNotification = ({ item }: EmailAdminNotificationProps) => {
  const updating = useSelector((state: IRootState) => state.emailTemplateSetting.updating);
  const dispatch = useDispatch() as (a: any) => Promise<any>;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [emailSaveProgress, setEmailSaveProgress] = useState(false);

  const saveEntity = async (values: Partial<IEmailTemplateSetting>) => {
    setEmailSaveProgress(true);
    await dispatch(updateEntity(values));
    await dispatch(getEmailEntities());
    setIsModalOpen(!isModalOpen);
    setEmailSaveProgress(false);
  };

  const checkEmailValidity = (emailId: string) => {
    if (
      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
        emailId
      )
    ) {
      return undefined;
    } else {
      return 'Please enter valid email.';
    }
  };

  const handleCheck = async (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.checked) {
      setIsModalOpen(!isModalOpen);
    } else {
      setEmailSaveProgress(true);
      await dispatch(updateEntity({ ...item, bccEmail: null }));
      await dispatch(getEmailEntities());
      setEmailSaveProgress(false);
    }
  };

  return (
    <Form
      initialValues={item}
      onSubmit={saveEntity}
      render={({ handleSubmit }) => {
        return (
          <form onSubmit={handleSubmit}>
            {!emailSaveProgress && <Checkbox id="adminNotification" checked={!!item?.bccEmail} onChange={handleCheck} />}
            {emailSaveProgress && <div className="appstle_loadersmall" />}
            <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
              <ModalHeader>Set Admin Notification Email</ModalHeader>
              <ModalBody>
                <Field
                  render={({ input, meta }) => (
                    <>
                      <Label>Email Address</Label>
                      <Input type="text" onChange={input.onChange} placeholder="Please enter email address here" />
                      {meta.error && meta.touched && <div className="invalid-feedback d-block">{meta.error}</div>}
                    </>
                  )}
                  name="bccEmail"
                  type="text"
                  validate={checkEmailValidity}
                />
                <br />
              </ModalBody>
              <ModalFooter>
                <Button
                  color="secondary"
                  onClick={() => {
                    setIsModalOpen(false);
                  }}
                >
                  Cancel
                </Button>
                <MySaveButton onClick={handleSubmit} text="Save" updating={updating} updatingText={'Saving'} />
              </ModalFooter>
            </Modal>
          </form>
        );
      }}
    />
  );
};

export default EmailAdminNotification;
