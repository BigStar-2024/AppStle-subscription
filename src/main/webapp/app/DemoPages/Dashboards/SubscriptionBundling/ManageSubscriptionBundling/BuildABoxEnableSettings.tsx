import React, { useState } from 'react';
import { Field, useFormState } from 'react-final-form';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { Button, Card, CardBody, FormText, Input, InputGroup, InputGroupAddon, Label } from 'reactstrap';
import Switch from 'react-switch';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCopy, faExternalLinkAlt } from '@fortawesome/free-solid-svg-icons';

const BuildABoxEnableSettings = () => {
  const [copied, setCopied] = useState(false);

  const { values } = useFormState();

  return (
    <Card>
      <CardBody className="d-flex" style={{ gap: '60px' }}>
        <Field name={'subscriptionBundlingEnabled'} autoComplete="off" className="form-control">
          {({ input }) => (
            <div>
              <Label for="subscriptionBundlingEnabled" className="d-block">
                Enabled
              </Label>
              <label className="d-block">
                <Switch
                  id="subscriptionBundlingEnabled"
                  checked={input.value}
                  onChange={input.onChange}
                  onColor="#3ac47d"
                  checkedIcon={
                    <div className="text-center text-white" style={{ fontSize: '.75rem', lineHeight: '24px' }}>
                      ON
                    </div>
                  }
                  uncheckedIcon={
                    <div className="text-center text-white" style={{ fontSize: '.75rem', lineHeight: '24px' }}>
                      OFF
                    </div>
                  }
                  handleDiameter={22}
                  width={60}
                  height={24}
                  boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                  activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                />
              </label>
            </div>
          )}
        </Field>
        <div style={{ flex: 1 }}>
          <Label for="subscriptionBundleLink">
            <strong>Build-A-Box Link</strong>
          </Label>
          <InputGroup>
            <Input value={values?.subscriptionBundleLink} disabled />
            <InputGroupAddon addonType='append'>
              <CopyToClipboard text={values?.subscriptionBundleLink} onCopy={() => setCopied(true)}>
                <Button color="primary" disabled={!values?.subscriptionBundleLink}>
                  <FontAwesomeIcon icon={faCopy} />
                  {copied && 'Copied!'}
                </Button>
              </CopyToClipboard>
            </InputGroupAddon>
            <InputGroupAddon addonType='append'>
              <Button
                disabled={!values?.subscriptionBundleLink}
                color="primary"
                onClick={() => window.open(values?.subscriptionBundleLink, '_blank')}
              >
                <FontAwesomeIcon icon={faExternalLinkAlt} />
              </Button>
            </InputGroupAddon>
          </InputGroup>
          {!values?.subscriptionBundleLink && (
            <FormText>Build-A-Box needs to be enabled and saved in order for link to be generated</FormText>
          )}
        </div>
      </CardBody>
    </Card>
  );
};

export default BuildABoxEnableSettings;
