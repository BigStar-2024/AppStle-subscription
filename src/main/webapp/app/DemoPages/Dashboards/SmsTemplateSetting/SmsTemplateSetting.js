// import React, { Fragment, useEffect, useRef, useState } from 'react';
// import Loader from 'react-loaders';
// //import './setting.scss';
// import { Card, CardBody, Col, FormGroup, Input, InputGroup, InputGroupAddon, Label, Row } from 'reactstrap';
// import { Field, Form, FormSpy } from 'react-final-form';
// import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
// import { connect } from 'react-redux';
// import PageTitle from 'app/Layout/AppMain/PageTitle';
// import { data } from './sms-settings-fields.js';
// import ColorPicker2 from '../Utilities/ColorPicker2.js';
// import { getEntity, updateEntity, createEntity, reset, fetchSMSPreviewText } from 'app/entities/sms-template-setting/sms-template-setting.reducer';
// import { ArrowBack, ArrowUpCircleOutline, Camera, LogoAppleAppstore } from 'react-ionicons';
// import moment from 'moment';
// import anchorme from "anchorme";
// import TextAreaWithCopoun from './TextAreaWithCopoun';

// const SmsTemplateSettings = ({
//                               automationEntity,
//                               getEntity,
//                               updateEntity,
//                               createEntity,
//                               loading,
//                               reset,
//                               history,
//                               fetchSMSPreviewText,
//                               smsPreviewText,
//                               ...props
//                             }) => {
//   const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
//   const [formErrors, setFormErrors] = useState(null);
//   const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
//   const [formState, setFormState] = useState({});
//   const [discountVariables, setDiscountVariables] = useState(['discount', 'shop_name', 'customer_name', 'checkout_url', 'shop_url', 'shipping_company', 'tracking_number']);

//   useEffect(() => {
//     if (isNew) {
//       reset();
//     } else {
//       getEntity(props.match.params.id);
//     }
//   }, []);

//   useEffect(() => {
//     fetchSMSPreviewText(formState);
//     if (formState?.smsSettingType === 'SUBSCRIPTION_CREATED') {
//       setDiscountVariables(['customer.display_name', 'shop.name']);

//     } else if (formState?.smsSettingType === 'TRANSACTION_FAILED') {
//       setDiscountVariables(['customer.display_name', 'shop.name']);

//     } else if (formState?.smsSettingType === 'UPCOMING_ORDER') {
//       setDiscountVariables(['customer.display_name', 'shop.name', 'nextOrderDate' ]);

//     } else if (formState?.smsSettingType === 'EXPIRING_CREDIT_CARD') {
//       setDiscountVariables(['customer.display_name', 'lastFourDigits ']);

//     }
//   }, [formState]);



//   const handleClose = () => {
//     history.push('/dashboards/sms-settings');
//   };


//   //   useEffect(() => setFormData(subscriptionGroupEntity), [props]);
//   useEffect(() => {
//     if (props.updateSuccess) handleClose();
//   }, [props.updateSuccess]);

//   const saveEntity = values => {
//     if (isNew) {
//       createEntity(values);
//     } else {
//       updateEntity(values);
//     }
//   };

//   const validateRequired = (value, displayName) => {
//     const type = typeof value
//     if (type === 'string' && value?.trim()) {
//       return undefined;
//     } else if (type === 'number') {
//       return undefined;
//     } else {
//       return `"${displayName}" field cannot be empty.`
//     }
//   }






