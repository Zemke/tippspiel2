import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  squadMarketValue: DS.attr('string'),
  competition: DS.belongsTo('competition'),
});
