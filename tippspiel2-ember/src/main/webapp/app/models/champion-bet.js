import DS from 'ember-data';

export default DS.Model.extend({
  user: DS.belongsTo('user'),
  team: DS.belongsTo('team'),
  bettingGame: DS.belongsTo('bettingGame'),
  modified: DS.attr('Date'),
});
