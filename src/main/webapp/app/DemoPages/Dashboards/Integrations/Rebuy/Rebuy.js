import React, {useState} from 'react';
import {Field, Form} from 'react-final-form';
import {connect} from 'react-redux';
import {FormGroup, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, FormText} from 'reactstrap';
// import {getEntity, updateEntity} from 'app/entities/shop-info/shop-info.reducer';
import { updateEntity, createKlavioSampleTemplate} from '../../../../entities/shop-info/shop-info.reducer';
import MySaveButton from '../../Utilities/MySaveButton';
import {required} from "app/DemoPages/Dashboards/Utilities/FormValidators";
import Switch from 'react-switch';
import {updateEntity as updateThemeSettings} from 'app/entities/theme-settings/theme-settings.reducer';

function Rebuy(props) {

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
    await props.updateThemeSettings(props.themeSettingsEntity);
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
              <ModalHeader>Rebuy Integration</ModalHeader>
              <ModalBody>
                <h6>
                  Personalized Recommendations, Upsell, Cross Sell, Buy it Again
                </h6>
                <FormGroup>
                  <b className="d-block mb-3">Enable</b>
                  <Field
                    render={({input, meta}) => (
                      <div>
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
                      </div>
                    )}
                    name="reBuyEnabled"
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
    updating: state.shopInfo.updating,
    themeSettingsEntity: state.themeSettings.entity,
});

const mapDispatchToProps = {
  updateEntity,
  updateThemeSettings
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Rebuy);
