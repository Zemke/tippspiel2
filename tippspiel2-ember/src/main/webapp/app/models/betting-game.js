import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  community: DS.belongsTo('community'),
  competition: DS.belongsTo('competition'),
  created: DS.attr('Date')
});
