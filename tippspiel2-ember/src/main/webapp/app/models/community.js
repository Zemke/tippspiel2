import DS from 'ember-data';
import attr from 'ember-data/attr';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  name: validator('presence', true),
  users: validator('length', { min: 2 }),
});

export default DS.Model.extend(Validations, {
  name: attr('string'),
  users: DS.hasMany('user'),
  created: attr('Date'),
  modified: attr('Date'),
});