//   let submit;
//   return (
//     <Fragment>
//       <ReactCSSTransitionGroup
//         component="div"
//         transitionName="TabsAnimation"
//         transitionAppear
//         transitionAppearTimeout={0}
//         transitionEnter={false}
//         transitionLeave={false}
//       >
//         <PageTitle
//           heading={isNew ? 'Create SMS Template' : 'Edit SMS Template'}
//           subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Customise SMS Template. Make it your own.</a>"
//           icon="lnr-pencil icon-gradient bg-mean-fruit"
//           actionTitle={isNew ? "Create" : "Update"}
//           enablePageTitleAction
//           onActionClick={() => {
//             submit();
//           }}
//           enableSecondaryPageTitleAction={true}
//           secondaryActionTitle="Cancel"
//           onSecondaryActionClick={() => {
//             history.push(`/dashboards/sms-settings`);
//           }}
//           formErrors={formErrors}
//           errorsVisibilityToggle={errorsVisibilityToggle}
//           onActionUpdating={props.updating}
//           updatingText={isNew ? "Saving" : "Updating"}
//           sticky={true}
//         />
//         {loading ? (
//           <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
//             <Loader type="line-scale" />
//           </div>
//         ) : (
//           <Form
//             initialValues={automationEntity}
//             onSubmit={saveEntity}
//             mutators={{
//               addVariablesToTextArea: ([name, value], state, utils) => {
//                 utils.changeValue(state, name, () => value);
//               }
//             }}
//             render={({ handleSubmit, form, submitting, pristine, values, errors }) => {
//               submit =
//                 Object.keys(errors).length === 0 && errors.constructor === Object
//                   ? handleSubmit
//                   : () => {
//                     if (Object.keys(errors).length) handleSubmit();
//                     setFormErrors(errors);
//                     setErrorsVisibilityToggle(!errorsVisibilityToggle);
//                   };
//               return (
//                 <form onSubmit={handleSubmit}>
//                   <div className="mb-3">
//                     <Card className="main-card p-2">
//                       <CardBody>
//                         <Row>
//                           <Col xs={12} sm={12} md={6} lg={7}>
//                             {Object.keys(data).map((emailTemplateElement, index) => {
//                               return (
//                                 <div key={data[emailTemplateElement].displayName}>
//                                   <h5
//                                     style={{
//                                       marginTop: '1.5rem',
//                                       marginBottom: '0.75rem',
//                                       color: '#545cd8'
//                                     }}
//                                   >
//                                     {data[emailTemplateElement].displayName}
//                                     <div>
//                                       <hr style={{ marginTop: '0.75rem' }} />
//                                     </div>
//                                   </h5>
//                                   <Row>
//                                     {Object.keys(data[emailTemplateElement])
//                                       .filter(element => typeof data[emailTemplateElement][element] === 'object')
//                                       .map(emailTemplateElementProperty => {
//                                         const item = data[emailTemplateElement][emailTemplateElementProperty];
//                                         return (
//                                           <Col
//                                             key={item?.id}
//                                             xs={12}
//                                             sm={12}
//                                             className="md-6"
//                                             style={{ display: item?.type === 'hidden' ? 'none' : '' }}
//                                           >
//                                             {item?.type === 'smsContent' ? <FormGroup className="form-group_margin-bottom">
//                                               <Label for="smsContent" style={{fontSize: '18px', marginBottom: '0'}}>Text message</Label>
//                                               <div style={{color: 'inherit'}}>
//                                                 <div
//                                                   className="automation_settings_help_text">Set the text of the message your customers will receive.</div>
//                                               </div>
//                                               <div
//                                                 style={{ color: 'inherit' }}
//                                                 dangerouslySetInnerHTML={{ __html: "An MMS contains 1600 characters, including emojis and special characters. A single message (SMS) contains up to 160 characters, but if you use emojis or special characters, its length will go down to 66 characters." }}
//                                               />
//                                               <Field
//                                                 render={({ input, meta }) => (
//                                                   <>
//                                                       <InputGroup>
//                                                         <Input
//                                                           {...input}
//                                                           placeholder={item?.placeholder}
//                                                           type="textarea"
//                                                           rows="8"
//                                                           invalid={meta.error && meta.touched ? true : null}
//                                                         />
//                                                       </InputGroup>
//                                                     {item?.type === 'hidden' && <Input {...input} type="hidden" />}
//                                                   </>
//                                                 )}
//                                                 validate={
//                                                   (value => {
//                                                       return validateRequired(value, "SMS Content");
//                                                   })
//                                                 }
//                                                 autoComplete="off"
//                                                 id="smsContent"
//                                                 className="form-control"
//                                                 name="smsContent"
//                                               />
//                                             </FormGroup> : <FormGroup className="form-group_margin-bottom">
//                                               <Label for={item?.id} style={{fontSize: '18px', marginBottom: '0'}}>{item?.displayName}</Label>
//                                               {item?.helpText && (
//                                                 <div
//                                                   style={{ color: 'inherit' }}
//                                                   dangerouslySetInnerHTML={{ __html: item?.helpText }}
//                                                 />
//                                               )}
//                                               <Field
//                                                 render={({ input, meta }) => (
//                                                   <>
//                                                     {item?.type === 'input' && (
//                                                       <InputGroup>
//                                                         <Input
//                                                           {...input}
//                                                           placeholder={item?.placeholder}
//                                                           type={item?.validation === 'NUMBER' ? 'number' : 'text'}
//                                                           invalid={meta.error && meta.touched ? true : null}
//                                                         />
//                                                         {item?.validation === 'NUMBER' && (
//                                                           <InputGroupAddon addonType='append'>px</InputGroupAddon>
//                                                         )}
//                                                       </InputGroup>
//                                                     )}

