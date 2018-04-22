import Model from 'ember-data/model';
import attr from 'ember-data/attr';
import {buildValidations, validator} from 'ember-cp-validations';

const Validations = buildValidations({
  firstName: validator('presence', true),
  lastName: validator('presence', true),
  password: validator('presence', true),
  passwordConfirmation: [
    validator('presence', true),
    validator('confirmation', {on: 'password'})
  ],
  email: [
    validator('presence', true),
    validator('format', {type: 'email'})
  ]
});

export default Model.extend(Validations, {
  firstName: attr('string'),
  lastName: attr('string'),
  email: attr('string'),
  password: attr('string'),
  token: attr('string'),
});
