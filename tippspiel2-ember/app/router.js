import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('standings');
  this.route('games');
  this.route('sign-up');
  this.route('sign-in');
  this.route('me');

  this.route('communities', function() {
    this.route('new');
  });
});

export default Router;