//                                                     {item?.type === 'select' && (
//                                                       <Input disabled {...input}>
//                                                         {item?.dropdownValues.map(option => (
//                                                           <option key={option.value} value={option.value}>
//                                                             {option.name}
//                                                           </option>
//                                                         ))}
//                                                       </Input>
//                                                     )}

//                                                     {item?.type === 'color' && (
//                                                       <ColorPicker2
//                                                         {...input}
//                                                         placeholder={item?.placeholder}
//                                                         onChange={value => input.onChange(value)}
//                                                       />
//                                                     )}

//                                                     {item?.type === 'textarea' && (
//                                                       <Input
//                                                         {...input}
//                                                         placeholder={item?.placeholder}
//                                                         type="textarea"
//                                                         rows="8"
//                                                         onChange={value => input.onChange(value)}
//                                                         invalid={meta.error && meta.touched ? true : null}
//                                                       />
//                                                     )}

//                                                     {item?.type === 'hidden' && <Input {...input} type="hidden" />}
//                                                   </>
//                                                 )}
//                                                 validate={
//                                                   (value => {
//                                                     if (item?.['validation'] === 'required') {
//                                                       return validateRequired(value, item?.displayName);
//                                                     } else {
//                                                       return undefined;
//                                                     }
//                                                   })
//                                                 }
//                                                 autoComplete="off"
//                                                 id={item?.id}
//                                                 className="form-control"
//                                                 type={item?.type}
//                                                 name={item?.id}
//                                               />

//                                             </FormGroup>}
//                                             {item?.id === 'delay' && values?.delay === 'ENABLED' && (
//                                               <>
//                                                 <FormGroup className="form-group_margin-bottom">
//                                                   <Label style={{ width: '100%', fontSize: '18px', marginBottom: '0' }} for="messageTime">
//                                                     Message Timing
//                                                   </Label>
//                                                   <div style={{ color: 'inherit' }}>
//                                                     <div class="automation_settings_help_text">Set the timing delay for your text messages.</div>
//                                                   </div>
//                                                   <Field
//                                                     render={({ input, meta }) => (
//                                                       <>
//                                                         <Input
//                                                           style={{ width: '49%', marginRight: '0', display: 'inline-block' }}
//                                                           {...input}
//                                                           placeholder="Timing delay"
//                                                           type="number"
//                                                         />
//                                                       </>
//                                                     )}
//                                                     autoComplete="off"
//                                                     id="messageTime"
//                                                     className="form-control"
//                                                     type="number"
//                                                     name="messageTime"
//                                                     validate={(value) => validateRequired(value, "Message Timing")}
//                                                   />
//                                                   <Field
//                                                     render={({ input, meta }) => (
//                                                       <>
//                                                         <Input {...input} style={{ width: '49%', marginLeft: '2%', display: 'inline-block' }}>
//                                                           <option value="MINUTE">Minute/s</option>

//                                                           <option value="HOUR">Hour/s</option>

