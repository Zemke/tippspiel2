import Route from '@ember/routing/route';

export default Route.extend({
  beforeModel() {
    this.get('store').findAll('user', { reload: true });
  },
  model(model) {
    return this.get('store')
      .findRecord('betting-game', model['betting-game_id'])
      .then((bettingGame) => {
        bettingGame.set(
          'users',
          this.get('store').query('user', {
            'betting-game': bettingGame.get('id'),
          })
        );
        return bettingGame;
      });
  },
});
