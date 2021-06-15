import DS from 'ember-data';

export default DS.Model.extend({
  date: DS.attr('Date'),
  status: DS.attr('string'),
  matchday: DS.attr('number'),
  goalsHomeTeam: DS.attr('number'),
  goalsAwayTeam: DS.attr('number'),
  homeTeam: DS.belongsTo('team'),
  awayTeam: DS.belongsTo('team'),
});
