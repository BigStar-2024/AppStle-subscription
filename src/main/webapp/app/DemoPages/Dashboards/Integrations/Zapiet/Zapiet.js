import React, {useState} from 'react';
import {Field, Form} from 'react-final-form';
import {connect} from 'react-redux';
import {FormGroup, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, FormText, Row, Col} from 'reactstrap';
import { updateEntity } from '../../../../entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';
import Switch from 'react-switch';
import EmbeddedExternalLink from '../../Utilities/EmbeddedExternalLink';

function Zapiet(props) {

  const {shopInfo} = props;

  const [isSaving, setIsSaving] = useState(false);

  let saveEntity = async values => {
    const {shopInfo} = props;
    const entity = {
      ...shopInfo,
      ...values
    };
    setIsSaving(true)
    await props.updateEntity(entity);
    setIsSaving(false)
    props.onClose();
  };

  return (
    <Form
      initialValues={shopInfo}
      onSubmit={saveEntity}
      render={({handleSubmit, pristine, form, submitting, values}) => {
        return (
          <form onSubmit={handleSubmit}>
            <Modal isOpen={props.isOpen} size="lg">
              <ModalHeader>Zapiet ‑ Pickup + Delivery Integration</ModalHeader>
              <ModalBody>
                <h6 className="mb-4">
                  Maximize Your Sales with Zapiet - The Ultimate Delivery, Pickup, and Shipping Scheduling App
                </h6>
                <Row className="mt-3">
                  <Col>
                    <FormGroup>
                      <Label for="zapietEnabled align-middle">
                        <b className="d-block">Enable</b>
                      </Label>
                      <Field
                        render={({input, meta}) => (
                          <span className='align-middle'>
                            <Switch
                              checked={input.value}
                              onChange={input.onChange}
                              onColor="#86d3ff"
                              onHandleColor="#2693e6"
                              handleDiameter={30}
                              uncheckedIcon={false}
                              checkedIcon={false}
                              boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                              activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                              height={20}
                              width={48}
                              className="ml-3"
                              id="material-switch"
                            />
                            {meta.touched && meta.error && <span>{meta.error}</span>}
                          </span>
                        )}
                        name="zapietEnabled"
                      />
                    </FormGroup>
                  
                  </Col>
                </Row>
                <div className="text-center mt-3">
                  <EmbeddedExternalLink 
                      className="btn btn-primary" 
                      href="https://apps.shopify.com/click-and-collect">Visit Now</EmbeddedExternalLink>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button className=" mr-2 btn btn-wide btn-shadow" onClick={props.onClose}>
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    saveEntity(values);
                  }}
                  updating={isSaving}
                />
              </ModalFooter>
            </Modal>
          </form>
        );
      }}
    />
  )
}

const mapStateToProps = state => ({
    shopInfo: state.shopInfo.entity,
    updating: state.shopInfo.updating
});

const mapDispatchToProps = {
  updateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Zapiet);
