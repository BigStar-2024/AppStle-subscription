import React from 'react';
import { FormText } from 'reactstrap';

export const required = value => (value ? undefined : <FormText color="danger">This field is required</FormText>);

export const mustBeNumber = value => (Number.isNaN(value) ? <FormText color="danger">Must be a number</FormText> : undefined);
export const minValue = min => value => (Number.isNaN(value) || value >= min ? undefined : `Should be greater than ${min}`);

export const mustBeEmail = mail =>
  /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(mail) ? undefined : <FormText color="danger">Must be a valid email</FormText>;
export const composeValidators = (...validators) => value => validators.reduce((error, validator) => error || validator(value), undefined);

export const mustBeDomain = url =>
  url === undefined || url === null || url === '' ? (
    undefined
  ) : /^(?!:\/\/)([a-zA-Z0-9-_]+\.)*[a-zA-Z0-9][a-zA-Z0-9-_]+\.[a-zA-Z]{2,11}?$/.test(url) ? (
    undefined
  ) : (
    <FormText color="danger">Please enter a valid domain</FormText>
  );
