import React, {Fragment, useEffect, useState} from 'react';

//import './setting.scss';
import {
  Card,
  CardBody,
  Col,
  FormGroup,
  Input,
  Row,
  CardHeader,
  Label
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  getEntity,
  getEntities,
  updateEntity
} from "app/entities/theme-settings/theme-settings.reducer";
import {
  updateEntity as updateShopInfoEntity
} from "app/entities/shop-info/shop-info.reducer";
import {
  updateEntity as widgetSettingsUpdateEntity
} from "app/entities/subscription-widget-settings/subscription-widget-settings.reducer";
import "./index.scss";

const WidgetTypes = ({subCustomCSSEntity, getEntities, getEntity, updateEntity, createEntity, widgetTypeFormSubmitRef, widgetTypeSubmit, shopInfoEntity, updateShopInfoEntity, widgetSettingsUpdateEntity, subWidgetSettingEntity, subWidgetSettingEntityFormState, ...props}) => {
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [cssContent, setCssContent] = useState(subCustomCSSEntity.customCss || "")
  useEffect(() => {
    // getEntities()c
    getEntity(0);
    // CKEditor.config.allowedContent =true
  }, [])

//  const onChange = (evt) =>{
//     var contentData = CKEDITOR.instances.editor1.document.getBody().getText();
//     setCssContent(contentData);
//  }

  const saveEntity = async (values) => {
    await updateShopInfoEntity({...shopInfoEntity});
    let widgetSettingEntity = {...subWidgetSettingEntityFormState, switchRadioButtonWidget: (values.widgetTemplateType !== "WIDGET_TYPE_2") ? true : false};
    if (widgetSettingEntity && widgetSettingEntity?.shopCustomizationData) {
      delete widgetSettingEntity.shopCustomizationData;
    }
    await widgetSettingsUpdateEntity(widgetSettingEntity)
    await updateEntity(values);
  };

  const widgetTypes = [{
    value: "WIDGET_TYPE_1",
    image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_11.08.34_AM_lll4oMORU.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657085961088"
  },
    {
      value: "WIDGET_TYPE_2",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_12.48.54_AM_m1rEUFf7R.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657048821287",

    }, {
      value: "WIDGET_TYPE_3",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_1.06.07_AM_KWdIdqgF2.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657049793940",

    }, {
      value: "WIDGET_TYPE_4",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-07-06_at_12.52.35_AM_9n_TIjH86.png?ik-sdk-version=javascript-1.4.3&updatedAt=1657048982249",

    },  {
      value: "WIDGET_TYPE_5",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2022-08-19_at_1.21.44_PM_5KzV79WDO.png?ik-sdk-version=javascript-1.4.3&updatedAt=1660895548539",

    },  {
      value: "WIDGET_TYPE_6",
      image: "https://ik.imagekit.io/mdclzmx6brh/Screenshot_2023-03-27_at_11.28.09_AM_i-HNVoBgW.png?updatedAt=1679930470065",

    }];

  // let widgetTypeSubmit;
  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading="Widget Types"
          subheading="Configure your widget Type"
          icon="lnr-magic-wand icon-gradient bg-mean-fruit"
          actionTitle="Update"
          enablePageTitleAction
          onActionClick={() => {
            widgetTypeSubmit();
          }}
          className="d-none"
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
        />
        <button type='submit' className="d-none" ref={widgetTypeFormSubmitRef} onClick={() => {
            widgetTypeSubmit();
          }}></button>
        <Form
          initialValues={subCustomCSSEntity}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            widgetTypeSubmit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
              if (Object.keys(errors).length) handleSubmit();
              setFormErrors(errors);
              setErrorsVisibilityToggle(!errorsVisibilityToggle);
            }
            return (
              <form onSubmit={handleSubmit}>
                <Row>
                  {widgetTypes?.map((type, index) => {
                    return <Col xs={12} md={6} className="mb-4">
                      <Field render={({input, meta}) => (
                        <>
                          <Input
                            {...input}
                            type="radio"
                            className="widgetType"
                            style={{display: "none"}}
                            onChange={(value) => input.onChange(type?.value)}
                            id={`widgetTemplateType${index}`}
                          />
                          <Label for={`widgetTemplateType${index}`} style={{width: "100%"}}>
                            <Card style={{width: "100%", cursor: "pointer"}}>
                              <CardHeader style={values?.widgetTemplateType === type?.value ? {
                                backgroundColor: "#3ac47d",
                                color: "#fff"
                              } : {
                                backgroundColor: "#ffa500",
                                color: "#fff"
                              }}
                              >
                                {values?.widgetTemplateType === type?.value ? "SELECTED" : "UNSELECTED"}
                              </CardHeader>
                              <CardBody style={{textAlign: "center", cursor: "pointer"}}>
                                <img src={type?.image}
                                     style={{width: "100%", height: "200px", objectFit: "contain", cursor: "pointer"}}
                                />
                              </CardBody>
                            </Card>
                          </Label>
                        </>
                      )}

                             id={`widgetTemplateType${index}`}
                             className="form-control"
                             type="radio"
                             name="widgetTemplateType"/>
                    </Col>
                  })}
                </Row>
              </form>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  subCustomCSSEntity: state.themeSettings.entity,
  loading: state.themeSettings.loading,
  updating: state.themeSettings.updating,
  updateSuccess: state.themeSettings.updateSuccess,
  shopInfoEntity: state.shopInfo.entity,
  subWidgetSettingEntity: state.subscriptionWidgetSettings.entity,
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity,
  updateShopInfoEntity,
  widgetSettingsUpdateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetTypes);
