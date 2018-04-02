import Model from 'ember-data/model';
import attr from 'ember-data/attr';
import {buildValidations, validator} from 'ember-cp-validations';

// const Validations = buildValidations({
//   firstName: validator('presence', true),
//   lastName: validator('presence', true),
//   password: [
//     validator('presence', true),
//     validator('length', {
//       min: 4
//     })
//   ],
//   email: [
//     validator('presence', true),
//     validator('format', {type: 'email'})
//   ],
//   emailConfirmation: [
//     validator('presence', true),
//     validator('confirmation', {
//       on: 'email',
//       message: '{description} do not match.',
//       description: 'Email addresses'
//     })
//   ]
// });

export default Model.extend({
  firstName: attr('string'),
  lastName: attr('string'),
  email: attr('string'),
  password: attr('string'),
});
