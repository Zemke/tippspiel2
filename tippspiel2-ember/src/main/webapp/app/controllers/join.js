import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  bettingGame: inject(),
  queryParams: {invitationToken: 'invitation-token'},
  invitationToken: null,
  actions: {
    submitInvitationToken() {
      this.get('store').query('betting-game', {'invitation-token': this.get('invitationToken')})
        .then(bettingGames => this.set('model.bettingGameToJoin', bettingGames.objectAt(0)));
    },
    joinBettingGame() {
      this.get('model.user.bettingGames').pushObject(this.get('model.bettingGameToJoin'));

      this.get('model.user').save()
        .then(() => {
          this.get('bettingGame').rememberCurrentBettingGame(this.get('model.bettingGameToJoin.id'));
          window.location.href = '/';
        })
        .catch(err => this.get('resHandler').handleError(err));
    }
  }
});
