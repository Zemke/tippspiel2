import DS from 'ember-data';

export default DS.Model.extend({
  caption: DS.attr('string'),
  league: DS.attr('string'),
  year: DS.attr('string'),
  currentMatchday: DS.attr('number'),
  numberOfMatchdays: DS.attr('number'),
  numberOfTeams: DS.attr('number'),
  numberOfGames: DS.attr('number'),
  lastUpdated: DS.attr('Date'),
  current: DS.attr('boolean', {allowNull: true}),
});
