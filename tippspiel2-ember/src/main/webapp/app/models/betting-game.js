import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  users: DS.hasMany('user'),
  invitationToken: DS.attr('string'),
  competition: DS.belongsTo('competition'),
  created: DS.attr('Date')
});
