import DS from 'ember-data';

export default DS.Model.extend({
  user: DS.belongsTo('string'),
  team: DS.belongsTo('string'),
  bettingGame: DS.belongsTo('string'),
  modified: DS.attr('Date'),
});
