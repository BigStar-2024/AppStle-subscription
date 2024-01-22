import React from 'react';
import { Field } from 'react-final-form';
import Select from 'react-select';
import { Row, Col, FormGroup, FormText, Button, Label, Input } from 'reactstrap';
import { PlanChildProps } from '../../CreateSubscriptionGroupPlans';
import { FieldArray } from 'react-final-form-arrays';
import { defaultPlanCustomField } from 'app/shared/model/subscription-group.model';

const CustomFieldsSettings = ({ fields, name, index }: PlanChildProps) => {
  const planFields = fields.value[index];

  const days = [
    { value: '0', label: 'Sunday' },
    { value: '1', label: 'Monday' },
    { value: '2', label: 'Tuesday' },
    { value: '3', label: 'Wednesday' },
    { value: '4', label: 'Thursday' },
    { value: '5', label: 'Friday' },
    { value: '6', label: 'Saturday' },
  ];

  const insetStyling = { background: '#eee', padding: '18px', borderRadius: '10px', marginBottom: '1.5rem' };

  return (
    <FieldArray name={`${name}.formFieldJsonArray`} key={`${name}.formFieldJsonArray.${planFields.formFieldJsonArray}`}>
      {({ fields: customFields }) => (
        <>
          {customFields.map((customFieldName, customFieldIndex) => (
            <FormGroup key={customFieldIndex + 'formfieldcardbody' + index} style={insetStyling}>
              <Row>
                <Col>
                  <Field name={`${customFieldName}.type`} id={`${customFieldName}.type`} type="select" initialValue={'date'}>
                    {({ input }) => (
                      <FormGroup>
                        <Label for={`${customFieldName}.type`} className="font-weight-bold">
                          Field Type
                        </Label>
                        <Input {...input} type="select" >
                          <option value="date">Order Date</option>
                          <option value="text">Text</option>
                          <option value="select">Select</option>
                        </Input>
                      </FormGroup>
                    )}
                  </Field>
                </Col>
              </Row>
              <Row>
                <Col>
                  <Field name={`${customFieldName}.label`} id={`${customFieldName}.label`} type="text" initialValue={''}>
                    {({ input }) => (
                      <FormGroup>
                        <Label for={`${customFieldName}.label`} className="font-weight-bold">
                          Field Label
                        </Label>
                        <Input {...input} type="text" />
                      </FormGroup>
                    )}
                  </Field>
                </Col>
              </Row>

              {customFields.value[customFieldIndex]?.type === 'date' && (
                <>
                  <Row>
                    <Col>
                      <Field
                        name={`${customFieldName}.config`}
                        id={`${customFieldName}.config`}
                        type="text"
                        initialValue={'{"dateFormat": "dd-mm-yy"}'}
                      >
                        {({ input }) => (
                          <FormGroup>
                            <Label for={`${customFieldName}.config`} className="font-weight-bold">
                              Date Picker Config
                            </Label>
                            <Input {...input} type="text" />
                          </FormGroup>
                        )}
                      </Field>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                      <Field id={`${customFieldName}.enabledDays`} name={`${customFieldName}.enabledDays`}>
                        {({ input }) => (
                          <FormGroup>
                            <Label for={`${customFieldName}.enabledDays`} className="font-weight-bold">
                              Date Picker Enabled Days
                            </Label>
                            {/* Using initalValue causes render loop for some reason, so setting initial value in value attribute*/}
                            <Select {...input} value={input.value ? input.value : days} options={days} defaultValue={days} isMulti />
                          </FormGroup>
                        )}
                      </Field>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                      <Field
                        name={`${customFieldName}.nextOrderMinimumThreshold`}
                        id={`${customFieldName}.nextOrderMinimumThreshold`}
                        type="number"
                        initialValue={0}
                      >
                        {({ input }) => (
                          <FormGroup>
                            <Label for={`${customFieldName}.nextOrderMinimumThreshold`} className="font-weight-bold">
                              Minimum threshold day for next Order
                            </Label>
                            <Input {...input} type="number" />
                          </FormGroup>
                        )}
                      </Field>
                    </Col>
                  </Row>
                </>
              )}
              {customFields.value[customFieldIndex]?.type !== 'date' && (
                <Row>
                  <Col>
                    <Field name={`${customFieldName}.name`} id={`${customFieldName}.name`} type="text" initialValue={''} validate={(value) => {
                      if (!value) {
                        return 'Please provide a field name';
                      }
                    }}>
                      {({ input }) => (
                        <FormGroup>
                          <Label for={`${customFieldName}.name`} className="font-weight-bold">
                            Field Name<sup className="text-danger">*</sup>
                          </Label>
                          <Input {...input} type="text" invalid={!input.value} />
                          {!input.value && <div className="invalid-feedback d-block">Please provide a field name</div>}
                        </FormGroup>
                      )}
                    </Field>
                  </Col>
                </Row>
              )}
              {customFields.value[customFieldIndex]?.type === 'select' && (
                <Row>
                  <Col>
                    <Field name={`${customFieldName}.selectOptions`} id={`${customFieldName}.selectOptions`} type="text" initialValue={''}>
                      {({ input }) => (
                        <FormGroup>
                          <Label for={`${customFieldName}.selectOptions`} className="font-weight-bold">
                            Select Options<sup className="text-danger">*</sup>
                          </Label>
                          <Input {...input} type="text" invalid={!input.value} />
                          <FormText>JSON object key value pair. Example: {`{"firstname": "First Name", "lastName": "Last Name"}`}</FormText>
                          {!input.value && <div className="invalid-feedback d-block">Please provide select options</div>}
                        </FormGroup>
                      )}
                    </Field>
                  </Col>
                </Row>
              )}

              <Row>
                <Col className="d-flex" style={{ gap: '1rem' }}>
                  <Field name={`${customFieldName}.required`} id={`${customFieldName}.required`} type="checkbox" initialValue={false}>
                    {({ input }) => (
                      <FormGroup>
                        <Label>
                          <Input {...input} id={`${customFieldName}.required`} type="checkbox"  style={{marginLeft: "0"}} />
                          Is Required
                        </Label>
                      </FormGroup>
                    )}
                  </Field>
                  <Field name={`${customFieldName}.visible`} id={`${customFieldName}.visible`} type="checkbox" initialValue={true}>
                    {({ input }) => (
                      <FormGroup>
                        <Label>
                          <Input {...input} type="checkbox" id={`${customFieldName}.visible`} style={{marginLeft: "0"}} />
                          Is Visible
                        </Label>
                      </FormGroup>
                    )}
                  </Field>
                </Col>
              </Row>
            </FormGroup>
          ))}
          <div className="d-flex" style={{ gap: '.5rem' }}>
            <Button
              size="lg"
              color="primary"
              className="btn-shadow-primary"
              onClick={() => {
                customFields.push(defaultPlanCustomField);
              }}
            >
              Add field
            </Button>
            {customFields?.length > 1 && (
              <Button
                size="lg"
                color="danger"
                className="btn-shadow-danger"
                onClick={() => {
                  customFields.pop();
                }}
              >
                Remove last field
              </Button>
            )}
          </div>
        </>
      )}
    </FieldArray>
  );
};

export default CustomFieldsSettings;
