import Route from '@ember/routing/route';
import { inject } from '@ember/service';
import RSVP from 'rsvp';

export default Route.extend({
  auth: inject(),
  model(model) {
    const hash = {
      user: this.get('auth.user').then((authenticatedUser) =>
        this.store.findRecord('user', authenticatedUser.id)
      ),
    };

    if (model != null && model.invitationToken != null) {
      hash.bettingGameToJoin = this.store
        .query('betting-game', { 'invitation-token': model.invitationToken })
        .then((bettingGames) => bettingGames.objectAt(0));
    }

    return RSVP.hash(hash);
  },
});
