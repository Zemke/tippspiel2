import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  goalsHomeTeamBet: [
    validator('presence', true),
    validator('number', { allowString: true, integer: true, gte: 0, lt: 100 }),
  ],
  goalsAwayTeamBet: [
    validator('presence', true),
    validator('number', { allowString: true, integer: true, gte: 0, lt: 100 }),
  ],
});

export default DS.Model.extend(Validations, {
  goalsHomeTeamBet: DS.attr('number'),
  goalsAwayTeamBet: DS.attr('number'),
  fixture: DS.belongsTo('fixture'),
  user: DS.belongsTo('user'),
  bettingGame: DS.belongsTo('betting-game'),
  modified: DS.attr('Date'),
});
