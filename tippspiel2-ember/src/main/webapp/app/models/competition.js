import DS from 'ember-data';

export default DS.Model.extend({
  caption: DS.attr('string'),
  league: DS.attr('string'),
  year: DS.attr('number'),
  currentMatchday: DS.attr('number'),
  lastUpdated: DS.attr('Date'),
  numberOfAvailableSeasons: DS.attr('number'),
  current: DS.attr('boolean'),
  championBetAllowed: DS.attr('boolean'),
  champion: DS.belongsTo('team'),
});

