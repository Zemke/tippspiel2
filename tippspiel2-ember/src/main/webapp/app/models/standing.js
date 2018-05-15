import DS from 'ember-data';

export default DS.Model.extend({
  points: DS.attr('number'),
  exactBets: DS.attr('number'),
  goalDifferenceBets: DS.attr('number'),
  winnerBets: DS.attr('number'),
  wrongBets: DS.attr('number'),
  missedBets: DS.attr('number'),
  user: DS.belongsTo('user'),
  bettingGame: DS.belongsTo('betting-game'),
});
