import React, {Fragment, useEffect, useState} from 'react';

//import './setting.scss';
import {
  Card,
  CardBody,
  Col,
  FormGroup,
  Input,
  Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  getEntity,
  getEntities,
  updateEntity
} from "app/entities/subscription-custom-css/subscription-custom-css.reducer";

const SubscriptionCustomCSS = ({subCustomCSSEntity, getEntities, getEntity, updateEntity, createEntity,customCSSFormSubmitRef, ...props}) => {
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

  const saveEntity = (values) => {
    // console.log("Dataaa" + cssContent);
    // console.log("Mydata"+ values.customCss)
    // debugger;
    updateEntity(values);
  };

  let submit;
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
          heading="Widget CSS"
          subheading="Advanced tool to align the look and feel of our widget with your store."
          icon="lnr-magic-wand icon-gradient bg-mean-fruit"
          actionTitle="Update"
          className="d-none"
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText="Updating"
          sticky={true}
        />
          <button type='submit' className="d-none" ref={customCSSFormSubmitRef} onClick={() => {
              submit();
          }}></button>
        <Form
          initialValues={subCustomCSSEntity}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            submit = Object.keys(errors).length === 0 && errors.constructor === Object ? handleSubmit : () => {
              if (Object.keys(errors).length) handleSubmit();
              setFormErrors(errors);
              setErrorsVisibilityToggle(!errorsVisibilityToggle);
            }
            return (
              <form onSubmit={handleSubmit} >
                <Card className="main-card p-3">
                  <CardBody>
                    <Row>
                      <Col xs={12} sm={12} md={12} lg={12} className="md-6">
                        <FormGroup>
                          <Field
                            render={({input, meta}) => (
                              <Fragment>
                                <Input {...input} rows="20" />
                              </Fragment>
                            )}
                            id="customCss"
                            className="form-control"
                            type="textarea"
                            name="customCss"
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </CardBody>
                </Card>
              </form>
            );
          }}
        />
      </ReactCSSTransitionGroup>
    </Fragment>
  );
}

const mapStateToProps = state => ({
  subCustomCSSEntity: state.subscriptionCustomCss.entity,
  loading: state.subscriptionCustomCss.loading,
  updating: state.subscriptionCustomCss.updating,
  updateSuccess: state.subscriptionCustomCss.updateSuccess,
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubscriptionCustomCSS);
