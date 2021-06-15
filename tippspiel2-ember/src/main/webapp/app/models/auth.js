import DS from 'ember-data';
import { attr } from '@ember-data/model';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  password: validator('presence', true),
  email: [validator('presence', true), validator('format', { type: 'email' })],
});

export default DS.Model.extend(Validations, {
  email: attr('string'),
  password: attr('string'),
});
