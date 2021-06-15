import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | betting-games/new', function (hooks) {
  setupTest(hooks);

  test('it exists', function (assert) {
    let route = this.owner.lookup('route:betting-games/new');
    assert.ok(route);
  });
});
