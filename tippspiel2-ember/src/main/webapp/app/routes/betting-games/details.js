import Route from '@ember/routing/route';

export default Route.extend({
  beforeModel() {
    this.store.findAll('user', { reload: true });
  },
  model(model) {
    return this.store
      .findRecord('betting-game', model['betting-game_id'])
      .then((bettingGame) => {
        bettingGame.set(
          'users',
          this.store.query('user', {
            'betting-game': bettingGame.get('id'),
          })
        );
        bettingGame.set('invitationLink', window.location.protocol + '//' + window.location.host + '/sign-up?invitation-token=' + bettingGame.get('invitationToken'));
        return bettingGame;
      });
  },
});
