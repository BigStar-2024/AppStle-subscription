import React from 'react';
import { Field } from 'react-final-form';
import { Card, CardBody, CardHeader, Col, FormGroup, Input, Label, Row } from 'reactstrap';
import HTMLTextEditor from 'app/DemoPages/Forms/Components/WysiwygEditor/HTMLTextEditor';

const BuildABoxAdvancedSettings = () => {
  return (
    <Card>
      <CardHeader>Advanced Settings</CardHeader>
      <CardBody>
        <Row>
          <Col>
            <FormGroup>
              <Field id="bundleTopHtml" name="bundleTopHtml" type="textarea">
                {({ input }) => (
                  <>
                    <div style={{ alignItems: 'center' }}>
                      <Label for="bundleTopHtml">Build-A-Box Top HTML</Label>
                      <HTMLTextEditor defaultValue={input.value} addHandler={(value: string) => input.onChange(value)} />
                    </div>
                    <Input {...input} />
                  </>
                )}
              </Field>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormGroup>
              <Field id="bundleBottomHtml" name="bundleBottomHtml" type="textarea">
                {({ input }) => (
                  <>
                    <div style={{ alignItems: 'center' }}>
                      <Label for="bundleBottomHtml">Build-A-Box Bottom HTML</Label>
                      <HTMLTextEditor defaultValue={input.value} addHandler={(value: string) => input.onChange(value)} />
                    </div>
                    <Input {...input} />
                  </>
                )}
              </Field>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          <Col xs={6}>
            <FormGroup>
              <Label for="proceedToCheckoutButtonText">Proceed To Checkout Button Text</Label>
              <Field id="proceedToCheckoutButtonText" name="proceedToCheckoutButtonText">
                {({ input }) => <Input {...input}> </Input>}
              </Field>
            </FormGroup>
          </Col>
          <Col xs={6}>
            <FormGroup>
              <Label for="chooseProductsText">Choose Products Text</Label>
              <Field id="chooseProductsText" name="chooseProductsText">
                {({ input }) => <Input {...input} />}
              </Field>
            </FormGroup>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};

export default BuildABoxAdvancedSettings;