//                                                           <option value="DAY">Day/s</option>
//                                                         </Input>
//                                                       </>
//                                                     )}
//                                                     autoComplete="off"
//                                                     id="messageTimeType"
//                                                     className="form-control"
//                                                     type="select"
//                                                     name="messageTimeType"
//                                                   />
//                                                 </FormGroup>
//                                               </>
//                                             )}
//                                           </Col>
//                                         );
//                                       })}
//                                   </Row>
//                                 </div>
//                               );
//                             })}
//                           </Col>
//                           <Col xs={12} sm={12} md={6} lg={5} style={{ alignItems: 'stretch', minHeight: '695px' }}>
//                             <>
//                               <h5
//                                 style={{
//                                   marginTop: '1.5rem',
//                                   marginBottom: '0.75rem',
//                                   color: '#545cd8'
//                                 }}
//                               >
//                                 Preview
//                                 <div>
//                                   <hr style={{ marginTop: '0.75rem' }} />
//                                 </div>
//                               </h5>
//                               <div
//                                 style={{
//                                   position: 'relative',
//                                   margin: '0 auto',
//                                   width: '100%',
//                                   maxWidth: '320px',
//                                   height: '100%',
//                                   maxHeight: '625px'
//                                 }}
//                               >
//                                 <img
//                                   src="https://ik.imagekit.io/mdclzmx6brh/iphone-12-pro-max_x8FeNFO-U.png"
//                                   style={{ width: '100%', position: 'absolute' }}
//                                 />
//                                 <div style={{ paddingTop: '23px', paddingLeft: '26px', paddingRight: '32px', paddingBottom: '40px', height: '100%', display: 'flex', flexDirection: 'column' }}>
//                                   <div
//                                     style={{
//                                       borderTopLeftRadius: '30px',
//                                       padding: '12px 0px',
//                                       textAlign: 'center',
//                                       backgroundColor: '#f2f2f7',
//                                       borderTopRightRadius: '30px'
//                                     }}
//                                   ></div>
//                                   <div className="iphone_contactWrapper">
//                                     <ArrowBack width="30px" height="30px" className="back-arrow" color="rgb(0, 122, 255)" />
//                                     <div className="iphone_contact_content">
//                                       <div className="iphone_contact">
//                                         <div className="avatar">
//                                           <div className="avatar_top"></div>
//                                           <div className="avatar_bottom"></div>
//                                         </div>
//                                       </div>
//                                       <div className="sender">Appstle
//                                         <ArrowBack width="12px" height="12px" color="rgb(142, 142, 147)" />
//                                       </div>
//                                     </div>
//                                   </div>
//                                   <div class="message-date">{moment().calendar()}</div>
//                                   {(smsPreviewText.length) > 0 && (
//                                     <div className="yours messages">
//                                       <div className="message last" dangerouslySetInnerHTML={{__html: (anchorme(smsPreviewText)).split('\n').join('<br/>')}} />
//                                     </div>)}
//                                   {(smsPreviewText.length <= 0) && (<div style={{padding: "20px 18px", fontSize: '12px', color: '#000'}}>"Text message" field is empty or there is a broken dynamic variables. Please get in touch with us, if you need any help.</div>)}                                 <div className="iphone-send-message">
//                                   <Camera width="35px" height="35px"  color="rgb(142, 142, 147)" />
//                                   <LogoAppleAppstore width="28px" height="28px"  color="rgb(142, 142, 147)"/>
//                                   <div className="iphone-send-message_input ml-1">
//                                     Text message
//                                     <div className="send-button">
//                                       <ArrowUpCircleOutline width="28px" height="28px"  color="#fff" />
//                                     </div>
//                                   </div>
//                                 </div>
//                                   <div className="iphone-footer">
//                                     <div className="navigation-pill"></div>
//                                   </div>
//                                 </div>
//                               </div>
//                             </>
//                           </Col>
//                         </Row>
//                       </CardBody>
//                     </Card>
//                   </div>
//                   <FormSpy subscription={{ values: true }}>
//                     {({ values }) => {
//                       setFormState(values);
//                       return <></>;
//                     }}
//                   </FormSpy>
//                 </form>
//               );
//             }}
//           />
//         )}
//       </ReactCSSTransitionGroup>
//     </Fragment>
//   );
// };

// const mapStateToProps = state => ({
//   automationEntity: state.smsTemplateSetting.entity,
//   loading: state.smsTemplateSetting.loading,
//   updating: state.smsTemplateSetting.updating,
//   updateSuccess: state.smsTemplateSetting.updateSuccess,
//   smsPreviewText: state.smsTemplateSetting.smsPreviewText
// });

// const mapDispatchToProps = {
//   getEntity,
//   updateEntity,
//   createEntity,
//   reset,
//   fetchSMSPreviewText
// };

// export default connect(mapStateToProps, mapDispatchToProps)(SmsTemplateSettings);
